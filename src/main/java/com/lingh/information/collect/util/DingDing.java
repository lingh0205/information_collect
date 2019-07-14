package com.lingh.information.collect.util;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import com.lingh.information.collect.dao.entity.Content;
import com.lingh.information.collect.dao.entity.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DingDing {

    private static final Logger LOGGER = LoggerFactory.getLogger(DingDing.class);
    private static final String prefix = "https://oapi.dingtalk.com/robot/send?access_token={}";

    public static boolean sendMsg(Resource resource, List<Content> contentList, List<String> tokens, List<String> contacts){
        LOGGER.info("Start to send dingding msg for resource {} at {}", resource.getUrl(), System.currentTimeMillis());
        try {
            Message message = generateMarkdownMsg(resource, contentList, contacts);
            tokens.forEach(token -> {
                HttpResponse response = HttpRequest.post(StrUtil.format(prefix, token))
                        .header("Content-Type", "application/json;charset=utf-8")
                        .body(JSONObject.toJSONString(message))
                        .execute();
                if (!response.isOk()){
                    LOGGER.error("Failed to send dingding msg : {}", JSONObject.toJSONString(message));
                }else {
                    LOGGER.info("Send DingDing Msg : {} Success.", JSONObject.toJSONString(message));
                }
                ThreadUtil.sleep(1, TimeUnit.SECONDS);
            });
            return Boolean.TRUE;
        }catch (Exception e){
            LOGGER.error("Failed to send dingding msg.", e);
        }
        return Boolean.FALSE;
    }

    private static Message generateMarkdownMsg(Resource resource, List<Content> contentList, List<String> contacts) {
        Message message = new Message();
        Markdown markdown = new Markdown();
        markdown.setTitle(resource.getDescription());
        StringBuffer buffer = new StringBuffer();
        contentList.forEach(content -> {
            buffer.append(StrUtil.format("#### {}\n\n", resource.getDescription()));
            buffer.append(StrUtil.format("[{}]({})\n\n", content.getTitle(), content.getLink()));
            if (checkNeedGen(content.getImageUrl())){
                buffer.append(StrUtil.format("![{}]({})\n\n", content.getTitle(), content.getImageUrl()));
            }
            if (checkNeedGen(content.getPubDate())) {
                buffer.append(StrUtil.format("{}\n\n", content.getPubDate()));
            }
        });
        markdown.setText(buffer.toString());
        message.setMarkdown(markdown);
        At at = new At();
        at.setAtMobiles(contacts);
        message.setAt(at);
        return message;
    }

    private static boolean checkNeedGen(String imageUrl) {
        return StrUtil.isNotBlank(imageUrl) && ! StrUtil.equalsIgnoreCase("--", imageUrl);
    }

    static class Message{

        private String msgtype;
        private Markdown markdown;
        private At at;

        public String getMsgtype() {
            return msgtype;
        }

        public void setMsgtype(String msgtype) {
            this.msgtype = msgtype;
        }

        public Markdown getMarkdown() {
            return markdown;
        }

        public void setMarkdown(Markdown markdown) {
            this.markdown = markdown;
            this.msgtype = markdown.getMsgType();
        }

        public At getAt() {
            return at;
        }

        public void setAt(At at) {
            this.at = at;
        }
    }

    static class At{
        private List<String> atMobiles = new ArrayList<>();
        private Boolean isAtAll = Boolean.FALSE;

        public List<String> getAtMobiles() {
            return atMobiles;
        }

        public void setAtMobiles(List<String> atMobiles) {
            this.atMobiles = atMobiles;
        }

        public void addAtPhone(String phone){
            this.atMobiles.add(phone);
        }

        public Boolean getAtAll() {
            return isAtAll;
        }

        public void setAtAll(Boolean atAll) {
            isAtAll = atAll;
        }
    }

    static class Markdown {
        private String msgType = "markdown";
        private String title;
        private String text;

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

}

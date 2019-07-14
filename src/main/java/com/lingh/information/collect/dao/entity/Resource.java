package com.lingh.information.collect.dao.entity;

import cn.hutool.core.util.StrUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Resource implements Serializable {
    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private String url;

    private String parser;

    private String description;

    private String template;

    private Byte status;

    private String link;

    private Long period;

    private Date lastExecuteTime;

    public String getTemplate() {
        return StrUtil.isNotBlank(this.template) ? this.template : "{\n" +
                "\t\"channel\": \"channel\",\n" +
                "\t\"item\": \"item\",\n" +
                "\t\"title\": \"title\",\n" +
                "\t\"description\": \"description\",\n" +
                "\t\"pubDate\": \"pubDate\",\n" +
                "\t\"link\": \"link\",\n" +
                "\t\"category\": \"category\",\n" +
                "}";
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public Date getLastExecuteTime() {
        return lastExecuteTime;
    }

    public void setLastExecuteTime(Date lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
    }

    List<Content> contentList = new ArrayList<>();

    public List<Content> getContentList() {
        return contentList;
    }

    public void setContentList(List<Content> contentList) {
        this.contentList = contentList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getParser() {
        return parser;
    }

    public void setParser(String parser) {
        this.parser = parser == null ? null : parser.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "url='" + url + '\'' +
                ", parser='" + parser + '\'' +
                '}';
    }

    public void addContent(Content content) {
        this.getContentList().add(content);
    }

    public static class Item{

        private String tag;
        private String attr;

        public boolean useAttr(){
            return StrUtil.isNotBlank(attr);
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getAttr() {
            return attr;
        }

        public void setAttr(String attr) {
            this.attr = attr;
        }
    }

    public static class Template{

        private Item channel;
        private Item item;
        private Item title;
        private Item link;
        private Item description;
        private Item pubDate;
        private Item imageUrl;
        private Item category;

        public String getChannel() {
            return channel.getTag();
        }

        public void setChannel(Item channel) {
            this.channel = channel;
        }

        public String getCategory() {
            return category.getTag();
        }

        public void setCategory(Item category) {
            this.category = category;
        }

        public String getItem() {
            return item.getTag();
        }

        public void setItem(Item item) {
            this.item = item;
        }

        public String getTitle() {
            return title.getTag();
        }

        public void setTitle(Item title) {
            this.title = title;
        }

        public String getLink() {
            return link.getTag();
        }

        public void setLink(Item link) {
            this.link = link;
        }

        public String getDescription() {
            return description.getTag();
        }

        public void setDescription(Item description) {
            this.description = description;
        }

        public String getPubDate() {
            return pubDate.getTag();
        }

        public void setPubDate(Item pubDate) {
            this.pubDate = pubDate;
        }

        public String getImageUrl() {
            return imageUrl.getTag();
        }

        public void setImageUrl(Item imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

}
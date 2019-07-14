package com.lingh.information.collect.parser.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.lingh.information.collect.commons.Constant;
import com.lingh.information.collect.dao.entity.Content;
import com.lingh.information.collect.dao.entity.Resource;
import com.lingh.information.collect.parser.AbstractParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HtmlParser extends AbstractParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlParser.class);

    public HtmlParser(){
        this(Constant.HTML);
    }

    public HtmlParser(String parserName) {
        super(parserName);
    }

    @Override
    protected List<Content> parser(final Document document, final Resource resource, final Resource.Template template, final UUID traceId) {
        JXDocument doc = JXDocument.create(document);
        List<JXNode> nodes = doc.selN(template.getItem());
        List<Content> contentList = new ArrayList<>();
        nodes.forEach(node -> {
            try {
                Content content = new Content();
                content.setResourceId(resource.getId());

                set(traceId, content, node, template, resource, "title");
                set(traceId, content, node, template, resource, "description");
                set(traceId, content, node, template, resource, "pubDate");
                set(traceId, content, node, template, resource, "link");
                set(traceId, content, node, template, resource, "imageUrl");

                checkContent(content);
                content.setStatus((byte) 1);
                contentList.add(content);
            } catch (IllegalStateException e){
                ;
            }catch (Exception e) {
                LOGGER.error("[{}]Failed to parser Resource Item {}.", traceId, node.asElement().html(), e);
            }
        });
        return contentList;
    }

    protected Boolean set(UUID traceId, Content content, JXNode node, Resource.Template template, Resource resource, String fieldName) throws IllegalAccessException {
        Resource.Item item = (Resource.Item) ReflectUtil.getFieldValue(template, fieldName);
        String xPath = item.getTag();
        if (StrUtil.equalsIgnoreCase("--", xPath)){
            setAndLogField(traceId, content, fieldName, xPath);
            return Boolean.TRUE;
        }
        JXNode jxn = node.selOne(xPath);
        if (ObjectUtil.isNull(jxn)){
            return Boolean.FALSE;
        }

        String value = "";
        if (item.useAttr()){
            value = getAttr(node, template, item.getAttr(), fieldName);
            if (checkHttp(value)){
                value = resource.getLink() + value;
            }
        }else{
            value = jxn.asElement().text();
        }
        value = parseWithPattern(item, value);
        return setAndLogField(traceId, content, fieldName, value);
    }

    protected String getAttr(JXNode node, Resource.Template template, String attrName, String fieldName){
        Resource.Item item = (Resource.Item) ReflectUtil.getFieldValue(template, fieldName);
        String xPath = item.getTag();
        if (StrUtil.equalsIgnoreCase("--", xPath)) {
            return "--";
        }
        JXNode jxn = node.selOne(xPath);
        if (ObjectUtil.isNull(jxn)){
            return "";
        }
        Element elem = node.selOne(xPath).asElement();
        return elem.attr(attrName);
    }

}

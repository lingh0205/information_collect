package com.lingh.information.collect.parser.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.lingh.information.collect.commons.Constant;
import com.lingh.information.collect.dao.entity.Content;
import com.lingh.information.collect.dao.entity.Resource;
import com.lingh.information.collect.parser.AbstractParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class XmlParser extends AbstractParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlParser.class);

    protected static final Pattern trimPattern = Pattern.compile("<[^<>]*>");
    protected static final Pattern imagePattern = Pattern.compile("img.*src=\"(.*?)\"");

    public XmlParser(){
        this(Constant.XML);
    }

    public XmlParser(String parserName) {
        super(parserName);
    }

    @Override
    protected List<Content> parser(final Document document, final Resource resource, final Resource.Template template, final UUID traceId) {
        Elements channels = document.getElementsByTag(template.getChannel());
        List<Content> contentList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(channels)){
            channels.forEach(chan -> {
                // parse channel items
                Elements items = chan.getElementsByTag(template.getItem());
                items.forEach(item -> {
                    try {
                        Content content = new Content();
                        content.setResourceId(resource.getId());
                        logAndSet(traceId, content, item, template, "title");
                        logAndSet(traceId, content, item, template, "description");
                        String description = content.getDescription();
                        Matcher matcher = imagePattern.matcher(item.html());
                        if (matcher.find()) {
                            String imageUrl = matcher.group(1);
                            content.setImageUrl(imageUrl);
                        }
                        Matcher triMatcher = trimPattern.matcher(description);
                        if (triMatcher.find()) {
                            description = triMatcher.replaceAll("");
                            if (StrUtil.length(description) > 200) {
                                description = description.substring(0, 200);
                            }
                            content.setDescription(description);
                        }

                        logAndSet(traceId, content, item, template, "pubDate");
                        logAndSet(traceId, content, item, template, "link");
                        logAndSet(traceId, content, item, template, "category");

                        checkContent(content);
                        content.setStatus((byte) 1);
                        contentList.add(content);
                    } catch (Exception e) {
                        LOGGER.error("[{}]Failed to parser Resource Item {}.", traceId, item.html(), e);
                    }
                });
            });
        }else {
            LOGGER.error("[{}]Parser xml found error. Cause by found an Empty channel list.", traceId);
        }
        return contentList;
    }

    protected <T> boolean logAndSet(UUID traceId, T content, Element elem, Resource.Template template, String fieldName) throws Exception {
        Resource.Item item = (Resource.Item) ReflectUtil.getFieldValue(template, fieldName);
        String tagName = item.getTag();
        if (StrUtil.equalsIgnoreCase("--", tagName)){
            setAndLogField(traceId, content, fieldName, tagName);
            return Boolean.TRUE;
        }
        String value = findTargetValue(elem, template, fieldName, tagName);
        Boolean x = setAndLogField(traceId, content, fieldName, value);
        if (x != null) return x;
        return false;
    }

    protected String findTargetValue(Element chan, Resource.Template template, String fieldName, String tagName) {
        List<String> list = findTargetValue(chan, template, fieldName, tagName, false);
        if (CollectionUtil.isEmpty(list)){
            return "";
        }
        return list.get(0);
    }

    protected List<String> findTargetValue(Element elem, Resource.Template template, String fieldName, String tagName, boolean isAll) {
        Resource.Item item = (Resource.Item) ReflectUtil.getFieldValue(template, fieldName);
        List<String> list = new ArrayList<>();
        Elements desElemList = elem.getElementsByTag(tagName);
        if (CollectionUtil.isNotEmpty(desElemList)){
            for (Element element : desElemList) {
                if (item.useAttr()){
                    list.add(element.attr(item.getAttr()));
                }else {
                    list.add(element.text());
                }
                if (!isAll){
                    break;
                }
            }
        }
        return list;
    }

}

package com.lingh.information.collect.parser;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.lingh.information.collect.commons.Constant;
import com.lingh.information.collect.dao.entity.Content;
import com.lingh.information.collect.dao.entity.Resource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractParser implements Parser {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractParser.class);

    private static final Pattern httpPattern = Pattern.compile("http[|s]://");

    private String parserName;

    public AbstractParser(String parserName) {
        this.parserName = parserName;
    }

    @PostConstruct
    private void init(){
        ParserFactory.register(this.parserName, this);
    }

    @Override
    public List<Content> parser(UUID traceId, Resource resource) throws IOException {
        String url = resource.getUrl();
        Document document = Jsoup.connect(url)
                .userAgent(Constant.USER_AGENT)
                .get();
        Resource.Template template = JSONObject.parseObject(resource.getTemplate(), Resource.Template.class);
        return parser(document, resource, template, traceId);
    }

    protected abstract List<Content> parser(final Document document, final Resource resource, final Resource.Template template, final UUID traceId);

    protected void checkContent(Content content) {
        Preconditions.checkState(StrUtil.isNotEmpty(content.getLink()), "内容项URL不能为空。");
        Preconditions.checkState(StrUtil.isNotEmpty(content.getTitle()), "内容项标题不能为空。");
    }

    protected <T> Boolean setAndLogField(UUID traceId, T target, String fieldName, String value) throws IllegalAccessException {
        if (StrUtil.isNotEmpty(value)) {
            Class<?> targetClass = target.getClass();
            LOGGER.info("[{}]Found {} : {} : {}.", traceId, targetClass.getSimpleName(), fieldName, value);
            Field field = ReflectUtil.getField(targetClass, fieldName);
            if (ObjectUtil.isNull(field)){
                LOGGER.warn("Failed to found Field : {}.{}.", targetClass.getSimpleName(), fieldName);
                return false;
            }
            set(target, field, value);
            return true;
        }
        return false;
    }

    protected boolean checkHttp(String url) {
        if (StrUtil.isEmpty(url) || StrUtil.equalsIgnoreCase("--", url)) {
            return false;
        }
        Matcher matcher = httpPattern.matcher(url);
        return ! matcher.find();
    }

    protected void set(Object targetBean, Field field, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(targetBean, value);
    }

    public String getParserName() {
        return parserName;
    }

    public void setParserName(String parserName) {
        this.parserName = parserName;
    }

}

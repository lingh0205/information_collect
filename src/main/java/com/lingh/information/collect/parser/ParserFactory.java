package com.lingh.information.collect.parser;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ParserFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParserFactory.class);

    private final static Map<String, Parser> registerCenter = new HashMap<>();

    public static Parser getParser(String parserName){
        Parser parser = registerCenter.get(parserName);
        if (ObjectUtil.isNull(parser)){
        }
        return registerCenter.get(parserName);
    }

    public static void register(String parserName, Parser parser){
        Preconditions.checkState(StrUtil.isNotEmpty(parserName), "解析器名称不能为空。");
        Preconditions.checkState(ObjectUtil.isNotNull(parser), "解析器不能为空。");
        LOGGER.info("Register Parser : {} at {}", parserName, System.currentTimeMillis());
        synchronized (registerCenter){
            registerCenter.put(parserName, parser);
        }
    }

}

package com.lingh.information.collect.parser;

import com.lingh.information.collect.dao.entity.Content;
import com.lingh.information.collect.dao.entity.Resource;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface Parser {

    String getParserName();

    List<Content> parser(UUID traceId, Resource resource) throws IOException;

}

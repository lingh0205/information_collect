package com.lingh.information.collect;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages = {"com.lingh.information.collect.dao.mapping"})
@EnableScheduling
public class InformationCollectApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(InformationCollectApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(InformationCollectApplication.class, args);
        }catch (Exception e){
            LOGGER.error("Failed to start application.", e);
        }
    }

}

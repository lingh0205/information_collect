package com.lingh.information.collect;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages = {"com.lingh.information.collect.dao.mapping"})
@EnableScheduling
public class InformationCollectApplication {

    public static void main(String[] args) {
        SpringApplication.run(InformationCollectApplication.class, args);
    }

}

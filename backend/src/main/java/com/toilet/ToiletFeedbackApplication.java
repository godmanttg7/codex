package com.toilet;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.toilet.mapper")
public class ToiletFeedbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToiletFeedbackApplication.class, args);
    }
}

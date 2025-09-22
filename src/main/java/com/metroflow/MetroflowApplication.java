package com.metroflow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.metroflow.mapper")
public class MetroflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetroflowApplication.class, args);
    }

}

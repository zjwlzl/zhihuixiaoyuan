package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.example.mapper")
public class SpringbootZhxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootZhxyApplication.class, args);
    }

}

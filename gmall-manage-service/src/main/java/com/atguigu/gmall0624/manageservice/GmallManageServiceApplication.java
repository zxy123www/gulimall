package com.atguigu.gmall0624.manageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;


@MapperScan("com.atguigu.gmall0624.manageservice.mapper")
@EnableTransactionManagement
@ComponentScan(basePackages = "com.atguigu.gmall0624")
@SpringBootApplication
public class GmallManageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallManageServiceApplication.class, args);
    }

}

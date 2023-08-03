package com.kyou.blog;


import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



/**
 * @author CC
 * time 2023-07-11
 * description
 */
@SpringBootApplication(scanBasePackages = {"com.kyou.blog.dataService","com.kyou.blog.common.meta"})
@EnableDubbo
@Slf4j
public class DataSerivceApplication {
    public static void main(String[] args) throws IllegalAccessException {
        SpringApplication.run(DataSerivceApplication.class,args);
        log.info("数据服务者启动成功");

    }



}

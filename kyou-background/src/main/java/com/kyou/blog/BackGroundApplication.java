package com.kyou.blog;

import com.kyou.blog.background.properties.CCProperties;
import com.kyou.blog.background.web.FreemarkerUtil;
import com.kyou.blog.background.webUtil.ThreadService;
import com.kyou.blog.common.constant.RedisConstant;
import com.kyou.blog.dataService.service.UserService;
import com.kyou.blog.model.entity.Menu;
import com.kyou.blog.model.vo.MenuVo;
import com.kyou.blog.model.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.File;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CC
 * time 2023-07-11
 * description
 */
@SpringBootApplication(
        scanBasePackages = {
                "com.kyou.blog.common",
                "com.kyou.blog.background",
                "com.kyou.blog.dataService",
        }
)
@Slf4j
@EnableDubbo
@EnableAspectJAutoProxy
//@ServletComponentScan
public class BackGroundApplication implements CommandLineRunner {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;



    public static void main(String[] args) throws Exception {
       SpringApplication.run(BackGroundApplication.class, args);
        log.info("kyou个人博客后台管理系统启动成功");

    }

    @Override
    public void run(String... args) throws Exception {

    }

}

package com.kyou.blog.background.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author CC
 * time 2023-07-07
 * description 自定义配置类
 */
@Configuration
@ConfigurationProperties(prefix = "cc.config")
@Data
@Slf4j
public class CCProperties {
    @PostConstruct
    public void init(){
        log.info("加载了自定义配置文件");
    }
    /**
     * 拦截器无视的路径
     */
    private List<String> ignoreUrls;
    /**
     * token过期时间（分钟）
     */
    private long tokenExpire;
    /**
     * token密钥
     */
    private String tokenSecret;
    /**
     * 记住我：延长token时间：2天
     */
    private long rememberMe;
    /**
     * freemarker的文章文件输出路径
     */
    private String freemarkerOutput;
    /**
     * freemarker的用户主页文件输出路径
     */
    private String freemarkerUserHomeOutPut;



}

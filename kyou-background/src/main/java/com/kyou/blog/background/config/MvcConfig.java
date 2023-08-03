package com.kyou.blog.background.config;

import com.kyou.blog.background.interceptor.TokenFlushInterceptor;
import com.kyou.blog.background.properties.CCProperties;
import com.kyou.blog.common.meta.JacksonObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;


import java.util.List;

/**
 * @author CC
 * time 2023-05-26
 * description
 */
@Configuration
//将所有访问路径转换为请求路径
//@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    private CCProperties ccProperties;
    @Autowired
    private TokenFlushInterceptor tokenFlushInterceptor;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
//       registry.addMapping("/**").
//               allowedHeaders("*") .
//               allowedMethods("*").
//               allowCredentials(true).
//               allowedOriginPatterns("*").
//               exposedHeaders("Authorization").
//               maxAge(3600L);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //默认是/static/
        registry.addResourceHandler("/static/**").
                addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**").
                addResourceLocations("classpath:/templates/");
        registry.addResourceHandler("/doc.html")
                        .addResourceLocations("classpath:/META-INF/resources/");
        // 放行swagger
        registry.addResourceHandler("/swagger-ui.html").
                addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").
                addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenFlushInterceptor);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter m = new MappingJackson2HttpMessageConverter();
        //设置对时间类序列化的处理
        m.setObjectMapper(new JacksonObjectMapper());
        //优先使用自定义消息转换器
        converters.add(0,m);
    }
}

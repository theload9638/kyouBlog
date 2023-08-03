package com.kyou.blog.background.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;


import java.util.ArrayList;
import java.util.List;

/**
 * @author CC
 * time 2023-05-26
 * description
 */
@Configuration
@EnableSwagger2WebMvc
@EnableSwagger2
public class SwaggerConfig {
    //启用swagger
    private boolean enabled=true;
    /**
     * 管理端文档
     * @return
     */
    @Bean
    public Docket adminDocket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("kyou个人博客后端接口文档")
                .version("1.0")
                .description("模块后端接口文档")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("博客接口后台管理")
                .enable(enabled)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.kyou.blog.background.web"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
    public List<Parameter> globalRequestParams(){
        List<Parameter> params = new ArrayList<>();
        ParameterBuilder tokenPar = new ParameterBuilder();
        //设置一个全局参数名称为token
        tokenPar.name("token")
                //此参数的描述
                .description("用户token")
                //默认值
                .defaultValue("")
                //参数数据类型
                .modelRef(new ModelRef("string"))
                //参数的存放类型为http请求头
                .parameterType("header")
                //设置参数是否必需为false
                .required(false)
                .build();
        params.add(tokenPar.build());
        return params;
    }



}

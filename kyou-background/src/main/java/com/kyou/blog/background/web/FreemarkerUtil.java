package com.kyou.blog.background.web;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CC
 * time 2023-07-28
 * description
 */
public final class FreemarkerUtil {

    private static String directorySrc="D:\\JAVA\\notes\\temp\\my-webLog\\kyouBlog\\kyou-background\\src\\main\\resources\\templates";

    /**
     * 生成 通用文章的方法
     * @param descAbsoluteFilePath 输出到的绝对路径
     * @param data
     */
    public static void generate(String descAbsoluteFilePath,Map data){
        generateData(descAbsoluteFilePath,data,"bkArticle.ftl");
    }
    public static void generateData(String descAbsoluteFilePath,Map data,String templateName){
        try {
            //1.创建配置类
            Configuration configuration=new Configuration(Configuration.getVersion());
            //2.设置模板所在的目录
            configuration.setDirectoryForTemplateLoading(new File(directorySrc));
            //3.设置字符集
            configuration.setDefaultEncoding("utf-8");
            //4.加载模板
            Template template = configuration.getTemplate(templateName);
            //5.创建数据模型
            //6.创建Writer对象
            Writer out =new OutputStreamWriter(new FileOutputStream(descAbsoluteFilePath));
            //7.输出
            template.process(data, out);
            //8.关闭Writer对象
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成用户主页
     */
    public static void generateUserHome(String descAbsoluteFilePath,Map data){
       generateData(descAbsoluteFilePath,data,"bkUserHome.ftl");
    }



}

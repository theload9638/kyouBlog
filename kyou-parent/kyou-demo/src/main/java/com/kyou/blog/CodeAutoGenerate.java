package com.kyou.blog;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.Scanner;

/**
 * @author CC
 * time 2023-07-05
 * description
 */

public final class CodeAutoGenerate {
    //代码生成的模块绝对路径
    private String moduleOutPath="";
    //数据库
    private String database="";
    //数据库用户名
    private String username="";
    //数据库密码
    private String pwd="";
    //包路径
    private String packageName;
    //模块名称
    private String moduleName;
    //数据库表
    private String[] tables;
    public CodeAutoGenerate(){

    }

    public CodeAutoGenerate(String username,String pwd,String database){
        this.username=username;
        this.pwd=pwd;
        this.database=database;
        Scanner scanner = new Scanner(System.in);
        System.out.println("是否采用对话模式(y/n)");
        String input = scanner.next();
        if (input.equalsIgnoreCase("y")) {
            System.out.println("请输入 代码输出路径--》");
            this.moduleOutPath= scanner.next();
            System.out.println("请输入 包路径---》");
            this.packageName=scanner.next();
            System.out.println("请输入 模块名称--》");
            this.moduleName=scanner.next();
            scanner.nextLine();
            System.out.println("请输入 数据库表，空格分隔---》");
            String table=scanner.nextLine();
            this.tables=table.split(" ");
        }
    }
    public void auto(){
        // 1、创建代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 2、全局配置
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(moduleOutPath+"/src/main/java");
        //去掉Service接口的首字母I
        gc.setServiceName("%sService");
        gc.setAuthor("cc");
        gc.setOpen(false);
        mpg.setGlobalConfig(gc);

        // 3、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/"+database+"?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true&allowMultiQueries=true");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername(username);
        dsc.setPassword(pwd);
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);
        // 4、包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(packageName);
        //模块名
        pc.setModuleName(moduleName);
        pc.setController("controller");
        pc.setService("service");
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);

        // 5、策略配置
        StrategyConfig strategy = new StrategyConfig();
        //配置数据库表
        strategy.setInclude(tables);
        //数据库表映射到实体的命名策略：下划线到驼峰
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //数据库表字段映射到实体的命名策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // lombok 模型 @Accessors(chain = true) setter链式操作
        strategy.setEntityLombokModel(true);
        //restful api风格控制器
        strategy.setRestControllerStyle(true);
        //url中驼峰转连字符
        strategy.setControllerMappingHyphenStyle(true);
        mpg.setStrategy(strategy);
        // 6、执行
        mpg.execute();


    }

}

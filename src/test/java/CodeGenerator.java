import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.Controller;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.config.builder.Mapper;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * 代码生成器，根据数据表名称生成对应的Model、Mapper、Service、Controller简化开发。
 */
public class CodeGenerator {
    //JDBC配置，请修改为你项目的实际配置
    private static final String JDBC_URL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=paas_test";
    private static final String JDBC_USERNAME = "sa";
    private static final String JDBC_PASSWORD = "123456";
    private static final String PROJECT_PATH = System.getProperty("user.dir");//项目在硬盘上的基础路径
    private static final String JAVA_PATH = "/src/main/java"; //java文件路径
    private static final String RESOURCES_PATH = "/src/main/resources";//资源文件路径
    private static final String PACKAGE_NAME = "com.uxlt.project";
    private static final String AUTHOR = "April Z";//@author

    private static final String MODULE_NAME = "my_module";//要生成的表
    private static final String[] TABLES = {"t_user"};//要生成的表
    private static final String[] TABLES_PREFIX = {"t_", "c_"};//前缀
    private static final String[] TABLES_SUFFIX = {};//后缀

    public static void main(String[] args) {
        //FastGenerate(MODULE_NAME, TABLES);
        //Generate(MODULE_NAME, TABLES);
        AutoGenerate(MODULE_NAME, TABLES);
    }

    /*
     * 链式调用
     *
     * @author apr
     * @date 2021/12/8 15:59
     */
    private static void FastGenerate(String module, String... tables){
        FastAutoGenerator.create(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD)
                .globalConfig(builder -> {
                    builder.author(AUTHOR) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            //.fileOverride() // 覆盖已生成文件
                            .outputDir(PROJECT_PATH+JAVA_PATH); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(PACKAGE_NAME) // 设置父包名
                            .moduleName(module) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, PROJECT_PATH+RESOURCES_PATH+"/mapper/")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables) // 设置需要生成的表名
                            .addTablePrefix(TABLES_PREFIX)// 设置过滤表前缀
                            .addTableSuffix(TABLES_SUFFIX)// 设置过滤表前缀
                            .controllerBuilder().enableRestStyle().enableHyphenStyle()
                            .mapperBuilder().enableMapperAnnotation()
                            .entityBuilder().enableLombok().enableTableFieldAnnotation();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateConfig( builder -> {
                    builder.entity("/templates/entity.java")
                            .service("/templates/service.java")
                            .serviceImpl("/templates/serviceImpl.java")
                            .mapper("/templates/mapper.java")
                            .mapperXml("/templates/mapper.xml")
                            .controller("/templates/controller.java")
                            .build();
                    //builder.disable(TemplateType.ENTITY); //禁用Entity模板
                })
        .execute();
    }

    /*
     * 链式调用
     *
     * @author apr
     * @date 2021/12/8 15:59
     */
    private static void AutoGenerate(String moduleName, String... tables){
        new AutoGenerator(new DataSourceConfig.Builder(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD).build())
                .global(new GlobalConfig.Builder()
                        .author(AUTHOR)
                        .enableSwagger()
                        .fileOverride()
                        .outputDir(PROJECT_PATH+JAVA_PATH)
                        .build())
                .packageInfo(new PackageConfig.Builder()
                        .parent(PACKAGE_NAME)
                        .moduleName(moduleName)
                        .pathInfo(Collections.singletonMap(OutputFile.mapperXml, PROJECT_PATH+RESOURCES_PATH+"/mapper/"))
                        .build())
                .strategy(new StrategyConfig.Builder()
                        .addInclude(tables)
                        .addTablePrefix(TABLES_PREFIX)
                        .addTableSuffix(TABLES_SUFFIX)
                        .controllerBuilder().enableRestStyle().enableHyphenStyle()
                        .mapperBuilder().enableMapperAnnotation()
                        .entityBuilder().enableLombok()//.enableTableFieldAnnotation()
                        .build())
                .template(new TemplateConfig.Builder()
                        .entity("/templates/entity.java")
                        .service("/templates/service.java")
                        .serviceImpl("/templates/serviceImpl.java")
                        .mapper("/templates/mapper.java")
                        .mapperXml("/templates/mapper.xml")
                        .controller("/templates/controller.java")
                        .build())
                .execute( new FreemarkerTemplateEngine() );
    }

    /*
     * 传统写法
     *
     * @author apr
     * @date 2021/12/8 16:00
     */
    @Deprecated
    private static void Generate(String moduleName, String[] tableNames){
        AutoGenerator gen = new AutoGenerator( getDataSourceConfig() );// 数据源配置
        gen.global( getGlobalConfig() );
        gen.packageInfo( getPackageConfig(moduleName) );
        gen.strategy( getStrategyConfig(tableNames) );
        gen.template( getTemplateConfig() );
        gen.execute( new FreemarkerTemplateEngine() );//用freemarker引擎

    }

    private static DataSourceConfig getDataSourceConfig() {
        DataSourceConfig.Builder dataSourceConfigBuilder= new DataSourceConfig.Builder(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
        return dataSourceConfigBuilder.build();
    }

    private static GlobalConfig getGlobalConfig() {
        GlobalConfig.Builder globalConfigBuilder = new GlobalConfig.Builder();
        globalConfigBuilder.author(AUTHOR); // 设置作者
        globalConfigBuilder.enableSwagger(); // 开启 swagger 模式
        globalConfigBuilder.fileOverride(); // 覆盖已生成文件
        globalConfigBuilder.outputDir(PROJECT_PATH+JAVA_PATH); // 指定输出目录.build();

        return globalConfigBuilder.build();
    }

    private static PackageConfig getPackageConfig(String moduleName) {
        PackageConfig.Builder packageConfigBuilder = new PackageConfig.Builder();
        packageConfigBuilder.parent(PACKAGE_NAME); // 设置父包名
        packageConfigBuilder.moduleName(moduleName); // 设置父包模块名
        packageConfigBuilder.pathInfo(Collections.singletonMap(OutputFile.mapperXml, PROJECT_PATH+RESOURCES_PATH+"/mapper/")); // 设置mapperXml生成路径

        return packageConfigBuilder.build();
    }

    private static TemplateConfig getTemplateConfig() {
        TemplateConfig.Builder templateConfigBuilder = new TemplateConfig.Builder();
        templateConfigBuilder.entity("/templates/entity.java");
        templateConfigBuilder.service("/templates/service.java");
        templateConfigBuilder.serviceImpl("/templates/serviceImpl.java");
        templateConfigBuilder.mapper("/templates/mapper.java");
        templateConfigBuilder.mapperXml("/templates/mapper.xml");
        templateConfigBuilder.controller("/templates/controller.java");

        return templateConfigBuilder.build();
    }

    private static StrategyConfig getStrategyConfig(String[] tables) {
        StrategyConfig.Builder strategyConfigBuilder = new StrategyConfig.Builder();
        strategyConfigBuilder.addInclude(tables);// 设置需要生成的表名
        strategyConfigBuilder.addTablePrefix(TABLES_PREFIX);// 设置过滤表前缀
        strategyConfigBuilder.addTableSuffix(TABLES_SUFFIX);// 设置过滤表前缀

        Controller.Builder controllerBuilder = strategyConfigBuilder.controllerBuilder();
        controllerBuilder.enableRestStyle();
        controllerBuilder.enableHyphenStyle();

        Mapper.Builder mapperBuilder = strategyConfigBuilder.mapperBuilder();
        mapperBuilder.enableMapperAnnotation();
        
        Entity.Builder entityBuilder = strategyConfigBuilder.entityBuilder();
        entityBuilder.enableLombok();
        entityBuilder.enableTableFieldAnnotation();

        return strategyConfigBuilder.build();
    }


}

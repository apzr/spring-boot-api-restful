import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import static com.uxlt.project.core.ProjectConstant.*;

/**
 * 代码生成器，根据数据表名称生成对应的Model、Mapper、Service、Controller简化开发。
 */
public class CodeGenerator {
    //JDBC配置，请修改为你项目的实际配置
    private static final String JDBC_URL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=paas_test";
    private static final String JDBC_USERNAME = "sa";
    private static final String JDBC_PASSWORD = "123456";
    private static final String JDBC_DIVER_CLASS_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private static final String PROJECT_PATH = System.getProperty("user.dir");//项目在硬盘上的基础路径
    private static final String TEMPLATE_FILE_PATH = PROJECT_PATH + "/src/test/resources/generator/template";//模板位置

    private static final String JAVA_PATH = "/src/main/java"; //java文件路径
    private static final String RESOURCES_PATH = "/src/main/resources";//资源文件路径

    private static final String PACKAGE_NAME = "com.uxlt.project";
    private static final String PACKAGE_PATH_SERVICE = packagePath(SERVICE_PACKAGE);//生成的Service存放路径
    private static final String PACKAGE_PATH_SERVICE_IMPL = packagePath(SERVICE_IMPL_PACKAGE);//生成的Service实现存放路径
    private static final String PACKAGE_PATH_CONTROLLER = packagePath(CONTROLLER_PACKAGE);//生成的Controller存放路径

    private static final String AUTHOR = "April Z";//@author
    private static final String DATE = new SimpleDateFormat("yyyy/MM/dd").format(new Date());//@date

    public static void main(String[] args) {
        FastAutoGenerator.create(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD)
            .globalConfig(builder -> {
                builder.author(AUTHOR) // 设置作者
                    .enableSwagger() // 开启 swagger 模式
                    .fileOverride() // 覆盖已生成文件
                    .outputDir(PROJECT_PATH+JAVA_PATH); // 指定输出目录
            })
            .packageConfig(builder -> {
                builder.parent(PACKAGE_NAME) // 设置父包名
                    .moduleName("UUU") // 设置父包模块名
                    .pathInfo(Collections.singletonMap(OutputFile.mapperXml, PROJECT_PATH+RESOURCES_PATH+"/mapper/")); // 设置mapperXml生成路径
            })
            .strategyConfig(builder -> {
                    builder.addInclude("t_user") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_")// 设置过滤表前缀
                            .controllerBuilder().enableRestStyle().enableHyphenStyle()
                            .mapperBuilder().enableMapperAnnotation()
                            .entityBuilder().enableLombok().enableTableFieldAnnotation();
                    //      .addTableFills(
                    //          new Column("create_time", FieldFill.INSERT)
                    //      ).build();
            })
            .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
            .templateConfig( builder -> {
                builder.disable(TemplateType.ENTITY)
                    .entity("/templates/entity.java")
                    .service("/templates/service.java")
                    .serviceImpl("/templates/serviceImpl.java")
                    .mapper("/templates/mapper.java")
                    .mapperXml("/templates/mapper.xml")
                    .controller("/templates/controller.java")
                    .build();
            })
            .execute();


    }

    /*
     * packagePath
     *
     * @param packageName
     * @return java.lang.String
     * @author apr
     * @date 2021/12/7 10:36
     */
    private static String packagePath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }

}

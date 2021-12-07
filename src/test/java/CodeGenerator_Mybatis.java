import com.google.common.base.CaseFormat;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.uxlt.project.core.ProjectConstant.*;

/**
 * 代码生成器，根据数据表名称生成对应的Model、Mapper、Service、Controller简化开发。
 */
public class CodeGenerator_Mybatis {
    //JDBC配置，请修改为你项目的实际配置
    private static final String JDBC_URL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=paas_test";
    private static final String JDBC_USERNAME = "sa";
    private static final String JDBC_PASSWORD = "123456";
    private static final String JDBC_DIVER_CLASS_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private static final String PROJECT_PATH = System.getProperty("user.dir");//项目在硬盘上的基础路径
    private static final String TEMPLATE_FILE_PATH = PROJECT_PATH + "/src/test/resources/generator/template";//模板位置

    private static final String JAVA_PATH = "/src/main/java"; //java文件路径
    private static final String RESOURCES_PATH = "/src/main/resources";//资源文件路径

    private static final String PACKAGE_PATH_SERVICE = packagePath(SERVICE_PACKAGE);//生成的Service存放路径
    private static final String PACKAGE_PATH_SERVICE_IMPL = packagePath(SERVICE_IMPL_PACKAGE);//生成的Service实现存放路径
    private static final String PACKAGE_PATH_CONTROLLER = packagePath(CONTROLLER_PACKAGE);//生成的Controller存放路径

    private static final String AUTHOR = "April Z";//@author
    private static final String DATE = new SimpleDateFormat("yyyy/MM/dd").format(new Date());//@date

    public static void main(String[] args) {
        //genCode("输入表名");
        generateCodeByCustomModelName("t_user","User");
    }

    /**
     * 通过数据表名称生成代码，Model 名称通过解析数据表名称获得，下划线转大驼峰的形式。
     * 如输入表名称 "t_user_detail" 将生成 TUserDetail、TUserDetailMapper、TUserDetailService ...
     * @param tableNames 数据表名称...
     */
    public static void genCode(String... tableNames) {
        for (String tableName : tableNames) {
            generateCodeByCustomModelName(tableName, null);
        }
    }

    /**
     * 通过数据表名称，和自定义的 Model 名称生成代码
     * 如输入表名称 "t_user_detail" 和自定义的 Model 名称 "User" 将生成 User、UserMapper、UserService ...
     * @param tableName 数据表名称
     * @param modelName 自定义的 Model 名称
     */
    public static void generateCodeByCustomModelName(String tableName, String modelName) {
        generateModelAndMapper(tableName, modelName);
        generateService(tableName, modelName);
        generateController(tableName, modelName);
    }

    public static void generateModelAndMapper(String tableName, String modelName) {
        try {
            Context context = getContext(tableName, modelName);
            Configuration config = getConfiguration(context);

            DefaultShellCallback callback = new DefaultShellCallback(true);
            List<String> warnings = new ArrayList();

            //生成
            MyBatisGenerator generator = new MyBatisGenerator(config, callback, warnings);
            generator.generate(null);

            if (generator.getGeneratedJavaFiles().isEmpty() || generator.getGeneratedXmlFiles().isEmpty())
                throw new RuntimeException("生成Model和Mapper失败：" + warnings);

            if (StringUtils.isEmpty(modelName))
                modelName = toUpperCase(tableName);

        } catch (Exception e) {
            throw new RuntimeException("生成Model和Mapper失败", e);
        }

        System.out.println(modelName + ".java 生成成功");
        System.out.println(modelName + "Mapper.java 生成成功");
        System.out.println(modelName + "Mapper.xml 生成成功");
    }

    public static void generateService(String tableName, String modelName) {
        try {
            freemarker.template.Configuration cfg = getConfiguration();
            String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? toUpperCase(tableName) : modelName;
            Map<String, Object> data = getServiceDataModel(tableName, modelNameUpperCamel);

            String servicePath = PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_SERVICE + modelNameUpperCamel + "Service.java";
            FileWriter serviceFileWriter = makeFileWriter(servicePath);
            cfg.getTemplate("service.ftl").process(data, serviceFileWriter);
            System.out.println(modelNameUpperCamel + "Service.java 生成成功");

            String serviceImplPath = PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_SERVICE_IMPL + modelNameUpperCamel + "ServiceImpl.java";
            FileWriter serviceImplFileWriter = makeFileWriter(serviceImplPath);
            cfg.getTemplate("service-impl.ftl").process(data, serviceImplFileWriter);
            System.out.println(modelNameUpperCamel + "ServiceImpl.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Service失败", e);
        }
    }

    public static void generateController(String tableName, String modelName) {
        try {
            freemarker.template.Configuration cfg = getConfiguration();
            String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? toUpperCase(tableName) : modelName;
            Map<String, Object> data = getControllerDataModel(tableName, modelNameUpperCamel);

            String controllerPath = PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_CONTROLLER + modelNameUpperCamel + "Controller.java";
            FileWriter controllerFileWriter = makeFileWriter(controllerPath);
            cfg.getTemplate("controller-restful.ftl").process(data, controllerFileWriter);
            System.out.println(modelNameUpperCamel + "Controller.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Controller失败", e);
        }
    }

    /*
     * getContext
     *
     * @param tableName
     * @param modelName
     * @return org.mybatis.generator.config.Context
     * @author apr
     * @date 2021/12/7 10:00
     */
    private static Context getContext(String tableName, String modelName) {
        Context context = new Context(ModelType.FLAT);
        context.setId("Potato");
        context.setTargetRuntime("MyBatis3Simple");
        context.addProperty(PropertyRegistry.CONTEXT_BEGINNING_DELIMITER, "`");
        context.addProperty(PropertyRegistry.CONTEXT_ENDING_DELIMITER, "`");

        context.setJdbcConnectionConfiguration(getJdbcConnectionConfiguration());
        context.addPluginConfiguration(getPluginConfiguration());
        context.setJavaModelGeneratorConfiguration(getJavaModelGeneratorConfiguration());
        context.setSqlMapGeneratorConfiguration(getSqlMapGeneratorConfiguration());
        context.setJavaClientGeneratorConfiguration( getJavaClientGeneratorConfiguration());
        context.addTableConfiguration(getTableConfiguration(tableName, modelName, context));

        return context;
    }

    /*
     * getConfiguration
     *
     * @param context
     * @return org.mybatis.generator.config.Configuration
     * @author apr
     * @date 2021/12/7 10:01
     */
    private static Configuration getConfiguration(Context context) throws InvalidConfigurationException {
        Configuration config = new Configuration();
        config.addContext(context);
        config.validate();

        return config;
    }

    /*
     * freemarker Configuration
     *
     * @param
     * @return freemarker.template.Configuration
     * @author apr
     * @date 2021/12/7 10:07
     */
    private static freemarker.template.Configuration getConfiguration() throws IOException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
        cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

        return cfg;
    }

    /*
     * getDataModel
     *
     * @param tableName
     * @param modelNameUpperCamel
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author apr
     * @date 2021/12/7 10:10
     */
    private static Map<String, Object> getServiceDataModel(String tableName, String modelNameUpperCamel) {
        Map<String, Object> data = new HashMap<>();
        data.put("date", DATE);
        data.put("author", AUTHOR);
        data.put("modelNameUpperCamel", modelNameUpperCamel);
        data.put("modelNameLowerCamel", toLowerCase(tableName));
        data.put("basePackage", BASE_PACKAGE);

        return data;
    }

    /*
     * getDataModel
     *
     * @param tableName
     * @param modelNameUpperCamel
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author apr
     * @date 2021/12/7 10:10
     */
    private static Map<String, Object> getControllerDataModel(String tableName, String modelNameUpperCamel) {
        Map<String, Object> data = new HashMap<>();
        data.put("date", DATE);
        data.put("author", AUTHOR);
        data.put("baseRequestMapping", modelPath(modelNameUpperCamel));
        data.put("modelNameUpperCamel", modelNameUpperCamel);
        data.put("modelNameLowerCamel", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelNameUpperCamel));
        data.put("basePackage", BASE_PACKAGE);

        return data;
    }

    private static FileWriter makeFileWriter(String path) throws IOException {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        return new FileWriter(file);
    }
    /*
     * getJdbcConnectionConfiguration
     *
     * @param
     * @return org.mybatis.generator.config.JDBCConnectionConfiguration
     * @author apr
     * @date 2021/12/7 9:55
     */
    private static JDBCConnectionConfiguration getJdbcConnectionConfiguration() {
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setConnectionURL(JDBC_URL);
        jdbcConnectionConfiguration.setUserId(JDBC_USERNAME);
        jdbcConnectionConfiguration.setPassword(JDBC_PASSWORD);
        jdbcConnectionConfiguration.setDriverClass(JDBC_DIVER_CLASS_NAME);
        return jdbcConnectionConfiguration;
    }

    /*
     * getPluginConfiguration
     *
     * @param
     * @return org.mybatis.generator.config.PluginConfiguration
     * @author apr
     * @date 2021/12/7 9:55
     */
    private static PluginConfiguration getPluginConfiguration() {
        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        pluginConfiguration.setConfigurationType("tk.mybatis.mapper.generator.MapperPlugin");
        pluginConfiguration.addProperty("mappers", MAPPER_INTERFACE_REFERENCE);
        return pluginConfiguration;
    }

    /**
     * getJavaModelGeneratorConfiguration
     *
     * @param
     * @return org.mybatis.generator.config.JavaModelGeneratorConfiguration
     * @author apr
     * @date 2021/12/7 9:55
     */
    private static JavaModelGeneratorConfiguration getJavaModelGeneratorConfiguration() {
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
        javaModelGeneratorConfiguration.setTargetProject(PROJECT_PATH + JAVA_PATH);
        javaModelGeneratorConfiguration.setTargetPackage(MODEL_PACKAGE);
        return javaModelGeneratorConfiguration;
    }

    /*
     * getSqlMapGeneratorConfiguration
     *
     * @param
     * @return org.mybatis.generator.config.SqlMapGeneratorConfiguration
     * @author apr
     * @date 2021/12/7 9:54
     */
    private static SqlMapGeneratorConfiguration getSqlMapGeneratorConfiguration() {
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
        sqlMapGeneratorConfiguration.setTargetProject(PROJECT_PATH + RESOURCES_PATH);
        sqlMapGeneratorConfiguration.setTargetPackage("mapper");
        return sqlMapGeneratorConfiguration;
    }

    /*
     * getJavaClientGeneratorConfiguration
     *
     * @param
     * @return org.mybatis.generator.config.JavaClientGeneratorConfiguration
     * @author apr
     * @date 2021/12/7 9:54
     */
    private static JavaClientGeneratorConfiguration getJavaClientGeneratorConfiguration() {
        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setTargetProject(PROJECT_PATH + JAVA_PATH);
        javaClientGeneratorConfiguration.setTargetPackage(MAPPER_PACKAGE);
        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        return javaClientGeneratorConfiguration;
    }

    /*
     * getTableConfiguration
     *
     * @param tableName
     * @param modelName
     * @param context
     * @return org.mybatis.generator.config.TableConfiguration
     * @author apr
     * @date 2021/12/7 9:54
     */
    private static TableConfiguration getTableConfiguration(String tableName, String modelName, Context context) {
        TableConfiguration tableConfiguration = new TableConfiguration(context);
        tableConfiguration.setTableName(tableName);
        if (StringUtils.isNotEmpty(modelName))
            tableConfiguration.setDomainObjectName(modelName);
        tableConfiguration.setGeneratedKey(new GeneratedKey("id", "SqlServer", true, null));
        return tableConfiguration;
    }

    /*
     * toLowerCase
     *
     * @param tableName
     * @return java.lang.String
     * @author apr
     * @date 2021/12/7 10:35
     */
    private static String toLowerCase(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName.toLowerCase());
    }

    /*
     * toUpperCase
     *
     * @param tableName
     * @return java.lang.String
     * @author apr
     * @date 2021/12/7 10:35
     */
    private static String toUpperCase(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.toLowerCase());
    }

    /*
     * tablePath
     *
     * @param tableName
     * @return java.lang.String
     * @author apr
     * @date 2021/12/7 10:36
     */
    private static String tablePath(String tableName) {
        tableName = tableName.toLowerCase();//兼容使用大写的表名
        return "/" + (tableName.contains("_") ? tableName.replaceAll("_", "/") : tableName);
    }

    /*
     * modelPath
     *
     * @param modelName
     * @return java.lang.String
     * @author apr
     * @date 2021/12/7 10:36
     */
    private static String modelPath(String modelName) {
        String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, modelName);
        return tablePath(tableName);
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

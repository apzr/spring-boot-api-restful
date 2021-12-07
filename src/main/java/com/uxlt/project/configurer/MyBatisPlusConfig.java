package com.uxlt.project.configurer;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisPlusConfig
 * <p>
 * <br/>
 *
 * @author apr
 * @date 2021/12/07 17:11:40
 **/
@Configuration
public class MyBatisPlusConfig {

    /*
     * 分页配置
     */
    @Bean
    public MybatisPlusInterceptor PaginationInnerInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.SQL_SERVER2005));
        return interceptor;
    }
}
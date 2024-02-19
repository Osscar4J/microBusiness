package com.zhao.micro.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@MapperScan("com.zhao.micro.mapper")
@Configuration
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor generatePaginationInterceptor() {
        return new PaginationInterceptor();
    }
}

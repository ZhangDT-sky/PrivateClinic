package org.code.privateclinic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // 允许所有源
        config.addAllowedOriginPattern("*");//跨域源
        config.addAllowedHeader("*");//请求头
        config.addAllowedMethod("*");//HTTP方法
        config.setAllowCredentials(true);//允许凭证
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}


package com.sjz.mock.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 *
 * ClassName: RestCorsConfig <br/>
 * Function: 允许跨域 <br/>
 * date: 2018年1月26日 下午2:31:58 <br/>
 *
 * @author shijun@richinfo.cn
 * @since V1.0
 */
@SpringBootConfiguration
public class RestCorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // 1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        // 放行哪些原始域
        config.addAllowedOrigin("*");
        // 放行哪些原始域(头部信息)
        config.addAllowedHeader("*");
        // 是否发送Cookie信息
        config.setAllowCredentials(true);
        // 放行哪些原始域(请求方式)
        config.addAllowedMethod("*");
        // config.addAllowedMethod("GET");
        // config.addAllowedMethod("PUT");
        // config.addAllowedMethod("POST");
        // config.addAllowedMethod("DELETE");

        // 暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
        // config.addExposedHeader("*");

        // 2.添加映射路径
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        // 3.返回新的CorsFilter.
        return new CorsFilter(configSource);
    }
}

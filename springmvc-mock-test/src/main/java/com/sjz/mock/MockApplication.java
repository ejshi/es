package com.sjz.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class MockApplication extends SpringBootServletInitializer {

    private static Logger LOGGER = LoggerFactory.getLogger(MockApplication.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MockApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication springApplication =
                new SpringApplicationBuilder(MockApplication.class).web(true).build();
        springApplication.run(args);
        LOGGER.info("======== start MockApplication success ======");
    }
}

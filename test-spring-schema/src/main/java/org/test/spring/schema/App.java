package org.test.spring.schema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.test.spring.schema.strategy.FristStrategy;

/**
 * Hello world!
 */
@SpringBootApplication
public class App { 
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
		FristStrategy bean = context.getBean(FristStrategy.class);
		System.out.println("数据调用结果集========"+bean.hello());
		System.out.println("Hello World!");
	}
}

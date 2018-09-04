package org.test.spring.schema.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations="spring-strategy.xml")
public class StrategyConfig {
	
}

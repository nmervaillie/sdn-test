package com.sdn.test.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "com.sdn.test")
@Import({Neo4JConfig.class})
public class AppConfig {
}

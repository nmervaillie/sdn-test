package com.sdn.test.config;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableNeo4jRepositories(basePackages = "com.sdn.test.repository")
@EnableTransactionManagement
@EnableRetry
public class Neo4JConfig {
    @Bean
    public SessionFactory sessionFactory() {
        // with domain entity base package(s)
        return new SessionFactory("com.sdn.test.domain");
    }

    @Bean
    public Neo4jTransactionManager transactionManager() {
        return new Neo4jTransactionManager(sessionFactory());
    }
}


//import org.neo4j.ogm.session.SessionFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.neo4j.config.Neo4jConfiguration;
//import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//@Configuration
//@EnableNeo4jRepositories(basePackages = "com.sdn.test.repository")
//@EnableTransactionManagement
//public class Neo4JConfig extends Neo4jConfiguration {
//
//    @Bean
//    public SessionFactory getSessionFactory() {
//        // with domain entity base package(s)
//        return new SessionFactory("com.sdn.test.domain");
//    }
//}



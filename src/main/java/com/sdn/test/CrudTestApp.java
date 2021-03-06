package com.sdn.test;

import com.sdn.test.config.AppConfig;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CrudTestApp {

    public static void main(String[] args) {

        Properties properties = new Properties();
        File file = new File("config.properties");

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        if(file.exists()) {
            try (Reader reader = new FileReader(file)) {
                properties.load(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String mode = args[0];
            Neo4jRepo loadTest = ctx.getBean(Neo4jRepo.class);

            switch (mode) {
                case "r":
                    loadTest.readTest(properties);
                    break;
                case "w":
                    loadTest.writeTest(properties);
                    break;
                case "d":
                    loadTest.deleteTest(properties);
                    break;
                case "c":
                    loadTest.crudTest(properties);
                    break;
            }

        }
        else {
            System.out.println("Need config.properties in current execution folder");
        }
    }
}

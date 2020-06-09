package com.test.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class DemoApplication {

    private static Logger log = Logger.getLogger(DemoApplication.class.getName());


    public static void main(String[] args) {
        
        log.info("Starting spring boot application...");
        SpringApplication.run(DemoApplication.class, args);
    }

}

package com.test.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import java.util.Properties;
import java.util.logging.Logger;


@SpringBootApplication
class DemoApplication {

    private static Logger log = Logger.getLogger(DemoApplication.class.getName());


    public static void main(String[] args) {

        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "org.hibernate.envers.audit_table_suffix", "_AUDIT_LOG");
        sessionFactory.setHibernateProperties(hibernateProperties);

        log.info("Starting spring boot application...");
        SpringApplication.run(DemoApplication.class, args);

    }

}



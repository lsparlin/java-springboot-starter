package io.prismic.starter;

import io.prismic.Cache;
import io.prismic.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // PRISMIC BEANS

    @Bean
    public static Cache prismicCache() {
        return new Cache.BuiltInCache(200);
    }

    @Bean
    public static Logger prismicLogger() {
        return new Logger.PrintlnLogger();
    }

}

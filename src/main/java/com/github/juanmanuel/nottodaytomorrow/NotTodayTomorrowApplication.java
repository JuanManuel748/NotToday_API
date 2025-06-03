package com.github.juanmanuel.nottodaytomorrow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NotTodayTomorrowApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotTodayTomorrowApplication.class, args);
    }
}
package ru.practicum.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.practicum.main", "ru.practicum.stat.client"})
public class ApplicationMainEwm {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationMainEwm.class, args);
    }
}

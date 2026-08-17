package ru.practicum.stat.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.practicum.stat.service.StatService;

@RestController
public class StatsController {

    private final StatService statService;

    public StatsController(StatService statService) {
        this.statService = statService;
    }

}

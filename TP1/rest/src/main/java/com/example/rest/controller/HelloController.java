package com.example.rest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping(value = {"/", "/hello"})
    public String hello() {
        return "Hello World from my first REST API with Spring Boot!";
    }
}

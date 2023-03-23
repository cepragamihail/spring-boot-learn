package com.springtutorial.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConferenceController {

    @GetMapping("/about")
    public String about() {
        return "Join us online September 1-2!";
    }
}

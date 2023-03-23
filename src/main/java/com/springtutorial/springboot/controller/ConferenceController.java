package com.springtutorial.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class ConferenceController {

    @GetMapping("/about")
    public void about() {}
}

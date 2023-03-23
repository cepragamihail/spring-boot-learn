package com.springtutorial.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConferenceController {

    @GetMapping("/about")
    public void about() {}
}

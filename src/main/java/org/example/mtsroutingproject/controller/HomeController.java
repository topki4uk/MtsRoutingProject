package org.example.mtsroutingproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Initial HTML page
     * @return rendered page
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }
}

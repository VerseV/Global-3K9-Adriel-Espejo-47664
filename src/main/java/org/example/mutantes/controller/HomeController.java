package org.example.mutantes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // Redirige de la raíz (localhost:8080) a Swagger
        return "redirect:/swagger-ui.html";
    }
}
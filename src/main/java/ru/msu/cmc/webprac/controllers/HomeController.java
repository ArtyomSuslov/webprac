package ru.msu.cmc.webprac.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;

@Controller
public class HomeController {
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {

        model.addAttribute("currentPage", "home");
        model.addAttribute("pageTitle", "Библиотечная система");
        model.addAttribute("description", "Добро пожаловать в систему управления библиотекой");

        return "home";
    }
}
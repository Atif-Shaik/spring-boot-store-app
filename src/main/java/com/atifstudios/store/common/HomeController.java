package com.atifstudios.store.common;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(Model model) { // model sends value to the html
        model.addAttribute("name", "Atif"); // add th value (Thymeleaf)
        return "index"; // remove .html because we are using thymeleaf
    } // method ends

} // class ends

package com.api.solver.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ComponentController {



    @GetMapping("/save_component")

    public String addComponent(){
        return "add_component";
    }


}

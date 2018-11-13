package com.sjz.mock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @ResponseBody
    @GetMapping(path = "/helloworld")
    public String helloWorld(){
        return "hello world";
    }
}

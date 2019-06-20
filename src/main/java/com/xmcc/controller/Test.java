package com.xmcc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class Test {
    //Logger logger=  LoggerFactory.getLogger(Test.class);
    @RequestMapping("/hello")
    @GetMapping
    public String test(){
        //log.info("info->{}","hello logback info sl4j");
        return "hello Spring-boot";
    }
}

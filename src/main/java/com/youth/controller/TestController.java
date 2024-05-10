package com.youth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/test")
    @PreAuthorize("hasAuthority('system:user:list')")
    public String test() {
        return "test";
    }


}

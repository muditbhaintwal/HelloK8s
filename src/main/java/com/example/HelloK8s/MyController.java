package com.example.HelloK8s;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class MyController {

    @GetMapping(path = "/v1/time")
    public String getTime() {
        return new Date().toString();
    }
}

package com.aspectfilter.logging.service;


import org.springframework.stereotype.Service;

@Service
public class DemoService {

    public String getMessage() {
        return "Hello from DemoService!";
    }

    public String getAnotherMessage() {
        return "Another message from DemoService!";
    }

    public String getThirdMessage() {
        return "Third message from DemoService!";
    }

}

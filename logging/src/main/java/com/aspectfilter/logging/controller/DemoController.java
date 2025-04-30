package com.aspectfilter.logging.controller;


import com.aspectfilter.logging.aspect.LoggingAspect;
import com.aspectfilter.logging.service.DemoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoController {

    private final DemoService demoService;

    public DemoController(DemoService demoService){
        this.demoService = demoService;
    }

    @LoggingAspect
    @RequestMapping("/demo")
    public ResponseEntity<String> demo(HttpServletRequest request) {

        return ResponseEntity.ok().body(demoService.getMessage());
    }

    @LoggingAspect
    @RequestMapping("/demo2")
    public ResponseEntity<String> demo2(HttpServletRequest request) {
        return ResponseEntity.ok().body(demoService.getAnotherMessage());
    }

    @LoggingAspect
    @RequestMapping("/demo3")
    public ResponseEntity<String> demo3(HttpServletRequest request) {
        return ResponseEntity.ok().body(demoService.getThirdMessage());
    }

}

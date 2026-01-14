package com.raphael.pesapal_interview_frontend.controller;

import com.raphael.pesapal_interview_frontend.dto.ChartClassDTOs;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class ChartClassController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String url = "http://localhost:8080/chartClass";



    @GetMapping("/")
    public String chartClass(Model  model) {
        var chartClasses = restTemplate.getForObject(url, ChartClassDTOs.ChartClassResponse[].class);
        model.addAttribute("chartClasses", chartClasses);
        return "ChartClass";
    }
}

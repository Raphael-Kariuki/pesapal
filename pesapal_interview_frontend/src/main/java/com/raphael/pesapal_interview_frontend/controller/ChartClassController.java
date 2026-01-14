package com.raphael.pesapal_interview_frontend.controller;

import com.raphael.pesapal_interview_frontend.dto.ChartClassDTOs;
import com.raphael.pesapal_interview_frontend.dto.RegisterChartClassRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Controller
public class ChartClassController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String url = "http://localhost:8080/chartClass";



    @GetMapping("/chartClasses")
    public String chartClass(Model  model) {
        var chartClasses = restTemplate.getForObject(url, ChartClassDTOs.ChartClassResponse[].class);
        model.addAttribute("chartClasses", chartClasses);
        return "ChartClass";
    }

    @GetMapping("/addChartClass")
    public String showAddChartClass(RegisterChartClassRequest registerChartClassRequest, Model model) {
        Map<String, String> map = new HashMap<>();
        var classTypes = restTemplate.getForObject(url + "/classTypes", map.getClass());
        model.addAttribute("classTypes", classTypes);
        return "AddChartClass";
    }

    @PostMapping("/addChartClass")
    public String addChartClass(RegisterChartClassRequest registerChartClassRequest) {
       var response = restTemplate.postForObject(url, new HashSet<>(List.of(registerChartClassRequest)), ChartClassDTOs.ChartClassResponse[].class);
       return "redirect:/chartClasses";
    }
}

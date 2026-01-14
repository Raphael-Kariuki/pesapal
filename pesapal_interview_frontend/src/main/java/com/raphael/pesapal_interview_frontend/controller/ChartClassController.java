package com.raphael.pesapal_interview_frontend.controller;

import com.raphael.pesapal_interview_frontend.dto.ChartClassDTOs;
import com.raphael.pesapal_interview_frontend.dto.RegisterChartClassRequest;
import com.raphael.pesapal_interview_frontend.dto.UpdateChartClassRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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

    @GetMapping("/update/{id}")
    public String showUpdateChartClass(@PathVariable Long id, Model model) {
        var chartClass = Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(url + "?oid" + id, ChartClassDTOs.ChartClassResponse[].class))).toList().getFirst();
        var updateChartClass = new UpdateChartClassRequest();
        assert chartClass != null;
        updateChartClass.setOid(id);
        updateChartClass.setClassName(chartClass.className());
        updateChartClass.setClassCode(chartClass.classCode());
        updateChartClass.setClassType(chartClass.classType());
        updateChartClass.setInactive(chartClass.inactive());


         model.addAttribute("chartClass", updateChartClass);

        Map<String, String> map = new HashMap<>();
        var classTypes = restTemplate.getForObject(url + "/classTypes", map.getClass());
        model.addAttribute("classTypes", classTypes);

        model.addAttribute("inactive", List.of(Boolean.TRUE, Boolean.FALSE));

         return "update-chart-class";
    }

    @PostMapping("/update/{id}")
    public String updateChartClass(@PathVariable Long id, UpdateChartClassRequest  updateChartClassRequest) {
        updateChartClassRequest.setOid(id);
        restTemplate.postForObject(url + "/update", new HashSet<>(List.of(updateChartClassRequest)), ChartClassDTOs.ChartClassResponse[].class);
        return "redirect:/chartClasses";

    }
}

package com.raphael.pesapal_interview_frontend.controller;

import com.raphael.pesapal_interview_frontend.dto.ChartClassDTOs;
import com.raphael.pesapal_interview_frontend.dto.RegisterChartClassRequest;
import com.raphael.pesapal_interview_frontend.dto.UpdateChartClassRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Controller
@RequestMapping("/chart-class")
public class ChartClassController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${backend.api-endpoint}/chartClass")
    private String baseUrl;



    @GetMapping
    public String chartClass(Model  model) {
        var chartClasses = restTemplate.getForObject(baseUrl, ChartClassDTOs.ChartClassResponse[].class);
        model.addAttribute("chartClasses", chartClasses);
        return "chart-class";
    }

    @GetMapping("/add")
    public String showAddChartClass(RegisterChartClassRequest registerChartClassRequest, Model model) {
        Map<String, String> map = new HashMap<>();
        var classTypes = restTemplate.getForObject(baseUrl + "/classTypes", map.getClass());
        model.addAttribute("classTypes", classTypes);
        model.addAttribute("registerChartClassRequest", registerChartClassRequest);
        return "create-chart-class";
    }

    @PostMapping("/add")
    public String addChartClass(RegisterChartClassRequest registerChartClassRequest) {
       var response = restTemplate.postForObject(baseUrl, new HashSet<>(List.of(registerChartClassRequest)), ChartClassDTOs.ChartClassResponse[].class);
       return "redirect:/chart-class";
    }

    @GetMapping("/update/{id}")
    public String showUpdateChartClass(@PathVariable Long id, Model model) {
        var finalUrl = baseUrl + "?oid=" + id;
        ResponseEntity<ChartClassDTOs.ChartClassResponse[]> entity = restTemplate.getForEntity(finalUrl, ChartClassDTOs.ChartClassResponse[].class);
        if(!entity.getStatusCode().is2xxSuccessful()){
            return "redirect:/error";
        }
        var chartClass = Arrays.stream(Objects.requireNonNull(entity.getBody())).toList().getFirst();
        var updateChartClassRequest = new UpdateChartClassRequest();
        assert chartClass != null;
        updateChartClassRequest.setOid(id);
        updateChartClassRequest.setClassName(chartClass.className());
        updateChartClassRequest.setClassCode(chartClass.classCode());
        updateChartClassRequest.setClassType(chartClass.classType());
        updateChartClassRequest.setInactive(chartClass.inactive());


         model.addAttribute("updateChartClassRequest", updateChartClassRequest);

        Map<String, String> map = new HashMap<>();
        var classTypes = restTemplate.getForObject(baseUrl + "/classTypes", map.getClass());
        model.addAttribute("classTypes", classTypes);

        model.addAttribute("inactive", List.of(Boolean.TRUE, Boolean.FALSE));

         return "update-chart-class";
    }

    @PostMapping("/update/{id}")
    public String updateChartClass(@PathVariable Long id, UpdateChartClassRequest  updateChartClassRequest) {
        updateChartClassRequest.setOid(id);
        var response = restTemplate.postForEntity(baseUrl + "/update", new HashSet<>(List.of(updateChartClassRequest)), ChartClassDTOs.ChartClassResponse[].class);
        if (!response.getStatusCode().is2xxSuccessful()){
            return "redirect:/error";
        }
        return "redirect:/chart-class";

    }

    @GetMapping("/delete/{id}")
    public String deleteChartClass(@PathVariable Long id) {
        restTemplate.delete(baseUrl + "?oid=" + id);
        return "redirect:/chart-class";
    }
}

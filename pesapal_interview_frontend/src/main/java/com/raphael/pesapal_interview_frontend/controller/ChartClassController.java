package com.raphael.pesapal_interview_frontend.controller;

import com.raphael.pesapal_interview_frontend.dto.ChartClassDTOs;
import com.raphael.pesapal_interview_frontend.dto.RegisterChartClassRequest;
import com.raphael.pesapal_interview_frontend.dto.UpdateChartClassRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

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
        model.addAttribute("registerChartClassRequest", registerChartClassRequest);
        return "AddChartClass";
    }

    @PostMapping("/addChartClass")
    public String addChartClass(RegisterChartClassRequest registerChartClassRequest) {
       var response = restTemplate.postForObject(url, new HashSet<>(List.of(registerChartClassRequest)), ChartClassDTOs.ChartClassResponse[].class);
       return "redirect:/chartClasses";
    }

    @GetMapping("/update/{id}")
    public String showUpdateChartClass(@PathVariable Long id, Model model) {
        var finalUrl = url + "?oid=" + id;
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
        var classTypes = restTemplate.getForObject(url + "/classTypes", map.getClass());
        model.addAttribute("classTypes", classTypes);

        model.addAttribute("inactive", List.of(Boolean.TRUE, Boolean.FALSE));

         return "update-chart-class";
    }

    @PostMapping("/update/{id}")
    public String updateChartClass(@PathVariable Long id, UpdateChartClassRequest  updateChartClassRequest) {
        updateChartClassRequest.setOid(id);
        var response = restTemplate.postForEntity(url + "/update", new HashSet<>(List.of(updateChartClassRequest)), ChartClassDTOs.ChartClassResponse[].class);
        if (!response.getStatusCode().is2xxSuccessful()){
            return "redirect:/error";
        }
        return "redirect:/chartClasses";

    }

    @GetMapping("/delete/{id}")
    public String deleteChartClass(@PathVariable Long id) {
        restTemplate.delete(url + "?oid=" + id);
        return "redirect:/chartClasses";
    }
}

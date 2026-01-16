package com.raphael.pesapal_interview_frontend.controller;

import com.raphael.pesapal_interview_frontend.dto.ChartClassDTOs;
import com.raphael.pesapal_interview_frontend.dto.ChartTypeDTOs.*;
import com.raphael.pesapal_interview_frontend.dto.UpdateChartTypesRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/chart-type")
public class ChartTypeController {

    private final RestTemplate restTemplate = new RestTemplate();


    @Value("${backend.api-endpoint}/chartType")
    private String baseUrl;

    @Value("${backend.api-endpoint}/chartClass")
    private String chartClassUrl;



    @GetMapping
    public String chartType(Model model) {


        var chartTypes = restTemplate.getForEntity(baseUrl, ChartTypeResponse[].class);
        if (!chartTypes.getStatusCode().is2xxSuccessful()) {
            return "redirect:/error";
        }
        model.addAttribute("chartTypes", chartTypes.getBody());
        return "chart-type";
    }

    @GetMapping("/add")
    public String showAddChartType(RegisterChartTypesRequest registerChartTypesRequest,Model model) {
        model.addAttribute("registerChartTypesRequest", registerChartTypesRequest);

        var chartClasses = restTemplate.getForEntity(chartClassUrl, ChartClassDTOs.ChartClassResponse[].class);
        if (!chartClasses.getStatusCode().is2xxSuccessful()) {
            return "redirect:/error";
        }
        model.addAttribute("chartClasses", chartClasses.getBody());

        var chartTypes = restTemplate.getForEntity(baseUrl, ChartTypeResponse[].class);
        if (!chartTypes.getStatusCode().is2xxSuccessful()) {
            return "redirect:/error";
        }
        assert chartTypes.getBody() != null;
        var chartTypeList = new ArrayList<>(Arrays.asList(chartTypes.getBody()));
        chartTypeList.add(new ChartTypeResponse(0L,"None",null,null,null,null,null,null));
        model.addAttribute("chartTypes",chartTypeList);


        return "create-chart-type";
    }

    @PostMapping("/add")
    public String addChartType(@Valid RegisterChartTypesRequest registerChartTypesRequest) {
        var response = restTemplate.postForEntity(baseUrl,new HashSet<>(List.of(registerChartTypesRequest)), ChartTypeResponse[].class);
        if (response.getStatusCode().value() != HttpStatus.CREATED.value()){
            return "redirect:/error";
        }
        return "redirect:/chart-type";
    }

    @GetMapping("/update/{id}")
    public String showUpdateChartType(@PathVariable Long id, Model model) {
        var chartTypeResponse = restTemplate.getForEntity(baseUrl +"?oid=" + id, ChartTypeResponse[].class);
        if (!chartTypeResponse.getStatusCode().is2xxSuccessful()) {
            return "redirect:/error";
        }
        assert chartTypeResponse.getBody() != null;
        var chartType = Arrays.stream(chartTypeResponse.getBody()).toList().getFirst();
        var updateChartTypeRequest = new UpdateChartTypesRequest();
        updateChartTypeRequest.setOid(id);
        updateChartTypeRequest.setChartTypeName(chartType.chartTypeName());
        updateChartTypeRequest.setChartTypeCode(chartType.chartTypeCode());
        updateChartTypeRequest.setChartClassId(chartType.chartClassId());
        updateChartTypeRequest.setParentId(chartType.parentId());
        updateChartTypeRequest.setInactive(chartType.inactive());

        model.addAttribute("updateChartTypesRequest", updateChartTypeRequest);


        var chartClasses = restTemplate.getForEntity(chartClassUrl, ChartClassDTOs.ChartClassResponse[].class);
        if (!chartClasses.getStatusCode().is2xxSuccessful()) {
            return "redirect:/error";
        }
        model.addAttribute("chartClasses", chartClasses.getBody());


        var chartTypes = restTemplate.getForEntity(baseUrl, ChartTypeResponse[].class);
        if (!chartTypes.getStatusCode().is2xxSuccessful()) {
            return "redirect:/error";
        }
        assert chartTypes.getBody() != null;
        var chartTypeList = new ArrayList<>(Arrays.asList(chartTypes.getBody()));
        chartTypeList.add(new ChartTypeResponse(0L,"None",null,null,null,null,null,null));
        model.addAttribute("chartTypes",chartTypeList);

        model.addAttribute("inactive", List.of(Boolean.TRUE, Boolean.FALSE));

        return "update-chart-type";
    }


    @PostMapping("/update/{id}")
    public String updateChartType(@PathVariable Long id,UpdateChartTypesRequest updateChartTypesRequest) {
        updateChartTypesRequest.setOid(id);
        var response = restTemplate.postForEntity(baseUrl + "/update",new HashSet<>(List.of(updateChartTypesRequest)), ChartTypeResponse[].class);
        if (response.getStatusCode().value() != HttpStatus.OK.value()){
            return "redirect:/error";
        }
        return "redirect:/chart-type";
    }

    @GetMapping("/delete/{id}")
    public String deleteChartType(@PathVariable Long id) {
        restTemplate.delete(baseUrl +"?oid=" + id);
        return "redirect:/chart-type";
    }

}

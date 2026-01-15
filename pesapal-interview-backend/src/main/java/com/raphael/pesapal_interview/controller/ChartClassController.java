package com.raphael.pesapal_interview.controller;

import com.raphael.pesapal_interview.dto.ChartClassDTOs.*;
import com.raphael.pesapal_interview.service.ChartClassService;
import com.raphael.pesapal_interview.utilities.OptionalNotBlank;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chartClass")
public class ChartClassController {


    private final ChartClassService chartClassService;

    public ChartClassController(ChartClassService chartClassService) {
        this.chartClassService = chartClassService;
    }

    @GetMapping("/classTypes")
    public ResponseEntity<Map<String, String>> getChartClassTypes() {
        var response = Arrays.stream(ChartClassTypeEnum.values()).collect(Collectors.toMap(ChartClassTypeEnum::getLabel, ChartClassTypeEnum::getDescription));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<ChartClassResponse>> getChartClass(
            @Min(value = 1L, message = "Please provide a valid chart class id")
            @RequestParam(name = "oid", required = false) Long oid,
            @OptionalNotBlank
            @RequestParam(name = "className", required = false) String className,
            @OptionalNotBlank
            @RequestParam(name = "classCode", required = false) String classCode,
            @RequestParam(name = "classType", required = false) ChartClassTypeEnum classType,
            @RequestParam(name = "inactive", required = false) Boolean inactive,
            @RequestParam(name = "pageNumber", required = true, defaultValue = "0")
            @NotNull(message = "Please provide a pageNumber")
            @Min(value = 0, message = "Please provide a valid page number") int pageNumber,
            @NotNull(message = "Please provide a pageSize")
            @Min(value = 1, message = "Please provide a valid pageSize")
            @RequestParam(name = "pageSize", required = true, defaultValue ="20") int pageSize
    ) {
        var getChartClassDTO = GetChartClassRequest.builder().oid(oid).classCode(classCode).className(className).classType(classType).inactive(inactive).pageNumber(pageNumber).pageSize(pageSize).build();
        return new ResponseEntity<>(chartClassService.getChartClass(getChartClassDTO), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Set<ChartClassResponse>> createChartClass(@RequestBody @NotEmpty Set<@Valid RegisterChartClassRequest> registerChartClassRequests){
        return new ResponseEntity<>(chartClassService.registerChartClass(registerChartClassRequests), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<Set<ChartClassResponse>> updateChartClass(@RequestBody @NotEmpty Set<@Valid UpdateChartClassRequest> updateChartClassRequests){
        return new ResponseEntity<>(chartClassService.updateChartClass(updateChartClassRequests), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteChartClass(@RequestParam(name = "oid") Set<Long> oid){
        chartClassService.deleteChartClass(oid.parallelStream().map(id -> DeleteChartClassRequest.builder().oid(id).build()).collect(Collectors.toSet()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

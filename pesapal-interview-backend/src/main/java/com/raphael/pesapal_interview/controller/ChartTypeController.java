package com.raphael.pesapal_interview.controller;

import com.raphael.pesapal_interview.dto.ChartTypeDTOs;
import com.raphael.pesapal_interview.dto.ChartTypeDTOs.UpdateChartTypesRequest;
import com.raphael.pesapal_interview.dto.ChartTypeDTOs.ChartTypeResponse;
import com.raphael.pesapal_interview.dto.ChartTypeDTOs.GetChartTypesRequest;
import com.raphael.pesapal_interview.dto.ChartTypeDTOs.RegisterChartTypesRequest;
import com.raphael.pesapal_interview.service.ChartTypeService;
import com.raphael.pesapal_interview.utilities.OptionalNotBlank;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("chartType")
public class ChartTypeController {

    private final ChartTypeService chartTypeService;

    public ChartTypeController(ChartTypeService chartTypeService) {
        this.chartTypeService = chartTypeService;
    }

    @GetMapping
    public ResponseEntity<Set<ChartTypeResponse>> getChartType(
            @Min(value = 1L)
            @RequestParam(name = "oid", required = false) Long oid,
            @OptionalNotBlank
            @RequestParam(name = "chartTypeName", required = false) String chartTypeName,
            @OptionalNotBlank
            @RequestParam(name = "typeCode", required = false) String chartTypeCode,
            @Min(value = 1L, message = "Please provide a valid chart class id")
            @RequestParam(name = "chartClassId", required = false) Long chartClassId,
            @Min(value = 1L, message = "Please provide a valid chart type parent id")
            @RequestParam(name = "parentId", required = false) Long parentId,
            @RequestParam(name = "inactive", required = false) Boolean inactive,
            @RequestParam(name = "pageNumber", required = true, defaultValue = "0")
            @NotNull(message = "Please provide a pageNumber")
            @Min(value = 0, message = "Please provide a valid page number") Integer pageNumber,
            @NotNull(message = "Please provide a pageSize")
            @Min(value = 1, message = "Please provide a valid pageSize")
            @RequestParam(name = "pageSize", required = true, defaultValue = "20") Integer pageSize
    ) {
        var getChartTypeDTO = GetChartTypesRequest.builder().oid(oid).chartTypeName(chartTypeName).chartTypeCode(chartTypeCode).chartClassId(chartClassId).parentId(parentId).inactive(inactive).pageNumber(pageNumber).pageSize(pageSize).build();
        return new ResponseEntity<>(chartTypeService.getChartTypes(getChartTypeDTO), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Set<ChartTypeResponse>> registerChartType(@RequestBody @NotEmpty Set<@Valid RegisterChartTypesRequest> registerChartTypesDTOS) {
        return new ResponseEntity<>(chartTypeService.registerChartType(registerChartTypesDTOS), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Set<ChartTypeResponse>> updateChartType(@RequestBody @NotEmpty Set<@Valid UpdateChartTypesRequest> updateChartTypesDTOS) {
        return new ResponseEntity<>(chartTypeService.updateChartType(updateChartTypesDTOS), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteChartType(
            @RequestParam(name = "oid", required = true) List<Long> deleteChartTypeIDs
    ) {
        chartTypeService.deleteChartType(deleteChartTypeIDs.parallelStream().map(id -> ChartTypeDTOs.DeleteChartTypeRequest.builder().oid(id).build()).toList());
        return new ResponseEntity<>( HttpStatus.OK);
    }
}

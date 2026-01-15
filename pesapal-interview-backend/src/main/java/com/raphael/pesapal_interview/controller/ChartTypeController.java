package com.raphael.pesapal_interview.controller;

import com.raphael.pesapal_interview.dto.ChartTypeDTOs;
import com.raphael.pesapal_interview.service.ChartTypeService;
import com.raphael.pesapal_interview.utilities.OptionalNotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Set<ChartTypeDTOs.ChartTypeResponse>> getChartType(
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
        var getChartTypeDTO = ChartTypeDTOs.GetChartTypesRequest.builder().oid(oid).chartTypeName(chartTypeName).chartTypeCode(chartTypeCode).chartClassId(chartClassId).parentId(parentId).inactive(inactive).pageNumber(pageNumber).pageSize(pageSize).build();
        return new ResponseEntity<>(chartTypeService.getChartTypes(getChartTypeDTO), HttpStatus.OK);
    }
}

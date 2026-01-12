package com.raphael.pesapal_interview;

import com.raphael.pesapal_interview.dto.ChartTypeDTOs;
import com.raphael.pesapal_interview.service.ChartClassService;
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

@RestController
@RequestMapping("/chartClass")
public class ChartClassController {


    private final ChartClassService chartClassService;

    public ChartClassController(ChartClassService chartClassService) {
        this.chartClassService = chartClassService;
    }

    @GetMapping
    public ResponseEntity<List<ChartTypeDTOs.ChartClassResponse>> getChartClass(
            @Min(value = 1L, message = "Please provide a valid chart class id")
            @RequestParam(name = "oid", required = false) Long oid,
            @OptionalNotBlank
            @RequestParam(name = "className", required = false) String className,
            @OptionalNotBlank
            @RequestParam(name = "classCode", required = false) String classCode,
            @RequestParam(name = "classType", required = false) ChartTypeDTOs.ChartClassTypeEnum classType,
            @RequestParam(name = "inactive", required = false) Boolean inactive,
            @RequestParam(name = "pageNumber", required = true, defaultValue = "0")
            @NotNull(message = "Please provide a pageNumber")
            @Min(value = 0, message = "Please provide a valid page number") int pageNumber,
            @NotNull(message = "Please provide a pageSize")
            @Min(value = 1, message = "Please provide a valid pageSize")
            @RequestParam(name = "pageSize", required = true, defaultValue ="20") int pageSize
    ) {
        var getChartClassDTO = ChartTypeDTOs.GetChartClassRequest.builder().oid(oid).classCode(classCode).className(className).classType(classType).inactive(inactive).pageNumber(pageNumber).pageSize(pageSize).build();
        return new ResponseEntity<>(chartClassService.getChartClass(getChartClassDTO), HttpStatus.OK);
    }

}

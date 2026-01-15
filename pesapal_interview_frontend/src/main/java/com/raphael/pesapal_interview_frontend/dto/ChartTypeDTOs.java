package com.raphael.pesapal_interview_frontend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class ChartTypeDTOs {
    @Builder
    public record ChartTypeResponse(
            Long oid,
            String chartTypeName,
            String chartTypeCode,
            Long chartClassId,
            String chartClassName,
            Long parentId,
            String parentName,
            Boolean inactive
    ) {

    }
    @Builder
    public record GetChartTypesRequest(
            @Min(value = 1L, message = "Please provide a valid chart type id")
            Long oid,
            @NotBlank(message = "Chart Type Name when provided cannot be blank")
            String chartTypeName,
            @NotBlank(message = "Chart Type Code when provided cannot be blamk")
            String chartTypeCode,
            @Min(value = 1L, message = "Please provide a valid chart class id")
            Long chartClassId,
            @Min(value = 0L, message = "Please provide a valid chart type parent id")
            Long parentId,
            Boolean inactive,
            @NotNull(message = "Please provide a pageNumber")
            @Min(value = 0, message = "Please provide a valid page number")
            int pageNumber,
            @NotNull(message = "Please provide a pageNumber")
            @Min(value = 1, message = "Please provide a valid page size")
            int pageSize) {

    }

    @Builder
    public record RegisterChartTypesRequest(
            @NotNull(message = "Chart Type name cannot be null")
            @NotBlank(message = "Please provide a valid chart type name")
            String chartTypeName,
            @NotNull(message = "Chart Type code cannot be null")
            @NotBlank(message = "Please provide a type code")
            String chartTypeCode,
            @NotNull(message = "Please provide a chart class id")
            @Min(value = 1L, message = "Please provide a valid chart class id")
            Long chartClassId,
            @NotNull(message = "Chart Type Parent Id cannot be null")
            @Min(value = 0L, message = "Please provide a valid parent id")
            Long parentId,
            @NotNull(message = "Username cannot be null")
            @NotBlank(message = "Username cannot be blank")
            String userName
    ) {

    }

    @Builder
    public record DeleteChartTypeRequest(
            @NotNull(message = "Please provide a chart type id")
            @Min(value = 1L, message = "Please provide a valid chart type id")
            Long oid) {

    }
}

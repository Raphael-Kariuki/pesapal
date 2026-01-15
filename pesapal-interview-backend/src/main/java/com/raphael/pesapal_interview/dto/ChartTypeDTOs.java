package com.raphael.pesapal_interview.dto;

import com.raphael.pesapal_interview.utilities.OptionalNotBlank;
import jakarta.validation.constraints.Min;
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
            @OptionalNotBlank(message = "Chart Type Name when provided cannot be blank")
            String chartTypeName,
            @OptionalNotBlank(message = "Chart Type Code when provided cannot be blamk")
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
}

package com.raphael.pesapal_interview.dto;

import com.raphael.pesapal_interview.utilities.OptionalNotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class ChartTypeDTOs {

    public enum ChartClassTypeEnum {
        BA("BA","BALANCE_SHEET_ASSETS"),
        BL("BL","BALANCE_SHEET_LIABILITIES"),
        BE("BE","BALANCE_SHEET_EQUITY"),
        PI("PI","PROFIT_LOSS_INCOME"),
        PE("PE","PROFIT_LOSS_EXPENSES");

        @Getter
        private final String label;
        @Getter
        private final String description;

        ChartClassTypeEnum(String label, String description) {
            this.label = label;
            this.description = description;
        }
    }

    @Builder
    public record GetChartClassRequest(
            @OptionalNotBlank
            Long oid,
            @OptionalNotBlank
            String className,
            @OptionalNotBlank
            String classCode,
            ChartClassTypeEnum classType,
            Boolean inactive,
            @NotNull(message = "Please provide a pageNumber")
            @Min(value = 0, message = "Please provide a valid page number")
            int pageNumber,
            @NotNull(message = "Please provide a pageNumber")
            @Min(value = 1, message = "Please provide a valid page size")
            int pageSize) {

    }

    @Builder
    public record GetChartTypeRequest(
            @Min(value = 1L, message = "Please provide a valid chart type id")
            Long oid,
            @OptionalNotBlank
            String chartTypeName,
            @OptionalNotBlank
            String chartTypeCode,
            @Min(value = 1L, message = "Please provide a valid chart class id")
            Long chartClassId,
            @Min(value = 0L, message = "Please provide a valid chart type parent id")
            Long parentId,
            Boolean inactive,
            @NotNull(message = "Please provide a pageNumber")
            @Min(value = 0, message = "Please provide a valid page number")
            Integer pageNumber,
            @NotNull(message = "Please provide a pageNumber")
            @Min(value = 1, message = "Please provide a valid page size")
            Integer pageSize) {

    }

    @Builder
    public record ChartClassResponse(
            Long oid,
            String className,
            String classCode,
            String classType,
            Boolean inactive) {

    }
}

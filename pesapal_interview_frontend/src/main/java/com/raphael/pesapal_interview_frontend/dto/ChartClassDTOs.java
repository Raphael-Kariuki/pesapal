package com.raphael.pesapal_interview_frontend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class ChartClassDTOs {
    @Builder
    public record ChartClassResponse(
            Long oid,
            String className,
            String classCode,
            ChartClassTypeEnum classType,
            Boolean inactive) {

    }

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
    public record UpdateChartClassRequest(
            @NotNull(message = "Please provide a valid chart class id")
            @Min(value = 1L, message = "Please provide a valid chart class id")
            Long oid,
            @NotBlank
            String className,
            @NotBlank
            String classCode,
            ChartClassTypeEnum classType,
            Boolean inactive,
            @NotNull
            @NotBlank
            String updateUser) {

    }
}

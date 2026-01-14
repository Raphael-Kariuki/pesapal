package com.raphael.pesapal_interview_frontend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RegisterChartClassRequest {
    @NotBlank(message = "Please provide a valid chart class name")
    String className;
    @NotBlank(message = "Please provide a valid chart class name")
    String classCode;
    ChartClassDTOs.ChartClassTypeEnum chartClassType;
    @NotNull
    @NotBlank
    String userName;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public ChartClassDTOs.ChartClassTypeEnum getChartClassType() {
        return chartClassType;
    }

    public void setChartClassType(String chartClassType) {
        this.chartClassType = ChartClassDTOs.ChartClassTypeEnum.valueOf(chartClassType);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

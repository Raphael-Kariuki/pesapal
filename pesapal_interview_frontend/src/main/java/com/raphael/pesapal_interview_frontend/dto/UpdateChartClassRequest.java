package com.raphael.pesapal_interview_frontend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateChartClassRequest {
    @NotNull(message = "id must not be null")
    @Min(value = 1L, message = "Id must be greater than 1")
    Long oid;
    @NotNull(message = "Class name must not be null")
    @NotBlank(message = "Class name must not be blank")
    String className;
    @NotNull(message = "Class code must not be null")
    @NotBlank(message = "Class code must not be blank")
    String classCode;
    @NotNull(message = "Class type must be null")
    ChartClassDTOs.ChartClassTypeEnum classType;
    @NotNull(message = "Class status must not be null")
    Boolean inactive;
    @NotNull(message = "Update user must not be null")
    @NotBlank(message = "Update user must not be blank")
    String updateUser;

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

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

    public ChartClassDTOs.ChartClassTypeEnum getClassType() {
        return classType;
    }

    public void setClassType(ChartClassDTOs.ChartClassTypeEnum classType) {
        this.classType = classType;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }
}

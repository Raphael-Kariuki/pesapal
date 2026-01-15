package com.raphael.pesapal_interview_frontend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateChartTypesRequest{
    @NotNull(message = "Please provide a chart type id")
    @Min(value = 1L, message = "Please provide a valid chart type id")
    Long oid;
    @NotBlank(message = "Chart Type Name when provide cannot be blank")
    String chartTypeName;
    @NotBlank(message = "Chart Type Code when provide cannot be blank")
    String chartTypeCode;
    @Min(value = 1L, message = "Please provide a valid chart class id")
    Long chartClassId;
    @Min(value = 0L, message = "Please provide a valid parent id")
    Long parentId;
    Boolean inactive;
    @NotNull(message = "Update User cannot be null")
    @NotBlank(message = "Update User cannot be blank")
    String updateUser;

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public String getChartTypeName() {
        return chartTypeName;
    }

    public void setChartTypeName(String chartTypeName) {
        this.chartTypeName = chartTypeName;
    }

    public String getChartTypeCode() {
        return chartTypeCode;
    }

    public void setChartTypeCode(String chartTypeCode) {
        this.chartTypeCode = chartTypeCode;
    }

    public Long getChartClassId() {
        return chartClassId;
    }

    public void setChartClassId(Long chartClassId) {
        this.chartClassId = chartClassId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}

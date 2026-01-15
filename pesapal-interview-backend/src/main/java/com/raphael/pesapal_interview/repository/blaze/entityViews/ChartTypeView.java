package com.raphael.pesapal_interview.repository.blaze.entityViews;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.FetchStrategy;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.raphael.pesapal_interview.models.ChartType;

@EntityView(ChartType.class)
public interface ChartTypeView {
    @IdMapping
    Long getOid();

    String getChartTypeName();

    @Mapping("chartClass.oid")
    Long getChartClassId();

    @Mapping("chartClass.className")
    String getChartClassClassName();

    String getTypeCode();

    @Mapping(value = "parentId.oid")
    Long getParentId();

    @Mapping("parentId.chartTypeName")
    String getParentTypeName();

    @Mapping("commonEntityAttributes.inactive")
    Boolean getInactive();
}
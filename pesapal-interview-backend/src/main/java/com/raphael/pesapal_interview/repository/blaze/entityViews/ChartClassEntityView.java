package com.raphael.pesapal_interview.repository.blaze.entityViews;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.Mapping;
import com.raphael.pesapal_interview.models.ChartClass;

@EntityView(ChartClass.class)
public interface ChartClassEntityView {
    Long getOid();
    String getClassName();
    String getClassCode();
    String getClassType();

    @Mapping("commonEntityAttributes.inactive")
    Boolean getInactive();
}

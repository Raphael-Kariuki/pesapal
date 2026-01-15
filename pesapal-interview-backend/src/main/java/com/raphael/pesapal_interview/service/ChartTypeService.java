package com.raphael.pesapal_interview.service;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.raphael.pesapal_interview.dto.ChartTypeDTOs.*;
import com.raphael.pesapal_interview.models.ChartType;
import com.raphael.pesapal_interview.repository.ChartClassesRepository;
import com.raphael.pesapal_interview.repository.blaze.entityViews.ChartTypeView;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChartTypeService {

    private final ChartClassesRepository chartClassesRepository;
    private final EntityManager entityManager;
    private final CriteriaBuilderFactory criteriaBuilderFactory;
    private final EntityViewManager entityViewManager;

    public ChartTypeService(ChartClassesRepository chartClassesRepository, EntityManager entityManager, CriteriaBuilderFactory criteriaBuilderFactory, EntityViewManager entityViewManager) {
        this.chartClassesRepository = chartClassesRepository;
        this.entityManager = entityManager;
        this.criteriaBuilderFactory = criteriaBuilderFactory;
        this.entityViewManager = entityViewManager;
    }


    @Transactional
    public Set<ChartTypeResponse> getChartTypes(@Valid GetChartTypesRequest getChartTypesRequest) {
        var cb = criteriaBuilderFactory.create(entityManager, ChartType.class, "ct");

        if (getChartTypesRequest.oid() != null) {
            cb.where("ct.oid").eq(getChartTypesRequest.oid());
        }
        if (getChartTypesRequest.chartTypeName() != null) {
            cb.where("ct.chartTypeName").like(Boolean.FALSE).value(getChartTypesRequest.chartTypeName()).noEscape();
        }
        if (getChartTypesRequest.chartTypeCode() != null) {
            cb.where("ct.chartTypeCode").like(Boolean.FALSE).value(getChartTypesRequest.chartTypeCode()).noEscape();
        }
        if (getChartTypesRequest.chartClassId() != null) {
            cb.where("ct.chartClass.oid").eq(getChartTypesRequest.chartClassId());
        }
        if (getChartTypesRequest.parentId() != null) {
            cb.where("ct.parentId.oid").eq(getChartTypesRequest.parentId());
        }
        if (getChartTypesRequest.inactive() != null) {
            cb.where("ct.commonEntityAttributes.inactive").eq(getChartTypesRequest.inactive());
        }

        var entityView = entityViewManager.applySetting(EntityViewSetting.create(ChartTypeView.class), cb);
        var pcb = entityView.page(getChartTypesRequest.pageNumber(), getChartTypesRequest.pageSize()).orderByAsc("oid");
        return pcb.getResultList().stream().map(view -> ChartTypeResponse.builder().oid(view.getOid()).chartTypeName(view.getChartTypeName()).chartTypeCode(view.getTypeCode()).chartClassId(view.getChartClassId()).chartClassName(view.getChartClassClassName()).parentId(view.getParentId()).parentName(view.getParentTypeName()).inactive(view.getInactive()).build()).collect(Collectors.toSet());

    }
}

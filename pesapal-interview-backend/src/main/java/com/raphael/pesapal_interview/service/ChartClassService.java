package com.raphael.pesapal_interview.service;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.raphael.pesapal_interview.dto.ChartTypeDTOs;
import com.raphael.pesapal_interview.models.ChartClass;
import com.raphael.pesapal_interview.repository.ChartClassesRepository;
import com.raphael.pesapal_interview.repository.blaze.entityViews.ChartClassEntityView;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class ChartClassService {

    private final ChartClassesRepository chartClassesRepository;
private final EntityManager entityManager;
    private final CriteriaBuilderFactory criteriaBuilderFactory;
    private final EntityViewManager entityViewManager;

    public ChartClassService(ChartClassesRepository chartClassesRepository, EntityManager entityManager, CriteriaBuilderFactory criteriaBuilderFactory, EntityViewManager entityViewManager) {
        this.chartClassesRepository = chartClassesRepository;
        this.entityManager = entityManager;
        this.criteriaBuilderFactory = criteriaBuilderFactory;
        this.entityViewManager = entityViewManager;
    }


    @Transactional
    public List<ChartTypeDTOs.ChartClassResponse> getChartClass(@Valid ChartTypeDTOs.GetChartClassRequest getChartClassesDTO) {


var cb = criteriaBuilderFactory.create(entityManager, ChartClass.class, "cc");
        if (getChartClassesDTO.oid() != null) {
            cb.where("cc.oid").eq(getChartClassesDTO.oid());
        }
        if (getChartClassesDTO.classCode() != null) {
            cb.where("cc.classCode").eq(getChartClassesDTO.classCode());
        }
        if (getChartClassesDTO.className() != null) {
            cb.where("cc.className").eq(getChartClassesDTO.className());
        }
        if (getChartClassesDTO.classType() != null) {
            cb.where("cc.classType").eq(getChartClassesDTO.classType().getLabel());
        }
        if (getChartClassesDTO.inactive() != null) {
            cb.where("cc.commonEntityAttributes.inactive").eq(getChartClassesDTO.inactive());
        }else{
           cb.where("cc.commonEntityAttributes.inactive").eq(false);
        }

        var view = entityViewManager.applySetting(EntityViewSetting.create(ChartClassEntityView.class), cb);
        var pcb = view.page(getChartClassesDTO.pageNumber(), getChartClassesDTO.pageSize()).orderByAsc("oid");
        return pcb.getResultList().stream().parallel().map(chartClass -> ChartTypeDTOs.ChartClassResponse.builder()
                .oid(chartClass.getOid())
                .classCode(chartClass.getClassCode())
                .className(chartClass.getClassName())
                .classType(chartClass.getClassType())
                .inactive(chartClass.getInactive())
                .build()).toList();
    }

}

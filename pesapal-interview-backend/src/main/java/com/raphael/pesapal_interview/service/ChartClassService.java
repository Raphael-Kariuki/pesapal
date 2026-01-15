package com.raphael.pesapal_interview.service;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.raphael.pesapal_interview.dto.ChartClassDTOs.*;
import com.raphael.pesapal_interview.models.ChartClass;
import com.raphael.pesapal_interview.models.CommonEntityAttributes;
import com.raphael.pesapal_interview.repository.ChartClassesRepository;
import com.raphael.pesapal_interview.repository.blaze.entityViews.ChartClassEntityView;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public List<ChartClassResponse> getChartClass(@Valid GetChartClassRequest getChartClassesDTO) {


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
        }
//        else{
//           cb.where("cc.commonEntityAttributes.inactive").eq(false);
//        }

        var view = entityViewManager.applySetting(EntityViewSetting.create(ChartClassEntityView.class), cb);
        var pcb = view.page(getChartClassesDTO.pageNumber(), getChartClassesDTO.pageSize()).orderByAsc("oid");
        return pcb.getResultList().stream().parallel().map(chartClass -> ChartClassResponse.builder()
                .oid(chartClass.getOid())
                .classCode(chartClass.getClassCode())
                .className(chartClass.getClassName())
                .classType(chartClass.getClassType())
                .inactive(chartClass.getInactive())
                .build()).toList();
    }

    @Transactional
    public Set<ChartClassResponse> registerChartClass(@NotEmpty Set<@Valid RegisterChartClassRequest> registerChartClassDTOS) {

        return chartClassesRepository.saveAllAndFlush(registerChartClassDTOS
                        .parallelStream()
                        .map(dto -> ChartClass.builder()
                                .classCode(dto.classCode())
                                .className(dto.className())
                                .classType(dto.chartClassType())
                                .commonEntityAttributes(CommonEntityAttributes.builder()
                                        .inactive(Boolean.FALSE)
                                        .userId(dto.userName())
                                        .build())
                                .build()).toList())
                .parallelStream()
                .map(chartClass -> ChartClassResponse.builder()
                        .oid(chartClass.getOid())
                        .classCode(chartClass.getClassCode())
                        .className(chartClass.getClassName())
                        .classType(chartClass.getClassType())
                        .inactive(chartClass.getCommonEntityAttributes().getInactive())
                        .build()).collect(Collectors.toSet());
    }


    @Transactional
    public Set<ChartClassResponse> updateChartClass(@NotEmpty Set<@Valid UpdateChartClassRequest> updateChartClassDTOS) {


        var dtoMap = updateChartClassDTOS.parallelStream().collect(Collectors.toMap(UpdateChartClassRequest::oid, dto -> dto, (existing, replacement) -> existing, HashMap::new));
        var entityMap = chartClassesRepository.findAllById(dtoMap.keySet()).parallelStream().collect(Collectors.toMap(ChartClass::getOid, entity -> entity, (existing, replacement) -> existing, HashMap::new));

        entityMap.forEach((id, entity) -> {
            var dto = dtoMap.get(id);
            if (dto != null) {
                if (dto.classCode() != null && !dto.classCode().equals(entity.getClassCode())) {
                    entity.setClassCode(dto.classCode());
                }
                if (dto.className() != null) {
                    entity.setClassName(dto.className());
                }
                if (dto.classType() != null){
                    entity.setClassType(dto.classType());
                }
                var common = entity.getCommonEntityAttributes();
                if (dto.inactive() != null) {
                    common.setInactive(dto.inactive());
                }
                common.setUpdateUser(dto.updateUser());
                entity.setCommonEntityAttributes(common);
            }
        });

        return chartClassesRepository.saveAllAndFlush(entityMap.values()).parallelStream()
                .map(chartClass -> ChartClassResponse.builder()
                        .oid(chartClass.getOid())
                        .classCode(chartClass.getClassCode())
                        .className(chartClass.getClassName())
                        .classType(chartClass.getClassType())
                        .inactive(chartClass.getCommonEntityAttributes().getInactive())
                        .build()).collect(Collectors.toSet());
    }

    @Transactional
    public void deleteChartClass(@NotEmpty Set<@Valid DeleteChartClassRequest> deleteChartClassDTOS) {
        chartClassesRepository.deleteAllById(deleteChartClassDTOS.parallelStream().map(DeleteChartClassRequest::oid).toList());
    }

}

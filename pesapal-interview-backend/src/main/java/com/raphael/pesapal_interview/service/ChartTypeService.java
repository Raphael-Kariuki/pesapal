package com.raphael.pesapal_interview.service;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.raphael.pesapal_interview.dto.ChartTypeDTOs.DeleteChartTypeRequest;
import com.raphael.pesapal_interview.dto.ChartTypeDTOs.UpdateChartTypesRequest;
import com.raphael.pesapal_interview.dto.ChartTypeDTOs.ChartTypeResponse;
import com.raphael.pesapal_interview.dto.ChartTypeDTOs.GetChartTypesRequest;
import com.raphael.pesapal_interview.dto.ChartTypeDTOs.RegisterChartTypesRequest;
import com.raphael.pesapal_interview.exceptions.BadRequestException;
import com.raphael.pesapal_interview.models.ChartClass;
import com.raphael.pesapal_interview.models.ChartType;
import com.raphael.pesapal_interview.models.CommonEntityAttributes;
import com.raphael.pesapal_interview.repository.ChartTypesRepository;
import com.raphael.pesapal_interview.repository.blaze.entityViews.ChartTypeView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChartTypeService {

    private final ChartTypesRepository chartTypesRepository;
    private final EntityManager entityManager;
    private final CriteriaBuilderFactory criteriaBuilderFactory;
    private final EntityViewManager entityViewManager;

    public ChartTypeService(ChartTypesRepository chartTypesRepository, EntityManager entityManager, CriteriaBuilderFactory criteriaBuilderFactory, EntityViewManager entityViewManager) {
        this.chartTypesRepository = chartTypesRepository;
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

    @Transactional
    public Set<ChartTypeResponse> registerChartType(@NotEmpty(message = "Register chart type payload cannot be empty") Set<@Valid RegisterChartTypesRequest> registerChartTypesRequests){
        //validate chartClass ids
        var chartClassIds =  registerChartTypesRequests.stream()
                .map(RegisterChartTypesRequest::chartClassId).collect(Collectors.toSet());
        var chartClasses = criteriaBuilderFactory.create(entityManager, Tuple.class)
                .from(ChartClass.class, "cc")
                .where("cc.oid").in(chartClassIds)
                .select("cc.oid", "chartClassId")
                .select("cc.className", "chartClassName")
                .select("cc.commonEntityAttributes.inactive", "inactive").getResultList();
        chartClassIds.removeAll(chartClasses.stream().map(chartClass -> chartClass.get("chartClassId", Long.class)).collect(Collectors.toSet()));
        if (!chartClassIds.isEmpty()) {
            throw new BadRequestException("Invalid chart class ids " +  chartClassIds);
        }
        chartClasses.forEach(chartClass -> {
            if (chartClass.get("inactive", Boolean.class)){
                throw new BadRequestException("Inactive chart class  " + chartClass.get("chartClassName", String.class));
            }
        });

        //validate parent ids
        var parentIds = registerChartTypesRequests.stream()
                .filter(Objects::nonNull)
                .map(RegisterChartTypesRequest::parentId)
                .filter(id -> !Objects.equals(0L, id))
                .collect(Collectors.toSet());
        var parents = criteriaBuilderFactory.create(entityManager, Tuple.class)
                .from(ChartType.class, "ct")
                .where("ct.oid").in(parentIds)
                .select("ct.oid", "oid")
                .select("ct.chartTypeName", "chartTypeName")
                .select("ct.commonEntityAttributes.inactive", "inactive").getResultList();
        parentIds.removeAll(parents.parallelStream().map(tuple -> tuple.get("oid", Long.class)).collect(Collectors.toSet()));
        if (!parentIds.isEmpty()) {
            throw new BadRequestException("Invalid chart type parent ids " +  parentIds);
        }
        parents.forEach(parent -> {
            if (parent.get("inactive", Boolean.class)){
                throw new BadRequestException("Inactive parent chart type " + parent.get("chartTypeName", String.class));
            }
        });

        var persisted = chartTypesRepository.saveAllAndFlush(registerChartTypesRequests.parallelStream()
                .map(dto -> ChartType.builder()
                .typeCode(dto.chartTypeCode())
                .chartTypeName(dto.chartTypeName())
                .chartClass(entityManager.getReference(ChartClass.class, dto.chartClassId()))
                .parentId(entityManager.getReference(ChartType.class, dto.parentId()))
                .commonEntityAttributes(CommonEntityAttributes.builder()
                        .inactive(Boolean.FALSE)
                        .userName(dto.userName())
                        .build())
                .build()).toList());

        var cb = criteriaBuilderFactory.create(entityManager, ChartType.class, "ct")
                .where("ct.oid").in(persisted.parallelStream().map(ChartType::getOid).collect(Collectors.toSet()));

        var views = entityViewManager.applySetting(EntityViewSetting.create(ChartTypeView.class), cb);

        return views.getResultList().stream().map(view -> ChartTypeResponse.builder().oid(view.getOid()).chartTypeName(view.getChartTypeName()).chartTypeCode(view.getTypeCode()).chartClassId(view.getChartClassId()).chartClassName(view.getChartClassClassName()).parentId(view.getParentId()).parentName(view.getParentTypeName()).inactive(view.getInactive()).build()).collect(Collectors.toSet());

    }

    @Transactional
    public Set<ChartTypeResponse> updateChartType(@NotEmpty(message = "Chart Type Update payload cannot be empty")Set<@Valid UpdateChartTypesRequest> updateChartTypesRequests){
        //validate chartClass ids
        var chartClassIds =  updateChartTypesRequests.stream()
                .map(UpdateChartTypesRequest::chartClassId).collect(Collectors.toSet());
        var chartClasses = criteriaBuilderFactory.create(entityManager, Tuple.class)
                .from(ChartClass.class, "cc")
                .where("cc.oid").in(chartClassIds)
                .select("cc.oid", "chartClassId")
                .select("cc.className", "chartClassName")
                .select("cc.commonEntityAttributes.inactive", "inactive").getResultList();
        chartClassIds.removeAll(chartClasses.stream().map(chartClass -> chartClass.get("chartClassId", Long.class)).collect(Collectors.toSet()));
        if (!chartClassIds.isEmpty()) {
            throw new BadRequestException("Invalid chart class ids " +  chartClassIds);
        }
        chartClasses.forEach(chartClass -> {
            if (chartClass.get("inactive", Boolean.class)){
                throw new BadRequestException("Inactive chart class  " + chartClass.get("chartClassName", String.class));
            }
        });

        //validate parent ids
        var parentIds = updateChartTypesRequests.stream()
                .filter(Objects::nonNull)
                .map(UpdateChartTypesRequest::parentId)
                .filter(id -> !Objects.equals(0L, id))
                .collect(Collectors.toSet());
        var parents = criteriaBuilderFactory.create(entityManager, Tuple.class)
                .from(ChartType.class, "ct")
                .where("ct.oid").in(parentIds)
                .select("ct.oid", "oid")
                .select("ct.chartTypeName", "chartTypeName")
                .select("ct.commonEntityAttributes.inactive", "inactive").getResultList();
        parentIds.removeAll(parents.parallelStream().map(tuple -> tuple.get("oid", Long.class)).collect(Collectors.toSet()));
        if (!parentIds.isEmpty()) {
            throw new BadRequestException("Invalid chart type parent ids " +  parentIds);
        }
        parents.forEach(parent -> {
            if (parent.get("inactive", Boolean.class)){
                throw new BadRequestException("Inactive parent chart type " + parent.get("chartTypeName", String.class));
            }
        });

        var dtoMap = updateChartTypesRequests.parallelStream().collect(Collectors.toMap(UpdateChartTypesRequest::oid, dto -> dto, (existing, replacement) -> existing, HashMap::new));
        var entityMap = chartTypesRepository.findAllById(dtoMap.keySet()).stream().collect(Collectors.toMap(ChartType::getOid, entity -> entity, (existing, replacement) -> existing, HashMap::new));

        entityMap.forEach((id, entity) -> {
            var dto = dtoMap.get(id);
            if (dto != null) {
                if (dto.chartTypeCode() != null && !dto.chartTypeCode().equals(entity.getTypeCode())) {
                    entity.setTypeCode(dto.chartTypeCode());
                }
                if (dto.chartTypeName() != null && !dto.chartTypeName().equals(entity.getChartTypeName())) {
                    entity.setChartTypeName(dto.chartTypeName());
                }
                if (dto.chartClassId() != null && !dto.chartClassId().equals(entity.getChartClass().getOid())) {
                    entity.setChartClass(entityManager.getReference(ChartClass.class, dto.chartClassId()));
                }
                if (dto.parentId() != null && !dto.parentId().equals(entity.getParentId().getOid())) {
                    if (dto.parentId().equals(dto.oid())){
                        throw new BadRequestException("Chart Type cannot be it's own parent");
                    }
                    entity.setParentId(entityManager.getReference(ChartType.class, dto.parentId()));
                }
                var common = entity.getCommonEntityAttributes();
                if (dto.inactive() != null) {
                    common.setInactive(dto.inactive());
                }
                common.setUpdateUser(dto.updateUser());
                entity.setCommonEntityAttributes(common);
            }
        });

        var updated = chartTypesRepository.saveAllAndFlush(entityMap.values());
        var cb = criteriaBuilderFactory.create(entityManager, ChartType.class, "ct")
                .where("ct.oid").in(updated.parallelStream().map(ChartType::getOid).collect(Collectors.toSet()));
        var views = entityViewManager.applySetting(EntityViewSetting.create(ChartTypeView.class), cb);
        return views.getResultList().stream().map(view -> ChartTypeResponse.builder().oid(view.getOid()).chartTypeName(view.getChartTypeName()).chartTypeCode(view.getTypeCode()).chartClassId(view.getChartClassId()).chartClassName(view.getChartClassClassName()).parentId(view.getParentId()).parentName(view.getParentTypeName()).inactive(view.getInactive()).build()).collect(Collectors.toSet());

    }

    @Transactional
    public void deleteChartType(@NotEmpty List<@Valid DeleteChartTypeRequest> deleteChartTypeDTOS) {

    criteriaBuilderFactory.delete(entityManager, ChartType.class)
            .where("oid").in(deleteChartTypeDTOS.parallelStream().map(DeleteChartTypeRequest::oid).collect(Collectors.toSet()))
            .executeUpdate();

    }
}

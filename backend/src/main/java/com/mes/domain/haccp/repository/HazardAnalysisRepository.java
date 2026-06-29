package com.mes.domain.haccp.repository;

import com.mes.domain.haccp.entity.HazardAnalysis;
import com.mes.domain.haccp.enums.HazardType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HazardAnalysisRepository {

    Optional<HazardAnalysis> findById(UUID id);

    List<HazardAnalysis> findByHaccpPlanId(UUID haccpPlanId);

    List<HazardAnalysis> findByStepId(UUID stepId);

    List<HazardAnalysis> findByHazardType(HazardType hazardType);

    HazardAnalysis save(HazardAnalysis hazardAnalysis);

    void deleteAllByHaccpPlanId(UUID haccpPlanId);
}

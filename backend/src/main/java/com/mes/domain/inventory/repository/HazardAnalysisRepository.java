package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.HazardAnalysis;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HazardAnalysisRepository {

    Optional<HazardAnalysis> findById(UUID id);

    List<HazardAnalysis> findByFactoryId(UUID factoryId);

    List<HazardAnalysis> findByHazardCategory(String hazardCategory);

    List<HazardAnalysis> findByAcceptability(String acceptability);

    HazardAnalysis save(HazardAnalysis hazardAnalysis);

    void delete(UUID hazardId);
}
package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.InspectionSpec;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InspectionSpecRepository {

    Optional<InspectionSpec> findById(UUID id);

    List<InspectionSpec> findByFactoryId(UUID factoryId);

    List<InspectionSpec> findByInspectionCategory(String inspectionCategory);

    List<InspectionSpec> findByInspectionItemType(String inspectionItemType);

    InspectionSpec save(InspectionSpec spec);

    void delete(UUID specId);
}
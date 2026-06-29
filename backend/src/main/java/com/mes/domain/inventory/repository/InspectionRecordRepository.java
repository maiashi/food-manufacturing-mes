package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.InspectionRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InspectionRecordRepository {

    Optional<InspectionRecord> findById(UUID id);

    List<InspectionRecord> findByFactoryId(UUID factoryId);

    List<InspectionRecord> findByLotId(UUID lotId);

    List<InspectionRecord> findByInspectionSpecId(UUID inspectionSpecId);

    List<InspectionRecord> findByInspectionResult(String inspectionResult);

    InspectionRecord save(InspectionRecord record);

    void delete(UUID recordId);
}
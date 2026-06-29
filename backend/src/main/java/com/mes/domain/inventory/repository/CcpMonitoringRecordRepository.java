package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.CcpMonitoringRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CcpMonitoringRecordRepository {

    Optional<CcpMonitoringRecord> findById(UUID id);

    List<CcpMonitoringRecord> findByFactoryId(UUID factoryId);

    List<CcpMonitoringRecord> findByCcpMapId(UUID ccpMapId);

    List<CcpMonitoringRecord> findByCriticalLimitId(UUID criticalLimitId);

    List<CcpMonitoringRecord> findByIsWithinLimit(boolean isWithinLimit);

    CcpMonitoringRecord save(CcpMonitoringRecord record);

    void delete(UUID recordId);
}
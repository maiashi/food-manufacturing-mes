package com.mes.domain.haccp.repository;

import com.mes.domain.haccp.entity.CcpMonitoringRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CcpMonitoringRecordRepository {

    Optional<CcpMonitoringRecord> findById(UUID id);

    List<CcpMonitoringRecord> findByBatchId(UUID batchId);

    List<CcpMonitoringRecord> findByStepIdAndMonitoringDateBetween(UUID stepId, LocalDateTime start, LocalDateTime end);

    List<CcpMonitoringRecord> findByDeviationTakenTrue();

    List<CcpMonitoringRecord> bulkInsert(List<CcpMonitoringRecord> records);

    CcpMonitoringRecord save(CcpMonitoringRecord record);
}

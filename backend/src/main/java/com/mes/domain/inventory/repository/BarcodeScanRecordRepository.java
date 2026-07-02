package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.BarcodeScanRecord;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BarcodeScanRecordRepository {

    Optional<BarcodeScanRecord> findById(UUID id);

    List<BarcodeScanRecord> findByFactoryIdAndScannedAtBetween(UUID factoryId, LocalDateTime start, LocalDateTime end);

    List<BarcodeScanRecord> findByScanTypeAndScanValue(String scanType, String scanValue);

    List<BarcodeScanRecord> findByStatus(String status);

    BarcodeScanRecord save(BarcodeScanRecord record);

    void delete(UUID scanId);
}
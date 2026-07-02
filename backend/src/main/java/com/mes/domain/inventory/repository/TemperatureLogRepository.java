package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.TemperatureLog;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TemperatureLogRepository {

    Optional<TemperatureLog> findById(UUID id);

    List<TemperatureLog> findByWarehouseIdAndRecordedAtBetween(UUID warehouseId, LocalDateTime start, LocalDateTime end);

    List<TemperatureLog> findByZoneIdAndRecordedAtBetween(UUID zoneId, LocalDateTime start, LocalDateTime end);

    List<TemperatureLog> findByStatus(String status);

    List<TemperatureLog> findByWarehouseId(UUID warehouseId);

    List<TemperatureLog> findByZoneId(UUID zoneId);

    TemperatureLog save(TemperatureLog temperatureLog);

    void delete(UUID logId);
}
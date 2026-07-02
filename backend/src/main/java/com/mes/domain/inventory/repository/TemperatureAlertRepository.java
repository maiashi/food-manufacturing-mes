package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.TemperatureAlert;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TemperatureAlertRepository {

    Optional<TemperatureAlert> findById(UUID id);

    List<TemperatureAlert> findByWarehouseIdAndIsResolvedFalse(UUID warehouseId);

    List<TemperatureAlert> findByZoneIdAndIsResolvedFalse(UUID zoneId);

    List<TemperatureAlert> findByAlertTypeAndIsResolvedFalse(String alertType);

    List<TemperatureAlert> findBySeverityAndIsResolvedFalse(String severity);

    List<TemperatureAlert> findByIsResolvedFalse();

    List<TemperatureAlert> findByOccurredAtBetween(LocalDateTime start, LocalDateTime end);

    TemperatureAlert save(TemperatureAlert temperatureAlert);

    void delete(UUID alertId);
}
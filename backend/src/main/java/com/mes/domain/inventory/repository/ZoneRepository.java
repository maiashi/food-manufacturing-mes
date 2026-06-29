package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.Zone;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ZoneRepository {

    Optional<Zone> findById(UUID id);

    List<Zone> findByFactoryIdAndWarehouseId(UUID factoryId, UUID warehouseId);

    List<Zone> findByWarehouseId(UUID warehouseId);

    List<Zone> findByZoneType(String zoneType);

    Zone save(Zone zone);

    void delete(UUID zoneId);
}
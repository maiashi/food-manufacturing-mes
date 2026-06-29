package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.Warehouse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WarehouseRepository {

    Optional<Warehouse> findById(UUID id);

    List<Warehouse> findByFactoryId(UUID factoryId);

    List<Warehouse> findByTemperatureZone(String temperatureZone);

    List<Warehouse> findByWarehouseType(String warehouseType);

    Warehouse save(Warehouse warehouse);

    void delete(UUID warehouseId);
}
package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.Lot;
import com.mes.domain.inventory.enums.LotStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LotRepository {

    Optional<Lot> findById(UUID id);

    List<Lot> findByFactoryIdAndStatus(UUID factoryId, LotStatus... statuses);

    List<Lot> findByExpiryBefore(LocalDate date);

    List<Lot> findByLotNumber(String lotNumber);

    Optional<Lot> findByMaterialIdAndStatus(UUID materialId, LotStatus status);

    List<Lot> findByWarehouseId(UUID warehouseId);

    List<Lot> findByStatus(LotStatus status);

    Lot save(Lot lot);

    void delete(UUID lotId);
}

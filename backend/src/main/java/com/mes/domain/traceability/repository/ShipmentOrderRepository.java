package com.mes.domain.traceability.repository;

import com.mes.domain.traceability.entity.ShipmentOrder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShipmentOrderRepository {

    Optional<ShipmentOrder> findById(UUID id);

    List<ShipmentOrder> findByFactoryId(UUID factoryId);

    ShipmentOrder save(ShipmentOrder shipmentOrder);
}

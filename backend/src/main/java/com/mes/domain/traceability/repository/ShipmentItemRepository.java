package com.mes.domain.traceability.repository;

import com.mes.domain.traceability.entity.ShipmentItem;

import java.util.List;
import java.util.UUID;

public interface ShipmentItemRepository {

    List<ShipmentItem> findByShipmentId(UUID shipmentId);

    List<ShipmentItem> findByLotId(UUID lotId);

    ShipmentItem save(ShipmentItem shipmentItem);
}

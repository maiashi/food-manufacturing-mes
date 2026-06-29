package com.mes.domain.traceability.entity;

import com.mes.domain.common.BaseEntity;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShipmentOrder extends BaseEntity {

    private UUID shipmentId;
    private UUID factoryId;
    private String shipmentNumber;
    private UUID customerId;
    private String deliveryAddress;
    private LocalDateTime shipmentDate;
    private com.mes.domain.traceability.enums.ShipmentStatus status;
    private LocalDateTime createdAt;

    public ShipmentOrder(UUID shipmentId, UUID factoryId, String shipmentNumber,
                         UUID customerId, String deliveryAddress, LocalDateTime shipmentDate,
                         com.mes.domain.traceability.enums.ShipmentStatus status) {
        super(shipmentId, java.time.LocalDateTime.now());
        this.shipmentId = shipmentId;
        this.factoryId = factoryId;
        this.shipmentNumber = shipmentNumber;
        this.customerId = customerId;
        this.deliveryAddress = deliveryAddress;
        this.shipmentDate = shipmentDate;
        this.status = status;
        this.createdAt = java.time.LocalDateTime.now();
    }

    @Override
    public ShipmentOrder copy(UUID newId) {
        return new ShipmentOrder(newId, factoryId, shipmentNumber, customerId,
                deliveryAddress, shipmentDate, status);
    }
}

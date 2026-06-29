package com.mes.domain.traceability.entity;

import com.mes.domain.common.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ShipmentItem extends BaseEntity {

    private UUID itemId;
    private UUID factoryId;
    private UUID shipmentId;
    private UUID lotId;
    private BigDecimal quantity;
    private LocalDateTime shippedDate;

    public ShipmentItem(UUID itemId, UUID factoryId, UUID shipmentId, UUID lotId,
                        BigDecimal quantity, LocalDateTime shippedDate) {
        super(itemId, java.time.LocalDateTime.now());
        this.itemId = itemId;
        this.factoryId = factoryId;
        this.shipmentId = shipmentId;
        this.lotId = lotId;
        this.quantity = quantity;
        this.shippedDate = shippedDate;
    }

    @Override
    public ShipmentItem copy(UUID newId) {
        return new ShipmentItem(newId, factoryId, shipmentId, lotId, quantity, shippedDate);
    }
}

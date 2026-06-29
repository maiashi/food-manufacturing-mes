package com.mes.domain.production.entity;

import com.mes.domain.common.BaseEntity;
import com.mes.domain.production.enums.ProductionOrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ProductionOrder extends BaseEntity {

    private UUID productionOrderId;
    private UUID factoryId;
    private String orderNumber;
    private UUID productId;
    private UUID lineId;
    private UUID warehouseId;
    private LocalDateTime plannedStartDate;
    private LocalDateTime actualStartDate;
    private LocalDateTime completedDate;
    private BigDecimal totalQty;
    private ProductionOrderStatus status;

    public ProductionOrder(UUID productionOrderId, UUID factoryId, String orderNumber, UUID productId,
                           UUID lineId, UUID warehouseId, LocalDateTime plannedStartDate,
                           LocalDateTime actualStartDate, LocalDateTime completedDate,
                           BigDecimal totalQty, ProductionOrderStatus status) {
        super(productionOrderId, java.time.LocalDateTime.now());
        this.productionOrderId = productionOrderId;
        this.factoryId = factoryId;
        this.orderNumber = orderNumber;
        this.productId = productId;
        this.lineId = lineId;
        this.warehouseId = warehouseId;
        this.plannedStartDate = plannedStartDate;
        this.actualStartDate = actualStartDate;
        this.completedDate = completedDate;
        this.totalQty = totalQty;
        this.status = status;
    }

    @Override
    public ProductionOrder copy(UUID newId) {
        return new ProductionOrder(
                newId, factoryId, orderNumber, productId, lineId, warehouseId,
                plannedStartDate, actualStartDate, completedDate, totalQty, status
        );
    }
}

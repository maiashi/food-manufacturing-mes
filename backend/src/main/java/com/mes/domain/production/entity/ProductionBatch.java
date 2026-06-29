package com.mes.domain.production.entity;

import com.mes.domain.common.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductionBatch extends BaseEntity {

    private UUID batchId;
    private UUID factoryId;
    private UUID lineId;
    private String batchNumber;
    private UUID productionOrderId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal inputQty;
    private BigDecimal outputQty;
    private BigDecimal yieldRate;
    private BigDecimal scrapQty; // WR-004: 副産物・廃棄物量
    private BigDecimal wasteQty; // WR-004: 廃棄物量

    public ProductionBatch(UUID batchId, UUID factoryId, UUID lineId, String batchNumber,
                           UUID productionOrderId, LocalDateTime startDate, LocalDateTime endDate,
                           BigDecimal inputQty, BigDecimal outputQty, BigDecimal yieldRate,
                           BigDecimal scrapQty, BigDecimal wasteQty) {
        super(batchId, java.time.LocalDateTime.now());
        this.batchId = batchId;
        this.factoryId = factoryId;
        this.lineId = lineId;
        this.batchNumber = batchNumber;
        this.productionOrderId = productionOrderId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.inputQty = inputQty;
        this.outputQty = outputQty;
        this.yieldRate = yieldRate;
        this.scrapQty = scrapQty;
        this.wasteQty = wasteQty;
    }

    @Override
    public ProductionBatch copy(UUID newId) {
        return new ProductionBatch(newId, factoryId, lineId, batchNumber, productionOrderId,
                startDate, endDate, inputQty, outputQty, yieldRate, scrapQty, wasteQty);
    }
}

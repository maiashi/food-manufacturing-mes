package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import com.mes.domain.inventory.enums.StockTransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class StockTransaction extends BaseEntity {

    private Long transactionId;
    private UUID lotId;
    private UUID factoryId;
    private StockTransactionType type;
    private BigDecimal quantityDelta;
    private BigDecimal remainingQty;
    private String referenceDocNo;
    private UUID createdByUserId;

    // Auto-increment primary key, so id parameter is null for new records
    public StockTransaction(UUID lotId, UUID factoryId, StockTransactionType type,
                            BigDecimal quantityDelta, BigDecimal remainingQty,
                            String referenceDocNo, UUID createdByUserId) {
        super(null, LocalDateTime.now());
        this.transactionId = null;
        this.lotId = lotId;
        this.factoryId = factoryId;
        this.type = type;
        this.quantityDelta = quantityDelta;
        this.remainingQty = remainingQty;
        this.referenceDocNo = referenceDocNo;
        this.createdByUserId = createdByUserId;
    }

    @Override
    public StockTransaction copy(UUID newId) {
        return new StockTransaction(
                lotId, factoryId, type, quantityDelta, remainingQty, referenceDocNo, createdByUserId
        );
    }
}

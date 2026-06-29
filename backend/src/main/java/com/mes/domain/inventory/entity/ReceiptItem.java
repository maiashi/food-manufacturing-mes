package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ReceiptItem extends BaseEntity {

    private UUID receiptItemId;
    private UUID factoryId;
    private UUID receiptId;
    private UUID materialId;
    private UUID lotId;
    private BigDecimal receivedQty;
    private String coaFileUrl;

    public ReceiptItem(UUID receiptItemId, UUID factoryId, UUID receiptId, UUID materialId,
                       UUID lotId, BigDecimal receivedQty, String coaFileUrl) {
        super(receiptItemId, java.time.LocalDateTime.now());
        this.receiptItemId = receiptItemId;
        this.factoryId = factoryId;
        this.receiptId = receiptId;
        this.materialId = materialId;
        this.lotId = lotId;
        this.receivedQty = receivedQty;
        this.coaFileUrl = coaFileUrl;
    }

    @Override
    public ReceiptItem copy(UUID newId) {
        return new ReceiptItem(
                newId, factoryId, receiptId, materialId, lotId,
                receivedQty, coaFileUrl
        );
    }
}

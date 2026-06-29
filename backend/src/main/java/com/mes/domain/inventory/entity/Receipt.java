package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import com.mes.domain.inventory.enums.InspectionResultType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Receipt extends BaseEntity {

    private UUID receiptId;
    private UUID factoryId;
    private UUID poId;
    private String receiptNumber;
    private InspectionResultType inspectionResult;
    private LocalDateTime receivedAt;
    private UUID receivedByUserId;

    public Receipt(UUID receiptId, UUID factoryId, UUID poId, String receiptNumber,
                   InspectionResultType inspectionResult, LocalDateTime receivedAt,
                   UUID receivedByUserId) {
        super(receiptId, java.time.LocalDateTime.now());
        this.receiptId = receiptId;
        this.factoryId = factoryId;
        this.poId = poId;
        this.receiptNumber = receiptNumber;
        this.inspectionResult = inspectionResult;
        this.receivedAt = receivedAt;
        this.receivedByUserId = receivedByUserId;
    }

    @Override
    public Receipt copy(UUID newId) {
        return new Receipt(
                newId, factoryId, poId, receiptNumber,
                inspectionResult, receivedAt, receivedByUserId
        );
    }
}

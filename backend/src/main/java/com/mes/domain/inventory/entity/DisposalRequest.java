package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import com.mes.domain.inventory.enums.DisposalReason;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisposalRequest extends BaseEntity {

    private UUID requestId;
    private UUID factoryId;
    private UUID lotId;
    private String lotNumber;
    private BigDecimal quantity;
    private DisposalReason reason;
    private String reasonDetail;
    private String status; // PENDING, APPROVED, REJECTED, COMPLETED
    private UUID requestedBy;
    private LocalDateTime requestedAt;
    private UUID approvedBy;
    private LocalDateTime approvedAt;
    private String disposalMethod; // 廃棄方法

    public DisposalRequest(UUID requestId, UUID factoryId, UUID lotId, String lotNumber,
                           BigDecimal quantity, DisposalReason reason, String reasonDetail,
                           String status, UUID requestedBy, LocalDateTime requestedAt) {
        super(requestId, LocalDateTime.now());
        this.requestId = requestId;
        this.factoryId = factoryId;
        this.lotId = lotId;
        this.lotNumber = lotNumber;
        this.quantity = quantity;
        this.reason = reason;
        this.reasonDetail = reasonDetail;
        this.status = status != null ? status : "PENDING";
        this.requestedBy = requestedBy;
        this.requestedAt = requestedAt != null ? requestedAt : LocalDateTime.now();
    }

    public void approve(UUID approvedBy, LocalDateTime approvedAt) {
        this.approvedBy = approvedBy;
        this.approvedAt = approvedAt != null ? approvedAt : LocalDateTime.now();
        this.status = "APPROVED";
    }

    public void reject() {
        this.status = "REJECTED";
    }

    public void complete(String disposalMethod) {
        this.status = "COMPLETED";
        this.disposalMethod = disposalMethod;
    }

    @Override
    public DisposalRequest copy(UUID newId) {
        return new DisposalRequest(newId, factoryId, lotId, lotNumber, quantity, reason, reasonDetail,
                status, requestedBy, requestedAt);
    }
}
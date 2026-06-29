package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationTransfer extends BaseEntity {

    private UUID transferId;
    private UUID factoryId;
    private UUID lotId;
    private String lotNumber;
    private UUID fromWarehouseId;
    private UUID fromZoneId;
    private UUID fromShelfId;
    private UUID toWarehouseId;
    private UUID toZoneId;
    private UUID toShelfId;
    private String transferReason;
    private String status; // 申請中/承認済/実行済/キャンセル済
    private UUID requestedBy;
    private UUID approvedBy;
    private java.time.LocalDateTime requestedAt;
    private java.time.LocalDateTime approvedAt;
    private java.time.LocalDateTime executedAt;

    public LocationTransfer(UUID transferId, UUID factoryId, UUID lotId, String lotNumber,
                            UUID fromWarehouseId, UUID fromZoneId, UUID fromShelfId,
                            UUID toWarehouseId, UUID toZoneId, UUID toShelfId,
                            String transferReason, UUID requestedBy) {
        super(transferId, java.time.LocalDateTime.now());
        this.transferId = transferId;
        this.factoryId = factoryId;
        this.lotId = lotId;
        this.lotNumber = lotNumber;
        this.fromWarehouseId = fromWarehouseId;
        this.fromZoneId = fromZoneId;
        this.fromShelfId = fromShelfId;
        this.toWarehouseId = toWarehouseId;
        this.toZoneId = toZoneId;
        this.toShelfId = toShelfId;
        this.transferReason = transferReason;
        this.status = "申請中";
        this.requestedBy = requestedBy;
        this.requestedAt = java.time.LocalDateTime.now();
    }

    public void approve(UUID approvedBy) {
        this.approvedBy = approvedBy;
        this.approvedAt = java.time.LocalDateTime.now();
        this.status = "承認済";
    }

    public void execute() {
        this.executedAt = java.time.LocalDateTime.now();
        this.status = "実行済";
    }

    public void cancel() {
        this.status = "キャンセル済";
    }

    @Override
    public LocationTransfer copy(UUID newId) {
        LocationTransfer copy = new LocationTransfer(newId, factoryId, lotId, lotNumber,
                fromWarehouseId, fromZoneId, fromShelfId, toWarehouseId, toZoneId, toShelfId,
                transferReason, requestedBy);
        copy.setApprovedBy(this.approvedBy);
        copy.setStatus(this.status);
        copy.setApprovedAt(this.approvedAt);
        copy.setExecutedAt(this.executedAt);
        return copy;
    }
}
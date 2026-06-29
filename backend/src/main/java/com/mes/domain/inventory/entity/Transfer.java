package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a stock transfer between locations within a warehouse.
 * Covers LOC-003 (transfer history) and TRF-001~TRF-004.
 */
@Getter
@Setter
public class Transfer extends BaseEntity {

    private UUID transferId;
    private UUID factoryId;
    private UUID fromWarehouseId;
    private UUID fromZoneId;
    private UUID fromShelfId;
    private UUID fromBinId;
    private UUID toWarehouseId;
    private UUID toZoneId;
    private UUID toShelfId;
    private UUID toBinId;
    private UUID lotId;
    private BigDecimal quantity;
    private LocalDateTime scheduledDate;
    private LocalDateTime executedDate;
    private String status; // SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
    private String reason;
    private UUID createdByUserId;
    private String transferNumber;

    public Transfer(UUID transferId, UUID factoryId,
                    UUID fromWarehouseId, UUID fromZoneId, UUID fromShelfId, UUID fromBinId,
                    UUID toWarehouseId, UUID toZoneId, UUID toShelfId, UUID toBinId,
                    UUID lotId, BigDecimal quantity, LocalDateTime scheduledDate,
                    LocalDateTime executedDate, String status, String reason,
                    UUID createdByUserId, String transferNumber) {
        super(transferId, java.time.LocalDateTime.now());
        this.transferId = transferId;
        this.factoryId = factoryId;
        this.fromWarehouseId = fromWarehouseId;
        this.fromZoneId = fromZoneId;
        this.fromShelfId = fromShelfId;
        this.fromBinId = fromBinId;
        this.toWarehouseId = toWarehouseId;
        this.toZoneId = toZoneId;
        this.toShelfId = toShelfId;
        this.toBinId = toBinId;
        this.lotId = lotId;
        this.quantity = quantity;
        this.scheduledDate = scheduledDate;
        this.executedDate = executedDate;
        this.status = status;
        this.reason = reason;
        this.createdByUserId = createdByUserId;
        this.transferNumber = transferNumber;
    }

    @Override
    public Transfer copy(UUID newId) {
        return new Transfer(
                newId, factoryId,
                fromWarehouseId, fromZoneId, fromShelfId, fromBinId,
                toWarehouseId, toZoneId, toShelfId, toBinId,
                lotId, quantity, scheduledDate, executedDate, status, reason,
                createdByUserId, transferNumber
        );
    }

    /**
     * Returns the full source location description.
     */
    public String getFromLocation() {
        StringBuilder sb = new StringBuilder();
        if (fromWarehouseId != null) sb.append("WH-").append(fromWarehouseId);
        if (fromZoneId != null) sb.append("-ZN-").append(fromZoneId);
        if (fromShelfId != null) sb.append("-SH-").append(fromShelfId);
        if (fromBinId != null) sb.append("-BN-").append(fromBinId);
        return sb.toString();
    }

    /**
     * Returns the full destination location description.
     */
    public String getToLocation() {
        StringBuilder sb = new StringBuilder();
        if (toWarehouseId != null) sb.append("WH-").append(toWarehouseId);
        if (toZoneId != null) sb.append("-ZN-").append(toZoneId);
        if (toShelfId != null) sb.append("-SH-").append(toShelfId);
        if (toBinId != null) sb.append("-BN-").append(toBinId);
        return sb.toString();
    }
}

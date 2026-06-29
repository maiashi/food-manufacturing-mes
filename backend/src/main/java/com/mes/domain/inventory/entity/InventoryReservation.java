package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryReservation extends BaseEntity {

    private UUID reservationId;
    private UUID factoryId;
    private String lotNumber;
    private BigDecimal reservedQty;
    private String sourceDocType; // 発注種別 (ProductionOrder, ShipmentOrder etc.)
    private String sourceDocId;   // 関連ドキュメントID

    public InventoryReservation(UUID reservationId, UUID factoryId, String lotNumber,
                                BigDecimal reservedQty, String sourceDocType,
                                String sourceDocId) {
        super(reservationId, LocalDateTime.now());
        this.reservationId = reservationId;
        this.factoryId = factoryId;
        this.lotNumber = lotNumber;
        this.reservedQty = reservedQty;
        this.sourceDocType = sourceDocType;
        this.sourceDocId = sourceDocId;
    }

    /**
     * 予約を解除する（deletedAtを設定）。
     */
    public void cancel() {
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * 新しいIDでコピーする。
     */
    @Override
    public InventoryReservation copy(UUID newId) {
        return new InventoryReservation(newId, factoryId, lotNumber, reservedQty,
                sourceDocType, sourceDocId);
    }
}

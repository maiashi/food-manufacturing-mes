package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import com.mes.domain.inventory.enums.LotStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Lot extends BaseEntity {

    private UUID lotId;
    private UUID factoryId;
    private String lotNumber;
    private UUID materialId;
    private UUID supplierId;
    private UUID parentLotId;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private BigDecimal quantity;
    private LotStatus status;
    private UUID warehouseId;
    private UUID zoneId;
    private UUID shelfId;
    private UUID binId;
    private Boolean isQuarantined;

    public Lot(UUID lotId, UUID factoryId, String lotNumber, UUID materialId, UUID supplierId,
               UUID parentLotId, LocalDate manufacturingDate, LocalDate expiryDate, BigDecimal quantity,
               LotStatus status, UUID warehouseId, UUID zoneId, UUID shelfId, UUID binId,
               Boolean isQuarantined) {
        super(lotId, java.time.LocalDateTime.now());
        this.lotId = lotId;
        this.factoryId = factoryId;
        this.lotNumber = lotNumber;
        this.materialId = materialId;
        this.supplierId = supplierId;
        this.parentLotId = parentLotId;
        this.manufacturingDate = manufacturingDate;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.status = status;
        this.warehouseId = warehouseId;
        this.zoneId = zoneId;
        this.shelfId = shelfId;
        this.binId = binId;
        this.isQuarantined = isQuarantined;
    }

    @Override
    public Lot copy(UUID newId) {
        return new Lot(
                newId, factoryId, lotNumber, materialId, supplierId, parentLotId,
                manufacturingDate, expiryDate, quantity, status,
                warehouseId, zoneId, shelfId, binId, isQuarantined
        );
    }
}

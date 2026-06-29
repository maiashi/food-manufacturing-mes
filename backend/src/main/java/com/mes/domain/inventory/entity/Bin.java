package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Bin extends BaseEntity {

    private UUID binId;
    private UUID shelfId;
    private String binCode;
    private BigDecimal weightCapacityKg;
    private Boolean isOccupied;

    public Bin(UUID binId, UUID shelfId, String binCode, BigDecimal weightCapacityKg, Boolean isOccupied) {
        super(binId, java.time.LocalDateTime.now());
        this.binId = binId;
        this.shelfId = shelfId;
        this.binCode = binCode;
        this.weightCapacityKg = weightCapacityKg;
        this.isOccupied = isOccupied;
    }

    @Override
    public Bin copy(UUID newId) {
        return new Bin(
                newId, shelfId, binCode, weightCapacityKg, isOccupied
        );
    }
}

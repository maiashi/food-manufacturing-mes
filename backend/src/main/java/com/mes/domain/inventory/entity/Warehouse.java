package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Warehouse extends BaseEntity {

    private UUID warehouseId;
    private UUID factoryId;
    private String warehouseCode;
    private String warehouseName;
    private String temperatureZone; // 常温/冷蔵/冷凍/特冷
    private BigDecimal capacityVolume; // 総容積
    private BigDecimal capacityWeight; // 総重量
    private BigDecimal usedVolume; // 使用容積
    private BigDecimal usedWeight; // 使用重量
    private String warehouseType; // 原材料倉庫/半成品倉庫/完成品倉庫/返品倉庫等

    public Warehouse(UUID warehouseId, UUID factoryId, String warehouseCode, String warehouseName,
                     String temperatureZone, BigDecimal capacityVolume, BigDecimal capacityWeight,
                     String warehouseType) {
        super(warehouseId, java.time.LocalDateTime.now());
        this.warehouseId = warehouseId;
        this.factoryId = factoryId;
        this.warehouseCode = warehouseCode;
        this.warehouseName = warehouseName;
        this.temperatureZone = temperatureZone;
        this.capacityVolume = capacityVolume;
        this.capacityWeight = capacityWeight;
        this.usedVolume = BigDecimal.ZERO;
        this.usedWeight = BigDecimal.ZERO;
        this.warehouseType = warehouseType;
    }

    @Override
    public Warehouse copy(UUID newId) {
        return new Warehouse(newId, factoryId, warehouseCode, warehouseName, temperatureZone,
                capacityVolume, capacityWeight, warehouseType);
    }
}
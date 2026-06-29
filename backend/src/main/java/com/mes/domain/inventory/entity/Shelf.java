package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Shelf extends BaseEntity {

    private UUID shelfId;
    private UUID factoryId;
    private UUID zoneId;
    private String shelfCode; // 例: A-01-03 = A通路, 01段目, 03番
    private BigDecimal maxWeight; // 積載上限重量(kg)
    private BigDecimal maxVolume; // 積載上限容積(L)
    private BigDecimal usedWeight; // 使用重量
    private BigDecimal usedVolume; // 使用容積
    private String status; // 使用中/点検中/使用不可

    public Shelf(UUID shelfId, UUID factoryId, UUID zoneId, String shelfCode, BigDecimal maxWeight,
                 BigDecimal maxVolume, String status) {
        super(shelfId, java.time.LocalDateTime.now());
        this.shelfId = shelfId;
        this.factoryId = factoryId;
        this.zoneId = zoneId;
        this.shelfCode = shelfCode;
        this.maxWeight = maxWeight;
        this.maxVolume = maxVolume;
        this.usedWeight = BigDecimal.ZERO;
        this.usedVolume = BigDecimal.ZERO;
        this.status = status != null ? status : "使用中";
    }

    @Override
    public Shelf copy(UUID newId) {
        return new Shelf(newId, factoryId, zoneId, shelfCode, maxWeight, maxVolume, status);
    }
}
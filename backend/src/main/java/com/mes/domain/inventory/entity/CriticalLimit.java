package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriticalLimit extends BaseEntity {

    private UUID limitId;
    private UUID factoryId;
    private UUID ccpMapId;
    private String limitCode;
    private String limitType; // 温度/時間/水分活性/塩素濃度等
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private String unit; // ℃/分/aW/ppm等
    private boolean isComposite; // 複合リミットかどうか

    public CriticalLimit(UUID limitId, UUID factoryId, UUID ccpMapId, String limitCode,
                         String limitType, BigDecimal minValue, BigDecimal maxValue, String unit,
                         boolean isComposite) {
        super(limitId, java.time.LocalDateTime.now());
        this.limitId = limitId;
        this.factoryId = factoryId;
        this.ccpMapId = ccpMapId;
        this.limitCode = limitCode;
        this.limitType = limitType;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.unit = unit;
        this.isComposite = isComposite;
    }

    @Override
    public CriticalLimit copy(UUID newId) {
        return new CriticalLimit(newId, factoryId, ccpMapId, limitCode, limitType, minValue, maxValue, unit, isComposite);
    }
}
package com.mes.domain.production.entity;

import com.mes.domain.common.BaseEntity;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ProductSpec extends BaseEntity {

    private UUID specId;
    private UUID factoryId;
    private String skuCode;
    private BigDecimal minWeightG;
    private BigDecimal maxWeightG;
    private Integer shelfLifeDays;
    private BigDecimal storageTempMinC;
    private BigDecimal storageTempMaxC;

    public ProductSpec(UUID specId, UUID factoryId, String skuCode, BigDecimal minWeightG,
                       BigDecimal maxWeightG, Integer shelfLifeDays, BigDecimal storageTempMinC,
                       BigDecimal storageTempMaxC) {
        super(specId, java.time.LocalDateTime.now());
        this.specId = specId;
        this.factoryId = factoryId;
        this.skuCode = skuCode;
        this.minWeightG = minWeightG;
        this.maxWeightG = maxWeightG;
        this.shelfLifeDays = shelfLifeDays;
        this.storageTempMinC = storageTempMinC;
        this.storageTempMaxC = storageTempMaxC;
    }

    @Override
    public ProductSpec copy(UUID newId) {
        return new ProductSpec(
                newId, factoryId, skuCode, minWeightG, maxWeightG, shelfLifeDays,
                storageTempMinC, storageTempMaxC
        );
    }
}

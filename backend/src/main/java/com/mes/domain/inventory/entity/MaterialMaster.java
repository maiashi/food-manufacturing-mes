package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class MaterialMaster extends BaseEntity {

    private UUID materialId;
    private String skuCode;
    private String materialName;
    private String category;
    private Boolean isAllergen;
    private List<String> allergenTypes;
    private String storageCondition;
    private String unit;
    private String shelfLifeDays; // INV-005: 賞味期限タイプ（製造日から○日／指定日／なし）
    private String coaFileUrl; // INV-008: COA/SDS PDF URL

    public MaterialMaster(UUID materialId, String skuCode, String materialName, String category,
                          Boolean isAllergen, List<String> allergenTypes, String storageCondition,
                          String unit, LocalDateTime createdAt, String shelfLifeDays, String coaFileUrl) {
        super(materialId, createdAt);
        this.materialId = materialId;
        this.skuCode = skuCode;
        this.materialName = materialName;
        this.category = category;
        this.isAllergen = isAllergen;
        this.allergenTypes = allergenTypes;
        this.storageCondition = storageCondition;
        this.unit = unit;
        this.shelfLifeDays = shelfLifeDays;
        this.coaFileUrl = coaFileUrl;
    }

    @Override
    public MaterialMaster copy(UUID newId) {
        return new MaterialMaster(
                newId,
                skuCode,
                materialName,
                category,
                isAllergen,
                allergenTypes,
                storageCondition,
                unit,
                getCreatedAt(),
                shelfLifeDays,
                coaFileUrl
        );
    }
}

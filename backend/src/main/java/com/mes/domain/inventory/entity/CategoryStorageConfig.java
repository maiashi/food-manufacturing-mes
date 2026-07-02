package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryStorageConfig extends BaseEntity {

    private UUID configId;
    private UUID factoryId;
    private String categoryName; // レトルト/チルド総菜/デリカ/おせち
    private String defaultWarehouseType; // 常温倉庫/冷蔵倉庫/冷凍倉庫
    private String defaultZoneType; // 常温ゾーン/冷蔵ゾーン/冷凍ゾーン
    private boolean allowMixedStorage; // 混在許可フラグ
    private String specialRules; // 特殊ルール（おせち期間など）

    public CategoryStorageConfig(UUID configId, UUID factoryId, String categoryName,
                                 String defaultWarehouseType, String defaultZoneType,
                                 boolean allowMixedStorage, String specialRules) {
        super(configId, java.time.LocalDateTime.now());
        this.configId = configId;
        this.factoryId = factoryId;
        this.categoryName = categoryName;
        this.defaultWarehouseType = defaultWarehouseType;
        this.defaultZoneType = defaultZoneType;
        this.allowMixedStorage = allowMixedStorage;
        this.specialRules = specialRules;
    }

    @Override
    public CategoryStorageConfig copy(UUID newId) {
        return new CategoryStorageConfig(newId, factoryId, categoryName,
                defaultWarehouseType, defaultZoneType, allowMixedStorage, specialRules);
    }
}
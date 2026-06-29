package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryFifoConfig extends BaseEntity {

    private UUID configId;
    private UUID factoryId;
    private String categoryCode; // レトルト/チルド総菜/デリカ/おせち
    private String categoryName;
    private Integer defaultExpiryDays;
    private String storageTemperature;
    private Integer fifoPriorityLevel; // 1-4 (4 is highest priority)
    private Integer warningThresholdDays; // 警告閾値（日数）
    private Boolean strictFifoRequired; // 厳格なFIFO必須かどうか
    private Boolean manualOverrideAllowed; // 手動オーバーライド許可かどうか
    private String overrideReasonRequired; // オーバーライド理由必須かどうか

    public CategoryFifoConfig(UUID configId, UUID factoryId, String categoryCode, String categoryName,
                              Integer defaultExpiryDays, String storageTemperature, Integer fifoPriorityLevel,
                              Integer warningThresholdDays, Boolean strictFifoRequired, Boolean manualOverrideAllowed,
                              String overrideReasonRequired) {
        super(configId, java.time.LocalDateTime.now());
        this.configId = configId;
        this.factoryId = factoryId;
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.defaultExpiryDays = defaultExpiryDays;
        this.storageTemperature = storageTemperature;
        this.fifoPriorityLevel = fifoPriorityLevel;
        this.warningThresholdDays = warningThresholdDays;
        this.strictFifoRequired = strictFifoRequired;
        this.manualOverrideAllowed = manualOverrideAllowed;
        this.overrideReasonRequired = overrideReasonRequired;
    }

    @Override
    public CategoryFifoConfig copy(UUID newId) {
        return new CategoryFifoConfig(
                newId, factoryId, categoryCode, categoryName, defaultExpiryDays, storageTemperature,
                fifoPriorityLevel, warningThresholdDays, strictFifoRequired, manualOverrideAllowed,
                overrideReasonRequired
        );
    }
}
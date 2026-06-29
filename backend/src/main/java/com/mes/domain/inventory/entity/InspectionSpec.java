package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InspectionSpec extends BaseEntity {

    private UUID specId;
    private UUID factoryId;
    private String specCode;
    private String specName;
    private String inspectionCategory; // 原材料/工程内/完成品
    private String inspectionItemType; // 物理的/化学的/生物学的/外観
    private String judgmentCriteria; // 判定基準

    public InspectionSpec(UUID specId, UUID factoryId, String specCode, String specName,
                          String inspectionCategory, String inspectionItemType, String judgmentCriteria) {
        super(specId, java.time.LocalDateTime.now());
        this.specId = specId;
        this.factoryId = factoryId;
        this.specCode = specCode;
        this.specName = specName;
        this.inspectionCategory = inspectionCategory;
        this.inspectionItemType = inspectionItemType;
        this.judgmentCriteria = judgmentCriteria;
    }

    @Override
    public InspectionSpec copy(UUID newId) {
        return new InspectionSpec(newId, factoryId, specCode, specName, inspectionCategory,
                inspectionItemType, judgmentCriteria);
    }
}
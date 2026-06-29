package com.mes.domain.haccp.entity;

import com.mes.domain.common.BaseEntity;
import com.mes.domain.haccp.enums.HaccpPlanType;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class HaccpPlan extends BaseEntity {

    private UUID haccpPlanId;
    private UUID factoryId;
    private String planVersion;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private HaccpPlanType type;
    private Boolean isActive;

    public HaccpPlan(UUID haccpPlanId, UUID factoryId, String planVersion, LocalDate effectiveDate,
                     LocalDate expiryDate, HaccpPlanType type, Boolean isActive) {
        super(haccpPlanId, java.time.LocalDateTime.now());
        this.haccpPlanId = haccpPlanId;
        this.factoryId = factoryId;
        this.planVersion = planVersion;
        this.effectiveDate = effectiveDate;
        this.expiryDate = expiryDate;
        this.type = type;
        this.isActive = isActive;
    }

    @Override
    public HaccpPlan copy(UUID newId) {
        return new HaccpPlan(newId, factoryId, planVersion, effectiveDate, expiryDate, type, isActive);
    }
}

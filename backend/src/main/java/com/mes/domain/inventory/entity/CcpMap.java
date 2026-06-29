package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CcpMap extends BaseEntity {

    private UUID ccpMapId;
    private UUID factoryId;
    private String processCode;
    private String processName;
    private boolean isCcp;
    private String ccpReason; // CCPとして特定された理由
    private String hazardMitigated; // 緩和される危害要因

    public CcpMap(UUID ccpMapId, UUID factoryId, String processCode, String processName,
                  boolean isCcp, String ccpReason, String hazardMitigated) {
        super(ccpMapId, java.time.LocalDateTime.now());
        this.ccpMapId = ccpMapId;
        this.factoryId = factoryId;
        this.processCode = processCode;
        this.processName = processName;
        this.isCcp = isCcp;
        this.ccpReason = ccpReason;
        this.hazardMitigated = hazardMitigated;
    }

    @Override
    public CcpMap copy(UUID newId) {
        return new CcpMap(newId, factoryId, processCode, processName, isCcp, ccpReason, hazardMitigated);
    }
}
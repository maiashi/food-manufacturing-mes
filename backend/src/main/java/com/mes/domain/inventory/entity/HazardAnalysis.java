package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HazardAnalysis extends BaseEntity {

    private UUID hazardId;
    private UUID factoryId;
    private String hazardCode;
    private String hazardName;
    private String hazardCategory; // 生物学的/化学的/物理的
    private String severityLevel; // 高/中/低
    private String probabilityLevel; // 高/中/低
    private String acceptability; // 許容/不許容
    private String analysisReport;

    public HazardAnalysis(UUID hazardId, UUID factoryId, String hazardCode, String hazardName,
                          String hazardCategory, String severityLevel, String probabilityLevel,
                          String acceptability, String analysisReport) {
        super(hazardId, java.time.LocalDateTime.now());
        this.hazardId = hazardId;
        this.factoryId = factoryId;
        this.hazardCode = hazardCode;
        this.hazardName = hazardName;
        this.hazardCategory = hazardCategory;
        this.severityLevel = severityLevel;
        this.probabilityLevel = probabilityLevel;
        this.acceptability = acceptability;
        this.analysisReport = analysisReport;
    }

    @Override
    public HazardAnalysis copy(UUID newId) {
        return new HazardAnalysis(newId, factoryId, hazardCode, hazardName, hazardCategory,
                severityLevel, probabilityLevel, acceptability, analysisReport);
    }
}
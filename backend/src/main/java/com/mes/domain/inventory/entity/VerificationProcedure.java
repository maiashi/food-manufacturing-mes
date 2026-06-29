package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationProcedure extends BaseEntity {

    private UUID verificationId;
    private UUID factoryId;
    private String verificationCode;
    private String verificationType; // CCP検証計画/機器校正記録/HACCP計画見直し/内部監査管理/統計解析
    private java.time.LocalDateTime verificationDate;
    private String verificationResult; // 合格/不合格/要改善
    private String findings;
    private String recommendations;
    private UUID verifiedBy;

    public VerificationProcedure(UUID verificationId, UUID factoryId, String verificationCode,
                                 String verificationType, java.time.LocalDateTime verificationDate,
                                 String verificationResult, String findings, String recommendations,
                                 UUID verifiedBy) {
        super(verificationId, java.time.LocalDateTime.now());
        this.verificationId = verificationId;
        this.factoryId = factoryId;
        this.verificationCode = verificationCode;
        this.verificationType = verificationType;
        this.verificationDate = verificationDate;
        this.verificationResult = verificationResult;
        this.findings = findings;
        this.recommendations = recommendations;
        this.verifiedBy = verifiedBy;
    }

    @Override
    public VerificationProcedure copy(UUID newId) {
        return new VerificationProcedure(newId, factoryId, verificationCode, verificationType,
                verificationDate, verificationResult, findings, recommendations, verifiedBy);
    }
}
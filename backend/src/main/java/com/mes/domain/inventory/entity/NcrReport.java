package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NcrReport extends BaseEntity {

    private UUID ncrId;
    private UUID factoryId;
    private String ncrCode;
    private UUID lotId;
    private String lotNumber;
    private String ncrReason; // 原因
    private String ncrDescription; // 不適合内容
    private String status; // 発生/調査中/是正措置済/閉鎖
    private java.time.LocalDateTime reportedAt;
    private UUID reportedBy;
    private java.time.LocalDateTime closedAt;
    private UUID closedBy;

    public NcrReport(UUID ncrId, UUID factoryId, String ncrCode, UUID lotId, String lotNumber,
                     String ncrReason, String ncrDescription, UUID reportedBy) {
        super(ncrId, java.time.LocalDateTime.now());
        this.ncrId = ncrId;
        this.factoryId = factoryId;
        this.ncrCode = ncrCode;
        this.lotId = lotId;
        this.lotNumber = lotNumber;
        this.ncrReason = ncrReason;
        this.ncrDescription = ncrDescription;
        this.status = "発生";
        this.reportedAt = java.time.LocalDateTime.now();
        this.reportedBy = reportedBy;
        this.closedAt = null;
        this.closedBy = null;
    }

    public void investigate() {
        this.status = "調査中";
    }

    public void completeCorrectiveAction() {
        this.status = "是正措置済";
    }

    public void closeNcr(UUID closedBy) {
        this.status = "閉鎖";
        this.closedAt = java.time.LocalDateTime.now();
        this.closedBy = closedBy;
    }

    @Override
    public NcrReport copy(UUID newId) {
        NcrReport copy = new NcrReport(newId, factoryId, ncrCode, lotId, lotNumber,
                ncrReason, ncrDescription, reportedBy);
        copy.setStatus(this.status);
        copy.setReportedAt(this.reportedAt);
        copy.setClosedAt(this.closedAt);
        copy.setClosedBy(this.closedBy);
        return copy;
    }
}
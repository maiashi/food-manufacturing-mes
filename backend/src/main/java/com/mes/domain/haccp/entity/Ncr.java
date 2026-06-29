package com.mes.domain.haccp.entity;

import com.mes.domain.common.BaseEntity;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Ncr extends BaseEntity {

    private UUID ncrId;
    private UUID factoryId;
    private String ncrNumber;
    private UUID lotId;
    private String causeCategory;
    private String description;
    private LocalDateTime reportedDate;
    private UUID reportedByUserId;

    public Ncr(UUID ncrId, UUID factoryId, String ncrNumber, UUID lotId,
               String causeCategory, String description, LocalDateTime reportedDate,
               UUID reportedByUserId) {
        super(ncrId, java.time.LocalDateTime.now());
        this.ncrId = ncrId;
        this.factoryId = factoryId;
        this.ncrNumber = ncrNumber;
        this.lotId = lotId;
        this.causeCategory = causeCategory;
        this.description = description;
        this.reportedDate = reportedDate;
        this.reportedByUserId = reportedByUserId;
    }

    @Override
    public Ncr copy(UUID newId) {
        return new Ncr(newId, factoryId, ncrNumber, lotId, causeCategory, description,
                reportedDate, reportedByUserId);
    }
}

package com.mes.domain.haccp.entity;

import com.mes.domain.common.BaseEntity;
import com.mes.domain.haccp.enums.CapaStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Capa extends BaseEntity {

    private UUID capaId;
    private String referenceType;
    private UUID referenceId;
    private String description;
    private String rootCauseAnalysis;
    private String correctiveActionPlan;
    private LocalDate dueDate;
    private LocalDateTime completedDate;
    private CapaStatus status;

    public Capa(UUID capaId, String referenceType, UUID referenceId, String description,
                String rootCauseAnalysis, String correctiveActionPlan, LocalDate dueDate,
                LocalDateTime completedDate, CapaStatus status) {
        super(capaId, java.time.LocalDateTime.now());
        this.capaId = capaId;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.description = description;
        this.rootCauseAnalysis = rootCauseAnalysis;
        this.correctiveActionPlan = correctiveActionPlan;
        this.dueDate = dueDate;
        this.completedDate = completedDate;
        this.status = status;
    }

    @Override
    public Capa copy(UUID newId) {
        return new Capa(newId, referenceType, referenceId, description, rootCauseAnalysis,
                correctiveActionPlan, dueDate, completedDate, status);
    }
}

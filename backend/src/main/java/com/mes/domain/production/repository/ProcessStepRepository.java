package com.mes.domain.production.repository;

import com.mes.domain.production.entity.ProcessStep;

import java.util.List;
import java.util.UUID;

public interface ProcessStepRepository {

    List<ProcessStep> findByProcessFlowIdOrderByStepOrder(UUID processFlowId);

    List<ProcessStep> findByProcessFlowIdAndIsCcp(UUID processFlowId);

    ProcessStep save(ProcessStep step);
}

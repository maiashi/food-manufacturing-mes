package com.mes.domain.production.repository;

import com.mes.domain.production.entity.ProcessFlow;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProcessFlowRepository {

    Optional<ProcessFlow> findById(UUID id);

    List<ProcessFlow> findByProductId(UUID productId);

    ProcessFlow save(ProcessFlow processFlow);
}

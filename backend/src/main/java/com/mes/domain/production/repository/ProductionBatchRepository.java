package com.mes.domain.production.repository;

import com.mes.domain.production.entity.ProductionBatch;
import com.mes.domain.production.enums.BatchStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductionBatchRepository {

    Optional<ProductionBatch> findById(UUID id);

    List<ProductionBatch> findByProductionOrderId(UUID productionOrderId);

    List<ProductionBatch> findByLineIdAndDateRange(UUID lineId, LocalDate start, LocalDate end);

    List<ProductionBatch> findByStatus(BatchStatus status);

    ProductionBatch save(ProductionBatch batch);
}

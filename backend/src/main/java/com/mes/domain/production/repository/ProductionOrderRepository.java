package com.mes.domain.production.repository;

import com.mes.domain.production.entity.ProductionOrder;
import com.mes.domain.production.enums.ProductionOrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductionOrderRepository {

    Optional<ProductionOrder> findById(UUID id);

    List<ProductionOrder> findByFactoryIdAndStatus(UUID factoryId, ProductionOrderStatus... statuses);

    Optional<ProductionOrder> findByOrderNumber(String orderNumber);

    ProductionOrder save(ProductionOrder productionOrder);
}

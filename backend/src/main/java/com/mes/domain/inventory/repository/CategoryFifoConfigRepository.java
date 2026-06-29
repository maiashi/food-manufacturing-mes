package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.CategoryFifoConfig;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryFifoConfigRepository {

    Optional<CategoryFifoConfig> findById(UUID id);

    List<CategoryFifoConfig> findByFactoryId(UUID factoryId);

    List<CategoryFifoConfig> findByCategoryCode(String categoryCode);

    CategoryFifoConfig save(CategoryFifoConfig config);

    void delete(UUID configId);
}
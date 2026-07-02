package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.CategoryStorageConfig;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryStorageConfigRepository {

    Optional<CategoryStorageConfig> findByFactoryIdAndCategoryName(UUID factoryId, String categoryName);

    List<CategoryStorageConfig> findByFactoryId(UUID factoryId);

    CategoryStorageConfig save(CategoryStorageConfig config);

    void delete(UUID configId);
}
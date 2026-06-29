package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.MaterialMaster;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MaterialMasterRepository {

    Optional<MaterialMaster> findById(UUID id);

    Optional<MaterialMaster> findBySkuCode(String skuCode);

    List<MaterialMaster> findAll();

    MaterialMaster save(MaterialMaster materialMaster);
}

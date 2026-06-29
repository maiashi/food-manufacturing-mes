package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.CriticalLimit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CriticalLimitRepository {

    Optional<CriticalLimit> findById(UUID id);

    List<CriticalLimit> findByFactoryId(UUID factoryId);

    List<CriticalLimit> findByCcpMapId(UUID ccpMapId);

    List<CriticalLimit> findByLimitType(String limitType);

    CriticalLimit save(CriticalLimit criticalLimit);

    void delete(UUID limitId);
}
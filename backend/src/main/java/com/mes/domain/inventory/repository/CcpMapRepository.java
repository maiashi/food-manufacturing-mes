package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.CcpMap;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CcpMapRepository {

    Optional<CcpMap> findById(UUID id);

    List<CcpMap> findByFactoryId(UUID factoryId);

    List<CcpMap> findByProcessCode(String processCode);

    List<CcpMap> findByIsCcp(boolean isCcp);

    CcpMap save(CcpMap ccpMap);

    void delete(UUID ccpMapId);
}
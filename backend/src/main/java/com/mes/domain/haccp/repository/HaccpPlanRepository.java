package com.mes.domain.haccp.repository;

import com.mes.domain.haccp.entity.HaccpPlan;
import com.mes.domain.haccp.enums.HaccpPlanType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HaccpPlanRepository {

    Optional<HaccpPlan> findById(UUID id);

    List<HaccpPlan> findByFactoryId(UUID factoryId);

    List<HaccpPlan> findByVersion(String version);

    HaccpPlan save(HaccpPlan haccpPlan);

    void delete(UUID id);
}

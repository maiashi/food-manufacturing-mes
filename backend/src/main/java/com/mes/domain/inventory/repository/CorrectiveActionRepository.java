package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.CorrectiveAction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CorrectiveActionRepository {

    Optional<CorrectiveAction> findById(UUID id);

    List<CorrectiveAction> findByFactoryId(UUID factoryId);

    List<CorrectiveAction> findByCcpMonitoringRecordId(UUID ccpMonitoringRecordId);

    List<CorrectiveAction> findByTakenBy(UUID takenBy);

    CorrectiveAction save(CorrectiveAction action);

    void delete(UUID actionId);
}
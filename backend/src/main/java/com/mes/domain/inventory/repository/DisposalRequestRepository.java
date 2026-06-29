package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.DisposalRequest;
import com.mes.domain.inventory.enums.DisposalReason;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DisposalRequestRepository {

    Optional<DisposalRequest> findById(UUID id);

    List<DisposalRequest> findByFactoryIdAndStatus(UUID factoryId, String status);

    List<DisposalRequest> findByLotId(UUID lotId);

    List<DisposalRequest> findByReason(DisposalReason reason);

    List<DisposalRequest> findByRequestedAtBetween(LocalDateTime start, LocalDateTime end);

    DisposalRequest save(DisposalRequest request);

    void delete(UUID requestId);
}
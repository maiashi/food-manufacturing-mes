package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.LocationTransfer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationTransferRepository {

    Optional<LocationTransfer> findById(UUID id);

    List<LocationTransfer> findByFactoryIdAndStatus(UUID factoryId, String status);

    List<LocationTransfer> findByLotId(UUID lotId);

    List<LocationTransfer> findByRequestedBy(UUID requestedBy);

    List<LocationTransfer> findByApprovedBy(UUID approvedBy);

    LocationTransfer save(LocationTransfer transfer);

    void delete(UUID transferId);
}
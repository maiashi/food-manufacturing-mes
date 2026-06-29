package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.NcrReport;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NcrReportRepository {

    Optional<NcrReport> findById(UUID id);

    List<NcrReport> findByFactoryId(UUID factoryId);

    List<NcrReport> findByLotId(UUID lotId);

    List<NcrReport> findByStatus(String status);

    List<NcrReport> findByReportedBy(UUID reportedBy);

    NcrReport save(NcrReport report);

    void delete(UUID ncrId);
}
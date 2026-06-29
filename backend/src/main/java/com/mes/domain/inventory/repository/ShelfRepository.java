package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.Shelf;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShelfRepository {

    Optional<Shelf> findById(UUID id);

    List<Shelf> findByFactoryIdAndZoneId(UUID factoryId, UUID zoneId);

    List<Shelf> findByZoneId(UUID zoneId);

    List<Shelf> findByStatus(String status);

    Shelf save(Shelf shelf);

    void delete(UUID shelfId);
}
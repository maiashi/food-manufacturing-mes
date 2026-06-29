package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.Receipt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReceiptRepository {

    Optional<Receipt> findById(UUID id);

    List<Receipt> findByPoId(UUID poId);

    List<Receipt> findByFactoryId(UUID factoryId);

    Receipt save(Receipt receipt);
}

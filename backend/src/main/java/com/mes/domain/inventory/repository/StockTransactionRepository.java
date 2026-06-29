package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.StockTransaction;

import java.util.List;
import java.util.UUID;

public interface StockTransactionRepository {

    List<StockTransaction> findByLotIdOrderByCreatedAtDesc(UUID lotId);

    List<StockTransaction> bulkInsert(List<StockTransaction> transactions);

    StockTransaction save(StockTransaction transaction);
}

package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.Transfer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 保管場所間移動（Transfer）リポジトリインタフェース
 */
public interface TransferRepository {

    /**
     * IDで移動記録を取得する。
     */
    Transfer findById(UUID transferId);

    /**
     * ファクトリIDで移動記録を取得する。
     */
    List<Transfer> findByFactoryId(UUID factoryId);

    /**
     * 出所・先でフィルタした移動記録を取得する。
     */
    List<Transfer> findByFromToLocation(UUID fromShelfId, UUID toBinId);

    /**
     * 日範囲でフィルタした移動記録を取得する。
     */
    List<Transfer> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    /**
     * 移動記録を保存する。
     */
    Transfer save(Transfer transfer);
}

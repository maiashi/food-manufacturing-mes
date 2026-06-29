package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.InventoryReservation;
import java.util.List;
import java.util.UUID;

/**
 * 在庫予約リポジトリインタフェース（STK-002: FIFO予約・保留管理）
 */
public interface InventoryReservationRepository {

    /**
     * IDで予約を取得する。
     */
    InventoryReservation findById(UUID reservationId);

    /**
     * ロット番号とファクトリで予約を取得する。
     */
    List<InventoryReservation> findByFactoryIdAndLotNumber(UUID factoryId, String lotNumber);

    /**
     * ソースドキュメントタイプとIDで予約を取得する。
     */
    List<InventoryReservation> findBySourceDoc(String sourceDocType, String sourceDocId);

    /**
     * 予約を保存する。
     */
    InventoryReservation save(InventoryReservation reservation);
}

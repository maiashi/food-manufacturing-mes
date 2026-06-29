package com.mes.domain.traceability.enums;

/**
 * 出荷ステータス (Shipment Status)
 */
public enum ShipmentStatus {
    PENDING,      // 出庫待ち
    SHIPPED,      // 出庫済み
    IN_TRANSIT,   // 配送中
    DELIVERED,    // 配達完了
    CANCELLED     // キャンセル
}

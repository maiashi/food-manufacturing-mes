package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BarcodeScanRecord extends BaseEntity {

    private UUID scanId;
    private UUID factoryId;
    private String scanType; // LOT_NUMBER/WAREHOUSE_ID/ZONE_ID/SHELF_ID/TRANSFER_ID
    private String scanValue; // スキャンされた値
    private String actionType; // CREATE_TRANSFER/EXECUTE_TRANSFER/CANCEL_TRANSFER
    private String status; // 処理中/完了/エラー
    private String errorMessage; // エラーメッセージ
    private UUID relatedTransferId; // 関連する移動伝票ID
    private UUID scannedBy; // スキャンしたユーザーID
    private java.time.LocalDateTime scannedAt;

    public BarcodeScanRecord(UUID scanId, UUID factoryId, String scanType, String scanValue,
                             String actionType, UUID scannedBy) {
        super(scanId, java.time.LocalDateTime.now());
        this.scanId = scanId;
        this.factoryId = factoryId;
        this.scanType = scanType;
        this.scanValue = scanValue;
        this.actionType = actionType;
        this.status = "処理中";
        this.scannedBy = scannedBy;
        this.scannedAt = java.time.LocalDateTime.now();
    }

    public void complete() {
        this.status = "完了";
    }

    public void error(String errorMessage) {
        this.status = "エラー";
        this.errorMessage = errorMessage;
    }

    @Override
    public BarcodeScanRecord copy(UUID newId) {
        BarcodeScanRecord copy = new BarcodeScanRecord(newId, factoryId, scanType, scanValue,
                actionType, scannedBy);
        copy.setStatus(this.status);
        copy.setErrorMessage(this.errorMessage);
        copy.setRelatedTransferId(this.relatedTransferId);
        copy.setScannedAt(this.scannedAt);
        return copy;
    }
}
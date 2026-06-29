package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InspectionRecord extends BaseEntity {

    private UUID recordId;
    private UUID factoryId;
    private UUID inspectionSpecId;
    private UUID lotId;
    private String lotNumber;
    private java.math.BigDecimal inspectedValue;
    private String unit;
    private java.time.LocalDateTime inspectedAt;
    private UUID inspectedBy;
    private String inspectionResult; // 合格/不合格/要再検査
    private String photoAttachment; // 写真添付のURLまたはパス

    public InspectionRecord(UUID recordId, UUID factoryId, UUID inspectionSpecId, UUID lotId,
                            String lotNumber, java.math.BigDecimal inspectedValue, String unit,
                            java.time.LocalDateTime inspectedAt, UUID inspectedBy, String inspectionResult) {
        super(recordId, java.time.LocalDateTime.now());
        this.recordId = recordId;
        this.factoryId = factoryId;
        this.inspectionSpecId = inspectionSpecId;
        this.lotId = lotId;
        this.lotNumber = lotNumber;
        this.inspectedValue = inspectedValue;
        this.unit = unit;
        this.inspectedAt = inspectedAt;
        this.inspectedBy = inspectedBy;
        this.inspectionResult = inspectionResult;
        this.photoAttachment = null;
    }

    public void addPhotoAttachment(String photoAttachment) {
        this.photoAttachment = photoAttachment;
    }

    @Override
    public InspectionRecord copy(UUID newId) {
        InspectionRecord copy = new InspectionRecord(newId, factoryId, inspectionSpecId, lotId,
                lotNumber, inspectedValue, unit, inspectedAt, inspectedBy, inspectionResult);
        copy.setPhotoAttachment(this.photoAttachment);
        return copy;
    }
}
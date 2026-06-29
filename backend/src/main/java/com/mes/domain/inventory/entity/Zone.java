package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Zone extends BaseEntity {

    private UUID zoneId;
    private UUID factoryId;
    private UUID warehouseId;
    private String zoneCode;
    private String zoneName;
    private String temperatureZone; // 温度帯継承/上書き
    private boolean inheritTemperatureFromWarehouse;
    private String zoneType; // 原料Zone/調合Zone/加熱Zone/冷却Zone等

    public Zone(UUID zoneId, UUID factoryId, UUID warehouseId, String zoneCode, String zoneName,
                String temperatureZone, boolean inheritTemperatureFromWarehouse, String zoneType) {
        super(zoneId, java.time.LocalDateTime.now());
        this.zoneId = zoneId;
        this.factoryId = factoryId;
        this.warehouseId = warehouseId;
        this.zoneCode = zoneCode;
        this.zoneName = zoneName;
        this.temperatureZone = temperatureZone;
        this.inheritTemperatureFromWarehouse = inheritTemperatureFromWarehouse;
        this.zoneType = zoneType;
    }

    @Override
    public Zone copy(UUID newId) {
        return new Zone(newId, factoryId, warehouseId, zoneCode, zoneName, temperatureZone,
                inheritTemperatureFromWarehouse, zoneType);
    }
}
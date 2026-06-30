package com.mes.domain.inventory.service;

import com.mes.domain.inventory.entity.Lot;
import com.mes.domain.inventory.enums.LotStatus;
import com.mes.domain.inventory.repository.LotRepository;
import com.mes.domain.inventory.vo.StockQuantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private LotRepository lotRepository;

    @InjectMocks
    private InventoryService service;

    private UUID materialId;
    private UUID warehouseId;
    private UUID factoryId;

    @BeforeEach
    void setUp() {
        materialId = UUID.randomUUID();
        warehouseId = UUID.randomUUID();
        factoryId = UUID.randomUUID();
    }

    @Test
    void allocateStock_validLotSucceeds() {
        String lotNumber = "LOT-2025-001";
        BigDecimal qty = new BigDecimal("10.5");

        Lot lot = createLot(lotNumber, materialId, warehouseId, LotStatus.RECEIVED, new BigDecimal("50"));
        when(lotRepository.findByLotNumber(lotNumber)).thenReturn(List.of(lot));
        when(lotRepository.save(any(Lot.class))).thenAnswer(inv -> inv.getArgument(0));

        StockQuantity result = service.allocateStock(lotNumber, qty);

        assertNotNull(result);
        assertEquals(new BigDecimal("10.500000"), result.toBigDecimal());
    }

    @Test
    void allocateStock_insufficientQtyThrows() {
        String lotNumber = "LOT-LOW-STOCK";

        Lot lot = createLot(lotNumber, materialId, warehouseId, LotStatus.RECEIVED, BigDecimal.ZERO);
        when(lotRepository.findByLotNumber(lotNumber)).thenReturn(List.of(lot));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            service.allocateStock(lotNumber, new BigDecimal("10"));
        });

        assertTrue(exception.getMessage().contains("no available stock"));
    }

    @Test
    void allocateStock_nonExistentLotThrows() {
        String unknownLot = "LOT-NONEXISTENT";

        when(lotRepository.findByLotNumber(unknownLot)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.allocateStock(unknownLot, new BigDecimal("10"));
        });

        assertTrue(exception.getMessage().contains("No lot found"));
    }

    @Test
    void allocateStock_quarantinedLotThrows() {
        String lotNumber = "LOT-QUARANTINE";

        Lot lot = createLot(lotNumber, materialId, warehouseId, LotStatus.QUARANTINE, new BigDecimal("100"));
        when(lotRepository.findByLotNumber(lotNumber)).thenReturn(List.of(lot));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            service.allocateStock(lotNumber, new BigDecimal("10"));
        });

        assertTrue(exception.getMessage().contains("not available for allocation"));
    }

    @Test
    void allocateStock_partialAllocation() {
        String lotNumber = "LOT-PARTIAL";
        BigDecimal requestedQty = new BigDecimal("75");
        BigDecimal availableQty = new BigDecimal("50");

        Lot lot = createLot(lotNumber, materialId, warehouseId, LotStatus.RECEIVED, availableQty);
        when(lotRepository.findByLotNumber(lotNumber)).thenReturn(List.of(lot));
        when(lotRepository.save(any(Lot.class))).thenAnswer(inv -> inv.getArgument(0));

        StockQuantity result = service.allocateStock(lotNumber, requestedQty);

        // Should allocate only what's available
        assertEquals(new BigDecimal("50.000000"), result.toBigDecimal());
    }

    @Test
    void releaseAllocation_succeeds() {
        String lotNumber = "LOT-RELEASE";
        BigDecimal qty = new BigDecimal("20");
        BigDecimal currentQty = new BigDecimal("30");

        Lot lot = createLot(lotNumber, materialId, warehouseId, LotStatus.RECEIVED, currentQty);
        when(lotRepository.findByLotNumber(lotNumber)).thenReturn(List.of(lot));
        when(lotRepository.save(any(Lot.class))).thenAnswer(inv -> inv.getArgument(0));

        assertDoesNotThrow(() -> service.releaseAllocation(lotNumber, qty));
    }

    @Test
    void getAvailableStock_returnsOnlyMatchingLots() {
        UUID otherMaterialId = UUID.randomUUID();
        Lot lotA = createLot("LOT-A", materialId, warehouseId, LotStatus.RECEIVED, new BigDecimal("30"));
        Lot lotB = createLot("LOT-B", otherMaterialId, warehouseId, LotStatus.RECEIVED, new BigDecimal("20"));
        Lot quarantined = createLot("LOT-Q", materialId, warehouseId, LotStatus.QUARANTINE, new BigDecimal("50"));

        when(lotRepository.findByWarehouseId(warehouseId)).thenReturn(List.of(lotA, lotB, quarantined));

        List<Lot> available = service.getAvailableStock(materialId, warehouseId);

        assertEquals(1, available.size());
        assertEquals("LOT-A", available.get(0).getLotNumber());
    }

    @Test
    void getAvailableStock_emptyWarehouseReturnsEmptyList() {
        when(lotRepository.findByWarehouseId(warehouseId)).thenReturn(Collections.emptyList());

        List<Lot> available = service.getAvailableStock(materialId, warehouseId);

        assertNotNull(available);
        assertTrue(available.isEmpty());
    }

    @Test
    void getAvailableStock_filtersByMaterial() {
        UUID otherMaterialId = UUID.randomUUID();
        Lot lot1 = createLot("LOT-1", materialId, warehouseId, LotStatus.RECEIVED, new BigDecimal("50"));
        Lot lot2 = createLot("LOT-2", otherMaterialId, warehouseId, LotStatus.RECEIVED, new BigDecimal("30"));

        when(lotRepository.findByWarehouseId(warehouseId)).thenReturn(List.of(lot1, lot2));

        List<Lot> available = service.getAvailableStock(materialId, warehouseId);

        assertEquals(1, available.size());
        assertEquals("LOT-1", available.get(0).getLotNumber());
    }

    private Lot createLot(String lotNumber, UUID materialId, UUID warehouseId,
                          LotStatus status, BigDecimal quantity) {
        UUID lotId = UUID.randomUUID();
        return new Lot(lotId, factoryId, lotNumber, materialId, null,
                null,
                LocalDate.now(), LocalDate.now().plusDays(180),
                quantity, status, warehouseId, null, null, null, false);
    }
}

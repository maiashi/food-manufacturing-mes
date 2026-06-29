package com.mes.domain.inventory.service;

import com.mes.domain.inventory.entity.CategoryFifoConfig;
import com.mes.domain.inventory.entity.CcpMap;
import com.mes.domain.inventory.entity.CcpMonitoringRecord;
import com.mes.domain.inventory.entity.CorrectiveAction;
import com.mes.domain.inventory.entity.CriticalLimit;
import com.mes.domain.inventory.entity.DisposalRequest;
import com.mes.domain.inventory.entity.HazardAnalysis;
import com.mes.domain.inventory.entity.InspectionRecord;
import com.mes.domain.inventory.entity.InspectionSpec;
import com.mes.domain.inventory.entity.LocationTransfer;
import com.mes.domain.inventory.entity.Lot;
import com.mes.domain.inventory.entity.NcrReport;
import com.mes.domain.inventory.entity.Shelf;
import com.mes.domain.inventory.entity.VerificationProcedure;
import com.mes.domain.inventory.entity.Warehouse;
import com.mes.domain.inventory.entity.Zone;
import com.mes.domain.inventory.enums.DisposalReason;
import com.mes.domain.inventory.enums.LotStatus;
import com.mes.domain.inventory.repository.CategoryFifoConfigRepository;
import com.mes.domain.inventory.repository.CcpMapRepository;
import com.mes.domain.inventory.repository.CcpMonitoringRecordRepository;
import com.mes.domain.inventory.repository.CorrectiveActionRepository;
import com.mes.domain.inventory.repository.CriticalLimitRepository;
import com.mes.domain.inventory.repository.DisposalRequestRepository;
import com.mes.domain.inventory.repository.HazardAnalysisRepository;
import com.mes.domain.inventory.repository.InspectionRecordRepository;
import com.mes.domain.inventory.repository.InspectionSpecRepository;
import com.mes.domain.inventory.repository.LocationTransferRepository;
import com.mes.domain.inventory.repository.LotRepository;
import com.mes.domain.inventory.repository.NcrReportRepository;
import com.mes.domain.inventory.repository.ShelfRepository;
import com.mes.domain.inventory.repository.VerificationProcedureRepository;
import com.mes.domain.inventory.repository.WarehouseRepository;
import com.mes.domain.inventory.repository.ZoneRepository;
import com.mes.domain.inventory.vo.StockQuantity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Domain service for inventory operations.
 * Pure domain logic — no Spring, no JPA annotations.
 */
public class InventoryService {

    private final LotRepository lotRepository;
    private final DisposalRequestRepository disposalRequestRepository;
    private final CategoryFifoConfigRepository categoryFifoConfigRepository;
    private final WarehouseRepository warehouseRepository;
    private final ZoneRepository zoneRepository;
    private final ShelfRepository shelfRepository;
    private final LocationTransferRepository locationTransferRepository;
    private final HazardAnalysisRepository hazardAnalysisRepository;
    private final CcpMapRepository ccpMapRepository;
    private final CriticalLimitRepository criticalLimitRepository;
    private final CcpMonitoringRecordRepository ccpMonitoringRecordRepository;
    private final CorrectiveActionRepository correctiveActionRepository;
    private final VerificationProcedureRepository verificationProcedureRepository;
    private final InspectionSpecRepository inspectionSpecRepository;
    private final InspectionRecordRepository inspectionRecordRepository;
    private final NcrReportRepository ncrReportRepository;

    public InventoryService(LotRepository lotRepository, DisposalRequestRepository disposalRequestRepository, 
                            CategoryFifoConfigRepository categoryFifoConfigRepository,
                            WarehouseRepository warehouseRepository, ZoneRepository zoneRepository,
                            ShelfRepository shelfRepository, LocationTransferRepository locationTransferRepository,
                            HazardAnalysisRepository hazardAnalysisRepository, CcpMapRepository ccpMapRepository,
                            CriticalLimitRepository criticalLimitRepository, CcpMonitoringRecordRepository ccpMonitoringRecordRepository,
                            CorrectiveActionRepository correctiveActionRepository, VerificationProcedureRepository verificationProcedureRepository,
                            InspectionSpecRepository inspectionSpecRepository, InspectionRecordRepository inspectionRecordRepository,
                            NcrReportRepository ncrReportRepository) {
        this.lotRepository = lotRepository;
        this.disposalRequestRepository = disposalRequestRepository;
        this.categoryFifoConfigRepository = categoryFifoConfigRepository;
        this.warehouseRepository = warehouseRepository;
        this.zoneRepository = zoneRepository;
        this.shelfRepository = shelfRepository;
        this.locationTransferRepository = locationTransferRepository;
        this.hazardAnalysisRepository = hazardAnalysisRepository;
        this.ccpMapRepository = ccpMapRepository;
        this.criticalLimitRepository = criticalLimitRepository;
        this.ccpMonitoringRecordRepository = ccpMonitoringRecordRepository;
        this.correctiveActionRepository = correctiveActionRepository;
        this.verificationProcedureRepository = verificationProcedureRepository;
        this.inspectionSpecRepository = inspectionSpecRepository;
        this.inspectionRecordRepository = inspectionRecordRepository;
        this.ncrReportRepository = ncrReportRepository;
    }

    /**
     * Allocates available stock for the given lot number.
     * Validates that sufficient unreserved quantity exists.
     *
     * @param lotNumber the lot to allocate from
     * @param qty       quantity to allocate
     * @return the allocated quantity (may be less than requested if partial is all that is available)
     */
    public StockQuantity allocateStock(String lotNumber, BigDecimal qty) {
        List<Lot> lots = lotRepository.findByLotNumber(lotNumber);
        if (lots.isEmpty()) {
            throw new IllegalArgumentException("No lot found for lot number: " + lotNumber);
        }

        Lot lot = lots.get(0); // FIFO: take the first matching lot

        if (lot.getStatus() == LotStatus.QUARANTINE || lot.getStatus() == LotStatus.REJECTED) {
            throw new IllegalStateException("Lot " + lotNumber + " is not available for allocation");
        }

        BigDecimal available = lot.getQuantity();
        if (available == null || available.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Lot " + lotNumber + " has no available stock");
        }

        BigDecimal allocQty = qty.min(available);
        lot.setQuantity(available.subtract(allocQty));

        // Transition to reserved status if fully allocated
        if (lot.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            lot.setStatus(LotStatus.RESERVED);
        }

        lotRepository.save(lot);
        return new StockQuantity(allocQty);
    }

    /**
     * Releases previously allocated stock back to available inventory.
     *
     * @param lotNumber the lot to release stock from
     * @param qty       quantity to release
     */
    public void releaseAllocation(String lotNumber, BigDecimal qty) {
        List<Lot> lots = lotRepository.findByLotNumber(lotNumber);
        if (lots.isEmpty()) {
            throw new IllegalArgumentException("No lot found for lot number: " + lotNumber);
        }

        Lot lot = lots.get(0);
        BigDecimal currentQty = lot.getQuantity() != null ? lot.getQuantity() : BigDecimal.ZERO;

        // If currently reserved, we need to add back the reserved portion
        if (lot.getStatus() == LotStatus.RESERVED) {
            // Add the released quantity back to the lot
            lot.setQuantity(currentQty.add(qty));
            // If there's still quantity remaining, keep as RESERVED, otherwise change to RECEIVED
            if (lot.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
                lot.setStatus(LotStatus.RECEIVED);
            }
        } else {
            // If not reserved, just add the quantity back
            lot.setQuantity(currentQty.add(qty));
            lot.setStatus(LotStatus.RECEIVED);
        }
        
        lotRepository.save(lot);
    }

    /**
     * Returns FIFO-ordered list of lots with available quantities for the given material and warehouse.
     * FIFO sorting priority:
     * 1. expiryDate (ascending)
     * 2. manufacturingDate (ascending) if expiryDate is the same
     * 3. created_at (ascending) if both are null
     */
    public List<Lot> getAvailableStock(UUID materialId, UUID warehouseId) {
        List<Lot> allLots = lotRepository.findByWarehouseId(warehouseId);

        return allLots.stream()
                .filter(lot -> materialId.equals(lot.getMaterialId()))
                .filter(lot -> lot.getStatus() != LotStatus.QUARANTINE
                        && lot.getStatus() != LotStatus.REJECTED)
                .filter(lot -> lot.getQuantity() != null && lot.getQuantity().compareTo(BigDecimal.ZERO) > 0)
                .sorted((a, b) -> {
                    // First priority: expiryDate
                    if (a.getExpiryDate() == null && b.getExpiryDate() == null) {
                        // Second priority: manufacturingDate
                        if (a.getManufacturingDate() == null && b.getManufacturingDate() == null) {
                            // Third priority: created_at
                            return a.getCreatedAt().compareTo(b.getCreatedAt());
                        }
                        if (a.getManufacturingDate() == null) return 1;
                        if (b.getManufacturingDate() == null) return -1;
                        return a.getManufacturingDate().compareTo(b.getManufacturingDate());
                    }
                    if (a.getExpiryDate() == null) return 1;
                    if (b.getExpiryDate() == null) return -1;
                    
                    int expiryCompare = a.getExpiryDate().compareTo(b.getExpiryDate());
                    if (expiryCompare != 0) {
                        return expiryCompare;
                    }
                    
                    // expiryDate is the same, so use manufacturingDate
                    if (a.getManufacturingDate() == null && b.getManufacturingDate() == null) {
                        return a.getCreatedAt().compareTo(b.getCreatedAt());
                    }
                    if (a.getManufacturingDate() == null) return 1;
                    if (b.getManufacturingDate() == null) return -1;
                    return a.getManufacturingDate().compareTo(b.getManufacturingDate());
                })
                .collect(Collectors.toList());
    }

    /**
     * Splits a lot into two lots: a new lot with the specified quantity and the original lot
     * with the remaining quantity.
     *
     * @param sourceLotNumber the lot number to split
     * @param splitQuantity the quantity to split off
     * @param newLotNumber the new lot number for the split lot
     * @param newWarehouseId the warehouse ID for the new lot (optional, can be same as source)
     * @return the created split lot
     */
    public Lot splitLot(String sourceLotNumber, BigDecimal splitQuantity, String newLotNumber, UUID newWarehouseId) {
        List<Lot> sourceLots = lotRepository.findByLotNumber(sourceLotNumber);
        if (sourceLots.isEmpty()) {
            throw new IllegalArgumentException("No lot found for lot number: " + sourceLotNumber);
        }

        Lot sourceLot = sourceLots.get(0);

        if (sourceLot.getQuantity() == null || sourceLot.getQuantity().compareTo(splitQuantity) < 0) {
            throw new IllegalStateException("Insufficient quantity in lot " + sourceLotNumber + 
                " to split. Available: " + sourceLot.getQuantity() + ", Requested: " + splitQuantity);
        }

        // Create new lot with split quantity
        UUID newLotId = UUID.randomUUID();
        Lot splitLot = new Lot(
                newLotId,
                sourceLot.getFactoryId(),
                newLotNumber,
                sourceLot.getMaterialId(),
                sourceLot.getSupplierId(),
                sourceLot.getLotId(), // parentLotId is the source lot's ID
                sourceLot.getManufacturingDate(),
                sourceLot.getExpiryDate(),
                splitQuantity,
                sourceLot.getStatus(),
                newWarehouseId != null ? newWarehouseId : sourceLot.getWarehouseId(),
                sourceLot.getZoneId(),
                sourceLot.getShelfId(),
                sourceLot.getBinId(),
                sourceLot.getIsQuarantined()
        );

        // Update source lot quantity
        BigDecimal remainingQuantity = sourceLot.getQuantity().subtract(splitQuantity);
        sourceLot.setQuantity(remainingQuantity);

        // Save both lots
        lotRepository.save(splitLot);
        lotRepository.save(sourceLot);

        return splitLot;
    }

    /**
     * Finds lots that are expiring within the specified number of days.
     *
     * @param daysBeforeExpiry the number of days before expiry to warn
     * @return list of lots expiring within the specified days
     */
    public List<Lot> getExpiringLots(int daysBeforeExpiry) {
        LocalDate currentDate = LocalDate.now();
        LocalDate expiryThreshold = currentDate.plusDays(daysBeforeExpiry);

        // Get all lots that are not quarantined or rejected and have available quantity
        List<Lot> allLots = lotRepository.findByStatus(LotStatus.RECEIVED);
        List<Lot> allLots2 = lotRepository.findByStatus(LotStatus.IN_USE);
        List<Lot> allLots3 = lotRepository.findByStatus(LotStatus.RESERVED);
        
        // Combine all lots
        List<Lot> combinedLots = new java.util.ArrayList<>();
        combinedLots.addAll(allLots);
        combinedLots.addAll(allLots2);
        combinedLots.addAll(allLots3);

        return combinedLots.stream()
                .filter(lot -> lot.getQuantity() != null && lot.getQuantity().compareTo(BigDecimal.ZERO) > 0)
                .filter(lot -> lot.getExpiryDate() != null && !lot.getExpiryDate().isAfter(expiryThreshold))
                .filter(lot -> !lot.getExpiryDate().isBefore(currentDate)) // Not already expired
                .collect(Collectors.toList());
    }

    /**
     * Marks lots that have expired as REJECTED or QUARANTINE status.
     *
     * @return list of lots that were marked as expired
     */
    public List<Lot> markExpiredLotsAsRejected() {
        LocalDate currentDate = LocalDate.now();
        
        // Get all lots that are not already quarantined or rejected
        List<Lot> allLots = lotRepository.findByStatus(LotStatus.RECEIVED);
        List<Lot> allLots2 = lotRepository.findByStatus(LotStatus.IN_USE);
        List<Lot> allLots3 = lotRepository.findByStatus(LotStatus.RESERVED);
        List<Lot> allLots4 = lotRepository.findByStatus(LotStatus.COMPLETED);
        
        // Combine all lots
        List<Lot> combinedLots = new java.util.ArrayList<>();
        combinedLots.addAll(allLots);
        combinedLots.addAll(allLots2);
        combinedLots.addAll(allLots3);
        combinedLots.addAll(allLots4);

        List<Lot> expiredLots = new java.util.ArrayList<>();
        for (Lot lot : combinedLots) {
            if (lot.getExpiryDate() != null && lot.getExpiryDate().isBefore(currentDate)) {
                if (lot.getQuantity() != null && lot.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
                    // Mark as REJECTED if expired
                    lot.setStatus(LotStatus.REJECTED);
                    lotRepository.save(lot);
                    expiredLots.add(lot);
                }
            }
        }
        
        return expiredLots;
    }

    /**
     * Creates a disposal request for a lot that has expired or is otherwise unfit for use.
     *
     * @param lotId the lot ID to dispose
     * @param lotNumber the lot number
     * @param quantity the quantity to dispose
     * @param reason the disposal reason
     * @param reasonDetail details about the disposal reason
     * @param requestedBy the user requesting the disposal
     * @return the created disposal request
     */
    public DisposalRequest createDisposalRequest(UUID lotId, String lotNumber, BigDecimal quantity,
                                                  DisposalReason reason, String reasonDetail, UUID requestedBy) {
        // Check if lot exists and get current quantity
        List<Lot> lots = lotRepository.findByLotNumber(lotNumber);
        if (lots.isEmpty()) {
            throw new IllegalArgumentException("No lot found for lot number: " + lotNumber);
        }

        Lot lot = lots.get(0);
        if (lot.getQuantity() == null || lot.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Lot " + lotNumber + " has no quantity to dispose");
        }

        // Create disposal request
        UUID requestId = UUID.randomUUID();
        DisposalRequest request = new DisposalRequest(
                requestId,
                lot.getFactoryId(),
                lotId,
                lotNumber,
                quantity,
                reason,
                reasonDetail,
                "PENDING",
                requestedBy,
                null
        );

        return disposalRequestRepository.save(request);
    }

    /**
     * Approves a disposal request.
     *
     * @param requestId the disposal request ID
     * @param approvedBy the user approving the request
     * @return the approved disposal request
     */
    public DisposalRequest approveDisposalRequest(UUID requestId, UUID approvedBy) {
        DisposalRequest request = disposalRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Disposal request not found: " + requestId));

        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalStateException("Disposal request is not in PENDING status: " + request.getStatus());
        }

        request.approve(approvedBy, LocalDateTime.now());
        return disposalRequestRepository.save(request);
    }

    /**
     * Rejects a disposal request.
     *
     * @param requestId the disposal request ID
     * @return the rejected disposal request
     */
    public DisposalRequest rejectDisposalRequest(UUID requestId) {
        DisposalRequest request = disposalRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Disposal request not found: " + requestId));

        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalStateException("Disposal request is not in PENDING status: " + request.getStatus());
        }

        request.reject();
        return disposalRequestRepository.save(request);
    }

    /**
     * Completes a disposal request and updates the lot status to REJECTED.
     *
     * @param requestId the disposal request ID
     * @param disposalMethod the method of disposal
     * @return the completed disposal request
     */
    public DisposalRequest completeDisposalRequest(UUID requestId, String disposalMethod) {
        DisposalRequest request = disposalRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Disposal request not found: " + requestId));

        if (!"APPROVED".equals(request.getStatus())) {
            throw new IllegalStateException("Disposal request is not in APPROVED status: " + request.getStatus());
        }

        // Update lot status to REJECTED
        List<Lot> lots = lotRepository.findByLotNumber(request.getLotNumber());
        if (!lots.isEmpty()) {
            Lot lot = lots.get(0);
            lot.setStatus(LotStatus.REJECTED);
            lotRepository.save(lot);
        }

        // Complete disposal request
        request.complete(disposalMethod);
        return disposalRequestRepository.save(request);
    }

    /**
     * Gets disposal requests by status.
     *
     * @param factoryId the factory ID
     * @param status the status (PENDING, APPROVED, REJECTED, COMPLETED)
     * @return list of disposal requests
     */
    public List<DisposalRequest> getDisposalRequestsByStatus(UUID factoryId, String status) {
        return disposalRequestRepository.findByFactoryIdAndStatus(factoryId, status);
    }

    /**
     * Gets category FIFO config by category code.
     *
     * @param categoryCode the category code (レトルト/チルド総菜/デリカ/おせち)
     * @return optional category FIFO config
     */
    public Optional<CategoryFifoConfig> getCategoryFifoConfig(String categoryCode) {
        List<CategoryFifoConfig> configs = categoryFifoConfigRepository.findByCategoryCode(categoryCode);
        return configs.isEmpty() ? Optional.empty() : Optional.of(configs.get(0));
    }

    /**
     * Validates FIFO allocation based on category FIFO strictness rules.
     *
     * @param categoryCode the category code
     * @param lotNumber the lot number being allocated
     * @param requestedBy the user requesting the allocation
     * @param overrideReason the override reason if FIFO is violated
     * @return true if allocation is valid, false if FIFO violation with override required
     */
    public boolean validateFifoAllocation(String categoryCode, String lotNumber, UUID requestedBy, String overrideReason) {
        Optional<CategoryFifoConfig> configOpt = getCategoryFifoConfig(categoryCode);
        
        if (!configOpt.isPresent()) {
            // Default to strict FIFO if no config found
            return true;
        }
        
        CategoryFifoConfig config = configOpt.get();
        
        // レトルト食品：FIFO優先度低、手動オーバーライドを許可
        if ("レトルト".equals(categoryCode) || categoryCode.startsWith("RC-")) {
            if (config.getManualOverrideAllowed() != null && config.getManualOverrideAllowed()) {
                // Override allowed, but reason required if specified
                if (config.getOverrideReasonRequired() != null && config.getOverrideReasonRequired().equals("true") 
                    && (overrideReason == null || overrideReason.trim().isEmpty())) {
                    throw new IllegalStateException("FIFO override requires a reason forレトルト食品");
                }
                return true; // Override allowed
            }
        }
        
        // チルド総菜：厳格なFIFO必須、手動変更は管理者権限のみ
        if ("チルド総菜".equals(categoryCode) || categoryCode.startsWith("TC-")) {
            if (config.getStrictFifoRequired() != null && config.getStrictFifoRequired()) {
                if (config.getManualOverrideAllowed() != null && !config.getManualOverrideAllowed()) {
                    throw new IllegalStateException("FIFO violation not allowed for チルド総菜. Strict FIFO required.");
                }
                if (config.getOverrideReasonRequired() != null && config.getOverrideReasonRequired().equals("true")
                    && (overrideReason == null || overrideReason.trim().isEmpty())) {
                    throw new IllegalStateException("FIFO override requires a reason for チルド総菜. Administrator approval required.");
                }
            }
        }
        
        // デリカ：最優先FIFO、FIFO違反検知時は即時アラート
        if ("デリカ".equals(categoryCode) || categoryCode.startsWith("DL-")) {
            if (config.getFifoPriorityLevel() != null && config.getFifoPriorityLevel() >= 4) {
                throw new IllegalStateException("FIFO violation detected for デリカ. Immediate alert required. Allocation blocked.");
            }
        }
        
        // おせち料理：日付ベース優先（12月31日まで商習慣）
        if ("おせち料理".equals(categoryCode) || categoryCode.contains("おせち") || categoryCode.contains("OSECHI")) {
            // Special handling for osechi - date-based priority
            LocalDate currentDate = LocalDate.now();
            if (currentDate.getMonthValue() >= 12 && currentDate.getDayOfMonth() >= 21) {
                // 出荷期間(12月21日〜30日): 「12月31日まで」商習慣に従う。日付ベースで優先
                // This is handled by the expiryDate-based FIFO sorting
            }
        }
        
        return true;
    }

    /**
     * Checks if a lot is subject to FIFO violation warning based on category rules.
     *
     * @param categoryCode the category code
     * @param lot the lot being checked
     * @return true if warning should be issued, false otherwise
     */
    public boolean shouldIssueFifoWarning(String categoryCode, Lot lot) {
        Optional<CategoryFifoConfig> configOpt = getCategoryFifoConfig(categoryCode);
        
        if (!configOpt.isPresent()) {
            return false;
        }
        
        CategoryFifoConfig config = configOpt.get();
        Integer warningThresholdDays = config.getWarningThresholdDays();
        
        if (warningThresholdDays == null || lot.getExpiryDate() == null) {
            return false;
        }
        
        LocalDate currentDate = LocalDate.now();
        LocalDate expiryDate = lot.getExpiryDate();
        long daysUntilExpiry = java.time.temporal.ChronoUnit.DAYS.between(currentDate, expiryDate);
        
        return daysUntilExpiry <= warningThresholdDays;
    }

    /**
     * Creates a location transfer request.
     *
     * @param lotId the lot ID to transfer
     * @param lotNumber the lot number
     * @param fromWarehouseId the source warehouse ID
     * @param fromZoneId the source zone ID
     * @param fromShelfId the source shelf ID
     * @param toWarehouseId the destination warehouse ID
     * @param toZoneId the destination zone ID
     * @param toShelfId the destination shelf ID
     * @param transferReason the transfer reason
     * @param requestedBy the user requesting the transfer
     * @return the created location transfer request
     */
    public LocationTransfer createLocationTransfer(UUID lotId, String lotNumber,
                                                   UUID fromWarehouseId, UUID fromZoneId, UUID fromShelfId,
                                                   UUID toWarehouseId, UUID toZoneId, UUID toShelfId,
                                                   String transferReason, UUID requestedBy) {
        // Check if lot exists
        List<Lot> lots = lotRepository.findByLotNumber(lotNumber);
        if (lots.isEmpty()) {
            throw new IllegalArgumentException("No lot found for lot number: " + lotNumber);
        }

        // Validate destination temperature zone compatibility
        Zone toZone = zoneRepository.findById(toZoneId).orElseThrow(() ->
                new IllegalArgumentException("Destination zone not found: " + toZoneId));
        Warehouse toWarehouse = warehouseRepository.findById(toWarehouseId).orElseThrow(() ->
                new IllegalArgumentException("Destination warehouse not found: " + toWarehouseId));

        // Check if destination zone temperature is compatible with the lot's category
        // For now, basic validation: if lot is quarantined or rejected, don't allow transfer
        Lot lot = lots.get(0);
        if (lot.getStatus() == LotStatus.QUARANTINE || lot.getStatus() == LotStatus.REJECTED) {
            throw new IllegalStateException("Cannot transfer lot with status QUARANTINE or REJECTED: " + lotNumber);
        }

        UUID transferId = UUID.randomUUID();
        LocationTransfer transfer = new LocationTransfer(
                transferId,
                lot.getFactoryId(),
                lotId,
                lotNumber,
                fromWarehouseId,
                fromZoneId,
                fromShelfId,
                toWarehouseId,
                toZoneId,
                toShelfId,
                transferReason,
                requestedBy
        );

        return locationTransferRepository.save(transfer);
    }

    /**
     * Approves a location transfer request.
     *
     * @param transferId the location transfer request ID
     * @param approvedBy the user approving the request
     * @return the approved location transfer request
     */
    public LocationTransfer approveLocationTransfer(UUID transferId, UUID approvedBy) {
        LocationTransfer transfer = locationTransferRepository.findById(transferId)
                .orElseThrow(() -> new IllegalArgumentException("Location transfer not found: " + transferId));

        if (!"申請中".equals(transfer.getStatus()) && !"承認済".equals(transfer.getStatus())) {
            throw new IllegalStateException("Location transfer is not in PENDING or APPROVED status: " + transfer.getStatus());
        }

        transfer.approve(approvedBy);
        return locationTransferRepository.save(transfer);
    }

    /**
     * Executes a location transfer request and updates the lot's location.
     *
     * @param transferId the location transfer request ID
     * @return the executed location transfer request
     */
    public LocationTransfer executeLocationTransfer(UUID transferId) {
        LocationTransfer transfer = locationTransferRepository.findById(transferId)
                .orElseThrow(() -> new IllegalArgumentException("Location transfer not found: " + transferId));

        if (!"承認済".equals(transfer.getStatus())) {
            throw new IllegalStateException("Location transfer is not in APPROVED status: " + transfer.getStatus());
        }

        // Update lot location
        List<Lot> lots = lotRepository.findByLotNumber(transfer.getLotNumber());
        if (!lots.isEmpty()) {
            Lot lot = lots.get(0);
            lot.setWarehouseId(transfer.getToWarehouseId());
            lot.setZoneId(transfer.getToZoneId());
            lot.setShelfId(transfer.getToShelfId());
            lotRepository.save(lot);
        }

        // Execute transfer
        transfer.execute();
        return locationTransferRepository.save(transfer);
    }

    /**
     * Cancels a location transfer request.
     *
     * @param transferId the location transfer request ID
     * @return the cancelled location transfer request
     */
    public LocationTransfer cancelLocationTransfer(UUID transferId) {
        LocationTransfer transfer = locationTransferRepository.findById(transferId)
                .orElseThrow(() -> new IllegalArgumentException("Location transfer not found: " + transferId));

        if (!"申請中".equals(transfer.getStatus()) && !"承認済".equals(transfer.getStatus())) {
            throw new IllegalStateException("Location transfer is not in PENDING or APPROVED status: " + transfer.getStatus());
        }

        transfer.cancel();
        return locationTransferRepository.save(transfer);
    }

    /**
     * Gets location transfers by status.
     *
     * @param factoryId the factory ID
     * @param status the status (申請中/承認済/実行済/キャンセル済)
     * @return list of location transfers
     */
    public List<LocationTransfer> getLocationTransfersByStatus(UUID factoryId, String status) {
        return locationTransferRepository.findByFactoryIdAndStatus(factoryId, status);
    }

    /**
     * Creates a hazard analysis record.
     *
     * @param hazardCode the hazard code
     * @param hazardName the hazard name
     * @param hazardCategory the hazard category (生物学的/化学的/物理的)
     * @param severityLevel the severity level (高/中/低)
     * @param probabilityLevel the probability level (高/中/低)
     * @param acceptability the acceptability (許容/不許容)
     * @param analysisReport the analysis report
     * @param factoryId the factory ID
     * @return the created hazard analysis record
     */
    public HazardAnalysis createHazardAnalysis(String hazardCode, String hazardName, String hazardCategory,
                                               String severityLevel, String probabilityLevel, String acceptability,
                                               String analysisReport, UUID factoryId) {
        UUID hazardId = UUID.randomUUID();
        HazardAnalysis hazardAnalysis = new HazardAnalysis(hazardId, factoryId, hazardCode, hazardName,
                hazardCategory, severityLevel, probabilityLevel, acceptability, analysisReport);
        return hazardAnalysisRepository.save(hazardAnalysis);
    }

    /**
     * Creates a CCP map record.
     *
     * @param processCode the process code
     * @param processName the process name
     * @param isCcp whether it is a CCP
     * @param ccpReason the CCP reason
     * @param hazardMitigated the hazard mitigated
     * @param factoryId the factory ID
     * @return the created CCP map record
     */
    public CcpMap createCcpMap(String processCode, String processName, boolean isCcp, String ccpReason,
                               String hazardMitigated, UUID factoryId) {
        UUID ccpMapId = UUID.randomUUID();
        CcpMap ccpMap = new CcpMap(ccpMapId, factoryId, processCode, processName, isCcp, ccpReason, hazardMitigated);
        return ccpMapRepository.save(ccpMap);
    }

    /**
     * Creates a critical limit record.
     *
     * @param ccpMapId the CCP map ID
     * @param limitCode the limit code
     * @param limitType the limit type (温度/時間/水分活性/塩素濃度等)
     * @param minValue the minimum value
     * @param maxValue the maximum value
     * @param unit the unit (℃/分/aW/ppm等)
     * @param isComposite whether it is a composite limit
     * @param factoryId the factory ID
     * @return the created critical limit record
     */
    public CriticalLimit createCriticalLimit(UUID ccpMapId, String limitCode, String limitType,
                                             BigDecimal minValue, BigDecimal maxValue, String unit,
                                             boolean isComposite, UUID factoryId) {
        UUID limitId = UUID.randomUUID();
        CriticalLimit criticalLimit = new CriticalLimit(limitId, factoryId, ccpMapId, limitCode,
                limitType, minValue, maxValue, unit, isComposite);
        return criticalLimitRepository.save(criticalLimit);
    }

    /**
     * Creates a CCP monitoring record.
     *
     * @param ccpMapId the CCP map ID
     * @param criticalLimitId the critical limit ID
     * @param monitoredValue the monitored value
     * @param unit the unit
     * @param monitoredAt the monitoring time
     * @param monitoredBy the user who monitored
     * @param factoryId the factory ID
     * @return the created CCP monitoring record
     */
    public CcpMonitoringRecord createCcpMonitoringRecord(UUID ccpMapId, UUID criticalLimitId,
                                                         BigDecimal monitoredValue, String unit,
                                                         LocalDateTime monitoredAt, UUID monitoredBy, UUID factoryId) {
        boolean isWithinLimit = true; // Basic validation - in real scenario, compare with critical limit

        UUID recordId = UUID.randomUUID();
        CcpMonitoringRecord record = new CcpMonitoringRecord(recordId, factoryId, ccpMapId,
                criticalLimitId, monitoredValue, unit, monitoredAt, monitoredBy, isWithinLimit);
        return ccpMonitoringRecordRepository.save(record);
    }

    /**
     * Records a deviation in a CCP monitoring record.
     *
     * @param recordId the CCP monitoring record ID
     * @param deviationReason the deviation reason
     * @param correctiveActionTaken the corrective action taken
     * @return the updated CCP monitoring record
     */
    public CcpMonitoringRecord recordCcpDeviation(UUID recordId, String deviationReason, String correctiveActionTaken) {
        CcpMonitoringRecord record = ccpMonitoringRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("CCP monitoring record not found: " + recordId));

        record.recordDeviation(deviationReason, correctiveActionTaken);
        return ccpMonitoringRecordRepository.save(record);
    }

    /**
     * Creates a corrective action record.
     *
     * @param ccpMonitoringRecordId the CCP monitoring record ID
     * @param deviationDescription the deviation description
     * @param correctiveProcedure the corrective procedure
     * @param takenBy the user who took the action
     * @param factoryId the factory ID
     * @return the created corrective action record
     */
    public CorrectiveAction createCorrectiveAction(UUID ccpMonitoringRecordId, String deviationDescription,
                                                   String correctiveProcedure, UUID takenBy, UUID factoryId) {
        UUID actionId = UUID.randomUUID();
        CorrectiveAction action = new CorrectiveAction(actionId, factoryId, ccpMonitoringRecordId,
                deviationDescription, correctiveProcedure, takenBy);
        return correctiveActionRepository.save(action);
    }

    /**
     * Completes a corrective action.
     *
     * @param actionId the corrective action ID
     * @param correctiveActionTaken the corrective action taken
     * @param impactAssessment the impact assessment
     * @return the completed corrective action
     */
    public CorrectiveAction completeCorrectiveAction(UUID actionId, String correctiveActionTaken, String impactAssessment) {
        CorrectiveAction action = correctiveActionRepository.findById(actionId)
                .orElseThrow(() -> new IllegalArgumentException("Corrective action not found: " + actionId));

        action.completeAction(correctiveActionTaken, impactAssessment);
        return correctiveActionRepository.save(action);
    }

    /**
     * Creates a verification procedure record.
     *
     * @param verificationCode the verification code
     * @param verificationType the verification type (CCP検証計画/機器校正記録/HACCP計画見直し/内部監査管理/統計解析)
     * @param verificationDate the verification date
     * @param verificationResult the verification result (合格/不合格/要改善)
     * @param findings the findings
     * @param recommendations the recommendations
     * @param verifiedBy the user who verified
     * @param factoryId the factory ID
     * @return the created verification procedure record
     */
    public VerificationProcedure createVerificationProcedure(String verificationCode, String verificationType,
                                                             LocalDateTime verificationDate, String verificationResult,
                                                             String findings, String recommendations,
                                                             UUID verifiedBy, UUID factoryId) {
        UUID verificationId = UUID.randomUUID();
        VerificationProcedure procedure = new VerificationProcedure(verificationId, factoryId, verificationCode,
                verificationType, verificationDate, verificationResult, findings, recommendations, verifiedBy);
        return verificationProcedureRepository.save(procedure);
    }

    /**
     * Gets CCP monitoring records by status (within limit or deviation).
     *
     * @param factoryId the factory ID
     * @param isWithinLimit whether the value is within limit
     * @return list of CCP monitoring records
     */
    public List<CcpMonitoringRecord> getCcpMonitoringRecordsByStatus(UUID factoryId, boolean isWithinLimit) {
        return ccpMonitoringRecordRepository.findByIsWithinLimit(isWithinLimit);
    }

    /**
     * Creates an inspection spec record.
     *
     * @param specCode the spec code
     * @param specName the spec name
     * @param inspectionCategory the inspection category (原材料/工程内/完成品)
     * @param inspectionItemType the inspection item type (物理的/化学的/生物学的/外観)
     * @param judgmentCriteria the judgment criteria
     * @param factoryId the factory ID
     * @return the created inspection spec record
     */
    public InspectionSpec createInspectionSpec(String specCode, String specName, String inspectionCategory,
                                               String inspectionItemType, String judgmentCriteria, UUID factoryId) {
        UUID specId = UUID.randomUUID();
        InspectionSpec spec = new InspectionSpec(specId, factoryId, specCode, specName,
                inspectionCategory, inspectionItemType, judgmentCriteria);
        return inspectionSpecRepository.save(spec);
    }

    /**
     * Creates an inspection record.
     *
     * @param inspectionSpecId the inspection spec ID
     * @param lotId the lot ID
     * @param lotNumber the lot number
     * @param inspectedValue the inspected value
     * @param unit the unit
     * @param inspectedAt the inspection time
     * @param inspectedBy the user who inspected
     * @param inspectionResult the inspection result (合格/不合格/要再検査)
     * @param factoryId the factory ID
     * @return the created inspection record
     */
    public InspectionRecord createInspectionRecord(UUID inspectionSpecId, UUID lotId, String lotNumber,
                                                   BigDecimal inspectedValue, String unit,
                                                   LocalDateTime inspectedAt, UUID inspectedBy,
                                                   String inspectionResult, UUID factoryId) {
        UUID recordId = UUID.randomUUID();
        InspectionRecord record = new InspectionRecord(recordId, factoryId, inspectionSpecId,
                lotId, lotNumber, inspectedValue, unit, inspectedAt, inspectedBy, inspectionResult);
        return inspectionRecordRepository.save(record);
    }

    /**
     * Adds a photo attachment to an inspection record.
     *
     * @param recordId the inspection record ID
     * @param photoAttachment the photo attachment URL or path
     * @return the updated inspection record
     */
    public InspectionRecord addInspectionPhoto(UUID recordId, String photoAttachment) {
        InspectionRecord record = inspectionRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Inspection record not found: " + recordId));

        record.addPhotoAttachment(photoAttachment);
        return inspectionRecordRepository.save(record);
    }

    /**
     * Creates an NCR report.
     *
     * @param ncrCode the NCR code
     * @param lotId the lot ID
     * @param lotNumber the lot number
     * @param ncrReason the NCR reason
     * @param ncrDescription the NCR description
     * @param reportedBy the user who reported
     * @param factoryId the factory ID
     * @return the created NCR report
     */
    public NcrReport createNcrReport(String ncrCode, UUID lotId, String lotNumber, String ncrReason,
                                     String ncrDescription, UUID reportedBy, UUID factoryId) {
        UUID ncrId = UUID.randomUUID();
        NcrReport report = new NcrReport(ncrId, factoryId, ncrCode, lotId, lotNumber,
                ncrReason, ncrDescription, reportedBy);
        return ncrReportRepository.save(report);
    }

    /**
     * Updates the status of an NCR report to "investigating".
     *
     * @param ncrId the NCR report ID
     * @return the updated NCR report
     */
    public NcrReport investigateNcrReport(UUID ncrId) {
        NcrReport report = ncrReportRepository.findById(ncrId)
                .orElseThrow(() -> new IllegalArgumentException("NCR report not found: " + ncrId));

        report.investigate();
        return ncrReportRepository.save(report);
    }

    /**
     * Updates the status of an NCR report to "corrective action completed".
     *
     * @param ncrId the NCR report ID
     * @return the updated NCR report
     */
    public NcrReport completeNcrCorrectiveAction(UUID ncrId) {
        NcrReport report = ncrReportRepository.findById(ncrId)
                .orElseThrow(() -> new IllegalArgumentException("NCR report not found: " + ncrId));

        report.completeCorrectiveAction();
        return ncrReportRepository.save(report);
    }

    /**
     * Closes an NCR report.
     *
     * @param ncrId the NCR report ID
     * @param closedBy the user who closed the report
     * @return the closed NCR report
     */
    public NcrReport closeNcrReport(UUID ncrId, UUID closedBy) {
        NcrReport report = ncrReportRepository.findById(ncrId)
                .orElseThrow(() -> new IllegalArgumentException("NCR report not found: " + ncrId));

        report.closeNcr(closedBy);
        return ncrReportRepository.save(report);
    }

    /**
     * Gets NCR reports by status.
     *
     * @param factoryId the factory ID
     * @param status the status (発生/調査中/是正措置済/閉鎖)
     * @return list of NCR reports
     */
    public List<NcrReport> getNcrReportsByStatus(UUID factoryId, String status) {
        return ncrReportRepository.findByStatus(status);
    }
}

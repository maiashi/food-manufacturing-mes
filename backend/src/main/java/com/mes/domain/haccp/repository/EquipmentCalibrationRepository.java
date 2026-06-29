package com.mes.domain.haccp.repository;

import com.mes.domain.haccp.entity.EquipmentCalibration;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Equipment Calibration domain entities.
 * Manages calibration records tied to HACCP control steps and equipment.
 */
public interface EquipmentCalibrationRepository {

    /**
     * Find a calibration record by its unique identifier.
     *
     * @param id the unique identifier
     * @return an Optional containing the calibration if found, empty otherwise
     */
    Optional<EquipmentCalibration> findById(UUID id);

    /**
     * Find all calibration records associated with a given HACCP control step.
     *
     * @param stepId the unique identifier of the control step
     * @return list of calibration records for the specified step
     */
    List<EquipmentCalibration> findByStepId(UUID stepId);

    /**
     * Find all calibration records whose due date is before the given date.
     * Useful for identifying overdue calibrations.
     *
     * @param date the reference date for comparison
     * @return list of calibration records past their due date
     */
    List<EquipmentCalibration> findByCalibrationDueDateBefore(LocalDate date);

    /**
     * Find all calibration records performed by a specific user.
     *
     * @param userId the unique identifier of the user who performed the calibration
     * @return list of calibration records for the specified user
     */
    List<EquipmentCalibration> findByCalibratedByUserId(UUID userId);

    /**
     * Persist a new or update an existing calibration record.
     *
     * @param calibration the calibration entity to save
     * @return the saved calibration entity with potentially updated fields (e.g. ID, timestamps)
     */
    EquipmentCalibration save(EquipmentCalibration calibration);

    /**
     * Delete a calibration record by its unique identifier.
     *
     * @param id the unique identifier of the calibration to delete
     */
    void delete(UUID id);
}

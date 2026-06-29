package com.mes.domain.haccp.repository;

import com.mes.domain.haccp.entity.Capa;
import com.mes.domain.haccp.enums.CapaStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Corrective and Preventive Action (CAPA) domain entities.
 * Manages CAPA records initiated to address root causes of non-conformances or potential issues.
 */
public interface CapaRepository {

    /**
     * Find a CAPA record by its unique identifier.
     *
     * @param id the unique identifier
     * @return an Optional containing the CAPA if found, empty otherwise
     */
    Optional<Capa> findById(UUID id);

    /**
     * Find all CAPA records associated with a given reference entity.
     * CAPAs are linked to external entities such as NCRs, audits, or risk assessments.
     *
     * @param referenceType the type of the referenced entity (e.g. "NCR", "AUDIT")
     * @param referenceId   the unique identifier of the referenced entity
     * @return list of CAPA records linked to the specified reference
     */
    List<Capa> findByReferenceAndId(String referenceType, UUID referenceId);

    /**
     * Find all CAPA records matching a given status.
     * Useful for filtering open, in-progress, or closed actions.
     *
     * @param status the status to filter by
     * @return list of CAPA records with the specified status
     */
    List<Capa> findByStatus(CapaStatus status);

    /**
     * Find all CAPA records whose due date is before the given date.
     * Useful for identifying overdue corrective and preventive actions.
     *
     * @param date the reference date for comparison
     * @return list of CAPA records past their due date
     */
    List<Capa> findByDueDateBefore(LocalDate date);

    /**
     * Persist a new or update an existing CAPA record.
     *
     * @param capa the CAPA entity to save
     * @return the saved CAPA entity with potentially updated fields (e.g. ID, timestamps)
     */
    Capa save(Capa capa);

    /**
     * Update the status of an existing CAPA record.
     *
     * @param id       the unique identifier of the CAPA to update
     * @param newStatus the new status to set (e.g. IN_PROGRESS, COMPLETED)
     */
    void updateStatus(UUID id, CapaStatus newStatus);
}

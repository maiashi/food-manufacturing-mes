package com.mes.domain.haccp.repository;

import com.mes.domain.haccp.entity.Ncr;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Non-Conformance Report (NCR) domain entities.
 * Manages records of deviations from HACCP standards identified during production.
 */
public interface NcrRepository {

    /**
     * Find an NCR by its unique identifier.
     *
     * @param id the unique identifier
     * @return an Optional containing the NCR if found, empty otherwise
     */
    Optional<Ncr> findById(UUID id);

    /**
     * Find all NCRs associated with a given factory and status.
     * Useful for filtering open or pending issues at a specific facility.
     *
     * @param factoryId the unique identifier of the factory
     * @param status    the status filter (e.g. "OPEN", "IN_PROGRESS", "CLOSED")
     * @return list of NCRs matching the factory and status criteria
     */
    List<Ncr> findByFactoryIdAndStatus(UUID factoryId, String status);

    /**
     * Find all NCRs associated with a given production lot.
     *
     * @param lotId the unique identifier of the production lot
     * @return list of NCRs for the specified lot
     */
    List<Ncr> findByLotId(UUID lotId);

    /**
     * Persist a new or update an existing NCR record.
     *
     * @param ncr the NCR entity to save
     * @return the saved NCR entity with potentially updated fields (e.g. ID, timestamps)
     */
    Ncr save(Ncr ncr);

    /**
     * Delete an NCR by its unique identifier.
     *
     * @param id the unique identifier of the NCR to delete
     */
    void delete(UUID id);
}

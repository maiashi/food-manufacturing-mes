package com.mes.domain.inventory.repository;

import com.mes.domain.inventory.entity.VerificationProcedure;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VerificationProcedureRepository {

    Optional<VerificationProcedure> findById(UUID id);

    List<VerificationProcedure> findByFactoryId(UUID factoryId);

    List<VerificationProcedure> findByVerificationType(String verificationType);

    List<VerificationProcedure> findByVerificationResult(String verificationResult);

    List<VerificationProcedure> findByVerifiedBy(UUID verifiedBy);

    VerificationProcedure save(VerificationProcedure procedure);

    void delete(UUID verificationId);
}
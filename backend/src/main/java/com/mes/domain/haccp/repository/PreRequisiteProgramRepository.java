package com.mes.domain.haccp.repository;

import com.mes.domain.haccp.entity.PreRequisiteProgram;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PreRequisiteProgramRepository {

    Optional<PreRequisiteProgram> findById(UUID id);

    List<PreRequisiteProgram> findAllActive();

    List<PreRequisiteProgram> findByProgramType(String programType);

    PreRequisiteProgram save(PreRequisiteProgram prp);

    void delete(UUID id);
}

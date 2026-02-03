package org.cric.back_office.work.repository;

import org.cric.back_office.work.entity.Project;
import org.cric.back_office.work.entity.Silo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SiloJpaRepository extends JpaRepository<Silo, Long> {
    List<Silo> findAllByIsDeleted(Boolean isDeleted);

    List<Project> findProjectBy(Long id);
}

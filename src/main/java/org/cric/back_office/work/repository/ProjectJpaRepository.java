package org.cric.back_office.work.repository;

import org.cric.back_office.work.entity.Project;
import org.cric.back_office.work.entity.Silo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectJpaRepository extends JpaRepository<Project, Long> {
    List<Project> findProjectBySiloIdAndIsDeleted(Long siloId,boolean isDeleted);
    List<Project> findBySiloAndIsDeleted(Silo silo, Boolean isDeleted);
}

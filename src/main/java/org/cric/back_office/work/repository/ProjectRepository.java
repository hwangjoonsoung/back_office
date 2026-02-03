package org.cric.back_office.work.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static org.cric.back_office.user.entity.QUserSilo.userSilo;
import static org.cric.back_office.work.entity.QSilo.silo;

@Repository
@RequiredArgsConstructor
public class ProjectRepository {

    private final EntityManager em;
    private final JPAQueryFactory factory;


}

package org.cric.back_office.work.repository;

import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.cric.back_office.user.entity.QUserSilo;
import org.cric.back_office.user.entity.UserSilo;
import org.cric.back_office.work.entity.QSilo;
import org.cric.back_office.work.entity.Silo;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.cric.back_office.work.entity.QSilo.silo;

@Repository
@RequiredArgsConstructor
public class SiloRepository {

    private final EntityManager em;
    private final JPAQueryFactory factory;

    public List<Silo> findSiloByUserId(Long userId) {
        QUserSilo userSilo = QUserSilo.userSilo;
        return factory.select(silo).from(silo).join(silo.userSilo, userSilo).where(userSilo.user.id.eq(userId), silo.isDeleted.eq(false)).fetch();
    }

    public Long persistUserSilo(UserSilo userSilo, Silo silo) {
        em.persist(silo);
        em.persist(userSilo);
        em.flush(); // DB에 즉시 반영
        
        // flush 후 ID가 생성되었는지 확인 가능
        if (silo.getId() == null || userSilo.getId() == null) {
            throw new IllegalStateException("Failed to persist entities - ID not generated");
        }
        
        em.clear(); // 영속성 컨텍스트 초기화
        return silo.getId();
    }
}

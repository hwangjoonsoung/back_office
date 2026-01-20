package org.cric.back_office.user.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.cric.back_office.user.dto.FindUserCondition;
import org.cric.back_office.user.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.cric.back_office.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;
    private final JPAQueryFactory factory;

    public List<User> searchUserWithCondition(FindUserCondition condition) {
        return factory.select(user).from(user).where(
                conditionUserName(condition.name()),
                conditionUserEmail(condition.email()),
                conditionUserAffiliation(condition.affiliation()),
                conditionUserPosition(condition.position()),
                conditionUserPhoneNumber(condition.phoneNumber()),
                conditionUserBirth(condition.birth())
        ).fetch();
    }

    private BooleanExpression conditionUserName(String name) {
        return StringUtils.hasText(name) ? user.name.like("%" + name + "%") : null;
    }
    private BooleanExpression conditionUserEmail(String email) {
        return StringUtils.hasText(email) ? user.email.like("%" + email + "%") : null;
    }
    private BooleanExpression conditionUserAffiliation(String affiliation) {
        return StringUtils.hasText(affiliation) ? user.affiliation.like("%" + affiliation + "%") : null;
    }
    private BooleanExpression conditionUserPosition(String position) {
        return StringUtils.hasText(position) ? user.position.like("%" + position + "%") : null;
    }
    private BooleanExpression conditionUserPhoneNumber(String phoneNumber) {
        return StringUtils.hasText(phoneNumber) ? user.phoneNumber.like("%" + phoneNumber + "%") : null;
    }
    private BooleanExpression conditionUserBirth(LocalDate localDate) {
        return localDate != null ? user.birthday.eq(localDate) : null;
    }

}

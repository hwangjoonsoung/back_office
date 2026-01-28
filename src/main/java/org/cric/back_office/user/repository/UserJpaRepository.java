package org.cric.back_office.user.repository;

import org.cric.back_office.user.entity.User;
import org.cric.back_office.user.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    User findByIdAndUserStatus(Integer id, UserStatus userStatus);
}

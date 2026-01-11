package org.cric.back_office.user.repository;

import org.cric.back_office.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
}

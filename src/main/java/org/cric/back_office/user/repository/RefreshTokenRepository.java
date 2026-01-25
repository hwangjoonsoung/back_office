package org.cric.back_office.user.repository;

import org.cric.back_office.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    
    Optional<RefreshToken> findByToken(String token);
    
    Optional<RefreshToken> findByUserId(Long userId);
    
    void deleteByUserId(Integer userId);
    
    void deleteByToken(String token);
}

package com.stock.tomorrowMarket.auth.repository;

import com.stock.tomorrowMarket.auth.entity.RefreshToken;
import com.stock.tomorrowMarket.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    List<RefreshToken> findByUser(Users user);
}

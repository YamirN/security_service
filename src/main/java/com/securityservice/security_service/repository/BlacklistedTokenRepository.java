package com.securityservice.security_service.repository;

import com.securityservice.security_service.model.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, String> {

    boolean existsByTokenId(String tokenId);

    void deleteByExpirationDateBefore(Date date);
}

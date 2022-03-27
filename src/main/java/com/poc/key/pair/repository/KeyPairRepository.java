package com.poc.key.pair.repository;

import com.poc.key.pair.entity.KeyPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeyPairRepository extends JpaRepository<KeyPair, Long> {
    Optional<KeyPair> findByClientId(String clientId);
}

package com.poc.key.pair.repository;

import com.poc.key.pair.entity.Desafio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesafioRepository extends JpaRepository<Desafio, String> {
}

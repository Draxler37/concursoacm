package com.concursoacm.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.concursoacm.domain.models.Pais;

@Repository
public interface PaisRepository extends JpaRepository<Pais, Integer> {
}



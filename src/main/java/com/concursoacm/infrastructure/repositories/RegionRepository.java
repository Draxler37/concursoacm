package com.concursoacm.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.concursoacm.domain.models.Region;

/**
 * *Repositorio para la gestión de la entidad Region.
 * *Proporciona métodos para realizar operaciones CRUD y consultas
 * *personalizadas.
 */
public interface RegionRepository extends JpaRepository<Region, Integer> {
}

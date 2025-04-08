package com.concursoacm.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.concursoacm.domain.models.Pais;

/**
 * *Repositorio para la gestión de la entidad Pais.
 * *Proporciona métodos para realizar operaciones CRUD y consultas
 * *personalizadas.
 */
@Repository
public interface PaisRepository extends JpaRepository<Pais, Integer> {
}

package com.concursoacm.tools.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.concursoacm.models.Pais;

/**
 * *Repositorio para la gestión de la entidad Pais.
 * *Proporciona métodos para realizar operaciones CRUD y consultas
 * *personalizadas.
 */
@Repository
public interface PaisRepository extends JpaRepository<Pais, Integer> {
}

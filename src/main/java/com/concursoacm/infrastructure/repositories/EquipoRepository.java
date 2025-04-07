package com.concursoacm.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.concursoacm.domain.models.Equipo;

/**
 * *Repositorio para la gestión de la entidad Equipo.
 * *Proporciona métodos para realizar operaciones CRUD y consultas
 * *personalizadas.
 */
@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Integer> {

    /**
     * *Cuenta el número de equipos de una categoría específica asociados a un país.
     *
     * @param idPais    ID del país.
     * @param categoria Categoría del equipo.
     * @return Número de equipos en la categoría.
     */
    int countByPaisIdPaisAndCategoria(int idPais, String categoria);
}

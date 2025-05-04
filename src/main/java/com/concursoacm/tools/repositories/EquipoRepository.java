package com.concursoacm.tools.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.concursoacm.models.Equipo;

/**
 * *Repositorio para la gestión de la entidad Equipo.
 * *Proporciona métodos para realizar operaciones CRUD y consultas
 * *personalizadas.
 */
@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Integer> {

    /**
     * *Cuenta el número de equipos asociados a un país y una categoría específicos.
     *
     * @param idPais      ID del país.
     * @param idCategoria ID de la categoría.
     * @return Número de equipos asociados al país y la categoría.
     */
    int countByPaisIdPaisAndEquipoCategoriaIdEquipoCategoria(int idPais, int idCategoria);
}

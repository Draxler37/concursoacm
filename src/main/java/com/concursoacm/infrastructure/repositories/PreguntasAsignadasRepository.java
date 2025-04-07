package com.concursoacm.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.concursoacm.domain.models.PreguntasAsignadas;

import java.util.Optional;

/**
 * *Repositorio para la gestión de la entidad PreguntasAsignadas.
 */
@Repository
public interface PreguntasAsignadasRepository extends JpaRepository<PreguntasAsignadas, Integer> {

    /**
     * *Obtiene la asignación de preguntas para un equipo específico.
     *
     * @param idEquipo ID del equipo.
     * @return Un Optional con la asignación de preguntas.
     */
    Optional<PreguntasAsignadas> findByEquipoIdEquipo(int idEquipo);
}

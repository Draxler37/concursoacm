package com.concursoacm.repositories;

import com.concursoacm.models.PreguntasAsignadas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PreguntasAsignadasRepository extends JpaRepository<PreguntasAsignadas, Integer> {
    // Obtener la asignación de preguntas para un equipo específico
    Optional<PreguntasAsignadas> findByEquipoIdEquipo(int idEquipo);
}


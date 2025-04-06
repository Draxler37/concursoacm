package com.concursoacm.repositories;

import com.concursoacm.models.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * *Repositorio para la gestión de la entidad Pregunta.
 * *Proporciona métodos para realizar operaciones CRUD y consultas
 * *personalizadas.
 */
@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Integer> {

    /**
     * *Encuentra preguntas no usadas por clase.
     *
     * @param clase Clase de la pregunta.
     * @return Lista de preguntas no usadas.
     */
    List<Pregunta> findByClaseAndUsadaFalse(String clase);
}

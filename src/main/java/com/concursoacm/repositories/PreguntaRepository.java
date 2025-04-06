package com.concursoacm.repositories;

import com.concursoacm.models.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Integer> {
    
    // Encuentra preguntas no usadas por clase
    List<Pregunta> findByClaseAndUsadaFalse(String clase);
}



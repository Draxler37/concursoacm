package com.concursoacm.repositories;

import com.concursoacm.models.Respuesta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Integer> {
    // Obtener respuestas por id de participante
    List<Respuesta> findByIdParticipante(int idParticipante);
    
    boolean existsByIdParticipanteAndIdPregunta(int idParticipante, int idPregunta);
}

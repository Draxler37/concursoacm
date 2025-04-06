package com.concursoacm.services;

import com.concursoacm.models.Participante;
import com.concursoacm.models.PreguntasAsignadas;
import com.concursoacm.models.Respuesta;
import com.concursoacm.repositories.ParticipanteRepository;
import com.concursoacm.repositories.PreguntasAsignadasRepository;
import com.concursoacm.repositories.RespuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RespuestaService {

    @Autowired
    private RespuestaRepository respuestaRepository;
    
    @Autowired
    private ParticipanteRepository participanteRepository;
    
    @Autowired
    private PreguntasAsignadasRepository preguntasAsignadasRepository;

    // Crear una respuesta
    public Respuesta crearRespuesta(int idParticipante, int idPregunta, String respuestaText) {
        // Buscar el participante
        Participante participante = participanteRepository.findById(idParticipante)
                .orElseThrow(() -> new IllegalArgumentException("Participante no encontrado."));
        
        if (respuestaRepository.existsByIdParticipanteAndIdPregunta(idParticipante, idPregunta)) {
            throw new IllegalArgumentException("El participante ya ha respondido esta pregunta.");
        }
        
        // Verificar que el participante tiene un equipo asignado
        if (participante.getEquipo() == null) {
            throw new IllegalArgumentException("El participante no tiene equipo asignado, no puede responder.");
        }
        
        // Validar que la pregunta que se intenta responder está asignada al equipo
        int idEquipo = participante.getEquipo().getIdEquipo();
        PreguntasAsignadas asignacion = preguntasAsignadasRepository.findByEquipoIdEquipo(idEquipo)
                .orElseThrow(() -> new IllegalArgumentException("No se han asignado preguntas al equipo."));
        
        // Verificar que el idPregunta se encuentra entre los 5 asignados
        if (idPregunta != asignacion.getPregunta1() && idPregunta != asignacion.getPregunta2() &&
            idPregunta != asignacion.getPregunta3() && idPregunta != asignacion.getPregunta4() &&
            idPregunta != asignacion.getPregunta5()) {
            throw new IllegalArgumentException("La pregunta no está asignada a este equipo.");
        }
        
        Respuesta respuesta = new Respuesta();
        respuesta.setIdParticipante(idParticipante);
        respuesta.setIdEquipo(idEquipo);
        respuesta.setIdPregunta(idPregunta);
        respuesta.setRespuestaParticipante(respuestaText);
        respuesta.setPuntuacionObtenida(0);  // Inicialmente 0

        return respuestaRepository.save(respuesta);
    }
    
    // Obtener respuestas por participante
    public List<Respuesta> getRespuestasPorParticipante(int idParticipante) {
        return respuestaRepository.findByIdParticipante(idParticipante);
    }
}





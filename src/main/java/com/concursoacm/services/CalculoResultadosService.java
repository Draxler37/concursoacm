package com.concursoacm.services;

import com.concursoacm.models.Pregunta;
import com.concursoacm.models.PreguntasAsignadas;
import com.concursoacm.models.Respuesta;
import com.concursoacm.models.Resultado;
import com.concursoacm.repositories.PreguntaRepository;
import com.concursoacm.repositories.PreguntasAsignadasRepository;
import com.concursoacm.repositories.RespuestaRepository;
import com.concursoacm.repositories.ResultadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CalculoResultadosService {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private ResultadoRepository resultadoRepository;
    
    @Autowired
    private PreguntasAsignadasRepository preguntasAsignadasRepository;

    @Transactional
    public String calcularResultados() {
        List<Respuesta> respuestas = respuestaRepository.findAll();
        // Map para acumular puntos por participante (clave: idParticipante, valor: suma de puntos)
        Map<Integer, Integer> sumaPuntosPorParticipante = new HashMap<>();

        for (Respuesta respuesta : respuestas) {
            int idPregunta = respuesta.getIdPregunta();
            int idParticipante = respuesta.getIdParticipante();
            // La evaluación ya está almacenada en el campo "puntuacionObtenida" de respuesta
            int evaluacion = respuesta.getPuntuacionObtenida();

            Pregunta pregunta = preguntaRepository.findById(idPregunta)
                    .orElseThrow(() -> new IllegalArgumentException("Pregunta no encontrada para id " + idPregunta));

            int maxPuntuacion = pregunta.getPuntuacionMaxima();
            String clase = pregunta.getClase(); // Se espera que sea "A" o "B"
            int puntos = calcularPuntos(evaluacion, maxPuntuacion, clase);

            sumaPuntosPorParticipante.put(idParticipante, 
            sumaPuntosPorParticipante.getOrDefault(idParticipante, 0) + puntos);
        }

        // Guardar o actualizar los resultados por participante en la tabla resultados
        for (Map.Entry<Integer, Integer> entry : sumaPuntosPorParticipante.entrySet()) {
            Resultado resultado = new Resultado();
            resultado.setIdParticipante(entry.getKey());
            resultado.setPuntuacionTotal(entry.getValue());
            resultadoRepository.save(resultado);
        }

        // Actualizar la columna usada en la tabla preguntas para todas las preguntas asignadas.
        // Se recogen los id de las preguntas de la tabla preguntas_asignadas sin repetir.
        List<PreguntasAsignadas> asignaciones = preguntasAsignadasRepository.findAll();
        Set<Integer> idsPreguntas = new HashSet<>();
        for (PreguntasAsignadas pa : asignaciones) {
            idsPreguntas.add(pa.getPregunta1());
            idsPreguntas.add(pa.getPregunta2());
            idsPreguntas.add(pa.getPregunta3());
            idsPreguntas.add(pa.getPregunta4());
            idsPreguntas.add(pa.getPregunta5());
        }
        for (Integer idPregunta : idsPreguntas) {
            Pregunta preg = preguntaRepository.findById(idPregunta)
                    .orElse(null);
            if (preg != null && !preg.isUsada()) {
                preg.setUsada(true);
                preguntaRepository.save(preg);
            }
        }
        
        return "Finalizado el cálculo";
    }

    private int calcularPuntos(int evaluacion, int maxPuntuacion, String clase) {
        if ("A".equalsIgnoreCase(clase)) {
            if (evaluacion >= 90 && evaluacion <= 100) {
                return maxPuntuacion;
            } else if (evaluacion >= 70 && evaluacion < 90) {
                return maxPuntuacion / 2;
            } else if (evaluacion >= 50 && evaluacion < 70) {
                return maxPuntuacion / 3;
            } else {
                return 0;
            }
        } else if ("B".equalsIgnoreCase(clase)) {
            if (evaluacion >= 95 && evaluacion <= 100) {
                return maxPuntuacion;
            } else if (evaluacion >= 80 && evaluacion < 95) {
                return maxPuntuacion - 2;
            } else if (evaluacion >= 65 && evaluacion < 80) {
                return maxPuntuacion - 4;
            } else {
                return 0;
            }
        }
        return 0;
    }
}




package com.concursoacm.application.services;

import com.concursoacm.domain.models.Pregunta;
import com.concursoacm.domain.models.Respuesta;
import com.concursoacm.domain.models.Resultado;
import com.concursoacm.infrastructure.repositories.PreguntaRepository;
import com.concursoacm.infrastructure.repositories.PreguntasAsignadasRepository;
import com.concursoacm.infrastructure.repositories.RespuestaRepository;
import com.concursoacm.infrastructure.repositories.ResultadoRepository;
import com.concursoacm.infrastructure.utils.Constantes;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * *Servicio que implementa la lógica de cálculo de resultados.
 */
@Service
public class ResultadoCalculoService {

    private final RespuestaRepository respuestaRepository;
    private final PreguntaRepository preguntaRepository;
    private final ResultadoRepository resultadoRepository;
    private final PreguntasAsignadasRepository preguntasAsignadasRepository;

    /**
     * *Constructor que inyecta las dependencias necesarias.
     *
     * @param respuestaRepository          Repositorio para la gestión de
     *                                     respuestas.
     * @param preguntaRepository           Repositorio para la gestión de preguntas.
     * @param resultadoRepository          Repositorio para la gestión de
     *                                     resultados.
     * @param preguntasAsignadasRepository Repositorio para la gestión de preguntas
     *                                     asignadas.
     */
    public ResultadoCalculoService(RespuestaRepository respuestaRepository,
            PreguntaRepository preguntaRepository,
            ResultadoRepository resultadoRepository,
            PreguntasAsignadasRepository preguntasAsignadasRepository) {
        this.respuestaRepository = respuestaRepository;
        this.preguntaRepository = preguntaRepository;
        this.resultadoRepository = resultadoRepository;
        this.preguntasAsignadasRepository = preguntasAsignadasRepository;
    }

    /**
     * *Calcula los resultados del concurso.
     *
     * @return Mensaje indicando el estado del cálculo.
     */
    @Transactional
    public String calcularResultados() {
        Map<Integer, Integer> sumaPuntosPorParticipante = calcularPuntosPorParticipante();
        guardarResultados(sumaPuntosPorParticipante);
        actualizarPreguntasUsadas();
        return "Cálculo de resultados finalizado.";
    }

    /**
     * *Calcula los puntos obtenidos por cada participante.
     *
     * @return Mapa con los puntos acumulados por participante.
     */
    private Map<Integer, Integer> calcularPuntosPorParticipante() {
        List<Respuesta> respuestas = respuestaRepository.findAll();
        Map<Integer, Integer> sumaPuntosPorParticipante = new HashMap<>();

        for (Respuesta respuesta : respuestas) {
            Pregunta pregunta = preguntaRepository.findById(respuesta.getIdPregunta())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Pregunta no encontrada para ID " + respuesta.getIdPregunta()));

            int puntos = calcularPuntos(respuesta.getPuntuacionObtenida(), pregunta.getPuntuacionMaxima(),
                    pregunta.getClase());
            sumaPuntosPorParticipante.merge(respuesta.getIdParticipante(), puntos, Integer::sum);
        }

        return sumaPuntosPorParticipante;
    }

    /**
     * *Guarda los resultados calculados en la base de datos.
     *
     * @param sumaPuntosPorParticipante Mapa con los puntos acumulados por
     *                                  participante.
     */
    private void guardarResultados(Map<Integer, Integer> sumaPuntosPorParticipante) {
        sumaPuntosPorParticipante.forEach((idParticipante, puntuacionTotal) -> {
            Resultado resultado = new Resultado();
            resultado.setIdParticipante(idParticipante);
            resultado.setPuntuacionTotal(puntuacionTotal);
            resultadoRepository.save(resultado);
        });
    }

    /**
     * *Actualiza el estado de las preguntas asignadas como "usadas".
     */
    private void actualizarPreguntasUsadas() {
        Set<Integer> idsPreguntas = new HashSet<>();
        preguntasAsignadasRepository.findAll().forEach(asignacion -> {
            idsPreguntas.add(asignacion.getPregunta1());
            idsPreguntas.add(asignacion.getPregunta2());
            idsPreguntas.add(asignacion.getPregunta3());
            idsPreguntas.add(asignacion.getPregunta4());
            idsPreguntas.add(asignacion.getPregunta5());
        });

        idsPreguntas.forEach(idPregunta -> {
            Pregunta pregunta = preguntaRepository.findById(idPregunta).orElse(null);
            if (pregunta != null && !pregunta.isUsada()) {
                pregunta.setUsada(true);
                preguntaRepository.save(pregunta);
            }
        });
    }

    /**
     * *Calcula los puntos obtenidos en base a la evaluación y la clase de la
     * *pregunta.
     *
     * @param evaluacion    Evaluación obtenida.
     * @param maxPuntuacion Puntuación máxima de la pregunta.
     * @param clase         Clase de la pregunta (A o B).
     * @return Puntos calculados.
     */
    private int calcularPuntos(int evaluacion, int maxPuntuacion, String clase) {
        if (Constantes.CLASE_A.equalsIgnoreCase(clase)) {
            if (evaluacion >= 90)
                return maxPuntuacion;
            if (evaluacion >= 70)
                return maxPuntuacion / 2;
            if (evaluacion >= 50)
                return maxPuntuacion / 3;
        } else if (Constantes.CLASE_B.equalsIgnoreCase(clase)) {
            if (evaluacion >= 95)
                return maxPuntuacion;
            if (evaluacion >= 80)
                return maxPuntuacion - 2;
            if (evaluacion >= 65)
                return maxPuntuacion - 4;
        }
        return 0;
    }
}

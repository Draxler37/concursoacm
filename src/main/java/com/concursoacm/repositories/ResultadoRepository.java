package com.concursoacm.repositories;

import com.concursoacm.models.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, Integer> {

    // Listado de puntuaciones por equipo según su categoría ("Competencia" o "Junior")
    @Query("SELECT e.nombreEquipo, SUM(r.puntuacionTotal) " +
           "FROM Resultado r " +
           "JOIN Participante p ON p.idParticipante = r.idParticipante " +
           "JOIN Equipo e ON e.idEquipo = p.equipo.idEquipo " +
           "WHERE e.categoria = :categoria " +
           "GROUP BY e.idEquipo, e.nombreEquipo " +
           "ORDER BY SUM(r.puntuacionTotal) DESC")
    List<Object[]> listarPuntuacionesEquiposPorCategoria(String categoria);

    // Top 3 preguntas en equipos de la categoría Junior (suma de puntos por pregunta)
    @Query("SELECT p.idPregunta, p.texto, SUM(res.puntuacionObtenida) " +
           "FROM Respuesta res " +
           "JOIN Pregunta p ON p.idPregunta = res.idPregunta " +
           "JOIN Participante par ON par.idParticipante = res.idParticipante " +
           "JOIN Equipo e ON e.idEquipo = par.equipo.idEquipo " +
           "WHERE e.categoria = 'Junior' " +
           "GROUP BY p.idPregunta, p.texto " +
           "ORDER BY SUM(res.puntuacionObtenida) DESC")
    List<Object[]> obtenerTop3PreguntasJunior();

    // País con mayor puntuación en el concurso
    @Query("SELECT pa.nombrePais, SUM(r.puntuacionTotal) " +
           "FROM Resultado r " +
           "JOIN Participante p ON p.idParticipante = r.idParticipante " +
           "JOIN Equipo e ON e.idEquipo = p.equipo.idEquipo " +
           "JOIN Pais pa ON pa.idPais = e.pais.idPais " +
           "GROUP BY pa.idPais, pa.nombrePais " +
           "ORDER BY SUM(r.puntuacionTotal) DESC")
    List<Object[]> paisConMayorPuntuacion();

    // Región con mayor puntuación en el concurso
    @Query("SELECT reg.nombreRegion, SUM(r.puntuacionTotal) " +
           "FROM Resultado r " +
           "JOIN Participante p ON p.idParticipante = r.idParticipante " +
           "JOIN Equipo e ON e.idEquipo = p.equipo.idEquipo " +
           "JOIN Pais pa ON pa.idPais = e.pais.idPais " +
           "JOIN Region reg ON reg.idRegion = pa.region.idRegion " +
           "GROUP BY reg.idRegion, reg.nombreRegion " +
           "ORDER BY SUM(r.puntuacionTotal) DESC")
    List<Object[]> regionConMayorPuntuacion();
}








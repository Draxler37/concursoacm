package com.concursoacm.tools.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.concursoacm.models.Resultado;

import java.util.List;

/**
 * *Repositorio para la gestión de la entidad Resultado.
 * *Proporciona métodos para realizar operaciones CRUD y consultas
 * *personalizadas.
 */
@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, Integer> {

       /**
        * *Obtiene las puntuaciones de los equipos por categoría.
        *
        * @param categoria Categoría del equipo (Competencia o Junior).
        * @return Lista de objetos con el nombre del equipo y su puntuación total.
        */
       @Query("SELECT e.nombreEquipo, SUM(r.puntuacionTotal) " +
                     "FROM Resultado r " +
                     "JOIN Participante p ON p.idParticipante = r.idParticipante " +
                     "JOIN Equipo e ON e.idEquipo = p.equipo.idEquipo " +
                     "WHERE e.categoria = :categoria " +
                     "GROUP BY e.idEquipo, e.nombreEquipo " +
                     "ORDER BY SUM(r.puntuacionTotal) DESC")
       List<Object[]> listarPuntuacionesEquiposPorCategoria(String categoria);

       /**
        * *Obtiene el top 3 de preguntas más puntuadas en equipos de la categoría
        * Junior.
        *
        * @return Lista de objetos con el ID de la pregunta, su texto y la puntuación
        *         total.
        */
       @Query("SELECT p.idPregunta, p.texto, SUM(res.puntuacionObtenida) " +
                     "FROM Respuesta res " +
                     "JOIN Pregunta p ON p.idPregunta = res.idPregunta " +
                     "JOIN Participante par ON par.idParticipante = res.idParticipante " +
                     "JOIN Equipo e ON e.idEquipo = par.equipo.idEquipo " +
                     "WHERE e.categoria = 'Junior' " +
                     "GROUP BY p.idPregunta, p.texto " +
                     "ORDER BY SUM(res.puntuacionObtenida) DESC")
       List<Object[]> obtenerTop3PreguntasJunior();

       /**
        * *Obtiene el país con mayor puntuación en el concurso.
        *
        * @return Lista de objetos con el nombre del país y su puntuación total.
        */
       @Query("SELECT pa.nombrePais, SUM(r.puntuacionTotal) " +
                     "FROM Resultado r " +
                     "JOIN Participante p ON p.idParticipante = r.idParticipante " +
                     "JOIN Equipo e ON e.idEquipo = p.equipo.idEquipo " +
                     "JOIN Pais pa ON pa.idPais = e.pais.idPais " +
                     "GROUP BY pa.idPais, pa.nombrePais " +
                     "ORDER BY SUM(r.puntuacionTotal) DESC")
       List<Object[]> paisConMayorPuntuacion();

       /**
        * *Obtiene la región con mayor puntuación en el concurso.
        *
        * @return Lista de objetos con el nombre de la región y su puntuación total.
        */
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

package com.concursoacm.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.concursoacm.domain.models.JefeDelegacion;

import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * *Repositorio para la gestión de la entidad JefeDelegacion.
 * *Proporciona métodos para realizar operaciones CRUD y consultas
 * *personalizadas.
 */
@Repository
public interface JefeDelegacionRepository extends JpaRepository<JefeDelegacion, Integer> {

    /**
     * *Obtiene el nombre del país del jefe de delegación basado en el ID del
     * *participante.
     *
     * @param idParticipante ID del participante.
     * @return Nombre del país asociado al jefe de delegación.
     */
    @Query("SELECT p.nombrePais FROM JefeDelegacion j " +
            "JOIN j.participante part " +
            "JOIN part.pais p " +
            "WHERE j.participante.idParticipante = :idParticipante")
    Optional<String> findPaisByParticipanteId(@Param("idParticipante") int idParticipante);

    /**
     * *Cuenta el número de jefes de delegación asociados a un país.
     *
     * @param idPais ID del país.
     * @return Número de jefes de delegación en el país.
     */
    @Query("SELECT COUNT(j) FROM JefeDelegacion j WHERE j.participante.pais.idPais = :idPais")
    int countByPaisId(@Param("idPais") int idPais);

    /**
     * *Busca un jefe de delegación por su usuario normalizado.
     *
     * @param usuarioNormalizado Usuario normalizado.
     * @return Objeto JefeDelegacion si se encuentra.
     */
    Optional<JefeDelegacion> findByUsuarioNormalizado(String usuarioNormalizado);

    /**
     * *Busca un jefe de delegación por el ID del participante.
     *
     * @param idParticipante ID del participante.
     * @return Objeto JefeDelegacion si se encuentra.
     */
    Optional<JefeDelegacion> findByParticipanteIdParticipante(int idParticipante);
}

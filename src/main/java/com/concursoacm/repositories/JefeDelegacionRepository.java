package com.concursoacm.repositories;

import com.concursoacm.models.JefeDelegacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

@Repository
public interface JefeDelegacionRepository extends JpaRepository<JefeDelegacion, Integer> {

    // Obtener el nombre del país del jefe de delegación basado en el ID del
    // participante
    @Query("SELECT p.nombrePais FROM JefeDelegacion j " +
            "JOIN j.participante part " +
            "JOIN part.pais p " +
            "WHERE j.participante.idParticipante = :idParticipante")
    Optional<String> obtenerPaisPorIdParticipante(@Param("idParticipante") int idParticipante);

    @Query("SELECT COUNT(j) FROM JefeDelegacion j WHERE j.participante.pais.idPais = :idPais")
    int countByPaisId(@Param("idPais") int idPais);

    Optional<JefeDelegacion> findByUsuarioNormalizado(String usuarioNormalizado);

    Optional<JefeDelegacion> findByParticipanteIdParticipante(int idParticipante);
}

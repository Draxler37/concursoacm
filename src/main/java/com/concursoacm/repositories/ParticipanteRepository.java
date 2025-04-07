package com.concursoacm.repositories;

import com.concursoacm.models.Participante;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * *Repositorio para la gestión de la entidad Participante.
 * *Proporciona métodos para realizar operaciones CRUD y consultas
 * *personalizadas.
 */
@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Integer> {

    /**
     * *Obtiene una lista de participantes filtrados por el ID de la región (a
     * través del país).
     *
     * @param idRegion ID de la región.
     * @return Lista de participantes en la región.
     */
    List<Participante> findParticipantesPorRegionId(int idRegion);

    /**
     * *Obtiene una lista de participantes filtrados por el ID del país.
     *
     * @param idPais ID del país.
     * @return Lista de participantes en el país.
     */
    List<Participante> findParticipantesPorPaisId(int idPais);

    /**
     * *Obtiene una lista de participantes que pertenecen a un equipo específico.
     *
     * @param idEquipo ID del equipo.
     * @return Lista de participantes en el equipo.
     */
    List<Participante> findParticipantesPorEquipoId(int idEquipo);

    /**
     * *Cuenta el número de participantes asociados a un país específico.
     *
     * @param idPais ID del país.
     * @return Número de participantes en el país.
     */
    int countByPaisIdPais(int idPais);

    /**
     * *Cuenta el número de participantes asociados a un equipo específico.
     *
     * @param idEquipo ID del equipo.
     * @return Número de participantes en el equipo.
     */
    int countByEquipoIdEquipo(int idEquipo);
}

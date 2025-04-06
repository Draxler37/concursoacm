package com.concursoacm.services.interfaces;

import com.concursoacm.dtos.ParticipanteDTO;
import com.concursoacm.dtos.ParticipantesPorPaisDTO;
import com.concursoacm.dtos.ParticipantesPorRegionDTO;
import com.concursoacm.models.Participante;

import java.util.List;

/**
 * *Interfaz que define los métodos para la gestión de participantes.
 */
public interface IParticipanteService {

    /**
     * *Obtiene todos los participantes como DTOs.
     *
     * @return Lista de objetos ParticipanteDTO.
     */
    List<ParticipanteDTO> getAllParticipantes();

    /**
     * *Obtiene los participantes por país y los empaqueta en un DTO.
     *
     * @param idPais ID del país.
     * @return Objeto ParticipantesPorPaisDTO.
     */
    ParticipantesPorPaisDTO getParticipantesPorPaisDTO(int idPais);

    /**
     * *Obtiene los participantes por región y los empaqueta en un DTO.
     *
     * @param idRegion ID de la región.
     * @return Objeto ParticipantesPorRegionDTO.
     */
    ParticipantesPorRegionDTO getParticipantesPorRegionDTO(int idRegion);

    /**
     * *Obtiene los participantes por equipo como DTOs.
     *
     * @param idEquipo ID del equipo.
     * @return Lista de objetos ParticipanteDTO.
     */
    List<ParticipanteDTO> getParticipantesPorEquipoDTO(int idEquipo);

    /**
     * *Obtiene un participante por su ID como DTO.
     *
     * @param id ID del participante.
     * @return Objeto ParticipanteDTO o null si no se encuentra.
     */
    ParticipanteDTO getParticipanteById(int id);

    /**
     * *Agrega un nuevo participante.
     *
     * @param participante Objeto Participante a agregar.
     * @return Objeto ParticipanteDTO del participante agregado.
     */
    ParticipanteDTO addParticipante(Participante participante);

    /**
     * *Asigna un participante a un equipo.
     *
     * @param idParticipante ID del participante.
     * @param idEquipo       ID del equipo.
     * @return Objeto Participante actualizado.
     */
    Participante asignarParticipanteAEquipo(int idParticipante, int idEquipo);

    /**
     * *Quita un participante de un equipo.
     *
     * @param idParticipante ID del participante.
     * @param idEquipo       ID del equipo.
     * @return Objeto Participante actualizado.
     */
    Participante quitarParticipanteDeEquipo(int idParticipante, int idEquipo);

    /**
     * *Actualiza un participante existente.
     *
     * @param id           ID del participante a actualizar.
     * @param participante Objeto Participante con los nuevos datos.
     * @return Objeto ParticipanteDTO actualizado o null si no se encuentra.
     */
    ParticipanteDTO updateParticipante(int id, Participante participante);

    /**
     * *Elimina un participante por su ID.
     *
     * @param id ID del participante a eliminar.
     * @return true si el participante fue eliminado, false en caso contrario.
     */
    boolean deleteParticipante(int id);
}
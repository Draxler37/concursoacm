package com.concursoacm.services.interfaces;

import com.concursoacm.dtos.EquipoDTO;
import com.concursoacm.models.Equipo;

import java.util.List;

/**
 * *Interfaz que define los métodos para la gestión de equipos.
 */
public interface IEquipoService {

    /**
     * *Obtiene una lista de todos los equipos.
     *
     * @return Lista de objetos EquipoDTO.
     */
    List<EquipoDTO> getAllEquipos();

    /**
     * *Obtiene un equipo por su ID.
     *
     * @param id ID del equipo.
     * @return Objeto EquipoDTO si se encuentra.
     */
    EquipoDTO getEquipoById(int id);

    /**
     * *Guarda un equipo (creación o actualización).
     *
     * @param equipo             Objeto Equipo a guardar.
     * @param usuarioNormalizado Usuario autenticado.
     * @return Objeto EquipoDTO del equipo guardado.
     */
    EquipoDTO guardarEquipo(Equipo equipo, String usuarioNormalizado);

    /**
     * *Elimina un equipo por su ID.
     *
     * @param idEquipo           ID del equipo a eliminar.
     * @param usuarioNormalizado Usuario autenticado.
     */
    void eliminarEquipo(int idEquipo, String usuarioNormalizado);
}
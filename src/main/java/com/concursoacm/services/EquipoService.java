package com.concursoacm.services;

import com.concursoacm.dtos.EquipoDTO;
import com.concursoacm.models.Equipo;
import com.concursoacm.models.Pais;
import com.concursoacm.repositories.EquipoRepository;
import com.concursoacm.repositories.JefeDelegacionRepository;
import com.concursoacm.repositories.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private JefeDelegacionRepository jefeDelegacionRepository;

    @Autowired
    private PaisRepository paisRepository;

    // Obtener todos los equipos como DTOs
    public List<EquipoDTO> getAllEquipos() {
        return equipoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Obtener un equipo por ID como DTO
    public EquipoDTO getEquipoById(int id) {
        return equipoRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }

    // Guardar o actualizar un equipo
    public Equipo guardarEquipo(Equipo equipo, String usuarioNormalizado) {
        // Obtener el ID del participante del jefe autenticado
        int idParticipanteJefe = jefeDelegacionRepository.findByUsuarioNormalizado(usuarioNormalizado)
                .map(jefe -> jefe.getParticipante().getIdParticipante())
                .orElseThrow(() -> new SecurityException("Usuario no encontrado como jefe de delegación."));

        // Obtener el país del jefe basado en su idParticipante
        String paisJefe = jefeDelegacionRepository.obtenerPaisPorIdParticipante(idParticipanteJefe)
                .orElseThrow(() -> new SecurityException("No tienes un país asignado."));

        if (equipo.getIdEquipo() != 0) { // Actualización de equipo
            Equipo equipoExistente = equipoRepository.findById(equipo.getIdEquipo())
                    .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado."));

            // Obtener el país del equipo desde la BD
            String paisEquipo = equipoExistente.getPais().getNombrePais();

            // Validar que el usuario autenticado solo pueda modificar equipos de su país
            if (!paisEquipo.equalsIgnoreCase(paisJefe)) {
                throw new SecurityException("No puedes modificar equipos de un país diferente al tuyo.");
            }

            equipo.setPais(equipoExistente.getPais()); // Mantener el país original
        } else { // Creación de equipo
            Pais paisCompleto = paisRepository.findById(equipo.getPais().getIdPais())
                    .orElseThrow(() -> new IllegalArgumentException("El país especificado no existe."));

            if (!paisCompleto.getNombrePais().equalsIgnoreCase(paisJefe)) {
                throw new SecurityException("No puedes crear equipos para un país diferente al tuyo.");
            }

            equipo.setPais(paisCompleto);
        }
        
        int idPais = equipo.getPais().getIdPais();
        if (equipo.getCategoria().equalsIgnoreCase("Competencia")) {
            int countCompetencia = equipoRepository.countByPaisIdPaisAndCategoria(idPais, "Competencia");
            if (countCompetencia >= 2) {
                throw new IllegalArgumentException("Ya existen 2 equipos de competencia para este país.");
            }
        } else if (equipo.getCategoria().equalsIgnoreCase("Junior")) {
            int countJunior = equipoRepository.countByPaisIdPaisAndCategoria(idPais, "Junior");
            if (countJunior >= 1) {
                throw new IllegalArgumentException("Ya existe un equipo Junior para este país.");
            }
        }

        return equipoRepository.save(equipo);
    }


    // Eliminar un equipo
    public void eliminarEquipo(int idEquipo, int idParticipanteJefe) {
        Equipo equipo = equipoRepository.findById(idEquipo)
                .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado"));

        // Validar el país del jefe
        String paisJefe = jefeDelegacionRepository.obtenerPaisPorIdParticipante(idParticipanteJefe)
                .orElseThrow(() -> new SecurityException("No tienes un país asignado."));

        if (!equipo.getPais().getNombrePais().equalsIgnoreCase(paisJefe)) {
            throw new SecurityException("No puedes eliminar equipos de un país diferente al tuyo.");
        }

        equipoRepository.deleteById(idEquipo);
    }

    // Convertir un Equipo en EquipoDTO
    private EquipoDTO convertToDto(Equipo equipo) {
        return new EquipoDTO(
                equipo.getIdEquipo(),
                equipo.getNombreEquipo(),
                equipo.getCategoria(),
                equipo.getPais().getNombrePais()
        );
    }
}









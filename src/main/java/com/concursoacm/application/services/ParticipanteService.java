package com.concursoacm.application.services;

import com.concursoacm.application.dtos.pais.PaisDTO;
import com.concursoacm.application.dtos.participantes.ParticipanteDTO;
import com.concursoacm.application.dtos.participantes.ParticipantesPorPaisDTO;
import com.concursoacm.application.dtos.participantes.ParticipantesPorRegionDTO;
import com.concursoacm.domain.models.Equipo;
import com.concursoacm.domain.models.JefeDelegacion;
import com.concursoacm.domain.models.Pais;
import com.concursoacm.domain.models.Participante;
import com.concursoacm.domain.services.IParticipanteService;
import com.concursoacm.infrastructure.repositories.EquipoRepository;
import com.concursoacm.infrastructure.repositories.JefeDelegacionRepository;
import com.concursoacm.infrastructure.repositories.ParticipanteRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * *Servicio que implementa la lógica de negocio para la gestión de
 * participantes.
 */
@Service
public class ParticipanteService implements IParticipanteService {

    private final ParticipanteRepository participanteRepository;
    private final PaisService paisService;
    private final EquipoRepository equipoRepository;
    private final JefeDelegacionRepository jefeDelegacionRepository;

    /**
     * *Constructor que inyecta las dependencias necesarias.
     *
     * @param participanteRepository   Repositorio para la gestión de participantes.
     * @param paisService              Servicio para la gestión de países.
     * @param equipoRepository         Repositorio para la gestión de equipos.
     * @param jefeDelegacionRepository Repositorio para la gestión de jefes de
     *                                 delegación.
     */

    public ParticipanteService(ParticipanteRepository participanteRepository,
            PaisService paisService,
            EquipoRepository equipoRepository,
            JefeDelegacionRepository jefeDelegacionRepository) {
        this.participanteRepository = participanteRepository;
        this.paisService = paisService;
        this.equipoRepository = equipoRepository;
        this.jefeDelegacionRepository = jefeDelegacionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParticipanteDTO> getAllParticipantes() {
        return participanteRepository.findAll().stream()
                .map(this::convertirAParticipanteDTO)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParticipantesPorPaisDTO getParticipantesPorPaisDTO(int idPais) {
        List<Participante> participantes = participanteRepository.findByPaisIdPais(idPais);
        if (participantes.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron participantes para el país con ID " + idPais);
        }
        String nombrePais = participantes.get(0).getPais().getNombrePais();
        List<ParticipanteDTO> dtos = participantes.stream()
                .map(this::convertirAParticipanteDTO)
                .collect(Collectors.toList());
        return new ParticipantesPorPaisDTO(nombrePais, dtos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParticipantesPorRegionDTO getParticipantesPorRegionDTO(int idRegion) {
        List<Participante> participantes = participanteRepository.findByPaisRegionIdRegion(idRegion);
        if (participantes.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron participantes para la región con ID " + idRegion);
        }
        String nombreRegion = participantes.get(0).getPais().getRegion().getNombreRegion();
        List<ParticipanteDTO> dtos = participantes.stream()
                .map(this::convertirAParticipanteDTO)
                .collect(Collectors.toList());
        return new ParticipantesPorRegionDTO(nombreRegion, dtos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParticipanteDTO> getParticipantesPorEquipoDTO(int idEquipo) {
        List<Participante> participantes = participanteRepository.findByEquipoIdEquipo(idEquipo);
        return participantes.stream()
                .map(this::convertirAParticipanteDTO)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParticipanteDTO getParticipanteById(int id) {
        return participanteRepository.findById(id)
                .map(this::convertirAParticipanteDTO)
                .orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParticipanteDTO crearParticipante(Participante participante) {
        PaisDTO paisDTO = paisService.obtenerPaisPorId(participante.getPais().getIdPais())
                .orElseThrow(() -> new RuntimeException("País no encontrado"));
        Pais pais = new Pais();
        pais.setIdPais(paisDTO.getIdPais());
        pais.setNombrePais(paisDTO.getNombrePais());

        participante.setPais(pais);
        participante.setPais(pais);

        int count = participanteRepository.countByPaisIdPais(participante.getPais().getIdPais());
        if (count >= 15) {
            throw new IllegalArgumentException("Ya se alcanzó el máximo de 15 participantes para este país.");
        }

        Participante nuevoParticipante = participanteRepository.save(participante);
        return convertirAParticipanteDTO(nuevoParticipante);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParticipanteDTO asignarAlEquipo(int idParticipante, int idEquipo, int idJefeDelegacion) {
        Participante participante = buscarParticipantePorId(idParticipante);
        Equipo equipo = equipoRepository.findById(idEquipo)
                .orElseThrow(() -> new IllegalArgumentException("El equipo con ID " + idEquipo + " no existe."));
        JefeDelegacion jefeDelegacion = jefeDelegacionRepository.findById(idJefeDelegacion)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El jefe de delegación con ID " + idJefeDelegacion + " no existe."));

        // Validar que el jefe de delegación no esté asignado a ningún equipo
        if (jefeDelegacion.getParticipante().getIdParticipante() == idParticipante) {
            throw new IllegalArgumentException("El jefe de delegación no puede estar asignado a ningún equipo.");
        }

        // Validar que el jefe de delegación solo pueda asignar participantes de su país
        if (jefeDelegacion.getParticipante().getPais().getIdPais() != participante.getPais().getIdPais()) {
            throw new SecurityException("No tienes permiso para asignar participantes de otro país.");
        }

        // Validar que el equipo pertenezca al mismo país que el participante
        if (participante.getPais().getIdPais() != equipo.getPais().getIdPais()) {
            throw new IllegalArgumentException("El equipo no pertenece al mismo país que el participante.");
        }

        // Validar que el participante no esté asignado a otro equipo
        if (participante.getEquipo() != null) {
            throw new IllegalArgumentException("El participante ya está asignado al equipo: "
                    + participante.getEquipo().getNombreEquipo());
        }

        // Validar que los menores de edad solo puedan estar en equipos "Junior"
        if (participante.getEdad() < 18 && !"Junior".equalsIgnoreCase(equipo.getCategoria())) {
            throw new IllegalArgumentException(
                    "Los participantes menores de edad solo pueden estar en equipos Junior.");
        }

        // Validar que los mayores de edad solo puedan estar en equipos "Competencia"
        if (participante.getEdad() >= 18 && !"Competencia".equalsIgnoreCase(equipo.getCategoria())) {
            throw new IllegalArgumentException(
                    "Los participantes mayores de edad solo pueden estar en equipos Competencia.");
        }

        // Validar que no haya más de 3 participantes en el equipo
        int participantesEnEquipo = participanteRepository.countByEquipoIdEquipo(idEquipo);
        if (participantesEnEquipo >= 3) {
            throw new IllegalArgumentException("El equipo ya tiene 3 participantes asignados.");
        }

        participante.setEquipo(equipo);
        participanteRepository.save(participante);

        return convertirAParticipanteDTO(participante);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParticipanteDTO quitarDelEquipo(int idParticipante, int idEquipo, int idJefeDelegacion) {
        Participante participante = buscarParticipantePorId(idParticipante);
        JefeDelegacion jefeDelegacion = jefeDelegacionRepository.findById(idJefeDelegacion)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El jefe de delegación con ID " + idJefeDelegacion + " no existe."));

        // Validar que el participante tenga un equipo asignado
        if (participante.getEquipo() == null) {
            throw new IllegalArgumentException("El participante no tiene un equipo asignado.");
        }

        // Validar que el equipo asignado al participante coincida con el ID del equipo
        // proporcionado
        if (participante.getEquipo().getIdEquipo() != idEquipo) {
            throw new IllegalArgumentException("El participante no está asignado al equipo con ID " + idEquipo + ".");
        }

        // Validar que el jefe de delegación solo pueda quitar participantes de equipos
        // de su país
        if (jefeDelegacion.getParticipante().getPais().getIdPais() != participante.getEquipo().getPais().getIdPais()) {
            throw new SecurityException("No tienes permiso para quitar participantes de equipos de otro país.");
        }

        participante.setEquipo(null); // Quitar el equipo
        participanteRepository.save(participante);

        return convertirAParticipanteDTO(participante);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParticipanteDTO updateParticipante(int id, Participante participante) {
        Participante existente = participanteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Participante no encontrado con ID " + id));

        existente.setNombre(participante.getNombre());
        existente.setNumCarnet(participante.getNumCarnet());
        existente.setEdad(participante.getEdad());
        existente.setSexo(participante.getSexo());

        if (participante.getPais() != null) {
            PaisDTO paisDTO = paisService.obtenerPaisPorId(participante.getPais().getIdPais())
                    .orElseThrow(() -> new RuntimeException("País no encontrado"));
            Pais pais = new Pais();
            pais.setIdPais(paisDTO.getIdPais());
            pais.setNombrePais(paisDTO.getNombrePais());
            existente.setPais(pais);
        }

        Participante actualizado = participanteRepository.save(existente);
        return convertirAParticipanteDTO(actualizado);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteParticipante(int id) {
        if (participanteRepository.existsById(id)) {
            participanteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * *Busca un participante por su ID.
     *
     * @param idParticipante int idParticipante.
     * @return Objeto Participante.
     */
    private Participante buscarParticipantePorId(int idParticipante) {
        return participanteRepository.findById(idParticipante)
                .orElseThrow(
                        () -> new IllegalArgumentException("El participante con ID " + idParticipante + " no existe."));
    }

    /**
     * *Convierte un objeto Participante en un ParticipanteDTO.
     *
     * @param participante Objeto Participante.
     * @return Objeto ParticipanteDTO.
     */

    private ParticipanteDTO convertirAParticipanteDTO(Participante participante) {
        return new ParticipanteDTO(
                participante.getIdParticipante(),
                participante.getNombre(),
                participante.getNumCarnet(),
                participante.getEdad(),
                participante.getSexo(),
                participante.getPais().getNombrePais(),
                participante.getEquipo() != null ? participante.getEquipo().getNombreEquipo() : "Sin equipo");
    }
}

package com.concursoacm.services;

import com.concursoacm.dtos.JefeDelegacionDTO;
import com.concursoacm.models.JefeDelegacion;
import com.concursoacm.models.Participante;
import com.concursoacm.repositories.JefeDelegacionRepository;
import com.concursoacm.repositories.ParticipanteRepository;
import com.concursoacm.services.interfaces.IJefeDelegacionService;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * *Servicio que implementa la lógica de negocio para la gestión de jefes de
 * delegación.
 */
@Service
public class JefeDelegacionService implements IJefeDelegacionService {

    private final JefeDelegacionRepository jefeDelegacionRepository;
    private final ParticipanteRepository participanteRepository;

    /**
     * *Constructor que inyecta las dependencias necesarias.
     *
     * @param jefeDelegacionRepository Repositorio para la gestión de jefes de
     *                                 delegación.
     * @param participanteRepository   Repositorio para la gestión de participantes.
     */
    public JefeDelegacionService(JefeDelegacionRepository jefeDelegacionRepository,
            ParticipanteRepository participanteRepository) {
        this.jefeDelegacionRepository = jefeDelegacionRepository;
        this.participanteRepository = participanteRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JefeDelegacionDTO> obtenerTodosLosJefes() {
        return jefeDelegacionRepository.findAll().stream()
                .map(jefe -> new JefeDelegacionDTO(
                        jefe.getIdJefe(),
                        jefe.getParticipante().getNombre()))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<JefeDelegacionDTO> obtenerJefePorId(int idJefe) {
        return jefeDelegacionRepository.findById(idJefe)
                .map(jefe -> new JefeDelegacionDTO(
                        jefe.getIdJefe(),
                        jefe.getParticipante().getNombre()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JefeDelegacion crearJefeDelegacion(int idParticipante, String contraseña) {
        Participante participante = validarParticipanteParaJefe(idParticipante);

        JefeDelegacion jefe = new JefeDelegacion();
        jefe.setParticipante(participante);
        jefe.setContrasena(contraseña);
        jefe.setUsuarioNormalizado(normalizarTexto(participante.getNombre()));

        return jefeDelegacionRepository.save(jefe);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean cambiarContraseña(int idJefe, String usuarioNormalizado, String contraseñaActual,
            String nuevaContraseña) {
        JefeDelegacion jefe = jefeDelegacionRepository.findById(idJefe)
                .orElseThrow(() -> new IllegalArgumentException("Jefe de delegación no encontrado."));

        if (!jefe.getUsuarioNormalizado().equals(usuarioNormalizado)) {
            throw new SecurityException("No puedes cambiar la contraseña de otro usuario.");
        }

        if (!jefe.getContrasena().equals(contraseñaActual)) {
            return false;
        }

        jefe.setContrasena(nuevaContraseña);
        jefeDelegacionRepository.save(jefe);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void eliminarJefeDelegacion(int idJefe) {
        if (!jefeDelegacionRepository.existsById(idJefe)) {
            throw new IllegalArgumentException("El jefe de delegación con ID " + idJefe + " no existe.");
        }
        jefeDelegacionRepository.deleteById(idJefe);
    }

    /**
     * *Valida si un participante puede ser jefe de delegación.
     *
     * @param idParticipante ID del participante.
     * @return Objeto Participante validado.
     */
    private Participante validarParticipanteParaJefe(int idParticipante) {
        Participante participante = participanteRepository.findById(idParticipante)
                .orElseThrow(
                        () -> new IllegalArgumentException("El participante con ID " + idParticipante + " no existe."));

        if (participante.getEdad() < 18) {
            throw new IllegalArgumentException("El participante debe ser mayor de edad para ser jefe de delegación.");
        }

        if (jefeDelegacionRepository.findByParticipanteIdParticipante(idParticipante).isPresent()) {
            throw new IllegalArgumentException("El participante ya es un jefe de delegación.");
        }

        int idPais = participante.getPais().getIdPais();
        int countJefes = jefeDelegacionRepository.countByPaisId(idPais);
        if (countJefes >= 1) {
            throw new IllegalArgumentException("Ya existe un jefe de delegación para este país.");
        }

        return participante;
    }

    /**
     * *Normaliza un texto eliminando acentos y convirtiéndolo a minúsculas.
     *
     * @param texto Texto a normalizar.
     * @return Texto normalizado.
     */
    private String normalizarTexto(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase();
    }
}

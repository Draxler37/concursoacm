package com.concursoacm.application.services;

import com.concursoacm.application.dtos.jefedelegacion.JefeDelegacionDTO;
import com.concursoacm.domain.models.JefeDelegacion;
import com.concursoacm.domain.models.Participante;
import com.concursoacm.domain.services.IJefeDelegacionService;
import com.concursoacm.infrastructure.repositories.JefeDelegacionRepository;
import com.concursoacm.infrastructure.repositories.ParticipanteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * *Servicio que implementa la lógica de negocio para la gestión de jefes de
 * *delegación.
 */
@Service
public class JefeDelegacionService implements IJefeDelegacionService {

    private final PasswordEncoder passwordEncoder;

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
            ParticipanteRepository participanteRepository, PasswordEncoder passwordEncoder) {
        this.jefeDelegacionRepository = jefeDelegacionRepository;
        this.participanteRepository = participanteRepository;
        this.passwordEncoder = passwordEncoder;
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
        JefeDelegacion jefe = buscarJefePorId(idJefe); // Reutilizamos el método privado
        return Optional.of(new JefeDelegacionDTO(jefe.getIdJefe(), jefe.getParticipante().getNombre()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JefeDelegacion crearJefeDelegacion(int idParticipante, String usuario, String contraseña) {
        return jefeDelegacionRepository
                .save(new JefeDelegacion(normalizarTexto(usuario), passwordEncoder.encode(contraseña),
                        validarParticipanteParaJefe(idParticipante, usuario, contraseña)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean cambiarContraseña(int idJefe, String usuario, String contraseñaActual,
            String nuevaContraseña) {
        JefeDelegacion jefeDelegacion = jefeDelegacionRepository.findById(idJefe)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El jefe de delegación con ID " + idJefe + " no existe."));

        validarCambioDeContraseña(jefeDelegacion, usuario, contraseñaActual, nuevaContraseña);

        jefeDelegacion.setContrasena(passwordEncoder.encode(nuevaContraseña));
        jefeDelegacionRepository.save(jefeDelegacion);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void eliminarJefeDelegacion(int idJefe) {
        JefeDelegacion jefe = buscarJefePorId(idJefe); // Reutilizamos el método privado
        jefeDelegacionRepository.delete(jefe);
    }

    /**
     * *Valida si un participante puede ser jefe de delegación.
     *
     * @param idParticipante ID del participante.
     * @param usuario        Usuario.
     * @param contraseña     Contraseña actual ingresada.
     * @return Objeto Participante validado.
     */
    private Participante validarParticipanteParaJefe(int idParticipante, String usuario, String contraseña) {
        Participante participante = participanteRepository.findById(idParticipante)
                .orElseThrow(
                        () -> new IllegalArgumentException("El participante con ID " + idParticipante + " no existe."));

        if (jefeDelegacionRepository.findByParticipanteIdParticipante(idParticipante).isPresent()) {
            throw new IllegalArgumentException("El participante ya es un jefe de delegación.");
        }

        if (participante.getEdad() < 18) {
            throw new IllegalArgumentException("El participante debe ser mayor de edad para ser jefe de delegación.");
        }

        if (usuario == null || usuario.isBlank() || contraseña == null
                || contraseña.isBlank()) {
            throw new IllegalArgumentException("El 'Usuario' y la 'Contraseña' son obligatorias.");
        }

        if (jefeDelegacionRepository.countByPaisId(participante.getPais().getIdPais()) >= 1) {
            throw new IllegalArgumentException("Ya existe un jefe de delegación para este país.");
        }

        validarLongitudContraseña(contraseña);

        return participante;
    }

    /**
     * *Valida las condiciones necesarias para cambiar la contraseña de un jefe de
     * *delegación.
     *
     * @param jefe             Objeto JefeDelegacion.
     * @param usuario          Usuario.
     * @param contraseñaActual Contraseña actual ingresada.
     * @param nuevaContraseña  Nueva contraseña ingresada.
     */
    private void validarCambioDeContraseña(JefeDelegacion jefe, String usuario, String contraseñaActual,
            String nuevaContraseña) {
        if (contraseñaActual == null || contraseñaActual.isBlank() || nuevaContraseña == null
                || nuevaContraseña.isBlank()) {
            throw new IllegalArgumentException("La 'contraseñaActual' y la 'nuevaContraseña' son obligatorias.");
        }

        if (!jefe.getUsuarioNormalizado().equals(usuario)) {
            throw new SecurityException("No puedes cambiar la contraseña de otro usuario.");
        }

        validarLongitudContraseña(nuevaContraseña);

        if (!passwordEncoder.matches(contraseñaActual, jefe.getContrasena())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta.");
        }
    }

    /**
     * *Valida que la contraseña cumpla con la longitud mínima requerida.
     *
     * @param contraseña Contraseña a validar.
     */
    private void validarLongitudContraseña(String contraseña) {
        if (contraseña.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        }
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

    /**
     * *Método privado para buscar un jefe de delegación por su ID.
     *
     * @param idJefe ID del jefe de delegación.
     * @return Objeto JefeDelegacion si se encuentra.
     * @throws IllegalArgumentException si no se encuentra el jefe de delegación.
     */
    private JefeDelegacion buscarJefePorId(int idJefe) {
        return jefeDelegacionRepository.findById(idJefe)
                .orElseThrow(() -> new IllegalArgumentException("El jefe de delegación con ID " + idJefe + " no existe."));
    }
}

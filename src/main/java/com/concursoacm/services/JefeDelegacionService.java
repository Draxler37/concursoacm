package com.concursoacm.services;

import com.concursoacm.dtos.JefeDelegacionDTO;
import com.concursoacm.models.JefeDelegacion;
import com.concursoacm.models.Participante;
import com.concursoacm.repositories.JefeDelegacionRepository;
import com.concursoacm.repositories.ParticipanteRepository;
import java.text.Normalizer;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JefeDelegacionService {

    @Autowired
    private JefeDelegacionRepository jefeDelegacionRepository;

    @Autowired
    private ParticipanteRepository participanteRepository;
    
    // Obtener todos los jefes de delegación (sin usuario normalizado)
    public List<JefeDelegacionDTO> obtenerTodosLosJefes() {
        return jefeDelegacionRepository.findAll().stream()
                .map(jefe -> new JefeDelegacionDTO(
                        jefe.getIdJefe(),
                        jefe.getParticipante().getNombre()
                ))
                .collect(Collectors.toList());
    }

    // Obtener un jefe de delegación por ID (sin usuario normalizado)
    public Optional<JefeDelegacionDTO> obtenerJefePorId(int idJefe) {
        return jefeDelegacionRepository.findById(idJefe)
                .map(jefe -> new JefeDelegacionDTO(
                        jefe.getIdJefe(),
                        jefe.getParticipante().getNombre()
                ));
    }

    //Crear un nuevo jefe delegación
    public JefeDelegacion crearJefeDelegacion(int idParticipante, String contraseña) {
        Optional<Participante> participanteOpt = participanteRepository.findById(idParticipante);

        if (participanteOpt.isEmpty()) {
            throw new IllegalArgumentException("El participante con ID " + idParticipante + " no existe.");
        }      

        if (jefeDelegacionRepository.findByParticipanteIdParticipante(idParticipante).isPresent()) {
            throw new IllegalArgumentException("El participante ya es un jefe de delegación.");
        }

        Participante participante = participanteOpt.get();
        if (participante.getEdad() < 18) {
            throw new IllegalArgumentException("El participante debe ser mayor de edad para ser jefe de delegación.");
        }
        
        int idPais = participante.getPais().getIdPais();
        int countJefes = jefeDelegacionRepository.countByPaisId(idPais);
        if (countJefes >= 1) {
            throw new IllegalArgumentException("Ya existe un jefe de delegación para este país.");
        }
        JefeDelegacion jefe = new JefeDelegacion();
        jefe.setParticipante(participante);
        jefe.setContrasena(contraseña);
        jefe.setUsuarioNormalizado(normalizarTexto(participante.getNombre()));

        return jefeDelegacionRepository.save(jefe);
    }
    
    // Actualizar un jefe de delegación
    public boolean cambiarContraseña(int idJefe, String usuarioNormalizado, String contraseñaActual, String nuevaContraseña) {
        Optional<JefeDelegacion> optionalJefe = jefeDelegacionRepository.findById(idJefe);

        if (optionalJefe.isEmpty()) {
            throw new IllegalArgumentException("Jefe de delegación no encontrado.");
        }

        JefeDelegacion jefe = optionalJefe.get();
        
        // Verificar que el usuario autenticado es el dueño de este ID
        if (!jefe.getUsuarioNormalizado().equals(usuarioNormalizado)) {
            throw new SecurityException("No puedes cambiar la contraseña de otro usuario.");
        }

        // Verificar la contraseña actual
        if (!contraseñaActual.equals(nuevaContraseña)) {
            return false; // Contraseña incorrecta
        }

        // Encriptar y actualizar la nueva contraseña
        jefe.setContrasena(nuevaContraseña);
        jefeDelegacionRepository.save(jefe);

        return true;
    }


    // Eliminar un jefe de delegación
    public void eliminarJefeDelegacion(int idJefe) {
        if (!jefeDelegacionRepository.existsById(idJefe)) {
            throw new IllegalArgumentException("El jefe de delegación con ID " + idJefe + " no existe.");
        }
        jefeDelegacionRepository.deleteById(idJefe);
    }

    // Método para normalizar nombres
    private String normalizarTexto(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "") // Elimina acentos
                .toLowerCase(); // Convierte a minúsculas
    }

    // Obtener el ID del participante del jefe de delegación autenticado
    public int obtenerIdParticipantePorUsuario(String usuarioNormalizado) {
        return jefeDelegacionRepository.findByUsuarioNormalizado(usuarioNormalizado)
                .map(jefe -> jefe.getParticipante().getIdParticipante())
                .orElseThrow(() -> new SecurityException("Usuario no encontrado."));
    }
}






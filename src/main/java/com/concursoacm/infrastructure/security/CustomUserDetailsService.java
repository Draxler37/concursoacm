package com.concursoacm.infrastructure.security;

import com.concursoacm.domain.models.JefeDelegacion;
import com.concursoacm.infrastructure.repositories.JefeDelegacionRepository;
import com.concursoacm.infrastructure.utils.Constantes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private JefeDelegacionRepository jefeDelegacionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca al jefe de delegación por usuario_normalizado
        JefeDelegacion jefeDelegacion = jefeDelegacionRepository.findByUsuarioNormalizado(username)
                .orElseThrow(() -> new UsernameNotFoundException(Constantes.ERROR_USUARIO_NO_ENCONTRADO));

        return User.builder()
                .username(jefeDelegacion.getUsuarioNormalizado())
                .password(jefeDelegacion.getContrasena()) // Contraseña encriptada
                .roles(Constantes.ROL_JEFE_DELEGACION) // Asigna el rol
                .build();
    }
}

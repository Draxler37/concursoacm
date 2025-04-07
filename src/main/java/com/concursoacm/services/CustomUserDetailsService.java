package com.concursoacm.services;

import com.concursoacm.models.JefeDelegacion;
import com.concursoacm.repositories.JefeDelegacionRepository;
import com.concursoacm.utils.Constantes;

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
        // Busca al jefe de delegación usando la columna usuario_normalizado
        JefeDelegacion jefeDelegacion = jefeDelegacionRepository.findByUsuarioNormalizado(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.builder()
                .username(jefeDelegacion.getUsuarioNormalizado())
                .password(jefeDelegacion.getContrasena())
                .roles(Constantes.ROL_JEFE_DELEGACION)
                .build();
    }
}

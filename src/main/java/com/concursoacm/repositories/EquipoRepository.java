package com.concursoacm.repositories;

import com.concursoacm.models.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Integer> {
    int countByPaisIdPaisAndCategoria(int idPais, String categoria);    
}


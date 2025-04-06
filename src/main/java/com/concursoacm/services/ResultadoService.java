package com.concursoacm.services;

import com.concursoacm.dtos.EquipoPuntuacionDTO;
import com.concursoacm.dtos.GanadoresDTO;
import com.concursoacm.dtos.PreguntaDTO;
import com.concursoacm.dtos.PaisPuntuacionDTO;
import com.concursoacm.dtos.RegionPuntuacionDTO;
import com.concursoacm.repositories.ResultadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ResultadoService {

    @Autowired
    private ResultadoRepository resultadoRepository;

    public List<EquipoPuntuacionDTO> obtenerPuntuacionesPorCategoria(String categoria) {
        List<Object[]> data = resultadoRepository.listarPuntuacionesEquiposPorCategoria(categoria);
        return data.stream()
            .map(obj -> new EquipoPuntuacionDTO((String)obj[0], ((Number)obj[1]).intValue()))
            .toList();
    }

    public GanadoresDTO obtenerGanadores() {
        List<EquipoPuntuacionDTO> competencia = obtenerPuntuacionesPorCategoria("Competencia");
        List<EquipoPuntuacionDTO> junior = obtenerPuntuacionesPorCategoria("Junior");
        EquipoPuntuacionDTO ganadorCompetencia = competencia.isEmpty() ? null : competencia.get(0);
        EquipoPuntuacionDTO ganadorJunior = junior.isEmpty() ? null : junior.get(0);
        return new GanadoresDTO(ganadorCompetencia, ganadorJunior);
    }

    public List<PreguntaDTO> obtenerTop3PreguntasJunior() {
        List<Object[]> data = resultadoRepository.obtenerTop3PreguntasJunior();
        return data.stream()
            .map(obj -> new PreguntaDTO(((Number)obj[0]).intValue(), (String)obj[1], ((Number)obj[2]).intValue()))
            .limit(3)
            .toList();
    }

    public PaisPuntuacionDTO obtenerPaisMayorPuntuacion() {
        List<Object[]> data = resultadoRepository.paisConMayorPuntuacion();
        if (data.isEmpty()) return null;
        Object[] row = data.get(0);
        return new PaisPuntuacionDTO((String)row[0], ((Number)row[1]).intValue());
    }

    public RegionPuntuacionDTO obtenerRegionMayorPuntuacion() {
        List<Object[]> data = resultadoRepository.regionConMayorPuntuacion();
        if (data.isEmpty()) return null;
        Object[] row = data.get(0);
        return new RegionPuntuacionDTO((String)row[0], ((Number)row[1]).intValue());
    }
}




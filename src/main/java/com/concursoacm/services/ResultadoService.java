package com.concursoacm.services;

import com.concursoacm.dtos.EquipoPuntuacionDTO;
import com.concursoacm.dtos.GanadoresPorCategoriaDTO;
import com.concursoacm.dtos.PaisConPuntuacionDTO;
import com.concursoacm.dtos.PreguntaDTO;
import com.concursoacm.dtos.RegionPuntuacionDTO;
import com.concursoacm.repositories.ResultadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * *Servicio que implementa la lógica de negocio para la gestión de resultados.
 */
@Service
public class ResultadoService {

    private final ResultadoRepository resultadoRepository;

    /**
     * *Constructor que inyecta el repositorio de resultados.
     *
     * @param resultadoRepository Repositorio para la gestión de resultados.
     */
    public ResultadoService(ResultadoRepository resultadoRepository) {
        this.resultadoRepository = resultadoRepository;
    }

    /**
     * *Obtiene las puntuaciones de los equipos por categoría.
     *
     * @param categoria Categoría del equipo (Competencia o Junior).
     * @return Lista de objetos EquipoPuntuacionDTO.
     */
    public List<EquipoPuntuacionDTO> obtenerPuntuacionesPorCategoria(String categoria) {
        return resultadoRepository.listarPuntuacionesEquiposPorCategoria(categoria).stream()
                .map(obj -> new EquipoPuntuacionDTO((String) obj[0], ((Number) obj[1]).intValue()))
                .collect(Collectors.toList());
    }

    /**
     * *Obtiene los ganadores de las categorías Competencia y Junior.
     *
     * @return Objeto GanadoresDTO con los ganadores de ambas categorías.
     */
    public GanadoresPorCategoriaDTO obtenerGanadores() {
        List<EquipoPuntuacionDTO> competencia = obtenerPuntuacionesPorCategoria("Competencia");
        List<EquipoPuntuacionDTO> junior = obtenerPuntuacionesPorCategoria("Junior");

        EquipoPuntuacionDTO ganadorCompetencia = competencia.isEmpty() ? null : competencia.get(0);
        EquipoPuntuacionDTO ganadorJunior = junior.isEmpty() ? null : junior.get(0);

        return new GanadoresPorCategoriaDTO(ganadorCompetencia, ganadorJunior);
    }

    /**
     * *Obtiene el top 3 de preguntas más puntuadas en la categoría Junior.
     *
     * @return Lista de objetos PreguntaDTO.
     */
    public List<PreguntaDTO> obtenerTop3PreguntasJunior() {
        return resultadoRepository.obtenerTop3PreguntasJunior().stream()
                .map(obj -> new PreguntaDTO(((Number) obj[0]).intValue(), (String) obj[1],
                        ((Number) obj[2]).intValue()))
                .limit(3)
                .collect(Collectors.toList());
    }

    /**
     * *Obtiene el país con mayor puntuación en el concurso.
     *
     * @return Objeto PaisPuntuacionDTO con el país y su puntuación total.
     */
    public PaisConPuntuacionDTO obtenerPaisMayorPuntuacion() {
        return resultadoRepository.paisConMayorPuntuacion().stream()
                .findFirst()
                .map(obj -> new PaisConPuntuacionDTO((String) obj[0], ((Number) obj[1]).intValue()))
                .orElse(null);
    }

    /**
     * *Obtiene la región con mayor puntuación en el concurso.
     *
     * @return Objeto RegionPuntuacionDTO con la región y su puntuación total.
     */
    public RegionPuntuacionDTO obtenerRegionMayorPuntuacion() {
        return resultadoRepository.regionConMayorPuntuacion().stream()
                .findFirst()
                .map(obj -> new RegionPuntuacionDTO((String) obj[0], ((Number) obj[1]).intValue()))
                .orElse(null);
    }
}

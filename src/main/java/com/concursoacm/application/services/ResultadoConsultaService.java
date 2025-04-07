package com.concursoacm.application.services;

import com.concursoacm.application.dtos.preguntas.PreguntaDTO;
import com.concursoacm.application.dtos.resultados.PuntuacionPorCategoriaDTO;
import com.concursoacm.application.dtos.resultados.PuntuacionPorEquipoDTO;
import com.concursoacm.application.dtos.resultados.PuntuacionPorPaisDTO;
import com.concursoacm.application.dtos.resultados.PuntuacionPorRegionDTO;
import com.concursoacm.infrastructure.repositories.ResultadoRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * *Servicio que implementa la lógica de negocio para la gestión de resultados.
 */
@Service
public class ResultadoConsultaService {

    private final ResultadoRepository resultadoRepository;

    /**
     * *Constructor que inyecta el repositorio de resultados.
     *
     * @param resultadoRepository Repositorio para la gestión de resultados.
     */
    public ResultadoConsultaService(ResultadoRepository resultadoRepository) {
        this.resultadoRepository = resultadoRepository;
    }

    /**
     * *Obtiene las puntuaciones de los equipos por categoría.
     *
     * @param categoria Categoría del equipo (Competencia o Junior).
     * @return Lista de objetos EquipoPuntuacionDTO.
     */
    public List<PuntuacionPorEquipoDTO> obtenerPuntuacionesPorCategoria(String categoria) {
        return resultadoRepository.listarPuntuacionesEquiposPorCategoria(categoria).stream()
                .map(obj -> new PuntuacionPorEquipoDTO((String) obj[0], ((Number) obj[1]).intValue()))
                .collect(Collectors.toList());
    }

    /**
     * *Obtiene los ganadores de las categorías Competencia y Junior.
     *
     * @return Objeto GanadoresDTO con los ganadores de ambas categorías.
     */
    public PuntuacionPorCategoriaDTO obtenerGanadores() {
        List<PuntuacionPorEquipoDTO> competencia = obtenerPuntuacionesPorCategoria("Competencia");
        List<PuntuacionPorEquipoDTO> junior = obtenerPuntuacionesPorCategoria("Junior");

        PuntuacionPorEquipoDTO ganadorCompetencia = competencia.isEmpty() ? null : competencia.get(0);
        PuntuacionPorEquipoDTO ganadorJunior = junior.isEmpty() ? null : junior.get(0);

        return new PuntuacionPorCategoriaDTO(ganadorCompetencia, ganadorJunior);
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
    public PuntuacionPorPaisDTO obtenerPaisMayorPuntuacion() {
        return resultadoRepository.paisConMayorPuntuacion().stream()
                .findFirst()
                .map(obj -> new PuntuacionPorPaisDTO((String) obj[0], ((Number) obj[1]).intValue()))
                .orElse(null);
    }

    /**
     * *Obtiene la región con mayor puntuación en el concurso.
     *
     * @return Objeto RegionPuntuacionDTO con la región y su puntuación total.
     */
    public PuntuacionPorRegionDTO obtenerRegionMayorPuntuacion() {
        return resultadoRepository.regionConMayorPuntuacion().stream()
                .findFirst()
                .map(obj -> new PuntuacionPorRegionDTO((String) obj[0], ((Number) obj[1]).intValue()))
                .orElse(null);
    }
}

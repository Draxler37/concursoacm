package com.concursoacm.controllers;

import com.concursoacm.dtos.EquipoPuntuacionDTO;
import com.concursoacm.dtos.GanadoresPorCategoriaDTO;
import com.concursoacm.dtos.PaisConPuntuacionDTO;
import com.concursoacm.dtos.PreguntaDTO;
import com.concursoacm.dtos.RegionPuntuacionDTO;
import com.concursoacm.services.CalculoResultadosService;
import com.concursoacm.services.ResultadoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resultados")
public class ResultadoController {

    @Autowired
    private ResultadoService resultadoService;

    @Autowired
    private CalculoResultadosService calculoResultadosService;

    @PostMapping("/calcular")
    public ResponseEntity<String> calcularResultados() {
        String mensaje = calculoResultadosService.calcularResultados();
        return ResponseEntity.ok(mensaje);
    }

    @GetMapping("/competencia")
    public ResponseEntity<List<EquipoPuntuacionDTO>> listarResultadosCompetencia() {
        List<EquipoPuntuacionDTO> lista = resultadoService.obtenerPuntuacionesPorCategoria("Competencia");
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/junior")
    public ResponseEntity<List<EquipoPuntuacionDTO>> listarResultadosJunior() {
        List<EquipoPuntuacionDTO> lista = resultadoService.obtenerPuntuacionesPorCategoria("Junior");
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/ganadores")
    public ResponseEntity<GanadoresPorCategoriaDTO> obtenerGanadoresPorCategoria() {
        GanadoresPorCategoriaDTO ganadores = resultadoService.obtenerGanadores();
        return ResponseEntity.ok(ganadores);
    }

    @GetMapping("/top-preguntas-junior")
    public ResponseEntity<List<PreguntaDTO>> obtenerTop3PreguntasJunior() {
        List<PreguntaDTO> top = resultadoService.obtenerTop3PreguntasJunior();
        return ResponseEntity.ok(top);
    }

    @GetMapping("/pais-mayor-puntuacion")
    public ResponseEntity<PaisConPuntuacionDTO> obtenerPaisMayorPuntuacion() {
        PaisConPuntuacionDTO dto = resultadoService.obtenerPaisMayorPuntuacion();
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/region-mayor-puntuacion")
    public ResponseEntity<RegionPuntuacionDTO> obtenerRegionMayorPuntuacion() {
        RegionPuntuacionDTO dto = resultadoService.obtenerRegionMayorPuntuacion();
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
}

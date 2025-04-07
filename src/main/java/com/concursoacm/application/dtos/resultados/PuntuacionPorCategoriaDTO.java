package com.concursoacm.application.dtos.resultados;

public class PuntuacionPorCategoriaDTO  {
    private PuntuacionPorEquipoDTO ganadorCompetencia;
    private PuntuacionPorEquipoDTO ganadorJunior;

    public PuntuacionPorCategoriaDTO (PuntuacionPorEquipoDTO ganadorCompetencia, PuntuacionPorEquipoDTO ganadorJunior) {
        this.ganadorCompetencia = ganadorCompetencia;
        this.ganadorJunior = ganadorJunior;
    }

    public PuntuacionPorEquipoDTO getGanadorCompetencia() {
        return ganadorCompetencia;
    }
    public void setGanadorCompetencia(PuntuacionPorEquipoDTO ganadorCompetencia) {
        this.ganadorCompetencia = ganadorCompetencia;
    }
    public PuntuacionPorEquipoDTO getGanadorJunior() {
        return ganadorJunior;
    }
    public void setGanadorJunior(PuntuacionPorEquipoDTO ganadorJunior) {
        this.ganadorJunior = ganadorJunior;
    }
}

package com.concursoacm.dtos;

public class GanadoresPorCategoriaDTO  {
    private EquipoPuntuacionDTO ganadorCompetencia;
    private EquipoPuntuacionDTO ganadorJunior;

    public GanadoresPorCategoriaDTO (EquipoPuntuacionDTO ganadorCompetencia, EquipoPuntuacionDTO ganadorJunior) {
        this.ganadorCompetencia = ganadorCompetencia;
        this.ganadorJunior = ganadorJunior;
    }

    public EquipoPuntuacionDTO getGanadorCompetencia() {
        return ganadorCompetencia;
    }
    public void setGanadorCompetencia(EquipoPuntuacionDTO ganadorCompetencia) {
        this.ganadorCompetencia = ganadorCompetencia;
    }
    public EquipoPuntuacionDTO getGanadorJunior() {
        return ganadorJunior;
    }
    public void setGanadorJunior(EquipoPuntuacionDTO ganadorJunior) {
        this.ganadorJunior = ganadorJunior;
    }
}

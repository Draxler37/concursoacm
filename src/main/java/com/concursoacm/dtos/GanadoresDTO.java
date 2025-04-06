package com.concursoacm.dtos;

public class GanadoresDTO {
    private EquipoPuntuacionDTO ganadorCompetencia;
    private EquipoPuntuacionDTO ganadorJunior;

    public GanadoresDTO(EquipoPuntuacionDTO ganadorCompetencia, EquipoPuntuacionDTO ganadorJunior) {
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

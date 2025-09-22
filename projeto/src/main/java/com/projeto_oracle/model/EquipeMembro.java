package com.projeto_oracle.model;

public class EquipeMembro {

    private int equipeId;
    private int usuarioId;

    public EquipeMembro() {
    }

    public EquipeMembro(int equipeId, int usuarioId) {
        this.equipeId = equipeId;
        this.usuarioId = usuarioId;
    }

    // --- Getters e Setters ---

    public int getEquipeId() {
        return equipeId;
    }

    public void setEquipeId(int equipeId) {
        this.equipeId = equipeId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
}
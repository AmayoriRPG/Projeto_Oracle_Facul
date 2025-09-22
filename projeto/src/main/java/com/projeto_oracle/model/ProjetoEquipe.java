package com.projeto_oracle.model;

public class ProjetoEquipe {

    private int projetoId;
    private int equipeId;

    public ProjetoEquipe() {
    }

    public ProjetoEquipe(int projetoId, int equipeId) {
        this.projetoId = projetoId;
        this.equipeId = equipeId;
    }

    // --- Getters e Setters ---

    public int getProjetoId() {
        return projetoId;
    }

    public void setProjetoId(int projetoId) {
        this.projetoId = projetoId;
    }

    public int getEquipeId() {
        return equipeId;
    }

    public void setEquipeId(int equipeId) {
        this.equipeId = equipeId;
    }
}
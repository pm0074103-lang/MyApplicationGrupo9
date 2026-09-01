package com.example.myapplication;

import java.util.Random;
public class Mistico extends Personaje {
    private int danoCompanerosRonda = 0;

    public Mistico(String nombre, int vida, int ataque, int defensa) {
        super(nombre, vida, ataque, defensa);
    }

    public void setDanoCompanerosRonda(int danoCompanerosRonda) {
        this.danoCompanerosRonda = danoCompanerosRonda;
    }

    @Override
    public void usarEstrategia(Equipo equipoAliado, Personaje.EstrategiaCallback callback) {
        int numeroSecreto = new Random().nextInt(6) + 1;
        if (callback != null) {
            callback.onRequiereInputMistico(numeroSecreto);
        }
    }

    public void aplicarEfectoEstrategia(boolean acerto) {
        if (acerto) {
            this.ataque += this.danoCompanerosRonda;
        }
    }
}




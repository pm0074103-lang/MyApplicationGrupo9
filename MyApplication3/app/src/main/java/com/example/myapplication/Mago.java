package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mago extends Personaje {

    public Mago(String nombre, int vida, int ataque, int defensa) {
        super(nombre, vida, ataque, defensa);
    }

    @Override
    public void usarEstrategia(Equipo equipoAliado, Personaje.EstrategiaCallback callback) {
        List<Personaje> aliadosVivos = new ArrayList<>();
        for (Personaje p : equipoAliado.getPersonajes()) {
            if (p.isEstaVivo()) {
                aliadosVivos.add(p);
            }
        }

        if (!aliadosVivos.isEmpty()) {
            Personaje objetivo = aliadosVivos.get(new Random().nextInt(aliadosVivos.size()));
            int curacion = (int) (this.vida * 0.25);
            objetivo.setVida(Math.min(objetivo.getVidaMax(), objetivo.getVida() + curacion));
        }
    }
}


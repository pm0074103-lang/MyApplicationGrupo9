package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class Equipo {
    private String nombre;
    private List<Personaje> personajes;

    public Equipo(String nombre) {
        this.nombre = nombre;
        this.personajes = new ArrayList<>();
    }

    public void agregarPersonaje(Personaje p) {
        if (personajes.size() < 4) {
            personajes.add(p);
        }
    }

    public boolean estaDerrotado() {
        for (Personaje p : personajes) {
            if (p.isEstaVivo()) return false;
        }
        return true;
    }

    public int vidaTotal() {
        int total = 0;
        for (Personaje p : personajes) {
            if (p.isEstaVivo()) {
                total += p.getVida();
            }
        }
        return total;
    }

    public String getNombre() { return nombre; }
    public List<Personaje> getPersonajes() { return personajes; }
}




package com.example.myapplication;
public class Guerrero extends Personaje {
    public Guerrero(String nombre, int vida, int ataque, int defensa) {
        super(nombre, vida, ataque, defensa);
    }

    @Override
    public void usarEstrategia(Equipo equipoAliado, Personaje.EstrategiaCallback callback) {
        this.ataque *= 2;
    }
}

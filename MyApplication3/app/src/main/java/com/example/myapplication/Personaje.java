package com.example.myapplication;

public abstract class Personaje{
    protected String nombre;
    protected int vida;
    protected int vidaMax;
    protected int ataque;
    protected int defensa;

    public interface EstrategiaCallback {
        void onRequiereInputMistico(int numeroSecreto);
    }

    public Personaje(String nombre, int vida, int ataque, int defensa) {
        this.nombre = nombre;
        this.vida = vida;
        this.vidaMax = vida;
        this.ataque = ataque;
        this.defensa = defensa;
    }

    public abstract void usarEstrategia(Equipo equipoAliado, EstrategiaCallback callback);

    public void recibirDano(int cantidad) {
        int danoReal = Math.max(1, cantidad - esteDefensaCalculada());
        this.vida = Math.max(0, this.vida - danoReal);
    }

    private int esteDefensaCalculada() {
        return this.defensa;
    }

    public int realizarAtaque(Personaje objetivo) {
        int danoCalculado = Math.max(1, this.ataque - objetivo.getDefensa());
        objetivo.recibirDano(this.ataque);
        return danoCalculado;
    }

    public boolean isEstaVivo() {
        return this.vida > 0;
    }

    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public void setVida(int vida) { this.vida = vida; }
    public int getVidaMax() { return vidaMax; }
    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }
}




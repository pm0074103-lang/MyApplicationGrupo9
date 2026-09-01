package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements Personaje.EstrategiaCallback {

    // Componentes del Layout (XML)
    private ScrollView panelSetup;
    private EditText etNombreA1, etVidaA1, etAtaqueA1, etDefensaA1;
    private EditText etNombreB1, etVidaB1, etAtaqueB1, etDefensaB1;
    private Button btnGuerreroA, btnItemA, btnMagoA;
    private Button btnGuerreroB, btnItemB, btnMagoB;
    private Button btnConfirmar;

    private LinearLayout panelCombate;
    private TextView tvTurnoActual, tvLogCombate;
    private ProgressBar pbVidaA, pbVidaB;
    private Button btnAtacar, btnEstrategia;

    // Control de selección de clases desde los botones
    private String tipoSeleccionadoA = "Guerrero";
    private String tipoSeleccionadoB = "Mago";

    // Variables de control de combate y modelo
    private Equipo equipoA, equipoB;
    private int rondaActual = 1;
    private int indiceTurnoA = 0;
    private int indiceTurnoB = 0;
    private boolean turnoEquipoA = true;
    private int danoAcumuladoRondaA = 0;
    private int danoAcumuladoRondaB = 0;

    // Referencia al Místico temporal para procesar la ventana emergente (AlertDialog)
    private Mistico misticoEnEspera = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupPresetButtons();

        btnConfirmar.setOnClickListener(v -> iniciarJuego());
        btnAtacar.setOnClickListener(v -> ejecutarTurnoAtaque());
        btnEstrategia.setOnClickListener(v -> ejecutarEstrategiaActual());
    }

    private void initViews() {
        // Enlaces con el XML
        panelSetup = findViewById(R.id.panelSetup);

        etNombreA1 = findViewById(R.id.etNombreA1);
        etVidaA1 = findViewById(R.id.etVidaA1);
        etAtaqueA1 = findViewById(R.id.etAtaqueA1);
        etDefensaA1 = findViewById(R.id.etDefensaA1);

        etNombreB1 = findViewById(R.id.etNombreB1);
        etVidaB1 = findViewById(R.id.etVidaB1);
        etAtaqueB1 = findViewById(R.id.etAtaqueB1);
        etDefensaB1 = findViewById(R.id.etDefensaB1);

        btnGuerreroA = findViewById(R.id.btnGuerreroA);
        btnItemA = findViewById(R.id.btnItemA);
        btnMagoA = findViewById(R.id.btnMagoA);

        btnGuerreroB = findViewById(R.id.btnGuerreroB);
        btnItemB = findViewById(R.id.btnItemB);
        btnMagoB = findViewById(R.id.btnMagoB);

        btnConfirmar = findViewById(R.id.btnConfirmar);

        // Panel de Combate
        panelCombate = findViewById(R.id.panelCombate);
        tvTurnoActual = findViewById(R.id.tvEstadoRonda); // ID del XML: tvEstadoRonda
        tvLogCombate = findViewById(R.id.tvLog);          // ID del XML: tvLog
        tvLogCombate.setMovementMethod(new ScrollingMovementMethod());

        pbVidaA = findViewById(R.id.pbVidaA);
        pbVidaB = findViewById(R.id.pbVidaB);

        btnAtacar = findViewById(R.id.btnAtacar);
        btnEstrategia = findViewById(R.id.btnEstrategia);
    }

    private void setStatsA(String nombre, String hp, String atk, String def) {
        if (etNombreA1 != null) etNombreA1.setText(nombre);
        if (etVidaA1 != null) etVidaA1.setText(hp);
        if (etAtaqueA1 != null) etAtaqueA1.setText(atk);
        if (etDefensaA1 != null) etDefensaA1.setText(def);
    }

    private void setStatsB(String nombre, String hp, String atk, String def) {
        if (etNombreB1 != null) etNombreB1.setText(nombre);
        if (etVidaB1 != null) etVidaB1.setText(hp);
        if (etAtaqueB1 != null) etAtaqueB1.setText(atk);
        if (etDefensaB1 != null) etDefensaB1.setText(def);
    }

    private void setupPresetButtons() {
        if (btnGuerreroA != null) btnGuerreroA.setOnClickListener(v -> {
            tipoSeleccionadoA = "Guerrero";
            setStatsA("Guerrero A", "120", "20", "10");
        });
        if (btnItemA != null) btnItemA.setOnClickListener(v -> {
            tipoSeleccionadoA = "Mistico";
            setStatsA("Mistico A", "100", "15", "8");
        });
        if (btnMagoA != null) btnMagoA.setOnClickListener(v -> {
            tipoSeleccionadoA = "Mago";
            setStatsA("Mago A", "80", "35", "5");
        });

        if (btnGuerreroB != null) btnGuerreroB.setOnClickListener(v -> {
            tipoSeleccionadoB = "Guerrero";
            setStatsB("Guerrero B", "120", "20", "10");
        });
        if (btnItemB != null) btnItemB.setOnClickListener(v -> {
            tipoSeleccionadoB = "Mistico";
            setStatsB("Mistico B", "100", "15", "8");
        });
        if (btnMagoB != null) btnMagoB.setOnClickListener(v -> {
            tipoSeleccionadoB = "Mago";
            setStatsB("Mago B", "80", "35", "5");
        });
    }

    private void iniciarJuego() {
        equipoA = new Equipo("Equipo A");
        equipoB = new Equipo("Equipo B");

        // Construcción Equipo A (2 Guerreros, 1 Mago, 1 Místico)
        equipoA.agregarPersonaje(crearPersonaje(tipoSeleccionadoA, etNombreA1.getText().toString(), etVidaA1, etAtaqueA1, etDefensaA1));
        equipoA.agregarPersonaje(new Guerrero("Guerrero A2", 120, 20, 10));
        equipoA.agregarPersonaje(new Mago("Mago A", 80, 25, 5));
        equipoA.agregarPersonaje(new Mistico("Mistico A", 100, 15, 8));

        // Construcción Equipo B (2 Guerreros, 1 Mago, 1 Místico)[cite: 1]
        equipoB.agregarPersonaje(crearPersonaje(tipoSeleccionadoB, etNombreB1.getText().toString(), etVidaB1, etAtaqueB1, etDefensaB1));
        equipoB.agregarPersonaje(new Guerrero("Guerrero B2", 120, 20, 10));
        equipoB.agregarPersonaje(new Mago("Mago B", 80, 25, 5));
        equipoB.agregarPersonaje(new Mistico("Mistico B", 100, 15, 8));

        // Configuración de vida inicial en ProgressBars
        pbVidaA.setMax(equipoA.vidaTotal());
        pbVidaA.setProgress(equipoA.vidaTotal());

        pbVidaB.setMax(equipoB.vidaTotal());
        pbVidaB.setProgress(equipoB.vidaTotal());

        // Ocultar configuración y mostrar interfaz de combate
        panelSetup.setVisibility(View.GONE);
        panelCombate.setVisibility(View.VISIBLE);

        tvLogCombate.setText("> BATTLE STARTED!\n> Equipo A vs Equipo B\n");
        actualizarInterfaz();
    }

    private Personaje crearPersonaje(String tipo, String nombre, EditText etV, EditText etA, EditText etD) {
        int v = Integer.parseInt(etV.getText().toString());
        int a = Integer.parseInt(etA.getText().toString());
        int d = Integer.parseInt(etD.getText().toString());

        switch (tipo) {
            case "Mago":
                return new Mago(nombre, v, a, d);
            case "Mistico":
                return new Mistico(nombre, v, a, d);
            default:
                return new Guerrero(nombre, v, a, d);
        }
    }

    private Personaje getPersonajeActivo(Equipo equipo, int indice) {
        if (equipo.getPersonajes().isEmpty()) return null;
        for (int i = 0; i < equipo.getPersonajes().size(); i++) {
            int pos = (indice + i) % equipo.getPersonajes().size();
            Personaje p = equipo.getPersonajes().get(pos);
            if (p.isEstaVivo()) {
                return p;
            }
        }
        return null;
    }

    private void ejecutarEstrategiaActual() {
        Equipo equipoAtacante = turnoEquipoA ? equipoA : equipoB;
        int indice = turnoEquipoA ? indiceTurnoA : indiceTurnoB;
        Personaje atacante = getPersonajeActivo(equipoAtacante, indice);

        if (atacante != null) {
            if (atacante instanceof Mistico) {
                misticoEnEspera = (Mistico) atacante;
                int danoCompaneros = turnoEquipoA ? danoAcumuladoRondaA : danoAcumuladoRondaB;
                misticoEnEspera.setDanoCompanerosRonda(danoCompaneros);
            }

            // Ejecuta el método usarEstrategia de la subclase
            atacante.usarEstrategia(equipoAtacante, this);

            if (!(atacante instanceof Mistico)) {
                tvLogCombate.append("> " + atacante.getNombre() + " usó su estrategia.\n");
                actualizarInterfaz();
            }
        }
    }

    // Callback llamado únicamente cuando la estrategia la usa la clase Mistico
    @Override
    public void onRequiereInputMistico(int numeroSecreto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("🔮 Predicción del Místico");
        builder.setMessage("Adivina un número entre 1 y 6:");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Adivinar", (dialog, which) -> {
            try {
                int numIngresado = Integer.parseInt(input.getText().toString());
                boolean acerto = (numIngresado == numeroSecreto);

                if (misticoEnEspera != null) {
                    misticoEnEspera.aplicarEfectoEstrategia(acerto);
                    if (acerto) {
                        tvLogCombate.append("> ¡" + misticoEnEspera.getNombre() + " acertó el número (" + numeroSecreto + ")! Su ataque aumentó.\n");
                    } else {
                        tvLogCombate.append("> " + misticoEnEspera.getNombre() + " falló (Era " + numeroSecreto + "). Ataque normal.\n");
                    }
                }
            } catch (Exception e) {
                if (misticoEnEspera != null) {
                    misticoEnEspera.aplicarEfectoEstrategia(false);
                }
                tvLogCombate.append("> Entrada inválida. Estrategia fallida.\n");
            }
            actualizarInterfaz();
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void ejecutarTurnoAtaque() {
        Equipo atacanteEq = turnoEquipoA ? equipoA : equipoB;
        Equipo defensorEq = turnoEquipoA ? equipoB : equipoA;

        Personaje atacante = getPersonajeActivo(atacanteEq, turnoEquipoA ? indiceTurnoA : indiceTurnoB);
        Personaje defensor = getPersonajeActivo(defensorEq, turnoEquipoA ? indiceTurnoB : indiceTurnoA);

        if (atacante != null && defensor != null) {
            int danoReal = atacante.realizarAtaque(defensor);
            tvLogCombate.append("> " + atacante.getNombre() + " infligió " + danoReal + " de daño a " + defensor.getNombre() + " (" + defensor.getVida() + " HP rest.)\n");

            if (turnoEquipoA) {
                danoAcumuladoRondaA += danoReal;
                indiceTurnoA++;
            } else {
                danoAcumuladoRondaB += danoReal;
                indiceTurnoB++;
            }
        }

        // Alternancia de turnos entre equipos
        turnoEquipoA = !turnoEquipoA;

        // Si volvió al Turno A, se completa una ronda
        if (turnoEquipoA) {
            rondaActual++;
            danoAcumuladoRondaA = 0;
            danoAcumuladoRondaB = 0;
        }

        actualizarInterfaz();
        verificarFinDelJuego();
    }

    private void actualizarInterfaz() {
        tvTurnoActual.setText("ROUND " + rondaActual + " - Turno: " + (turnoEquipoA ? "Equipo A" : "Equipo B"));

        pbVidaA.setProgress(Math.max(0, equipoA.vidaTotal()));
        pbVidaB.setProgress(Math.max(0, equipoB.vidaTotal()));
    }

    private void verificarFinDelJuego() {
        boolean finPorDerrota = equipoA.estaDerrotado() || equipoB.estaDerrotado();
        boolean finPorRondas = rondaActual > 15; // Regla de 15 rondas[cite: 1]

        if (finPorDerrota || finPorRondas) {
            btnAtacar.setEnabled(false);
            btnEstrategia.setEnabled(false);

            String resultado = "\n=== ¡FIN DEL JUEGO! ===\n";
            if (equipoA.vidaTotal() > equipoB.vidaTotal()) {
                resultado += "🏆 WINNER: " + equipoA.getNombre();
            } else if (equipoB.vidaTotal() > equipoA.vidaTotal()) {
                resultado += "🏆 WINNER: " + equipoB.getNombre();
            } else {
                resultado += "🤝 EMPATE TÉCNICO";
            }

            tvLogCombate.append(resultado + "\n");
            Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();
        }
    }
}



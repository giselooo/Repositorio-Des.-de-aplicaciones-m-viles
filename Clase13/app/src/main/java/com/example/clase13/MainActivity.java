package com.example.clase13;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

/**
 * Actividad principal de la simulación de tráfico aéreo.
 *
 * Responsabilidades:
 * ─ Inicializar N aviones (aleatorio 100-1000) en un grid aleatorio (20-60).
 * ─ Gestionar el historial con Stack<List<Avion>> + Deep Copies.
 * ─ Detectar colisiones 3D en O(n) con HashMap<Long, List<Integer>>.
 * ─ Actualizar la UI (paso, colisiones, botones).
 * ─ Botón Reiniciar: regenera todo con valores aleatorios.
 */
public class MainActivity extends AppCompatActivity {

    // ── Rangos para el botón Reiniciar ─────────────────────────────────────
    private static final int AVIONES_MIN  = 100;
    private static final int AVIONES_MAX  = 1000;
    private static final int GRID_MIN     = 20;
    private static final int GRID_MAX     = 60;
    private static final int MAX_HISTORIAL = 300; // Entradas máximas en la pila

    // ── Rama 3D: altitud ─────────────────────────────────────────────────
    // En modo 2D puro todos los aviones vuelan en z=0.
    // Cambiar MAX_ALTITUD > 0 activa la dimensión de altitud.
    private static final int MAX_ALTITUD = 5;

    // ── Vistas ─────────────────────────────────────────────────────────────
    private TableroView tableroView;
    private TextView    tvPasoActual;
    private TextView    tvColisiones;
    private TextView    tvInfo;      // Muestra gridSize y cantidad de aviones
    private Button      btnAdelante;
    private Button      btnAtras;
    private Button      btnReiniciar;

    // ── Estado de la simulación ────────────────────────────────────────────
    private List<Avion> aviones;
    private int gridSize;
    private int numAviones;
    private int pasoActual     = 0;
    private int totalColisiones = 0;

    /**
     * Pila del historial (LIFO).
     * Cada entrada es una copia profunda completa de la lista de aviones.
     * push() al avanzar, pop() al retroceder.
     */
    private final Stack<List<Avion>> historial = new Stack<>();

    private final Random random = new Random();

    // ── Ciclo de vida ──────────────────────────────────────────────────────

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Vincular vistas
        tableroView  = findViewById(R.id.tableroView);
        tvPasoActual = findViewById(R.id.tvPasoActual);
        tvColisiones = findViewById(R.id.tvColisiones);
        tvInfo       = findViewById(R.id.tvInfo);
        btnAdelante  = findViewById(R.id.btnAdelante);
        btnAtras     = findViewById(R.id.btnAtras);
        btnReiniciar = findViewById(R.id.btnReiniciar);

        // Primera sesión con valores aleatorios
        iniciarSesionAleatoria();

        // Botones
        btnAdelante .setOnClickListener(v -> avanzarPaso());
        btnAtras    .setOnClickListener(v -> retrocederPaso());
        btnReiniciar.setOnClickListener(v -> reiniciar());
    }

    // ── Inicialización / Reinicio ──────────────────────────────────────────

    /**
     * Genera una nueva sesión con gridSize y numAviones aleatorios.
     * Limpia el historial y los contadores.
     */
    private void iniciarSesionAleatoria() {
        gridSize   = GRID_MIN + random.nextInt(GRID_MAX - GRID_MIN + 1);
        numAviones = AVIONES_MIN + random.nextInt(AVIONES_MAX - AVIONES_MIN + 1);

        historial.clear();
        pasoActual      = 0;
        totalColisiones = 0;

        tableroView.setGridSize(gridSize);
        generarAviones();
        actualizarUI();
    }

    /**
     * Crea numAviones aviones con posiciones y direcciones aleatorias.
     * En modo 2D puro z=0; las direcciones 2D son NORTE/SUR/ESTE/OESTE.
     */
    private void generarAviones() {
        aviones = new ArrayList<>(numAviones);
        // Agregamos ASCENDER y DESCENDER al abanico de opciones
        int[] dirs3D = {Avion.NORTE, Avion.SUR, Avion.ESTE, Avion.OESTE, Avion.ASCENDER, Avion.DESCENDER};

        for (int i = 0; i < numAviones; i++) {
            int x   = random.nextInt(gridSize);
            int y   = random.nextInt(gridSize);
            int z   = random.nextInt(MAX_ALTITUD + 1); // Ahora pueden nacer a distintas alturas
            int dir = dirs3D[random.nextInt(dirs3D.length)];
            aviones.add(new Avion(i, x, y, z, dir));
        }
    }

    private void reiniciar() {
        tableroView.resetearVista();
        iniciarSesionAleatoria();
        Toast.makeText(this,
                "Reiniciado: " + numAviones + " aviones · Grid " + gridSize + "×" + gridSize,
                Toast.LENGTH_SHORT).show();
    }

    // ── Lógica de pasos ────────────────────────────────────────────────────

    /**
     * Avanza un paso:
     * 1. Guarda copia profunda en la pila.
     * 2. Mueve todos los aviones no colisionados.
     * 3. Detecta colisiones 3D.
     * 4. Actualiza UI.
     */
    private void avanzarPaso() {
        guardarEnHistorial();

        // --- ESTA ES LA LÍNEA MÁGICA ---
        // Eliminamos de la lista los aviones que ya chocaron en el paso anterior
        aviones.removeIf(avion -> avion.isColisionado());
        // -------------------------------

        for (Avion a : aviones) {
            a.mover(gridSize, MAX_ALTITUD);
        }

        detectarColisiones();

        pasoActual++;
        actualizarUI();
    }

    /**
     * Retrocede un paso restaurando el estado desde la pila.
     */
    private void retrocederPaso() {
        if (historial.isEmpty()) {
            Toast.makeText(this, "Sin pasos anteriores", Toast.LENGTH_SHORT).show();
            return;
        }

        aviones = historial.pop();
        pasoActual--;
        recalcularTotalColisiones();
        actualizarUI();
    }

    // ── Historial con Deep Copy ────────────────────────────────────────────

    /**
     * Clona cada avión y empuja la copia en la pila.
     *
     * Deep Copy garantiza que modificar aviones[] más adelante
     * NO altere el estado guardado en el historial.
     *
     * Si se supera MAX_HISTORIAL se descarta la entrada más antigua (fondo).
     */
    private void guardarEnHistorial() {
        List<Avion> copia = new ArrayList<>(aviones.size());
        for (Avion a : aviones) {
            copia.add(a.clone()); // clone() individual → Deep Copy
        }
        historial.push(copia);

        // Gestión de memoria: descartar el estado más antiguo
        if (historial.size() > MAX_HISTORIAL) {
            historial.remove(0); // O(n) pero ocurre raramente; aceptable
        }
    }

    // ── Detección de colisiones 3D ─────────────────────────────────────────

    /**
     * Detecta colisiones en O(n) usando HashMap<Long, List<Integer>>.
     *
     * La clave Long codifica (x, y, z) → posicionKey3D().
     * Se marcan colisionados SOLO los aviones que NO lo estaban aún.
     */
    private void detectarColisiones() {
        Map<Long, List<Integer>> porPosicion = new HashMap<>();

        for (int i = 0; i < aviones.size(); i++) {
            Avion a = aviones.get(i);
            if (a.isColisionado()) continue; // Ya fuera de juego

            long key = a.posicionKey3D(gridSize);
            List<Integer> grupo = porPosicion.get(key);
            if (grupo == null) {
                grupo = new ArrayList<>(2);
                porPosicion.put(key, grupo);
            }
            grupo.add(i);
        }

        for (List<Integer> grupo : porPosicion.values()) {
            if (grupo.size() > 1) {
                for (int idx : grupo) {
                    aviones.get(idx).setColisionado(true);
                    totalColisiones++;
                }
            }
        }
    }

    /**
     * Recalcula totalColisiones contando los aviones marcados en el estado
     * actual. Se usa tras restaurar un estado desde el historial.
     */
    private void recalcularTotalColisiones() {
        // Si quieres que el contador muestre cuántos han chocado en total desde el inicio:
        // Es la diferencia entre los que empezaron y los que hay ahora,
        // pero sumando los que están marcados como "colisionado" en la lista actual.
        int colisionadosActuales = 0;
        for (Avion a : aviones) {
            if (a.isColisionado()) colisionadosActuales++;
        }

        // Total = (Aviones que ya no existen) + (Aviones que están en rojo ahorita)
        totalColisiones = (numAviones - aviones.size()) + colisionadosActuales;
    }

    // ── Actualización de UI ────────────────────────────────────────────────

    private void actualizarUI() {
        tvPasoActual.setText("Paso: " + pasoActual);
        tvColisiones.setText("Colisiones: " + totalColisiones);

        // Mostramos cuántos quedan vivos del total original
        int avionesRestantes = aviones.size();
        tvInfo.setText("Grid " + gridSize + "×" + gridSize
                + "  ·  Vivos: " + avionesRestantes + "/" + numAviones
                + "  ·  Historial: " + historial.size());

        boolean hayHistorial = !historial.isEmpty();
        btnAtras.setEnabled(hayHistorial);
        btnAtras.setAlpha(hayHistorial ? 1f : 0.38f);

        tableroView.setAviones(aviones);
    }
}
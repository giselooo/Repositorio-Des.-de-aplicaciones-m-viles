package com.example.clase13;

public class Avion implements Cloneable {

    // ── Constantes de dirección ────────────────────────────────────────────
    public static final int NORTE     = 0;
    public static final int SUR       = 1;
    public static final int ESTE      = 2;
    public static final int OESTE     = 3;
    public static final int ASCENDER  = 4; // Rama 3D: sube un nivel de altitud
    public static final int DESCENDER = 5; // Rama 3D: baja un nivel de altitud

    // ── Campos del modelo ──────────────────────────────────────────────────
    private final int id;
    private int x;            // Columna en el grid
    private int y;            // Fila en el grid
    private int z;            // Altitud (0 = suelo; Rama 3D)
    private int direccion;
    private boolean colisionado;

    // ── Constructor ────────────────────────────────────────────────────────

    public Avion(int id, int x, int y, int z, int direccion) {
        this.id          = id;
        this.x           = x;
        this.y           = y;
        this.z           = z;
        this.direccion   = direccion;
        this.colisionado = false;
    }

    // ── Lógica de movimiento ───────────────────────────────────────────────

    /**
     * Mueve el avión un paso según su dirección.
     *
     * - X e Y usan wrap-around (el avión reaparece por el lado opuesto).
     * - Z se limita al rango [0, maxAltitud] sin wrap (Rama 3D).
     *
     * @param gridSize   Tamaño N del grid
     * @param maxAltitud Altitud máxima (usa 0 para modo 2D puro)
     */
    public void mover(int gridSize, int maxAltitud) {
        if (colisionado) return;

        switch (direccion) {
            case NORTE:     y = (y - 1 + gridSize) % gridSize; break;
            case SUR:       y = (y + 1) % gridSize;            break;
            case ESTE:      x = (x + 1) % gridSize;            break;
            case OESTE:     x = (x - 1 + gridSize) % gridSize; break;
            case ASCENDER:  z = Math.min(z + 1, maxAltitud);   break;
            case DESCENDER: z = Math.max(z - 1, 0);            break;
        }
    }

    /**
     * Genera una clave única de posición 3D para usar en el HashMap de
     * detección de colisiones. Empaqueta (x, y, z) en un solo long.
     *
     * Capacidad: grids hasta 65535×65535 con hasta 255 niveles de altitud.
     */
    public long posicionKey3D(int gridSize) {
        return ((long) z * gridSize * gridSize) + ((long) y * gridSize) + x;
    }

    // ── Deep Copy ─────────────────────────────────────────────────────────

    /**
     * Copia profunda del avión.
     * Todos los campos son primitivos → super.clone() es correcto y suficiente.
     */
    @Override
    public Avion clone() {
        try {
            return (Avion) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone no soportado en Avion", e);
        }
    }

    // ── Getters ────────────────────────────────────────────────────────────

    public int     getId()          { return id; }
    public int     getX()           { return x; }
    public int     getY()           { return y; }
    public int     getZ()           { return z; }
    public int     getDireccion()   { return direccion; }
    public boolean isColisionado()  { return colisionado; }

    // ── Setters ────────────────────────────────────────────────────────────

    public void setColisionado(boolean v) { this.colisionado = v; }
}
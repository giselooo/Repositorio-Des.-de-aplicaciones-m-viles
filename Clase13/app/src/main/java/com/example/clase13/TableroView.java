package com.example.clase13;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.List;

/**
 * Vista personalizada que renderiza el grid N×N y los aviones usando Canvas.
 *
 * Características:
 * ─ Panning (arrastre): desplaza el canvas con android.graphics.Matrix.
 * ─ Zoom: pellizco (pinch-to-zoom) con ScaleGestureDetector.
 * ─ Objetos Paint/Path reutilizados: NUNCA se crean dentro de onDraw.
 * ─ Aviones como triángulos orientados según su dirección de vuelo.
 * ─ Color azul (normal) / rojo (colisionado).
 */
public class TableroView extends View {

    // ── Paleta de colores ──────────────────────────────────────────────────
    private static final int COLOR_FONDO   = Color.parseColor("#0D1117");
    private static final int COLOR_LINEAS  = Color.parseColor("#21262D");
    private static final int COLOR_NORMAL  = Color.parseColor("#58A6FF"); // Azul GitHub
    private static final int COLOR_CRASH   = Color.parseColor("#F85149"); // Rojo GitHub
    private static final int COLOR_SOMBRA  = Color.parseColor("#1F6FEB"); // Halo avión normal

    // ── Zoom ───────────────────────────────────────────────────────────────
    private static final float ZOOM_MIN = 0.3f;
    private static final float ZOOM_MAX = 8.0f;

    // ── Estado ─────────────────────────────────────────────────────────────
    private List<Avion> aviones;
    private int gridSize = 50;

    // ── Transformación del canvas ──────────────────────────────────────────
    // La Matrix acumula traslación (panning) y escala (zoom).
    private final Matrix matrix        = new Matrix();
    private final Matrix matrixInverse = new Matrix(); // Para convertir touch → coords mundo

    private float translateX = 0f;
    private float translateY = 0f;
    private float scaleFactor = 1.0f;

    // ── Dimensiones de celda (en coordenadas de mundo, antes de Matrix) ────
    private float celdaW;
    private float celdaH;

    // ── Objetos de dibujo (creados una sola vez) ───────────────────────────
    private final Paint paintFondo  = new Paint();
    private final Paint paintLineas = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintNormal = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintCrash  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path  pathAvion   = new Path();

    // ── Detectores de gestos ───────────────────────────────────────────────
    private GestureDetector      gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;

    // ══ Constructores ══════════════════════════════════════════════════════

    public TableroView(Context context) {
        super(context); init(context);
    }
    public TableroView(Context context, AttributeSet attrs) {
        super(context, attrs); init(context);
    }
    public TableroView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr); init(context);
    }

    // ── Inicialización ─────────────────────────────────────────────────────

    private void init(Context ctx) {
        // Fondos y líneas
        paintFondo.setColor(COLOR_FONDO);
        paintFondo.setStyle(Paint.Style.FILL);

        paintLineas.setColor(COLOR_LINEAS);
        paintLineas.setStyle(Paint.Style.STROKE);
        paintLineas.setStrokeWidth(0.8f);

        // Aviones normales
        paintNormal.setColor(COLOR_NORMAL);
        paintNormal.setStyle(Paint.Style.FILL);

        // Aviones colisionados
        paintCrash.setColor(COLOR_CRASH);
        paintCrash.setStyle(Paint.Style.FILL);

        // ── Gesture: panning ──────────────────────────────────────────────
        gestureDetector = new GestureDetector(ctx,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                            float distanceX, float distanceY) {
                        translateX -= distanceX;
                        translateY -= distanceY;
                        actualizarMatrix();
                        return true;
                    }
                });

        // ── Gesture: zoom (pinch) ─────────────────────────────────────────
        scaleGestureDetector = new ScaleGestureDetector(ctx,
                new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                    @Override
                    public boolean onScale(ScaleGestureDetector detector) {
                        float factor = detector.getScaleFactor();
                        float newScale = scaleFactor * factor;
                        scaleFactor = Math.max(ZOOM_MIN, Math.min(ZOOM_MAX, newScale));
                        actualizarMatrix();
                        return true;
                    }
                });
    }

    // ── Tamaño ─────────────────────────────────────────────────────────────

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        recalcularCeldas();
        // Centrar el grid en la vista al inicio
        centrarGrid();
    }

    private void recalcularCeldas() {
        if (getWidth() > 0 && getHeight() > 0 && gridSize > 0) {
            // Las celdas se calculan en "coordenadas de mundo" (sin zoom).
            // El zoom lo aplica la Matrix al Canvas.
            float tamanoMundo = Math.min(getWidth(), getHeight());
            celdaW = tamanoMundo / gridSize;
            celdaH = tamanoMundo / gridSize;
        }
    }

    private void centrarGrid() {
        // Offset inicial para que el grid quede centrado en pantalla
        float gridPxW = celdaW * gridSize;
        float gridPxH = celdaH * gridSize;
        translateX = (getWidth()  - gridPxW) / 2f;
        translateY = (getHeight() - gridPxH) / 2f;
        actualizarMatrix();
    }

    // ── Matrix de transformación ───────────────────────────────────────────

    /**
     * Reconstruye la Matrix a partir de translateX/Y y scaleFactor.
     * Orden: primero trasladar, luego escalar desde el origen.
     * (Para zoom centrado en pantalla se usaría post-scale con pivote.)
     */
    private void actualizarMatrix() {
        float anchoMundo = celdaW * gridSize;
        float altoMundo = celdaH * gridSize;

        // El truco del "infinito": usamos el módulo para que el
        // desplazamiento siempre esté dentro del rango de un solo mapa.
        if (anchoMundo > 0 && altoMundo > 0) {
            translateX %= (anchoMundo * scaleFactor);
            translateY %= (altoMundo * scaleFactor);
        }

        matrix.reset();
        matrix.postTranslate(translateX, translateY);
        matrix.postScale(scaleFactor, scaleFactor, getWidth() / 2f, getHeight() / 2f);
        matrix.invert(matrixInverse);
        invalidate();
    }

    // ── Manejo de touch ────────────────────────────────────────────────────

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Pasar el evento a ambos detectores; el de escala tiene prioridad
        scaleGestureDetector.onTouchEvent(event);
        if (!scaleGestureDetector.isInProgress()) {
            gestureDetector.onTouchEvent(event);
        }
        return true; // Consumir el evento
    }

    // ── Dibujo ─────────────────────────────────────────────────────────────

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintFondo);

        canvas.save();
        canvas.concat(matrix);

        // Calculamos el tamaño real del mundo en píxeles
        float anchoMundo = celdaW * gridSize;
        float altoMundo = celdaH * gridSize;

        // Dibujamos el grid y los aviones repetidos en un área de 3x3
        // Esto llena el espacio vacío alrededor del centro
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                canvas.save();
                canvas.translate(i * anchoMundo, j * altoMundo);

                dibujarGrid(canvas);
                if (aviones != null) dibujarAviones(canvas);

                canvas.restore();
            }
        }

        canvas.restore();
    }

    private void dibujarGrid(Canvas canvas) {
        float anchoMundo = celdaW * gridSize;
        float altoMundo = celdaH * gridSize;

        // Calculamos cuántos "mundos" caben o en qué posición estamos
        // Esto hace que las líneas se dibujen siempre frente a la cámara
        float inicioX = (float) Math.floor(-translateX / scaleFactor / celdaW) * celdaW - anchoMundo;
        float finX = inicioX + (getWidth() / scaleFactor) + (anchoMundo * 2);

        float inicioY = (float) Math.floor(-translateY / scaleFactor / celdaH) * celdaH - altoMundo;
        float finY = inicioY + (getHeight() / scaleFactor) + (altoMundo * 2);

        // Dibujar líneas verticales
        for (float x = inicioX; x <= finX; x += celdaW) {
            canvas.drawLine(x, inicioY, x, finY, paintLineas);
        }
        // Dibujar líneas horizontales
        for (float y = inicioY; y <= finY; y += celdaH) {
            canvas.drawLine(inicioX, y, finX, y, paintLineas);
        }
    }

    private void dibujarAviones(Canvas canvas) {
        for (Avion avion : aviones) {
            dibujarAvion(canvas, avion);
        }
    }

    /**
     * Dibuja un avión como triángulo orientado según su dirección.
     * Reutiliza pathAvion para no crear objetos en cada iteración.
     */
    private void dibujarAvion(Canvas canvas, Avion avion) {
        float cx = avion.getX() * celdaW + celdaW * 0.5f;
        float cy = avion.getY() * celdaH + celdaH * 0.5f;

        // ── Lógica 3D: Escala según la altitud ──────────────────────────────
        // Entre más alto (Z), más grande se ve el avión (simula cercanía)
        float factorEscala = 1.0f + (avion.getZ() * 0.25f);
        float r = (Math.min(celdaW, celdaH) * 0.36f) * factorEscala;

        // ── Lógica 3D: Opacidad según la altitud ────────────────────────────
        if (avion.isColisionado()) {
            paintCrash.setAlpha(255); // Rojo brillante en choque
        } else {
            // Los aviones más bajos (Z=0) se ven más tenues
            int alpha = 130 + (avion.getZ() * 25);
            paintNormal.setAlpha(Math.min(alpha, 255));

            // Si el avión está subiendo/bajando (dirección 4 o 5), cambiamos a un azul claro
            if (avion.getDireccion() > 3) {
                paintNormal.setColor(Color.parseColor("#A5D6FF"));
            } else {
                paintNormal.setColor(COLOR_NORMAL); // El azul original
            }
        }

        pathAvion.reset();

        switch (avion.getDireccion()) {
            case Avion.NORTE:
                pathAvion.moveTo(cx,             cy - r);
                pathAvion.lineTo(cx - r * 0.65f, cy + r * 0.6f);
                pathAvion.lineTo(cx + r * 0.65f, cy + r * 0.6f);
                break;
            case Avion.SUR:
                pathAvion.moveTo(cx,             cy + r);
                pathAvion.lineTo(cx - r * 0.65f, cy - r * 0.6f);
                pathAvion.lineTo(cx + r * 0.65f, cy - r * 0.6f);
                break;
            case Avion.ESTE:
                pathAvion.moveTo(cx + r,         cy);
                pathAvion.lineTo(cx - r * 0.6f,  cy - r * 0.65f);
                pathAvion.lineTo(cx - r * 0.6f,  cy + r * 0.65f);
                break;
            case Avion.OESTE:
                pathAvion.moveTo(cx - r,         cy);
                pathAvion.lineTo(cx + r * 0.6f,  cy - r * 0.65f);
                pathAvion.lineTo(cx + r * 0.6f,  cy + r * 0.65f);
                break;
            default:
                // ASCENDER / DESCENDER: Triángulo mirando al Norte por defecto
                pathAvion.moveTo(cx,             cy - r);
                pathAvion.lineTo(cx - r * 0.65f, cy + r * 0.6f);
                pathAvion.lineTo(cx + r * 0.65f, cy + r * 0.6f);
                break;
        }
        pathAvion.close();

        canvas.drawPath(pathAvion, avion.isColisionado() ? paintCrash : paintNormal);
    }

    // ── API pública ────────────────────────────────────────────────────────

    /** Actualiza la lista de aviones y solicita redibujo. */
    public void setAviones(List<Avion> aviones) {
        this.aviones = aviones;
        invalidate();
    }

    /** Cambia el tamaño del grid y recentra la vista. */
    public void setGridSize(int size) {
        this.gridSize = size;
        if (getWidth() > 0 && getHeight() > 0) {
            recalcularCeldas();
            centrarGrid();
        }
        invalidate();
    }

    /** Resetea la transformación al estado inicial (centrado, sin zoom). */
    public void resetearVista() {
        scaleFactor = 1.0f;
        centrarGrid();
    }
}
package com.attendance.app;

import static org.junit.Assert.*;

import com.attendance.app.data.entities.Alumno;
import com.attendance.app.data.entities.AlumnoCursoCrossRef;
import com.attendance.app.data.entities.Asistencia;
import com.attendance.app.data.entities.Curso;

import org.junit.Test;

/**
 * Tests unitarios puros (JVM) para las entidades del dominio.
 * No requieren contexto Android ni base de datos.
 */
public class EntityTest {

    @Test
    public void alumno_constructorYGetters() {
        Alumno a = new Alumno("María García", "MAT-001");
        assertEquals("María García", a.getNombre());
        assertEquals("MAT-001", a.getMatricula());
        assertEquals(0L, a.getId());  // default antes de insertar
    }

    @Test
    public void alumno_setters() {
        Alumno a = new Alumno();
        a.setId(42L);
        a.setNombre("Juan");
        a.setMatricula("X99");
        assertEquals(42L, a.getId());
        assertEquals("Juan", a.getNombre());
        assertEquals("X99", a.getMatricula());
    }

    @Test
    public void curso_constructorYGetters() {
        Curso c = new Curso("Cálculo I", "CALC-101");
        assertEquals("Cálculo I", c.getNombre());
        assertEquals("CALC-101", c.getCodigo());
    }

    @Test
    public void crossRef_alumnoYCursoCorrectos() {
        AlumnoCursoCrossRef ref = new AlumnoCursoCrossRef(10L, 20L);
        assertEquals(10L, ref.getAlumnoId());
        assertEquals(20L, ref.getCursoId());
    }

    @Test
    public void asistencia_presente() {
        Asistencia a = new Asistencia(1L, 2L, "2025-05-10", true);
        assertTrue(a.isPresente());
        assertEquals("2025-05-10", a.getFecha());
    }

    @Test
    public void asistencia_ausente() {
        Asistencia a = new Asistencia(1L, 2L, "2025-05-11", false);
        assertFalse(a.isPresente());
    }

    @Test
    public void asistencia_setPresente_alterna() {
        Asistencia a = new Asistencia(1L, 1L, "2025-01-01", false);
        a.setPresente(true);
        assertTrue(a.isPresente());
    }

    @Test
    public void curso_setters() {
        Curso c = new Curso();
        c.setId(7L);
        c.setNombre("Química");
        c.setCodigo("QUI-200");
        assertEquals(7L, c.getId());
        assertEquals("Química", c.getNombre());
        assertEquals("QUI-200", c.getCodigo());
    }
}

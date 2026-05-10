package com.attendance.app.navigation;

/**
 * Interfaz que la única Activity debe implementar.
 * Los fragmentos la obtienen vía requireActivity() para navegar
 * sin acoplar la lógica de navegación a la Activity concreta.
 */
public interface NavigationHost {

    /**
     * Navega al listado de alumnos inscritos en el curso indicado.
     *
     * @param cursoId  ID del curso seleccionado.
     * @param cursoNombre Nombre del curso para mostrarlo en el header.
     */
    void navegarAAlumnos(long cursoId, String cursoNombre);

    /**
     * Navega al historial de asistencias de un alumno en un curso.
     *
     * @param alumnoId    ID del alumno seleccionado.
     * @param cursoId     ID del curso en contexto.
     * @param alumnoNombre Nombre del alumno para mostrarlo en el header.
     */
    void navegarAAsistencias(long alumnoId, long cursoId, String alumnoNombre);

    /**
     * Regresa al fragmento anterior en el back stack.
     */
    void navegarAtras();
}

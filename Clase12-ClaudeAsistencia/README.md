# 📱 Control de Asistencias V2 — Single Activity Architecture

## Stack tecnológico

| Tecnología | Uso |
|---|---|
| **Room** | Persistencia local (Entity, DAO, Database) |
| **ViewModel** | Ciclo de vida + lógica de presentación |
| **LiveData** + `Transformations.switchMap` | Datos reactivos entre pantallas |
| **ViewBinding** | Acceso seguro a vistas (null-safe) |
| **Single Activity** | `MainActivity` + 3 Fragments via `FragmentManager` |
| **GenericAdapter** (`BaseAdapter<T, VB>`) | Adaptador reutilizable con ViewBinding |
| **NavigationHost** | Interfaz que desacopla los Fragments de la Activity |

---

## Arquitectura de paquetes

```
com.attendance.app
├── MainActivity.java                         ← Única Activity; implementa NavigationHost
│
├── navigation/
│   └── NavigationHost.java                   ← Interfaz: navegarAAlumnos, navegarAAsistencias, navegarAtras
│
├── data/
│   ├── entities/
│   │   ├── Alumno.java                       ← @Entity: tabla alumnos
│   │   ├── Curso.java                        ← @Entity: tabla cursos
│   │   ├── AlumnoCursoCrossRef.java          ← @Entity: tabla intermedia N:M
│   │   └── Asistencia.java                   ← @Entity: tabla asistencias
│   ├── dao/
│   │   ├── AlumnoDao.java                    ← CRUD + inscribir + getAlumnosPorCurso
│   │   ├── CursoDao.java                     ← CRUD + getAllCursos (LiveData)
│   │   └── AsistenciaDao.java                ← insert, update, getAsistencias (LiveData)
│   ├── db/
│   │   └── AppDatabase.java                  ← Room singleton + seed de datos
│   └── repository/
│       └── AppRepository.java                ← Capa de abstracción sobre los DAOs
│
└── ui/
    ├── adapters/
    │   ├── BaseAdapter.java                  ← Clase abstracta genérica <T, VB extends ViewBinding>
    │   ├── CursoAdapter.java                 ← extiende BaseAdapter<Curso, ItemCursoBinding>
    │   ├── AlumnoAdapter.java                ← extiende BaseAdapter<Alumno, ItemAlumnoBinding>
    │   └── AsistenciaAdapter.java            ← extiende BaseAdapter<Asistencia, ItemAsistenciaBinding>
    ├── fragments/
    │   ├── CursosFragment.java               ← Pantalla principal (lista de cursos)
    │   ├── AlumnosFragment.java              ← Alumnos del curso seleccionado
    │   └── AsistenciasFragment.java          ← Historial + registro de asistencia
    └── viewmodel/
        └── AppViewModel.java                 ← ViewModel único con switchMap para las 3 pantallas
```

---

## Modelo de datos

```
alumnos ─────────────── alumno_curso_cross_ref ─── cursos
  id PK (autoGenerate)    alumnoId FK                id PK (autoGenerate)
  nombre                  cursoId  FK                nombre
  matricula                                           codigo

asistencias
  id PK (autoGenerate)
  alumnoId FK → alumnos.id    (CASCADE DELETE)
  cursoId  FK → cursos.id     (CASCADE DELETE)
  fecha    (ISO-8601)
  presente (boolean)
```

**Relaciones:**
- `Alumno` ↔ `Curso`: **N:M** a través de `AlumnoCursoCrossRef`
- `Asistencia`: un alumno en un curso puede tener **múltiples registros** (uno por sesión)

---

## Patrón BaseAdapter

```java
// Solo implementar 2 métodos abstractos:
public class CursoAdapter extends BaseAdapter<Curso, ItemCursoBinding> {

    @Override
    protected ItemCursoBinding inflate(LayoutInflater inflater, ViewGroup parent) {
        return ItemCursoBinding.inflate(inflater, parent, false);
    }

    @Override
    protected void bind(ItemCursoBinding b, Curso item, int position) {
        b.tvCursoNombre.setText(item.nombre);
        b.getRoot().setOnClickListener(v -> listener.onClick(item));
    }
}
```

---

## Flujo de navegación

```
CursosFragment
    │ click en curso
    ▼
AlumnosFragment(cursoId, cursoNombre)
    │ click en alumno
    ▼
AsistenciasFragment(alumnoId, cursoId, alumnoNombre)
    │ click "Regresar" / back press
    ▲─────────────────────────────────
```

`NavigationHost.navegarAtras()` hace `popBackStack()` — el Fragment anterior se restaura automáticamente.

---

## Pruebas unitarias

### DaoTest.java (8 casos — Robolectric + Room in-memory)
| # | Caso |
|---|---|
| 1 | Inserción de Curso → ID positivo |
| 2 | Inserción de Curso → datos recuperados correctamente |
| 3 | Inserción de Alumno → datos se conservan tras inscripción |
| 4 | Inscripción alumno ↔ curso → `isInscrito` retorna 1 |
| 5 | Desinscripción → `isInscrito` retorna 0 |
| 6 | `getAlumnosPorCurso` → solo alumnos del curso solicitado |
| 7 | Alumno inscrito en múltiples cursos → aparece en cada uno |
| 8 | Registro de asistencia → se detecta con `existeRegistro` |

### EntityTest.java (8 casos — JVM puro)
Constructores, getters, setters y lógica básica de cada entidad.

```bash
# Ejecutar todos los tests
./gradlew test
```

---

## Importar en Android Studio

1. `File → Open` → seleccionar `AttendanceV2/`
2. Esperar sincronización de Gradle
3. Ejecutar en emulador API 26+ o dispositivo físico

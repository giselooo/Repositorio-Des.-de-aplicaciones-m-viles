package com.attendance.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.attendance.app.data.dao.AlumnoDao;
import com.attendance.app.data.dao.AlumnoDao_Impl;
import com.attendance.app.data.dao.AsistenciaDao;
import com.attendance.app.data.dao.AsistenciaDao_Impl;
import com.attendance.app.data.dao.CursoDao;
import com.attendance.app.data.dao.CursoDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile AlumnoDao _alumnoDao;

  private volatile CursoDao _cursoDao;

  private volatile AsistenciaDao _asistenciaDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `alumnos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT, `matricula` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `cursos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT, `codigo` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `alumno_curso_cross_ref` (`alumnoId` INTEGER NOT NULL, `cursoId` INTEGER NOT NULL, PRIMARY KEY(`alumnoId`, `cursoId`), FOREIGN KEY(`alumnoId`) REFERENCES `alumnos`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`cursoId`) REFERENCES `cursos`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_alumno_curso_cross_ref_alumnoId` ON `alumno_curso_cross_ref` (`alumnoId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_alumno_curso_cross_ref_cursoId` ON `alumno_curso_cross_ref` (`cursoId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `asistencias` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `alumnoId` INTEGER NOT NULL, `cursoId` INTEGER NOT NULL, `fecha` TEXT, `presente` INTEGER NOT NULL, FOREIGN KEY(`alumnoId`) REFERENCES `alumnos`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`cursoId`) REFERENCES `cursos`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_asistencias_alumnoId` ON `asistencias` (`alumnoId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_asistencias_cursoId` ON `asistencias` (`cursoId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '9f1091f9d7ed213eb304484d6021817a')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `alumnos`");
        db.execSQL("DROP TABLE IF EXISTS `cursos`");
        db.execSQL("DROP TABLE IF EXISTS `alumno_curso_cross_ref`");
        db.execSQL("DROP TABLE IF EXISTS `asistencias`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsAlumnos = new HashMap<String, TableInfo.Column>(3);
        _columnsAlumnos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlumnos.put("nombre", new TableInfo.Column("nombre", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlumnos.put("matricula", new TableInfo.Column("matricula", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAlumnos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAlumnos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAlumnos = new TableInfo("alumnos", _columnsAlumnos, _foreignKeysAlumnos, _indicesAlumnos);
        final TableInfo _existingAlumnos = TableInfo.read(db, "alumnos");
        if (!_infoAlumnos.equals(_existingAlumnos)) {
          return new RoomOpenHelper.ValidationResult(false, "alumnos(com.attendance.app.data.entities.Alumno).\n"
                  + " Expected:\n" + _infoAlumnos + "\n"
                  + " Found:\n" + _existingAlumnos);
        }
        final HashMap<String, TableInfo.Column> _columnsCursos = new HashMap<String, TableInfo.Column>(3);
        _columnsCursos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCursos.put("nombre", new TableInfo.Column("nombre", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCursos.put("codigo", new TableInfo.Column("codigo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCursos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCursos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCursos = new TableInfo("cursos", _columnsCursos, _foreignKeysCursos, _indicesCursos);
        final TableInfo _existingCursos = TableInfo.read(db, "cursos");
        if (!_infoCursos.equals(_existingCursos)) {
          return new RoomOpenHelper.ValidationResult(false, "cursos(com.attendance.app.data.entities.Curso).\n"
                  + " Expected:\n" + _infoCursos + "\n"
                  + " Found:\n" + _existingCursos);
        }
        final HashMap<String, TableInfo.Column> _columnsAlumnoCursoCrossRef = new HashMap<String, TableInfo.Column>(2);
        _columnsAlumnoCursoCrossRef.put("alumnoId", new TableInfo.Column("alumnoId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlumnoCursoCrossRef.put("cursoId", new TableInfo.Column("cursoId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAlumnoCursoCrossRef = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysAlumnoCursoCrossRef.add(new TableInfo.ForeignKey("alumnos", "CASCADE", "NO ACTION", Arrays.asList("alumnoId"), Arrays.asList("id")));
        _foreignKeysAlumnoCursoCrossRef.add(new TableInfo.ForeignKey("cursos", "CASCADE", "NO ACTION", Arrays.asList("cursoId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesAlumnoCursoCrossRef = new HashSet<TableInfo.Index>(2);
        _indicesAlumnoCursoCrossRef.add(new TableInfo.Index("index_alumno_curso_cross_ref_alumnoId", false, Arrays.asList("alumnoId"), Arrays.asList("ASC")));
        _indicesAlumnoCursoCrossRef.add(new TableInfo.Index("index_alumno_curso_cross_ref_cursoId", false, Arrays.asList("cursoId"), Arrays.asList("ASC")));
        final TableInfo _infoAlumnoCursoCrossRef = new TableInfo("alumno_curso_cross_ref", _columnsAlumnoCursoCrossRef, _foreignKeysAlumnoCursoCrossRef, _indicesAlumnoCursoCrossRef);
        final TableInfo _existingAlumnoCursoCrossRef = TableInfo.read(db, "alumno_curso_cross_ref");
        if (!_infoAlumnoCursoCrossRef.equals(_existingAlumnoCursoCrossRef)) {
          return new RoomOpenHelper.ValidationResult(false, "alumno_curso_cross_ref(com.attendance.app.data.entities.AlumnoCursoCrossRef).\n"
                  + " Expected:\n" + _infoAlumnoCursoCrossRef + "\n"
                  + " Found:\n" + _existingAlumnoCursoCrossRef);
        }
        final HashMap<String, TableInfo.Column> _columnsAsistencias = new HashMap<String, TableInfo.Column>(5);
        _columnsAsistencias.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAsistencias.put("alumnoId", new TableInfo.Column("alumnoId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAsistencias.put("cursoId", new TableInfo.Column("cursoId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAsistencias.put("fecha", new TableInfo.Column("fecha", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAsistencias.put("presente", new TableInfo.Column("presente", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAsistencias = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysAsistencias.add(new TableInfo.ForeignKey("alumnos", "CASCADE", "NO ACTION", Arrays.asList("alumnoId"), Arrays.asList("id")));
        _foreignKeysAsistencias.add(new TableInfo.ForeignKey("cursos", "CASCADE", "NO ACTION", Arrays.asList("cursoId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesAsistencias = new HashSet<TableInfo.Index>(2);
        _indicesAsistencias.add(new TableInfo.Index("index_asistencias_alumnoId", false, Arrays.asList("alumnoId"), Arrays.asList("ASC")));
        _indicesAsistencias.add(new TableInfo.Index("index_asistencias_cursoId", false, Arrays.asList("cursoId"), Arrays.asList("ASC")));
        final TableInfo _infoAsistencias = new TableInfo("asistencias", _columnsAsistencias, _foreignKeysAsistencias, _indicesAsistencias);
        final TableInfo _existingAsistencias = TableInfo.read(db, "asistencias");
        if (!_infoAsistencias.equals(_existingAsistencias)) {
          return new RoomOpenHelper.ValidationResult(false, "asistencias(com.attendance.app.data.entities.Asistencia).\n"
                  + " Expected:\n" + _infoAsistencias + "\n"
                  + " Found:\n" + _existingAsistencias);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "9f1091f9d7ed213eb304484d6021817a", "baddacd528e8d438c69b5236c646ffa4");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "alumnos","cursos","alumno_curso_cross_ref","asistencias");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `alumnos`");
      _db.execSQL("DELETE FROM `cursos`");
      _db.execSQL("DELETE FROM `alumno_curso_cross_ref`");
      _db.execSQL("DELETE FROM `asistencias`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AlumnoDao.class, AlumnoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CursoDao.class, CursoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AsistenciaDao.class, AsistenciaDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public AlumnoDao alumnoDao() {
    if (_alumnoDao != null) {
      return _alumnoDao;
    } else {
      synchronized(this) {
        if(_alumnoDao == null) {
          _alumnoDao = new AlumnoDao_Impl(this);
        }
        return _alumnoDao;
      }
    }
  }

  @Override
  public CursoDao cursoDao() {
    if (_cursoDao != null) {
      return _cursoDao;
    } else {
      synchronized(this) {
        if(_cursoDao == null) {
          _cursoDao = new CursoDao_Impl(this);
        }
        return _cursoDao;
      }
    }
  }

  @Override
  public AsistenciaDao asistenciaDao() {
    if (_asistenciaDao != null) {
      return _asistenciaDao;
    } else {
      synchronized(this) {
        if(_asistenciaDao == null) {
          _asistenciaDao = new AsistenciaDao_Impl(this);
        }
        return _asistenciaDao;
      }
    }
  }
}

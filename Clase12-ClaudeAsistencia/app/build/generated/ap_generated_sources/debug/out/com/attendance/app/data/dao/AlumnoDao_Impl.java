package com.attendance.app.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.attendance.app.data.entities.Alumno;
import com.attendance.app.data.entities.AlumnoCursoCrossRef;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AlumnoDao_Impl implements AlumnoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Alumno> __insertionAdapterOfAlumno;

  private final EntityInsertionAdapter<AlumnoCursoCrossRef> __insertionAdapterOfAlumnoCursoCrossRef;

  private final EntityDeletionOrUpdateAdapter<Alumno> __deletionAdapterOfAlumno;

  private final EntityDeletionOrUpdateAdapter<Alumno> __updateAdapterOfAlumno;

  private final SharedSQLiteStatement __preparedStmtOfDesinscribir;

  public AlumnoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAlumno = new EntityInsertionAdapter<Alumno>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `alumnos` (`id`,`nombre`,`matricula`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Alumno entity) {
        statement.bindLong(1, entity.id);
        if (entity.nombre == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.nombre);
        }
        if (entity.matricula == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.matricula);
        }
      }
    };
    this.__insertionAdapterOfAlumnoCursoCrossRef = new EntityInsertionAdapter<AlumnoCursoCrossRef>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `alumno_curso_cross_ref` (`alumnoId`,`cursoId`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final AlumnoCursoCrossRef entity) {
        statement.bindLong(1, entity.alumnoId);
        statement.bindLong(2, entity.cursoId);
      }
    };
    this.__deletionAdapterOfAlumno = new EntityDeletionOrUpdateAdapter<Alumno>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `alumnos` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Alumno entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfAlumno = new EntityDeletionOrUpdateAdapter<Alumno>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `alumnos` SET `id` = ?,`nombre` = ?,`matricula` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Alumno entity) {
        statement.bindLong(1, entity.id);
        if (entity.nombre == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.nombre);
        }
        if (entity.matricula == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.matricula);
        }
        statement.bindLong(4, entity.id);
      }
    };
    this.__preparedStmtOfDesinscribir = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM alumno_curso_cross_ref WHERE alumnoId = ? AND cursoId = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final Alumno alumno) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfAlumno.insertAndReturnId(alumno);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void inscribir(final AlumnoCursoCrossRef crossRef) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfAlumnoCursoCrossRef.insert(crossRef);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Alumno alumno) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfAlumno.handle(alumno);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Alumno alumno) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfAlumno.handle(alumno);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void desinscribir(final long alumnoId, final long cursoId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDesinscribir.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, alumnoId);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, cursoId);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDesinscribir.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Alumno>> getAlumnosPorCurso(final long cursoId) {
    final String _sql = "    SELECT a.* FROM alumnos a\n"
            + "    INNER JOIN alumno_curso_cross_ref ref ON a.id = ref.alumnoId\n"
            + "    WHERE ref.cursoId = ?\n"
            + "    ORDER BY a.nombre ASC\n";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, cursoId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"alumnos",
        "alumno_curso_cross_ref"}, false, new Callable<List<Alumno>>() {
      @Override
      @Nullable
      public List<Alumno> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfMatricula = CursorUtil.getColumnIndexOrThrow(_cursor, "matricula");
          final List<Alumno> _result = new ArrayList<Alumno>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Alumno _item;
            _item = new Alumno();
            _item.id = _cursor.getLong(_cursorIndexOfId);
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _item.nombre = null;
            } else {
              _item.nombre = _cursor.getString(_cursorIndexOfNombre);
            }
            if (_cursor.isNull(_cursorIndexOfMatricula)) {
              _item.matricula = null;
            } else {
              _item.matricula = _cursor.getString(_cursorIndexOfMatricula);
            }
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<Alumno> getAlumnosPorCursoSync(final long cursoId) {
    final String _sql = "    SELECT a.* FROM alumnos a\n"
            + "    INNER JOIN alumno_curso_cross_ref ref ON a.id = ref.alumnoId\n"
            + "    WHERE ref.cursoId = ?\n"
            + "    ORDER BY a.nombre ASC\n";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, cursoId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
      final int _cursorIndexOfMatricula = CursorUtil.getColumnIndexOrThrow(_cursor, "matricula");
      final List<Alumno> _result = new ArrayList<Alumno>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Alumno _item;
        _item = new Alumno();
        _item.id = _cursor.getLong(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfNombre)) {
          _item.nombre = null;
        } else {
          _item.nombre = _cursor.getString(_cursorIndexOfNombre);
        }
        if (_cursor.isNull(_cursorIndexOfMatricula)) {
          _item.matricula = null;
        } else {
          _item.matricula = _cursor.getString(_cursorIndexOfMatricula);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int isInscrito(final long alumnoId, final long cursoId) {
    final String _sql = "SELECT COUNT(*) FROM alumno_curso_cross_ref WHERE alumnoId = ? AND cursoId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, alumnoId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, cursoId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getCount() {
    final String _sql = "SELECT COUNT(*) FROM alumnos";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

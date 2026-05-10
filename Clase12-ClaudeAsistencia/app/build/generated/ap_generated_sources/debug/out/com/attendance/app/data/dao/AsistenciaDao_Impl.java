package com.attendance.app.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.attendance.app.data.entities.Asistencia;
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
public final class AsistenciaDao_Impl implements AsistenciaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Asistencia> __insertionAdapterOfAsistencia;

  private final EntityDeletionOrUpdateAdapter<Asistencia> __updateAdapterOfAsistencia;

  public AsistenciaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAsistencia = new EntityInsertionAdapter<Asistencia>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `asistencias` (`id`,`alumnoId`,`cursoId`,`fecha`,`presente`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final Asistencia entity) {
        statement.bindLong(1, entity.id);
        statement.bindLong(2, entity.alumnoId);
        statement.bindLong(3, entity.cursoId);
        if (entity.fecha == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.fecha);
        }
        final int _tmp = entity.presente ? 1 : 0;
        statement.bindLong(5, _tmp);
      }
    };
    this.__updateAdapterOfAsistencia = new EntityDeletionOrUpdateAdapter<Asistencia>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `asistencias` SET `id` = ?,`alumnoId` = ?,`cursoId` = ?,`fecha` = ?,`presente` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final Asistencia entity) {
        statement.bindLong(1, entity.id);
        statement.bindLong(2, entity.alumnoId);
        statement.bindLong(3, entity.cursoId);
        if (entity.fecha == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.fecha);
        }
        final int _tmp = entity.presente ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.id);
      }
    };
  }

  @Override
  public long insert(final Asistencia asistencia) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfAsistencia.insertAndReturnId(asistencia);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Asistencia asistencia) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfAsistencia.handle(asistencia);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<Asistencia>> getAsistencias(final long alumnoId, final long cursoId) {
    final String _sql = "    SELECT * FROM asistencias\n"
            + "    WHERE alumnoId = ? AND cursoId = ?\n"
            + "    ORDER BY fecha DESC\n";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, alumnoId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, cursoId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"asistencias"}, false, new Callable<List<Asistencia>>() {
      @Override
      @Nullable
      public List<Asistencia> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAlumnoId = CursorUtil.getColumnIndexOrThrow(_cursor, "alumnoId");
          final int _cursorIndexOfCursoId = CursorUtil.getColumnIndexOrThrow(_cursor, "cursoId");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfPresente = CursorUtil.getColumnIndexOrThrow(_cursor, "presente");
          final List<Asistencia> _result = new ArrayList<Asistencia>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Asistencia _item;
            _item = new Asistencia();
            _item.id = _cursor.getLong(_cursorIndexOfId);
            _item.alumnoId = _cursor.getLong(_cursorIndexOfAlumnoId);
            _item.cursoId = _cursor.getLong(_cursorIndexOfCursoId);
            if (_cursor.isNull(_cursorIndexOfFecha)) {
              _item.fecha = null;
            } else {
              _item.fecha = _cursor.getString(_cursorIndexOfFecha);
            }
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfPresente);
            _item.presente = _tmp != 0;
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
  public List<Asistencia> getAsistenciasSync(final long alumnoId, final long cursoId) {
    final String _sql = "    SELECT * FROM asistencias\n"
            + "    WHERE alumnoId = ? AND cursoId = ?\n"
            + "    ORDER BY fecha DESC\n";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, alumnoId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, cursoId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfAlumnoId = CursorUtil.getColumnIndexOrThrow(_cursor, "alumnoId");
      final int _cursorIndexOfCursoId = CursorUtil.getColumnIndexOrThrow(_cursor, "cursoId");
      final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
      final int _cursorIndexOfPresente = CursorUtil.getColumnIndexOrThrow(_cursor, "presente");
      final List<Asistencia> _result = new ArrayList<Asistencia>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Asistencia _item;
        _item = new Asistencia();
        _item.id = _cursor.getLong(_cursorIndexOfId);
        _item.alumnoId = _cursor.getLong(_cursorIndexOfAlumnoId);
        _item.cursoId = _cursor.getLong(_cursorIndexOfCursoId);
        if (_cursor.isNull(_cursorIndexOfFecha)) {
          _item.fecha = null;
        } else {
          _item.fecha = _cursor.getString(_cursorIndexOfFecha);
        }
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfPresente);
        _item.presente = _tmp != 0;
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getCount() {
    final String _sql = "SELECT COUNT(*) FROM asistencias";
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

  @Override
  public int existeRegistro(final long alumnoId, final long cursoId, final String fecha) {
    final String _sql = "    SELECT COUNT(*) FROM asistencias\n"
            + "    WHERE alumnoId = ? AND cursoId = ? AND fecha = ?\n";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, alumnoId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, cursoId);
    _argIndex = 3;
    if (fecha == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, fecha);
    }
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

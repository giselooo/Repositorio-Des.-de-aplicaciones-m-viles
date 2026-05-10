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
import com.attendance.app.data.entities.Curso;
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
public final class CursoDao_Impl implements CursoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Curso> __insertionAdapterOfCurso;

  private final EntityDeletionOrUpdateAdapter<Curso> __deletionAdapterOfCurso;

  private final EntityDeletionOrUpdateAdapter<Curso> __updateAdapterOfCurso;

  public CursoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCurso = new EntityInsertionAdapter<Curso>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `cursos` (`id`,`nombre`,`codigo`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Curso entity) {
        statement.bindLong(1, entity.id);
        if (entity.nombre == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.nombre);
        }
        if (entity.codigo == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.codigo);
        }
      }
    };
    this.__deletionAdapterOfCurso = new EntityDeletionOrUpdateAdapter<Curso>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `cursos` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Curso entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfCurso = new EntityDeletionOrUpdateAdapter<Curso>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `cursos` SET `id` = ?,`nombre` = ?,`codigo` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Curso entity) {
        statement.bindLong(1, entity.id);
        if (entity.nombre == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.nombre);
        }
        if (entity.codigo == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.codigo);
        }
        statement.bindLong(4, entity.id);
      }
    };
  }

  @Override
  public long insert(final Curso curso) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfCurso.insertAndReturnId(curso);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Curso curso) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfCurso.handle(curso);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Curso curso) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfCurso.handle(curso);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<Curso>> getAllCursos() {
    final String _sql = "SELECT * FROM cursos ORDER BY nombre ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"cursos"}, false, new Callable<List<Curso>>() {
      @Override
      @Nullable
      public List<Curso> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "codigo");
          final List<Curso> _result = new ArrayList<Curso>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Curso _item;
            _item = new Curso();
            _item.id = _cursor.getLong(_cursorIndexOfId);
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _item.nombre = null;
            } else {
              _item.nombre = _cursor.getString(_cursorIndexOfNombre);
            }
            if (_cursor.isNull(_cursorIndexOfCodigo)) {
              _item.codigo = null;
            } else {
              _item.codigo = _cursor.getString(_cursorIndexOfCodigo);
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
  public List<Curso> getAllCursosSync() {
    final String _sql = "SELECT * FROM cursos ORDER BY nombre ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
      final int _cursorIndexOfCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "codigo");
      final List<Curso> _result = new ArrayList<Curso>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Curso _item;
        _item = new Curso();
        _item.id = _cursor.getLong(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfNombre)) {
          _item.nombre = null;
        } else {
          _item.nombre = _cursor.getString(_cursorIndexOfNombre);
        }
        if (_cursor.isNull(_cursorIndexOfCodigo)) {
          _item.codigo = null;
        } else {
          _item.codigo = _cursor.getString(_cursorIndexOfCodigo);
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
  public int getCount() {
    final String _sql = "SELECT COUNT(*) FROM cursos";
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

package com.livraison.supervision.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class LivraisonLocaleDao_Impl implements LivraisonLocaleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LivraisonLocale> __insertionAdapterOfLivraisonLocale;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStatut;

  private final SharedSQLiteStatement __preparedStmtOfMarkSynced;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldData;

  public LivraisonLocaleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLivraisonLocale = new EntityInsertionAdapter<LivraisonLocale>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `livraison_locale` (`id`,`numeroCommande`,`clientNom`,`clientTelephone`,`clientAdresse`,`clientVille`,`statut`,`modePaiement`,`nbArticles`,`montant`,`dateLivraison`,`remarque`,`zone`,`synced`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final LivraisonLocale entity) {
        statement.bindLong(1, entity.id);
        if (entity.numeroCommande == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.numeroCommande);
        }
        if (entity.clientNom == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.clientNom);
        }
        if (entity.clientTelephone == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.clientTelephone);
        }
        if (entity.clientAdresse == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.clientAdresse);
        }
        if (entity.clientVille == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.clientVille);
        }
        if (entity.statut == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.statut);
        }
        if (entity.modePaiement == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.modePaiement);
        }
        statement.bindLong(9, entity.nbArticles);
        statement.bindDouble(10, entity.montant);
        if (entity.dateLivraison == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.dateLivraison);
        }
        if (entity.remarque == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.remarque);
        }
        if (entity.zone == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.zone);
        }
        final int _tmp = entity.synced ? 1 : 0;
        statement.bindLong(14, _tmp);
      }
    };
    this.__preparedStmtOfUpdateStatut = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE livraison_locale SET statut = ?, remarque = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE livraison_locale SET synced = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOldData = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM livraison_locale WHERE dateLivraison != ?";
        return _query;
      }
    };
  }

  @Override
  public void insertAll(final List<LivraisonLocale> livraisons) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLivraisonLocale.insert(livraisons);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insert(final LivraisonLocale livraison) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLivraisonLocale.insert(livraison);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateStatut(final long id, final String statut, final String remarque) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStatut.acquire();
    int _argIndex = 1;
    if (statut == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, statut);
    }
    _argIndex = 2;
    if (remarque == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, remarque);
    }
    _argIndex = 3;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateStatut.release(_stmt);
    }
  }

  @Override
  public void markSynced(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfMarkSynced.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfMarkSynced.release(_stmt);
    }
  }

  @Override
  public void deleteOldData(final String date) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldData.acquire();
    int _argIndex = 1;
    if (date == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, date);
    }
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteOldData.release(_stmt);
    }
  }

  @Override
  public List<LivraisonLocale> getByDate(final String date) {
    final String _sql = "SELECT * FROM livraison_locale WHERE dateLivraison = ? ORDER BY zone ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (date == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, date);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfNumeroCommande = CursorUtil.getColumnIndexOrThrow(_cursor, "numeroCommande");
      final int _cursorIndexOfClientNom = CursorUtil.getColumnIndexOrThrow(_cursor, "clientNom");
      final int _cursorIndexOfClientTelephone = CursorUtil.getColumnIndexOrThrow(_cursor, "clientTelephone");
      final int _cursorIndexOfClientAdresse = CursorUtil.getColumnIndexOrThrow(_cursor, "clientAdresse");
      final int _cursorIndexOfClientVille = CursorUtil.getColumnIndexOrThrow(_cursor, "clientVille");
      final int _cursorIndexOfStatut = CursorUtil.getColumnIndexOrThrow(_cursor, "statut");
      final int _cursorIndexOfModePaiement = CursorUtil.getColumnIndexOrThrow(_cursor, "modePaiement");
      final int _cursorIndexOfNbArticles = CursorUtil.getColumnIndexOrThrow(_cursor, "nbArticles");
      final int _cursorIndexOfMontant = CursorUtil.getColumnIndexOrThrow(_cursor, "montant");
      final int _cursorIndexOfDateLivraison = CursorUtil.getColumnIndexOrThrow(_cursor, "dateLivraison");
      final int _cursorIndexOfRemarque = CursorUtil.getColumnIndexOrThrow(_cursor, "remarque");
      final int _cursorIndexOfZone = CursorUtil.getColumnIndexOrThrow(_cursor, "zone");
      final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
      final List<LivraisonLocale> _result = new ArrayList<LivraisonLocale>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final LivraisonLocale _item;
        _item = new LivraisonLocale();
        _item.id = _cursor.getLong(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfNumeroCommande)) {
          _item.numeroCommande = null;
        } else {
          _item.numeroCommande = _cursor.getString(_cursorIndexOfNumeroCommande);
        }
        if (_cursor.isNull(_cursorIndexOfClientNom)) {
          _item.clientNom = null;
        } else {
          _item.clientNom = _cursor.getString(_cursorIndexOfClientNom);
        }
        if (_cursor.isNull(_cursorIndexOfClientTelephone)) {
          _item.clientTelephone = null;
        } else {
          _item.clientTelephone = _cursor.getString(_cursorIndexOfClientTelephone);
        }
        if (_cursor.isNull(_cursorIndexOfClientAdresse)) {
          _item.clientAdresse = null;
        } else {
          _item.clientAdresse = _cursor.getString(_cursorIndexOfClientAdresse);
        }
        if (_cursor.isNull(_cursorIndexOfClientVille)) {
          _item.clientVille = null;
        } else {
          _item.clientVille = _cursor.getString(_cursorIndexOfClientVille);
        }
        if (_cursor.isNull(_cursorIndexOfStatut)) {
          _item.statut = null;
        } else {
          _item.statut = _cursor.getString(_cursorIndexOfStatut);
        }
        if (_cursor.isNull(_cursorIndexOfModePaiement)) {
          _item.modePaiement = null;
        } else {
          _item.modePaiement = _cursor.getString(_cursorIndexOfModePaiement);
        }
        _item.nbArticles = _cursor.getInt(_cursorIndexOfNbArticles);
        _item.montant = _cursor.getDouble(_cursorIndexOfMontant);
        if (_cursor.isNull(_cursorIndexOfDateLivraison)) {
          _item.dateLivraison = null;
        } else {
          _item.dateLivraison = _cursor.getString(_cursorIndexOfDateLivraison);
        }
        if (_cursor.isNull(_cursorIndexOfRemarque)) {
          _item.remarque = null;
        } else {
          _item.remarque = _cursor.getString(_cursorIndexOfRemarque);
        }
        if (_cursor.isNull(_cursorIndexOfZone)) {
          _item.zone = null;
        } else {
          _item.zone = _cursor.getString(_cursorIndexOfZone);
        }
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfSynced);
        _item.synced = _tmp != 0;
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LivraisonLocale getById(final long id) {
    final String _sql = "SELECT * FROM livraison_locale WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfNumeroCommande = CursorUtil.getColumnIndexOrThrow(_cursor, "numeroCommande");
      final int _cursorIndexOfClientNom = CursorUtil.getColumnIndexOrThrow(_cursor, "clientNom");
      final int _cursorIndexOfClientTelephone = CursorUtil.getColumnIndexOrThrow(_cursor, "clientTelephone");
      final int _cursorIndexOfClientAdresse = CursorUtil.getColumnIndexOrThrow(_cursor, "clientAdresse");
      final int _cursorIndexOfClientVille = CursorUtil.getColumnIndexOrThrow(_cursor, "clientVille");
      final int _cursorIndexOfStatut = CursorUtil.getColumnIndexOrThrow(_cursor, "statut");
      final int _cursorIndexOfModePaiement = CursorUtil.getColumnIndexOrThrow(_cursor, "modePaiement");
      final int _cursorIndexOfNbArticles = CursorUtil.getColumnIndexOrThrow(_cursor, "nbArticles");
      final int _cursorIndexOfMontant = CursorUtil.getColumnIndexOrThrow(_cursor, "montant");
      final int _cursorIndexOfDateLivraison = CursorUtil.getColumnIndexOrThrow(_cursor, "dateLivraison");
      final int _cursorIndexOfRemarque = CursorUtil.getColumnIndexOrThrow(_cursor, "remarque");
      final int _cursorIndexOfZone = CursorUtil.getColumnIndexOrThrow(_cursor, "zone");
      final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
      final LivraisonLocale _result;
      if (_cursor.moveToFirst()) {
        _result = new LivraisonLocale();
        _result.id = _cursor.getLong(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfNumeroCommande)) {
          _result.numeroCommande = null;
        } else {
          _result.numeroCommande = _cursor.getString(_cursorIndexOfNumeroCommande);
        }
        if (_cursor.isNull(_cursorIndexOfClientNom)) {
          _result.clientNom = null;
        } else {
          _result.clientNom = _cursor.getString(_cursorIndexOfClientNom);
        }
        if (_cursor.isNull(_cursorIndexOfClientTelephone)) {
          _result.clientTelephone = null;
        } else {
          _result.clientTelephone = _cursor.getString(_cursorIndexOfClientTelephone);
        }
        if (_cursor.isNull(_cursorIndexOfClientAdresse)) {
          _result.clientAdresse = null;
        } else {
          _result.clientAdresse = _cursor.getString(_cursorIndexOfClientAdresse);
        }
        if (_cursor.isNull(_cursorIndexOfClientVille)) {
          _result.clientVille = null;
        } else {
          _result.clientVille = _cursor.getString(_cursorIndexOfClientVille);
        }
        if (_cursor.isNull(_cursorIndexOfStatut)) {
          _result.statut = null;
        } else {
          _result.statut = _cursor.getString(_cursorIndexOfStatut);
        }
        if (_cursor.isNull(_cursorIndexOfModePaiement)) {
          _result.modePaiement = null;
        } else {
          _result.modePaiement = _cursor.getString(_cursorIndexOfModePaiement);
        }
        _result.nbArticles = _cursor.getInt(_cursorIndexOfNbArticles);
        _result.montant = _cursor.getDouble(_cursorIndexOfMontant);
        if (_cursor.isNull(_cursorIndexOfDateLivraison)) {
          _result.dateLivraison = null;
        } else {
          _result.dateLivraison = _cursor.getString(_cursorIndexOfDateLivraison);
        }
        if (_cursor.isNull(_cursorIndexOfRemarque)) {
          _result.remarque = null;
        } else {
          _result.remarque = _cursor.getString(_cursorIndexOfRemarque);
        }
        if (_cursor.isNull(_cursorIndexOfZone)) {
          _result.zone = null;
        } else {
          _result.zone = _cursor.getString(_cursorIndexOfZone);
        }
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfSynced);
        _result.synced = _tmp != 0;
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<LivraisonLocale> getNonSynced(final String date) {
    final String _sql = "SELECT * FROM livraison_locale WHERE synced = 0 AND dateLivraison = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (date == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, date);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfNumeroCommande = CursorUtil.getColumnIndexOrThrow(_cursor, "numeroCommande");
      final int _cursorIndexOfClientNom = CursorUtil.getColumnIndexOrThrow(_cursor, "clientNom");
      final int _cursorIndexOfClientTelephone = CursorUtil.getColumnIndexOrThrow(_cursor, "clientTelephone");
      final int _cursorIndexOfClientAdresse = CursorUtil.getColumnIndexOrThrow(_cursor, "clientAdresse");
      final int _cursorIndexOfClientVille = CursorUtil.getColumnIndexOrThrow(_cursor, "clientVille");
      final int _cursorIndexOfStatut = CursorUtil.getColumnIndexOrThrow(_cursor, "statut");
      final int _cursorIndexOfModePaiement = CursorUtil.getColumnIndexOrThrow(_cursor, "modePaiement");
      final int _cursorIndexOfNbArticles = CursorUtil.getColumnIndexOrThrow(_cursor, "nbArticles");
      final int _cursorIndexOfMontant = CursorUtil.getColumnIndexOrThrow(_cursor, "montant");
      final int _cursorIndexOfDateLivraison = CursorUtil.getColumnIndexOrThrow(_cursor, "dateLivraison");
      final int _cursorIndexOfRemarque = CursorUtil.getColumnIndexOrThrow(_cursor, "remarque");
      final int _cursorIndexOfZone = CursorUtil.getColumnIndexOrThrow(_cursor, "zone");
      final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
      final List<LivraisonLocale> _result = new ArrayList<LivraisonLocale>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final LivraisonLocale _item;
        _item = new LivraisonLocale();
        _item.id = _cursor.getLong(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfNumeroCommande)) {
          _item.numeroCommande = null;
        } else {
          _item.numeroCommande = _cursor.getString(_cursorIndexOfNumeroCommande);
        }
        if (_cursor.isNull(_cursorIndexOfClientNom)) {
          _item.clientNom = null;
        } else {
          _item.clientNom = _cursor.getString(_cursorIndexOfClientNom);
        }
        if (_cursor.isNull(_cursorIndexOfClientTelephone)) {
          _item.clientTelephone = null;
        } else {
          _item.clientTelephone = _cursor.getString(_cursorIndexOfClientTelephone);
        }
        if (_cursor.isNull(_cursorIndexOfClientAdresse)) {
          _item.clientAdresse = null;
        } else {
          _item.clientAdresse = _cursor.getString(_cursorIndexOfClientAdresse);
        }
        if (_cursor.isNull(_cursorIndexOfClientVille)) {
          _item.clientVille = null;
        } else {
          _item.clientVille = _cursor.getString(_cursorIndexOfClientVille);
        }
        if (_cursor.isNull(_cursorIndexOfStatut)) {
          _item.statut = null;
        } else {
          _item.statut = _cursor.getString(_cursorIndexOfStatut);
        }
        if (_cursor.isNull(_cursorIndexOfModePaiement)) {
          _item.modePaiement = null;
        } else {
          _item.modePaiement = _cursor.getString(_cursorIndexOfModePaiement);
        }
        _item.nbArticles = _cursor.getInt(_cursorIndexOfNbArticles);
        _item.montant = _cursor.getDouble(_cursorIndexOfMontant);
        if (_cursor.isNull(_cursorIndexOfDateLivraison)) {
          _item.dateLivraison = null;
        } else {
          _item.dateLivraison = _cursor.getString(_cursorIndexOfDateLivraison);
        }
        if (_cursor.isNull(_cursorIndexOfRemarque)) {
          _item.remarque = null;
        } else {
          _item.remarque = _cursor.getString(_cursorIndexOfRemarque);
        }
        if (_cursor.isNull(_cursorIndexOfZone)) {
          _item.zone = null;
        } else {
          _item.zone = _cursor.getString(_cursorIndexOfZone);
        }
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfSynced);
        _item.synced = _tmp != 0;
        _result.add(_item);
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

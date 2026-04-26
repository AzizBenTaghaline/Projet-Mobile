package com.livraison.supervision.db;

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
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile LivraisonLocaleDao _livraisonLocaleDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `livraison_locale` (`id` INTEGER NOT NULL, `numeroCommande` TEXT, `clientNom` TEXT, `clientTelephone` TEXT, `clientAdresse` TEXT, `clientVille` TEXT, `statut` TEXT, `modePaiement` TEXT, `nbArticles` INTEGER NOT NULL, `montant` REAL NOT NULL, `dateLivraison` TEXT, `remarque` TEXT, `zone` TEXT, `synced` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3da7537147ae1c94e740646cc909dd6a')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `livraison_locale`");
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
        final HashMap<String, TableInfo.Column> _columnsLivraisonLocale = new HashMap<String, TableInfo.Column>(14);
        _columnsLivraisonLocale.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLivraisonLocale.put("numeroCommande", new TableInfo.Column("numeroCommande", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLivraisonLocale.put("clientNom", new TableInfo.Column("clientNom", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLivraisonLocale.put("clientTelephone", new TableInfo.Column("clientTelephone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLivraisonLocale.put("clientAdresse", new TableInfo.Column("clientAdresse", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLivraisonLocale.put("clientVille", new TableInfo.Column("clientVille", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLivraisonLocale.put("statut", new TableInfo.Column("statut", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLivraisonLocale.put("modePaiement", new TableInfo.Column("modePaiement", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLivraisonLocale.put("nbArticles", new TableInfo.Column("nbArticles", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLivraisonLocale.put("montant", new TableInfo.Column("montant", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLivraisonLocale.put("dateLivraison", new TableInfo.Column("dateLivraison", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLivraisonLocale.put("remarque", new TableInfo.Column("remarque", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLivraisonLocale.put("zone", new TableInfo.Column("zone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLivraisonLocale.put("synced", new TableInfo.Column("synced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLivraisonLocale = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLivraisonLocale = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLivraisonLocale = new TableInfo("livraison_locale", _columnsLivraisonLocale, _foreignKeysLivraisonLocale, _indicesLivraisonLocale);
        final TableInfo _existingLivraisonLocale = TableInfo.read(db, "livraison_locale");
        if (!_infoLivraisonLocale.equals(_existingLivraisonLocale)) {
          return new RoomOpenHelper.ValidationResult(false, "livraison_locale(com.livraison.supervision.db.LivraisonLocale).\n"
                  + " Expected:\n" + _infoLivraisonLocale + "\n"
                  + " Found:\n" + _existingLivraisonLocale);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "3da7537147ae1c94e740646cc909dd6a", "9e8d6ddf44d2a4c008d6baf0a7cef856");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "livraison_locale");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `livraison_locale`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
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
    _typeConvertersMap.put(LivraisonLocaleDao.class, LivraisonLocaleDao_Impl.getRequiredConverters());
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
  public LivraisonLocaleDao livraisonLocaleDao() {
    if (_livraisonLocaleDao != null) {
      return _livraisonLocaleDao;
    } else {
      synchronized(this) {
        if(_livraisonLocaleDao == null) {
          _livraisonLocaleDao = new LivraisonLocaleDao_Impl(this);
        }
        return _livraisonLocaleDao;
      }
    }
  }
}

package com.livraison.supervision.db;

import android.content.Context;
import androidx.room.*;
import java.util.List;

// ===== Entité Room (table locale) =====
@Entity(tableName = "livraison_locale")
class LivraisonLocale {
    @PrimaryKey
    public long id;
    public String numeroCommande;
    public String clientNom;
    public String clientTelephone;
    public String clientAdresse;
    public String clientVille;
    public String statut;
    public String modePaiement;
    public int nbArticles;
    public double montant;
    public String dateLivraison;
    public String remarque;
    public String zone;
    public boolean synced = false; // true après sync fin de journée
}

// ===== DAO =====
@Dao
interface LivraisonLocaleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LivraisonLocale> livraisons);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LivraisonLocale livraison);

    @Query("SELECT * FROM livraison_locale WHERE dateLivraison = :date ORDER BY zone ASC")
    List<LivraisonLocale> getByDate(String date);

    @Query("SELECT * FROM livraison_locale WHERE id = :id")
    LivraisonLocale getById(long id);

    @Query("UPDATE livraison_locale SET statut = :statut, remarque = :remarque WHERE id = :id")
    void updateStatut(long id, String statut, String remarque);

    @Query("UPDATE livraison_locale SET synced = 1 WHERE id = :id")
    void markSynced(long id);

    @Query("SELECT * FROM livraison_locale WHERE synced = 0 AND dateLivraison = :date")
    List<LivraisonLocale> getNonSynced(String date);

    @Query("DELETE FROM livraison_locale WHERE dateLivraison != :date")
    void deleteOldData(String date); // garder seulement la journée en cours
}

// ===== Database =====
@Database(entities = {LivraisonLocale.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;

    public abstract LivraisonLocaleDao livraisonLocaleDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "supervision_livraison.db"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return instance;
    }
}

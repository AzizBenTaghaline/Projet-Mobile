package com.livraison.supervision.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "SupervisionSession";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_NOM = "nom";
    private static final String KEY_PRENOM = "prenom";
    private static final String KEY_IDENTIFIANT = "identifiant";
    private static final String KEY_ROLE = "role";

    private static SessionManager instance;
    private final SharedPreferences prefs;

    private SessionManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    public void saveSession(String token, Long userId, String nom,
                            String prenom, String identifiant, String role) {
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putLong(KEY_USER_ID, userId)
                .putString(KEY_NOM, nom)
                .putString(KEY_PRENOM, prenom)
                .putString(KEY_IDENTIFIANT, identifiant)
                .putString(KEY_ROLE, role)
                .apply();
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    public String getToken()       { return prefs.getString(KEY_TOKEN, null); }
    public Long   getUserId()      { return prefs.getLong(KEY_USER_ID, -1L); }
    public String getNom()         { return prefs.getString(KEY_NOM, ""); }
    public String getPrenom()      { return prefs.getString(KEY_PRENOM, ""); }
    public String getIdentifiant() { return prefs.getString(KEY_IDENTIFIANT, ""); }
    public String getRole()        { return prefs.getString(KEY_ROLE, ""); }
    public String getNomComplet()  { return getNom() + " " + getPrenom(); }
    public boolean isControleur()  { return "CONTROLEUR".equals(getRole()); }
    public boolean isLivreur()     { return "LIVREUR".equals(getRole()); }
}

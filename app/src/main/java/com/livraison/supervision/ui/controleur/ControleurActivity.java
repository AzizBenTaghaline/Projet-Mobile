package com.livraison.supervision.ui.controleur;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.livraison.supervision.R;
import com.livraison.supervision.api.RetrofitClient;
import com.livraison.supervision.ui.auth.LoginActivity;
import com.livraison.supervision.utils.SessionManager;

public class ControleurActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controleur);

        // Réinjecter le token au redémarrage
        String token = SessionManager.getInstance(this).getToken();
        if (token != null) RetrofitClient.getInstance().setToken(token);

        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();
            if (id == R.id.nav_dashboard) {
                fragment = new DashboardFragment();
            } else if (id == R.id.nav_livraisons) {
                fragment = new LivraisonsControleurFragment();
            } else if (id == R.id.nav_recherche) {
                fragment = new RechercheFragment();
            } else if (id == R.id.nav_messages) {
                fragment = new MessagesControleurFragment();
            }
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .commit();
                return true;
            }
            return false;
        });

        // Fragment par défaut
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_dashboard);
        }
    }

    public void logout() {
        SessionManager.getInstance(this).clearSession();
        RetrofitClient.getInstance().clearToken();
        startActivity(new Intent(this, LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }
}

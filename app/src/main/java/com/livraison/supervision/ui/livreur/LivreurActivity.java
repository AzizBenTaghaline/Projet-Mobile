package com.livraison.supervision.ui.livreur;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.livraison.supervision.R;
import com.livraison.supervision.api.RetrofitClient;
import com.livraison.supervision.ui.auth.LoginActivity;
import com.livraison.supervision.utils.SessionManager;

public class LivreurActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livreur);

        String token = SessionManager.getInstance(this).getToken();
        if (token != null) RetrofitClient.getInstance().setToken(token);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavLivreur);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();
            if (id == R.id.nav_tournee) {
                fragment = new TourneeFragment();
            } else if (id == R.id.nav_messages_livreur) {
                fragment = new MessagesLivreurFragment();
            }
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerLivreur, fragment)
                        .commit();
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_tournee);
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
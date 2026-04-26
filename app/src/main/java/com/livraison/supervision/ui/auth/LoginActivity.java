package com.livraison.supervision.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.livraison.supervision.R;
import com.livraison.supervision.api.RetrofitClient;
import com.livraison.supervision.model.LoginRequest;
import com.livraison.supervision.model.LoginResponse;
import com.livraison.supervision.ui.controleur.ControleurActivity;
import com.livraison.supervision.ui.livreur.LivreurActivity;
import com.livraison.supervision.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etIdentifiant, etMotDePasse;
    private Button btnConnexion, btnRoleControleur, btnRoleLivreur;
    private ProgressBar progressBar;
    private TextView tvError;
    private String roleSelectionne = "CONTROLEUR"; // défaut

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Si déjà connecté → rediriger directement
        SessionManager session = SessionManager.getInstance(this);
        if (session.isLoggedIn()) {
            redirectByRole(session.getRole());
            return;
        }

        setContentView(R.layout.activity_login);
        initViews();
        setupListeners();
    }

    private void initViews() {
        etIdentifiant    = findViewById(R.id.etIdentifiant);
        etMotDePasse     = findViewById(R.id.etMotDePasse);
        btnConnexion     = findViewById(R.id.btnConnexion);
        btnRoleControleur = findViewById(R.id.btnRoleControleur);
        btnRoleLivreur   = findViewById(R.id.btnRoleLivreur);
        progressBar      = findViewById(R.id.progressBar);
        tvError          = findViewById(R.id.tvError);
    }

    private void setupListeners() {
        // Sélection rôle (visuel seulement, le backend vérifie le vrai rôle)
        btnRoleControleur.setOnClickListener(v -> {
            roleSelectionne = "CONTROLEUR";
            btnRoleControleur.setAlpha(1f);
            btnRoleLivreur.setAlpha(0.5f);
        });

        btnRoleLivreur.setOnClickListener(v -> {
            roleSelectionne = "LIVREUR";
            btnRoleLivreur.setAlpha(1f);
            btnRoleControleur.setAlpha(0.5f);
        });

        btnConnexion.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String identifiant = etIdentifiant.getText() != null
                ? etIdentifiant.getText().toString().trim() : "";
        String motDePasse  = etMotDePasse.getText() != null
                ? etMotDePasse.getText().toString().trim() : "";

        if (identifiant.isEmpty() || motDePasse.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }

        setLoading(true);

        LoginRequest request = new LoginRequest(identifiant, motDePasse);
        RetrofitClient.getInstance().getApi().login(request)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call,
                                           Response<LoginResponse> response) {
                        setLoading(false);
                        if (response.isSuccessful() && response.body() != null) {
                            LoginResponse body = response.body();

                            // Vérifier que le rôle correspond à la sélection
                            if (!body.role.equals(roleSelectionne)) {
                                showError("Ce compte n'est pas un " +
                                        (roleSelectionne.equals("CONTROLEUR")
                                                ? "contrôleur" : "livreur"));
                                return;
                            }

                            // Sauvegarder session
                            SessionManager session = SessionManager.getInstance(LoginActivity.this);
                            session.saveSession(body.token, body.userId,
                                    body.nom, body.prenom, body.identifiant, body.role);

                            // Injecter le token dans Retrofit
                            RetrofitClient.getInstance().setToken(body.token);

                            redirectByRole(body.role);
                        } else {
                            showError("Identifiant ou mot de passe incorrect");
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        setLoading(false);
                        showError("Impossible de joindre le serveur.\nVérifiez votre connexion.");
                    }
                });
    }

    private void redirectByRole(String role) {
        Intent intent;
        if ("CONTROLEUR".equals(role)) {
            intent = new Intent(this, ControleurActivity.class);
        } else {
            intent = new Intent(this, LivreurActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnConnexion.setEnabled(!loading);
        tvError.setVisibility(View.GONE);
    }

    private void showError(String msg) {
        tvError.setText(msg);
        tvError.setVisibility(View.VISIBLE);
    }
}

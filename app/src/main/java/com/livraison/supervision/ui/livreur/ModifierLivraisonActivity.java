package com.livraison.supervision.ui.livreur;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.livraison.supervision.R;
import com.livraison.supervision.api.RetrofitClient;
import com.livraison.supervision.model.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifierLivraisonActivity extends AppCompatActivity {

    private Spinner spinnerStatut;
    private EditText etRemarque, etMessageUrgence;
    private Button btnEnregistrer, btnEnvoyerUrgence;

    private final String[] statutLabels = {"En cours", "Livrée", "Non livrée"};
    private final String[] statutValues = {"EN_COURS", "LIVREE", "ECHOUEE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_livraison);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Modifier " +
                    getIntent().getStringExtra("numeroCommande"));
        }

        Long livraisonId    = getIntent().getLongExtra("livraisonId", -1L);
        Long controleurId   = getIntent().getLongExtra("controleurId", 1L);
        String numeroCommande = getIntent().getStringExtra("numeroCommande");

        spinnerStatut     = findViewById(R.id.spinnerStatut);
        etRemarque        = findViewById(R.id.etRemarque);
        etMessageUrgence  = findViewById(R.id.etMessageUrgence);
        btnEnregistrer    = findViewById(R.id.btnEnregistrer);
        btnEnvoyerUrgence = findViewById(R.id.btnEnvoyerUrgence);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statutLabels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatut.setAdapter(adapter);

        btnEnregistrer.setOnClickListener(v -> {
            int pos = spinnerStatut.getSelectedItemPosition();
            String statut  = statutValues[pos];
            String remarque = etRemarque.getText().toString().trim();

            LivraisonUpdateRequest req = new LivraisonUpdateRequest(livraisonId, statut, remarque);
            RetrofitClient.getInstance().getApi().updateStatut(req)
                    .enqueue(new Callback<LivraisonDTO>() {
                        @Override
                        public void onResponse(Call<LivraisonDTO> call,
                                               Response<LivraisonDTO> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(ModifierLivraisonActivity.this,
                                        "✓ Statut mis à jour", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ModifierLivraisonActivity.this,
                                        "Erreur mise à jour", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override public void onFailure(Call<LivraisonDTO> call, Throwable t) {
                            Toast.makeText(ModifierLivraisonActivity.this,
                                    "Erreur réseau", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btnEnvoyerUrgence.setOnClickListener(v -> {
            String contenuUrgence = etMessageUrgence.getText().toString().trim();
            if (contenuUrgence.isEmpty()) {
                Toast.makeText(this, "Décrivez l'urgence", Toast.LENGTH_SHORT).show();
                return;
            }
            // Le message urgence contient automatiquement le N° commande
            String contenuComplet = "[URGENCE] " + numeroCommande + " : " + contenuUrgence;
            MessageRequest msgReq = new MessageRequest(controleurId, contenuComplet, true, livraisonId);
            RetrofitClient.getInstance().getApi().envoyerMessage(msgReq)
                    .enqueue(new Callback<MessageDTO>() {
                        @Override
                        public void onResponse(Call<MessageDTO> call,
                                               Response<MessageDTO> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(ModifierLivraisonActivity.this,
                                        "🆘 Message envoyé au contrôleur", Toast.LENGTH_SHORT).show();
                                etMessageUrgence.setText("");
                            }
                        }
                        @Override public void onFailure(Call<MessageDTO> call, Throwable t) {
                            Toast.makeText(ModifierLivraisonActivity.this,
                                    "Erreur envoi", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @Override public boolean onSupportNavigateUp() { finish(); return true; }
}

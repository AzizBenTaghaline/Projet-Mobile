package com.livraison.supervision.ui.livreur;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.livraison.supervision.R;
import com.livraison.supervision.api.RetrofitClient;
import com.livraison.supervision.model.LivraisonDTO;
import com.livraison.supervision.utils.StatutHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailLivraisonLivreurActivity extends AppCompatActivity {

    private LivraisonDTO livraison;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_livreur);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Long id = getIntent().getLongExtra("livraisonId", -1L);
        String numero = getIntent().getStringExtra("numeroCommande");
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(numero);

        TextView tvStatut = findViewById(R.id.tvStatutDetail);
        Button btnAppeler = findViewById(R.id.btnAppeler);
        Button btnModifier = findViewById(R.id.btnModifier);

        RetrofitClient.getInstance().getApi().getLivraisonById(id)
                .enqueue(new Callback<LivraisonDTO>() {
                    @Override
                    public void onResponse(Call<LivraisonDTO> call,
                                           Response<LivraisonDTO> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            livraison = response.body();
                            StatutHelper.applyBadge(tvStatut, livraison.statut);

                            setRow(R.id.rowClient,   "Client",      livraison.client != null ? livraison.client.nom : "-");
                            setRow(R.id.rowTel,      "Téléphone",   livraison.client != null ? livraison.client.telephone : "-");
                            setRow(R.id.rowAdresse,  "Adresse",     livraison.client != null ? livraison.client.adresse + ", " + livraison.client.ville : "-");
                            setRow(R.id.rowPaiement, "Paiement",    livraison.modePaiement);
                            setRow(R.id.rowMontant,  "Montant",     livraison.montant + " TND");
                            setRow(R.id.rowArticles, "Nb articles", livraison.nbArticles != null ? String.valueOf(livraison.nbArticles) : "-");

                            btnAppeler.setOnClickListener(v -> {
                                if (livraison.client != null && livraison.client.telephone != null) {
                                    startActivity(new Intent(Intent.ACTION_DIAL,
                                            Uri.parse("tel:" + livraison.client.telephone)));
                                }
                            });
                        }
                    }
                    @Override public void onFailure(Call<LivraisonDTO> call, Throwable t) {}
                });

        btnModifier.setOnClickListener(v -> {
            if (livraison != null) {
                Intent i = new Intent(this, ModifierLivraisonActivity.class);
                i.putExtra("livraisonId", livraison.id);
                i.putExtra("numeroCommande", livraison.numeroCommande);
                i.putExtra("controleurId", 9L); // ID contrôleur (Amira Ben Ali)
                startActivity(i);
            }
        });
    }

    private void setRow(int rowId, String label, String value) {
        View row = findViewById(rowId);
        if (row == null) return;
        ((TextView) row.findViewById(R.id.tvLabel)).setText(label);
        ((TextView) row.findViewById(R.id.tvValue)).setText(value != null ? value : "-");
    }

    @Override public boolean onSupportNavigateUp() { finish(); return true; }
}

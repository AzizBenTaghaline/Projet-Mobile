package com.livraison.supervision.ui.controleur;

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

public class DetailLivraisonActivity extends AppCompatActivity {

    private TextView tvStatut;
    private ProgressBar progressBar;
    // Lignes de détail
    private TextView tvClientVal, tvTelVal, tvAdresseVal, tvVilleVal,
            tvPaiementVal, tvArticlesVal, tvMontantVal, tvLivreurVal, tvRemarqueVal;
    private TextView tvClientLbl, tvTelLbl, tvAdresseLbl, tvVilleLbl,
            tvPaiementLbl, tvArticlesLbl, tvMontantLbl, tvLivreurLbl, tvRemarqueLbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_livraison);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Détail commande");
        }

        tvStatut    = findViewById(R.id.tvStatutDetail);
        progressBar = findViewById(R.id.progressBar);

        // Récupérer les TextViews des rows
        initRows();

        Long id = getIntent().getLongExtra("livraisonId", -1L);
        if (id != -1L) loadDetail(id);
    }

    private void initRows() {
        View rowClient   = findViewById(R.id.rowClient);
        View rowTel      = findViewById(R.id.rowTel);
        View rowAdresse  = findViewById(R.id.rowAdresse);
        View rowVille    = findViewById(R.id.rowVille);
        View rowPaiement = findViewById(R.id.rowPaiement);
        View rowArticles = findViewById(R.id.rowArticles);
        View rowMontant  = findViewById(R.id.rowMontant);
        View rowLivreur  = findViewById(R.id.rowLivreur);
        View rowRemarque = findViewById(R.id.rowRemarque);

        setRow(rowClient,   "Client",       null);
        setRow(rowTel,      "Téléphone",    null);
        setRow(rowAdresse,  "Adresse",      null);
        setRow(rowVille,    "Ville",        null);
        setRow(rowPaiement, "Paiement",     null);
        setRow(rowArticles, "Nb articles",  null);
        setRow(rowMontant,  "Montant",      null);
        setRow(rowLivreur,  "Livreur",      null);
        setRow(rowRemarque, "Remarque",     null);

        tvClientVal   = rowClient.findViewById(R.id.tvValue);
        tvTelVal      = rowTel.findViewById(R.id.tvValue);
        tvAdresseVal  = rowAdresse.findViewById(R.id.tvValue);
        tvVilleVal    = rowVille.findViewById(R.id.tvValue);
        tvPaiementVal = rowPaiement.findViewById(R.id.tvValue);
        tvArticlesVal = rowArticles.findViewById(R.id.tvValue);
        tvMontantVal  = rowMontant.findViewById(R.id.tvValue);
        tvLivreurVal  = rowLivreur.findViewById(R.id.tvValue);
        tvRemarqueVal = rowRemarque.findViewById(R.id.tvValue);
    }

    private void setRow(View row, String label, String value) {
        ((TextView) row.findViewById(R.id.tvLabel)).setText(label);
        if (value != null) ((TextView) row.findViewById(R.id.tvValue)).setText(value);
    }

    private void loadDetail(Long id) {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getInstance().getApi().getLivraisonById(id)
                .enqueue(new Callback<LivraisonDTO>() {
                    @Override
                    public void onResponse(Call<LivraisonDTO> call,
                                           Response<LivraisonDTO> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            populate(response.body());
                        }
                    }
                    @Override
                    public void onFailure(Call<LivraisonDTO> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(DetailLivraisonActivity.this,
                                "Erreur réseau", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populate(LivraisonDTO l) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(l.numeroCommande);

        StatutHelper.applyBadge(tvStatut, l.statut);
        tvClientVal.setText(l.client != null ? l.client.nom : "-");
        tvTelVal.setText(l.client != null ? l.client.telephone : "-");
        tvAdresseVal.setText(l.client != null ? l.client.adresse : "-");
        tvVilleVal.setText(l.client != null ? l.client.ville : "-");
        tvPaiementVal.setText(l.modePaiement != null ? l.modePaiement : "-");
        tvArticlesVal.setText(l.nbArticles != null ? String.valueOf(l.nbArticles) : "-");
        tvMontantVal.setText(l.montant != null ? l.montant + " TND" : "-");
        tvLivreurVal.setText(l.livreur != null ? l.livreur.getNomComplet() : "-");
        tvRemarqueVal.setText(l.remarque != null ? l.remarque : "-");
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}

package com.livraison.supervision.ui.controleur;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import com.livraison.supervision.R;
import com.livraison.supervision.api.RetrofitClient;
import com.livraison.supervision.model.*;
import com.livraison.supervision.utils.StatutHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.*;

public class RechercheFragment extends Fragment {

    private EditText etDateDebut, etDateFin, etClient, etNumero;
    private Spinner spinnerLivreur;
    private Button btnRechercher, btnReset;
    private RecyclerView rvResultats;
    private TextView tvResultCount;
    private LivraisonsControleurFragment.LivraisonAdapter adapter;
    private List<UtilisateurDTO> livreurs = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recherche, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etDateDebut    = view.findViewById(R.id.etDateDebut);
        etDateFin      = view.findViewById(R.id.etDateFin);
        etClient       = view.findViewById(R.id.etClient);
        etNumero       = view.findViewById(R.id.etNumero);
        spinnerLivreur = view.findViewById(R.id.spinnerLivreur);
        btnRechercher  = view.findViewById(R.id.btnRechercher);
        btnReset       = view.findViewById(R.id.btnReset);
        rvResultats    = view.findViewById(R.id.rvResultats);
        tvResultCount  = view.findViewById(R.id.tvResultCount);

        rvResultats.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LivraisonsControleurFragment.LivraisonAdapter(new ArrayList<>(), l -> {
            Intent intent = new Intent(getContext(), DetailLivraisonActivity.class);
            intent.putExtra("livraisonId", l.id);
            startActivity(intent);
        });
        rvResultats.setAdapter(adapter);

        btnRechercher.setOnClickListener(v -> doRecherche());
        btnReset.setOnClickListener(v -> resetForm());
    }

    private void doRecherche() {
        RechercheRequest req = new RechercheRequest();
        req.dateDebut = etDateDebut.getText().toString().trim().isEmpty() ? null
                : etDateDebut.getText().toString().trim();
        req.dateFin   = etDateFin.getText().toString().trim().isEmpty() ? null
                : etDateFin.getText().toString().trim();
        req.nomClient     = etClient.getText().toString().trim().isEmpty() ? null
                : etClient.getText().toString().trim();
        req.numeroCommande = etNumero.getText().toString().trim().isEmpty() ? null
                : etNumero.getText().toString().trim();

        RetrofitClient.getInstance().getApi().recherche(req)
                .enqueue(new Callback<List<LivraisonDTO>>() {
                    @Override
                    public void onResponse(Call<List<LivraisonDTO>> call,
                                           Response<List<LivraisonDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<LivraisonDTO> res = response.body();
                            tvResultCount.setText("Résultats (" + res.size() + ")");
                            adapter.setData(res);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<LivraisonDTO>> call, Throwable t) {
                        Toast.makeText(getContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void resetForm() {
        etDateDebut.setText("");
        etDateFin.setText("");
        etClient.setText("");
        etNumero.setText("");
        tvResultCount.setText("Résultats");
        adapter.setData(new ArrayList<>());
    }
}

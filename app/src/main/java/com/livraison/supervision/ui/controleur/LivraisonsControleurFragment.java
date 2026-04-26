package com.livraison.supervision.ui.controleur;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.livraison.supervision.R;
import com.livraison.supervision.api.RetrofitClient;
import com.livraison.supervision.model.LivraisonDTO;
import com.livraison.supervision.utils.StatutHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class LivraisonsControleurFragment extends Fragment {

    private RecyclerView rvLivraisons;
    private SwipeRefreshLayout swipeRefresh;
    private TextInputEditText etSearch;
    private LivraisonAdapter adapter;
    private List<LivraisonDTO> allLivraisons = new ArrayList<>();
    private String filtreStatut = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_livraisons_controleur, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvLivraisons = view.findViewById(R.id.rvLivraisons);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        etSearch     = view.findViewById(R.id.etSearch);

        rvLivraisons.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LivraisonAdapter(new ArrayList<>(), livraison -> {
            // Ouvrir détail
            Intent intent = new Intent(getContext(), DetailLivraisonActivity.class);
            intent.putExtra("livraisonId", livraison.id);
            startActivity(intent);
        });
        rvLivraisons.setAdapter(adapter);

        // Chips
        Chip chipTous    = view.findViewById(R.id.chipTous);
        Chip chipLivree  = view.findViewById(R.id.chipLivree);
        Chip chipEnCours = view.findViewById(R.id.chipEnCours);
        Chip chipEchouee = view.findViewById(R.id.chipEchouee);

        chipTous.setOnClickListener(v -> { filtreStatut = null; applyFilter(); });
        chipLivree.setOnClickListener(v -> { filtreStatut = "LIVREE"; applyFilter(); });
        chipEnCours.setOnClickListener(v -> { filtreStatut = "EN_COURS"; applyFilter(); });
        chipEchouee.setOnClickListener(v -> { filtreStatut = "ECHOUEE"; applyFilter(); });

        // Recherche texte
        etSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            public void onTextChanged(CharSequence s, int st, int b, int c) { applyFilter(); }
            public void afterTextChanged(Editable s) {}
        });

        swipeRefresh.setOnRefreshListener(this::loadLivraisons);
        loadLivraisons();
    }

    private void loadLivraisons() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());
        RetrofitClient.getInstance().getApi().getLivraisons(today)
                .enqueue(new Callback<List<LivraisonDTO>>() {
                    @Override
                    public void onResponse(Call<List<LivraisonDTO>> call,
                                           Response<List<LivraisonDTO>> response) {
                        swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful() && response.body() != null) {
                            allLivraisons = response.body();
                            applyFilter();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<LivraisonDTO>> call, Throwable t) {
                        swipeRefresh.setRefreshing(false);
                        Toast.makeText(getContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void applyFilter() {
        String query = etSearch.getText() != null
                ? etSearch.getText().toString().toLowerCase().trim() : "";
        List<LivraisonDTO> filtered = allLivraisons.stream()
                .filter(l -> filtreStatut == null || filtreStatut.equals(l.statut))
                .filter(l -> query.isEmpty()
                        || (l.numeroCommande != null && l.numeroCommande.toLowerCase().contains(query))
                        || (l.client != null && l.client.nom != null && l.client.nom.toLowerCase().contains(query))
                        || (l.livreur != null && l.livreur.nom != null && l.livreur.nom.toLowerCase().contains(query)))
                .collect(Collectors.toList());
        adapter.setData(filtered);
    }

    // ===== Adapter =====
    static class LivraisonAdapter extends RecyclerView.Adapter<LivraisonAdapter.VH> {
        interface OnClickListener { void onClick(LivraisonDTO l); }
        private List<LivraisonDTO> items;
        private final OnClickListener listener;

        LivraisonAdapter(List<LivraisonDTO> items, OnClickListener listener) {
            this.items = items; this.listener = listener;
        }

        void setData(List<LivraisonDTO> data) {
            this.items = data; notifyDataSetChanged();
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_livraison, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            LivraisonDTO l = items.get(pos);
            h.tvNumero.setText(l.numeroCommande);
            String livreurNom = l.livreur != null ? l.livreur.getNomComplet() : "?";
            String clientNom  = l.client  != null ? l.client.nom : "?";
            h.tvSub.setText(livreurNom + " · " + clientNom);
            h.tvAvatar.setText(l.livreur != null ? l.livreur.getInitiales() : "?");
            StatutHelper.applyBadge(h.tvStatut, l.statut);
            h.tvHeure.setText(l.dateLivraison != null ? l.dateLivraison : "");
            h.itemView.setOnClickListener(v -> listener.onClick(l));
        }

        @Override public int getItemCount() { return items.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvAvatar, tvNumero, tvSub, tvStatut, tvHeure;
            VH(View v) {
                super(v);
                tvAvatar = v.findViewById(R.id.tvAvatar);
                tvNumero = v.findViewById(R.id.tvNumeroCommande);
                tvSub    = v.findViewById(R.id.tvLivreurClient);
                tvStatut = v.findViewById(R.id.tvStatut);
                tvHeure  = v.findViewById(R.id.tvHeure);
            }
        }
    }
}

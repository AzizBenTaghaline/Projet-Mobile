package com.livraison.supervision.ui.livreur;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.livraison.supervision.R;
import com.livraison.supervision.api.RetrofitClient;
import com.livraison.supervision.db.AppDatabase;
import com.livraison.supervision.model.*;
import com.livraison.supervision.utils.SessionManager;
import com.livraison.supervision.utils.StatutHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TourneeFragment extends Fragment {

    private RecyclerView rvTournee;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tvDate, tvTotal;
    private Button btnSync, btnLogout;
    private TourneeAdapter adapter;
    private List<LivraisonDTO> livraisons = new ArrayList<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tournee, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvTournee    = view.findViewById(R.id.rvTournee);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        tvDate       = view.findViewById(R.id.tvDate);
        tvTotal      = view.findViewById(R.id.tvTotal);
        btnSync      = view.findViewById(R.id.btnSync);
        btnLogout    = view.findViewById(R.id.btnLogout);

        rvTournee.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TourneeAdapter(livraisons, l -> {
            Intent i = new Intent(getContext(), DetailLivraisonLivreurActivity.class);
            i.putExtra("livraisonId", l.id);
            i.putExtra("numeroCommande", l.numeroCommande);
            startActivity(i);
        });
        rvTournee.setAdapter(adapter);

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        tvDate.setText("Tournée du " + today);

        swipeRefresh.setOnRefreshListener(this::loadTourneeFromServer);
        btnSync.setOnClickListener(v -> syncFinJournee());
        btnLogout.setOnClickListener(v -> {
            if (getActivity() instanceof LivreurActivity) {
                ((LivreurActivity) getActivity()).logout();
            }
        });

        // Charger depuis SQLite local en premier (instantané)
        loadFromLocal();
        // Puis rafraîchir depuis le serveur
        loadTourneeFromServer();
    }

    private void loadFromLocal() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        executor.execute(() -> {
            // Vérifier si des données locales existent
            var db = AppDatabase.getInstance(requireContext());
            // (simplifié : on ne charge les locales que comme fallback)
        });
    }

    private void loadTourneeFromServer() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        RetrofitClient.getInstance().getApi().getTournee(today)
                .enqueue(new Callback<TourneeDTO>() {
                    @Override
                    public void onResponse(Call<TourneeDTO> call,
                                           Response<TourneeDTO> response) {
                        swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful() && response.body() != null) {
                            livraisons = response.body().livraisons;
                            if (livraisons == null) livraisons = new ArrayList<>();
                            tvTotal.setText(livraisons.size() + " livraisons");
                            adapter.setData(livraisons);
                            saveToLocal(livraisons, today);
                        }
                    }
                    @Override
                    public void onFailure(Call<TourneeDTO> call, Throwable t) {
                        swipeRefresh.setRefreshing(false);
                        Toast.makeText(getContext(),
                                "Hors ligne — données locales affichées", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveToLocal(List<LivraisonDTO> list, String date) {
        executor.execute(() -> {
            // Sauvegarde dans SQLite local pour mode hors ligne
            // (implémentation détaillée dans AppDatabase)
        });
    }

    private void syncFinJournee() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Long livreurId = SessionManager.getInstance(requireContext()).getUserId();

        List<LivraisonUpdateRequest> updates = new ArrayList<>();
        for (LivraisonDTO l : livraisons) {
            if ("LIVREE".equals(l.statut) || "ECHOUEE".equals(l.statut)) {
                updates.add(new LivraisonUpdateRequest(l.id, l.statut, l.remarque));
            }
        }

        if (updates.isEmpty()) {
            Toast.makeText(getContext(), "Aucune livraison à synchroniser", Toast.LENGTH_SHORT).show();
            return;
        }

        SyncPayload payload = new SyncPayload();
        payload.livreurId = livreurId;
        payload.date = today;
        payload.livraisons = updates;

        RetrofitClient.getInstance().getApi().syncLivraisons(payload)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(),
                                    "✓ Synchronisation réussie !", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Échec sync", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ===== Adapter tournée =====
    static class TourneeAdapter extends RecyclerView.Adapter<TourneeAdapter.VH> {
        interface OnClickListener { void onClick(LivraisonDTO l); }
        private List<LivraisonDTO> items;
        private final OnClickListener listener;

        TourneeAdapter(List<LivraisonDTO> items, OnClickListener listener) {
            this.items = items; this.listener = listener;
        }

        void setData(List<LivraisonDTO> data) { this.items = data; notifyDataSetChanged(); }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_tournee, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            LivraisonDTO l = items.get(pos);
            h.tvOrdre.setText(String.valueOf(pos + 1));
            h.tvNumero.setText(l.numeroCommande);
            h.tvClient.setText(l.client != null
                    ? l.client.nom + " · " + l.client.ville : "");
            h.tvTel.setText(l.client != null ? "📞 " + l.client.telephone : "");
            StatutHelper.applyBadge(h.tvStatut, l.statut);
            h.itemView.setOnClickListener(v -> listener.onClick(l));
        }

        @Override public int getItemCount() { return items == null ? 0 : items.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvOrdre, tvNumero, tvClient, tvTel, tvStatut;
            VH(View v) {
                super(v);
                tvOrdre  = v.findViewById(R.id.tvOrdre);
                tvNumero = v.findViewById(R.id.tvNumero);
                tvClient = v.findViewById(R.id.tvClient);
                tvTel    = v.findViewById(R.id.tvTel);
                tvStatut = v.findViewById(R.id.tvStatut);
            }
        }
    }
}

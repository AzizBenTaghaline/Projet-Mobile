package com.livraison.supervision.ui.controleur;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.livraison.supervision.R;
import com.livraison.supervision.api.RetrofitClient;
import com.livraison.supervision.model.DashboardDTO;
import com.livraison.supervision.model.StatLivreurDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.SimpleDateFormat;
import java.util.*;

public class DashboardFragment extends Fragment {

    private TextView tvDate, tvTotal, tvLivrees, tvEnCours, tvEchouees;
    private RecyclerView rvLivreurs;
    private SwipeRefreshLayout swipeRefresh;
    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvDate       = view.findViewById(R.id.tvDate);
        tvTotal      = view.findViewById(R.id.tvTotal);
        tvLivrees    = view.findViewById(R.id.tvLivrees);
        tvEnCours    = view.findViewById(R.id.tvEnCours);
        tvEchouees   = view.findViewById(R.id.tvEchouees);
        rvLivreurs   = view.findViewById(R.id.rvLivreurs);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        btnLogout    = view.findViewById(R.id.btnLogout);

        rvLivreurs.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefresh.setOnRefreshListener(this::loadDashboard);

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                if (getActivity() instanceof ControleurActivity) {
                    ((ControleurActivity) getActivity()).logout();
                }
            });
        }

        loadDashboard();
    }

    private void loadDashboard() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());
        tvDate.setText("Aujourd'hui — " + today);

        RetrofitClient.getInstance().getApi().getDashboard(today)
                .enqueue(new Callback<DashboardDTO>() {
                    @Override
                    public void onResponse(Call<DashboardDTO> call,
                                           Response<DashboardDTO> response) {
                        swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful() && response.body() != null) {
                            updateUI(response.body());
                        } else {
                            showError("Erreur chargement dashboard");
                        }
                    }

                    @Override
                    public void onFailure(Call<DashboardDTO> call, Throwable t) {
                        swipeRefresh.setRefreshing(false);
                        showError("Serveur inaccessible");
                    }
                });
    }

    private void updateUI(DashboardDTO data) {
        tvTotal.setText(String.valueOf(data.totalLivraisons));
        tvLivrees.setText(String.valueOf(data.livrees));
        tvEnCours.setText(String.valueOf(data.enCours));
        tvEchouees.setText(String.valueOf(data.echouees));

        if (data.statsParLivreur != null && !data.statsParLivreur.isEmpty()) {
            rvLivreurs.setAdapter(new StatLivreurAdapter(data.statsParLivreur));
        }
    }

    private void showError(String msg) {
        if (getContext() != null)
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    // ===== Adapter corrigé =====
    static class StatLivreurAdapter extends RecyclerView.Adapter<StatLivreurAdapter.VH> {

        private final List<StatLivreurDTO> items;
        private final int maxTotal;

        StatLivreurAdapter(List<StatLivreurDTO> items) {
            this.items = items;
            int max = 1;
            for (StatLivreurDTO s : items) {
                if (s.total > max) max = s.total;
            }
            this.maxTotal = max;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_stat_livreur, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            StatLivreurDTO s = items.get(pos);
            h.tvNom.setText(s.nomComplet);
            h.tvCount.setText(String.valueOf(s.total));

            // ✅ Correction du crash : utiliser post() + FrameLayout.LayoutParams
            h.barFill.post(() -> {
                int trackWidth = h.barTrack.getWidth();
                if (trackWidth > 0 && maxTotal > 0) {
                    int fillWidth = (int) ((float) s.total / maxTotal * trackWidth);
                    android.widget.FrameLayout.LayoutParams params =
                            new android.widget.FrameLayout.LayoutParams(
                                    fillWidth,
                                    android.widget.FrameLayout.LayoutParams.MATCH_PARENT
                            );
                    h.barFill.setLayoutParams(params);
                }
            });
        }

        @Override
        public int getItemCount() { return items.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvNom, tvCount;
            View barTrack, barFill;

            VH(View v) {
                super(v);
                tvNom    = v.findViewById(R.id.tvNom);
                tvCount  = v.findViewById(R.id.tvCount);
                barTrack = v.findViewById(R.id.barTrack);
                barFill  = v.findViewById(R.id.barFill);
            }
        }
    }
}
package com.livraison.supervision.ui.controleur;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import com.livraison.supervision.R;
import com.livraison.supervision.api.RetrofitClient;
import com.livraison.supervision.model.*;
import com.livraison.supervision.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.*;

public class MessagesControleurFragment extends Fragment {

    private RecyclerView rvMessages;
    private EditText etMessage;
    private Button btnEnvoyer;
    private LinearLayout layoutUrgents;
    private MessageAdapter adapter;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable pollRunnable;
    private Long selectedLivreurId = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages_controleur, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMessages    = view.findViewById(R.id.rvMessages);
        etMessage     = view.findViewById(R.id.etMessage);
        btnEnvoyer    = view.findViewById(R.id.btnEnvoyer);
        layoutUrgents = view.findViewById(R.id.layoutUrgents);

        rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MessageAdapter(new ArrayList<>(),
                SessionManager.getInstance(getContext()).getUserId());
        rvMessages.setAdapter(adapter);

        btnEnvoyer.setOnClickListener(v -> envoyerMessage());

        // Bouton pour revenir à la liste des urgents
        layoutUrgents.setOnLongClickListener(v -> {
            selectedLivreurId = null;
            adapter.setData(new ArrayList<>());
            loadMessages();
            return true;
        });

        loadMessages();
        startPolling();
    }

    private void loadMessages() {
        // Si un livreur est sélectionné, afficher la conversation avec lui
        // Sinon afficher les messages d'urgence
        if (selectedLivreurId != null) {
            RetrofitClient.getInstance().getApi().getConversation(selectedLivreurId)
                    .enqueue(new Callback<List<MessageDTO>>() {
                        @Override
                        public void onResponse(Call<List<MessageDTO>> call,
                                               Response<List<MessageDTO>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                adapter.setData(response.body());
                                if (adapter.getItemCount() > 0) {
                                    rvMessages.scrollToPosition(adapter.getItemCount() - 1);
                                }
                            }
                        }
                        @Override public void onFailure(Call<List<MessageDTO>> call, Throwable t) {}
                    });
        } else {
            // Charger les messages urgents pour sélectionner un livreur
            RetrofitClient.getInstance().getApi().getUrgents()
                    .enqueue(new Callback<List<MessageDTO>>() {
                        @Override
                        public void onResponse(Call<List<MessageDTO>> call,
                                               Response<List<MessageDTO>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                showUrgents(response.body());
                            }
                        }
                        @Override public void onFailure(Call<List<MessageDTO>> call, Throwable t) {}
                    });
        }
    }

    private void showUrgents(List<MessageDTO> urgents) {
        if (layoutUrgents == null) return;
        layoutUrgents.removeAllViews();
        for (MessageDTO m : urgents) {
            TextView tv = new TextView(getContext());
            tv.setText("⚠ " + (m.expediteur != null ? m.expediteur.getNomComplet() : "?")
                    + " — " + (m.numeroCommande != null ? m.numeroCommande : "")
                    + " — " + m.contenu);
            tv.setTextColor(Color.parseColor("#A32D2D"));
            tv.setBackgroundColor(Color.parseColor("#FCEBEB"));
            tv.setPadding(16, 10, 16, 10);
            tv.setTextSize(12f);
            if (m.expediteur != null) {
                Long livreurId = m.expediteur.id;
                tv.setOnClickListener(v -> {
                    selectedLivreurId = livreurId;
                    loadMessages(); // Charger la conversation avec ce livreur
                });
            }
            layoutUrgents.addView(tv);
        }
        layoutUrgents.setVisibility(urgents.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void envoyerMessage() {
        String contenu = etMessage.getText().toString().trim();
        if (contenu.isEmpty() || selectedLivreurId == null) {
            Toast.makeText(getContext(),
                    "Appuyez sur une urgence pour selectionner le livreur",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        MessageRequest req = new MessageRequest(selectedLivreurId, contenu, false, null);
        RetrofitClient.getInstance().getApi().envoyerMessage(req)
                .enqueue(new Callback<MessageDTO>() {
                    @Override
                    public void onResponse(Call<MessageDTO> call, Response<MessageDTO> response) {
                        if (response.isSuccessful()) {
                            etMessage.setText("");
                            loadMessages();
                        }
                    }
                    @Override public void onFailure(Call<MessageDTO> call, Throwable t) {}
                });
    }

    private void startPolling() {
        pollRunnable = () -> {
            loadMessages();
            handler.postDelayed(pollRunnable, 15000);
        };
        handler.postDelayed(pollRunnable, 15000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(pollRunnable);
    }
}
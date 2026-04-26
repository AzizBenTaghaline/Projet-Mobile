package com.livraison.supervision.ui.livreur;

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
import com.livraison.supervision.ui.controleur.MessageAdapter;
import com.livraison.supervision.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.*;

public class MessagesLivreurFragment extends Fragment {

    private RecyclerView rvMessages;
    private EditText etMessage;
    private Button btnEnvoyer;
    private MessageAdapter adapter;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable pollRunnable;
    private Long controleurId = 9L;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages_livreur, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMessages = view.findViewById(R.id.rvMessages);
        etMessage  = view.findViewById(R.id.etMessage);
        btnEnvoyer = view.findViewById(R.id.btnEnvoyer);

        rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MessageAdapter(new ArrayList<>(),
                SessionManager.getInstance(getContext()).getUserId());
        rvMessages.setAdapter(adapter);

        btnEnvoyer.setOnClickListener(v -> envoyerReponse());
        loadMessages();
        startPolling();
    }

    private void loadMessages() {
        RetrofitClient.getInstance().getApi().getMessagesRecus()
                .enqueue(new Callback<List<MessageDTO>>() {
                    @Override
                    public void onResponse(Call<List<MessageDTO>> call,
                                           Response<List<MessageDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            adapter.setData(response.body());
                            rvMessages.scrollToPosition(adapter.getItemCount() - 1);
                        }
                    }
                    @Override public void onFailure(Call<List<MessageDTO>> call, Throwable t) {}
                });
    }

    private void envoyerReponse() {
        String contenu = etMessage.getText().toString().trim();
        if (contenu.isEmpty()) return;
        MessageRequest req = new MessageRequest(controleurId, contenu, false, null);
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
package com.livraison.supervision.ui.controleur;

import android.graphics.Color;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.*;
import com.livraison.supervision.R;
import com.livraison.supervision.model.MessageDTO;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.VH> {

    private List<MessageDTO> items;
    private final Long myId;

    public MessageAdapter(List<MessageDTO> items, Long myId) {
        this.items = items;
        this.myId = myId;
    }

    public void setData(List<MessageDTO> data) {
        this.items = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        MessageDTO m = items.get(pos);
        boolean isMine = m.expediteur != null && myId.equals(m.expediteur.id);
        h.tvContenu.setText(m.contenu);
        h.tvInfo.setText(
                (m.expediteur != null ? m.expediteur.getNomComplet() : "?")
                        + " · "
                        + (m.dateEnvoi != null && m.dateEnvoi.length() >= 16
                        ? m.dateEnvoi.substring(11, 16) : ""));

        if (Boolean.TRUE.equals(m.estUrgence)) {
            h.tvContenu.setBackgroundColor(Color.parseColor("#FCEBEB"));
            h.tvContenu.setTextColor(Color.parseColor("#A32D2D"));
        } else if (isMine) {
            h.tvContenu.setBackgroundColor(Color.parseColor("#185FA5"));
            h.tvContenu.setTextColor(Color.WHITE);
        } else {
            h.tvContenu.setBackgroundColor(Color.parseColor("#F5F5F2"));
            h.tvContenu.setTextColor(Color.parseColor("#1A1A18"));
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                h.tvContenu.getLayoutParams();
        params.gravity = isMine ? android.view.Gravity.END : android.view.Gravity.START;
        h.tvContenu.setLayoutParams(params);
        h.tvInfo.setGravity(isMine ? android.view.Gravity.END : android.view.Gravity.START);
    }

    @Override
    public int getItemCount() { return items == null ? 0 : items.size(); }

    public static class VH extends RecyclerView.ViewHolder {
        TextView tvContenu, tvInfo;
        public VH(View v) {
            super(v);
            tvContenu = v.findViewById(R.id.tvContenu);
            tvInfo    = v.findViewById(R.id.tvInfo);
        }
    }
}
package com.livraison.supervision.utils;

import android.content.Context;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.livraison.supervision.R;

public class StatutHelper {

    public static void applyBadge(TextView tv, String statut) {
        if (statut == null) return;
        Context ctx = tv.getContext();
        int bg, fg;
        String label;

        switch (statut) {
            case "LIVREE":
                bg = R.color.badge_livree_bg;
                fg = R.color.badge_livree_text;
                label = "Livrée";
                break;
            case "EN_COURS":
                bg = R.color.badge_encours_bg;
                fg = R.color.badge_encours_text;
                label = "En cours";
                break;
            case "ECHOUEE":
                bg = R.color.badge_echouee_bg;
                fg = R.color.badge_echouee_text;
                label = "Échouée";
                break;
            default: // EN_ATTENTE
                bg = R.color.badge_attente_bg;
                fg = R.color.badge_attente_text;
                label = "En attente";
                break;
        }

        tv.setText(label);
        tv.setBackgroundColor(ContextCompat.getColor(ctx, bg));
        tv.setTextColor(ContextCompat.getColor(ctx, fg));
    }
}

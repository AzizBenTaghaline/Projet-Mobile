package com.livraison.supervision.model;

import java.math.BigDecimal;

public class LivraisonDTO {
    public Long id;
    public String numeroCommande;
    public ClientDTO client;
    public UtilisateurDTO livreur;
    public String statut; // "EN_ATTENTE","EN_COURS","LIVREE","ECHOUEE"
    public String modePaiement;
    public Integer nbArticles;
    public BigDecimal montant;
    public String dateLivraison;
    public String remarque;
    public String zone;

    public String getStatutLabel() {
        if (statut == null) return "Inconnu";
        switch (statut) {
            case "LIVREE":    return "Livrée";
            case "EN_COURS":  return "En cours";
            case "ECHOUEE":   return "Échouée";
            case "EN_ATTENTE": return "En attente";
            default: return statut;
        }
    }
}

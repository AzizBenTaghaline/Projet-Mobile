package com.livraison.supervision.model;
public class LivraisonUpdateRequest {
    public Long livraisonId;
    public String statut;
    public String remarque;
    public LivraisonUpdateRequest(Long livraisonId, String statut, String remarque) {
        this.livraisonId = livraisonId;
        this.statut = statut;
        this.remarque = remarque;
    }
}

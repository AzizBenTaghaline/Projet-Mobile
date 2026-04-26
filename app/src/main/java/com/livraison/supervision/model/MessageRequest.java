package com.livraison.supervision.model;
public class MessageRequest {
    public Long destinataireId;
    public String contenu;
    public Boolean estUrgence;
    public Long livraisonId;
    public MessageRequest(Long destinataireId, String contenu, Boolean estUrgence, Long livraisonId) {
        this.destinataireId = destinataireId;
        this.contenu = contenu;
        this.estUrgence = estUrgence;
        this.livraisonId = livraisonId;
    }
}

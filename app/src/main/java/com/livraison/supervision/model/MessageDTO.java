package com.livraison.supervision.model;

import java.time.LocalDateTime;

public class MessageDTO {
    public Long id;
    public UtilisateurDTO expediteur;
    public UtilisateurDTO destinataire;
    public String contenu;
    public LocalDateTime dateEnvoi;
    public Boolean estUrgence;
    public Long livraisonId;
    public String numeroCommande;
    public Boolean lu;
}

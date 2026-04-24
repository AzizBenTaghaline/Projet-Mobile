package com.livraison.supervision.dto;
import com.livraison.supervision.entity.enums.Statut;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class LivraisonDTO {
    public Long id;
    public String numeroCommande;
    public ClientDTO client;
    public UtilisateurDTO livreur;
    public Statut statut;
    public String modePaiement;
    public Integer nbArticles;
    public BigDecimal montant;
    public LocalDate dateLivraison;
    public String remarque;
    public String zone;
}

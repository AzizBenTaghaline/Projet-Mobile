package com.livraison.supervision.dto;
import lombok.Data;
import java.time.LocalDate;
@Data
public class RechercheRequest {
    public LocalDate dateDebut;
    public LocalDate dateFin;
    public Long livreurId;
    public String nomClient;
    public String numeroCommande;
}

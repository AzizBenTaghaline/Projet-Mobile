package com.livraison.supervision.dto;
import lombok.Data;
@Data
public class StatLivreurDTO {
    public Long livreurId;
    public String nomComplet;
    public int total;
    public int livrees;
    public int enCours;
    public int echouees;
    public int enAttente;
}

package com.livraison.supervision.dto;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
@Data
public class DashboardDTO {
    public LocalDate date;
    public int totalLivraisons;
    public int livrees;
    public int enCours;
    public int echouees;
    public int enAttente;
    public List<StatLivreurDTO> statsParLivreur;
}

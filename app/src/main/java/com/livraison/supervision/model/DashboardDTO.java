package com.livraison.supervision.model;
import java.util.List;
public class DashboardDTO {
    public String date;
    public int totalLivraisons;
    public int livrees;
    public int enCours;
    public int echouees;
    public int enAttente;
    public List<StatLivreurDTO> statsParLivreur;
}

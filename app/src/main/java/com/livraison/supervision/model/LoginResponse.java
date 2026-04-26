package com.livraison.supervision.model;

public class LoginResponse {
    public String token;
    public Long userId;
    public String nom;
    public String prenom;
    public String identifiant;
    public String role; // "CONTROLEUR" ou "LIVREUR"
}

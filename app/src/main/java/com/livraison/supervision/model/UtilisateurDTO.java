package com.livraison.supervision.model;

public class UtilisateurDTO {
    public Long id;
    public String nom;
    public String prenom;
    public String identifiant;
    public Role role;

    public String getNomComplet() {
        return nom + " " + prenom;
    }

    public String getInitiales() {
        String i = "";
        if (nom != null && !nom.isEmpty()) i += nom.charAt(0);
        if (prenom != null && !prenom.isEmpty()) i += prenom.charAt(0);
        return i.toUpperCase();
    }
}

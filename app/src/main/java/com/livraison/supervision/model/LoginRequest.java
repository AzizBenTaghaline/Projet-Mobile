// ===== LoginRequest.java =====
package com.livraison.supervision.model;
public class LoginRequest {
    public String identifiant;
    public String motDePasse;
    public LoginRequest(String identifiant, String motDePasse) {
        this.identifiant = identifiant;
        this.motDePasse = motDePasse;
    }
}

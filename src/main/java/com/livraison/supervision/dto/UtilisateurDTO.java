package com.livraison.supervision.dto;
import com.livraison.supervision.entity.enums.Role;
import lombok.Data;
@Data
public class UtilisateurDTO {
    public Long id;
    public String nom;
    public String prenom;
    public String identifiant;
    public Role role;
}

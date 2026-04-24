package com.livraison.supervision.dto;
import com.livraison.supervision.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data @AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long userId;
    private String nom;
    private String prenom;
    private String identifiant;
    private Role role;
}

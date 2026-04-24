package com.livraison.supervision.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class LoginRequest {
    @NotBlank public String identifiant;
    @NotBlank public String motDePasse;
}

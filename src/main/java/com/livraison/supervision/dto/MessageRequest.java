package com.livraison.supervision.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class MessageRequest {
    @NotNull public Long destinataireId;
    @NotBlank public String contenu;
    public Boolean estUrgence = false;
    public Long livraisonId;
}

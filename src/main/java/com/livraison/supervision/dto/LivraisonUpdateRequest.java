package com.livraison.supervision.dto;
import com.livraison.supervision.entity.enums.Statut;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class LivraisonUpdateRequest {
    @NotNull public Long livraisonId;
    @NotNull public Statut statut;
    public String remarque;
}

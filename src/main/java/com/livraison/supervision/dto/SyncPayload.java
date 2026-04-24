package com.livraison.supervision.dto;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
@Data
public class SyncPayload {
    public Long livreurId;
    public LocalDate date;
    public List<LivraisonUpdateRequest> livraisons;
}

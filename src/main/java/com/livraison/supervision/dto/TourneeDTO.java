package com.livraison.supervision.dto;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
@Data
public class TourneeDTO {
    public Long livreurId;
    public LocalDate date;
    public List<LivraisonDTO> livraisons;
}

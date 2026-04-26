package com.livraison.supervision.model;
import java.util.List;
public class SyncPayload {
    public Long livreurId;
    public String date;
    public List<LivraisonUpdateRequest> livraisons;
}

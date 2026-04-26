package com.livraison.supervision.api;

import com.livraison.supervision.model.*;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;

public interface ApiService {

    // ===== AUTH =====
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    // ===== DASHBOARD (Contrôleur) =====
    @GET("api/dashboard")
    Call<DashboardDTO> getDashboard(@Query("date") String date);

    // ===== LIVRAISONS (Contrôleur) =====
    @GET("api/livraisons")
    Call<List<LivraisonDTO>> getLivraisons(@Query("date") String date);

    @GET("api/livraisons/{id}")
    Call<LivraisonDTO> getLivraisonById(@Path("id") Long id);

    @GET("api/livraisons/statut/{statut}")
    Call<List<LivraisonDTO>> getLivraisonsParStatut(@Path("statut") String statut);

    @POST("api/livraisons/recherche")
    Call<List<LivraisonDTO>> recherche(@Body RechercheRequest request);

    // ===== STATUT (Livreur - échec immédiat) =====
    @PATCH("api/livraisons/statut")
    Call<LivraisonDTO> updateStatut(@Body LivraisonUpdateRequest request);

    // ===== TOURNEE (Livreur) =====
    @GET("api/tournee")
    Call<TourneeDTO> getTournee(@Query("date") String date);

    // ===== SYNC fin de journée (Livreur) =====
    @POST("api/sync/livraisons")
    Call<Void> syncLivraisons(@Body SyncPayload payload);

    // ===== MESSAGES =====
    @POST("api/messages")
    Call<MessageDTO> envoyerMessage(@Body MessageRequest request);

    @GET("api/messages/recus")
    Call<List<MessageDTO>> getMessagesRecus();

    @GET("api/messages/urgents")
    Call<List<MessageDTO>> getUrgents();

    @GET("api/messages/conversation/{autreUserId}")
    Call<List<MessageDTO>> getConversation(@Path("autreUserId") Long autreUserId);

    @GET("api/messages/non-lus/count")
    Call<Long> countNonLus();

    @PATCH("api/messages/marquer-lus")
    Call<Void> marquerTousLus();
}

package com.livraison.supervision.controller;

import com.livraison.supervision.dto.*;
import com.livraison.supervision.entity.Utilisateur;
import com.livraison.supervision.entity.enums.Statut;
import com.livraison.supervision.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// ====================== AUTH ======================

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }
}

// ====================== DASHBOARD (Contrôleur) ======================

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CONTROLEUR')")
class DashboardController {

    private final LivraisonService livraisonService;

    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboard(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(livraisonService.getDashboard(
                date != null ? date : LocalDate.now()));
    }
}

// ====================== LIVRAISONS ======================

@RestController
@RequestMapping("/api/livraisons")
@RequiredArgsConstructor
class LivraisonController {

    private final LivraisonService livraisonService;

    // Contrôleur : toutes les livraisons du jour
    @GetMapping
    @PreAuthorize("hasRole('CONTROLEUR')")
    public ResponseEntity<List<LivraisonDTO>> getLivraisonsJour(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(livraisonService.getLivraisonsJour(
                date != null ? date : LocalDate.now()));
    }

    // Contrôleur : détail d'une livraison
    @GetMapping("/{id}")
    public ResponseEntity<LivraisonDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(livraisonService.getById(id));
    }

    // Contrôleur : filtre par statut
    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasRole('CONTROLEUR')")
    public ResponseEntity<List<LivraisonDTO>> parStatut(@PathVariable Statut statut) {
        return ResponseEntity.ok(livraisonService.getLivraisonsParStatut(statut));
    }

    // Contrôleur : recherche avancée
    @PostMapping("/recherche")
    @PreAuthorize("hasRole('CONTROLEUR')")
    public ResponseEntity<List<LivraisonDTO>> recherche(@RequestBody RechercheRequest req) {
        return ResponseEntity.ok(livraisonService.recherche(req));
    }

    // Livreur : modifier statut d'une livraison (en cas d'échec => sync immédiate)
    @PatchMapping("/statut")
    @PreAuthorize("hasRole('LIVREUR')")
    public ResponseEntity<LivraisonDTO> updateStatut(
            @Valid @RequestBody LivraisonUpdateRequest req) {
        return ResponseEntity.ok(livraisonService.updateStatut(req));
    }
}

// ====================== TOURNEE (Livreur) ======================

@RestController
@RequestMapping("/api/tournee")
@RequiredArgsConstructor
@PreAuthorize("hasRole('LIVREUR')")
class TourneeController {

    private final LivraisonService livraisonService;

    // Retourne la tournée complète du livreur connecté (chargée au démarrage)
    @GetMapping
    public ResponseEntity<TourneeDTO> getTournee(
            @AuthenticationPrincipal Utilisateur utilisateur,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(livraisonService.getTournee(
                utilisateur.getId(),
                date != null ? date : LocalDate.now()));
    }
}

// ====================== SYNC (Livreur → serveur, fin de journée) ======================

@RestController
@RequestMapping("/api/sync")
@RequiredArgsConstructor
@PreAuthorize("hasRole('LIVREUR')")
class SyncController {

    private final LivraisonService livraisonService;

    @PostMapping("/livraisons")
    public ResponseEntity<Void> syncLivraisons(@RequestBody SyncPayload payload) {
        livraisonService.syncLivraisons(payload);
        return ResponseEntity.ok().build();
    }
}

// ====================== MESSAGES ======================

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
class MessageController {

    private final MessageService messageService;

    // Envoyer un message (contrôleur → livreur ou livreur → contrôleur)
    @PostMapping
    public ResponseEntity<MessageDTO> envoyer(
            @Valid @RequestBody MessageRequest req,
            @AuthenticationPrincipal Utilisateur utilisateur) {
        return ResponseEntity.ok(messageService.envoyer(req, utilisateur.getId()));
    }

    // Récupérer tous les messages reçus
    @GetMapping("/recus")
    public ResponseEntity<List<MessageDTO>> getRecus(
            @AuthenticationPrincipal Utilisateur utilisateur) {
        return ResponseEntity.ok(messageService.getMessagesRecus(utilisateur.getId()));
    }

    // Messages d'urgence non lus (polling contrôleur)
    @GetMapping("/urgents")
    @PreAuthorize("hasRole('CONTROLEUR')")
    public ResponseEntity<List<MessageDTO>> getUrgents(
            @AuthenticationPrincipal Utilisateur utilisateur) {
        return ResponseEntity.ok(messageService.getUrgentsNonLus(utilisateur.getId()));
    }

    // Conversation entre 2 utilisateurs
    @GetMapping("/conversation/{autreUserId}")
    public ResponseEntity<List<MessageDTO>> getConversation(
            @PathVariable Long autreUserId,
            @AuthenticationPrincipal Utilisateur utilisateur) {
        return ResponseEntity.ok(messageService.getConversation(
                utilisateur.getId(), autreUserId));
    }

    // Nombre de messages non lus (badge)
    @GetMapping("/non-lus/count")
    public ResponseEntity<Long> countNonLus(
            @AuthenticationPrincipal Utilisateur utilisateur) {
        return ResponseEntity.ok(messageService.countNonLus(utilisateur.getId()));
    }

    // Marquer tous comme lus
    @PatchMapping("/marquer-lus")
    public ResponseEntity<Void> marquerLus(
            @AuthenticationPrincipal Utilisateur utilisateur) {
        messageService.marquerTousLus(utilisateur.getId());
        return ResponseEntity.ok().build();
    }
}

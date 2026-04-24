package com.livraison.supervision.service;

import com.livraison.supervision.dto.*;
import com.livraison.supervision.entity.*;
import org.springframework.stereotype.Service;

@Service
public class MapperService {

    public UtilisateurDTO toDTO(Utilisateur u) {
        if (u == null) return null;
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.id = u.getId();
        dto.nom = u.getNom();
        dto.prenom = u.getPrenom();
        dto.identifiant = u.getIdentifiant();
        dto.role = u.getRole();
        return dto;
    }

    public ClientDTO toDTO(Client c) {
        if (c == null) return null;
        ClientDTO dto = new ClientDTO();
        dto.id = c.getId();
        dto.nom = c.getNom();
        dto.telephone = c.getTelephone();
        dto.adresse = c.getAdresse();
        dto.ville = c.getVille();
        return dto;
    }

    public LivraisonDTO toDTO(Livraison l) {
        if (l == null) return null;
        LivraisonDTO dto = new LivraisonDTO();
        dto.id = l.getId();
        dto.numeroCommande = l.getNumeroCommande();
        dto.client = toDTO(l.getClient());
        dto.livreur = toDTO(l.getLivreur());
        dto.statut = l.getStatut();
        dto.modePaiement = l.getModePaiement();
        dto.nbArticles = l.getNbArticles();
        dto.montant = l.getMontant();
        dto.dateLivraison = l.getDateLivraison();
        dto.remarque = l.getRemarque();
        dto.zone = l.getZone();
        return dto;
    }

    public MessageDTO toDTO(Message m) {
        if (m == null) return null;
        MessageDTO dto = new MessageDTO();
        dto.id = m.getId();
        dto.expediteur = toDTO(m.getExpediteur());
        dto.destinataire = toDTO(m.getDestinataire());
        dto.contenu = m.getContenu();
        dto.dateEnvoi = m.getDateEnvoi();
        dto.estUrgence = m.getEstUrgence();
        dto.livraisonId = m.getLivraison() != null ? m.getLivraison().getId() : null;
        dto.numeroCommande = m.getLivraison() != null ? m.getLivraison().getNumeroCommande() : null;
        dto.lu = m.getLu();
        return dto;
    }
}

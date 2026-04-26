package com.livraison.supervision.service;

import com.livraison.supervision.dto.*;
import com.livraison.supervision.entity.Livraison;
import com.livraison.supervision.entity.enums.Statut;
import com.livraison.supervision.repository.LivraisonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LivraisonService {

    private final LivraisonRepository livraisonRepository;
    private final MapperService mapper;

    // ---------- Contrôleur ----------

    public List<LivraisonDTO> getLivraisonsJour(LocalDate date) {
        LocalDate dateEnd = date.plusDays(1);
        return livraisonRepository.findByDateLivraison(date, dateEnd)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<LivraisonDTO> getLivraisonsParStatut(Statut statut) {
        return livraisonRepository.findByStatut(statut)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public LivraisonDTO getById(Long id) {
        Livraison l = livraisonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livraison introuvable : " + id));
        return mapper.toDTO(l);
    }

    public List<LivraisonDTO> recherche(RechercheRequest req) {
        return livraisonRepository.rechercheAvancee(
                req.dateDebut, req.dateFin, req.livreurId, req.nomClient, req.numeroCommande
        ).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    // ---------- Dashboard ----------

    public DashboardDTO getDashboard(LocalDate date) {
        DashboardDTO dash = new DashboardDTO();
        dash.date = date;
        LocalDate dateEnd = date.plusDays(1);

        // Stats globales par statut
        List<Object[]> statsStatut = livraisonRepository.countByStatutForDate(date, dateEnd);
        System.out.println(">>> Stats statut rows: " + statsStatut.size());

        for (Object[] row : statsStatut) {
            // Oracle renvoie le statut comme String, pas comme enum
            String statutStr = String.valueOf(row[0]);
            int count = ((Number) row[1]).intValue();
            System.out.println(">>> Statut: " + statutStr + " = " + count);

            switch (statutStr) {
                case "LIVREE":    dash.livrees   = count; break;
                case "EN_COURS":  dash.enCours   = count; break;
                case "ECHOUEE":   dash.echouees  = count; break;
                case "EN_ATTENTE": dash.enAttente = count; break;
                default:
                    System.out.println(">>> Statut inconnu: " + statutStr);
            }
        }
        dash.totalLivraisons = dash.livrees + dash.enCours + dash.echouees + dash.enAttente;

        // Stats par livreur
        List<Object[]> rawStats = livraisonRepository.statsParLivreur(date, dateEnd);
        System.out.println(">>> Stats livreur rows: " + rawStats.size());

        Map<String, StatLivreurDTO> map = new LinkedHashMap<>();

        for (Object[] row : rawStats) {
            String nom    = String.valueOf(row[0]);
            String prenom = String.valueOf(row[1]);
            // row[2] = statut (String depuis Oracle)
            String statutStr = String.valueOf(row[2]);
            int count = ((Number) row[3]).intValue();
            String key = nom + " " + prenom;

            StatLivreurDTO stat = map.computeIfAbsent(key, k -> {
                StatLivreurDTO st = new StatLivreurDTO();
                st.nomComplet = k;
                return st;
            });

            stat.total += count;
            switch (statutStr) {
                case "LIVREE":    stat.livrees   += count; break;
                case "EN_COURS":  stat.enCours   += count; break;
                case "ECHOUEE":   stat.echouees  += count; break;
                case "EN_ATTENTE": stat.enAttente += count; break;
            }
        }

        dash.statsParLivreur = new ArrayList<>(map.values());
        return dash;
    }

    // ---------- Livreur ----------

    public TourneeDTO getTournee(Long livreurId, LocalDate date) {
        TourneeDTO tournee = new TourneeDTO();
        tournee.livreurId = livreurId;
        tournee.date = date;
        LocalDate dateEnd = date.plusDays(1);
        List<LivraisonDTO> livraisons = livraisonRepository
                .findByLivreurIdAndDateLivraison(livreurId, date, dateEnd)
                .stream()
                .sorted(Comparator.comparing(l -> l.getZone() != null ? l.getZone() : ""))
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        tournee.livraisons = livraisons;
        return tournee;
    }

    @Transactional
    public LivraisonDTO updateStatut(LivraisonUpdateRequest req) {
        Livraison l = livraisonRepository.findById(req.livraisonId)
                .orElseThrow(() -> new RuntimeException("Livraison introuvable : " + req.livraisonId));
        l.setStatut(req.statut);
        if (req.remarque != null) l.setRemarque(req.remarque);
        return mapper.toDTO(livraisonRepository.save(l));
    }

    @Transactional
    public void syncLivraisons(SyncPayload payload) {
        for (LivraisonUpdateRequest req : payload.livraisons) {
            livraisonRepository.findById(req.livraisonId).ifPresent(l -> {
                l.setStatut(req.statut);
                if (req.remarque != null) l.setRemarque(req.remarque);
                livraisonRepository.save(l);
            });
        }
    }
}
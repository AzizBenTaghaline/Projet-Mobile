package com.livraison.supervision.repository;

import com.livraison.supervision.entity.Livraison;
import com.livraison.supervision.entity.enums.Statut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Long> {

    // Toutes les livraisons d'une date donnée
    List<Livraison> findByDateLivraison(LocalDate date);

    // Livraisons du jour pour un livreur
    List<Livraison> findByLivreurIdAndDateLivraison(Long livreurId, LocalDate date);

    // Par statut
    List<Livraison> findByStatut(Statut statut);

    // Par livreur et statut
    List<Livraison> findByLivreurIdAndStatut(Long livreurId, Statut statut);

    // Recherche avancée (contrôleur)
    @Query("SELECT l FROM Livraison l " +
           "JOIN FETCH l.client c " +
           "JOIN FETCH l.livreur u " +
           "WHERE (:dateDebut IS NULL OR l.dateLivraison >= :dateDebut) " +
           "AND (:dateFin IS NULL OR l.dateLivraison <= :dateFin) " +
           "AND (:livreurId IS NULL OR u.id = :livreurId) " +
           "AND (:nomClient IS NULL OR LOWER(c.nom) LIKE LOWER(CONCAT('%', :nomClient, '%'))) " +
           "AND (:numeroCommande IS NULL OR l.numeroCommande LIKE CONCAT('%', :numeroCommande, '%'))")
    List<Livraison> rechercheAvancee(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin,
            @Param("livreurId") Long livreurId,
            @Param("nomClient") String nomClient,
            @Param("numeroCommande") String numeroCommande);

    // Statistiques dashboard : compter par statut pour une date
    @Query("SELECT l.statut, COUNT(l) FROM Livraison l WHERE l.dateLivraison = :date GROUP BY l.statut")
    List<Object[]> countByStatutForDate(@Param("date") LocalDate date);

    // Stats par livreur pour une date
    @Query("SELECT u.nom, u.prenom, l.statut, COUNT(l) FROM Livraison l " +
           "JOIN l.livreur u WHERE l.dateLivraison = :date GROUP BY u.id, u.nom, u.prenom, l.statut")
    List<Object[]> statsParLivreur(@Param("date") LocalDate date);

    // Livraisons non-synchronisées d'un livreur (LIVREE ou ECHOUEE)
    @Query("SELECT l FROM Livraison l WHERE l.livreur.id = :livreurId " +
           "AND l.statut IN ('LIVREE', 'ECHOUEE') AND l.dateLivraison = :date")
    List<Livraison> findLivraisonsASync(@Param("livreurId") Long livreurId,
                                        @Param("date") LocalDate date);

    Optional<Livraison> findByNumeroCommande(String numeroCommande);
}

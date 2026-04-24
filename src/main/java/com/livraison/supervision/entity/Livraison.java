package com.livraison.supervision.entity;

import com.livraison.supervision.entity.enums.Statut;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "LIVRAISON")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Livraison {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_liv")
    @SequenceGenerator(name = "seq_liv", sequenceName = "SEQ_LIVRAISON", allocationSize = 1)
    private Long id;

    @Column(name = "NUMERO_COMMANDE", unique = true, nullable = false, length = 20)
    private String numeroCommande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LIVREUR_ID")
    private Utilisateur livreur;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Statut statut = Statut.EN_ATTENTE;

    @Column(name = "MODE_PAIEMENT", length = 30)
    private String modePaiement;

    @Column(name = "NB_ARTICLES")
    private Integer nbArticles;

    @Column(precision = 10, scale = 3)
    private BigDecimal montant;

    @Column(name = "DATE_LIVRAISON")
    private LocalDate dateLivraison;

    @Column(length = 500)
    private String remarque;

    @Column(length = 100)
    private String zone;

    @PrePersist
    public void prePersist() {
        if (dateLivraison == null) dateLivraison = LocalDate.now();
        if (statut == null) statut = Statut.EN_ATTENTE;
    }
}

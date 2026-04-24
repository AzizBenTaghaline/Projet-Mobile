package com.livraison.supervision.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MESSAGE")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_msg")
    @SequenceGenerator(name = "seq_msg", sequenceName = "SEQ_MESSAGE", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXPEDITEUR_ID")
    private Utilisateur expediteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DESTINATAIRE_ID")
    private Utilisateur destinataire;

    @Column(nullable = false, length = 1000)
    private String contenu;

    @Column(name = "DATE_ENVOI")
    private LocalDateTime dateEnvoi;

    @Column(name = "EST_URGENCE")
    private Boolean estUrgence = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LIVRAISON_ID")
    private Livraison livraison;

    @Column(nullable = false)
    private Boolean lu = false;

    @PrePersist
    public void prePersist() {
        if (dateEnvoi == null) dateEnvoi = LocalDateTime.now();
        if (estUrgence == null) estUrgence = false;
        if (lu == null) lu = false;
    }
}

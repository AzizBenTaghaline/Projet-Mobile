package com.livraison.supervision.entity;

import com.livraison.supervision.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "UTILISATEUR")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_util")
    @SequenceGenerator(name = "seq_util", sequenceName = "SEQ_UTILISATEUR", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(unique = true, nullable = false, length = 50)
    private String identifiant;

    @Column(name = "MOT_DE_PASSE", nullable = false)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(nullable = false)
    private Boolean actif = true;
}

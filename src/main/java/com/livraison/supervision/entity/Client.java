package com.livraison.supervision.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CLIENT")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_client")
    @SequenceGenerator(name = "seq_client", sequenceName = "SEQ_CLIENT", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(length = 20)
    private String telephone;

    @Column(length = 255)
    private String adresse;

    @Column(length = 100)
    private String ville;
}

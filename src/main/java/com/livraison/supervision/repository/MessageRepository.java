package com.livraison.supervision.repository;

import com.livraison.supervision.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Messages reçus par un utilisateur, non lus en premier
    @Query("SELECT m FROM Message m JOIN FETCH m.expediteur " +
           "WHERE m.destinataire.id = :userId ORDER BY m.dateEnvoi DESC")
    List<Message> findByDestinataireId(@Param("userId") Long userId);

    // Messages urgents non lus (pour contrôleur)
    @Query("SELECT m FROM Message m JOIN FETCH m.expediteur " +
           "WHERE m.destinataire.id = :userId AND m.estUrgence = true AND m.lu = false " +
           "ORDER BY m.dateEnvoi DESC")
    List<Message> findUrgentsNonLus(@Param("userId") Long userId);

    // Conversation entre 2 utilisateurs
    @Query("SELECT m FROM Message m JOIN FETCH m.expediteur JOIN FETCH m.destinataire " +
           "WHERE (m.expediteur.id = :u1 AND m.destinataire.id = :u2) " +
           "OR (m.expediteur.id = :u2 AND m.destinataire.id = :u1) " +
           "ORDER BY m.dateEnvoi ASC")
    List<Message> findConversation(@Param("u1") Long u1, @Param("u2") Long u2);

    // Compter messages non lus
    long countByDestinataireIdAndLuFalse(Long destinataireId);

    // Marquer comme lus
    @Modifying
    @Query("UPDATE Message m SET m.lu = true WHERE m.destinataire.id = :userId AND m.lu = false")
    void marquerTousLus(@Param("userId") Long userId);
}

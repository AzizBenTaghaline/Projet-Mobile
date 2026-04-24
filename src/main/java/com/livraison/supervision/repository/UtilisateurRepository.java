package com.livraison.supervision.repository;

import com.livraison.supervision.entity.Utilisateur;
import com.livraison.supervision.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByIdentifiant(String identifiant);
    List<Utilisateur> findByRole(Role role);
    List<Utilisateur> findByRoleAndActifTrue(Role role);
    boolean existsByIdentifiant(String identifiant);
}

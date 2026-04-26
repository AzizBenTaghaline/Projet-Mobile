package com.livraison.supervision.service;

import com.livraison.supervision.dto.LoginRequest;
import com.livraison.supervision.dto.LoginResponse;
import com.livraison.supervision.entity.Utilisateur;
import com.livraison.supervision.repository.UtilisateurRepository;
import com.livraison.supervision.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        Utilisateur utilisateur = utilisateurRepository
                .findByIdentifiant(request.identifiant)
                .orElseThrow(() -> new RuntimeException("Identifiant ou mot de passe incorrect"));

        if (!utilisateur.getActif()) {
            throw new RuntimeException("Compte désactivé");
        }

        // TODO: TEMPORAIRE - contournement BCrypt pour debugging
        boolean passwordMatch = request.motDePasse.equals(utilisateur.getMotDePasse());
        System.out.println(">>> PASSWORD MATCH: " + passwordMatch);
        System.out.println(">>> MDP FOURNI: " + request.motDePasse + " | MDP EN BASE: " + utilisateur.getMotDePasse());
        if (!passwordMatch) {
            throw new RuntimeException("Identifiant ou mot de passe incorrect");
        }

        String token = jwtUtil.generateToken(
                utilisateur.getIdentifiant(),
                utilisateur.getRole().name(),
                utilisateur.getId()
        );

        return new LoginResponse(
                token,
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getIdentifiant(),
                utilisateur.getRole()
        );
    }

    public void logout(Long userId) {
        System.out.println(">>> USER LOGOUT: id=" + userId);
    }
}

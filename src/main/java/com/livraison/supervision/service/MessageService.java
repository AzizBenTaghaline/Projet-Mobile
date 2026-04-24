package com.livraison.supervision.service;

import com.livraison.supervision.dto.MessageDTO;
import com.livraison.supervision.dto.MessageRequest;
import com.livraison.supervision.entity.Message;
import com.livraison.supervision.entity.Utilisateur;
import com.livraison.supervision.repository.LivraisonRepository;
import com.livraison.supervision.repository.MessageRepository;
import com.livraison.supervision.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final LivraisonRepository livraisonRepository;
    private final MapperService mapper;

    @Transactional
    public MessageDTO envoyer(MessageRequest req, Long expediteurId) {
        Utilisateur expediteur = utilisateurRepository.findById(expediteurId)
                .orElseThrow(() -> new RuntimeException("Expéditeur introuvable"));

        Utilisateur destinataire = utilisateurRepository.findById(req.destinataireId)
                .orElseThrow(() -> new RuntimeException("Destinataire introuvable"));

        Message msg = Message.builder()
                .expediteur(expediteur)
                .destinataire(destinataire)
                .contenu(req.contenu)
                .estUrgence(req.estUrgence != null && req.estUrgence)
                .livraison(req.livraisonId != null
                        ? livraisonRepository.findById(req.livraisonId).orElse(null)
                        : null)
                .build();

        return mapper.toDTO(messageRepository.save(msg));
    }

    public List<MessageDTO> getMessagesRecus(Long userId) {
        return messageRepository.findByDestinataireId(userId)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<MessageDTO> getUrgentsNonLus(Long userId) {
        return messageRepository.findUrgentsNonLus(userId)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<MessageDTO> getConversation(Long u1, Long u2) {
        return messageRepository.findConversation(u1, u2)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public long countNonLus(Long userId) {
        return messageRepository.countByDestinataireIdAndLuFalse(userId);
    }

    @Transactional
    public void marquerTousLus(Long userId) {
        messageRepository.marquerTousLus(userId);
    }
}

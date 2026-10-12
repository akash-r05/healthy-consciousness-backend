package com.healthyconsciousness.backend.service;

import com.healthyconsciousness.backend.dto.symptom.SymptomHistoryRequest;
import com.healthyconsciousness.backend.dto.symptom.SymptomHistoryResponse;
import com.healthyconsciousness.backend.dto.symptom.SymptomRequest;
import com.healthyconsciousness.backend.dto.symptom.SymptomResponse;
import com.healthyconsciousness.backend.entity.SymptomCatalog;
import com.healthyconsciousness.backend.entity.SymptomHistory;
import com.healthyconsciousness.backend.entity.User;
import com.healthyconsciousness.backend.exception.ResourceNotFoundException;
import com.healthyconsciousness.backend.repository.SymptomCatalogRepository;
import com.healthyconsciousness.backend.repository.SymptomHistoryRepository;
import com.healthyconsciousness.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SymptomService {

    private final SymptomCatalogRepository symptomCatalogRepository;
    private final SymptomHistoryRepository symptomHistoryRepository;
    private final UserRepository userRepository;

    public SymptomService(SymptomCatalogRepository symptomCatalogRepository,
                         SymptomHistoryRepository symptomHistoryRepository,
                         UserRepository userRepository) {
        this.symptomCatalogRepository = symptomCatalogRepository;
        this.symptomHistoryRepository = symptomHistoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<SymptomResponse> listSymptoms(String query, Pageable pageable) {
        return symptomCatalogRepository.searchSymptoms(query, pageable)
                .map(this::toCatalogResponse);
    }

    @Transactional(readOnly = true)
    public SymptomResponse getSymptom(Long id) {
        return toCatalogResponse(findCatalogById(id));
    }

    @Transactional
    public SymptomResponse createSymptom(SymptomRequest request) {
        SymptomCatalog symptom = SymptomCatalog.builder()
                .name(request.name().trim())
                .description(request.description())
                .commonSigns(request.commonSigns())
                .whenToSeekHelp(request.whenToSeekHelp())
                .emergencyNote(request.emergencyNote())
                .sourceName(request.sourceName())
                .sourceUrl(request.sourceUrl())
                .published(request.published())
                .build();
        return toCatalogResponse(symptomCatalogRepository.save(symptom));
    }

    @Transactional
    public SymptomResponse updateSymptom(Long id, SymptomRequest request) {
        SymptomCatalog symptom = findCatalogById(id);
        if (request.name() != null) symptom.setName(request.name().trim());
        if (request.description() != null) symptom.setDescription(request.description());
        if (request.commonSigns() != null) symptom.setCommonSigns(request.commonSigns());
        if (request.whenToSeekHelp() != null) symptom.setWhenToSeekHelp(request.whenToSeekHelp());
        if (request.emergencyNote() != null) symptom.setEmergencyNote(request.emergencyNote());
        if (request.sourceName() != null) symptom.setSourceName(request.sourceName());
        if (request.sourceUrl() != null) symptom.setSourceUrl(request.sourceUrl());
        symptom.setPublished(request.published());
        return toCatalogResponse(symptomCatalogRepository.save(symptom));
    }

    @Transactional
    public void deleteSymptom(Long id) {
        SymptomCatalog symptom = findCatalogById(id);
        symptomCatalogRepository.delete(symptom);
    }

    @Transactional
    public SymptomHistoryResponse logSymptom(Long userId, SymptomHistoryRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        SymptomCatalog symptom = findCatalogById(request.symptomId());

        SymptomHistory history = SymptomHistory.builder()
                .user(user)
                .symptom(symptom)
                .notes(request.notes())
                .severity(request.severity())
                .startedAt(LocalDateTime.now())
                .build();
        return toHistoryResponse(symptomHistoryRepository.save(history));
    }

    @Transactional(readOnly = true)
    public Page<SymptomHistoryResponse> getUserSymptomHistory(Long userId, Pageable pageable) {
        return symptomHistoryRepository.findByUserId(userId, pageable)
                .map(this::toHistoryResponse);
    }

    private SymptomCatalog findCatalogById(Long id) {
        return symptomCatalogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Symptom not found: " + id));
    }

    private SymptomResponse toCatalogResponse(SymptomCatalog symptom) {
        return new SymptomResponse(
                symptom.getId(),
                symptom.getName(),
                symptom.getDescription(),
                symptom.getCommonSigns(),
                symptom.getWhenToSeekHelp(),
                symptom.getEmergencyNote(),
                symptom.getSourceName(),
                symptom.getSourceUrl(),
                symptom.isPublished()
        );
    }

    private SymptomHistoryResponse toHistoryResponse(SymptomHistory history) {
        return new SymptomHistoryResponse(
                history.getId(),
                history.getUser().getId(),
                history.getSymptom().getId(),
                history.getSymptom().getName(),
                history.getNotes(),
                history.getSeverity(),
                history.getStartedAt(),
                history.getCreatedAt()
        );
    }
}

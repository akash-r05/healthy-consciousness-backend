package com.healthyconsciousness.backend.service;

import com.healthyconsciousness.backend.dto.preventive.PreventiveCheckupRequest;
import com.healthyconsciousness.backend.dto.preventive.PreventiveCheckupResponse;
import com.healthyconsciousness.backend.dto.preventive.VaccinationRecordRequest;
import com.healthyconsciousness.backend.dto.preventive.VaccinationRecordResponse;
import com.healthyconsciousness.backend.entity.PreventiveCheckup;
import com.healthyconsciousness.backend.entity.User;
import com.healthyconsciousness.backend.entity.VaccinationRecord;
import com.healthyconsciousness.backend.exception.ResourceNotFoundException;
import com.healthyconsciousness.backend.repository.PreventiveCheckupRepository;
import com.healthyconsciousness.backend.repository.UserRepository;
import com.healthyconsciousness.backend.repository.VaccinationRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PreventiveHealthcareService {

    private final PreventiveCheckupRepository checkupRepository;
    private final VaccinationRecordRepository vaccinationRepository;
    private final UserRepository userRepository;

    public PreventiveHealthcareService(PreventiveCheckupRepository checkupRepository,
                                      VaccinationRecordRepository vaccinationRepository,
                                      UserRepository userRepository) {
        this.checkupRepository = checkupRepository;
        this.vaccinationRepository = vaccinationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PreventiveCheckupResponse scheduleCheckup(Long userId, PreventiveCheckupRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        PreventiveCheckup checkup = PreventiveCheckup.builder()
                .user(user)
                .title(request.title())
                .scheduledDate(request.scheduledDate())
                .notes(request.notes())
                .status("SCHEDULED")
                .build();
        return toCheckupResponse(checkupRepository.save(checkup));
    }

    @Transactional
    public PreventiveCheckupResponse completeCheckup(Long checkupId, Long userId) {
        PreventiveCheckup checkup = checkupRepository.findById(checkupId)
                .orElseThrow(() -> new ResourceNotFoundException("Checkup not found: " + checkupId));
        if (!checkup.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Unauthorized access to checkup");
        }
        checkup.setStatus("COMPLETED");
        checkup.setCompletedDate(LocalDateTime.now());
        return toCheckupResponse(checkupRepository.save(checkup));
    }

    @Transactional(readOnly = true)
    public Page<PreventiveCheckupResponse> getUserCheckups(Long userId, Pageable pageable) {
        return checkupRepository.findByUserId(userId, pageable)
                .map(this::toCheckupResponse);
    }

    @Transactional
    public VaccinationRecordResponse recordVaccination(Long userId, VaccinationRecordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        VaccinationRecord record = VaccinationRecord.builder()
                .user(user)
                .vaccineName(request.vaccineName())
                .administeredDate(request.administeredDate())
                .nextDueDate(request.nextDueDate())
                .notes(request.notes())
                .build();
        return toVaccinationResponse(vaccinationRepository.save(record));
    }

    @Transactional(readOnly = true)
    public Page<VaccinationRecordResponse> getUserVaccinations(Long userId, Pageable pageable) {
        return vaccinationRepository.findByUserId(userId, pageable)
                .map(this::toVaccinationResponse);
    }

    private PreventiveCheckupResponse toCheckupResponse(PreventiveCheckup checkup) {
        return new PreventiveCheckupResponse(
                checkup.getId(),
                checkup.getTitle(),
                checkup.getScheduledDate(),
                checkup.getCompletedDate(),
                checkup.getStatus(),
                checkup.getNotes()
        );
    }

    private VaccinationRecordResponse toVaccinationResponse(VaccinationRecord record) {
        return new VaccinationRecordResponse(
                record.getId(),
                record.getVaccineName(),
                record.getAdministeredDate(),
                record.getNextDueDate(),
                record.getNotes()
        );
    }
}

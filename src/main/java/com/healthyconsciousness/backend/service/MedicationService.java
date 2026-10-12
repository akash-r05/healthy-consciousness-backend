package com.healthyconsciousness.backend.service;

import com.healthyconsciousness.backend.dto.medication.MedicationRecordRequest;
import com.healthyconsciousness.backend.dto.medication.MedicationRecordResponse;
import com.healthyconsciousness.backend.dto.medication.MedicationReminderRequest;
import com.healthyconsciousness.backend.dto.medication.MedicationReminderResponse;
import com.healthyconsciousness.backend.entity.MedicationRecord;
import com.healthyconsciousness.backend.entity.MedicationReminder;
import com.healthyconsciousness.backend.entity.User;
import com.healthyconsciousness.backend.exception.ResourceNotFoundException;
import com.healthyconsciousness.backend.repository.MedicationRecordRepository;
import com.healthyconsciousness.backend.repository.MedicationReminderRepository;
import com.healthyconsciousness.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MedicationService {

    private final MedicationRecordRepository recordRepository;
    private final MedicationReminderRepository reminderRepository;
    private final UserRepository userRepository;

    public MedicationService(MedicationRecordRepository recordRepository,
                            MedicationReminderRepository reminderRepository,
                            UserRepository userRepository) {
        this.recordRepository = recordRepository;
        this.reminderRepository = reminderRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public MedicationRecordResponse addMedication(Long userId, MedicationRecordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        MedicationRecord record = MedicationRecord.builder()
                .user(user)
                .name(request.name())
                .dosage(request.dosage())
                .form(request.form())
                .startDate(request.startDate() != null ? request.startDate() : LocalDateTime.now())
                .endDate(request.endDate())
                .notes(request.notes())
                .build();
        return toRecordResponse(recordRepository.save(record));
    }

    @Transactional(readOnly = true)
    public Page<MedicationRecordResponse> getActiveMedications(Long userId, Pageable pageable) {
        return recordRepository.findActiveMedicationsByUserId(userId, pageable)
                .map(this::toRecordResponse);
    }

    @Transactional
    public MedicationReminderResponse setReminder(MedicationReminderRequest request) {
        MedicationRecord medication = recordRepository.findById(request.medicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found: " + request.medicationId()));
        MedicationReminder reminder = MedicationReminder.builder()
                .medication(medication)
                .reminderTime(request.reminderTime())
                .frequency(request.frequency())
                .status("ACTIVE")
                .build();
        return toReminderResponse(reminderRepository.save(reminder));
    }

    @Transactional
    public void removeMedication(Long medicationId, Long userId) {
        MedicationRecord record = recordRepository.findById(medicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found: " + medicationId));
        if (!record.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Unauthorized access to medication");
        }
        recordRepository.delete(record);
    }

    private MedicationRecordResponse toRecordResponse(MedicationRecord record) {
        return new MedicationRecordResponse(
                record.getId(),
                record.getName(),
                record.getDosage(),
                record.getForm(),
                record.getStartDate(),
                record.getEndDate(),
                record.getNotes(),
                record.getCreatedAt()
        );
    }

    private MedicationReminderResponse toReminderResponse(MedicationReminder reminder) {
        return new MedicationReminderResponse(
                reminder.getId(),
                reminder.getMedication().getId(),
                reminder.getReminderTime().toString(),
                reminder.getFrequency(),
                reminder.getStatus()
        );
    }
}

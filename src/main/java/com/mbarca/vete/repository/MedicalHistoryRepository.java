package com.mbarca.vete.repository;

import com.mbarca.vete.domain.MedicalHistory;

import java.util.List;

public interface MedicalHistoryRepository {
Integer createMedicalHistory (MedicalHistory medicalHistory, Long petId);
List<MedicalHistory> getMedicalHistoryForPet (Long petId);
MedicalHistory getMedicalHistoryById (Long medicalHistoryId);
Integer deleteMedicalHistory (Long medicalHistoryId);
}

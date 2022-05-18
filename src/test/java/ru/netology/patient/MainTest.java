package ru.netology.patient;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MainTest {
    @Test
    public void test_checkTemperature() {
        PatientInfo patientInfo = new PatientInfo("1234", "Ivan", "Ivanov",
                LocalDate.of(1980, 10, 20),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)));

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.anyString())).thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkTemperature("1234", new BigDecimal("34.4"));

        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        Assertions.assertEquals("Warning, patient with id: 1234, need help", argumentCaptor.getValue());
        Mockito.verify(sendAlertService, Mockito.times(1)).send(Mockito.anyString());
    }

    @Test
    public void test_checkBloodPressure() {
        PatientInfo patientInfo = new PatientInfo("1234", "Ivan", "Ivanov",
                LocalDate.of(1980, 10, 20),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)));

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.anyString())).thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure("1234", new BloodPressure(150, 90));

        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        Assertions.assertEquals("Warning, patient with id: 1234, need help", argumentCaptor.getValue());
        Mockito.verify(sendAlertService, Mockito.times(1)).send(Mockito.anyString());
    }

    @Test
    public void test_checkBloodPressure_checkTemperature_normalIndicators() {
        PatientInfo patientInfo = new PatientInfo("1234", "Ivan", "Ivanov",
                LocalDate.of(1980, 10, 20),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)));

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.anyString())).thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure("1234", new BloodPressure(120, 80));
        medicalService.checkTemperature("1234", new BigDecimal("37.1"));
        Mockito.verify(sendAlertService, Mockito.times(0)).send(Mockito.anyString());
    }
}

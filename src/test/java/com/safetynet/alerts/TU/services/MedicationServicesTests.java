package com.safetynet.alerts.TU.services;

import com.safetynet.alerts.UTHelper;
import com.safetynet.alerts.server.database.entities.*;
import com.safetynet.alerts.server.database.repositories.AllergeneRepository;
import com.safetynet.alerts.server.database.repositories.MedicationRepository;
import com.safetynet.alerts.server.database.repositories.PersonRepository;
import com.safetynet.alerts.server.services.MedicationPostServices;
import io.swagger.model.Medicalrecords;
import io.swagger.model.PersonsRsp;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MedicationServicesTests {

	@Mock
	PersonRepository personRepository;

	@Mock
	AllergeneRepository allergeneRepository;

	@Mock
	MedicationRepository medicationRepository;

	MedicationPostServices medicationServices;

	PersonEntity adult = new PersonEntity();

	List<PersonEntity> personEntityList2;
	List<PersonEntity> personEntityList1;

	String address = "1509 Culver St";

	Medicalrecords medicalrecords;


	@BeforeAll
	void prepare() {

		personEntityList2 = new ArrayList<>();
		personEntityList1 = new ArrayList<>();

		medicationServices = new MedicationPostServices(personRepository, medicationRepository, allergeneRepository);

		MedicationEntity medicationEntity = new MedicationEntity();

		AddressEntity addressEntity = new AddressEntity();

		List<MedicationEntity> medicationEntities = new ArrayList<>();

		MedicineEntity medicineEntity = new MedicineEntity();

		List<AllergeneEntity> allergeneEntities = new ArrayList<>();

		AllergeneEntity allergeneEntity = new AllergeneEntity();

		String phone = "841-874-6512";

		addressEntity.setAddress(address);
		addressEntity.setStation(1);
		addressEntity.setCity("Culver");
		addressEntity.setZip("97451");

		medicineEntity.setMedecineName("aznol");
		medicineEntity.setId(0);

		medicationEntity.setDosage(350);
		medicationEntity.setMedicineEntity(medicineEntity);
		medicationEntity.setId(0);

		allergeneEntity.setAllergene("nillacilan");
		allergeneEntity.setId(0);
		allergeneEntities.add(allergeneEntity);

		NameEntity adultName = new NameEntity();
		adultName.setFirstName("John");
		adultName.setLastName("Boyd");

		NameEntity childName = new NameEntity();
		childName.setFirstName("Tenley");
		childName.setLastName("Boyd");

		adult.setBirthdate(LocalDate.of(1984, 3, 6));
		adult.setAddressEntity(addressEntity);
		adult.setPhone(phone);
		adult.setAllergies(allergeneEntities);
		adult.setMedications(medicationEntities);
		adult.setNameEntity(adultName);

		PersonEntity child = new PersonEntity();
		child.setMedications(medicationEntities);
		child.setAddressEntity(addressEntity);
		child.setPhone(phone);
		child.setAllergies(allergeneEntities);
		child.setMedications(medicationEntities);
		child.setBirthdate(LocalDate.of(2012, 2, 18));
		child.setNameEntity(childName);

		personEntityList2.add(child);
		personEntityList2.add(adult);
		personEntityList1.add(adult);

		try {
			medicalrecords = UTHelper.stringToObject(UTHelper.readFileAsString("requestBody/MedicalRecords/medicalrecords.json"), Medicalrecords.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	void MedicalRecords_OK_returnPersonsRsp() {
		// GIVEN
		String firstName = "John";
		String lastName = "Boyd";
		when(medicationServices.personRepository.findPersonEntityByNameEntityLike(firstName, lastName)).thenReturn(adult);
		when(medicationServices.medicationRepository.saveAll(any(ArrayList.class))).thenReturn(null);
		when(medicationServices.allergeneRepository.saveAll(any(ArrayList.class))).thenReturn(null);

		// WHEN
		PersonsRsp personsRsp = medicationServices.addMedicalRecords(medicalrecords);

		// THEN
		verify(medicationServices.personRepository, times(1)).findPersonEntityByNameEntityLike(firstName, lastName);
		assertThat(personsRsp).isInstanceOf(PersonsRsp.class);
		try {
			assertThat(personsRsp).isEqualTo(UTHelper.stringToObject(UTHelper.readFileAsString("responseBody/ChildAlert/Addresses.json"), PersonsRsp.class));
		} catch (IOException e) {
			e.printStackTrace();

		}
	}


}

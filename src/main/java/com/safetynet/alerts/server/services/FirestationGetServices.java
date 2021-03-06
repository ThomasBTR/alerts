package com.safetynet.alerts.server.services;

import com.safetynet.alerts.server.constants.EActionsProceedConstants;
import com.safetynet.alerts.server.constants.EObjectConstants;
import com.safetynet.alerts.server.constants.EStatusConstants;
import com.safetynet.alerts.server.database.entities.PersonEntity;
import com.safetynet.alerts.server.database.repositories.PersonRepository;
import com.safetynet.alerts.server.mapping.IFirestationMapper;
import io.swagger.model.Firestation;
import io.swagger.model.PersonReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FirestationGetServices {

	@Autowired
	public PersonRepository personRepository;


	private static final Logger logger = LoggerFactory.getLogger(FirestationGetServices.class);

	FirestationGetServices(){}

	public FirestationGetServices(PersonRepository personRepository){
		this.personRepository = personRepository;
	}

	public Firestation getPersonsInfosFromFirestationID(Integer stationNumber) {
		logger.debug(EActionsProceedConstants.FIRESTATIONINFOS_FROMID_START.getValue(), stationNumber);

		List<PersonEntity> personEntities = personRepository.findPersonEntitiesByAddressEntityStation(stationNumber);
		logger.debug(EStatusConstants.DATA_RECEIVED_LIST.getValue(), EObjectConstants.PERSON.getObject(), personEntities.size());
		int adultCount = 0;
		int childCount = 0;

		for (final PersonEntity personEntity : personEntities) {
			if (personEntity.getBirthdate().isAfter(LocalDate.now().minusYears(18))) {
				++childCount;
			} else {
				++adultCount;
			}
		}

		List<PersonReq> persons = IFirestationMapper.INSTANCE.personEntityListToFirestationPerson(personEntities);

		Firestation firestationResponseBody = new Firestation();

		firestationResponseBody.setAdultCount(adultCount);
		firestationResponseBody.setChildCount(childCount);
		firestationResponseBody.setPersons(persons);

		logger.info(EActionsProceedConstants.FIRESTATIONINFOS_FROMID_SUCCESS.getValue(), stationNumber, persons.size(), childCount, adultCount);

		return firestationResponseBody;
	}


}

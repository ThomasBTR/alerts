package com.safetynet.alerts.SIT;


import com.safetynet.alerts.UTHelper;
import com.safetynet.alerts.server.controllers.FirestationController;
import com.safetynet.alerts.server.services.FirestationGetServices;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@SpringBootTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(controllers = {FirestationController.class, FirestationGetServices.class})
@ExtendWith(SpringExtension.class)
class FirestationControllerPostFirestationsIT {

	private MockMvc mvc;

	@Autowired
	private FirestationController firestationController;

	@BeforeAll
	public void prepare() {
		mvc = MockMvcBuilders.standaloneSetup(firestationController).build();
		Assertions.assertNotNull(mvc);
	}

	@Test
	void test_200_insert1Firestation() throws Exception {
		mvc.perform(post("/firestations")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(UTHelper.readFileAsString("requestBody/Firestations/firestations.json")))
				.andExpect(status().isOk());

		assertThat(true).isTrue();
	}


}

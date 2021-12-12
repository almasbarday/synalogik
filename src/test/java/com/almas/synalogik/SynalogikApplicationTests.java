package com.almas.synalogik;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
	    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
	    classes = {SynalogikApplication.class},
	    properties = {"application.properties"})
@ActiveProfiles("test")
class SynalogikApplicationTests {

	@Test
	void contextLoads() {
		assertTrue(true);
	}

}

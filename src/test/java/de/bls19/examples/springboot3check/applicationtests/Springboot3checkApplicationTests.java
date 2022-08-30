package de.bls19.examples.springboot3check.applicationtests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class Springboot3checkApplicationTests {

	@Value("${test.value}")
	String myTestValue;

	@Autowired
	ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		Assertions.assertEquals("aValue", myTestValue);
		Assertions.assertNotNull(applicationContext);
	}

}

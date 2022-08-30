package de.bls19.examples.springboot3check.values;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestValues {

  @Value("${test.value}")
  String aValue;

  @Test
  public void testInjectValueFromProperties() {
    Assertions.assertEquals("aValue", this.aValue);
  }

}

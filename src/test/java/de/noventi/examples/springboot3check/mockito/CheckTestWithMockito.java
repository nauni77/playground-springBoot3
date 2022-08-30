package de.noventi.examples.springboot3check.mockito;

import de.noventi.examples.springboot3check.configuration.TestConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackageClasses = TestConfiguration.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CheckTestWithMockito {

  @Autowired
  RandomTestBean myTestBean;

  @MockBean
  RandomTestBean randomTestBeanMockito;

  @Test
  public void testMyBean() {
    for (int i = 0; i < 1000; i++) {
      int randomValue = myTestBean.generateRandomInteger();
      Assertions.assertTrue(randomValue <= 100, "needs to be smaller/equal 100");
      Assertions.assertTrue(randomValue >= 0, "needs to be bigger/equal 0");
    }
  }

  @Test
  public void testMyMockitoTestBean() {
    Mockito.when(randomTestBeanMockito.generateRandomInteger()).thenReturn(200);
    for (int i = 0; i < 10; i++) {
      int randomValue = randomTestBeanMockito.generateRandomInteger();
      Assertions.assertEquals(200, randomValue, "needs to be 200, because of Mockito");
    }
  }

}

package de.bls19.examples.springboot3check.dynamicproxybean;

import de.bls19.examples.springboot3check.dynamicproxybean.myservice.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.util.UUID;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@Slf4j
public class TestMyService {

  @Autowired
  ApplicationContext applicationContext;

  @Autowired
  MyServiceImplA serviceA;

  @Autowired
  MyServiceImplB serviceB;

  // only works if one Service is @Primary
//  @Autowired
//  MyServiceIF serviceAutomatic;

  @Test
  @Order(1)
  public void testDirectAccessToImplBean() {
    UUID uuid = UUID.randomUUID();
    Assertions.assertEquals(MyServiceImplA.REPEAT_PREFIX + uuid.toString(), serviceA.repeat(uuid.toString()));
    Assertions.assertEquals(MyServiceImplB.REPEAT_PREFIX + uuid.toString(), serviceB.repeat(uuid.toString()));
  }

  @Test
  @Order(10)
  public void testAddAnotherServiceAInstance() {
    String beanNameServiceC = "serviceC";
    UUID uuid = UUID.randomUUID();

    try {
      applicationContext.getBean(beanNameServiceC);
      Assertions.fail("this bean should NOT be found!");
    } catch (NoSuchBeanDefinitionException nsbdException) {

    }

    try {
      applicationContext.getBean(MyServiceIF.class);
      Assertions.fail("this bean should NOT work - because it is not clean which implementation!");
    } catch (NoUniqueBeanDefinitionException nubdException) {

    }

    ((GenericWebApplicationContext) applicationContext).registerBean(
        beanNameServiceC, MyServiceIF.class, () -> new MyServiceImplC(), new BeanDefinitionCustomizer() {
          @Override
          public void customize(BeanDefinition bd) {
            bd.setPrimary(true);
          }
        });

    try {
      MyServiceIF theService = (MyServiceIF) applicationContext.getBean(beanNameServiceC);
      Assertions.assertEquals(MyServiceImplC.REPEAT_PREFIX + uuid.toString(), theService.repeat(uuid.toString()));

      theService = applicationContext.getBean(MyServiceImplC.class);
      Assertions.assertEquals(MyServiceImplC.REPEAT_PREFIX + uuid.toString(), theService.repeat(uuid.toString()));

      // because of Primary this is unique and should work
      theService = applicationContext.getBean(MyServiceIF.class);
      Assertions.assertEquals(MyServiceImplC.REPEAT_PREFIX + uuid.toString(), theService.repeat(uuid.toString()));

    } catch (NoSuchBeanDefinitionException nsbdException) {
      Assertions.fail("this bean SHOULD be found!");
    }

    ((GenericWebApplicationContext) applicationContext).removeBeanDefinition(beanNameServiceC);

    try {
      applicationContext.getBean(beanNameServiceC);
      Assertions.fail("this bean should NOT be found!");
    } catch (NoSuchBeanDefinitionException nsbdException) {

    }
  }

  @Test
  @Order(20)
  public void testGetServiceAddedViaBeanRegistration() {
    UUID uuid = UUID.randomUUID();
    MyServiceIF theService = applicationContext.getBean(MyServiceImplViaBeanRegistration.class);

    Assertions.assertEquals(MyServiceImplViaBeanRegistration.REPEAT_PREFIX + uuid.toString(),
        theService.repeat(uuid.toString()));

  }

  }

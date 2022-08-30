package de.bls19.examples.springboot3check.dynamicproxybean;

import de.bls19.examples.springboot3check.dynamicproxybean.myservice.MyServiceIF;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.util.UUID;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@Slf4j
public class TestMyServiceViaProxyBean {

  @Autowired
  ApplicationContext applicationContext;


  @Test
  @Order(10)
  public void testSpecializedProxyBean() {

    String beanName = "spezializedProxy";
    UUID uuid = UUID.randomUUID();

    ((GenericWebApplicationContext) applicationContext).registerBean(
        beanName, MyServiceIF.class, () -> new GlobalProxyServiceBeanSpezialized());

    MyServiceIF myService = (MyServiceIF) applicationContext.getBean(beanName);
    System.out.println(myService.repeat(uuid.toString()));

    ((GenericWebApplicationContext) applicationContext).removeBeanDefinition(beanName);

  }

  @Test
  @Order(20)
  public void testUnspecializedProxyBean() {

    String beanName = "unspezializedProxy";
    UUID uuid = UUID.randomUUID();

    // this is not working, because "Unspezialized" to not implement MyServiceIF - only GlobalServiceIF.class
//    ((GenericWebApplicationContext) applicationContext).registerBean(
//        beanName, MyServiceIF.class, () -> new GlobalProxyServiceBeanUnspezialized());

    ((GenericWebApplicationContext) applicationContext).registerBean(
        beanName, GlobalServiceIF.class, () -> new GlobalProxyServiceBeanUnspezialized());

    try {
      MyServiceIF myService = (MyServiceIF) applicationContext.getBean(beanName);
      Assertions.fail("this should cause a ClassCastException!");
    } catch (ClassCastException ccException) {

    }

    ((GenericWebApplicationContext) applicationContext).removeBeanDefinition(beanName);
  }

  }

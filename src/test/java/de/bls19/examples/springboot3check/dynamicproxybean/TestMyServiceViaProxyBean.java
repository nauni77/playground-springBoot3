package de.bls19.examples.springboot3check.dynamicproxybean;

import de.bls19.examples.springboot3check.dynamicproxybean.myservice.MyServiceIF;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ClassUtils;
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

  @Test
  @Order(50)
  public void testRealProxyBean() {

    UUID uuid = UUID.randomUUID();

    Class interfaceClass = MyServiceIF.class;
    String beanName = "proxy" + ClassUtils.getShortNameAsProperty(interfaceClass);
    log.info("bean name: " + beanName);

    GenericBeanDefinition dispatcherDefinition = new GenericBeanDefinition();
    dispatcherDefinition.setBeanClass(interfaceClass);

    ConstructorArgumentValues construcorArgs = new ConstructorArgumentValues();
    construcorArgs.addGenericArgumentValue(interfaceClass.getClassLoader());
    construcorArgs.addGenericArgumentValue(interfaceClass);
    dispatcherDefinition.setConstructorArgumentValues(construcorArgs);

    dispatcherDefinition.setFactoryBeanName(DispatcherFactory.BEAN_NAME);
    dispatcherDefinition.setFactoryMethodName(DispatcherFactory.FACTORY_METHOD_NAME);
    dispatcherDefinition.setPrimary(true);

    ((GenericWebApplicationContext) applicationContext)
        .registerBeanDefinition(beanName, dispatcherDefinition);

    try {

      MyServiceIF myService = (MyServiceIF) applicationContext.getBean(beanName);
      log.info("by name => arg: YES!!! => result: " + myService.repeat("YES!!!"));

      myService = applicationContext.getBean(MyServiceIF.class);
      String result = myService.repeat("YES!!!");
      log.info("by clazz => arg: YES!!! => result: " + result);
      Assertions.assertEquals("invoke was called", result);

      // now the dispatcher will use the defined bean to answer the question
      // ... dependent on the parameter!
      result = myService.repeat("A!!!");
      log.info("by clazz => arg: A!!! => result: " + result);
      Assertions.assertEquals("A: A!!!", result);
      result = myService.repeat("B!!!");
      log.info("by clazz => arg: B!!! => result: " + result);
      Assertions.assertEquals("B: B!!!", result);
      result = myService.repeat("C!!!");
      log.info("by clazz => arg: C!!! => result: " + result);
      Assertions.assertEquals("C: C!!!", result);

    } catch (ClassCastException ccException) {

    }

    ((GenericWebApplicationContext) applicationContext).removeBeanDefinition(beanName);

  }

}

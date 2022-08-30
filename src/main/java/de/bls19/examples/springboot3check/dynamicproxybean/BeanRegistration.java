package de.bls19.examples.springboot3check.dynamicproxybean;

import de.bls19.examples.springboot3check.dynamicproxybean.myservice.MyServiceImplViaBeanRegistration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * For some explanations about this class you can read:
 * https://www.javaprogramto.com/2020/05/spring-beandefinitionregistrypostprocessor.html
 * <p>
 * Howto get the configuration values from properties file:
 * https://scanningpages.wordpress.com/2017/07/28/spring-dynamic-beans/
 * </p>
 * <p>
 * At this class all configured WebClients will be added.
 * </p>
 */
@Slf4j
@Configuration
public class BeanRegistration {

  /**
   * Create a beanPostProcessor , @Bean for adding the dynamic beans.
   * <p>
   * At this position no injection is possible. Because of this I need to make
   * it possible to initialize the ReflectionsService without Spring-Bean
   * injection.
   */
  @Bean
  static BeanDefinitionRegistryPostProcessor beanRegistryPostProcessor(final ConfigurableEnvironment environment) {

    return new BeanDefinitionRegistryPostProcessor() {

      public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanRegistry) throws BeansException {

        // for now don't care how to find the services - just register the java proxy as bean
        log.info("execute post process bean DEFINITION REGISTRY");

        // 1.1 createBean definition via BUILDER
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
            .genericBeanDefinition(MyServiceImplViaBeanRegistration.class)
            .setPrimary(false)
            .setLazyInit(true);

        BeanDefinition bd = builder.getBeanDefinition();

        // 1.2 createBean definition via GenericBeanDefinition
        GenericBeanDefinition myBeanDef = new GenericBeanDefinition();
        myBeanDef.setBeanClass(MyServiceImplViaBeanRegistration.class);
        myBeanDef.setPrimary(false);
        myBeanDef.setLazyInit(true);

        // 2. register bean
        beanRegistry.registerBeanDefinition(MyServiceImplViaBeanRegistration.BEAN_NAME, myBeanDef);

      }

      public void postProcessBeanFactory(ConfigurableListableBeanFactory arg0) throws BeansException {
        log.debug("execute post process bean FACTORY");
      }

    };
  }

}

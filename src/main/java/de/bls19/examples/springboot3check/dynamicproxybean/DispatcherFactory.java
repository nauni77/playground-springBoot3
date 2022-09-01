package de.bls19.examples.springboot3check.dynamicproxybean;

import de.bls19.examples.springboot3check.dynamicproxybean.myservice.MyServiceImplA;
import de.bls19.examples.springboot3check.dynamicproxybean.myservice.MyServiceImplB;
import de.bls19.examples.springboot3check.dynamicproxybean.myservice.MyServiceImplC;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Service(value = DispatcherFactory.BEAN_NAME)
@Slf4j
public class DispatcherFactory {

  public static final String BEAN_NAME = "dispatcherFactory";
  public static final String FACTORY_METHOD_NAME = "createDispatcherProxy";

  ApplicationContext applicationContext;

  public DispatcherFactory(ApplicationContext appContext) {
    this.applicationContext = appContext;
  }

  /**
   * Bean factory method, that creates a dynamic proxy implementing the given interface class.
   * <p>
   * The following prerequisites have to be fullfilled: <br>
   * The implementations have to be annotated with @NesServerImpl with the correct storage service
   * type <br>
   * The eprescription id parameter in the method has to be annotated with @EprescriptionIdParam
   * </p>
   *
   * @param classLoader ClassLoader
   * @param interfaceClass Class
   * @param <T> Type
   * @return T
   */
  public <T> T createDispatcherProxy(ClassLoader classLoader, Class<T> interfaceClass) {
    return (T) Proxy.newProxyInstance(classLoader, new Class<?>[] {interfaceClass},
        new ProxyHandler(interfaceClass, applicationContext));
  }

  private class ProxyHandler implements InvocationHandler {

    ApplicationContext applicationContext;

    Class beanClazz;

    boolean postConstructWasExecuted = false;

    public ProxyHandler(Class beanClazz, ApplicationContext applicationContext) {
      this.beanClazz = beanClazz;
      this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void postConstruct() {
      this.postConstructWasExecuted = true;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
      this.applicationContext = applicationContext;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      // just log if the lifecycle methods are called
      // => so this lifecycle will not be used if you register a bean like this.
      log.info("postConstruct was executed: " + postConstructWasExecuted);

      // at this example it's only one type of proxy ...
      // so always one argument and a String! ;-)
      // and the return value is always a STRING
      String argument = (String) args[0];

      if (argument.contains("A"))
        return applicationContext.getBean(MyServiceImplA.class).repeat(argument);
      else if (argument.contains("B"))
        return applicationContext.getBean(MyServiceImplB.class).repeat(argument);
      else if (argument.contains("C"))
        return applicationContext.getBean(MyServiceImplC.class).repeat(argument);

      log.info("invoke was called!!!");
      return "invoke was called";
    }
  }

}

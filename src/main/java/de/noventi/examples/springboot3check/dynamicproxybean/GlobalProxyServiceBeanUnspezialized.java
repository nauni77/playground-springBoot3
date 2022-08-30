package de.noventi.examples.springboot3check.dynamicproxybean;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * This will delegate the calls to the instances of existing/ configured
 * Beans/ WebClients.
 * <p/>
 * While it is redirected this will check which servers still provide this
 * service. And if it is not responding this service will retry. If it's
 * not returning several times it will try another server for this service.
 * <p/>
 * For more information about this:
 * https://www.baeldung.com/java-dynamic-proxies
 */
@Slf4j
@NoArgsConstructor
public class GlobalProxyServiceBeanUnspezialized implements InvocationHandler, GlobalServiceIF {

  Class<? extends GlobalServiceIF> theServiceInterface = null;

  public GlobalProxyServiceBeanUnspezialized(Class<? extends GlobalServiceIF> implementingService) {
    this.theServiceInterface = implementingService;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    log.info("MarvinService2 proxy invoke method is called " +
        "(object: " + proxy.getClass().getName() + ", method: " + method.getName() + ")");
    if (theServiceInterface != null)
      log.info("this proxy is used for interface : " + theServiceInterface.getName());
    else
      log.info("this proxy is used for interface : UNDEFINED");
    return "INVOKE WAS CALLED";
  }

}

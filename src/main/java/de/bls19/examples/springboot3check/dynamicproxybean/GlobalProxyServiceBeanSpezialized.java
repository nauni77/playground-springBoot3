package de.bls19.examples.springboot3check.dynamicproxybean;

import de.bls19.examples.springboot3check.dynamicproxybean.myservice.MyServiceIF;
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
public class GlobalProxyServiceBeanSpezialized implements InvocationHandler, MyServiceIF {

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    log.info("MarvinService2 proxy invoke method is called " +
        "(object: " + proxy.getClass().getName() + ", method: " + method.getName() + ")");
    return "INVOKE WAS CALLED";
  }

  @Override
  public String repeat(String someWord) {
    return "haha, this is used ... no invoke";
  }
}

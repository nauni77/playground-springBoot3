# playground SpringBoot3

This project is used to check some problems with SpringBoot.

## java dynamic proxy registered as bean

You need to define the class, if you register programmatically a bean. See the examples at `java/de/bls19/examples/springboot3check/dynamicproxybean`. But if you have a dynamic java proxy (see [baeldung java-dynamic-proxies](https://www.baeldung.com/java-dynamic-proxies)) you need to create these proxies with:

```java
    MyServiceIF myService = (MyServiceIF) Proxy.newProxyInstance(
        GlobalProxyServiceBeanUnspezialized.class.getClassLoader(),
        new Class[] { MyServiceIF.class },
        new GlobalProxyServiceBeanUnspezialized(MyServiceIF.class));
```

Examples: `java/de/bls19/examples/springboot3check/dynamicproxybean/TestProxyNoBean.java`

### unresolved question

How can I register a dynamic java proxy as a bean. Or in other ways how can I register a bean without creating this bean via spring - just register the created object. I know this will not keep the default lifecycle of spring.

## mockito

Some simple mockito tests at `java/de/bls19/examples/springboot3check/mockito` - just to explain how it works and show that it works with SpringBoot3.

## serialization

Just to make sure the serialization class is still included in SpringBoot3.

`java/de/bls19/examples/springboot3check/serialization`

## test configuration

Check how it works to run a test without web container. See: `java/de/bls19/examples/springboot3check/configuration`.

## application tests

Check if the Tests work with SpringBootTest annotation - running the complete container.

`java/de/bls19/examples/springboot3check/applicationtests`
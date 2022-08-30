package de.bls19.examples.springboot3check.dynamicproxybean.myservice;

public class MyServiceImplViaBeanRegistration implements MyServiceIF {

  public static final String BEAN_NAME = "viaBeanRegistration";
  public static final String REPEAT_PREFIX = "viaBeanRegistration: ";

  @Override
  public String repeat(String someWord) {
    return REPEAT_PREFIX + someWord;
  }

}

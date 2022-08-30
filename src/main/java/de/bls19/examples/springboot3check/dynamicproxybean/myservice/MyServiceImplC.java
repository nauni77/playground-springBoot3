package de.bls19.examples.springboot3check.dynamicproxybean.myservice;

import org.springframework.stereotype.Service;

@Service
public class MyServiceImplC implements MyServiceIF {

  public static final String REPEAT_PREFIX = "C: ";

  @Override
  public String repeat(String someWord) {
    return REPEAT_PREFIX + someWord;
  }

}

package de.noventi.examples.springboot3check.dynamicproxybean.myservice;

import org.springframework.stereotype.Service;

@Service
public class MyServiceImplA implements MyServiceIF {

  public static final String REPEAT_PREFIX = "A: ";

  @Override
  public String repeat(String someWord) {
    return REPEAT_PREFIX + someWord;
  }
}

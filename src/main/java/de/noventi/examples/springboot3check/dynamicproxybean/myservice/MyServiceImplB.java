package de.noventi.examples.springboot3check.dynamicproxybean.myservice;

import org.springframework.stereotype.Service;

@Service
public class MyServiceImplB implements MyServiceIF {

  public static final String REPEAT_PREFIX = "B: ";

  @Override
  public String repeat(String someWord) {
    return REPEAT_PREFIX + someWord;
  }

}

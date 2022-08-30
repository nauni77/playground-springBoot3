package de.noventi.examples.springboot3check.dynamicproxybean.myservice;

import de.noventi.examples.springboot3check.dynamicproxybean.GlobalServiceIF;

public interface MyServiceIF extends GlobalServiceIF {

  /**
   * Repeat the given word - okay it's not really a nice example. But it's just to
   * write a stupid test.
   *
   * @param someWord word to repeat
   * @return implementation followed by the repeated word
   */
  String repeat(String someWord);

}

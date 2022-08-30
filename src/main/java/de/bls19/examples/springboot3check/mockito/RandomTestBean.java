package de.bls19.examples.springboot3check.mockito;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomTestBean {

  public int generateRandomInteger() {
    return generateRandomInteger(0, 100);
  }

  public int generateRandomInteger(int min, int max) {
    Random random = new Random();
    return random.nextInt(max - min) + min;
  }

}

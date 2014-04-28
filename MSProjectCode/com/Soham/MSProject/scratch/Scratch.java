package com.Soham.MSProject.scratch;

import java.util.Random;

public class Scratch 
{
  public static void main( String [] args )
  {
    Random r = new Random();
    for(int i = 0; i < 5; i++) {
      System.out.println("new random "+ r.nextDouble());
    }
  }
}
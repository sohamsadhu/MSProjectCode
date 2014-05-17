package com.Soham.MSProject.scratch;

public class Scratch 
{
  public static void main( String [] args )
  {
    String s = new String("The quick brown fox jumps over the lazy dog");
    byte[] arr = s.getBytes();
    System.out.println("length is "+ (s.length() * 8));
    for( byte a : arr ) {
      System.out.printf("%02X", a);
    }
  }
}
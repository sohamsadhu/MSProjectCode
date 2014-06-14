package com.Soham.MSProject.scratch;

public class Scratch 
{  
  public static void main( String [] args )
  {
    String a = new String("The quick brown fox jumps over the lazy dog.");
    byte[] by = a.getBytes();
    for(byte b : by) {
      System.out.printf("%02X", b);
    }
    System.out.println(" length "+ (by.length * 8));
  }
}
package com.Soham.MSProject.scratch;

import org.apache.commons.codec.binary.Hex;

public class Scratch 
{  
  public static void main( String [] args )
  {
    byte[][] b1 = new byte[][]{new byte[]{0x00, 0x01}, new byte[]{0x4A, 0x4A}};
    System.out.println(Hex.encodeHexString( b1[1] ));
  }
}
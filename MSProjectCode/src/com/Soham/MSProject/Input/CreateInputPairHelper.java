package com.Soham.MSProject.Input;

import java.io.UnsupportedEncodingException;

public class CreateInputPairHelper
{
  public String[] createListOfFlippedBytes( final String seed, final int flips )
  {    
    if( seed == null ) 
    {
      System.out.println("Input string seed should not be null.");
      return null;
    }
    if (seed.isEmpty()) {
      return listFromEmptyString( flips );
    }
    String[] list_flipped_bits = new String[flips];
    return list_flipped_bits;
  }
  
  public String[] listFromEmptyString( final int flips )
  {
    String[] listFromEmptyChar = new String[ flips ];
    listFromEmptyChar[0] = "";
    for( int i = 1; i < flips ; i++ ) {
      listFromEmptyChar[i] = Character.toString(( char )( 31 + i));
    }
    return listFromEmptyChar;
  }
  
  public void test( String something )
  {
    byte[] input;
    try
    {
      input = something.getBytes("utf-8");
      for( byte ip : input ) {
        System.out.println(String.format("%8s", Integer.toBinaryString(ip & 0xFF)).replace(' ', '0'));
      }
      System.out.println("hello"+ new String(input, "utf-8") +"World");
      System.out.println("hello"+ Character.toString(( char )33) +"World");
    }
    catch (UnsupportedEncodingException e) 
    {
      e.printStackTrace();
      System.out.println("The given seed could not be encoded. Exiting now.");
      return;
    }    
  }
  
  public static void main( String [] args )
  {
    CreateInputPairHelper cih = new CreateInputPairHelper();
    cih.test("转");
    cih.test(" ");
  }
}
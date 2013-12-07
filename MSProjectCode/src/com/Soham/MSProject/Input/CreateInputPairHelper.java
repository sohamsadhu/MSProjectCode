package com.Soham.MSProject.Input;

import java.io.UnsupportedEncodingException;

public class CreateInputPairHelper
{
  /**
   * This function will return an array strings with at most 21 elements. If flips > 20 or flips < 0, then
   * null returned suggesting failure. For 0 flip, list with empty string, for 1 empty string and space.
   * @param flips number of String elements requested, start from empty string followed by space and so on.
   * @return String[].length <= 21, first element is empty string, second element is space,
   * third element is which comes after space in ASCII table '!', and so till twentieth element
   * as they appear in ASCII table.
   */
  public String[] listFromEmptyString( final int flips )
  {
    if( flips > 20 || flips < 0 ) 
    {
      System.err.println("Please request at least 0 or less than 21 flips.");
      return null;
    }
    String[] listFromEmptyChar = new String[ flips + 1 ];
    listFromEmptyChar[0] = "";
    for( int i = 1; i < (flips + 1); i++ ) {
      listFromEmptyChar[i] = Character.toString(( char )( 31 + i));
    }
    return listFromEmptyChar;
  }
  
  // The question here is when the flips are more than 8 bits, then how do I shift to the next byte.
  public String[] flipBitsFromEnd( final byte[] seed, final int flips )
  {
    int toggles = (flips - 8) > 0 ? 8 : flips;
    System.out.println("Toggles "+ toggles);
    String[] flipped_list = new String[ flips + 1 ];
    flipped_list[0] = seed.toString();
    int seed_length = seed.length;
    byte last = seed[ seed_length - 1 ];
    byte[] temp = seed;
    for( int i = 0; i < toggles; i++ )
    {
      byte b = (byte) (last ^ (1 << i));
      temp[seed_length - 1] = b;
      flipped_list[ i + 1 ] = temp.toString();
    }
    if( flips <= toggles ) {
      return flipped_list;
    } 
    else 
    {
      byte second_last = seed[ seed_length - 2 ];
      temp = seed;
      for( int i = 9; i < flips; i++ )
      {
        byte b = (byte) (second_last ^ (1 << i));
        temp[seed_length - 1] = b;
        flipped_list[ i + 1 ] = temp.toString();
      }
      return flipped_list;
    }
  }
  
  /**
   * Returns the array of strings with bits toggled from end. For example if a string provided 
   * happens to be byte 10000000 then at flips 2, the return array would be [10000000, 10000001,
   * 10000010]. So the original seed is the head of the list, and the rest follow. If flip is 
   * @param seed The original string, which will be first entry in the return array.
   * @param flips Determines the number of flips to bits and hence the length of return array.
   * If the number of flips is zero, then the list is only the seed string with length one.
   * If the number of flips > number of bits present in seed, then all the bits are flipped.
   * @return Array of string/s that have one bit toggled from the original one. The toggling
   * starts from the end and proceeds towards head.  
   */
  public String[] createListOfFlippedBytes( final String seed, final int flips )
  {    
    if( seed == null || flips > 21 || flips < 0 ) 
    {
      System.err.println("Please correct the input. Seed cannot be null, and number of flips"
          + "is limited at between 1 and 20 included.");
      return null;
    }
    if( seed.isEmpty() ) {
      return listFromEmptyString( flips );
    }
    if( 0 == flips ) {
      return new String[]{ seed };
    }
    byte[] input;
    try
    {
      input = seed.getBytes("utf-8");
      if( flips > (input.length * 8)) { 
        flipBitsFromEnd( input, input.length * 8 );
      } else {
        flipBitsFromEnd( input, flips );
      }
    }
    catch (UnsupportedEncodingException e) 
    {
      e.printStackTrace();
      System.out.println("The given seed could not be encoded. Exiting now.");
      return null;
    }
    return null;
  }
  
  public void test( String something )
  {
    byte[] input;
    try
    {
      input = something.getBytes("utf-8");
      byte b = 1 << 0;
      System.out.println(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
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
    //cih.test("转");
    cih.test("a");
  }
}
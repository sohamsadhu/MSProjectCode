package com.Soham.MSProject.Input;

import java.io.UnsupportedEncodingException;

public class CreateInputFiles 
{
  public byte[][] getStartingFlippedBytes( byte[] input, String num_of_flips )
  {
    int flips = Integer.parseInt( num_of_flips );
    byte[][] flipped_bytes = new byte[ flips + 1 ][ input.length ];
    flipped_bytes[0] = input;
    int i = 0, j, byte_pos;
    while( i < flips )
    {
      j = i % 8;
      byte_pos = i / 8;
      byte [] temp = new byte[ input.length ];
      System.arraycopy( input, 0, temp, 0, input.length );
      temp[ byte_pos ] = (byte) (temp[ byte_pos ] ^ (0x80 >> j));
      flipped_bytes[ i + 1 ] = temp;
      i++;
    }
    return flipped_bytes;
  }
  
  public void createStartingFlippedFiles( String input, String num_of_flips )
  {
    byte[] ip;
    try 
    {
      ip = input.getBytes("utf-8");
      byte[][] flipped_collection = getStartingFlippedBytes( ip, num_of_flips );
      for( byte[] b : flipped_collection )
      {
        for( byte op : b ){
          System.out.printf("%02X",op);
        }
        System.out.println();
      }
    } 
    catch( UnsupportedEncodingException uex ) {
      System.err.println("Something went wrong in encoding the starting flips to bytes.");
    }    
  }
  
  public void createMiddleFlippedFiles( String input, String num_of_flips )
  {}
  
  public void createEndingFlippedFiles( String input, String num_of_flips )
  {}
  
  public void createFile( String input, String end, String num_of_flips )
  {
    switch( end )
    {
      case "Starting":
        createStartingFlippedFiles( input, num_of_flips );
        break;
      case "Middle":
        createMiddleFlippedFiles( input, num_of_flips );
        break;
      case "Ending":
        createEndingFlippedFiles( input, num_of_flips );
        break;
    }
  }
}
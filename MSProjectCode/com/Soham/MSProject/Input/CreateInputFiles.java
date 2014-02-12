package com.Soham.MSProject.Input;

import org.apache.commons.codec.binary.Hex;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
  
  public void writePairsToFiles( String folder, byte[][] flipped_bits )
  {
    int length = flipped_bits.length ;
    for( int i = 1; i < length; i++ )
    {
      File f = new File("./"+ folder +"/"+ i +".txt");
      f.getParentFile().mkdirs();
      try 
      {
        BufferedWriter bw = new BufferedWriter(new FileWriter(f.getAbsoluteFile()));
        bw.write( Hex.encodeHexString( flipped_bits[0] ));
        bw.newLine();
        bw.write( Hex.encodeHexString( flipped_bits[i] ));
        bw.close();
      } 
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  public void createStartingFlippedFiles( String input, String num_of_flips )
  {
    byte[] ip;
    try 
    {
      ip = input.getBytes("utf-8");
      byte[][] flipped_collection = getStartingFlippedBytes( ip, num_of_flips );
      writePairsToFiles( "Input/Start", flipped_collection);      
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
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
  
  public byte[][] getMiddleFlippedBytes( byte[] input, String num_of_flips )
  {
    int flips = Integer.parseInt( num_of_flips );
    byte[][] flipped_bytes = new byte[ flips + 1 ][ input.length ];
    flipped_bytes[0] = input;
    byte[] combinations = new byte[]{(byte)0x80, (byte)0x40, (byte)0x20, 
        (byte)0x10, (byte)0x08, (byte)0x04, (byte)0x02, (byte)0x01};
    int start_pos = ((input.length * 8) / 2) - (flips / 2);
    int end_pos   = start_pos + flips;
    int byte_pos, bit_pos, i = 1;
    while( start_pos < end_pos )
    {
      byte_pos = start_pos / 8;
      bit_pos  = start_pos % 8;
      byte [] temp = new byte[ input.length ];
      System.arraycopy( input, 0, temp, 0, input.length );
      temp[ byte_pos ] = (byte)(temp[ byte_pos ] ^ combinations[ bit_pos ]);
      flipped_bytes[i] = temp;
      i++;
      start_pos++;
    }
    return flipped_bytes;
  }
  
  public byte[][] getEndFlippedBytes( byte[] input, String num_of_flips )
  {
    int flips = Integer.parseInt( num_of_flips );
    byte[][] flipped_bytes = new byte[ flips + 1 ][ input.length ];
    flipped_bytes[0] = input;
    int i = (input.length * 8) - 1;
    int end_pos = i - flips;
    int j, byte_pos;
    int k = 1;
    while( i > end_pos )
    {
      j = i % 8;
      byte_pos = i / 8;
      byte [] temp = new byte[ input.length ];
      System.arraycopy( input, 0, temp, 0, input.length );
      temp[ byte_pos ] = (byte) (temp[ byte_pos ] ^ (0x80 >> j));
      flipped_bytes[ k ] = temp;
      k++;
      i--;
    }
    return flipped_bytes;
  }
  
  public void writePairsToFiles( String folder, byte[][] flipped_bits )
  {
    int length = flipped_bits.length ;
    for( int i = 1; i < length; i++ )
    {
      File f = new File("./Input/"+ folder +"/"+ i +".txt");
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
      writePairsToFiles( "Start", flipped_collection);      
    } 
    catch( UnsupportedEncodingException uex ) {
      System.err.println("Something went wrong in encoding the starting flips to bytes.");
    }    
  }
  
  public void createMiddleFlippedFiles( String input, String num_of_flips )
  {
    byte[] ip;
    try 
    {
      ip = input.getBytes("utf-8");
      byte[][] flipped_collection = getMiddleFlippedBytes( ip, num_of_flips );
      writePairsToFiles( "Middle", flipped_collection);      
    } 
    catch( UnsupportedEncodingException uex ) {
      System.err.println("Something went wrong in encoding the starting flips to bytes.");
    }
  }
  
  public void createEndingFlippedFiles( String input, String num_of_flips )
  {
    byte[] ip;
    try 
    {
      ip = input.getBytes("utf-8");
      byte[][] flipped_collection = getEndFlippedBytes( ip, num_of_flips );
      writePairsToFiles( "End", flipped_collection);      
    } 
    catch( UnsupportedEncodingException uex ) {
      System.err.println("Something went wrong in encoding the starting flips to bytes.");
    }
  }
  
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
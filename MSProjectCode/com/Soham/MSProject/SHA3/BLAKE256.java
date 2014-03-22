package com.Soham.MSProject.SHA3;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class BLAKE256 
{
  public static final int WORD_BITLENGTH = 32;
  public static final int OUTPUT_BYTELENGTH = 32;
  
  public static final int[] INITIAL_VECTOR = { 0x6A09E667, 0xBB67AE85, 0x3C6EF372, 0xA54FF53A,
    0x510E527F, 0x9B05688C, 0x1F83D9AB, 0x5BE0CD19};
  
  public static final int[] CONSTANTS = {0x243F6A88, 0x85A308D3, 0x13198A2E, 0x03707344,
    0xA4093822, 0x299F31D0, 0x082EFA98, 0xEC4E6C89, 0x452821E6, 0x38D01377, 0xBE5466CF,
    0x34E90C6C, 0xC0AC29B7, 0xC97C50DD, 0x3F84D5B5, 0xB5470917};
  
  public ArrayList<Byte> padding( ArrayList<Byte> msg )
  {
    byte[] padding_bytes = new byte[]{ (byte)0x80, (byte)0x00, (byte)0x01, (byte)0x81 };
    int bytes_append = msg.size()  % 64;
    if( bytes_append < 56 )
    {
      if( bytes_append <= 54 )
      {
        msg.add( padding_bytes[0] );
        int zero_bytes = 56 - (bytes_append + 2);
        for( int i = 0; i < zero_bytes; i++ ) {
          msg.add( padding_bytes[1] );
        }
        msg.add( padding_bytes[2] );
      }
      else { // bytes_append == 55
        msg.add( padding_bytes[3] );
      }
    }
    else
    {
      msg.add( padding_bytes[0] );
      bytes_append = (msg.size() % 64) + 55;
      for( int i = 0; i < bytes_append; i++ ) {
        msg.add( padding_bytes[1] );
      }
      msg.add( padding_bytes[2] );
    }
    int length = msg.size() * 8; // For message of size l bits
    BigInteger bi = BigInteger.valueOf( length );
    byte[] temp_num_bitlength = bi.toByteArray();
    byte[] byte_num_bitlength = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    for( int i = 0; i < temp_num_bitlength.length; i++ ) 
    {
      byte_num_bitlength[ byte_num_bitlength.length - 1 - i ] |= 
          temp_num_bitlength[temp_num_bitlength.length - 1 - i ]; 
    }
    for( byte b : byte_num_bitlength ) {
      msg.add(b);
    }
    return msg;
  }
  
  public int[][] getMessageBlocks( ArrayList<Byte> message )
  {
    int rows = message.size() / 64;
    int[][] message_block = new int[ rows ][16];
    for( int i = 0; i < rows; i++ ) 
    {
      int k = 64 * i;
      for( int j = 0; j < 16; j++ ) 
      {
        byte[] b = { message.get(k + 4*j), message.get(k + 4*j + 1), message.get(k + 4*j + 2), 
            message.get(k + 4*j + 3) };
        message_block[i][j] = ByteBuffer.wrap(b).getInt();
      }
    }
    return message_block;
  }
  
  public byte[] compression( int[][] message_blocks )
  {
    int[] msg1 = INITIAL_VECTOR;
    for( int[] msg2 : message_blocks )
    {
      int[] salt = {0, 0, 0, 0};
    }
    return null;
  }
  
  public byte[] hash( ArrayList<Byte> message )
  {
    message = padding( message );
    if( 0 != ((message.size() * 8) % 512)) {
      System.err.println("Error in padding.");
    }
    int[][] message_blocks = getMessageBlocks( message );
    byte[] hash = compression( message_blocks );
    return null;
  }
}
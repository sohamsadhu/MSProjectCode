package com.Soham.MSProject.SHA3;

import java.nio.ByteBuffer;

public class BLAKE 
{
  public static final int[] IV224 = { 0xC1059ED8, 0x367CD507, 0x3070DD17, 0xF70E5939, 0xFFC00B31,
    0x68581511, 0x64F98FA7, 0xBEFA4FA4 };
  
  public static final int[] IV256 = { 0x6A09E667, 0xBB67AE85, 0x3C6EF372, 0xA54FF53A, 0x510E527F,
    0x9B05688C, 0x1F83D9AB, 0x5BE0CD19 };
  
  public static final int[] CONST256 = { 0x243F6A88, 0x85A308D3, 0x13198A2E, 0x03707344, 
    0xA4093822, 0x299F31D0, 0x082EFA98, 0xEC4E6C89, 0x452821E6, 0x38D01377, 0xBE5466CF,
    0x34E90C6C, 0xC0AC29B7, 0xC97C50DD, 0x3F84D5B5, 0xB5470917 };
  
  public static final long[] IV384 = { 0xCBBB9D5DC1059ED8L, 0x629A292A367CD507L, 
    0x9159015A3070DD17L, 0x152FECD8F70E5939L, 0x67332667FFC00B31L, 0x8EB44A8768581511L,
    0xDB0C2E0D64F98FA7L, 0x47B5481DBEFA4FA4L };
  
  public static final long[] IV512 = { 0x6A09E667F3BCC908L, 0xBB67AE8584CAA73BL, 
    0x3C6EF372FE94F82BL, 0xA54FF53A5F1D36F1L, 0x510E527FADE682D1L, 0x9B05688C2B3E6C1FL,
    0x1F83D9ABFB41BD6BL, 0x5BE0CD19137E2179L };
  
  public static final long[] CONST512 = { 0x243F6A8885A308D3L, 0x13198A2E03707344L,
    0xA4093822299F31D0L, 0x082EFA98EC4E6C89L, 0x452821E638D01377L, 0xBE5466CF34E90C6CL,
    0xC0AC29B7C97C50DDL, 0x3F84D5B5B5470917L, 0x9216D5D98979FB1BL, 0xD1310BA698DFB5ACL,
    0x2FFD72DBD01ADFB7L, 0xB8E1AFED6A267E96L, 0xBA7C9045F12C7F99L, 0x24A19947B3916CF7L,
    0x0801F2E2858EFC16L, 0x636920D871574E69L };  

  public static final int[][] SIGMA = new int[][] {
    { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 },
    { 14, 10, 4, 8, 9, 15, 13, 6, 1, 12, 0, 2, 11, 7, 5, 3 },
    { 11, 8, 12, 0, 5, 2, 15, 13, 10, 14, 3, 6, 7, 1, 9, 4 },
    { 7, 9, 3, 1, 13, 12, 11, 14, 2, 6, 5, 10, 4, 0, 15, 8 },
    { 9, 0, 5, 7, 2, 4, 10, 15, 14, 1, 11, 12, 6, 8, 3, 13 },
    { 2, 12, 6, 10, 0, 11, 8, 3, 4, 13, 7, 5, 15, 14, 1, 9 },
    { 12, 5, 1, 15, 14, 13, 4, 10, 0, 7, 6, 3, 9, 2, 8, 11 },
    { 13, 11, 7, 14, 12, 1, 3, 9, 5, 0, 15, 4, 8, 6, 2, 10 },
    { 6, 15, 14, 9, 11, 3, 0, 8, 12, 2, 13, 7, 1, 4, 10, 5 },
    { 10, 2, 8, 4, 7, 6, 1, 5, 15, 11, 9, 14, 3, 12, 13, 0 }
  };
  
  /**
   * This method converts the string that is in hex format, to its equivalent byte format.
   * For example the string AB is converted to byte format 1010 1011.
   * @param msg in hex format representation of bytes
   * @return array list of bytes
   */
  //Source: http://stackoverflow.com/questions/13185073/convert-a-string-to-byte-array
  public byte[] convertHexStringToBytes( String msg )
  {
    int length = msg.length();
    byte[] message = new byte[msg.length() / 2];
    for( int i = 0; i < length; i += 2 )
    {
      message[i / 2] = (byte)((Character.digit(msg.charAt(i), 16) << 4) +
          Character.digit(msg.charAt(i + 1), 16));
    }
    return message;
  }
  
  public byte[] padHelper( byte[] pad_msg, int msg_len, int digest_len, byte[] bit_len,
      byte[] msg )
  {
    for( int i = 0; i < msg_len; i++ ) {
      pad_msg[i] = msg[i];
    }
    pad_msg[msg_len] = ( byte )0x80;
    int zero_index = pad_msg.length - (((384 == digest_len) || (512 == digest_len)) ? 17 : 9);
    for( int i = msg_len + 1; i < zero_index; i++ ) {
      pad_msg[i] = ( byte )0x00;
    }
    pad_msg[zero_index] = ((512 == digest_len) || (256 == digest_len)) ? (byte)0x01 : (byte)0x00;
    int word_len = ((384 == digest_len) || (512 == digest_len)) ? 16 : 8;
    for( int i = 0; i < word_len; i++ ) {
      pad_msg[pad_msg.length - word_len + i] = bit_len[i];
    }
    return pad_msg;
  }
  
  public byte[] pad256( byte[] message, int digest_length )
  {
    if( !((224 == digest_length) || (256 == digest_length))) { return null; }
    long msg_bit_len = message.length * 8;
    ByteBuffer buf = ByteBuffer.allocate(8);
    buf.putLong( msg_bit_len );
    byte[] bit_len = buf.array(); // <l> length of message in bits, in 64 bit format.
    int bits_remaining = ( int )(msg_bit_len % 512); // For our applications message length is small.
    if( 440 == bits_remaining ) // Only byte 0x81 needs to append and <l>64
    {
      byte[] pad_msg = new byte[message.length + 9];
      for( int i = 0; i < message.length; i++ ) {
        pad_msg[i] = message[i];
      }
      pad_msg[ message.length ] = (224 == digest_length) ? (byte)0x80 : (byte)0x81;
      for( int i = 0; i < 8; i++ ) {
        pad_msg[message.length + 1 + i] = bit_len[i];
      }
      return pad_msg;
    }
    if( bits_remaining > 440 )
    {
      byte[] pad_msg = new byte[ message.length + 64 + (64 - (bits_remaining / 8)) ];
      return padHelper( pad_msg, message.length, digest_length, bit_len, message );
    }
    else // bits_remaining < 440
    {
      byte[] pad_msg = new byte[message.length + ((512 - bits_remaining) / 8)];
      return padHelper( pad_msg, message.length, digest_length, bit_len, message );
    }
  }
  
  public byte[] pad512( byte[] message, int digest_length )
  {
    if( !((384 == digest_length) || (512 == digest_length)) ){ return null; }
    long msg_bit_len = message.length * 8;   // For our purpose the message is not around 1 ^ 128.
    ByteBuffer buf = ByteBuffer.allocate(8);
    buf.putLong( msg_bit_len );
    byte[] msg_bit_len_arr = buf.array();
    byte[] bit_len = new byte[16];
    for( int i = 0; i < 16; i++ ) { bit_len[i] = 0x00; }
    for( int i = 7; i >= 0; i-- ) {
      bit_len[ 8 + i ] = msg_bit_len_arr[i];
    }
    int bits_remaining = (message.length * 8) % 1024;
    if( 888 == bits_remaining )
    {
      byte[] pad_msg = new byte[ message.length + 17 ];
      for( int i = 0; i < message.length; i++ ) {
        pad_msg[i] = message[i];
      }
      pad_msg[ message.length ] = (512 == digest_length) ? (byte)0x81 : (byte)0x80;
      for( int i = 0; i < 16; i++ ) {
        pad_msg[ message.length + 1 + i ] = bit_len[i];
      }
      return pad_msg;
    }
    if( bits_remaining < 888 )
    {
      byte[] pad_msg = new byte[message.length + ((1024 - bits_remaining) / 8)];
      return padHelper( pad_msg, message.length, digest_length, bit_len, message );
    }
    else // bits_remaining > 888
    {
      byte[] pad_msg = new byte[message.length + 128 + ((1024 - bits_remaining) / 8)];
      return padHelper( pad_msg, message.length, digest_length, bit_len, message );
    }
  }
  
  public byte[] hash( String msg, int digest_length, int rounds )
  {
    byte[] message = convertHexStringToBytes( msg );
    byte[] padded_msg;
    switch( digest_length )
    {
    case 224:
      padded_msg = pad256( message, digest_length );
      break;
    case 256:
      padded_msg = pad256( message, digest_length );
      break;
    case 384:
      padded_msg = pad512( message, digest_length );
      break;
    case 512:
      padded_msg = pad512( message, digest_length );
      break;
    }
    return null;
  }
  
  public static void main( String[] args )
  {
    BLAKE b = new BLAKE();
    b.hash("", 224, 0);
  }
}
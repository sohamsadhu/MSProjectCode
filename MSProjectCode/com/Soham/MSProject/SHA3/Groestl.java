package com.Soham.MSProject.SHA3;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

// Input is a Hex String needs to be converted to bytes.
// Output is a byte array. Size of the byte array 224, 256, 384, 512.
// Message is padded and split into blocks of size l, and m blocks.
// hi <- f( h i-1, mi)
// Two l bits of input to one l bit of output. For 256 bits the message
// block is 512, and beyond that the internal state is of 1024 bits.
// Once you have consumed all the message blocks, you will go to the Omega function
// to do the truncation. Finished the hashing.
public class Groestl
{
  public static final byte[] GROESTL_SBOX = new byte[]{ 
    (byte)0x63, (byte)0x7c, (byte)0x77, (byte)0x7b, (byte)0xf2, (byte)0x6b, (byte)0x6f, (byte)0xc5,
    (byte)0x30, (byte)0x01, (byte)0x67, (byte)0x2b, (byte)0xfe, (byte)0xd7, (byte)0xab, (byte)0x76,
    (byte)0xca, (byte)0x82, (byte)0xc9, (byte)0x7d, (byte)0xfa, (byte)0x59, (byte)0x47, (byte)0xf0,
    (byte)0xad, (byte)0xd4, (byte)0xa2, (byte)0xaf, (byte)0x9c, (byte)0xa4, (byte)0x72, (byte)0xc0,
    (byte)0xb7, (byte)0xfd, (byte)0x93, (byte)0x26, (byte)0x36, (byte)0x3f, (byte)0xf7, (byte)0xcc,
    (byte)0x34, (byte)0xa5, (byte)0xe5, (byte)0xf1, (byte)0x71, (byte)0xd8, (byte)0x31, (byte)0x15,
    (byte)0x04, (byte)0xc7, (byte)0x23, (byte)0xc3, (byte)0x18, (byte)0x96, (byte)0x05, (byte)0x9a,
    (byte)0x07, (byte)0x12, (byte)0x80, (byte)0xe2, (byte)0xeb, (byte)0x27, (byte)0xb2, (byte)0x75,
    (byte)0x09, (byte)0x83, (byte)0x2c, (byte)0x1a, (byte)0x1b, (byte)0x6e, (byte)0x5a, (byte)0xa0,
    (byte)0x52, (byte)0x3b, (byte)0xd6, (byte)0xb3, (byte)0x29, (byte)0xe3, (byte)0x2f, (byte)0x84,
    (byte)0x53, (byte)0xd1, (byte)0x00, (byte)0xed, (byte)0x20, (byte)0xfc, (byte)0xb1, (byte)0x5b,
    (byte)0x6a, (byte)0xcb, (byte)0xbe, (byte)0x39, (byte)0x4a, (byte)0x4c, (byte)0x58, (byte)0xcf,
    (byte)0xd0, (byte)0xef, (byte)0xaa, (byte)0xfb, (byte)0x43, (byte)0x4d, (byte)0x33, (byte)0x85,
    (byte)0x45, (byte)0xf9, (byte)0x02, (byte)0x7f, (byte)0x50, (byte)0x3c, (byte)0x9f, (byte)0xa8,
    (byte)0x51, (byte)0xa3, (byte)0x40, (byte)0x8f, (byte)0x92, (byte)0x9d, (byte)0x38, (byte)0xf5,
    (byte)0xbc, (byte)0xb6, (byte)0xda, (byte)0x21, (byte)0x10, (byte)0xff, (byte)0xf3, (byte)0xd2,
    (byte)0xcd, (byte)0x0c, (byte)0x13, (byte)0xec, (byte)0x5f, (byte)0x97, (byte)0x44, (byte)0x17,
    (byte)0xc4, (byte)0xa7, (byte)0x7e, (byte)0x3d, (byte)0x64, (byte)0x5d, (byte)0x19, (byte)0x73,
    (byte)0x60, (byte)0x81, (byte)0x4f, (byte)0xdc, (byte)0x22, (byte)0x2a, (byte)0x90, (byte)0x88,
    (byte)0x46, (byte)0xee, (byte)0xb8, (byte)0x14, (byte)0xde, (byte)0x5e, (byte)0x0b, (byte)0xdb,
    (byte)0xe0, (byte)0x32, (byte)0x3a, (byte)0x0a, (byte)0x49, (byte)0x06, (byte)0x24, (byte)0x5c,
    (byte)0xc2, (byte)0xd3, (byte)0xac, (byte)0x62, (byte)0x91, (byte)0x95, (byte)0xe4, (byte)0x79,
    (byte)0xe7, (byte)0xc8, (byte)0x37, (byte)0x6d, (byte)0x8d, (byte)0xd5, (byte)0x4e, (byte)0xa9,
    (byte)0x6c, (byte)0x56, (byte)0xf4, (byte)0xea, (byte)0x65, (byte)0x7a, (byte)0xae, (byte)0x08,
    (byte)0xba, (byte)0x78, (byte)0x25, (byte)0x2e, (byte)0x1c, (byte)0xa6, (byte)0xb4, (byte)0xc6,
    (byte)0xe8, (byte)0xdd, (byte)0x74, (byte)0x1f, (byte)0x4b, (byte)0xbd, (byte)0x8b, (byte)0x8a,
    (byte)0x70, (byte)0x3e, (byte)0xb5, (byte)0x66, (byte)0x48, (byte)0x03, (byte)0xf6, (byte)0x0e,
    (byte)0x61, (byte)0x35, (byte)0x57, (byte)0xb9, (byte)0x86, (byte)0xc1, (byte)0x1d, (byte)0x9e,
    (byte)0xe1, (byte)0xf8, (byte)0x98, (byte)0x11, (byte)0x69, (byte)0xd9, (byte)0x8e, (byte)0x94,
    (byte)0x9b, (byte)0x1e, (byte)0x87, (byte)0xe9, (byte)0xce, (byte)0x55, (byte)0x28, (byte)0xdf,
    (byte)0x8c, (byte)0xa1, (byte)0x89, (byte)0x0d, (byte)0xbf, (byte)0xe6, (byte)0x42, (byte)0x68,
    (byte)0x41, (byte)0x99, (byte)0x2d, (byte)0x0f, (byte)0xb0, (byte)0x54, (byte)0xbb, (byte)0x16
    };
  
  // A utility method that converts my hexadecimal string from the file to an array of Bytes.
  // Source: http://stackoverflow.com/questions/13185073/convert-a-string-to-byte-array
  public ArrayList<Byte> convertHexStringToBytes( String msg )
  {
    int length = msg.length();
    // Number of bytes is even and divisible by 2. Two characters fit to one byte.
    ArrayList<Byte> message = new ArrayList<>(msg.length() / 2);
    for( int i = 0; i < length; i += 2 )
    {
      message.add((byte)((Character.digit(msg.charAt(i), 16) << 4) +
          Character.digit(msg.charAt(i + 1), 16)));
    }
    return message;
  }
  
  // The box function, input of two 512 bit blocks or two 1024 blocks.
  // Sends back a 512 or 1024 block back.
  // f(h, m) = P(h xor m) xor Q(m) xor h
  
  // Omega function will return the last n bits.
  // Omega(x) = truncate(P(x) xor x)
  // It will also need to know what you want to truncate to, so the bit number.
  
  // function P the functions in order are 
  // addRoundConstant, SubBytes, ShiftBytes, MixBytes
  
  // function Q
  // addRoundConstant, SubBytes, ShiftBytes, MixBytes
  
  // Mapping of the bytes to the matrix. From top to bottom column wise.
  
  // Adding the round constant, map the constant matrix on the fly and then add.
  
  // Substitute with the s box.
  
  // Shift bytes for P and Q.
  
  // Mixing bytes with the circulant matrix. That is multiply.
  
  public byte[] getInitialVector( int digest_length )
  {
    if( !((digest_length == 224) || (digest_length == 256) 
        || (digest_length == 384) || (digest_length == 512))) 
    {
      System.err.println("The digest length is not of correct size for initial value making.");
      return new byte[]{((byte)0x00)};
    }
    byte [] iv = new byte[((digest_length == 224) || (digest_length == 256)) ? 64 : 128];
    int i = 0;
    switch( digest_length )
    {
    case 224:
      for(i = 0; i < 63; i++) {
        iv[i] = (byte)0x00;
      }
      iv[63] = (byte)0xe0;
      return iv;
    case 256:
      for(i = 0; i < 62; i++) {
        iv[i] = (byte)0x00;
      }
      iv[62] = (byte)0x01;
      iv[63] = (byte)0x00;
      return iv;
    case 384:
      for(i = 0; i < 126; i++) {
        iv[i] = (byte)0x00;
      }
      iv[126] = (byte)0x01;
      iv[127] = (byte)0x80;
      return iv;
    case 512:
      for(i = 0; i < 126; i++) {
        iv[i] = (byte)0x00;
      }
      iv[126] = (byte)0x02;
      iv[127] = (byte)0x00;
      return iv;
    }
    return new byte[]{((byte)0x00)};
  }
  
  // Function to do the padding.
  ArrayList<Byte> pad( String msg, int block_length )
  {
    ArrayList<Byte> message = convertHexStringToBytes(msg);
    // Java has negative modulus, so add with the dividing number. Then get out the one
    // byte pad of 0x80 and you know how many zeros you have to add in bytes by division of 8.
    int zero_pad_len = ((((-message.size() * 8) - 65) % block_length) + block_length - 7) / 8;
    message.add((byte)0x80);
    for( int i = 0; i < zero_pad_len; i++ ) {
      message.add((byte)0x00);
    }
    int num_blocks = (message.size() * 8 + 64) / block_length;
    BigInteger bi = BigInteger.valueOf( num_blocks );
    byte[] temp_num_blocks = bi.toByteArray();
    byte[] byte_num_blocks = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    for( int i = 0; i < temp_num_blocks.length; i++ ) 
    {
      byte_num_blocks[ byte_num_blocks.length - 1 - i ] |= 
          temp_num_blocks[temp_num_blocks.length - 1 - i ]; 
    }
    for( int i = 0; i < byte_num_blocks.length; i++ ) {
      message.add( byte_num_blocks[i] );
    }
    return message;
  }
  
  public ArrayList< byte[][] > convertMsgTo2DByteArray( byte[] iv, ArrayList<Byte> msg, 
      int block_length )
  {
    ArrayList<byte[][]> message_blocks = new ArrayList<>();
    int columns = block_length == 512 ? 8 : 16;
    byte[][] temp = new byte[8][columns];
    for( int i = 0; i < 8; i++ )
    {
      for( int j = 0; j < columns; j++ ) {
        temp[i][j] = iv[(j * columns) + i];
      }
    }
    message_blocks.add(temp);
    int fromIndex = 0;
    int toIndex = block_length;
    while(toIndex <= msg.size())
    {
      List <Byte> msg_block = msg.subList(fromIndex, toIndex);
      for( int i = 0; i < 8; i++ )
      {
        for( int j = 0; j < columns; j++ ) {
          temp[i][j] = msg_block.get((j * columns) + i);
        }
      }
      message_blocks.add(temp);
      toIndex = fromIndex;
      fromIndex += block_length;
    }
    return message_blocks;
  }
  
  // This function will take in a text message and return a hash value in bytes.
  public ArrayList<Byte> hash( String msg, int digest_length, int num_of_rounds)
  {
    if( !((digest_length == 224) || (digest_length == 256) 
        || (digest_length == 384) || (digest_length == 512))) 
    {
      System.err.println("The digest length is wrong.");
      return null;
    }
    int block_length = (digest_length == 224) || (digest_length == 256) ? 512 : 1024;
    byte[] iv = getInitialVector( digest_length ); // Fetch initial vector.
    if( num_of_rounds == 0 ) {
      num_of_rounds = digest_length <= 256 ? 10 : 14;
    }
    // Pad the message.
    ArrayList<Byte> message = pad( msg, block_length );
    ArrayList< byte[][] > message_blocks = convertMsgTo2DByteArray(iv, message, block_length);
    return message;
  }
  
  public static void main(String [] args)
  {
    Groestl g = new Groestl();
//    BigInteger bi = BigInteger.valueOf(300);
//    byte[] temp = bi.toByteArray();
//    byte[] temp2 = new byte[]{0x00, 0x00, 0x00, 0x00};
//    StringBuilder sb = new StringBuilder();
//    for( int i = 0; i < temp.length; i++ ) {
//      temp2[ temp2.length - 1 - i ] ^= temp[ temp.length - 1 - i ];
//    }
//    for( byte s : temp2) {
//      sb.append(String.format("%02x", s));
//    }
    String s = new String("abc");
    byte[] str = s.getBytes();
    StringBuilder sb = new StringBuilder();
    for( byte some : str ) {
      sb.append(String.format("%02x", some));
    }
    ArrayList<Byte> test = g.hash(sb.toString(), 224, 0);
    for( Byte some : test ) {
      sb.append(String.format("%02x", some));
    }
    System.out.println(" Size of padded message "+ test.size());
    System.out.println(sb.toString());
  }
}
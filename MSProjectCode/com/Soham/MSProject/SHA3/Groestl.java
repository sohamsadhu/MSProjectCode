package com.Soham.MSProject.SHA3;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
  
  public static final int[][] SHIFT_INDEX = new int[][] {{0,1,2,3,4,5,6,7}, {0,1,2,3,4,5,6,11},
    {1,3,5,7,0,2,4,6}, {1,3,5,11,0,2,4,6}};
  
  // Source: http://stackoverflow.com/questions/13185073/convert-a-string-to-byte-array
  public ArrayList<Byte> convertHexStringToBytes( String msg )
  {
    int length = msg.length();
    ArrayList<Byte> message = new ArrayList<>(msg.length() / 2);
    for( int i = 0; i < length; i += 2 )
    {
      message.add((byte)((Character.digit(msg.charAt(i), 16) << 4) +
          Character.digit(msg.charAt(i + 1), 16)));
    }
    return message;
  }
  
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
  
  ArrayList<Byte> pad( String msg, int block_length )
  {
    ArrayList<Byte> message = convertHexStringToBytes(msg);
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
        temp[i][j] = iv[(j * 8) + i];
      }
    }
    message_blocks.add(temp);
    int fromIndex = 0;
    block_length = block_length / 8;
    int toIndex = block_length;
    while(toIndex <= msg.size())
    {
      List <Byte> msg_block = msg.subList(fromIndex, toIndex);
      temp = new byte[8][columns];
      for( int i = 0; i < 8; i++ )
      {
        for( int j = 0; j < columns; j++ ) {
          temp[i][j] = msg_block.get((j * 8) + i);
        }
      }
      message_blocks.add(temp);
      fromIndex = toIndex;
      toIndex += block_length;
    }
    return message_blocks;
  }
  
  public void printState( ArrayList< byte [][]> message_blocks, int block_length )
  {
    int columns = block_length <= 512 ? 8 : 16;
    for( byte[][] block : message_blocks )
    {
      System.out.println(" Block");
      for( int i = 0; i < 8; i++ )
      {
        for( int j = 0; j < columns; j++ ) {
          System.out.printf("%02x ", block[i][j]);
        }
        System.out.println(" ");
      }
      System.out.println("");
    }
  }
  
  public byte[][] addRoundConstant( byte[][] msg, int columns, int round, char variant )
  {
    switch( variant )
    {
    case 'P':
      for( int i = 0; i < 8; i++ ) {
        msg[0][i] = ( byte )(msg[0][i] ^ (i << 4) ^ round);
      }
      break;
    case 'Q':
      for( int i = 0; i < 7; i++ )
      {
        for( int j = 0; j < columns; j++ ) {
          msg[0][j] ^= 0xFF;
        }
      }
      for( int i = 0; i < columns; i++ ) {
        msg[7][i] = ( byte )(msg[7][i] ^ (i << 4) ^ 0xFF ^ round);
      }
      break;
    }
    return msg;
  }
  
  public byte[][] subBytes( byte[][] msg, int columns )
  {
    for( int i = 0; i < 8; i++ )
    {
      for( int j = 0; j < 8; j++ ) {
        msg[i][j] = GROESTL_SBOX[ msg[i][j] ];
      }
    }
    return msg;
  }
  
  public byte[][] shiftBytes( byte[][] msg, int columns, char variant )
  {
    int[] shift;
    if( variant == 'P' ) {
      shift = (columns == 8) ? SHIFT_INDEX[0] : SHIFT_INDEX[1];
    } else {
      shift = (columns == 8) ? SHIFT_INDEX[2] : SHIFT_INDEX[3];
    }
    byte[] temp = new byte[ columns ];
    for( int i = 0; i < 8; i++ )
    {
      for( int j = 0; j < columns; j++ ) {
        temp[j] = msg[i][(j + shift[j]) % columns];
      }
      for( int j = 0; i < columns; j++ ) {
        msg[i][j] = temp[j];
      }
    }
    return msg;
  }
  
  public byte[][] mixBytes( byte[][] msg )
  {
    return null;
  }
  
  public byte[][] permutationP( byte[][] msg1, byte[][] msg2, int block_length, int num_rounds )
  {
    int columns = block_length / 8 / 8;
    byte[][] msg = new byte[8][ columns ];
    for( int i = 0; i < 8; i++ )
    {
      for( int j = 0; j < columns; j++ ) {
        msg[i][j] = ( byte )(msg1[i][j] ^ msg2[i][j]);
      }
    }
    for( int i = 0; i < num_rounds; i++ )
    {
      msg = addRoundConstant( msg, columns, i, 'P' );
      msg = subBytes( msg, columns );
      msg = shiftBytes( msg, columns, 'P' );
      msg = mixBytes( msg );
    }
    return msg;
  }
  
  public byte[][] permutationQ( byte[][] msg2, int block_length, int num_rounds )
  {
    return null;
  }
  
  public byte[][] permutationFunction( byte[][] msg1, byte[][] msg2, int block_length,
      int num_rounds )
  {
    byte[][] temp = msg1;
    msg1 = permutationP( msg1, msg2, block_length, num_rounds );
    msg2 = permutationQ( msg2, block_length, num_rounds );
    int columns = block_length / 8 / 8;
    byte[][] permuted = new byte[8][block_length / 8 / 8];
    for( int i = 0; i < 8; i++ )
    {
      for( int j = 0; j < columns; j++ ) {
        permuted[i][j] = (byte)( temp[i][j] ^ msg1[i][j] ^ msg2[i][j] );
      }
    }
    return permuted;
  }
  
  public byte[] omega( byte[][] permutedBlock, int block_length, int digest_length,
      int num_rounds )
  {
    byte[][] temp = permutedBlock;
    permutedBlock = omegaHelper( permutedBlock, block_length, num_rounds );
    byte[] hash = new byte[digest_length / 8];
    int i = 0;
    int j = (block_length / 8) - (digest_length / 8);
    for( ; j < (block_length / 8); j++, i++ ) {
      hash[i] = permutedBlock[j % 8][j / 8];
    }
    return hash;
  }
  
  public byte[] transform( ArrayList< byte[][] > msg_blocks, int block_length, int num_rounds,
      int digest_length )
  {
    byte[][] msg1 = msg_blocks.get(0);  // First block is the iv.
    byte[][] msg2 = null;
    for( int i = 1; i < msg_blocks.size(); i++ )
    {
      msg2 = msg_blocks.get(i);
      msg1 = permutationFunction( msg1, msg2, block_length, num_rounds );
    }
    byte[] hash = omega( msg1, block_length, digest_length, num_rounds );
    return hash;
  }
  
  public byte[] hash( String msg, int digest_length, int num_of_rounds)
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
    ArrayList<Byte> message = pad( msg, block_length );
    ArrayList< byte[][] > message_blocks = convertMsgTo2DByteArray(iv, message, block_length);
    byte [] hash = transform( message_blocks, block_length, num_of_rounds, digest_length );
    return hash;
  }
  
  public static void main(String [] args)
  {
    Groestl g = new Groestl();
    String s = new String("abc");
    byte[] str = s.getBytes();
    StringBuilder sb = new StringBuilder();
    for( byte some : str ) {
      sb.append(String.format("%02x", some));
    }
    g.hash(sb.toString(), 224, 0);
  }
}
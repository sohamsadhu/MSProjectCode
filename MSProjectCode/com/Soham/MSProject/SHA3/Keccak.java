package com.Soham.MSProject.SHA3;

import java.nio.ByteBuffer;

public class Keccak 
{  
  public static final long[] RC = { 0x0000000000000001, 0x0000000000008082,
    0x800000000000808AL, 0x8000000080008000L, 0x000000000000808BL, 0x0000000080000001L,
    0x8000000080008081L, 0x8000000000008009L, 0x000000000000008AL, 0x0000000000000088L,
    0x0000000080008009, 0x000000008000000AL, 0x000000008000808BL, 0x800000000000008BL,
    0x8000000000008089L, 0x8000000000008003L, 0x8000000000008002L, 0x8000000000000080L,
    0x000000000000800AL, 0x800000008000000AL, 0x8000000080008081L, 0x8000000000008080L,
    0x0000000080000001, 0x8000000080008008L };
  
  public static final int[][] ROTATION = {{0, 36, 3, 41, 18}, {1, 44, 10, 45, 2},
    {62, 6, 43, 15, 61}, {28, 55, 25, 21, 56}, {27, 20, 39, 8, 14}};
  
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
  
  public void printState( long[][] state )
  {
    for( long[] row : state )
    {
      for( long cell : row )
      {
        ByteBuffer buf = ByteBuffer.allocate(8);
        buf.putLong( cell );
        byte[] statebyte = buf.array();
        for( byte b : statebyte ) {
          System.out.printf("%02X", b);
        }
        System.out.printf(" ");
      }
      System.out.println("");
    }
  }
  
  public byte[] pad( byte[] message, int block_length )
  {
    int pad_length = (message.length * 8) % block_length;
    byte[] pad_bytes = {(byte) 0x81, 0x00, (byte) 0x80, 0x01};
    if( 0 == pad_length ) {
      pad_length = block_length / 8; // Message already multiple of block size. Add another block.
    }
    byte[] padded_message = new byte[ message.length + pad_length ];
    for( int i = 0; i < message.length; i++ ) {
      padded_message[i] = message[i];   // Copy the message into new padded message array.
    }
    if( 1 == pad_length ) 
    {
      padded_message[ message.length ] = pad_bytes[0];
      return padded_message;
    }
    else
    {
      padded_message[ message.length ] = pad_bytes[3];
      for( int i = (message.length + 1); i < (padded_message.length - 1); i++ ) {
        padded_message[i] = pad_bytes[1];
      }
      padded_message[padded_message.length - 1] = pad_bytes[2];
    }
    return padded_message;
  }
  
  public byte[][] getMsgBlocks( byte[] message, int block_length )
  {
    int num_message_blocks = (message.length * 8) / block_length;
    block_length = block_length / 8; // Make block length equivalent to in bytes.
//    System.out.println("number of message blocks "+ message.length +" and block "
//        + "length is "+ block_length);
    byte[][] message_blocks = new byte[ num_message_blocks ][ block_length ];
    for( int i = 0; i < num_message_blocks; i++)
    {
      for( int j = 0; j < block_length; j++ ) {
        message_blocks[i][j] = message[(block_length * i) + j];
      }
    }
    return message_blocks;
  }
  
  public long[][] xorStatePermutation( long[][] state, byte[] msg_block )
  {
    byte[] temp_block = new byte[ 200 ]; // State size is fixed at 1600 bits.
    // Pad the message with the zeros, so that it can be easily XORed with the state.
    for( int i = 0; i < msg_block.length; i++ )  {
      temp_block[i] = msg_block[i];
    }
    for( int i = msg_block.length; i < 200; i++ ) {
      temp_block[i] = 0x00;
    }
    ByteBuffer wrapped;
    for( int i = 0; i < 5; i++ )
    {
      for( int j = 0; j < 5; j++ )
      {
        byte[] temp = new byte[8];
        int row_offset = 5 * 8 * i;
        int column_offset = (j + 1) * 8;
        for( int k = 0; k < 8; k++ ) {          
          temp[k] =  temp_block[row_offset + column_offset - 1 - k];
        }
        wrapped = ByteBuffer.wrap( temp );
        long temp_long = wrapped.getLong();
        state[i][j] = state[i][j] ^ temp_long;
      }
    }
    return state;
  }
  
  // Source: Keccak Python implementation by Renaud Bauvin
  public long rotation( long lane, int rotate )
  {
    rotate = rotate % 64;
    lane = (lane >>> (64 - rotate)) | (lane << rotate);
    return lane;
  }
  
  public long[][] theta( long[][] state )
  {
    long [] c = new long[5];
    long [] d = new long[5];
    for( int i = 0; i < 5; i++ ) {
      c[i] = state[0][i] ^ state[1][i] ^ state[2][i] ^ state[3][i] ^ state[4][i];
    }
    d[0] = c[4] ^ rotation(c[1], 1);
    for( int i = 1; i < 5; i++ ) {
      d[i] = c[i - 1] ^ rotation(c[(i + 1) % 5], 1);
    }
    for( int i = 0; i < 5; i++ )
    {
      for( int j = 0; j < 5; j++ ) {
        state[i][j] = state[i][j] ^ d[j];
      }
    }
    return state;
  }
  
  public long[][] rhoPi( long[][] state )
  {
    long[][] b = new long[5][5];
    for( int i = 0; i < 5; i++ )
    {
      for( int j = 0; j < 5; j++ ) {
        b[j][((2 * i) + (3 * j)) % 5] = rotation(state[i][j], ROTATION[i][j]);
      }
    }
    return b;
  }
  
  public long[][] chi( long[][] state )
  {
    long[][] temp = new long[5][5];
    for( int i = 0; i < 5; i++ )
    {
      for( int j = 0; j < 5; j++ ) {
        temp[i][j] = state[i][j];
      }
    }
    for( int i = 0; i < 5; i++ )
    {
      for( int j = 0; j < 5; j++ ) {
        state[i][j] = temp[i][j] ^ ((~temp[(i + 1) % 5][j]) & temp[(i + 2) % 5][j]);
      }
    }
    return state;
  }
  
  public long[][] permute( long[][] state, int rounds )
  {
    System.out.println("Before rounds");
    printState(state);
    for( int i = 0; i < rounds; i++ )
    {
      System.out.println("round "+ i);
      state = theta( state );
      System.out.println("after theta ");
      printState(state);
      state = rhoPi( state );
      System.out.println("after rho pi ");
      printState(state);
      state = chi( state );
      System.out.println("after chi ");
      printState(state);
      state[0][0] = state[0][0] ^ RC[i];
      System.out.println("after round constant ");
      printState(state);
    }
    return state;
  }
  
  public byte[] squeeze( long[][] state, int digest_length )
  {
    long temp;
    byte[] hashed = new byte[ 200 ];
    for( int i = 0; i < 5; i++ )
    {
      for( int j = 0; j < 5; j++ )
      {
        temp = state[i][j];
        ByteBuffer buf = ByteBuffer.allocate(8);
        buf.putLong( temp );
        byte[] lane = buf.array();
        for( int k = 0; k < 8; k++ ) {
          hashed[(i * 40) + (j * 8) + k] = lane[7 - k];
        }
      }
    }
    digest_length = digest_length / 8; // Get the length in bytes.
    byte[] squeezed = new byte[ digest_length ];
    for( int i = 0; i < digest_length; i++ ) {
      squeezed[i] = hashed[i];
    }
    return squeezed;
  }
  
  public byte[] transform( byte[] message, int bit_rate, int rounds )
  {
    long[][] state = {{0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, 
        {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}};
    byte[][] msg_blocks = getMsgBlocks( message, bit_rate );
    for( byte[] block : msg_blocks )
    {
      state = xorStatePermutation( state, block );
      state = permute( state, rounds );
    }
    byte[] hash = squeeze( state, (1600 - bit_rate) / 2 );
    return hash;
  }
  
  public byte[] hash( String message,  int digest_length, int rounds )
  {
    int capacity = 2 * digest_length;
    int bit_rate = 1600 - capacity;    // block length
    rounds = rounds == 0 ? 24 : rounds;
    byte[] msg = convertHexStringToBytes( message );
    byte[] padded_msg = pad( msg, bit_rate );
    byte[] hash = transform( padded_msg, bit_rate, rounds );
    return hash;
  }
  
  public static void main( String [] args )
  {
    Keccak k = new Keccak();
    byte[] hashed = k.hash("00112233445566778899AABBCCDDEEFF", 224, 1);
    for( byte b : hashed ) {
      System.out.printf("%02X", b);
    }
  }
}
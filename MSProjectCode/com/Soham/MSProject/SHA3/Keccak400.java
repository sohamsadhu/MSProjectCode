package com.Soham.MSProject.SHA3;

import java.nio.ByteBuffer;

public class Keccak400 implements Hash
{
  public static final short[] RC = { 0x0001, ( short )0x8082, ( short )0x808A, ( short )0x8000,
    ( short )0x808B, 0x0001, ( short )0x8081, ( short )0x8009, ( short )0x008A, ( short )0x0088,
    ( short )0x8009, ( short )0x000A, ( short )0x808B, 0x008B, ( short )0x8089, ( short )0x8003,
    ( short )0x8002, 0x0080, ( short )0x800A, 0x000A }; //, 0x80008081, 0x00008080 , 0x80000001, 0x80008008
  
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
  
  /**
   * Pad the message as per the bit rate of the function.
   * @param message
   * @param block_length this is the bit rate of the function.
   * @return The padded message.
   */
  public byte[] pad( byte[] message, int block_length )
  {
    int pad_length = (message.length * 8) % block_length;
    if( pad_length != 0 ) {
      pad_length = block_length - pad_length; // Find how many bits you need to fill.
    }
    pad_length = pad_length / 8;  // Convert the above bit value to byte value.
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
  
  /**
   * Break the message into blocks are per the bit rate.
   * @param message
   * @param block_length
   * @return 2D array of bytes, with second dimension holding the blocks.
   */
  public byte[][] getMsgBlocks( byte[] message, int block_length )
  {
    int num_message_blocks = (message.length * 8) / block_length;
    block_length = block_length / 8; // Make block length equivalent to in bytes.
    byte[][] message_blocks = new byte[ num_message_blocks ][ block_length ];
    for( int i = 0; i < num_message_blocks; i++)
    {
      for( int j = 0; j < block_length; j++ ) {
        message_blocks[i][j] = message[(block_length * i) + j];
      }
    }
    return message_blocks;
  }
  
  /**
   * Called each time during absorption, when message state absorbed block by block.
   * @param state
   * @param msg_block
   * @return
   */
  public short[][] xorStatePermutation( short[][] state, byte[] msg_block )
  {
    byte[] temp_block = new byte[ 50 ]; // State size is fixed at 1600 bits.
    // Pad the message with the zeros, so that it can be easily XORed with the state.
    for( int i = 0; i < msg_block.length; i++ )  {
      temp_block[i] = msg_block[i];
    }
    for( int i = msg_block.length; i < 50; i++ ) {
      temp_block[i] = 0x00;
    }
    ByteBuffer wrapped;
    for( int i = 0; i < 5; i++ )
    {
      for( int j = 0; j < 5; j++ )
      {
        byte[] temp = new byte[2];
        int row_offset = 5 * 2 * i;
        int column_offset = (j + 1) * 2;
        for( int k = 0; k < 2; k++ ) {          
          temp[k] = temp_block[row_offset + column_offset - 1 - k];
        }
        wrapped = ByteBuffer.wrap( temp );
        int temp_short = wrapped.getShort();
        state[i][j] = ( short )(state[i][j] ^ temp_short);
      }
    }
    return state;
  }
  
  /**
   * Is the rot or the rotation function used in Keccak. Used in rho step for putting shifting
   * the lanes. Note the use of unsigned right shift operator >>>.
   * Source: Keccak Python implementation by Renaud Bauvin
   * @param lane
   * @param rotate
   * @return
   */
  public short rotation( short lane, int rotate )
  {
    rotate = rotate % 16;
    lane = (short) (((lane & 0xFFFF) >>> (16 - rotate)) | (lane << rotate));
    return lane;
  }
  
  /**
   * Theta works on the column, the first index x in specification is not the row but the column.
   * @param state
   * @return
   */
  public short[][] theta( short[][] state )
  {
    short [] c = new short[5];
    short [] d = new short[5];
    for( int i = 0; i < 5; i++ ) {
      c[i] = ( short )(state[0][i] ^ state[1][i] ^ state[2][i] ^ state[3][i] ^ state[4][i]);
    }
    d[0] = ( short )(c[4] ^ rotation(c[1], 1));
    for( int i = 1; i < 5; i++ ) {
      d[i] = ( short )(c[i - 1] ^ rotation(c[(i + 1) % 5], 1));
    }
    for( int i = 0; i < 5; i++ )
    {
      for( int j = 0; j < 5; j++ ) {
        state[i][j] = ( short )(state[i][j] ^ d[j]);
      }
    }
    return state;
  }
  
  /**
   * The state that I had created put the message blocks one after other row-wise in little 
   * endian format. However the state is supposed to be little endian and placed column wise one
   * after other, rather than row wise. That is what you see me doing in the first and last for 
   * loop before the operation, that is putting my state column wise from row wise and then after
   * operation rearranging them row wise. Also note the use of j and i indices in operation loop.
   * They have been arranged to reflect the pseudocode of specification using x and y.
   * @param state
   * @return
   */
  public short[][] rhoPi( short[][] state )
  {
    short[][] temp = new short[5][5];
    for( int i = 0; i < 5; i++ )
    {
      for( int j = 0; j < 5; j++ ) {
        temp[j][i] = state[i][j];
      }
    }
    // Rho Pi operation loop begins.
    short[][] b = new short[5][5];
    for( int i = 0; i < 5; i++ )
    {
      for( int j = 0; j < 5; j++ ) {
        b[j][((2 * i) + (3 * j)) % 5] = rotation(temp[i][j], ROTATION[i][j]);
      }
    }
    // Rho Pi operation loop ends.
    for( int i = 0; i < 5; i++ )
    {
      for( int j = 0; j < 5; j++ ) {
        state[i][j] = b[j][i];
      }
    }
    return state;
  }
  
  /**
   * Again here the state is row wise for me, so the chi operation results are stored column wise
   * instead of row wise. The actual results are though pulled from same state.
   * @param state
   * @return
   */
  public short[][] chi( short[][] state )
  {
    short[][] temp = new short[5][5];
    for( int i = 0; i < 5; i++ )
    {
      for( int j = 0; j < 5; j++ ) {
        temp[i][j] = state[j][i];
      }
    }
    for( int i = 0; i < 5; i++ )
    {
      for( int j = 0; j < 5; j++ ) {
        state[j][i] = ( short )(temp[i][j] ^ ((~temp[(i + 1) % 5][j]) & temp[(i + 2) % 5][j]));
      }
    }
    return state;
  }
  
  /**
   * Just permute the provided block for as many rounds, as per the round parameter.
   * @param state
   * @param rounds
   * @return
   */
  public short[][] permute( short[][] state, int rounds )
  {
    for( int i = 0; i < rounds; i++ )
    {
      state = theta( state );
//      System.out.println("after theta");
//      printState( state );
      state = rhoPi( state );
//      System.out.println("after rho pi");
//      printState( state );
      state = chi( state );
//      System.out.println("after chi");
//      printState( state );
      state[0][0] = ( short )(state[0][0] ^ RC[i]);
//      System.out.println("after round "+ i);
//      printState( state );
    }
    return state;
  }
  
  public byte[] stateToString( short[][] state )
  {
    short temp;
    byte[] hashed = new byte[ 50 ];
    for( int i = 0; i < 5; i++ )
    {
      for( int j = 0; j < 5; j++ )
      {
        temp = state[i][j];
        ByteBuffer buf = ByteBuffer.allocate(2);
        buf.putShort( temp );
        byte[] lane = buf.array();
        for( int k = 0; k < 2; k++ ) {
          hashed[(i * 10) + (j * 2) + k] = lane[1 - k];
        }
      }
    }
    return hashed;
  }
  
  /**
   * After absorption, squeeze the required bits. This function is called once, since for our
   * implementation the digest length is shorter than the state size.
   * @param state
   * @param digest_length
   * @return
   */
  public byte[] squeeze( short[][] state, int bit_rate, int digest_length, int rounds )
  {    
    digest_length = digest_length / 8; // Get the length in bytes.
    bit_rate = bit_rate / 8; // Get the bit rate in bytes
    byte[] squeezed = new byte[ digest_length ];
    byte[] hashed = stateToString( state );
    if( digest_length <= bit_rate )
    {
      for( int i = 0; i < digest_length; i++ ) {
        squeezed[i] = hashed[i];
      }
    }
    else
    {
      int squeezed_bytes;
      for( squeezed_bytes = 0; squeezed_bytes < bit_rate; squeezed_bytes++ ) {
        squeezed[squeezed_bytes] = hashed[squeezed_bytes];
      }
      while( squeezed_bytes < digest_length )
      {
        state = permute( state, rounds );
        hashed = stateToString( state );
        int i = 0;
        while((i < bit_rate) && (squeezed_bytes < digest_length)) 
        {
          squeezed[squeezed_bytes] = hashed[i];
          i++; squeezed_bytes++;
        }
      } 
    }    
    return squeezed;
  }
  
  /**
   * Just print the state as it is.
   * @param state of Keccak, assumed to be of 1600 bits.
   */
  public void printState( short[][] state )
  {
    for( short[] row : state )
    {
      for( short cell : row )
      {
        ByteBuffer buf = ByteBuffer.allocate(2);
        buf.putShort( cell );
        byte[] statebyte = buf.array();
        for( byte b : statebyte ) {
          System.out.printf("%02X", b);
        }
        System.out.printf(" ");
      }
      System.out.println("");
    }
  }
  
  /**
   * The method that does bulk of transformation from absorbing to squeezing.
   * @param message
   * @param bit_rate
   * @param rounds
   * @return
   */
  public byte[] transform( byte[] message, int bit_rate, int rounds, int digest_length )
  {
    short[][] state = {{0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}};
    byte[][] msg_blocks = getMsgBlocks( message, bit_rate );
    for( byte[] block : msg_blocks )
    {
      state = xorStatePermutation( state, block );
//      System.out.println("initial state");
//      printState( state );
      state = permute( state, rounds );
    }
    byte[] hash = squeeze( state, bit_rate, digest_length, rounds );
    return hash;
  }

  /**
   * Call this function to get your hash.
   * @param message hexadecimal string representing message in bytes.
   * @param digest_length either 224, 256, 384, or 512
   * @param rounds will be 24 if provided by zero or will be done as provided.
   * @return
   */
  public byte[] hash( String message, int digest_length, int rounds )
  {
    int capacity = digest_length / 2;
    int bit_rate = 400 - capacity;    // block length
    rounds = (rounds == 0) ? 20 : rounds; // rounds = 12 + 2l = 12 + 2 * 4 = 20
    byte[] msg = convertHexStringToBytes( message );
    byte[] padded_msg = pad( msg, bit_rate );
    byte[] hash = transform( padded_msg, bit_rate, rounds, digest_length );
    return hash;
  }
  
  public static void main( String [] args )
  {
    Keccak400 k = new Keccak400();
    byte[] hashed = k.hash("00112233445566778899AABBCCDDEEFF", 224, 0);
    for( byte b : hashed ) {
      System.out.printf("%02X", b);
    }
  }
}
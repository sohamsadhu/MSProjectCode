package com.Soham.MSProject.SHA3;

import java.nio.ByteBuffer;

/**
 * This Keccak class accepts only hexadecimal representation of bytes to be hashed as string.
 * The state is assumed to of 1600 bits and only hash lengths that are supported by this are
 * 224, 256, 384 and 512 as per the NIST specification. Any other digest lengths are not supported.
 * @author Soham Sadhu
 */
public class Keccak implements Hash
{  
  /* The L after each of the long type needs to be there, else the Java compiler thinks that 
   * leading zeros are absent, with the sign bit set. This leads to weird results. Look here
   * for the explanation. http://stackoverflow.com/questions/22651465/bitwise-xor-java-long
   */
  public static final long[] RC = { 0x0000000000000001L, 0x0000000000008082L, 
    0x800000000000808AL, 0x8000000080008000L, 0x000000000000808BL, 0x0000000080000001L, 
    0x8000000080008081L, 0x8000000000008009L, 0x000000000000008AL, 0x0000000000000088L, 
    0x0000000080008009L, 0x000000008000000AL, 0x000000008000808BL, 0x800000000000008BL, 
    0x8000000000008089L, 0x8000000000008003L, 0x8000000000008002L, 0x8000000000000080L, 
    0x000000000000800AL, 0x800000008000000AL, 0x8000000080008081L, 0x8000000000008080L, 
    0x0000000080000001L, 0x8000000080008008L };
  
  /* The rotation numbers are based on x value of the row. Be careful to set this up this way,
   * else the Rho Pi step goes awry.
   */
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
   * Just print the state as it is.
   * @param state of Keccak, assumed to be of 1600 bits.
   */
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
  
  /**
   * Is the rot or the rotation function used in Keccak. Used in rho step for putting shifting
   * the lanes. Note the use of unsigned right shift operator >>>.
   * Source: Keccak Python implementation by Renaud Bauvin
   * @param lane
   * @param rotate
   * @return
   */
  public long rotation( long lane, int rotate )
  {
    rotate = rotate % 64;
    lane = (lane >>> (64 - rotate)) | (lane << rotate);
    return lane;
  }
  
  /**
   * Theta works on the column, the first index x in specification is not the row but the column.
   * @param state
   * @return
   */
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
  public long[][] rhoPi( long[][] state )
  {
    long[][] temp = new long[5][5];
    for( int i = 0; i < 5; i++ )
    {
      for( int j = 0; j < 5; j++ ) {
        temp[j][i] = state[i][j];
      }
    }
    // Rho Pi operation loop begins.
    long[][] b = new long[5][5];
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
  public long[][] chi( long[][] state )
  {
    long[][] temp = new long[5][5];
    for( int i = 0; i < 5; i++ )
    {
      for( int j = 0; j < 5; j++ ) {
        temp[i][j] = state[j][i];
      }
    }
    for( int i = 0; i < 5; i++ )
    {
      for( int j = 0; j < 5; j++ ) {
        state[j][i] = temp[i][j] ^ ((~temp[(i + 1) % 5][j]) & temp[(i + 2) % 5][j]);
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
  public long[][] permute( long[][] state, int rounds )
  {
    for( int i = 0; i < rounds; i++ )
    {
      state = theta( state );
      state = rhoPi( state );
      state = chi( state );
      state[0][0] = state[0][0] ^ RC[i];
    }
    return state;
  }
  
  /**
   * After absorption, squeeze the required bits. This function is called once, since for our
   * implementation the digest length is shorter than the state size.
   * @param state
   * @param digest_length
   * @return
   */
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
  
  /**
   * The method that does bulk of transformation from absorbing to squeezing.
   * @param message
   * @param bit_rate
   * @param rounds
   * @return
   */
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
  
  /**
   * Call this function to get your hash.
   * @param message hexadecimal string representing message in bytes.
   * @param digest_length either 224, 256, 384, or 512
   * @param rounds will be 24 if provided by zero or will be done as provided.
   * @return
   */
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
    byte[] hashed = k.hash("3A3A819C48EFDE2AD914FBF00E18AB6BC4F14513AB27D0C178A188B61431E7F5623CB6"
        + "6B23346775D386B50E982C493ADBBFC54B9A3CD383382336A1A0B2150A15358F336D03AE18F666C7573D55C"
        + "4FD181C29E6CCFDE63EA35F0ADF5885CFC0A3D84A2B2E4DD24496DB789E663170CEF74798AA1BBCD4574EA0"
        + "BBA40489D764B2F83AADC66B148B4A0CD95246C127D5871C4F11418690A5DDF01246A0C80A43C70088B6183"
        + "639DCFDA4125BD113A8F49EE23ED306FAAC576C3FB0C1E256671D817FC2534A52F5B439F72E424DE376F4C5"
        + "65CCA82307DD9EF76DA5B7C4EB7E085172E328807C02D011FFBF33785378D79DC266F6A5BE6BB0E4A92ECEE"
        + "BAEB1", 224, 0);
    for( byte b : hashed ) {
      System.out.printf("%02X", b);
    }
  }
}
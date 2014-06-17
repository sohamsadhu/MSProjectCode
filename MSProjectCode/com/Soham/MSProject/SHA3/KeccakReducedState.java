package com.Soham.MSProject.SHA3;

public class KeccakReducedState implements Hash
{
  private int state_size; // This should be in [25, 50, 100, 200, 400, 800, 1600]
  private int lane_length;
  private int perm_rounds;
  private int capacity;
  private int bit_rate;
  
  public static final byte [][] RC = {{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01}, 
    {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0x80, (byte)0x82}, 
    {(byte)0x80, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0x80, (byte)0x8A},
    {(byte)0x80, 0x00, 0x00, 0x00, (byte)0x80, 0x00, (byte)0x80, 0x00}, 
    {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0x80, (byte)0x8B},
    {0x00, 0x00, 0x00, 0x00, (byte)0x80, 0x00, 0x00, 0x01},
    {(byte)0x80, 0x00, 0x00, 0x00, (byte)0x80, 0x00, (byte)0x80, (byte)0x81},
    {(byte)0x80, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0x80, 0x09},
    {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0x8A},
    {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0x88},
    {0x00, 0x00, 0x00, 0x00, (byte)0x80, 0x00, (byte)0x80, 0x09},
    {0x00, 0x00, 0x00, 0x00, (byte)0x80, 0x00, 0x00, 0x0A}, 
    {0x00, 0x00, 0x00, 0x00, (byte)0x80, 0x00, (byte)0x80, (byte)0x8B},
    {(byte)0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0x8B},
    {(byte)0x80, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0x80, (byte)0x89},
    {(byte)0x80, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0x80, 0x03}, 
    {(byte)0x80, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0x80, 0x02},
    {(byte)0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0x80}, 
    {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0x80, 0x0A},
    {(byte)0x80, 0x00, 0x00, 0x00, (byte)0x80, 0x00, 0x00, 0x0A}, 
    {(byte)0x80, 0x00, 0x00, 0x00, (byte)0x80, 0x00, (byte)0x80, (byte)0x81},
    {(byte)0x80, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0x80, (byte)0x80}, 
    {0x00, 0x00, 0x00, 0x00, (byte)0x80, 0x00, 0x00, 0x01},
    {(byte)0x80, 0x00, 0x00, 0x00, (byte)0x80, 0x00, (byte)0x80, 0x08}};
  
  public static final int[][] ROTATION = {{0, 1, 62, 28, 27}, {36, 44, 6, 55, 20},
    {3, 10, 43, 25, 39}, {41, 45, 15, 21, 8}, {18, 2, 61, 56, 14}};
  
  public KeccakReducedState( int state_size, int capacity )
  {
    this.state_size  = state_size;
    this.lane_length = state_size / 25;
    this.perm_rounds = 12 + 2 * ((int)(Math.log(this.lane_length) / Math.log(2)));
    this.capacity = capacity;
    this.bit_rate = this.state_size - this.capacity;
  }

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
  public byte[][][] xorStatePermutation( byte[][][] state, byte[] msg_block, int lane_length_byte )
  {
    int state_length = lane_length_byte * 25;
    byte[] temp_block = new byte[ state_length ]; // Get the state size in bytes.
    // Pad the message with the zeros, so that it can be easily XORed with the state.
    for( int i = 0; i < msg_block.length; i++ )  {
      temp_block[i] = msg_block[i];
    }
    for( int i = msg_block.length; i < state_length; i++ ) {
      temp_block[i] = 0x00;
    }
    int row_offset, col_offset;
    for( int i = 0; i < 5; i++ )
    {
      row_offset = i * lane_length_byte * 5;
      for( int j = 0; j < 5; j++ )
      {
        col_offset = j * lane_length;
        for( int k = 0; k < lane_length_byte; k++ ) {
          state[i][j][k] ^= temp_block[row_offset + col_offset + (lane_length_byte - 1 - k)];
        }
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
  public byte[][][] permute( byte[][][] state, int rounds )
  {
    for( int i = 0; i < rounds; i++ )
    {
//      state = theta( state );
//      state = rhoPi( state );
//      state = chi( state );
//      state = iota(state);
    }
    return state;
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
    int lane_length_byte = this.lane_length / 8;
    byte[][][] state = new byte[5][5][lane_length_byte];
    for( int i = 0; i < 5; i++ )    // Initialise the starting state.
    {
      for( int j = 0; j < 5; j++ )
      {
        for( int k = 0; k < lane_length_byte; k++ ) {
          state[i][j][k] = 0x00;
        }
      }
    }
    byte[][] msg_blocks = getMsgBlocks( message, bit_rate );
    for( byte[] block : msg_blocks )
    {
      state = xorStatePermutation( state, block, lane_length_byte );
      state = permute( state, rounds );
    }
//    byte[] hash = squeeze( state, (1600 - bit_rate) / 2 );
    return null;
  }
  
  @Override
  public byte[] hash(String message, int digest_length, int rounds) 
  {
    rounds = rounds == 0 ? this.perm_rounds : rounds;
    byte[] msg = convertHexStringToBytes( message );
    byte[] padded_msg = pad( msg, bit_rate );
    byte[] hash = transform( padded_msg, bit_rate, rounds );
    return hash;
  }
}
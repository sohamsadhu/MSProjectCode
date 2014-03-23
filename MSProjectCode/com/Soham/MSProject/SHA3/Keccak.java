package com.Soham.MSProject.SHA3;

public class Keccak 
{
  public static final byte[][] RC = {
    {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01},
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
    {(byte)0x80, 0x00, 0x00, 0x00, (byte)0x80, 0x00, (byte)0x80, 0x08},
  };
  
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
  
  public byte[] pad( byte[] message, int block_length )
  {
    int pad_length = (message.length * 8) % block_length;
    byte[] pad_bytes = {(byte) 0x81, 0x00, (byte) 0x80, 0x01};
    pad_length = pad_length / 8; // Convert padding length from bits to bytes.
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
    byte[][] message_blocks = new byte[ num_message_blocks ][ block_length ];
    for( int i = 0; i < num_message_blocks; i++)
    {
      for( int j = 0; j < block_length; j++ ) {
        message_blocks[i][j] = message[(block_length * i) + j];
      }
    }
    return message_blocks;
  }
  
  public byte[][][] xorStatePermutation( byte[][][] state, byte[] msg_block )
  {
    byte[] temp_block = new byte[ 200 ]; // State size is fixed at 1600 bits.
    // Pad the message with the zeros, so that it can be easily XORed with the state.
    for( int i = 0; i < msg_block.length; i++ )  {
      temp_block[i] = msg_block[i];
    }
    for( int i = msg_block.length; i < 200; i++ ) {
      temp_block[i] = 0x00;
    }
    for( int i = 0; i < 5; i++ )
    {
      for( int j = 0; j < 5; j++ )
      {
        for( int k = 0; k < 8; k++ ) 
        {
          int row_offset = 5 * 8 * i;
          int column_offset = (j + 1) * 8;
          state[i][j][k] = ( byte )(state[i][j][k] ^ msg_block[row_offset + column_offset - 1 - k]);
        }
      }
    }
    return state;
  }
  
  public byte[][][] permute( byte[][][] state )
  {
    
    return null;
  }
  
  public byte[] transform( byte[] message, int bit_rate )
  {
    byte[][][] state = new byte[5][5][8];
    for( int i = 0; i < 5; i++ )
    {
      for( int j = 0; j < 5; j++ )
      {
        for( int k = 0; k < 8; k++ ) {
          state[i][j][k] = 0x00;
        }
      }
    }
    byte[][] msg_blocks = getMsgBlocks( message, bit_rate );
    for( byte[] block : msg_blocks )
    {
      state = xorStatePermutation( state, block );
      state = permute( state );
    }
    return null;
  }
  
  public byte[] hash( String message,  int digest_length )
  {
    int capacity = 2 * digest_length;
    int bit_rate = 1600 - capacity;    // block length
    byte[] msg = convertHexStringToBytes( message );
    byte[] padded_msg = pad( msg, bit_rate );
    byte[] hash = transform( padded_msg, bit_rate );
    return null;
  }
  
  public static void main()
  {
    // 2 * digest length = capacity
    // bit rate = block length = 1600 - capacity
  }
}
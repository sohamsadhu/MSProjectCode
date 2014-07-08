package com.Soham.MSProject.SimulationAlgorithm;

import java.nio.ByteBuffer;
import java.util.Random;

import com.Soham.MSProject.SHA3.Hash;

public class FindCollisionImpl implements FindCollision
{
  public long[] startIterativeCollision(Hash sha3, String msg1, String msg2,
      String cv, String rounds, String digest_length)
  {
    return null;    // To be overridden in the child classes.
  }
  
  public int getEpsilon( int digest_length )
  {
    switch( digest_length )
    {
    case 224: return 56;    // 65% match 146/224, 78. 75%, 168/224, 56. 85%, 191/224, 33.
    case 256: return 64;    // 167/256, 89. 192/256, 64. 218/256, 38.
    case 384: return 96;   // 250/384, 134. 288/384, 96. 327/384, 57.
    case 512: return 128;   // 333/512, 179. 384/512, 128. 436/512, 76.
    default:  return 0;
    }
  }
  
  public int getHW( byte[] data )
  {
    int hw = 0;
    int endpoint = data.length;
    // Since the data will be equivalent to digest length, this can be broken down to integers.
    for( int i = 0; i < endpoint; i += 4 )
    {
      ByteBuffer bbf = ByteBuffer.wrap
          (new byte[]{ data[i], data[i + 1], data[i + 2], data[i + 3] });
      hw += Integer.bitCount( bbf.getInt() );
    }
    return hw;
  }
  
  // From http://stackoverflow.com/a/140861 by http://stackoverflow.com/users/3093/dave-l
  public byte[] hexStringToByteArray(String s) 
  {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) 
    {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                             + Character.digit(s.charAt(i+1), 16));
    }
    return data;
  }
  
  public String getChainValue( String cv_length )
  {
    int cvlen = Integer.parseInt(cv_length);
    cvlen = cvlen / 4; // Make the chaining value equal number of byte character string.
    final String collection = new String("ABCDEF0123456789"); // You want a hexadecimal string.
    Random random = new Random();
    StringBuilder cv = new StringBuilder( cvlen );
    for( int i = 0; i < cvlen; i++ ) {
      cv.append( collection.charAt( random.nextInt( collection.length())));
    }
    return (cv.toString());
  }
  
  public int getEvaluation( Hash sha3, String msg1, String msg2, String cv, String rounds, 
      String digest_length )
  {
    msg1 += cv;     // Append the initial chaining value to both the message.
    msg2 += cv;
    byte[] hash1 = sha3.hash(msg1, Integer.parseInt(digest_length), Integer.parseInt(rounds));
    byte[] hash2 = sha3.hash(msg2, Integer.parseInt(digest_length), Integer.parseInt(rounds));
    for( int i = 0; i < hash1.length; i++ ) {
      hash1[i] ^= hash2[i];  // Bitwise XOR the two hash to get the evaluation function data.
    }
    return (getHW( hash1 ));
  }
  
  public byte[][] getNeighbours( byte[] cv )
  {
    byte[] neighbour_transform = new byte[]{ (byte)0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01 };
    int cvlen  = cv.length;
    int length = cvlen * 8;
    // We just need space for only 2 bit neighbours. For one bit neighbour the space would be
    // equivalent to the chaining value length and for 2 bit neighbour for chaining length n it
    // would be n(n - 1) / 2. That would be nC2. The space for first dimension has to be in bits.
    byte[][] neighbours = new byte[length + ((length * (length - 1)) / 2)][cvlen];
    int byte_pos;
    byte[] temp  = new byte[ cvlen ];
    byte[] temp2 = new byte[ cvlen ];
    for( int i = 0; i < length; )
    {
      byte_pos = i / 8; // This int index chooses the byte that undergoes the transformation.
      for( int j = 0; j < 8; j++ )
      {
        for( int k = 0; k < cvlen; k++ ) {
          temp[k] = cv[k];
        }
        temp[byte_pos] ^= neighbour_transform[j]; // Flip the jth bit in the selected CV byte.
        for( int k = 0 ; k < cvlen; k++ ) {
          neighbours[i][k] = temp[k];          // Now assign the temp byte array to neighbour.
        }
        i++;  // Increment the neighbour counter, each bit flip a new neighbour.
      }
    }
    int neighbour_index = length - 1;
    for( int i = 0; i < (length - 1); i++ )
    {
      for( int k = 0; k < cvlen; k++ ) {
        temp[k] = cv[k];    // First get the chaining value in the temp.
      }
      temp[i / 8] ^= neighbour_transform[i % 8]; // You flip the bit of the byte.
      for( int j = i + 1; j < length; j++ )
      {
        for( int k = 0; k < cvlen; k++ ) {
          temp2[k] = temp[k];       // Each time get the fresh copy of outer into the inner temp.
        }
        temp2[j / 8] ^= neighbour_transform[j % 8];
        neighbour_index++;
        for( int k = 0; k < cvlen; k++ ) {
          neighbours[neighbour_index][k] = temp2[k];
        }
      }
    }
    return neighbours;
  }
}
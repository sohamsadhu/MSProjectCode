package com.Soham.MSProject.SimulationAlgorithm;

import java.nio.ByteBuffer;

import com.Soham.MSProject.SHA3.Hash;

// What would you consider as near collision when then epsilon / n ratios would be
// 146/224 | 167/256 | 250/384 | 333/512
public class HillClimbing implements FindCollision
{
  /**
   * Returns Hamming weight or the number of bits that are 1 in the given byte array. 
   * @param data
   * @return integer that is number of bits that are 1 in the byte array data given.
   */
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
  
  public void startIterativeCollision()
  {
    long num_success = 0;
    long num_failure = 0;
    long sum_iteration_success = 0L;
    long sum_iteration_failure = 0L;
    double avg_iteration_success = 0;
    double avg_iteration_failure = 0;    
  }
  
  /**
   * Returns the Hamming weight of the two hash digests obtained from the two messages.
   * @param sha3   the object of the hash function that we are evaluating.
   * @param msg1
   * @param msg2   
   * @param cv     the chaining value
   * @param rounds the number of rounds the SHA-3 function will work.
   * @param digest_length the hash digest length that we are interested in.
   * @return
   */
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
  
  /**
   * Returns 2 bit neighbours of the the given chaining value.
   * @param cv chaining value whose neighbours are required.
   * @return 2D byte array that has the neighbours of that chaining value.
   */
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
        //System.out.println("neighbour index "+ neighbour_index);
        for( int k = 0; k < cvlen; k++ ) {
          neighbours[neighbour_index][k] = temp2[k];
        }
      }
    }
    return neighbours;
  }
  
  public void hillClimbing( Hash sha3, String msg1, String msg2, String cv, String rounds,
      String digest_length )
  {
    int best = getEvaluation( sha3, msg1, msg2, cv, rounds, digest_length );    
  }
  
  public static void main( String[] args )
  {
    HillClimbing hc = new HillClimbing();
    byte[][] neighbours = hc.getNeighbours(new byte[]{ 0x01, 0x01 });
    System.out.println(" length "+ neighbours.length);
    for( byte[] neighbour : neighbours )
    {
      System.out.printf("new byte[]{");
      for( byte b : neighbour ) {
        System.out.printf("(byte)0x%02X, ", b);
      }
      System.out.printf("}, ");
    }
  }
}
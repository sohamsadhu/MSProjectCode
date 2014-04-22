package com.Soham.MSProject.SimulationAlgorithm;

import java.nio.ByteBuffer;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;

import com.Soham.MSProject.SHA3.Hash;

public class HillClimbing implements FindCollision
{
  /**
   * Will return value of Hamming weight desired that is 65% of bits match, that would
   * be considered as collsion taken place.
   * @param digest_length
   * @return
   */
  public int getEpsilon( int digest_length )
  {
    switch( digest_length )
    {
    case 224: return 78;    // 146/224 bits match
    case 256: return 89;    // 167/256 bits match
    case 384: return 134;   // 250/384 bits match
    case 512: return 179;   // 333/512 bits match
    default:  return 0;
    }
  }
  
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
  
  /**
   * Generates random hex represented string, for the chaining value.
   * Source: http://stackoverflow.com/a/157202 by http://stackoverflow.com/users/21152/maxp
   * @param cv_length the length of chaining value.
   * @return
   */
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
  
  /**
   * Iterates the search algorithm 128 times with different chaining values.
   * @param sha3
   * @param msg1
   * @param msg2
   * @param cv
   * @param rounds
   * @param digest_length
   * @return an array of long of 4 elements, with first two saying number of success and 
   * iterations for the same, and next two doing same for the failures. 
   */
  public long[] startIterativeCollision( Hash sha3, String msg1, String msg2, String cv, 
      String rounds, String digest_length )
  {
    long num_success = 0L;
    long num_failure = 0L;
    long sum_iteration_success = 0L;
    long sum_iteration_failure = 0L;
    String chain_value;
    for( int i = 0; i < 128; i++ )  // Experiment with 128 different random chaining values.
    {
      chain_value = getChainValue( cv );
      long[] results = hillClimbing(sha3, msg1, msg2, chain_value, rounds, digest_length);
      if(1 == results[0]) 
      {
        num_success++;
        sum_iteration_success += results[1];
      }
      else
      {
        num_failure++;
        sum_iteration_failure += results[1];
      }
    }
    return (new long[]{num_success, sum_iteration_success, num_failure, sum_iteration_failure});
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
  
  /**
   * The algorithm that does the hill climbing to find the near collisions.
   * @param sha3 the algorithm used for hashing.
   * @param msg1 first of the message from message pairs
   * @param msg2
   * @param cv the starting chaining value
   * @param rounds the number of rounds the hashing algorithm will be run.
   * @param digest_length of the message digest.
   * @return
   */
  public long[] hillClimbing( Hash sha3, String msg1, String msg2, String cv, String rounds,
      String digest_length )
  {
    int best = getEvaluation( sha3, msg1, msg2, cv, rounds, digest_length );
    byte[][] neighbours = getNeighbours( hexStringToByteArray( cv ));
    boolean continue_search = true;
    long iteration = 0L;
    while( continue_search )
    {
      continue_search = false;
      int length = neighbours.length;
      for( int i = 0; i < length; i++ )
      {
        iteration++;    // Increment the iteration count, for each attempt.
        String chain_value = Hex.encodeHexString( neighbours[i] );
        // You take the greedy gradient ascent approach, by taking the first best chaining
        // value for the neighbours, and break the loop. You do not care for k-optimum that
        // is equality but only that selected chaining value has a less value than what we
        // have. If the minimum from the neighbours has greater value than present no point
        // in continuing. Even if it is equal you still check all values for least, but if
        // not found then no point in continuing.
        if(( getEvaluation( sha3, msg1, msg2, chain_value, rounds, digest_length ))  < best )
        {
          best = getEvaluation( sha3, msg1, msg2, chain_value, rounds, digest_length );
          cv   = chain_value;
          continue_search = true;
          break;
        }
      }
      if( continue_search ) {   // We got favourable result. Continue search with new neighbours.
        neighbours = getNeighbours( hexStringToByteArray( cv ));
      }
    }
    // If Hamming weight, best; is less than equal to set 65% colliding bits then success as 1.
    long success = (best <= getEpsilon( Integer.parseInt(digest_length) )) ? 1L : 0L;
    return (new long[]{ success, iteration });
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
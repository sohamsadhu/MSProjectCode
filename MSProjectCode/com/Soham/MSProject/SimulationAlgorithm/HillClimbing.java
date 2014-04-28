package com.Soham.MSProject.SimulationAlgorithm;

import org.apache.commons.codec.binary.Hex;

import com.Soham.MSProject.SHA3.Hash;

public class HillClimbing extends FindCollisionImpl
{  
  /**
   * Iterates the search algorithm 256 times with different chaining values.
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
      chain_value = getChainValue( cv );    // For each experiment get a new chaining value.
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
}
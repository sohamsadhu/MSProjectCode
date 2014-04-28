package com.Soham.MSProject.SimulationAlgorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.binary.Hex;

import com.Soham.MSProject.SHA3.Hash;

public class TabooSearch extends FindCollisionImpl
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
  public long[] startIterativeCollision(Hash sha3, String msg1, String msg2, String cv,
      String rounds, String digest_length) 
  {
    long num_success = 0L;
    long num_failure = 0L;
    long sum_iteration_success = 0L;
    long sum_iteration_failure = 0L;
    String chain_value;
    for( int i = 0; i < 128; i++ )  // Experiment with 128 different random chaining values.
    {
      chain_value = getChainValue( cv );    // For each experiment get a new chaining value.
      long[] results = tabooSearch(sha3, msg1, msg2, chain_value, rounds, digest_length);
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
   * The algorithm that does the taboo search to find the near collisions.
   * @param sha3 the algorithm used for hashing.
   * @param msg1 first of the message from message pairs
   * @param msg2
   * @param cv the starting chaining value
   * @param rounds the number of rounds the hashing algorithm will be run.
   * @param digest_length of the message digest.
   * @return
   */
  public long[] tabooSearch( Hash sha3, String msg1, String msg2, String cv, String rounds,
      String digest_length )
  {
    int best = getEvaluation( sha3, msg1, msg2, cv, rounds, digest_length );
    long iteration = 0;
    String best_candidate = null;
    int length = cv.length() * 4;   // Chaining value is in hexadecimal string format.
    List<byte[]> candidate_list;
    Set<byte[]> tabu_list = new LinkedHashSet<byte[]>(length);
    long trials = length * 10;  // Since tabu search takes steepest ascent.
    long num_trials = 0;        // So limit the tabu search to specific number of trials.
    boolean continue_search = true;
    while( continue_search  && (num_trials < trials))
    {
      continue_search = false;
      num_trials++;
      candidate_list = new ArrayList<byte[]>( length + (length * (length - 1)) / 2 );
      byte[][] neighbours = getNeighbours( hexStringToByteArray( cv ));
      for( byte[] neighbour : neighbours )
      {
        if( !tabu_list.contains( neighbour )) {
          candidate_list.add( neighbour );
        }
        iteration++;    // Note the iterations for tabu candidate list operation.
      }
      int minval = Integer.parseInt(digest_length); // Maximum hamming distance would be digest length.
      int tempminval;
      for( byte[] candidate : candidate_list )
      {
        tempminval = getEvaluation( sha3, msg1, msg2, Hex.encodeHexString(candidate), 
            rounds, digest_length );
        if( tempminval < minval )   // Find the best candidate.
        {
          minval = tempminval;
          best_candidate = Hex.encodeHexString(candidate);
        }
        iteration++;    // Increment the operation for search the best. Kind of steep ascent. 
      }
      if( minval < best )
      {
        continue_search = true;
        cv = best_candidate;
        tabu_list.add( hexStringToByteArray( cv ));
        for( Iterator<byte[]> li = tabu_list.iterator(); li.hasNext() 
            && (tabu_list.size() > length);  )
        {
          li.next();
          li.remove();
          iteration++;  // increment the operation for removal.
        }
      }
    }
    long success = (best <= getEpsilon( Integer.parseInt(digest_length) )) ? 1L : 0L;
    return (new long[]{ success, iteration });
  }
}
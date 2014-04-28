package com.Soham.MSProject.SimulationAlgorithm;

import java.util.Random;

import org.apache.commons.codec.binary.Hex;

import com.Soham.MSProject.SHA3.Hash;

public class SimulatedAnnealing extends FindCollisionImpl
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
      long[] results = simulatedAnnealing(sha3, msg1, msg2, chain_value, rounds, digest_length);
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
   * The algorithm that does the simulated annealing to find the near collisions. Reference taken
   * from http://www.theprojectspot.com/tutorial-post/simulated-annealing-algorithm-for-beginners/6
   * by Lee Jacobson
   * @param sha3 the algorithm used for hashing.
   * @param msg1 first of the message from message pairs
   * @param msg2
   * @param cv the starting chaining value
   * @param rounds the number of rounds the hashing algorithm will be run.
   * @param digest_length of the message digest.
   * @return
   */
  public long[] simulatedAnnealing( Hash sha3, String msg1, String msg2, String cv, String rounds,
      String digest_length )
  {
    int best = 0;
    int delta = 0;
    byte[][] neighbours = getNeighbours( hexStringToByteArray( cv ));
    String next = null;
    long iteration = 0L;
    double temperature = Math.pow((cv.length() * 4), 2);  // Make the temperature as square of bit length
    double cooling_rate = 1;    // of chaining value which is roughly twice of neighbourhood size.
    double probability;
    Random random = new Random();
    while( temperature > 0 )
    {
      best = getEvaluation( sha3, msg1, msg2, cv, rounds, digest_length );      
      temperature = temperature - cooling_rate; // Just a simple decrement.
      iteration++;
      next = Hex.encodeHexString( neighbours[random.nextInt( neighbours.length )] );
      delta = best - getEvaluation( sha3, msg1, msg2, next, rounds, digest_length );
      // If the difference is positive then next value had better collision and smaller number.
      // Choose that one as your next chaining value.
      if( delta > 0 ) 
      { 
        cv = next;
        neighbours = getNeighbours( hexStringToByteArray( next ));
      } 
      else 
      {
        probability = Math.pow(Math.E, (delta / temperature));
        if( probability > random.nextDouble() ) 
        { 
          cv = next;
          neighbours = getNeighbours( hexStringToByteArray( next ));
        }
      }
    }
    long success = (best <= getEpsilon( Integer.parseInt(digest_length) )) ? 1L : 0L;
    return (new long[]{ success, iteration });
  }
}
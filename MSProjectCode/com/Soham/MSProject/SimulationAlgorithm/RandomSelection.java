package com.Soham.MSProject.SimulationAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import org.apache.commons.codec.binary.Hex;

import com.Soham.MSProject.SHA3.Hash;

public class RandomSelection extends FindCollisionImpl
{
  /**
   * Calculates the average time taken, for particular chaining value length, SHA-3, digest 
   * length, and given rounds for all input files as evaluated by the rest of the collision
   * finding methods.
   * @param sha3
   * @param digest_len
   * @param rounds
   * @param cv
   * @return
   */
  public long getTrials( Hash sha3, String digest_len, String rounds, String cv )
  {
    File    file = null;
    Scanner scan = null;
    String[] collision_methods = new String[]{"HillClimbing", "SimulatedAnnealing", "TabooSearch"};
    String hash_method = sha3.getClass().getName().substring
        (sha3.getClass().getName().lastIndexOf('.') + 1);
    String path_template = new String("./Output/%s/"+ digest_len +"/"+ hash_method +"/"+ rounds);
    double sum_iters = 0;
    for( String collision : collision_methods )
    {
      String path = String.format(path_template, collision);
      String[] parts;
      for( int i = 1; i < 21; i++ )
      {
        String file_path = path + "/" + i +".txt";
        String line_avg_iter = null;
        try
        {
          file = new File( file_path );
          scan = new Scanner( file );
          int line_counter = 1;
          while( scan.hasNextLine() )
          {
            if( 6 == line_counter ) {
              line_avg_iter = scan.nextLine();  // Get the 6th total average iterations.
            } else {
              scan.nextLine();      // Just empty scan the file.
            }            
            line_counter++;
          }
          scan.close();
        }
        catch( IOException ioex ) {
          ioex.printStackTrace();
        }
        parts = line_avg_iter.split(":");
        double iters = Double.parseDouble( parts[1] );  // Extract total 
        sum_iters += iters;
      }
    }
    // There are 20 files for 3 of the collision method, so average is out of 60.
    long average_iterations = (long)( sum_iters / 60 );
    return (average_iterations);
  }
  
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
    for( int i = 0; i < 256; i++ )  // Experiment with 128 different random chaining values.
    {
      chain_value = getChainValue( cv );    // For each experiment get a new chaining value.
      long[] results = randomSelect(sha3, msg1, msg2, chain_value, rounds, digest_length);
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
   * The algorithm that does the random selection to find the near collisions.
   * @param sha3 the algorithm used for hashing.
   * @param msg1 first of the message from message pairs
   * @param msg2
   * @param cv the starting chaining value
   * @param rounds the number of rounds the hashing algorithm will be run.
   * @param digest_length of the message digest.
   * @return
   */
  public long[] randomSelect( Hash sha3, String msg1, String msg2, String cv, String rounds,
      String digest_length )
  {
    // Random search will go for trials, based on average trials from each of other methods.
    long trials = getTrials( sha3, digest_length, rounds, cv );
    long trial_counter = 0;
    Random random = new Random();
    int best = getEvaluation( sha3, msg1, msg2, cv, rounds, digest_length );
    byte[][] neighbours = getNeighbours( hexStringToByteArray( cv ));
    while( trial_counter < trials )
    {
      String chain_value = Hex.encodeHexString( neighbours[random.nextInt( neighbours.length )] );
      if(( getEvaluation( sha3, msg1, msg2, chain_value, rounds, digest_length ))  < best )
      {
        best = getEvaluation( sha3, msg1, msg2, chain_value, rounds, digest_length );
        cv   = chain_value;
        neighbours = getNeighbours( hexStringToByteArray( cv ));
      }
      trial_counter++;
    }
    long success = (best <= getEpsilon( Integer.parseInt(digest_length) )) ? 1L : 0L;
    return (new long[]{ success, trials });
  }
}
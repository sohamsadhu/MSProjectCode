package com.Soham.MSProject.SimulationAlgorithm;

import com.Soham.MSProject.SHA3.Hash;

public class RandomSelection extends FindCollisionImpl
{  
  public long getTrialsBLAKE224( String rounds, String cv )
  {
    switch( rounds )
    {
    case "1": return 0;
    case "2": return 0;
    case "3": return 0;
    default:  return 0;
    }
  }
  
  public long getTrialsGroestl224( String rounds, String cv )
  {
    switch( rounds )
    {
    case "1": return 0;
    case "2": return 0;
    case "3": return 0;
    default:  return 0;
    }
  }
  
  public long getTrialsKeccak224( String rounds, String cv )
  {
    switch( rounds )
    {
    case "1": return 0;
    case "2": return 0;
    case "3": return 0;
    default:  return 0;
    }
  }
  
  public long getTrialsBLAKE256( String rounds, String cv )
  {
    switch( rounds )
    {
    case "1": return 0;
    case "2": return 0;
    case "3": return 0;
    default:  return 0;
    }
  }
  
  public long getTrialsGroestl256( String rounds, String cv )
  {
    switch( rounds )
    {
    case "1": return 0;
    case "2": return 0;
    case "3": return 0;
    default:  return 0;
    }
  }
  
  public long getTrialsKeccak256( String rounds, String cv )
  {
    switch( rounds )
    {
    case "1": return 0;
    case "2": return 0;
    case "3": return 0;
    default:  return 0;
    }
  }
  
  public long getTrialsBLAKE384( String rounds, String cv )
  {
    switch( rounds )
    {
    case "1": return 0;
    case "2": return 0;
    case "3": return 0;
    default:  return 0;
    }
  }
  
  public long getTrialsGroestl384( String rounds, String cv )
  {
    switch( rounds )
    {
    case "1": return 0;
    case "2": return 0;
    case "3": return 0;
    default:  return 0;
    }
  }
  
  public long getTrialsKeccak384( String rounds, String cv )
  {
    switch( rounds )
    {
    case "1": return 0;
    case "2": return 0;
    case "3": return 0;
    default:  return 0;
    }
  }
  
  public long getTrialsBLAKE512( String rounds, String cv )
  {
    switch( rounds )
    {
    case "1": return 0;
    case "2": return 0;
    case "3": return 0;
    default:  return 0;
    }
  }
  
  public long getTrialsGroestl512( String rounds, String cv )
  {
    switch( rounds )
    {
    case "1": return 0;
    case "2": return 0;
    case "3": return 0;
    default:  return 0;
    }
  }
  
  public long getTrialsKeccak512( String rounds, String cv )
  {
    switch( rounds )
    {
    case "1": return 0;
    case "2": return 0;
    case "3": return 0;
    default:  return 0;
    }
  }
  
  public long getTrials224( Hash sha3, String rounds, String cv )
  {
    switch( sha3.getClass().getName() )
    {
    case "com.Soham.MSProject.SHA3.BLAKE":   return getTrialsBLAKE224( rounds, cv );
    case "com.Soham.MSProject.SHA3.Groestl": return getTrialsGroestl224( rounds, cv );
    case "com.Soham.MSProject.SHA3.Keccak":  return getTrialsKeccak224( rounds, cv );
    default: return 0;
    }
  }
  
  public long getTrials256( Hash sha3, String rounds, String cv )
  {
    switch( sha3.getClass().getName() )
    {
    case "com.Soham.MSProject.SHA3.BLAKE":   return getTrialsBLAKE256( rounds, cv );
    case "com.Soham.MSProject.SHA3.Groestl": return getTrialsGroestl256( rounds, cv );
    case "com.Soham.MSProject.SHA3.Keccak":  return getTrialsKeccak256( rounds, cv );
    default: return 0;
    }
  }
  
  public long getTrials384( Hash sha3, String rounds, String cv )
  {
    switch( sha3.getClass().getName() )
    {
    case "com.Soham.MSProject.SHA3.BLAKE":   return getTrialsBLAKE384( rounds, cv );
    case "com.Soham.MSProject.SHA3.Groestl": return getTrialsGroestl384( rounds, cv );
    case "com.Soham.MSProject.SHA3.Keccak":  return getTrialsKeccak384( rounds, cv );
    default: return 0;
    }
  }
  
  public long getTrials512( Hash sha3, String rounds, String cv )
  {
    switch( sha3.getClass().getName() )
    {
    case "com.Soham.MSProject.SHA3.BLAKE":   return getTrialsBLAKE512( rounds, cv );
    case "com.Soham.MSProject.SHA3.Groestl": return getTrialsGroestl512( rounds, cv );
    case "com.Soham.MSProject.SHA3.Keccak":  return getTrialsKeccak512( rounds, cv );
    default: return 0;
    }
  }
  
  public long getTrials( Hash sha3, String digest_len, String rounds, String cv )
  {
    switch( digest_len )
    {
    case "224": return getTrials224( sha3, rounds, cv );
    case "256": return getTrials256( sha3, rounds, cv );
    case "384": return getTrials384( sha3, rounds, cv );
    case "512": return getTrials512( sha3, rounds, cv );
    default: return 0;
    }
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
    // Random search will be fished based on the average data.
    long trials = getTrials( sha3, digest_length, rounds, cv );
    return null;
  }
}
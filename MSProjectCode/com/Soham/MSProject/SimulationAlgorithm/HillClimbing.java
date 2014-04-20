package com.Soham.MSProject.SimulationAlgorithm;

import java.nio.ByteBuffer;

import com.Soham.MSProject.SHA3.Hash;
import com.Soham.MSProject.SHA3.Keccak;

// Design of to be experiment.
// First hierarchy would be the folder with chaining value,
// The folder numbers would be 32, 64, 128, 256, 512, 1024.
// Next in each of these folders would 4 attack algorithms. So each folder 
// would have hill climb, simulated annealing, taboo search, random selection.
// Each of these algorithm will have 4 digest sizes consisting of 224, 256, 384, 512.
// Each of the digest size will have 3 SHA-3 finalist algorithm folder.
// Each of the finalist algorithm folder will have 3 rounds folder to them.
// Each round will have another 3 folder for start, middle and end. And each folder
// will have 20 files in them. So 51,840 files of data for me to handle.
// So the file will consist of number of success, number of failures, 
// average iteration for failure, success, and average iteration.

// What would you consider as near collision when then epsilon / n ratios would be
// 146/224 | 167/256 | 250/384 | 333/512
public class HillClimbing implements FindCollision
{
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
  
  public int getBest( Hash sha3, String msg1, String msg2, String cv, String rounds, 
      String digest_length )
  {
    msg1 += cv;     // Append the initial chaining value to both the message.
    msg2 += cv;
    byte[] hash1 = sha3.hash(msg1, Integer.parseInt(digest_length), Integer.parseInt(rounds));
    byte[] hash2 = sha3.hash(msg2, Integer.parseInt(digest_length), Integer.parseInt(rounds));
    for( int i = 0; i < hash1.length; i++ ) {
      hash1[i] ^= hash2[i];
    }
    return (getHW( hash1 ));
  }
  
  public void hillClimbing( Hash sha3, String msg1, String msg2, String cv, String rounds,
      String digest_length )
  {
    int best = getBest( sha3, msg1, msg2, cv, rounds, digest_length );
  }
  
  public static void main( String[] args )
  {
    HillClimbing hc = new HillClimbing();
    Keccak k = new Keccak();
    byte[] b = k.hash("54686520717569636b2062726f776e20666f78206a756d7073206f76657220746865"
        + "206c617a7920646f67", 224, 0);
    System.out.println("\nHW is "+ hc.getHW( b ));
    b = k.hash("54686520717569636b2062726f776e20666f78206a756d7073206f76657220746865"
        + "206c617a7920646f67", 256, 0);
    System.out.println("HW is "+ hc.getHW( b ));
    b = k.hash("54686520717569636b2062726f776e20666f78206a756d7073206f76657220746865"
        + "206c617a7920646f67", 384, 0);
    System.out.println("HW is "+ hc.getHW( b ));
    b = k.hash("54686520717569636b2062726f776e20666f78206a756d7073206f76657220746865"
        + "206c617a7920646f67", 512, 0);
    System.out.println("HW is "+ hc.getHW( b ));
  }
}
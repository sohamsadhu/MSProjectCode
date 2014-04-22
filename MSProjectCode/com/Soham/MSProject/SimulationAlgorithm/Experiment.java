package com.Soham.MSProject.SimulationAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import com.Soham.MSProject.SHA3.BLAKE;
import com.Soham.MSProject.SHA3.Groestl;
import com.Soham.MSProject.SHA3.Hash;
import com.Soham.MSProject.SHA3.Keccak;

// Where or what this class has to write. 
// First level hierarchy would be the main Output folder.
// Then the chaining values 32, 64, 128, 256, 512, 1024.
// Next folder would be hill climb, simulated annealing, taboo search, random selection.
// Each attack on digest size of folder 224, 256, 384, 512.
// Each digest size has 3 SHA3 finalist folder BLAKE, Keccak, Groestl
// Each SHA3 will have 3 rounds of folder 1, 2, 3
// Each folder will have 20 files with 1.txt, 2.txt ... that will have
// So the file will consist of number of success, number of failures, 
// average iteration for failure, success, and average iteration.
public class Experiment 
{  
  /**
   * By default the hash function returned is that of Keccak.
   * @param hash_name any one of the 3 SHA-3 finalist string name.
   * @return the object of the hash function.
   */
  public Hash getSHA3( String hash_name )
  {
    switch( hash_name )
    {
    case "BLAKE":   return (new BLAKE());
    case "Groestl": return (new Groestl());
    case "Keccak":  return (new Keccak());
    default:        return (new Keccak());
    }
  }
  
  /**
   * Return the collision fetching algorithm object. By default return random search.
   * @param collision the class name of the collision you are looking for.
   * @return the object of the implemented algorithm.
   */
  public FindCollision getCollision( String collision )
  {
    switch( collision )
    {
    case "Hill Climbing":       return (new HillClimbing());
    case "Simulated Annealing": return (new SimulatedAnnealing());
    case "Taboo Search":        return (new TabooSearch());
    case "Random Search":       return (new RandomSelection());
    default:                    return (new RandomSelection());
    }
  }
  
  /**
   * Return the message pair in the file name in the respective flipped folder.
   * @param flipend the folder where the file should be.
   * @param file_name
   * @return array with two strings.
   */
  public String[] getMessages( String flipend, int file_name )
  {
    File    file = null;
    Scanner scan = null;
    String msg1 = null, msg2 = null;
    try 
    {
      file = new File( String.format("./Input/"+ flipend +"/%s", file_name +".txt") ); 
      scan = new Scanner( file );
      int j = 1;        
      while( scan.hasNextLine() && j < 3 )
      {
        if( 1 == j ) {
          msg1 = scan.nextLine();
        } else {
          msg2 = scan.nextLine();
        }
        j++;
      }
      scan.close();
    } 
    catch (IOException e) {
      e.printStackTrace();
    }
    return (new String[]{msg1, msg2});
  }
  
  public void getCollisions( String cv, FindCollision fc, Hash sha3, String digest_len,
      String rounds, String flipend, String msg1, String msg2 )
  {
    // What this thing needs to write in the output file? Four things
    // 1.number of success 2.number of failure 3.average iter success 4.avg iteration failure.
  }
  
  public void startExperiment( String cv, String m_collision, String diglen, String sha3,
      String rounds, String flipend )
  {
    FindCollision fc   = getCollision( m_collision );
    Hash          hash = getSHA3( sha3 );
    for( int i = 1; i < 21; i++ )
    {
      String[] msgpairs = getMessages( flipend, i );
      getCollisions( cv, fc, hash, diglen, rounds, flipend, msgpairs[0], msgpairs[1] );
    }
  }
  
  public static void main(String [] args)
  {
    Experiment e = new Experiment();
    System.out.println(e.getSHA3("BLAKE").getClass().getName());
  }
}
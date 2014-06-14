package com.Soham.MSProject.SimulationAlgorithm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.Soham.MSProject.SHA3.BLAKE;
import com.Soham.MSProject.SHA3.Groestl;
import com.Soham.MSProject.SHA3.Hash;
import com.Soham.MSProject.SHA3.Keccak;

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
  
  /**
   * Based on the parameters it will create the file in ./Output/cv/collision algorithm/
   * digest length/SHA-3/rounds/flipped end/input file.txt. There are 6 things written to this
   * file separated by : on each line. They are number of success from 256 tries, the number of
   * iterations for success, and average iterations for success. Similar 2 values for failure,
   * and lastly total iterations and average iterations.
   * @param cv
   * @param fc
   * @param sha3
   * @param digest_len
   * @param rounds the number of rounds that the given SHA-3 algorithm would run to produce hash. 
   * @param flipend
   * @param ip_file the input file name in int, from the input folder flipped end.
   * @param msg1 the first of the two message which differs from the second by only a bit.
   * @param msg2
   */
  public void getCollisions( String cv, FindCollision fc, Hash sha3, String digest_len,
      String rounds, String flipend, int ip_file, String msg1, String msg2 )
  {
    String collision_folder = fc.getClass().getName()
        .substring(fc.getClass().getName().lastIndexOf('.') + 1);
    String sha3_folder = sha3.getClass().getName().substring
        (sha3.getClass().getName().lastIndexOf('.') + 1);
    File f = new File("./Output/"+ cv +"/"+ collision_folder +"/"+ digest_len +"/"+ sha3_folder +
        "/"+ rounds +"/"+ flipend +"/"+ ip_file +".txt");
    f.getParentFile().mkdirs();
    double avg_success_iter, avg_fail_iter, avg_iter;
    long total_iter;
    try
    {
      BufferedWriter bw = new BufferedWriter(new FileWriter(f.getAbsoluteFile()));
      long[] results = fc.startIterativeCollision(sha3, msg1, msg2, cv, rounds, digest_len);
      bw.write("Success:"+ results[0]); bw.newLine();
      bw.write("Total number of success iterations:"+ results[1]); bw.newLine();
      avg_success_iter = (0 != results[0]) ? ((results[1] * 1.0) / results[0]) : 0.0;
      bw.write("Average number of success iterations:"+ avg_success_iter); bw.newLine();
      bw.write("Failure:"+ results[2]); bw.newLine();
      bw.write("Total number of failure iterations:"+ results[3]); bw.newLine();
      avg_fail_iter = (0 != results[2]) ? ((results[3] * 1.0) / results[2]) : 0.0;
      bw.write("Average number of failed iterations:"+ avg_fail_iter); bw.newLine();
      total_iter = results[1] + results[3];
      bw.write("Total iterations:"+ total_iter); bw.newLine();
      avg_iter = (total_iter * 1.0) / (results[0] + results[2]);
      bw.write("Average iterations:"+ avg_iter);
      bw.close();
    } 
    catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Go through each of the input folders and provide with the file name and the input string
   * messages to work with to the function that calls experiment and write them in result folder.
   * @param cv
   * @param m_collision
   * @param diglen
   * @param sha3
   * @param rounds
   * @param flipend
   */
  public void startExperiment( String cv, String m_collision, String diglen, String sha3,
      String rounds, String flipend )
  {
    FindCollision fc   = getCollision( m_collision );
    Hash          hash = getSHA3( sha3 );
    for( int i = 1; i < 21; i++ )
    {
      String[] msgpairs = getMessages( flipend, i );
      getCollisions( cv, fc, hash, diglen, rounds, flipend, i, msgpairs[0], msgpairs[1] );
    }
  }
  
  // A helper method to loop over the different experiment variables and start the experiment.
  public void loopExperimentVariables()
  {
    String[] cv         = new String[]{"32"}; //"32", "64", "128", "256", "512"
    String[] fc         = new String[]{"Hill Climbing"};
                                      //"Random Search", "Simulated Annealing", "Taboo Search"
    String[] sha3       = new String[]{"BLAKE", "Groestl", "Keccak"};
    String[] digest_len = new String[]{"224", "256", "384", "512"};
    String[] rounds     = new String[]{"3", "4", "5"};
    String[] flipend    = new String[]{"Start", "Middle", "End"};
    // Ugly bow shaped loop, coming up!
    for( String chain_value : cv ) {
      for( String find_collision : fc ) {
        for( String hash_method : sha3 ) {
          for( String digest_length : digest_len ) {
            for( String round : rounds ) {
              for( String flip_folder : flipend ) {
                startExperiment(chain_value, find_collision, digest_length, hash_method, 
                    round, flip_folder);
              }
            }
          }
        }
      }
    }
  }
  
  public static void main(String [] args)
  {
    Experiment e = new Experiment();
    System.out.println("Start experiment.");
    e.loopExperimentVariables();
    System.out.println("End experiment.");
  }
}
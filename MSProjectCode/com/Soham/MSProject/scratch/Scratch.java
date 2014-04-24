package com.Soham.MSProject.scratch;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Scratch 
{  
  public static void main( String [] args )
  {
    File    file = null;
    Scanner scan = null;
    String[] collision_methods = new String[]{"HillClimbing"};
    String hash_method = "BLAKE";
    String path_template = new String("./Output/%s/224/"+ hash_method +"/1");
    double sum_iters = 0;
    for( String collision : collision_methods )
    {
      String path = String.format(path_template, collision);
      String[] parts;
      for( int i = 1; i < 6; i++ )
      {
        String file_path = path + "/" + i +".txt";
        String line_avg_iter = null;
        try
        {
          file = new File( file_path );
          System.out.println("on file path "+ file_path);
          scan = new Scanner( file );
          int line_counter = 1;
          while( scan.hasNextLine() )
          {
            if( 6 == line_counter ) {
              line_avg_iter = scan.nextLine();
            } else {
              scan.nextLine();
            }            
            line_counter++;
          }
          scan.close();
        }
        catch( IOException ioex ) {
          ioex.printStackTrace();
        }
        System.out.println("line obtained "+ line_avg_iter);
        parts = line_avg_iter.split(":");
        double iters = Double.parseDouble( parts[1] );
        sum_iters += iters;
      }
    }
//     There are 20 files for each of the collision finding method, so average is out of 60.
    long average_iterations = (long)( sum_iters / 5 );
    System.out.println("avg iterations "+ average_iterations);
  }
}
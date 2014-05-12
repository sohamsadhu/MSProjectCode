package com.Soham.MSProject.Input;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CreateOutputXls 
{
  public float getAverage( String path_begin, String chain_value, String find_collision, 
      String hash_method, String digest_length, String round, String flip_folder )
  {
    String file_path = new String("./"+ path_begin +"/"+ chain_value +"/"+ find_collision 
        +"/"+ digest_length +"/"+ hash_method +"/"+ round +"/"+ flip_folder +"/");
    String[] files = new String[]{"1.txt", "2.txt", "3.txt", "4.txt", "5.txt", "6.txt", "7.txt",
        "8.txt", "9.txt", "10.txt", "11.txt", "12.txt", "13.txt", "14.txt", "15.txt", "16.txt",
        "17.txt", "18.txt", "19.txt", "20.txt"};
    float sum = 0;
    for( String file_name : files )
    {
      File file = new File(file_path + file_name);
      try
      {
        Scanner scan = new Scanner( file );
        int j = 0;        
        while( scan.hasNextLine() && j < 8 )
        {
          if( 7 == j ) 
          {
            String[] parts = scan.nextLine().split(":");
            sum += Float.parseFloat( parts[1] );
          } 
          else {
            scan.nextLine();
          }
          j++;
        }
        scan.close();
      }
      catch( IOException ioex ) {
        ioex.printStackTrace();
      }
    }
    return (sum/20);
  }
  
  public void fileLoop()
  {
    String   path_begin = "Output/";
    String[] cv         = new String[]{"32"}; //"32", "64", "128", "256", "512"
    String[] fc         = new String[]{"HillClimbing"};
    String[] sha3       = new String[]{"BLAKE", "Groestl", "Keccak"};    //
    String[] digest_len = new String[]{"512"};
    String[] rounds     = new String[]{"3", "4"};
    String[] flipend    = new String[]{"Start", "Middle", "End"};
    // Ugly bow shaped loop, coming up!
    for( String chain_value : cv ) {
      for( String find_collision : fc ) {
        for( String hash_method : sha3 ) {
          for( String digest_length : digest_len ) {
            for( String round : rounds ) {
              for( String flip_folder : flipend ) {
                float avg = getAverage( path_begin, chain_value, find_collision, hash_method, 
                    digest_length, round, flip_folder );
                System.out.println("avg "+ hash_method +" "+ round +" "+ flip_folder +" "+ avg);
              }
            }
          }
        }
      }
    }
  }
  
  public static void main( String[] args )
  {
    CreateOutputXls cox = new CreateOutputXls();
    cox.fileLoop();
  }
}
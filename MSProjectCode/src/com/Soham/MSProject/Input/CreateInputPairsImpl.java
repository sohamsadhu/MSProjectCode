package com.Soham.MSProject.Input;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class CreateInputPairsImpl implements CreateInputPairs 
{  
  public void writePairsToFile( byte [] input, int combinations, int encoded_length,
      String output_file)
  {
    try
    {
      File file = new File(output_file + ".txt");
      if (!file.exists()) {
        file.createNewFile();
      }
      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      String something;
      byte temp = input[encoded_length - 1];
      for( int i = 0; i < 8; i++ ) 
      {
        byte b = (byte) (temp ^ (1 << i));
        input[encoded_length - 1] = b;
        String s = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        something = new String(input);
        bw.write(something); bw.newLine();
        bw.write(s); bw.newLine();
      }
      bw.close();
      fw.close();
    } 
    catch (IOException e)
    {
      System.out.println("The input file could not be created.");
      e.printStackTrace();
    }
  }
  
  public void createInputPairsFile(String seed, int combinations, String output_file) 
  {
    byte[] input = new byte[]{};
    try 
    {
      input = seed.getBytes("utf-8");
    } 
    catch (UnsupportedEncodingException e) 
    {
      e.printStackTrace();
      System.out.println("The given seed could not be encoded. Exiting now.");
      return;
    }
    writePairsToFile( input, combinations, input.length, output_file );
  }
  
  public void createFile( String seed, String flipend, Integer flips, String file_name )
  {
    System.out.println( "Seed " + seed + " flipend " + flipend + " flips " + flips +
        "file name " + file_name );
  }
  
  public static void main(String [] args) 
  {
    if( args.length < 3 )
    {
      System.out.println("The number of arguments are incorrect.");
      System.out.println("Usage: java CreateInputPairsImpl \"seed\" "
          + "\"number of combinations\" \"output file name\"");
      System.exit(0);
    }
    String seed = args[0];
    int combinations = 0;
    try 
    {
      combinations = Integer.parseInt(args[1]);
    } 
    catch( NumberFormatException nex ) 
    {
      System.out.println("The second argument to program should be a function.");
      System.out.println("Usage: java CreateInputPairsImpl \"seed\" "
          + "\"number of combinations\" \"output file name\"");
      nex.printStackTrace();
      System.exit(0);
    }
    String output_file = args[2];
    CreateInputPairs cip = new CreateInputPairsImpl();
    cip.createInputPairsFile(seed, combinations, output_file);
  }
}
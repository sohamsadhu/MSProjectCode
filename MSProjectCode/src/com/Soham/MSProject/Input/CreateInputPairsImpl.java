package com.Soham.MSProject.Input;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
  
  public String[] flipSeedStarting( final String seed, final int flips )
  {
    return ( new String[] { null } );
  }
  
  public String[] flipSeedMiddle( final String seed, final int flips )
  {
    return ( new String[] { null } );
  }
  
  public String[] flipSeedTrailing( final String seed, final int flips )
  {
    return ( new String[] { null } );
  }
  
  public String[] getFlippedSeeds( final String seed, final String flipend, 
      final int flips )
  {
    if( 0 == flips ) { // If flips are just 0, no need to flip anything.
      return new String[]{ seed };
    }
    switch( flipend )
    {
      case "Starting" :
        return flipSeedStarting( seed, flips );
      case "Middle" :
        return flipSeedMiddle( seed, flips );
      case "Trailing" :
        return flipSeedTrailing( seed, flips );
      default :  // If no choice prescribed, flip bits from starting.
        return flipSeedStarting( seed, flips );
    }
  }
  
  public void writeToFile( final String seed, final String flipend, 
      final Integer flips, final File file )
  {
    String [] flipped_seeds = getFlippedSeeds( seed, flipend, flips );
  }
  
  public Object[] checkInputFileOptions( final String seed, final String flip_end,
      final int flips, final String file_name)
  {
    Object [] checked_result = new Object[]{ true, "" } ;
    
    // Make sure all the values are present.
    if ((seed == null) || (flip_end == null) || (file_name == null))
    {
      System.out.println("1");
      checked_result[0] = false;
      checked_result[1] = "Please make sure values for all parameters for "
          + "input file creation are there.";
      return checked_result; // Since preliminary failure return here.
    }
    
    // Make sure the end to flip bits has been specified properly.
    if( flip_end.equals("Starting") || flip_end.equals("Middle") || 
        flip_end.equals("Trailing") ) 
    { /*Do nothing. Everything is fine. Else, make a note of error.*/}
    else
    {
      checked_result[0] = false;
      checked_result[1] += "Please enter which end you want the bits to flip. "
          + "Starting, Middle or Trailing. \n";
    }
    
    // Make sure that flips are in between 0 and 21.
    if ((flips < 1) || (flips > 20))
    {
      checked_result[0] = false;
      checked_result[1] += "The number of flips has to be in between 1 and 20, inclusive. \n";
    }
    
    // Check the file name is not null or not empty.
    if ((file_name == null) || (file_name.equals("")))
    {
      checked_result[0] = false;
      checked_result[1] += "The file name to be created cannot be empty.";
    }
    return checked_result;
  }
  
  public Object[] createFile( final String seed, final String flip_end, 
      final Integer flips, final String file_name )
  {
    System.out.println( "Seed " + seed + " flipend " + flip_end + " flips " + flips +
        " file name " + file_name );
    Object[] check_result = checkInputFileOptions(seed, flip_end, flips.intValue(), file_name);
    if( !(( Boolean )check_result[0]) ) {
      return check_result;  // If one of the parameters is in error, send error message.
    }
    String filename = "";
    try 
    {
      filename = new String(URLEncoder.encode(file_name, "UTF-8")); // Encode any shit for an inappropriate file name.
      filename = filename.endsWith(".txt") ? filename : (filename + ".txt"); // Make sure of its' text format.
      System.out.println("File name is "+ filename);
      File file = new File( filename );  // Create the file object and the file.
      if (!file.exists()) {
        file.createNewFile();
      }
      writeToFile( seed, flip_end, flips, file );
    }
    catch (UnsupportedEncodingException uex) 
    {
      uex.printStackTrace();
      return (new Object[] { false, "Please provide a valid name for input file to be created." });
    }
    catch( IOException iex )
    {
      iex.printStackTrace();
      return (new Object[] { false, "Input file creation failed. Could not create or write to the file." });
    }
    return (new Object[] {true, "Input file "+ filename +" successfully created."});
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class CreateInputPairsImpl implements CreateInputPairs 
{
  
  public void writePairsToFile( byte [] input, int combinations, int encoded_length )
  {
    try 
    {
      File file = new File("input.txt");
      if (!file.exists()) {
        file.createNewFile();
      }        
      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      String something;
      byte temp = input[encoded_length - 1];
      for( int i = 0; i < 8; i++ ) {
        byte b = (byte) (temp ^ (1 << i));
        input[encoded_length - 1] = b;
        String s = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        something = new String(input);
        bw.write(something); bw.newLine();
        bw.write(s); bw.newLine();
      }
      bw.close();
    } catch (IOException e) {
      System.out.println("The input file could not be created.");
      e.printStackTrace();
    }
  }
  
  public void createInputPairsFile(String seed, int combinations) 
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
    writePairsToFile( input, combinations, input.length );
  }
  
  public static void main(String [] args) 
  {
    if( args.length < 2 )
    {
      System.out.println("The number of arguments are incorrect.");
      System.out.println("Usage: java CreateInputPairsImpl \"seed\" number_of_combinations");
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
      System.out.println("Usage: java CreateInputPairsImpl \"seed\" number_of_combinations");
      nex.printStackTrace();
      System.exit(0);
    }
    CreateInputPairs cip = new CreateInputPairsImpl();
    cip.createInputPairsFile(seed, combinations);
  }
}
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * This class will create the input file containing pairs of text. The seed text will be given to the file.
 * And that will be changed for each one of the bits, step by step. That is bits beginning from 1 to 15
 * in the file will be changed, if asked for 15 different pairs to be made. Then pairs will be made, so that
 * would be <sup>15</sup>C<sub>2<sub> = 105 pairs of text, for my experimental data.
 * @author ssadhu
 * 
 */
public class CreateInputPairs {
  
  public static void main(String [] args) {
    String text = "Hello World!";
    byte[] utf8 = new byte[]{};
    try {
      utf8 = text.getBytes("utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
//    for( byte i : utf8 ) {
//      String s = String.format("%8s", Integer.toBinaryString(i & 0xFF)).replace(' ', '0');
//      System.out.println(s);
//    }
    byte [] temp = utf8;
    try {
      File file = new File("input.txt");
      if (!file.exists()) {
        file.createNewFile();
      }        
      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      String something;
      byte some = temp[11];
      for( int i = 0; i < 8; i++ ) {
        byte b = (byte) (some ^ (1 << i));
        temp[11] = b;
        //s = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        something = new String(utf8);
        bw.write(something + "\n");
      }
      bw.close();
    } catch (IOException e) {
      System.out.println("The input file could not be created.");
      e.printStackTrace();
    }    
//    for( byte i : utf8 ) {
//      String s = String.format("%8s", Integer.toBinaryString(i & 0xFF)).replace(' ', '0');
//      System.out.print(s);
//    }
    String s = String.format("%8s", Integer.toBinaryString(utf8[11] & 0xFF)).replace(' ', '0');
    byte c = utf8[11];
    for( int i = 0; i < 8; i++ ) {
      byte b = (byte) (c ^ (1 << i));
      utf8[11] = b;
      //s = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
      s = new String(utf8);
      System.out.println(s);
    }    
  }
}
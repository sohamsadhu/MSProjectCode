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
    // I want to have the ASCII representation of a string.
    String text = "Hello World!";
    byte[] utf8 = new byte[]{};
    try {
      utf8 = text.getBytes("utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    for( byte i : utf8 ) {
      System.out.print(i);
    }
  }
}
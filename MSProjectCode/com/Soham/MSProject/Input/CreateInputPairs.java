package com.Soham.MSProject.Input;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * This interface will create the input file containing pairs of text. The seed text will be given to the file.
 * And that will be changed for each one of the bits, step by step. That is bits beginning from 1 to 15
 * in the file will be changed, if asked for 15 different pairs to be made. Then pairs will be made, so that
 * would be <sup>15</sup>C<sub>2<sub> = 105 pairs of text, for my experimental data.
 * @author ssadhu
 * 
 */
public interface CreateInputPairs 
{
  /**
   * Make sure parameters are as per requirement.
   * @param seed only checked for null.
   * @param flip_end has to be either "Starting", "Middle" or "Trailing" value.
   * @param flips int value between 1 and 20, inclusive.
   * @param file_name just cannot be null.
   * @return An object array of 2 elements. First element a boolean saying pass or
   * fail. The second element a string describing the problems. In case of pass, the
   * second element is empty.
   */
  public Object[] checkInputFileOptions( final String seed, final String flip_end, 
      final int flips, final String file_name);
  
  /**
   * Returns the array of strings with bits toggled from end specified. For example if a string 
   * provided happens to be byte 10000000 and the flipend is trailing. Then at flips 2, the 
   * return array would be [10000000, 10000001, 10000010]. So the original seed is the head of 
   * the list, and the rest follow. If the bits to be modified provided are more than those present
   * then all the bits will be flipped.
   * @param seed The actual input string provided for bit flipping.
   * @param flipend determines the bits to be flipped from which end.
   * @param flips the number of bits that will be flipped. This should be between 1 and 20, inclusive.
   * @return Array of strings, with bits flipped.
   * @throws UnsupportedEncodingException in case byte to string conversion does not work out.
   */
  public String[] getFlippedSeeds( final byte[] seed, final String flipend, 
      final int flips ) throws UnsupportedEncodingException;
  
  /**
   * Makes all pairs possibles ignoring the order, for the strings provided. The second dimension in 
   * return value is 2 to make a pair. And the first dimension is <sup>input length</sup>C<sub>2</sub>.
   * @param seeds the list of the flipped bit strings, that you want to make pairs of.
   * @return a 2D array, with each entry having two entries of strings making pair.
   */
  public String[][] createInputPairs( final String[] seeds );
  
  /**
   * This is the method that will create the file, check the seeds, flips and make the file
   * by calling another function to write into the file.
   * @param seed the starting string that needs to be flipped and experimented with.
   * @param flipend, the end "Starting", "Middle", "Trailing" where you want the bits flipped.
   * @param flips, the number of flips or toggle you want to give to the seed.
   * @param file_name, the name of the input file.
   * @return an object array with first thing being boolean success or fail, and the second
   * thing being a string with the failure message.
   */
  public Object[] createFile( String seed, String flipend, Integer flips, String file_name );
  
  /**
   * Will write to the file object given, the pairs with new line separation, and a blank new
   * line between the pairs.
   * @param seed, the string that will spawn the flipped strings to write into the file.
   * @param flipend, the end "Starting", "Middle", "Trailing" where you want the bits flipped.
   * @param flips, the number of flips or toggle you want to give to the seed.
   * @param file, the actual file object in which you write the results.
   * @return a boolean success or failure.
   * @throws UnsupportedEncodingException
   */
  public boolean writeToFile( String seed, String flipend, Integer flips, 
      File file ) throws UnsupportedEncodingException;
  
  /**
   * Checks if the strings in the given array have a new line character. This is important since,
   * the experiment setup takes new line as separator, and extra new line can confuse it.
   * @param flipped_seeds array of strings, to be checked for new line.
   * @return boolean true if new line present in any one string, else false.
   */
  public boolean newLinePresent( final String[] flipped_seeds );
}
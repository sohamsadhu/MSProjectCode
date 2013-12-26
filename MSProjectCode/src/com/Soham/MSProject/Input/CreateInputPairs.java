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
  
  public void createInputPairs( final String[] seeds, final int combinations );
  
  public Object[] createFile( String seed, String flipend, Integer flips, String file_name );
  
  public void writeToFile( String seed, String flipend, Integer flips, 
      File file ) throws UnsupportedEncodingException;
}
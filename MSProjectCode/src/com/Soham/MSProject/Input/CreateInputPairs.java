package com.Soham.MSProject.Input;

import java.io.File;

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
  
  public String[] getFlippedSeeds( final String seed, final String flipend, final int flips );
  
  public void createInputPairsFile(String seed, int combinations, String output_file);
  
  public Object[] createFile( String seed, String flipend, Integer flips, String file_name );
  
  public void writeToFile( String seed, String flipend, Integer flips, File file );
}
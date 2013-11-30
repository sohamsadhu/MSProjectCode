package com.Soham.MSProject.Input;
/**
 * This interface will create the input file containing pairs of text. The seed text will be given to the file.
 * And that will be changed for each one of the bits, step by step. That is bits beginning from 1 to 15
 * in the file will be changed, if asked for 15 different pairs to be made. Then pairs will be made, so that
 * would be <sup>15</sup>C<sub>2<sub> = 105 pairs of text, for my experimental data.
 * @author ssadhu
 * 
 */
public interface CreateInputPairs {
  void createInputPairsFile(String seed, int combinations, String output_file);
}
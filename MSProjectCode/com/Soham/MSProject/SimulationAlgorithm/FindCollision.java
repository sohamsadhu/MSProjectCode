package com.Soham.MSProject.SimulationAlgorithm;

import com.Soham.MSProject.SHA3.Hash;

public interface FindCollision
{
  /**
   * Iterates the search algorithm 128 times with different chaining values.
   * @param sha3
   * @param msg1
   * @param msg2
   * @param cv
   * @param rounds
   * @param digest_length
   * @return an array of long of 4 elements, with first two saying number of success and 
   * iterations for the same, and next two doing same for the failures. 
   */
  public long[] startIterativeCollision( Hash sha3, String msg1, String msg2, String cv, 
      String rounds, String digest_length );
  
  /**
   * Will return value of Hamming weight desired that is 65% of bits match, that would
   * be considered as collsion taken place.
   * @param digest_length
   * @return
   */
  public int getEpsilon( int digest_length );
  
  /**
   * Returns Hamming weight or the number of bits that are 1 in the given byte array. 
   * @param data
   * @return integer that is number of bits that are 1 in the byte array data given.
   */
  public int getHW( byte[] data );
  
  /**
   * Return byte array representation of the hexadecimal characters.
   * @param s string of hex characters
   * @return a byte array of the hex string given.
   */
  public byte[] hexStringToByteArray(String s);
  
  /**
   * Generates random hexadecimal character string, for the chaining value.
   * Source: http://stackoverflow.com/a/157202 by http://stackoverflow.com/users/21152/maxp
   * @param cv_length the length of chaining value.
   * @return
   */
  public String getChainValue( String cv_length );
  
  /**
   * Returns the Hamming weight of the two hash digests obtained from the two messages.
   * @param sha3   the object of the hash function that we are evaluating.
   * @param msg1
   * @param msg2   
   * @param cv     the chaining value
   * @param rounds the number of rounds the SHA-3 function will work.
   * @param digest_length the hash digest length that we are interested in.
   * @return
   */
  public int getEvaluation( Hash sha3, String msg1, String msg2, String cv, String rounds, 
      String digest_length );
  
  /**
   * Returns 2 bit neighbours of the the given chaining value.
   * @param cv chaining value whose neighbours are required.
   * @return 2D byte array that has the neighbours of that chaining value.
   */
  public byte[][] getNeighbours( byte[] cv );
}
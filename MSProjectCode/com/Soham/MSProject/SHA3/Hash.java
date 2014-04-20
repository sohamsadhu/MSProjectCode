package com.Soham.MSProject.SHA3;

/**
 * An interface for all the hashing algorithms.
 * @author Soham
 */
public interface Hash
{
  public byte[] hash( String msg, int digest_length, int num_of_rounds);
}
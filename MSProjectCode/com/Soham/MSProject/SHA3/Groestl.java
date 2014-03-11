package com.Soham.MSProject.SHA3;

// Input is a Hex String needs to be converted to bytes.
// Output is a byte array. Size of the byte array 224, 256, 384, 512.
// Message is padded and split into blocks of size l, and m blocks.
// hi <- f( h i-1, mi)
// Two l bits of input to one l bit of output. For 256 bits the message
// block is 512, and beyond that the internal state is of 1024 bits.
// Once you have consumed all the message blocks, you will go to the Omega function
// to do the truncation. Finished the hashing.
public class Groestl
{
  // The box function, input of two 512 bit blocks or two 1024 blocks.
  // Sends back a 512 or 1024 block back.
  // f(h, m) = P(h xor m) xor Q(m) xor h
  
  // Omega function will return the last n bits.
  // Omega(x) = truncate(P(x) xor x)
  
  // function P
  
  // function Q
  public void something()
  {}
}
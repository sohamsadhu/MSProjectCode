//
package com.Soham.MSProject.scratch;


public class Scratch 
{
  public static void main( String [] args )
  {
    int[] startArray = {11, 21, 31, 41, 51};
    int[] finishArray = {61, 71, 81, 91, 101, 111, 121, 131};
    System.arraycopy(startArray, 0, finishArray, 0, startArray.length); 
    for( int a : finishArray ) {
      System.out.println("some "+ a);
    }
  }
}
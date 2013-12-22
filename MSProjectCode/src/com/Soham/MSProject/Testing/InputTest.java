package com.Soham.MSProject.Testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import com.Soham.MSProject.Input.CreateInputPairHelper;
import com.Soham.MSProject.Input.CreateInputPairs;
import com.Soham.MSProject.Input.CreateInputPairsImpl;

import org.junit.BeforeClass;
import org.junit.Test;

public class InputTest 
{
  private static CreateInputPairHelper cih;
  private static CreateInputPairs cip;
  
  @BeforeClass
  public static void initialise()
  {
    cih = new CreateInputPairHelper();
    cip = new CreateInputPairsImpl();
  }
  
  @Test
  //Edge cases for empty string
  public void testcreateListOfFlippedBytesEmptyStringCase() 
  {    
    String[] result = cih.createListOfFlippedBytes( null, 10 );
    assertArrayEquals( result, null );    
    result = cih.createListOfFlippedBytes("", -1);
    assertArrayEquals( result, null );
    result = cih.createListOfFlippedBytes("", 0);
    assertArrayEquals( result, new String[]{""} );
    result = cih.createListOfFlippedBytes("", 1);
    assertEquals(2, result.length);
    assertEquals(true, "".equals(result[0]));
    result = cih.createListOfFlippedBytes("", 2);
    assertEquals(3, result.length);
    assertEquals(true, " ".equals(result[1]));
    result = cih.listFromEmptyString( 20 );
    assertArrayEquals( result, new String[]{"", " ", "!", "\"", "#", "$", "%", "&", "'", 
        "(", ")", "*", "+", ",", "-", ".", "/", "0", "1", "2", "3"} );
    result = cih.createListOfFlippedBytes( "A", 21 );
    assertArrayEquals( result, null );    
  }
  
  @Test
  public void testlistFromEmptyString()
  {
    String[] result = cih.listFromEmptyString( 21 );
    assertArrayEquals( result, null );
    result = cih.listFromEmptyString( -1 );
    assertArrayEquals( result, null );
    result = cih.listFromEmptyString( 0 );
    assertArrayEquals( result, new String[]{""} );
    result = cih.listFromEmptyString( 1 );
    assertArrayEquals( result, new String[]{"", " "} );
    result = cih.listFromEmptyString( 3 );
    assertArrayEquals( result, new String[]{"", " ", "!", "\""} );
    result = cih.listFromEmptyString( 20 );
    assertArrayEquals( result, new String[]{"", " ", "!", "\"", "#", "$", "%", "&", "'", 
        "(", ")", "*", "+", ",", "-", ".", "/", "0", "1", "2", "3"} );
  }

  @Test
  public void testcheckInputFileOptions()
  {
    Object[] results = cip.checkInputFileOptions("seed", "Starting", 20, "output_file" );
    assertEquals( results.length, 2 );
    assertEquals( "", results[1] );
    assertEquals( true, results[0] );
    results = cip.checkInputFileOptions("seed", "something else", 1, "output_file" );
    assertEquals( "Please enter which end you want the bits to flip. "
        + "Starting, Middle or Trailing. \n", results[1] );
    assertEquals( false, results[0] );
    results = cip.checkInputFileOptions("seed", "something else", 0, "output_file" );
    assertEquals( "Please enter which end you want the bits to flip. Starting, Middle or Trailing. \n" 
        + "The number of flips has to be in between 1 and 20, inclusive. \n", results[1] );
    assertEquals( false, results[0] );
    results = cip.checkInputFileOptions("seed", "something else", 21, "" );
    assertEquals( "Please enter which end you want the bits to flip. Starting, Middle or Trailing. \n" 
        + "The number of flips has to be in between 1 and 20, inclusive. \n"
        + "The file name to be created cannot be empty.", results[1] );
    assertEquals( false, results[0] );
  }
}
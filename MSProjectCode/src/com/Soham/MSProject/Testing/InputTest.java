package com.Soham.MSProject.Testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import com.Soham.MSProject.Input.CreateInputPairHelper;

import org.junit.Before;
import org.junit.Test;

public class InputTest 
{
  private CreateInputPairHelper cih;
  
  @Before
  public void initialise()
  {
    cih = new CreateInputPairHelper();
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
}

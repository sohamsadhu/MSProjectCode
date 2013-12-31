package com.Soham.MSProject.Testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.io.UnsupportedEncodingException;

import com.Soham.MSProject.Input.CreateInputPairs;
import com.Soham.MSProject.Input.CreateInputPairsImpl;

import org.junit.BeforeClass;
import org.junit.Test;

public class InputTest 
{
  private static CreateInputPairs cip;
  
  @BeforeClass
  public static void initialise()
  {
    cip = new CreateInputPairsImpl();
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
  
  @Test
  public void testcreateInputPairs()
  {
    String[] input = new String[]{ "a", "b", "c" }; // Normal function
    String[][] output = cip.createInputPairs( input );
    assertEquals( 3, output.length );
    assertArrayEquals( output[0], new String[] {"a", "b"} );
    assertArrayEquals( output[1], new String[] {"a", "c"} );
    assertArrayEquals( output[2], new String[] {"b", "c"} );
    input = new String[] {"a", "b"};  // Edge case just two inputs.
    output = cip.createInputPairs( input );
    assertEquals( 1, output.length );
    assertArrayEquals( output[0], new String[] {"a", "b"} );
    input = new String[]{ "a", "b", "c", "d", "e" }; // Scale and then check.
    output = cip.createInputPairs( input );
    assertEquals( 10, output.length );
    assertArrayEquals( output[0], new String[]{"a", "b"} );
    assertArrayEquals( output[1], new String[]{"a", "c"} );
    assertArrayEquals( output[2], new String[]{"a", "d"} );
    assertArrayEquals( output[3], new String[]{"a", "e"} );
    assertArrayEquals( output[4], new String[]{"b", "c"} );
    assertArrayEquals( output[5], new String[]{"b", "d"} );
    assertArrayEquals( output[6], new String[]{"b", "e"} );
    assertArrayEquals( output[7], new String[]{"c", "d"} );
    assertArrayEquals( output[8], new String[]{"c", "e"} );
    assertArrayEquals( output[9], new String[]{"d", "e"} );
  }
  
  @Test
  public void testnewLinePresent()
  {
    assertEquals( false, cip.newLinePresent( new String[]{"a", "b"}));
    assertEquals( true, cip.newLinePresent( new String[]{"\n"}));
    assertEquals( false, cip.newLinePresent( new String[]{"blah", "foo"}));
    assertEquals( true, cip.newLinePresent( new String[]{"blah", "fo\no"}));
    assertEquals( true, cip.newLinePresent( new String[]{"\nblah", "foo"}));
    assertEquals( true, cip.newLinePresent( new String[]{"blah \n", "foo"}));
    assertEquals( true, cip.newLinePresent( new String[]{"blah\n", "foo"}));
  }
  
  @Test
  public void flipSeedStarting()
  {
    try 
    {
      byte[] b = new byte[]{0x20};
      String[] results = cip.getFlippedSeeds( b, "Starting", 0 );
      assertArrayEquals( results, new String[]{" "} ); // No flips.
      b = new byte[]{0x41};
      results = cip.getFlippedSeeds( b, "Starting", 1 );
      assertArrayEquals( results, new String[]{new String(new byte[] {0x41}, "UTF-8"), 
          new String(new byte[] {( byte )0xC1}, "UTF-8")} );
      results = cip.getFlippedSeeds( b, "Something else", 1 ); //Default check.
      assertArrayEquals( results, new String[]{new String(new byte[] {0x41}, "UTF-8"), 
          new String(new byte[] {( byte )0xC1}, "UTF-8")} );
      results = cip.getFlippedSeeds( b, "Starting", 3 );
      assertArrayEquals( results, new String[]{new String(new byte[] {0x41}, "UTF-8"), 
          new String(new byte[] {( byte )0xC1}, "UTF-8"), new String(new byte[] {0x01}, "UTF-8"),
          new String(new byte[] {0x61}, "UTF-8")} );
      results = cip.getFlippedSeeds( b, "Starting", 7 );
      assertArrayEquals( results, new String[]{new String(new byte[] {0x41}, "UTF-8"), 
          new String(new byte[] {( byte )0xC1}, "UTF-8"), new String(new byte[] {0x01}, "UTF-8"),
          new String(new byte[] {0x61}, "UTF-8"), new String(new byte[] {0x51}, "UTF-8"),
          new String(new byte[] {0x49}, "UTF-8"), new String(new byte[] {0x45}, "UTF-8"),
          new String(new byte[] {0x43}, "UTF-8")} );
      results = cip.getFlippedSeeds( b, "Starting", 8 );
      assertArrayEquals( results, new String[]{new String(new byte[] {0x41}, "UTF-8"), 
          new String(new byte[] {( byte )0xC1}, "UTF-8"), new String(new byte[] {0x01}, "UTF-8"),
          new String(new byte[] {0x61}, "UTF-8"), new String(new byte[] {0x51}, "UTF-8"),
          new String(new byte[] {0x49}, "UTF-8"), new String(new byte[] {0x45}, "UTF-8"),
          new String(new byte[] {0x43}, "UTF-8"), new String(new byte[] {0x40}, "UTF-8")} );
      b = new byte[]{0x41, 0x42};
      results = cip.getFlippedSeeds( b, "Starting", 7 );
      assertArrayEquals( results, new String[]{new String(new byte[] {0x41, 0x42}, "UTF-8"), 
          new String(new byte[] {( byte )0xC1, 0x42}, "UTF-8"), new String(new byte[] {0x01, 0x42}, "UTF-8"),
          new String(new byte[] {0x61, 0x42}, "UTF-8"), new String(new byte[] {0x51, 0x42}, "UTF-8"),
          new String(new byte[] {0x49, 0x42}, "UTF-8"), new String(new byte[] {0x45, 0x42}, "UTF-8"),
          new String(new byte[] {0x43, 0x42}, "UTF-8")} );
      results = cip.getFlippedSeeds( b, "Starting", 12 );
      assertArrayEquals( results, new String[]{new String(new byte[] {0x41, 0x42}, "UTF-8"), 
          new String(new byte[] {( byte )0xC1, 0x42}, "UTF-8"), new String(new byte[] {0x01, 0x42}, "UTF-8"),
          new String(new byte[] {0x61, 0x42}, "UTF-8"), new String(new byte[] {0x51, 0x42}, "UTF-8"),
          new String(new byte[] {0x49, 0x42}, "UTF-8"), new String(new byte[] {0x45, 0x42}, "UTF-8"),
          new String(new byte[] {0x43, 0x42}, "UTF-8"), new String(new byte[] {0x40, 0x42}, "UTF-8"),
          new String(new byte[] {0x41, ( byte )0xC2}, "UTF-8"), new String(new byte[] {0x41, 0x02}, "UTF-8"),
          new String(new byte[] {0x41, 0x62}, "UTF-8"), new String(new byte[] {0x41, 0x52}, "UTF-8")} );
      results = cip.getFlippedSeeds( b, "Starting", 18 );
      assertArrayEquals( results, new String[]{new String(new byte[] {0x41, 0x42}, "UTF-8"), 
          new String(new byte[] {( byte )0xC1, 0x42}, "UTF-8"), new String(new byte[] {0x01, 0x42}, "UTF-8"),
          new String(new byte[] {0x61, 0x42}, "UTF-8"), new String(new byte[] {0x51, 0x42}, "UTF-8"),
          new String(new byte[] {0x49, 0x42}, "UTF-8"), new String(new byte[] {0x45, 0x42}, "UTF-8"),
          new String(new byte[] {0x43, 0x42}, "UTF-8"), new String(new byte[] {0x40, 0x42}, "UTF-8"),
          new String(new byte[] {0x41, ( byte )0xC2}, "UTF-8"), new String(new byte[] {0x41, 0x02}, "UTF-8"),
          new String(new byte[] {0x41, 0x62}, "UTF-8"), new String(new byte[] {0x41, 0x52}, "UTF-8"),
          new String(new byte[] {0x41, 0x4A}, "UTF-8"), new String(new byte[] {0x41, 0x46}, "UTF-8"),
          new String(new byte[] {0x41, 0x40}, "UTF-8"), new String(new byte[] {0x41, 0x43}, "UTF-8")} );
    } 
    catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void flipSeedMiddle()
  {
    try 
    {
      byte[] b = new byte[]{0x20};
      String[] results = cip.getFlippedSeeds( b, "Middle", 0 );
      assertArrayEquals( results, new String[]{" "} ); // No flips.
      b = new byte[]{0x41};
      results = cip.getFlippedSeeds( b, "Middle", 2 );
      assertArrayEquals( results, new String[]{ new String(new byte[] {0x41}, "UTF-8"), 
          new String(new byte[] {0x51}, "UTF-8"), new String(new byte[] {0x49}, "UTF-8") } );
      results = cip.getFlippedSeeds( b, "Middle", 6 );
      assertArrayEquals( results, new String[]{ new String(new byte[] {0x41}, "UTF-8"), 
          new String(new byte[] {0x01}, "UTF-8"), new String(new byte[] {0x61}, "UTF-8"),
          new String(new byte[] {0x51}, "UTF-8"), new String(new byte[] {0x49}, "UTF-8"),
          new String(new byte[] {0x45}, "UTF-8"), new String(new byte[] {0x43}, "UTF-8") } );
      b = new byte[]{0x41, 0x42};
      results = cip.getFlippedSeeds( b, "Middle", 6 );
      assertArrayEquals( results, new String[]{ new String(new byte[] {0x41, 0x42}, "UTF-8"), 
          new String(new byte[] {0x45, 0x42}, "UTF-8"), new String(new byte[] {0x43, 0x42}, "UTF-8"),
          new String(new byte[] {0x40, 0x42}, "UTF-8"), new String(new byte[] {0x41, ( byte )0xC2}, "UTF-8"),
          new String(new byte[] {0x41, 0x02}, "UTF-8"), new String(new byte[] {0x41, 0x62}, "UTF-8") } );
      results = cip.getFlippedSeeds( b, "Middle", 12 );
      assertArrayEquals( results, new String[]{ new String(new byte[] {0x41, 0x42}, "UTF-8"), 
          new String(new byte[] {0x61, 0x42}, "UTF-8"), new String(new byte[] {0x51, 0x42}, "UTF-8"),
          new String(new byte[] {0x49, 0x42}, "UTF-8"), new String(new byte[] {0x45, 0x42}, "UTF-8"),
          new String(new byte[] {0x43, 0x42}, "UTF-8"), new String(new byte[] {0x40, 0x42}, "UTF-8"),
          new String(new byte[] {0x41, ( byte )0xC2}, "UTF-8"), new String(new byte[] {0x41, 0x02}, "UTF-8"),
          new String(new byte[] {0x41, 0x62}, "UTF-8"), new String(new byte[] {0x41, 0x52}, "UTF-8"),
          new String(new byte[] {0x41, ( byte )0x4A}, "UTF-8"), new String(new byte[] {0x41, 0x46}, "UTF-8") } );
      results = cip.getFlippedSeeds( b, "Middle", 19 );
      assertArrayEquals( results, new String[]{ new String(new byte[] {0x41, 0x42}, "UTF-8"),
          new String(new byte[] {( byte )0xC1, 0x42}, "UTF-8"), new String(new byte[] {0x01, 0x42}, "UTF-8"),
          new String(new byte[] {0x61, 0x42}, "UTF-8"), new String(new byte[] {0x51, 0x42}, "UTF-8"),
          new String(new byte[] {0x49, 0x42}, "UTF-8"), new String(new byte[] {0x45, 0x42}, "UTF-8"),
          new String(new byte[] {0x43, 0x42}, "UTF-8"), new String(new byte[] {0x40, 0x42}, "UTF-8"),
          new String(new byte[] {0x41, ( byte )0xC2}, "UTF-8"), new String(new byte[] {0x41, 0x02}, "UTF-8"),
          new String(new byte[] {0x41, 0x62}, "UTF-8"), new String(new byte[] {0x41, 0x52}, "UTF-8"),
          new String(new byte[] {0x41, ( byte )0x4A}, "UTF-8"), new String(new byte[] {0x41, 0x46}, "UTF-8"),
          new String(new byte[] {0x41, 0x40}, "UTF-8"), new String(new byte[] {0x41, 0x43}, "UTF-8") } );
    } 
    catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testflipSeedTrailing()
  {
    try 
    {
      byte[] b = new byte[]{0x20};
      String[] results = cip.getFlippedSeeds( b, "Trailing", 0 );
      assertArrayEquals( results, new String[]{" "} ); // No flips.
      b = new byte[]{0x41};
      results = cip.getFlippedSeeds( b, "Trailing", 1 );
      assertArrayEquals( results, new String[]{new String(new byte[] {0x41}, "UTF-8"), 
          new String(new byte[] {0x40}, "UTF-8")} );
      results = cip.getFlippedSeeds( b, "Trailing", 3 );
      assertArrayEquals( results, new String[]{new String(new byte[] {0x41}, "UTF-8"), 
          new String(new byte[] {0x40}, "UTF-8"), new String(new byte[] {0x43}, "UTF-8"),
          new String(new byte[] {0x45}, "UTF-8")} );
      results = cip.getFlippedSeeds( b, "Trailing", 7 );
      assertArrayEquals( results, new String[]{new String(new byte[] {0x41}, "UTF-8"), 
          new String(new byte[] {0x40}, "UTF-8"), new String(new byte[] {0x43}, "UTF-8"),
          new String(new byte[] {0x45}, "UTF-8"), new String(new byte[] {0x49}, "UTF-8"),
          new String(new byte[] {0x51}, "UTF-8"), new String(new byte[] {0x61}, "UTF-8"),
          new String(new byte[] {0x01}, "UTF-8")} );
      results = cip.getFlippedSeeds( b, "Trailing", 8 );
      assertArrayEquals( results, new String[]{new String(new byte[] {0x41}, "UTF-8"), 
          new String(new byte[] {0x40}, "UTF-8"), new String(new byte[] {0x43}, "UTF-8"),
          new String(new byte[] {0x45}, "UTF-8"), new String(new byte[] {0x49}, "UTF-8"),
          new String(new byte[] {0x51}, "UTF-8"), new String(new byte[] {0x61}, "UTF-8"),
          new String(new byte[] {0x01}, "UTF-8"), new String(new byte[] {(byte) 0xC1}, "UTF-8")} );
      results = cip.getFlippedSeeds( b, "Trailing", 14 );
      assertArrayEquals( results, new String[]{new String(new byte[] {0x41}, "UTF-8"), 
          new String(new byte[] {0x40}, "UTF-8"), new String(new byte[] {0x43}, "UTF-8"),
          new String(new byte[] {0x45}, "UTF-8"), new String(new byte[] {0x49}, "UTF-8"),
          new String(new byte[] {0x51}, "UTF-8"), new String(new byte[] {0x61}, "UTF-8"),
          new String(new byte[] {0x01}, "UTF-8"), new String(new byte[] {(byte) 0xC1}, "UTF-8")} );
      b = new byte[]{0x42, 0x41};
      results = cip.getFlippedSeeds( b, "Trailing", 12 );
      assertArrayEquals( results, new String[]{new String(new byte[] {0x42, 0x41}, "UTF-8"), 
        new String(new byte[] {0x42, 0x40}, "UTF-8"), new String(new byte[] {0x42, 0x43}, "UTF-8"),
        new String(new byte[] {0x42, 0x45}, "UTF-8"), new String(new byte[] {0x42, 0x49}, "UTF-8"),
        new String(new byte[] {0x42, 0x51}, "UTF-8"), new String(new byte[] {0x42, 0x61}, "UTF-8"),
        new String(new byte[] {0x42, 0x01}, "UTF-8"), new String(new byte[] {0x42, (byte) 0xC1}, "UTF-8"),
        new String(new byte[] {0x43, 0x41}, "UTF-8"), new String(new byte[] {0x40, 0x41}, "UTF-8"),
        new String(new byte[] {0x46, 0x41}, "UTF-8"), new String(new byte[] {0x4A, 0x41}, "UTF-8")});
      results = cip.getFlippedSeeds( b, "Trailing", 18 );
      assertArrayEquals( results, new String[]{new String(new byte[] {0x42, 0x41}, "UTF-8"), 
        new String(new byte[] {0x42, 0x40}, "UTF-8"), new String(new byte[] {0x42, 0x43}, "UTF-8"),
        new String(new byte[] {0x42, 0x45}, "UTF-8"), new String(new byte[] {0x42, 0x49}, "UTF-8"),
        new String(new byte[] {0x42, 0x51}, "UTF-8"), new String(new byte[] {0x42, 0x61}, "UTF-8"),
        new String(new byte[] {0x42, 0x01}, "UTF-8"), new String(new byte[] {0x42, (byte) 0xC1}, "UTF-8"),
        new String(new byte[] {0x43, 0x41}, "UTF-8"), new String(new byte[] {0x40, 0x41}, "UTF-8"),
        new String(new byte[] {0x46, 0x41}, "UTF-8"), new String(new byte[] {0x4A, 0x41}, "UTF-8"),
        new String(new byte[] {0x52, 0x41}, "UTF-8"), new String(new byte[] {0x62, 0x41}, "UTF-8"),
        new String(new byte[] {0x02, 0x41}, "UTF-8"), new String(new byte[] {(byte) 0xC2, 0x41}, "UTF-8")});
    } 
    catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
}
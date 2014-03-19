package com.Soham.MSProject.Testing;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.Soham.MSProject.SHA3.Groestl;

public class SHA3Test 
{
  private static Groestl groestl;
  
  @BeforeClass
  public static void initialise()
  {
    groestl = new Groestl();
  }
  
  @Test
  public void testGroestl()
  {
    ArrayList<Byte> expected = groestl.convertHexStringToBytes
        ("ed7bb299331c99ee485d49c22d368f05d9158f2055b9605676786f43");
    byte [] expects = new byte[ expected.size() ];
    int i = 0;
    for( Byte b : expected )
    {
      expects[i] = b.byteValue();
      i++;
    }
    String s = new String("abc");
    byte[] str = s.getBytes();
    StringBuilder sb = new StringBuilder();
    for( byte some : str ) {
      sb.append(String.format("%02x", some));
    }
    assertArrayEquals( expects, groestl.hash(sb.toString(), 224, 0));
    
    expected = groestl.convertHexStringToBytes
        ("f3c1bb19c048801326a7efbcf16e3d7887446249829c379e1840d1a3a1e7d4d2");
    expects = new byte[ expected.size() ];
    i = 0;
    for( Byte b : expected )
    {
      expects[i] = b.byteValue();
      i++;
    }
    s = new String("abc");
    str = s.getBytes();
    sb = new StringBuilder();
    for( byte some : str ) {
      sb.append(String.format("%02x", some));
    }
    assertArrayEquals( expects, groestl.hash(sb.toString(), 256, 0));
    
    expected = groestl.convertHexStringToBytes
        ("32c39f82ab41ee4fdb1582f83dde41089d47b904988b1a9a647553cb1a50" 
            + "2cf07df7eb1e11dc3d66bec096a39a790336");
    expects = new byte[ expected.size() ];
    i = 0;
    for( Byte b : expected )
    {
      expects[i] = b.byteValue();
      i++;
    }
    s = new String("abc");
    str = s.getBytes();
    sb = new StringBuilder();
    for( byte some : str ) {
      sb.append(String.format("%02x", some));
    }
    assertArrayEquals( expects, groestl.hash(sb.toString(), 384, 0));
    
    expected = groestl.convertHexStringToBytes
        ("70e1c68c60df3b655339d67dc291cc3f1dde4ef343f11b23fdd44957693815a75a" 
            + "8339c682fc28322513fd1f283c18e53cff2b264e06bf83a2f0ac8c1f6fbff6");
    expects = new byte[ expected.size() ];
    i = 0;
    for( Byte b : expected )
    {
      expects[i] = b.byteValue();
      i++;
    }
    s = new String("abc");
    str = s.getBytes();
    sb = new StringBuilder();
    for( byte some : str ) {
      sb.append(String.format("%02x", some));
    }
    assertArrayEquals( expects, groestl.hash(sb.toString(), 512, 0));
    
    expected = groestl.convertHexStringToBytes
        ("b7b310994ad64eb635141fce7a8494703da7db05099a89fdd004c940");
    expects = new byte[ expected.size() ];
    i = 0;
    for( Byte b : expected )
    {
      expects[i] = b.byteValue();
      i++;
    }
    s = new String("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq");
    str = s.getBytes();
    sb = new StringBuilder();
    for( byte some : str ) {
      sb.append(String.format("%02x", some));
    }
    assertArrayEquals( expects, groestl.hash(sb.toString(), 224, 0));
    
    expected = groestl.convertHexStringToBytes
        ("22c23b160e561f80924d44f2cc5974cd5a1d36f69324211861e63b9b6cb7974c");
    expects = new byte[ expected.size() ];
    i = 0;
    for( Byte b : expected )
    {
      expects[i] = b.byteValue();
      i++;
    }
    s = new String("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq");
    str = s.getBytes();
    sb = new StringBuilder();
    for( byte some : str ) {
      sb.append(String.format("%02x", some));
    }
    assertArrayEquals( expects, groestl.hash(sb.toString(), 256, 0));
    
    expected = groestl.convertHexStringToBytes
        ("33625fdddcc2809a83b912d70910d3b5e1408ef017c949617"
            + "c5543bb835939f13484e60bfe6ff27acf225c7a4b596504");
    expects = new byte[ expected.size() ];
    i = 0;
    for( Byte b : expected )
    {
      expects[i] = b.byteValue();
      i++;
    }
    s = new String("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopqopqrpqrsqrstr" 
        + "stustuvtuvwuvwxvwxywxyzxyzayzabzabcabcdbcdecdefdefg");
    str = s.getBytes();
    sb = new StringBuilder();
    for( byte some : str ) {
      sb.append(String.format("%02x", some));
    }
    assertArrayEquals( expects, groestl.hash(sb.toString(), 384, 0));
    
    expected = groestl.convertHexStringToBytes
        ("9f0867f941b5f3f2520e7b60b6e615eca82b61e2c5dd810f562450466f6a80fd72e6391f829" 
            + "dea656c4f84cdd7615e2098a99336d330b7226299e4139d3def75");
    expects = new byte[ expected.size() ];
    i = 0;
    for( Byte b : expected )
    {
      expects[i] = b.byteValue();
      i++;
    }
    s = new String("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopqopqrpqrsqrstrstu" 
        + "stuvtuvwuvwxvwxywxyzxyzayzabzabcabcdbcdecdefdefg");
    str = s.getBytes();
    sb = new StringBuilder();
    for( byte some : str ) {
      sb.append(String.format("%02x", some));
    }
    assertArrayEquals( expects, groestl.hash(sb.toString(), 512, 0));
  }
}
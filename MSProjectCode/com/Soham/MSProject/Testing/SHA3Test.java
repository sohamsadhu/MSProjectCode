package com.Soham.MSProject.Testing;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.Soham.MSProject.SHA3.Groestl;
import com.Soham.MSProject.SHA3.Keccak;

public class SHA3Test 
{
  private static Groestl groestl;
  private static Keccak keccak;
  
  @BeforeClass
  public static void initialise()
  {
    groestl = new Groestl();
    keccak = new Keccak();
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
  
  @Test
  public void test224Keccak()
  {
    byte[] expects = keccak.convertHexStringToBytes
        ("038907E89C919CD8F90A7FBC5A88FF9278108DAEF3EBCDA0CEB383E1");
    assertArrayEquals( expects, keccak.hash("00112233445566778899AABBCCDDEEFF", 224, 0));
    expects = keccak.convertHexStringToBytes
        ("E405869DA1464A705700A3CBCE131AABEEBA9C8D2FE6576B21BCBE16");
    assertArrayEquals( expects, keccak.hash("C1ECFDFC", 224, 0));
    expects = keccak.convertHexStringToBytes
        ("A9CAB59EB40A10B246290F2D6086E32E3689FAF1D26B470C899F2802");
    assertArrayEquals( expects, keccak.hash("CC", 224, 0));
    expects = keccak.convertHexStringToBytes
        ("7A5C2CB3F999DD00EFF7399963314CA647DD0E5AE1BDDEC611F8338D");
    assertArrayEquals( expects, keccak.hash("4A4F202484512526", 224, 0));
    expects = keccak.convertHexStringToBytes
        ("455584A1A3BBFBB977AE08DDEE93DA5ACAE0F2F4C3CDAAF089728AAE");
    assertArrayEquals( expects, keccak.hash("82E192E4043DDCD12ECF52969D0F807EED", 224, 0));
    expects = keccak.convertHexStringToBytes
        ("E7B181DAEC132D3B6C9DFBF61841135B87FB995BE20957B8CD095E2B");
    assertArrayEquals( expects, keccak.hash("06E4EFE45035E61FAAF4287B4D8D1F12CA97E5", 224, 0));
    expects = keccak.convertHexStringToBytes
        ("8214A2B0E8BB60CD3E4DFB0D0855D0F6C4BA6D2728D0687BDF75F79E");
    assertArrayEquals( expects, keccak.hash("94F7CA8E1A54234C6D53CC734BB3D3150C8BA8C5F880EAB8D25FE"
        + "D13793A9701EBE320509286FD8E422E931D99C98DA4DF7E70AE447BAB8CFFD92382D8A77760A259FC4FBD72"
        , 224, 0));
  }
  
  @Test
  public void test256Keccak()
  {
    byte[] expects = keccak.convertHexStringToBytes
        ("EEAD6DBFC7340A56CAEDC044696A168870549A6A7F6F56961E84A54BD9970B8A");
    assertArrayEquals( expects, keccak.hash("CC", 256, 0));
    expects = keccak.convertHexStringToBytes
        ("574271CD13959E8DDEAE5BFBDB02A3FDF54F2BABFD0CBEB893082A974957D0C1");
    assertArrayEquals( expects, keccak.hash("E926AE8B0AF6E53176DBFFCC2A6B88C6BD765F939D3D178A9BDE"
        + "9EF3AA131C61E31C1E42CDFAF4B4DCDE579A37E150EFBEF5555B4C1CB40439D835A724E2FAE7", 256, 0));
    expects = keccak.convertHexStringToBytes
        ("BD6F5492582A7C1B116304DE28314DF9FFFE95B0DA11AF52FE9440A717A34859");
    assertArrayEquals( expects, keccak.hash("B771D5CEF5D1A41A93D15643D7181D2A2EF0A8E84D91812F20ED"
        + "21F147BEF732BF3A60EF4067C3734B85BC8CD471780F10DC9E8291B58339A677B960218F71E793F2797AEA"
        + "349406512829065D37BB55EA796FA4F56FD8896B49B2CD19B43215AD967C712B24E5032D065232E02C1274"
        + "09D2ED4146B9D75D763D52DB98D949D3B0FED6A8052FBB", 256, 0));
    expects = keccak.convertHexStringToBytes
        ("E717A7769448ABBE5FEF8187954A88AC56DED1D22E63940AB80D029585A21921");
    assertArrayEquals( expects, keccak.hash("B32D95B0B9AAD2A8816DE6D06D1F86008505BD8C14124F6E9A16"
        + "3B5A2ADE55F835D0EC3880EF50700D3B25E42CC0AF050CCD1BE5E555B23087E04D7BF9813622780C7313A19"
        + "54F8740B6EE2D3F71F768DD417F520482BD3A08D4F222B4EE9DBD015447B33507DD50F3AB4247C5DE9A8A"
        + "BD62A8DECEA01E3B87C8B927F5B08BEB37674C6F8E380C04", 256, 0));
  }
  
  @Test
  public void test384Keccak()
  {
    byte[] expects = keccak.convertHexStringToBytes("1B84E62A46E5A201861754AF5DC95C4A1A69CAF4A7"
        + "96AE405680161E29572641F5FA1E8641D7958336EE7B11C58F73E9");
    assertArrayEquals( expects, keccak.hash("CC", 384, 0));
    expects = keccak.convertHexStringToBytes("14AA679B0C11F9C363F549330261B45E1E90CE31F4A1B0CE5C"
        + "B9EB81BD6079A3742D8602356C50985D0D3E540FDFDCFB");
    assertArrayEquals( expects, keccak.hash("E926AE8B0AF6E53176DBFFCC2A6B88C6BD765F939D3D178A9BDE"
        + "9EF3AA131C61E31C1E42CDFAF4B4DCDE579A37E150EFBEF5555B4C1CB40439D835A724E2FAE7", 384, 0));
    expects = keccak.convertHexStringToBytes("4B3087F800E4084D7F685737AC635DB459CF70C4FA863C711C1"
        + "143CC10F0C4AB0A2370C099FB282F9C1CE5F015BF3F79");
    assertArrayEquals( expects, keccak.hash("E90B4FFEF4D457BC7711FF4AA72231CA25AF6B2E206F8BF859D8"
        + "758B89A7CD36105DB2538D06DA83BAD5F663BA11A5F6F61F236FD5F8D53C5E89F183A3CEC615B50C7C681E7"
        + "73D109FF7491B5CC22296C5", 384, 0));
  }
  
  @Test
  public void test512Keccak()
  {
    byte[] expects = keccak.convertHexStringToBytes("8630C13CBD066EA74BBE7FE468FEC1DEE10EDC1254FB4"
        + "C1B7C5FD69B646E44160B8CE01D05A0908CA790DFB080F4B513BC3B6225ECE7A810371441A5AC666EB9");
    assertArrayEquals( expects, keccak.hash("CC", 512, 0));
    expects = keccak.convertHexStringToBytes("C0A4D8DCA967772DBF6E5508C913E7BEBA1B749A2B1AC963D067"
        + "6E6F1DCD4EBAA3F909EF87DD849882DC8253347A5F6520B5B9F510973F443976455F923CFCB9");
    assertArrayEquals( expects, keccak.hash("E926AE8B0AF6E53176DBFFCC2A6B88C6BD765F939D3D178A9BDE"
        + "9EF3AA131C61E31C1E42CDFAF4B4DCDE579A37E150EFBEF5555B4C1CB40439D835A724E2FAE7", 512, 0));
    expects = keccak.convertHexStringToBytes("81950E7096D31D4F22E3DB71CAC725BF59E81AF54C7CA9E6AEEE"
        + "71C010FC5467466312A01AA5C137CFB140646941556796F612C9351268737C7E9A2B9631D1FA");
    assertArrayEquals( expects, keccak.hash("3A3A819C48EFDE2AD914FBF00E18AB6BC4F14513AB27D0C178A1"
        + "88B61431E7F5623CB66B23346775D386B50E982C493ADBBFC54B9A3CD383382336A1A0B2150A15358F336D"
        + "03AE18F666C7573D55C4FD181C29E6CCFDE63EA35F0ADF5885CFC0A3D84A2B2E4DD24496DB789E663170CE"
        + "F74798AA1BBCD4574EA0BBA40489D764B2F83AADC66B148B4A0CD95246C127D5871C4F11418690A5DDF012"
        + "46A0C80A43C70088B6183639DCFDA4125BD113A8F49EE23ED306FAAC576C3FB0C1E256671D817FC2534A52F"
        + "5B439F72E424DE376F4C565CCA82307DD9EF76DA5B7C4EB7E085172E328807C02D011FFBF33785378D79DC"
        + "266F6A5BE6BB0E4A92ECEEBAEB1", 512, 0));    
  }
}
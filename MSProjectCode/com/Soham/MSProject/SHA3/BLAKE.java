package com.Soham.MSProject.SHA3;

import java.nio.ByteBuffer;

public class BLAKE 
{
  public static final int[] IV224 = { 0xC1059ED8, 0x367CD507, 0x3070DD17, 0xF70E5939, 0xFFC00B31,
    0x68581511, 0x64F98FA7, 0xBEFA4FA4 };
  
  public static final int[] IV256 = { 0x6A09E667, 0xBB67AE85, 0x3C6EF372, 0xA54FF53A, 0x510E527F,
    0x9B05688C, 0x1F83D9AB, 0x5BE0CD19 };
  
  public static final int[] CONST256 = { 0x243F6A88, 0x85A308D3, 0x13198A2E, 0x03707344, 
    0xA4093822, 0x299F31D0, 0x082EFA98, 0xEC4E6C89, 0x452821E6, 0x38D01377, 0xBE5466CF,
    0x34E90C6C, 0xC0AC29B7, 0xC97C50DD, 0x3F84D5B5, 0xB5470917 };
  
  public static final long[] IV384 = { 0xCBBB9D5DC1059ED8L, 0x629A292A367CD507L, 
    0x9159015A3070DD17L, 0x152FECD8F70E5939L, 0x67332667FFC00B31L, 0x8EB44A8768581511L,
    0xDB0C2E0D64F98FA7L, 0x47B5481DBEFA4FA4L };
  
  public static final long[] IV512 = { 0x6A09E667F3BCC908L, 0xBB67AE8584CAA73BL, 
    0x3C6EF372FE94F82BL, 0xA54FF53A5F1D36F1L, 0x510E527FADE682D1L, 0x9B05688C2B3E6C1FL,
    0x1F83D9ABFB41BD6BL, 0x5BE0CD19137E2179L };
  
  public static final long[] CONST512 = { 0x243F6A8885A308D3L, 0x13198A2E03707344L,
    0xA4093822299F31D0L, 0x082EFA98EC4E6C89L, 0x452821E638D01377L, 0xBE5466CF34E90C6CL,
    0xC0AC29B7C97C50DDL, 0x3F84D5B5B5470917L, 0x9216D5D98979FB1BL, 0xD1310BA698DFB5ACL,
    0x2FFD72DBD01ADFB7L, 0xB8E1AFED6A267E96L, 0xBA7C9045F12C7F99L, 0x24A19947B3916CF7L,
    0x0801F2E2858EFC16L, 0x636920D871574E69L };  

  public static final int[][] SIGMA = new int[][] {
    { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 },
    { 14, 10, 4, 8, 9, 15, 13, 6, 1, 12, 0, 2, 11, 7, 5, 3 },
    { 11, 8, 12, 0, 5, 2, 15, 13, 10, 14, 3, 6, 7, 1, 9, 4 },
    { 7, 9, 3, 1, 13, 12, 11, 14, 2, 6, 5, 10, 4, 0, 15, 8 },
    { 9, 0, 5, 7, 2, 4, 10, 15, 14, 1, 11, 12, 6, 8, 3, 13 },
    { 2, 12, 6, 10, 0, 11, 8, 3, 4, 13, 7, 5, 15, 14, 1, 9 },
    { 12, 5, 1, 15, 14, 13, 4, 10, 0, 7, 6, 3, 9, 2, 8, 11 },
    { 13, 11, 7, 14, 12, 1, 3, 9, 5, 0, 15, 4, 8, 6, 2, 10 },
    { 6, 15, 14, 9, 11, 3, 0, 8, 12, 2, 13, 7, 1, 4, 10, 5 },
    { 10, 2, 8, 4, 7, 6, 1, 5, 15, 11, 9, 14, 3, 12, 13, 0 }
  };
  
  /**
   * This method converts the string that is in hex format, to its equivalent byte format.
   * For example the string AB is converted to byte format 1010 1011.
   * @param msg in hex format representation of bytes
   * @return array list of bytes
   */
  //Source: http://stackoverflow.com/questions/13185073/convert-a-string-to-byte-array
  public byte[] convertHexStringToBytes( String msg )
  {
    int length = msg.length();
    byte[] message = new byte[msg.length() / 2];
    for( int i = 0; i < length; i += 2 )
    {
      message[i / 2] = (byte)((Character.digit(msg.charAt(i), 16) << 4) +
          Character.digit(msg.charAt(i + 1), 16));
    }
    return message;
  }
  
  public byte[] padHelper2( byte[] pad_msg, int msg_len, int digest_len, byte[] bit_len,
      byte[] msg )
  {
    for( int i = 0; i < msg_len; i++ ) {
      pad_msg[i] = msg[i];
    }
    pad_msg[msg_len] = ( byte )0x80;
    int zero_index = pad_msg.length - (((384 == digest_len) || (512 == digest_len)) ? 17 : 9);
    for( int i = msg_len + 1; i < zero_index; i++ ) {
      pad_msg[i] = ( byte )0x00;
    }
    pad_msg[zero_index] = ((512 == digest_len) || (256 == digest_len)) ? (byte)0x01 : (byte)0x00;
    int word_len = ((384 == digest_len) || (512 == digest_len)) ? 16 : 8;
    for( int i = 0; i < word_len; i++ ) {
      pad_msg[pad_msg.length - word_len + i] = bit_len[i];
    }
    return pad_msg;
  }
  
  public byte[] padHelper1( byte[] message, byte[] bit_len, int digest_length )
  {
    int pad_len = ((224 == digest_length) || (256 == digest_length)) ? 9 : 17;
    byte[] pad_msg = new byte[message.length + pad_len];
    for( int i = 0; i < message.length; i++ ) {
      pad_msg[i] = message[i];
    }
    pad_msg[message.length] = ((224 == digest_length) || (384 == digest_length)) ? 
        (byte)0x80 : (byte)0x81;
    for( int i = 0; i < (pad_len - 1); i++ ) {
      pad_msg[message.length + 1 + i] = bit_len[i];
    }
    return pad_msg;
  }
  
  public byte[] pad256( byte[] message, int digest_length )
  {
    if( !((224 == digest_length) || (256 == digest_length))) { return null; }
    long msg_bit_len = message.length * 8;
    ByteBuffer buf = ByteBuffer.allocate(8);
    buf.putLong( msg_bit_len );
    byte[] bit_len = buf.array(); // <l> length of message in bits, in 64 bit format.
    int bits_remaining = ( int )(msg_bit_len % 512); // For our applications message length is small.
    if( 440 == bits_remaining ) { // Only byte 0x81 needs to append and <l>64
      return padHelper1( message, bit_len, digest_length );
    }
    if( bits_remaining > 440 )
    {
      byte[] pad_msg = new byte[ message.length + 64 + (64 - (bits_remaining / 8)) ];
      return padHelper2( pad_msg, message.length, digest_length, bit_len, message );
    }
    else // bits_remaining < 440
    {
      byte[] pad_msg = new byte[message.length + ((512 - bits_remaining) / 8)];
      return padHelper2( pad_msg, message.length, digest_length, bit_len, message );
    }
  }
  
  public byte[] pad512( byte[] message, int digest_length )
  {
    if( !((384 == digest_length) || (512 == digest_length)) ){ return null; }
    long msg_bit_len = message.length * 8;   // For our purpose the message is not around 1 ^ 128.
    ByteBuffer buf = ByteBuffer.allocate(8);
    buf.putLong( msg_bit_len );
    byte[] msg_bit_len_arr = buf.array();
    byte[] bit_len = new byte[16];
    for( int i = 0; i < 16; i++ ) { bit_len[i] = 0x00; }
    for( int i = 7; i >= 0; i-- ) {
      bit_len[ 8 + i ] = msg_bit_len_arr[i];
    }
    int bits_remaining = (message.length * 8) % 1024;
    if( 888 == bits_remaining ) {
      return padHelper1( message, bit_len, digest_length );
    }
    if( bits_remaining < 888 )
    {
      byte[] pad_msg = new byte[message.length + ((1024 - bits_remaining) / 8)];
      return padHelper2( pad_msg, message.length, digest_length, bit_len, message );
    }
    else // bits_remaining > 888
    {
      byte[] pad_msg = new byte[message.length + 128 + ((1024 - bits_remaining) / 8)];
      return padHelper2( pad_msg, message.length, digest_length, bit_len, message );
    }
  }
  
  public int[][] getBlocks32Word( byte[] padded_msg )
  {
    int rows = padded_msg.length / 64;
    int [][] blocks = new int[rows][16];
    int k = 0;
    ByteBuffer wrapped;
    for( int i = 0; i < rows; i++ )
    {
      for( int j = 0; j < 16; j++ )
      {
        k = (i * 64) + (j * 4);
        byte[] arr = {padded_msg[k], padded_msg[k + 1], padded_msg[k + 2], padded_msg[k + 3]};
        wrapped = ByteBuffer.wrap( arr );
        blocks[i][j] = wrapped.getInt();
      }
    }
    return blocks;
  }
  
  public long[][] getBlocks64Word( byte[] padded_msg )
  {
    int rows = padded_msg.length / 128;
    long [][] blocks = new long[rows][16];
    int k = 0;
    ByteBuffer wrapped;
    for( int i = 0; i < rows; i++ )
    {
      for( int j = 0; j < 16; j++ )
      {
        k = (i * 128) + (j * 8);
        byte[] arr = {padded_msg[k], padded_msg[k + 1], padded_msg[k + 2], padded_msg[k + 3],
            padded_msg[k + 4], padded_msg[k + 5], padded_msg[k + 6], padded_msg[k + 7]};
        wrapped = ByteBuffer.wrap( arr );
        blocks[i][j] = wrapped.getLong();
      }
    }
    return blocks;
  }
  
  public void g32( int a, int b, int c, int d, int[] state, int index, int[] msg, int round )
  {
    int sig1 = SIGMA[round % 10][2 * index];
    int sig2 = SIGMA[round % 10][(2 * index) + 1];
    state[a] = state[a] + state[b] + (msg[sig1] ^ CONST256[sig2]); //a ← a + b + (mσr (2i) ⊕ cσr (2i+1) )
    state[d] = (state[d] ^ state[a]) >>> 16; //d ← (d ⊕ a) >>> 16
    state[c] = state[c] + state[d]; //c ← c+d
    state[b] = (state[b] + state[c]) >>> 12; //b ← (b ⊕ c) >>> 12
    state[a] = state[a] + state[b] + (msg[sig2] ^ CONST256[sig1]);//a ← a + b + (mσr (2i+1) ⊕ cσr (2i) )
    state[d] = (state[d] ^ state[a]) >>> 8; //d ← (d ⊕ a) >>> 8
    state[c] = state[c] + state[d]; //c ← c+d
    state[b] = (state[b] ^ state[c]) >>> 7; //b ← (b ⊕ c) >>> 7
  }
  
  public int[] compress32( int[] pre_state, int[] block, int counter )
  {
    int [] state = new int[16];
    state[0] = pre_state[0]; state[1] = pre_state[1]; state[2] = pre_state[2]; state[3] = pre_state[3];
    state[4] = pre_state[4]; state[5] = pre_state[5]; state[6] = pre_state[6]; state[7] = pre_state[7];
    // Salt is not there, no use of XOR with zero, so just assign the values.
    state[8] = CONST256[0]; state[9] = CONST256[1]; state[10] = CONST256[2]; state[11] = CONST256[3];
    // Our message is not above 2^32 bits, so just using int for the counter. The 1st 2 do not need it.
    state[12] = CONST256[4]; state[13] = CONST256[5];
    state[14] = counter ^ CONST256[6]; state[15] = counter ^ CONST256[7];
    for( int i = 0; i < 14; i++ )
    {
      g32( 0, 4, 8,  12, state, 0, block, i ); //G0 (v0 , v4 , v8 , v12 )
      g32( 1, 5, 9,  13, state, 1, block, i ); //G1 (v1 , v5 , v9 , v13 )
      g32( 2, 6, 10, 14, state, 2, block, i ); //G2 (v2 , v6 , v10 , v14 )
      g32( 3, 7, 11, 15, state, 3, block, i ); //G3 (v3 , v7 , v11 , v15 )
      g32( 0, 5, 10, 15, state, 4, block, i ); //G4 (v0 , v5 , v10 , v15 )
      g32( 1, 6, 11, 12, state, 5, block, i ); //G5 (v1 , v6 , v11 , v12 )
      g32( 2, 7, 8,  13, state, 6, block, i ); //G6 (v2 , v7 , v8 , v13 )
      g32( 3, 4, 9,  14, state, 7, block, i ); //G7 (v3 , v4 , v9 , v14 )
    }
    int[] finalised = new int[8]; // None of them are salted.
    finalised[0] = pre_state[0] ^ state[0] ^ state[8];  // h0 ← h0 ⊕ s 0 ⊕ v0 ⊕ v8
    finalised[1] = pre_state[1] ^ state[1] ^ state[9];  // h1 ← h1 ⊕ s 1 ⊕ v1 ⊕ v9
    finalised[2] = pre_state[2] ^ state[2] ^ state[10]; // h2 ← h2 ⊕ s2 ⊕ v2 ⊕ v10
    finalised[3] = pre_state[3] ^ state[3] ^ state[11]; // h3 ← h3 ⊕ s3 ⊕ v3 ⊕ v11
    finalised[4] = pre_state[4] ^ state[4] ^ state[12]; // h4 ← h4 ⊕ s0 ⊕ v4 ⊕ v12
    finalised[5] = pre_state[5] ^ state[5] ^ state[13]; // h5 ← h5 ⊕ s1 ⊕ v5 ⊕ v13
    finalised[6] = pre_state[6] ^ state[6] ^ state[14]; // h6 ← h6 ⊕ s2 ⊕ v6 ⊕ v14
    finalised[7] = pre_state[7] ^ state[7] ^ state[15]; // h7 ← h7 ⊕ s3 ⊕ v7 ⊕ v15
    return finalised;
  }
  
  public int[] transform32( int[][] blocks, int rounds, int msg_len, int digest_len )
  {
    int[] counter = new int[ blocks.length ]; // You will need as many counters as many blocks.
    if((msg_len % 512) > 440) 
    {
      counter[counter.length - 1] = 0;
      counter[counter.length - 2] = msg_len;
    }
    else
    {
      counter[counter.length - 1] = msg_len;
      if(counter.length > 1){ counter[counter.length - 2] = 512; }
    }
    if( counter.length > 2 )
    {
      int bit_counter = 0;
      for( int i = 0; i < (counter.length - 2); i++ ) 
      {
        bit_counter += 512;
        counter[i] = bit_counter;
      }
    }
    int[] state = (224 == digest_len) ? IV224 : IV256;
    for( int i = 0; i < blocks.length ; i++ ) {
      state = compress32( state, blocks[i], counter[i] );
    }
    return state;
  }
  
  public void g64( int a, int b, int c, int d, long[] state, int index, long[] msg, int round )
  {
    int sig1 = SIGMA[round % 10][2 * index];
    int sig2 = SIGMA[round % 10][(2 * index) + 1];
    state[a] = state[a] + state[b] + (msg[sig1] ^ CONST256[sig2]); //a ← a + b + (mσr (2i) ⊕ cσr (2i+1) )
    state[d] = (state[d] ^ state[a]) >>> 16; //d ← (d ⊕ a) >>> 16
    state[c] = state[c] + state[d]; //c ← c+d
    state[b] = (state[b] + state[c]) >>> 12; //b ← (b ⊕ c) >>> 12
    state[a] = state[a] + state[b] + (msg[sig2] ^ CONST256[sig1]);//a ← a + b + (mσr (2i+1) ⊕ cσr (2i) )
    state[d] = (state[d] ^ state[a]) >>> 8; //d ← (d ⊕ a) >>> 8
    state[c] = state[c] + state[d]; //c ← c+d
    state[b] = (state[b] ^ state[c]) >>> 7; //b ← (b ⊕ c) >>> 7
  }
  
  public long[] compress64( long[] pre_state, long[] block, long counter )
  {
    long [] state = new long[16];
    state[0] = pre_state[0]; state[1] = pre_state[1]; state[2] = pre_state[2]; state[3] = pre_state[3];
    state[4] = pre_state[4]; state[5] = pre_state[5]; state[6] = pre_state[6]; state[7] = pre_state[7];
    // Salt is not there, no use of XOR with zero, so just assign the values.
    state[8] = CONST512[0]; state[9] = CONST512[1]; state[10] = CONST512[2]; state[11] = CONST512[3];
    // Our message is not above 2^32 bits, so just using int for the counter. The 1st 2 do not need it.
    state[12] = CONST512[4]; state[13] = CONST512[5];
    state[14] = counter ^ CONST512[6]; state[15] = counter ^ CONST512[7];
    for( int i = 0; i < 14; i++ )
    {
      g64( 0, 4, 8,  12, state, 0, block, i ); //G0 (v0 , v4 , v8 , v12 )
      g64( 1, 5, 9,  13, state, 1, block, i ); //G1 (v1 , v5 , v9 , v13 )
      g64( 2, 6, 10, 14, state, 2, block, i ); //G2 (v2 , v6 , v10 , v14 )
      g64( 3, 7, 11, 15, state, 3, block, i ); //G3 (v3 , v7 , v11 , v15 )
      g64( 0, 5, 10, 15, state, 4, block, i ); //G4 (v0 , v5 , v10 , v15 )
      g64( 1, 6, 11, 12, state, 5, block, i ); //G5 (v1 , v6 , v11 , v12 )
      g64( 2, 7, 8,  13, state, 6, block, i ); //G6 (v2 , v7 , v8 , v13 )
      g64( 3, 4, 9,  14, state, 7, block, i ); //G7 (v3 , v4 , v9 , v14 )
    }
    long[] finalised = new long[8]; // None of them are salted.
    finalised[0] = pre_state[0] ^ state[0] ^ state[8];  // h0 ← h0 ⊕ s 0 ⊕ v0 ⊕ v8
    finalised[1] = pre_state[1] ^ state[1] ^ state[9];  // h1 ← h1 ⊕ s 1 ⊕ v1 ⊕ v9
    finalised[2] = pre_state[2] ^ state[2] ^ state[10]; // h2 ← h2 ⊕ s2 ⊕ v2 ⊕ v10
    finalised[3] = pre_state[3] ^ state[3] ^ state[11]; // h3 ← h3 ⊕ s3 ⊕ v3 ⊕ v11
    finalised[4] = pre_state[4] ^ state[4] ^ state[12]; // h4 ← h4 ⊕ s0 ⊕ v4 ⊕ v12
    finalised[5] = pre_state[5] ^ state[5] ^ state[13]; // h5 ← h5 ⊕ s1 ⊕ v5 ⊕ v13
    finalised[6] = pre_state[6] ^ state[6] ^ state[14]; // h6 ← h6 ⊕ s2 ⊕ v6 ⊕ v14
    finalised[7] = pre_state[7] ^ state[7] ^ state[15]; // h7 ← h7 ⊕ s3 ⊕ v7 ⊕ v15
    return finalised;
  }
  
  public long[] transform64( long[][] blocks, int rounds, int msg_len, int digest_len )
  {
    long[] counter = new long[ blocks.length ];
    if((msg_len % 1024) > 888) 
    {
      counter[counter.length - 1] = 0;
      counter[counter.length - 2] = msg_len;
    }
    else
    {
      counter[counter.length - 1] = msg_len;
      if(counter.length > 1){ counter[counter.length - 2] = 1024; }
    }
    if( counter.length > 2 )
    {
      long bit_counter = 0;
      for( int i = 0; i < (counter.length - 2); i++ ) 
      {
        bit_counter += 1024;
        counter[i] = bit_counter;
      }
    }
    long[] state = (384 == digest_len) ? IV384 : IV512;
    for( int i = 0; i < blocks.length ; i++ ) {
      state = compress64( state, blocks[i], counter[i] );
    }
    return state;
  }
  
  public byte[] squeezeBytesInt( int[] hashed, int digest_len )
  {
    int end_point = digest_len / 32;
    byte[] hash = new byte[ digest_len ];
    ByteBuffer buf = ByteBuffer.allocate(4);
    for( int i = 0; i < end_point; i++ )
    {
      buf.putInt( hashed[i] );
      byte[] arr = buf.array();
      for( int j = 0; j < 4; j++ ) {
        hash[(i * 4) + j] = arr[j];
      }
    }
    return hash;
  }
  
  public byte[] squeezeBytesLong( long[] hashed, int digest_len )
  {
    int end_point = digest_len / 64;
    byte[] hash = new byte[ digest_len ];
    ByteBuffer buf = ByteBuffer.allocate(8);
    for( int i = 0; i < end_point; i++ )
    {
      buf.putLong( hashed[i] );
      byte[] arr = buf.array();
      for( int j = 0; j < 8; j++ ) {
        hash[(i * 8) + j] = arr[j];
      }
    }
    return hash;
  }
  
  public byte[] hash( String msg, int digest_length, int rounds )
  {
    byte [] message = convertHexStringToBytes( msg );
    byte [] padded_msg;
    byte [] hash;
    int [] hash32;
    long [] hash64;
    int [][] blocks32bit;
    long [][] blocks64bit;
    switch( digest_length )
    {
    case 224:
      padded_msg = pad256( message, digest_length );
      blocks32bit = getBlocks32Word( padded_msg );
      hash32 = transform32( blocks32bit, rounds, message.length * 8, digest_length );
      hash = squeezeBytesInt( hash32, digest_length );
      return hash;
    case 256:
      padded_msg = pad256( message, digest_length );
      blocks32bit = getBlocks32Word( padded_msg );
      hash32 = transform32( blocks32bit, rounds, message.length * 8, digest_length );
      hash = squeezeBytesInt( hash32, digest_length );
      return hash;
    case 384:
      padded_msg = pad512( message, digest_length );
      blocks64bit = getBlocks64Word( padded_msg );
      hash64 = transform64( blocks64bit, rounds, message.length * 8, digest_length );
      hash = squeezeBytesLong( hash64, digest_length );
      return hash;
    case 512:
      padded_msg = pad512( message, digest_length );
      blocks64bit = getBlocks64Word( padded_msg );
      hash64 = transform64( blocks64bit, rounds, message.length * 8, digest_length );
      hash = squeezeBytesLong( hash64, digest_length );
      return hash;
    }
    return null;
  }
  
  public static void main( String[] args )
  {
    BLAKE b = new BLAKE();
    byte[] digest = b.hash("54686520717569636b2062726f776e20666f78206a756d7073206f766572207468"
        + "65206c617a7920646f67", 512, 0);
    for( byte d : digest ) {
      System.out.printf("%02X", d);
    }
  }
}
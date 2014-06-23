/*
 * Copyright 2014 Guidewire Software, Inc.
 */

/* Guidewire Software
 *
 * Creator information:
 * User: aheydon
 * Date: Wed May 10 10:56:59 PDT 2006
 *
 * Revision information:
 */

package gw.util.fingerprint;

import gw.test.TestClass;
import gw.util.StreamUtil;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Tests the {@link FP64} implementation.
 *
 * @author Allan Heydon
 */
public class FP64Test extends TestClass {

  protected static final char[] CHAR_ARRAY = new char[] { 'a', 'b', 'c', 'd', 'e' };
  protected static final byte[] BYTE_ARRAY = new byte[] { 50, 51, 52, 53, 54, 55 };

  public void testNullaryConstructorIsFingerprintOfEmptyString() {
    assertEquals(new FP64(""), new FP64());
  }

  public void testCopyConstructor() {
    FP64 fp = new FP64("\u2345testCopyConstructor\u2345");
    assertEquals(fp, new FP64(fp));
  }

  public void testCharArrayConstructor() {
    String s = "\u2345testCharArrayConstructor\u2345";
    assertEquals(new FP64(s), new FP64(s.toCharArray()));
  }

  public void testByteArrayConstructor() throws UnsupportedEncodingException {
    String s = "testByteArrayConstructor";
    FP64 fp = new FP64(s);
    assertEquals(fp, new FP64(StreamUtil.toBytes(s)));
  }

  public void testByteSubarrayConstructor() throws UnsupportedEncodingException {
    String s = "testByteSubarrayConstructor";
    assertEquals(new FP64(s.substring(10, 15)), new FP64(StreamUtil.toBytes(s), 10, 5));
  }

  public void testInputStreamConstructor() throws Exception {
    String s = "testInputStreamConstructor";
    assertEquals(new FP64(s), new FP64(new ByteArrayInputStream(StreamUtil.toBytes(s))));
  }

  public void testToBytesNoBuffer() {
    String s = "\u2345testToBytesNoBuffer\u2345";
    FP64 fp = new FP64(s);
    byte[] expBytes = new byte[]{
      (byte) 0xde, (byte) 0x94, (byte) 0x7c, (byte) 0x06,
      (byte) 0x48, (byte) 0x2d, (byte) 0x2f, (byte) 0xad
    };
    assertByteArraysEqual(expBytes, fp.toBytes());
  }

  public void testToBytesWithBufferFailsForNullArg() {
    try {
      new FP64().toBytes(null);
      throw new RuntimeException( "Should have failed" );
    } catch (AssertionError ex) {
      // expected case
    }
  }

  public void testToBytesWithBufferFailsForIncorrectBufferSize() {
    try {
      new FP64().toBytes(new byte[9]);
      throw new RuntimeException( "Should have failed" );
    } catch (AssertionError ex) {
      // expected case
    }
  }

  public void testToBytesWithBuffer() {
    String s = "\u2345testToBytesWithBuffer\u2345";
    FP64 fp = new FP64(s);
    byte[] expBytes = new byte[]{
      (byte) 0x53, (byte) 0x1c, (byte) 0x1f, (byte) 0x3b,
      (byte) 0xf0, (byte) 0x33, (byte) 0xf9, (byte) 0xa2
    };
    byte[] buffer = new byte[8];
    assertByteArraysEqual(expBytes, fp.toBytes(/*INOUT*/ buffer));
    assertByteArraysEqual(expBytes, buffer);
  }

  public void testToHexString() {
    String s = "\u2345testToHexString\u2345";
    FP64 fp = new FP64(s);
    String expHexString = "";
    byte[] bytes = fp.toBytes();
    for (byte aByte : bytes) {
      expHexString = byteToHexString(aByte) + expHexString;
    }
    assertEquals(expHexString, fp.toHexString());
  }

  public void testToHexStringPadsToSixteenChars() {
    FP64 fp = new FP64();
    String hexString;
    do {
      hexString = fp.toHexString();
      assertEquals(16, hexString.length());
      fp.extend("abc");
    } while (!hexString.startsWith("0000"));
  }

  private String byteToHexString(byte b) {
    return encodeNibble((byte) (b >> 4)) + encodeNibble(b);
  }

  private static final String[] HEX_CHARS = new String[] {
    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"
  };

  private String encodeNibble(byte b) {
    return HEX_CHARS[b & 0xf];
  }

  /**
   * If "s", "s1", and "s2" are any Strings such that s = s1 + s2, this
   * tests that "new FP64(s) = new FP64(s1).extend(s2)".
   */
  public void testExtendByStringEquivalentToFingerprintingConcatenationOfStrings() {
    // form the string "s"
    String s = makeBigString();

    // fingerprint "s"
    FP64 strFP = new FP64(s);

    // check that sFP = new FP64(s1).extend(s2) for all strings s1, s2
    for (int i = 0; i <= s.length(); i++) {
      String s1 = s.substring(0, i);
      String s2 = s.substring(i);
      FP64 s1s2FP = new FP64(s1).extend(s2);
      assertEquals(strFP, s1s2FP);
    }
  }

  public void testExtendByCharSubarray() {
    String s = makeBigString();
    FP64 strFP = new FP64(s);
    char[] chars = s.toCharArray();
    int curr = 10, len = 0;
    FP64 charFP = new FP64(chars, 0, curr);
    while (curr < chars.length) {
      len = Math.min(len, chars.length - curr);
      charFP.extend(chars, curr, len);
      curr += len;
      len++;
    }
    assertEquals(strFP, charFP);
  }

  public void testExtendByByteSubarray() throws UnsupportedEncodingException {
    String s = makeBigString();
    FP64 strFP = new FP64(StreamUtil.toBytes(s));
    byte[] bytes = StreamUtil.toBytes(s);
    int curr = 10, len = 0;
    FP64 charFP = new FP64(bytes, 0, curr);
    while (curr < bytes.length) {
      len = Math.min(len, bytes.length - curr);
      charFP.extend(bytes, curr, len);
      curr += len;
      len++;
    }
    assertEquals(strFP, charFP);
  }

  public void testExtendByInt() {
    FP64 fp1 = new FP64().extend((byte) 1).extend((byte) 2).extend((byte) 3).extend((byte) 4);
    FP64 fp2 = new FP64().extend((1 << 24) + 2 * (1 << 16) + 3 * (1 << 8) + 4);
    assertEquals(fp1, fp2);
  }

  public void testExtendByChar() {
    String s = makeBigString();
    char[] chars = s.toCharArray();
    FP64 strFP = new FP64(chars);
    FP64 byCharFP = new FP64();
    for (char aChar : chars) {
      byCharFP.extend(aChar);
    }
    assertEquals(strFP, byCharFP);
  }

  public void testExtendByByte() throws UnsupportedEncodingException {
    String s = makeBigString();
    byte[] bytes = StreamUtil.toBytes(s);
    FP64 strFP = new FP64(bytes);
    FP64 byByteFP = new FP64();
    for (byte aByte : bytes) {
      byByteFP.extend(aByte);
    }
    assertEquals(strFP, byByteFP);
  }

  public void testEqualsReturnsTrueForEqualRefs() {
    FP64 fp = new FP64();
    assertTrue(fp.equals(fp));
  }

  public void testEqualsReturnsFalseForArgumentOfIncorrectType() {
    String s = "testEqualsReturnsFalseForArgumentOfIncorrectType";
    assertFalse(new FP64(s).toString().equals(s));
  }

  public void testEqualsReturnsFalseForFPsOfDifferentBytes() {
    String s1 = "testEqualsReturnsFalseForFPsOfDifferentBytes";
    String s2 = s1 + "-v2";
    assertFalse(new FP64(s1).equals(new FP64(s2)));
  }

  public void testEqualsReturnsTrueForDifferentFPsOfIdenticalBytes() {
    String s = "testEqualsReturnsTrueForDifferentFPsOfIdenticalBytes";
    FP64 fp1 = new FP64(s);
    FP64 fp2 = new FP64(s);
    assertNotSame(fp1, fp2);
    assertTrue(fp1.equals(fp2));
  }

  public void testHashcodeAndEquals() {
    // populate map with FPs mapped to strings that produced them
    final int N = 100000;
    Map<FP64, String> map = new HashMap<FP64, String>(N);
    final String suffix = makeBigString();
    for (int i = 0; i < N; i++) {
      String s = Integer.toString(i) + " - " + suffix;
      FP64 fp = new FP64(s);
      Object gotS = map.put(fp, s);
      // assert that no two distinct strings have the same FP
      assertNull("Diff strings have identical FP: '" + s + "' and '" + gotS + "'", gotS);
    }
    assertEquals(N, map.size());

    // check ability to look up values in the map by FP
    for (String s : map.values()) {
      FP64 sFP = new FP64(s);
      Object val = map.get(sFP);
      assertEquals(s, val);
    }
  }

  private String makeBigString() {
    String x = "\u2345Hello, Fingerprint!\u2345";
    String res = "";
    for (int i = 0; i < 10; i++) {
      res += x;
    }
    return res;
  }

  private void assertByteArraysEqual(byte[] expBytes, byte[] gotBytes) {
    assertEquals(expBytes.length, gotBytes.length);
    for (int i = 0; i < expBytes.length; i++) {
      assertEquals(expBytes[i], gotBytes[i]);
    }
  }

  //this test proves a point: you can't stick data into a fingerprint in arbitrary order!
  public void testFingerprintIsNonSymmetric() {
    final FP64 fp64a = new FP64().extend("first").extend("second");
    final FP64 fp64b = new FP64().extend("second").extend("first");
    assertFalse(fp64a.equals(fp64b));
  }

  public void testFingprintingOfSimilarStrings() {
    Set<FP64> s = new HashSet<FP64>();
    fp(s, "The quick brown fox");
    fp(s, "The Quick brown fox");
    fp(s, "The quick crown fox");
    fp(s, "The quick brown box");
    fp(s, "The quick brown foxx");
    fp(s, "");
    fp(s, "a");
    fp(s, "aa");
    fp(s, "aaa");
    fp(s, "aaaa");
    fp(s, "b");
    fp(s, "bb");
    fp(s, "bbb");
    fp(s, "bbbb");
  }

  // NOTE pdalbora 10-Jul-2009 -- I added this test to confirm that a bug existed in a prior implementation. FP64
  // used to have this mode where it would normalize EOL characters. This code introduced a bug whereby all 0xFF
  // bytes were ignored. In other words, the byte sequences such 0xA0FFB1FF and 0xA0B1FFFF would have yielded the same
  // fingerprint!
  public void testFFNotIgnored() {
    int i = 0xA0FFB1FF;
    int j = 0xA0B1FFFF;
    assertFalse(new FP64().extend(i).equals(new FP64().extend(j)));
  }

  private void fp(Set<FP64> seen, String s) {
    FP64 fp = new FP64(s);
    //System.out.println("FP64(" + s + ") --> " + fp.toHexString());
    assertFalse(seen.contains(fp));
    seen.add(fp);
  }
}

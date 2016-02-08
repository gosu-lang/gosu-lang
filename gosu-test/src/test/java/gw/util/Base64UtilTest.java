/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import gw.util.StreamUtil;
import gw.util.Base64Util;
import gw.test.TestClass;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * To change this template use Options | File Templates.
 */
public class Base64UtilTest extends TestClass {

  public void testEncodeDecode() {
    byte[] bytes = createBytes();
    String encoded = Base64Util.encode(bytes);
    byte[] result = Base64Util.decode(encoded);
    assertTrue("Expected " + new String(bytes) + " but got " + new String(result),
        Arrays.equals(bytes, Base64Util.decode(encoded)));
  }

  public void testStringEncodingProducesSameCharsAsCastingBytesUpToChar() {
    byte[] bytes = createBytes();
    String encoded = Base64Util.encode(bytes);
    byte[] encodedBytes = Base64Util.Base64.encodeBase64(bytes);
    char[] encodedChars = new char[encodedBytes.length];
    for (int i = 0; i < encodedBytes.length; i++) {
      byte encodedByte = encodedBytes[i];
      encodedChars[i] = (char) encodedByte;
    }
    assertTrue("Expected " + new String(encodedChars) + " but got " + encoded,
        Arrays.equals(encodedChars, encoded.toCharArray()));
  }

  public void testIsBase64ReturnsTrueForBase64EncodedString() throws UnsupportedEncodingException {
    assertTrue(Base64Util.isBase64( Base64Util.encode( StreamUtil.toBytes("Lot\'s of-/bad characters"))));
  }

  public void testIsBase64ReturnsFalseForStringWithDisallowedCharacters() {
    assertFalse(Base64Util.isBase64("Lot\'s of-/bad characters"));
  }

  private byte[] createBytes() {
    // Create a byte array with every possible byte value.
    byte[] bytes = new byte[Byte.MAX_VALUE - Byte.MIN_VALUE];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) (Byte.MIN_VALUE + i);
    }
    return bytes;
  }
}

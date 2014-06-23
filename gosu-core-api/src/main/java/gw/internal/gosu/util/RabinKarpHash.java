/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Fast multi-pattern string matcher.
 *
 * Quick and dirty implementation of Rabin-Karp algorithm. Used for paths matching.
 *
 * We mach patterns from the end to reduce amount of possible collisions (filesystem paths tend to have common prefixes,
 * like package names, etc).
 *
 * FIXME: Tests...
 *
 * @see <a href="http://en.wikipedia.org/wiki/Rabin%E2%80%93Karp_algorithm">Rabin–Karp algorithm</a>
 */
public class RabinKarpHash {

  private final static int A = 31;
  private final int _block; // block length
  private final int _Apowblock; // 'a' constant in power of block length


  private Set<Integer> _hashes = new HashSet<Integer>();
  private String[] _patterns;

  // Hash values for characters
  private static int CHAR_HASHES[] = new int[1 << 16];
  static {
    // Generate random hashes for characters
    Random r = new Random();
    for (int k = 0; k < CHAR_HASHES.length; ++k) {
      CHAR_HASHES[k] = r.nextInt();
    }
  }


  public RabinKarpHash(String... patterns) {
    _block = minLen(patterns);
    int apowbl = 1;
    for (int i = 0; i < _block; ++i) {
      apowbl *= A;
    }
    _Apowblock = apowbl;

    // Remember hashes last 'block' characters of all patterns.
    _patterns = patterns;
    for (String pattern : patterns) {
      int hash = reverseHash(pattern);
      _hashes.add(hash);
    }
  }

  /**
   * Find the shortest of all patterns.
   * @param patterns
   * @return
   */
  private static int minLen(String... patterns) {
    int minLen = patterns[0].length();
    for (String str : patterns) {
      if (str.length() < minLen) {
        minLen = str.length();
      }
    }
    return minLen;
  }

  public boolean matches(String str) {
    // String is shorter than longest pattern
    int len = str.length();
    if (len < _block) {
      return false;
    }
    // Get reverse hash of the tail of the string
    int hash = reverseHash(str);

    // Start matching the hash
    for(int i = 0; i < len - _block; i++) {
      if (_hashes.contains(hash) && exactMatch(str, i)) {
        return true;
      }
      hash = rollHash(hash, str, i);
    }

    // Last iteration
    return _hashes.contains(hash) && exactMatch(str, len - _block);
  }

  /**
   * Check for exact match.
   * FIXME:
   * @param str
   * @param i
   * @return
   */
  private boolean exactMatch(String str, int i) {
    int end = str.length() - i;
    for (String p : _patterns) {
      int start = end - p.length();
      if (start >= 0 && str.regionMatches(start, p, 0, p.length())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Update rolling hash values.
   * @param hashvalue
   * @param str
   * @param i
   * @return
   */
  private int rollHash(int hashvalue, String str, int i) {
    // 'roll' hash
    char outchar = str.charAt(str.length() - 1 - i);
    char inchar = str.charAt(str.length() - _block - 1 - i);
    hashvalue = A * hashvalue + CHAR_HASHES[inchar] - _Apowblock * CHAR_HASHES[outchar];
    return hashvalue;
  }

  /**
   * Take rolling hash of last 'block' characters. Start from the end of the string.
   * @param str
   * @return
   */
  private int reverseHash(String str) {
    int hash = 0;
    int len = str.length();
    for (int i = 0; i < _block; i++) {
      char c = str.charAt(len - i - 1);
      hash = A * hash + CHAR_HASHES[c];
    }
    return hash;
  }
}

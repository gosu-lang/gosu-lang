/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

import gw.util.GosuObjectUtil;
import gw.util.GosuStringUtil;
import junit.framework.TestCase;

import gw.util.Array;
import java.util.*;

public abstract class AssertUtil extends TestCase implements ITestWithMetadata {

  public static void assertArrayEquals(Object[] expected, Object[] got) {
    assertArrayEquals(expected, got, new TestClass.EqualityTester() {
      @Override
      public boolean equals(Object expected, Object got) {
        if (expected != null && got != null && expected.getClass().isArray() && got.getClass().isArray() && Array.getLength(expected) == Array.getLength(got)) {
          int length = Array.getLength(expected);
          for (int i = 0; i < length; i++) {
            if (!equals(Array.get(expected, i), Array.get(got, i))) {
              return false;
            }
          }
          return true;
        }
        return GosuObjectUtil.equals(expected, got);
      }
    });
  }

  /**
   * Verifies that all elements in the first array are present in the second
   * array and match the elements in the first array. Uses EqualityUtil to
   * determine equality and is order-insensitive.
   *
   * @param expected the expected result (reference)
   * @param got      the obtained result (what to compare against the reference)
   */
  public static void assertArrayEquals(Object[] expected, Object[] got, TestClass.EqualityTester tester) {
    if (expected == null) {
      if (got == null) {
        return;
      } else {
        fail("Expected null, got non-null");
      }
    } else {
      if (got == null) {
        fail("Expected non-null, got null");
      }
    }
    boolean[] expectedFound = makeFoundArray(expected.length);
    boolean[] gotFound = makeFoundArray(got.length);
    for (int i = 0; i < expected.length; i++) {
      Object expectedObject = expected[i];
      for (int j = 0; j < got.length; j++) {
        if (tester.equals(expectedObject, got[j]) && !gotFound[j]) {
          expectedFound[i] = true;
          gotFound[j] = true;
          break;
        }
      }
    }
    if (!allTrue(expectedFound) || !allTrue(gotFound)) {
      StringBuffer sb = new StringBuffer();
      sb.append("\nExpected:\n");
      appendFoundStatus(sb, expected, expectedFound);
      sb.append("\nGot:\n");
      appendFoundStatus(sb, got, gotFound);
      fail(sb.toString());
    }
  }

  private static void appendFoundStatus(StringBuffer /*INOUT*/ sb, Object[] expected, boolean[] expectedFound) {
    sb.append("[\n");
    for (int i = 0; i < expected.length; i++) {
      Object o = expected[i];
      sb.append(expectedFound[i] ? "  " : "! ");  // "! " means we didn't find it
      sb.append(o);
      sb.append("\n");
    }
    sb.append("]\n");
  }

  private static boolean[] makeFoundArray(int length) {
    boolean[] found = new boolean[length];
    for (int i = 0; i < found.length; i++) {
      found[i] = false;
    }
    return found;
  }

  private static boolean allTrue(boolean[] booleans) {
    for (boolean b : booleans) {
      if (!b) {
        return false;
      }
    }
    return true;
  }

  // TODO - AHK - This should be using the other variant
  public static void assertArrayEquals(String message, Object[] o1, Object[] o2) {
    boolean equals = false;
    if (o1.length == o2.length) {
      equals = true;
      for (int i = 0; i < o1.length; i++) {
        if (!GosuObjectUtil.equals(o1[i], o2[i])) {
          equals = false;
          break;
        }
      }
    }
    assertTrue(message + " Arrays were not equal. Expected \n[" + GosuStringUtil.join(o1, ",") + "] but found \n[" + GosuStringUtil.join(o2, ",") + "]", equals);
  }

  public static void assertSetsEqual(Set o1, Set o2) {
    boolean equals = GosuObjectUtil.equals(o1, o2);
    assertTrue("Sets were not equal.  Expected \n[" + GosuStringUtil.join(o1, ",") + "] but found \n[" + GosuStringUtil.join(o2, ",") + "]", equals);
  }

  public static void assertCollectionEquals(Collection o1, Collection o2) {
    assertIterableEqualsIgnoreOrder(o1, o2);
  }

  public static void assertListEquals(List o1, List o2) {
    assertIterableEquals(o1, o2, "Lists");
  }

  public static void assertIterableEquals(Iterable o1, Iterable o2) {
    assertIterableEquals(makeList(o1), makeList(o2), "Iterables");
  }

  public static void assertCollectionEquals(Collection o1, Collection o2, Comparator c) {
    assertIterableEquals(o1, o2, c, "Collections");
  }

  public static void assertListEquals(List o1, List o2, Comparator c) {
    assertIterableEquals(o1, o2, c, "Lists");
  }

  public static void assertIterableEquals(Iterable o1, Iterable o2, Comparator c) {
    assertIterableEquals(makeList(o1), makeList(o2), c, "Iterables");
  }

  public static void assertIterableEqualsIgnoreOrder(Iterable i1, Iterable i2) {
    Map count1 = makeHistogram(i1);
    Map count2 = makeHistogram(i2);
    if (!count1.equals(count2)) {
      assertTrue("Iterators were not equal ignoring order.  Expected [" + GosuStringUtil.join(i1.iterator(), ",") + "] but found [" + GosuStringUtil.join(i2.iterator(), ",") + "]", false);
    }
  }

  public static void assertZero(int i) {
    assertTrue("Should be zero, but found " + i, i == 0);
  }

  public static void assertZero(long i) {
    assertTrue("Should be zero, but found " + i, i == 0);
  }

  public static void assertMatchRegex(String message, String pattern, String result) {
    assertTrue(message + ": " + pattern + " does not match " + result, result.matches(pattern));
  }

  private static Map makeHistogram(Iterable o1) {
    HashMap<Object, Integer> hist = new HashMap<Object, Integer>();
    if (o1 != null) {
      for (Object o : o1) {
        Integer integer = hist.get(o);
        if (integer == null) {
          hist.put(o, 0);
        } else {
          hist.put(o, ++integer);
        }
      }
    }
    return hist;
  }

  private static void assertIterableEquals(Iterable i1, Iterable i2, String s) {
    boolean equals = true;

    if (i1 == i2) return;

    Iterator e1 = i1.iterator();
    Iterator e2 = i2.iterator();
    while (e1.hasNext() && e2.hasNext()) {
      Object o1 = e1.next();
      Object o2 = e2.next();
      if (o1 == null) {
        if (o2 != null) {
          equals = false;
          break;
        }
      } else if (!o1.equals(o2)) {
        equals = false;
        break;
      }
    }
    if (equals) {
      equals = !(e1.hasNext() || e2.hasNext());
    }
    assertTrue(s + " were not equal.  Expected \n[" + GosuStringUtil.join(i1.iterator(), ",") + "] but found \n[" + GosuStringUtil.join(i2.iterator(), ",") + "]", equals);
  }

  private static void assertIterableEquals(Iterable i1, Iterable i2, Comparator c, String s) {
    boolean equals = true;

    if (i1 == i2) return;

    Iterator e1 = i1.iterator();
    Iterator e2 = i2.iterator();
    while (e1.hasNext() && e2.hasNext()) {
      Object o1 = e1.next();
      Object o2 = e2.next();
      if (o1 == null) {
        if (o2 != null) {
          equals = false;
          break;
        }
      } else if (c.compare(o1, o2) != 0) {
        equals = false;
        break;
      }
    }
    if (equals) {
      equals = !(e1.hasNext() || e2.hasNext());
    }
    assertTrue(s + " were not equal.  Expected \n[" + GosuStringUtil.join(i1.iterator(), ",") + "] but found \n[" + GosuStringUtil.join(i2.iterator(), ",") + "]", equals);
  }

  private static List makeList(Iterable o1) {
    ArrayList lst = new ArrayList();
    for (Object o : o1) {
      lst.add(o);
    }
    return lst;
  }

  public static void assertCausesException(Runnable r, Class<? extends Throwable> c) {
    try {
      r.run();
    } catch (Throwable t) {
      if (c.isAssignableFrom(t.getClass())) {
        return;
      } else {
        fail("Expecting exception of type " + c + ", but got exception of type " + t.getClass());
      }
    }
    fail("No exception was thrown when executing " + r + ".  Expected exception of type " + c);
  }
}


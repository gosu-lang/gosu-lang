/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class Asm_InnerClasses {
  class Inner1 {
    class Inner1_Inner {}
  }
  public static class Inner2<T extends Inner1> {
    class Inner2_Inner {
      List<T> getOuterList() { return new ArrayList<T>(); }
    }

    interface Inner2_InnerInterface {
    }

    enum Inner2_InnerEnum {
      ABC( "hello" ),
      DEF( "bye" ),
      GHI( "fred" );

      Inner2_InnerEnum( String x ) {

      }
    }
  }

  private Object _anon =
    new Object() {
      public String toString() {
        Object o =
          new Object() {

          };
        return o.toString();
      }
    };

  public Inner2 usesInnerClasses( Inner1.Inner1_Inner p0 ) {
    Inner2 x = null;
    Inner2.Inner2_Inner y = null;
    return x;
  }


  public class Muh <S, T>  {
      public Muh() {}

      public abstract class Inner implements java.lang.Comparable<Inner> {
          public Inner() {}
          public int compareTo( Inner o ) { return 0; }
      }
  }
}

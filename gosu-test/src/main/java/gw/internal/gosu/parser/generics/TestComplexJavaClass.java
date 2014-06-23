/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.generics;

import java.util.ArrayList;

/**
 */
public class TestComplexJavaClass<A,B> extends ArrayList<B>
{
  private A _a;

  public TestComplexJavaClass( A a )
  {
    _a = a;
  }
}

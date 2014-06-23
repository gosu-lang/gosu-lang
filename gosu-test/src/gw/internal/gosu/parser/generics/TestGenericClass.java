/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.generics;

import java.util.List;
import java.util.ArrayList;

/**
 */
public class TestGenericClass<T>
{
  private T _member;

  public TestGenericClass( T in )
  {
    _member = in;
  }

  public TestGenericClass( T in, String str )
  {
    _member = in;
//    if( str instanceof T ) // Illegal
  }

  public T getMember()
  {
    return _member;
  }
  public void setMember( T member )
  {
    _member = member;
  }

  public List<? extends CharSequence> getCharSequenceList()
  {
    List<CharSequence> list = new ArrayList<CharSequence>();
    list.add( new StringBuilder() );
    return list;
  }

  public <Q extends CharSequence> Q genericMethod( Q q ) { return q; }
}

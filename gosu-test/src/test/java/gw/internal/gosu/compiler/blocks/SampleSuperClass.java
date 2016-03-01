/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler.blocks;

public class SampleSuperClass
{
  protected String _str;

  public void setIt( String s )
  {
    _str = s;
  }

  public String callIt()
  {
    return _str;
  }
}

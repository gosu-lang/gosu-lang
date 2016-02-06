/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler.sample.statement.classes;

import java.awt.*;

/**
 */
public class JavaBaseClass<T extends Shape>
{
  private T _t;

  public T access()
  {
    return _t;
  }
  public void modify( T t )
  {
    _t = t;
  }

  public <M> T involvesMethodTv( T t, M m )
  {
    return t;
  }

  public <M extends String> M me( M m )
  {
    return m;
  }
}

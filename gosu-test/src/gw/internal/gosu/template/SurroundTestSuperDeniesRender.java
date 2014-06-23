/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.template;

import gw.lang.reflect.IType;

import java.io.IOException;
import java.io.Writer;

public class SurroundTestSuperDeniesRender
{
  public static boolean beforeRender( IType template, Writer writer ) throws IOException
  {
    return false;
  }

  public static void afterRender( IType template, Writer writer ) throws IOException
  {
    throw new RuntimeException( "Shouldn't have been called" );
  }
}

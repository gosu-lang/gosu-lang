/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.template;

import gw.lang.reflect.IType;

import java.io.IOException;
import java.io.Writer;

public class SurroundTestSuper1
{
  public static void beforeRender( IType template, Writer writer ) throws IOException
  {
    writer.write( "Before Template : " + template.getName() );
  }

  public static void afterRender( IType template, Writer writer ) throws IOException
  {
    writer.write( "After Template : " + template.getName() );
  }
}

/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

import gw.lang.parser.IFileContext;

import java.io.File;

public class ProgramFileContext implements IFileContext
{
  private File _file;
  private String _fqn;

  public ProgramFileContext( File file, String fqn )
  {
    _file = file;
    _fqn = fqn;
  }

  @Override
  public String getContextString()
  {
    return null;
  }

  @Override
  public String getClassName()
  {
    return _fqn != null ? _fqn : makeJavaName( _file.getName() );
  }

  @Override
  public String getFilePath()
  {
    return _file != null ? _file.getName() : null;
  }

  private static String makeJavaName( String name )
  {
    StringBuilder sb = new StringBuilder();
    for( int i = 0; i < name.length(); i++ )
    {
      char c = name.charAt( i );
      if( !Character.isJavaIdentifierPart( c ) && c != '.' )
      {
        sb.append( "_" );
      }
      else
      {
        sb.append( c );
      }
    }
    return sb.toString();
  }
}

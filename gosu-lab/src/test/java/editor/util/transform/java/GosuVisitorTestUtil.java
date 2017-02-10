/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package editor.util.transform.java;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

public class GosuVisitorTestUtil
{

  public String transform( String jsrc ) throws IOException
  {
    String src = readFile( jsrc );
    if( src == null )
    {
      return null;
    }

    return JavaToGosu.convertString( src, 4 );
  }

  public String readFile( String path ) throws IOException
  {
    StringBuilder src = new StringBuilder();
    URL url = null;
    try
    {
      url = Thread.currentThread().getContextClassLoader().getResource( path ).toURI().toURL();
    }
    catch( URISyntaxException e )
    {
      throw new RuntimeException( e );
    }
    try( BufferedReader reader = new BufferedReader( new InputStreamReader( url.openStream() ) ) )
    {
      String line;
      while( (line = reader.readLine()) != null )
      {
        src.append( line ).append( "\n" );
      }
    }
    return src.toString();
  }
}

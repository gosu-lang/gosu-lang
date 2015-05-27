package gw.gosudoc;


import gw.lang.Gosu;
import gw.lang.reflect.ReflectUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class GosuDoc
{
  public static void main( String[] args ) throws IOException
  {
    if( args.length != 3 )
    {
      System.out.printf( "Usage: java gw.gosudoc.GosuDoc <classpath> <directoriesToDoc> <outputDirector>" );
      System.exit( -1 );
    }

    File outputDirectory = new File( args[2] );

    List<File> inputDirs = new ArrayList<>();
    for( String file : args[1].split( ";:" ) )
    {
      inputDirs.add( new File( file ) );
    }

    List<File> gosuClassPath = new ArrayList<>();
    for( String file : args[0].split( ";:" ) )
    {
      gosuClassPath.add( new File( file ) );
    }

    // hack for GW build
    if( System.getProperty( "gw.classpath.file" ) != null )
    {
      Reader fileReader = new FileReader( new File( System.getProperty( "gw.classpath.file" ) ) );
      try (BufferedReader reader = new BufferedReader( fileReader ))
      {
        String classPathFromFile = reader.readLine();
        for( String file : classPathFromFile.split( ";:" ) )
        {
          gosuClassPath.add( new File( file ) );
        }
      }
    }

    // initialize
    Gosu.init(gosuClassPath);

    // Reflectively invoke the writer
    ReflectUtil.invokeStaticMethod( "gw.gosudoc.GSDocHTMLWriter", "go", inputDirs, outputDirectory );
  }

}
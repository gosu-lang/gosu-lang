/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.init.ClasspathToGosuPathEntryUtil;
import gw.lang.init.GosuInitialization;
import gw.lang.reflect.TypeSystem;
import gw.test.Suite;
import gw.test.TestEnvironment;
import gw.util.Predicate;
import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;

public class PLGosuBenchmarkSuite extends Suite {

  public static Test suite()
  {
    return new PLGosuBytecodeSuite()
      .withTestEnvironment( new BenchmarkTestEnvironment() )
      .withPackages( "gw.internal.gosu.compiler" )
      .withIFileFilter(
        new Predicate<IFile>()
        {
          public boolean evaluate( IFile o )
          {
            return o.getName().contains( "BenchmarkTest" );
          }
        } );
  }

  private static class BenchmarkTestEnvironment extends TestEnvironment
  {
    @Override
    public void initializeTypeSystem()
    {
      List<IDirectory> classpath = constructClasspathFromSystemClasspath();
      for( IDirectory file : new ArrayList<>( classpath ) )
      {
        if( file.getName().endsWith( "classes" ) )
        {
          classpath.add( file.getParent().dir( "gsrc" ) );
          classpath.add( file.getParent().dir( "gtest" ) );
        }
      }
      GosuInitialization.instance( TypeSystem.getExecutionEnvironment() )
        .initializeRuntime( ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries( classpath ) );
    }
  }

}
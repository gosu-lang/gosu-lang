/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.internal.gosu.compiler.sample.benchmark.raytrace.JavaRayTrace;
import gw.testharness.Disabled;

/**
 */
@Disabled(assignee = "smckinney", reason = "This is causing an OOM error in TH")
public class RayTraceBenchmarkTest extends ByteCodeTestBase
{
  public void testJavaRayTraceBenchmark() throws Exception
  {
    long t = System.currentTimeMillis();
    for( int i = 0; i < 10; i++ )
    {
      JavaRayTrace.main( null );
    }
    long delta = System.currentTimeMillis() - t;
    long seconds = delta / 10 / 1000;
    System.out.println( "Java time: " + delta / 10 / 1000 );

    assertTrue( seconds <= 20 );
  }

  public void testGosuRayTraceBenchmark() throws Exception
  {
    Class<?> javaClass = GosuClassLoader.instance().findClass( "gw.internal.gosu.compiler.sample.benchmark.raytrace.RayTrace" );
    long t = System.currentTimeMillis();
    for( int i = 0; i < 10; i++ )
    {
      javaClass.getMethod( "main" ).invoke( null );
    }
    long delta = System.currentTimeMillis() - t;
    long seconds = delta / 10 / 1000;
    System.out.println( "Gosu time: " + delta / 10 / 1000 );

    assertTrue( seconds <= 20 );
  }
}
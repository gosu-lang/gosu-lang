/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.internal.gosu.compiler.sample.benchmark.josephus.Chain;

/**
 */
public class JosephusBenchmarkTest extends ByteCodeTestBase
{
  public void testJosephusGosu() throws Exception
  {
    final String cls = "gw.internal.gosu.compiler.sample.benchmark.josephus.GosuChain";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    int iNanos = (Integer)javaClass.getMethod( "run" ).invoke( null );
    //TODO cgross - figure out a good way to run benchmarks consistently
    System.out.println( "Time was " + iNanos );
    assertTrue( "Time needs to be less than 30000, but was " + iNanos, iNanos < 30000 );
  }

  public void testJosephusJava() throws Exception
  {
    Chain.main( null );
  }

}

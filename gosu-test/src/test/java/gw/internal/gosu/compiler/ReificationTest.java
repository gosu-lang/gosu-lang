/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 */
public class ReificationTest extends ByteCodeTestBase
{
  private static final int WORK_LOOP = 1000000;
  private static final int REPEAT_COUNT = 20;

  public void testReification() throws Exception {
    IGosuClass cls = (IGosuClass)TypeSystem.getByFullName( "gw.internal.gosu.compiler.sample.benchmark.reification.ReificationBenchmark" );
    Callable<List<Runnable>> test = (Callable<List<Runnable>>)cls.getBackingClass().newInstance();
    List<Runnable> runnables = test.call();
    for( Runnable r: runnables ) {
      work( r );
      for( int i = 0; i < REPEAT_COUNT; i++ ) {
        long t = System.nanoTime();
        work( r );
        long delta = System.nanoTime() - t;
        long millis = delta / WORK_LOOP;
        System.out.println( "Gosu time: " + millis + "nanos" );
      }
    }
  }

  private void work( Runnable r ) {
    for( int i = 0; i < WORK_LOOP; i++ ) {
      r.run();
    }
  }

  public void testBaseline() throws Exception {
    baselineWork();
    for( int i = 0; i < REPEAT_COUNT; i++ ) {
      long t = System.nanoTime();
      baselineWork();
      long delta = System.nanoTime() - t;
      long millis = delta / WORK_LOOP;
      System.out.println( "Baseline time: " + millis + "nanos" );
    }
  }

  private void baselineWork() {
    Map m = new HashMap();
    for( int i = 0; i < WORK_LOOP; i++ ) {
      m.get( i );
    }
  }

}

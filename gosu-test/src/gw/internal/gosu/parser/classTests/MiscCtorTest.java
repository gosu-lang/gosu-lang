/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.classTests;

import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.test.TestClass;

public class MiscCtorTest extends TestClass
{
  public void testSubclassWithStaticMethodDoesNotOverrideSuperCtor() throws ClassNotFoundException, InterruptedException
  {
    Task task = new Task();

//    Thread[] threads = new Thread[] {new Thread( task ), new Thread( task ), new Thread( task ), new Thread( task ), new Thread( task ), new Thread( task ), new Thread( task ), new Thread( task ), new Thread( task ), new Thread( task ), new Thread( task ), new Thread( task ), new Thread( task ), new Thread( task ) };
//    for( int i = 0; i < threads.length; i++ )
//    {
//      threads[i].start();
//    }
//    for( int i = 0; i < threads.length; i++ )
//    {
//      threads[i].join();
//    }
    new Task().run();
  }

  class Task implements Runnable
  {
    public void run()
    {
      IGosuClass gsClass = (IGosuClass)TypeSystem.getByFullNameIfValid( "gw.internal.gosu.parser.classTests.gwtest.ctor.FooSuite" );
      gsClass.getTypeInfo().getConstructor().getConstructor().newInstance();
      assertTrue( gsClass.isValid() );
      IMethodInfo m = gsClass.getTypeInfo().getMethod( "suite" );
      assertTrue( m.isStatic() );
      m.getCallHandler().handleCall( null );
      System.out.println( Thread.currentThread().getName() );
    }
  }
}
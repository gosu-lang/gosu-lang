/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.classTests.gwtest.profile;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.test.TestClass;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

//!!
//!! IMPORTANT!  Run the accompanying gen.gsp program to generate the classes required for this test
//!!
public class ClassProfileTest extends TestClass
{
  private static final long KBYTES = 1024;
  private static final long MBYTES = KBYTES * KBYTES;


  @Override
  public void beforeTestMethod()
  {
    super.beforeTestMethod();
    TypeSystem.getAllTypeNames();
    IType gsClass = TypeSystem.getByFullNameIfValid( "gw.internal.gosu.parser.classTests.gwtest.profile.Class_Empty" );
    gsClass.isValid();
    assertNotNull( gsClass );
  }

  public void test() {

  }

//!! Uncomment Me to run this test, but not before running .\gen.gsp to gen class files.  
//  public void testClassProfile() throws Exception
//  {
//    report(
//      new Callable()
//      {
//        public Object call() throws Exception
//        {
//          List<IGosuClass> classes = reportLoadTime();
//          reportParseTime( classes );
//          reportCompileTime( classes );
//          reportInstanceTime( classes );
//
//          System.out.println( "Total: " );
//          return null;
//        }
//      } );
//  }

  private Object report( Callable r ) throws Exception
  {
    long lMemBefore = calcUsedMemory();
    long lPgBefore = calcUsedPermGen();
    long lBegin = System.currentTimeMillis();

    Object ret = r.call();

    double dTotal = System.currentTimeMillis() - lBegin;
    System.out.println( "Time used: " + dTotal/1000 + " s" );
    System.out.println( "Memory Used: " + formatMemory( calcUsedMemory() - lMemBefore ) );
    System.out.println( "PermGen Used: " + formatMemory( calcUsedPermGen() - lPgBefore ) );
    System.out.println( "" );

    return ret;
  }

  private void reportParseTime( final List<IGosuClass> classes ) throws Exception
  {
    System.out.println( "Parse Time: " );
    report(
      new Callable()
      {
        public Object call()
        {
          for( IGosuClass gsClass : classes )
          {
            assertTrue( gsClass.isValid() );
          }
          return null;
        }
      } );
  }

  private List<IGosuClass> reportLoadTime() throws Exception
  {
    System.out.println( "Load Time: " );
    //noinspection unchecked
    return (List<IGosuClass>)report(
      new Callable<List<IGosuClass>>()
      {
        public List<IGosuClass> call()
        {
          List<IGosuClass> classes = new ArrayList<IGosuClass>( 10000 );
          for( int i = 0; i < 10000; i++ )
          {
            IGosuClass gsClass = (IGosuClass)TypeSystem.getByFullNameIfValid( "gw.internal.gosu.parser.classTests.gwtest.profile.lots.Class_" + i );
            assertNotNull( gsClass );
            classes.add( gsClass );
          }
          assertEquals( 10000, classes.size() );
          return classes;
        }
      } );
   }

  private void reportCompileTime( final List<IGosuClass> classes ) throws Exception
  {
    System.out.println( "Compile Time: " );
    report(
      new Callable()
      {
        public Object call()
        {
          for( IGosuClass gsClass : classes )
          {
            gsClass.getBackingClass();
          }
          return null;
        }
      } );
  }


  private void reportInstanceTime( final List<IGosuClass> classes ) throws Exception
  {

    System.out.println( "Instance Time: " );
    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection"})
    final List<Object> objs = new ArrayList<Object>( classes.size() );
    report(
      new Callable()
      {
        public Object call() throws Exception
        {
          for( IGosuClass gsClass : classes )
          {
            objs.add( gsClass.getBackingClass().newInstance() );
          }
          return null;
        }
      } );
  }

  private long calcUsedMemory()
  {
    System.gc();
    return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
  }
  
  public static String formatMemory(long mem) {
    if (mem == 0) {
      return "0";
    }
    long mbytes = (mem / MBYTES);
    long kbytes = ((mem % MBYTES) / KBYTES);
    kbytes = (kbytes * 1000) / KBYTES; // turn it into (0-999) as oppose to (0-1023)
    return mbytes + "." + (kbytes < 100 ? "0" : "") + (kbytes < 10 ? "0" : "") + kbytes + " MB";
  }

  private long calcUsedPermGen()
  {
    //return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();
    for( MemoryPoolMXBean item : ManagementFactory.getMemoryPoolMXBeans() )
    {
      if( item.getName().equals( "Perm Gen" ) )
      {
        return item.getUsage().getUsed();
      }
    }

    return -1;
  }
}
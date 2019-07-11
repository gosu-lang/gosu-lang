package gw.lang;

import gw.lang.init.ClasspathToGosuPathEntryUtil;
import gw.lang.init.GosuInitialization;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.BytecodeOptions;
import gw.test.Suite;
import gw.test.TestEnvironment;
import junit.framework.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Typical JVM args:
 *   -Dgw.tests.skip.knownbreak=true
 *   -DcheckedArithmetic=true
 */
public class GosuScratchSuite extends Suite
{
  public static Test suite()
  {
    BytecodeOptions.enableAggressiveVerification();
    return new GosuScratchSuite()
            .withTestEnvironment( new ScratchTestEnvironment() )
      .withTest( "gw.specContrib.ProcessSpecContribTest" );
//      .withTest( "gw.specContrib.statements.usesStatement.StaticImportMiscTest" );
    //  .withTest( "gw.specContrib.rexpod.CoreIterableParallelizationTest" );
    //  .withTest( "gw.internal.gosu.parser.classTests.gwtest.dynamic.JsonTest" );
  }

  private static class ScratchTestEnvironment extends TestEnvironment
  {
    @Override
    public void initializeTypeSystem()
    {
      List<File> classpath = constructClasspathFromSystemClasspath();
      for( File file : new ArrayList<File>(classpath) )
      {
        if( file.getName().endsWith( "classes" ) )
        {
          classpath.add( new File( file.getParentFile(), "test-classes" ) );
        }
      }
      GosuInitialization.instance( TypeSystem.getExecutionEnvironment() )
        .initializeRuntime( ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries( classpath ) );
    }
  }
}

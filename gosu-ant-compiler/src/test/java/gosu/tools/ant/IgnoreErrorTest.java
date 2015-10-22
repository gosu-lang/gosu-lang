package gosu.tools.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildFileTest;

import java.io.File;

/**
 * Note that BuildFileTest requires JUnit 3-style tests
 */
public class IgnoreErrorTest extends BuildFileTest {

  private final String BASEDIR = "src/test/resources/ignore_error/";
  
  public IgnoreErrorTest( String name ) {
    super(name);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    configureProject(BASEDIR + "failOnError.xml");
    executeTarget("setUp");
  }
  
  @Override
  public void tearDown() throws Exception {
      super.tearDown(); //implicitly calls executeTarget("tearDown")
  }
  
  public void testGosuc() {
    
    try {
      executeTarget("compile");
    } catch(BuildException e) {
      System.out.println(e.getMessage());
      fail("BuildException should not be thrown since failOnError=\"false\"");
    } finally {

      System.out.println("--- Dumping log ---");
      System.out.println(getLog());
      System.out.println("--- Done dumping log ---");

      assertLogContaining("Gosu compilation completed with 1 error:");
      assertLogContaining("Ignoring compilation failure(s) as 'failOnError' was set to false");
      assertFalse(new File(BASEDIR + "dest/IntentionalError.class").exists());
    }
  }
 
}

package gosu.tools.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildFileTest;

import java.io.File;

/**
 * Note that BuildFileTest requires JUnit 3-style tests
 */
public class ErrorTest extends BuildFileTest {

  private final String BASEDIR = "src/test/resources/error/";
  
  public ErrorTest( String name ) {
    super(name);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    configureProject(BASEDIR + "error.xml");
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
    } finally {

      System.out.println("--- Dumping log ---");
      System.out.println(getLog());
      System.out.println("--- Done dumping log ---");

      assertLogContaining("Gosu compilation completed with 1 error:");
      assertFalse(new File(BASEDIR + "dest/IntentionalError.class").exists());
    }
  }
 
}

package gosu.tools.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildFileTest;

import java.io.File;

/**
 * Note that BuildFileTest requires JUnit 3-style tests
 */
public class WarningAndErrorTest extends BuildFileTest {

  private final String BASEDIR = "src/test/resources/warning_and_error/";
  
  public WarningAndErrorTest( String name ) {
    super(name);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    configureProject(BASEDIR + "build.xml");
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

      assertLogContaining("Gosu compilation completed with 1 warning and 1 error");
      assertTrue(new File(BASEDIR + "dest/IntentionalWarning.class").exists());
      assertFalse(new File(BASEDIR + "dest/IntentionalError.class").exists());
    }
  }
 
}

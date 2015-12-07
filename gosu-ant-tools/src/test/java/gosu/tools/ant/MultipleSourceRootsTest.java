package gosu.tools.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildFileTest;

import java.io.File;

public class MultipleSourceRootsTest extends BuildFileTest {

  private final String BASEDIR = "src/test/resources/multiple_source_roots/";
  
  public MultipleSourceRootsTest( String name ) {
    super(name);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    configureProject(BASEDIR + "multiple.xml");
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
      fail();
    } finally {

      System.out.println("--- Dumping log ---");
      System.out.println(getLog());
      System.out.println("--- Done dumping log ---");

      assertLogContaining("Gosu compilation completed successfully.");
      assertTrue(new File(BASEDIR + "dest/Parent.class").exists());
      assertTrue(new File(BASEDIR + "dest/Child.class").exists());

    }
  }
}

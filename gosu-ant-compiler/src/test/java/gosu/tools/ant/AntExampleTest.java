package gosu.tools.ant;

import org.apache.tools.ant.BuildFileTest;

/**
 * Note that BuildFileTest requires JUnit 3-style tests
 * 
 * This is just a sanity test to understand how BuildFileTest works
 */
public class AntExampleTest extends BuildFileTest {

  private final String BASEDIR = "src/test/resources/echo/";
  
  public AntExampleTest( String name ) {
    super(name);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    configureProject(BASEDIR + "sayhi.xml");
  }
  
  @Override
  public void tearDown() throws Exception {
    super.tearDown(); //implicitly calls executeTarget("tearDown")
  }
  
  public void testMySanity() {
    executeTarget("stimulus");

    System.out.println("--- Dumping log ---");
    System.out.println(getLog());
    System.out.println("--- Done dumping log ---");
    
    assertLogContaining("response!");
  }
 
}

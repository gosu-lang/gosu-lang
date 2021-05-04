package gosu.tools.ant.gosudoc;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildFileTest;

import java.io.File;

/**
 * Note that BuildFileTest requires JUnit 3-style tests
 *
 * This is just a sanity test to kick-off GosuDoc via ant
 */
public class GosuDocBasicTest extends BuildFileTest {

  private final String BASEDIR = "src/test/resources/gosudoc/";
  private final String SLASH = File.separator;

  public GosuDocBasicTest( String name ) {
    super(name);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    configureProject(BASEDIR + "build.xml", 1);
  }

  @Override
  public void tearDown() throws Exception {
    super.tearDown(); //implicitly calls executeTarget("tearDown"), if it exists
  }

  public void testSimpleGosudocGeneration() {
    try {
      executeTarget("gen-doc");
    } catch(BuildException e) {
      fail(e.getMessage());
    }

    //uncomment below for debugging purposes
//    System.out.println("--- Dumping full log ---");
//    System.out.println(getFullLog());
//    System.out.println("--- Done dumping full log ---");
//    System.out.println();
//    System.out.println("--- Dumping output ---");
//    System.out.println(getOutput());
//    System.out.println("--- Done dumping output ---");
//    System.out.println();
//    System.out.println("--- Dumping error ---");
//    System.out.println(getError());
//    System.out.println("--- Done dumping error ---");
    
    assertOutputContaining("Generating Documentation");
    assertOutputContaining("gosudoc.src.hi.MyPogo - document : true"); //only works when verbose="true"
    assertOutputContaining("src" + SLASH + "hi" + SLASH + "gosudoc.src.hi.MyPogo.html...");
    assertEquals("System.err should be empty", "", getError());
  }

}

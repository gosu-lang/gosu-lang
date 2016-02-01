package gosu.tools.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Note that BuildFileTest requires JUnit 3-style tests
 */
public class ForceCompileTest extends BuildFileTest {

  private final String BASEDIR = "src/test/resources/force/";
  private static BasicFileAttributes fooOriginalAttributes, barOriginalTimestamp;

  public ForceCompileTest( String name ) {
    super(name);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    configureProject(BASEDIR + "force.xml");
    executeTarget("setUp");
    
    try {
      executeTarget("compile");
    } catch(BuildException e) {
      System.out.println("--- FAILED: Dumping full log ---");
      System.out.println(getFullLog());
      System.out.println("--- Done dumping full log ---");
      System.out.println();
      System.out.println("--- FAILED: Dumping output ---");
      System.out.println(getOutput());
      System.out.println("--- Done dumping output ---");
      System.out.println();
      System.out.println("--- FAILED: Dumping error ---");
      System.out.println(getError());
      System.out.println("--- Done dumping error ---");
      fail(e.getMessage());
    }

    System.out.println("--- Dumping log ---");
    System.out.println(getLog());
    System.out.println("--- Done dumping log ---");

    assertLogContaining("Relying on ant's SourceFileScanner, which only looks at timestamps.  If broken references result, try setting option 'force' to true.");
    assertLogContaining("Gosu compilation completed successfully.");
    File fooBytecode = new File(BASEDIR + "dest/Foo.class");
    File barBytecode = new File(BASEDIR + "dest/Bar.class");
    assertTrue(fooBytecode.exists());
    assertTrue(barBytecode.exists());
    BasicFileAttributes fooBytecodeAttributes = Files.readAttributes(fooBytecode.toPath(), BasicFileAttributes.class);
    BasicFileAttributes barBytecodeAttributes = Files.readAttributes(barBytecode.toPath(), BasicFileAttributes.class);
    fooOriginalAttributes = fooBytecodeAttributes;
    barOriginalTimestamp = barBytecodeAttributes;

    //sleep the minimum number of milliseconds that the OS requires to recognize different file timestamps
    long sleeptime = FileUtils.getFileUtils().getFileTimestampGranularity();
    System.out.println("--- sleeping for " + sleeptime + "ms ---");
    Thread.sleep(sleeptime);
  }

  @Override
  public void tearDown() throws Exception {
    super.tearDown(); //implicitly calls executeTarget("tearDown")
  }

  /**
   * Set 'force' to the default value (true)
   * Then test that both Foo and Bar are recompiled, i.e. have more recent timestamps
   * @throws IOException
   */
  public void testForceCompileIsTrue() throws IOException {

    Target compileTarget = project.getTargets().get("compile");
    Task gosucTask = null;
    for(Task task : compileTarget.getTasks()) {
      if(task.getTaskName().equals("gosuc")) {
        gosucTask = task;
        continue;
      }
    }
    assertNotNull(gosucTask);
    gosucTask.getRuntimeConfigurableWrapper().removeAttribute("force"); // effectively reverts to true
    //assert that force is not present, therefore is running as the default (true)
    assertNull(gosucTask.getRuntimeConfigurableWrapper().getAttributeMap().get("force"));
    
    try {
      executeTarget("compile");
    } catch(BuildException e) {
      fail(e.getMessage());
    }

    System.out.println("--- Dumping log ---");
    System.out.println(getLog());
    System.out.println("--- Done dumping log ---");

    assertLogContaining("Gosu compilation completed successfully.");
    assertLogNotContaining("Relying on ant's SourceFileScanner, which only looks at timestamps.  If broken references result, try setting option 'force' to true.");

    File fooBytecode = new File(BASEDIR + "dest/Foo.class");
    File barBytecode = new File(BASEDIR + "dest/Bar.class");
    assertTrue(fooBytecode.exists());
    assertTrue(barBytecode.exists());
    BasicFileAttributes fooBytecodeAttributes = Files.readAttributes(fooBytecode.toPath(), BasicFileAttributes.class);
    BasicFileAttributes barBytecodeAttributes = Files.readAttributes(barBytecode.toPath(), BasicFileAttributes.class);
    //here we compare last modified to the original creation dates, because the filesystem did not destroy and recreate the files
    assertTrue(fooBytecodeAttributes.lastModifiedTime().toMillis() > fooOriginalAttributes.creationTime().toMillis());
    assertTrue(barBytecodeAttributes.lastModifiedTime().toMillis() > barOriginalTimestamp.creationTime().toMillis());

  }

  /**
   * Set 'force' to false, then delete Bar.class while leaving Foo untouched
   * Then verify that Foo is not recompiled, but Bar is
   * @throws IOException
   */
  public void testForceCompileIsFalse() throws IOException {

    Target compileTarget = project.getTargets().get("compile");
    Task gosucTask = null;
    for(Task task : compileTarget.getTasks()) {
      if(task.getTaskName().equals("gosuc")) {
        gosucTask = task;
        continue;
      }
    }
    assertNotNull(gosucTask);
    //assert that force is set to false
    assertEquals("false", (String) gosucTask.getRuntimeConfigurableWrapper().getAttributeMap().get("force"));

    assertTrue(new File(BASEDIR + "dest/Bar.class").delete()); //delete Bar.class
    assertFalse(new File(BASEDIR + "dest/Bar.class").exists());

    try {
      executeTarget("compile");
    } catch(BuildException e) {
      fail(e.getMessage());
    }
    
    System.out.println("--- Dumping log ---");
    System.out.println(getLog());
    System.out.println("--- Done dumping log ---");

    assertLogContaining("Gosu compilation completed successfully.");
    assertLogContaining("Relying on ant's SourceFileScanner, which only looks at timestamps.  If broken references result, try setting option 'force' to true.");

    // test that ONLY Bar is recompiled (since Bar.class was removed)
    File fooBytecode = new File(BASEDIR + "dest/Foo.class");
    File barBytecode = new File(BASEDIR + "dest/Bar.class");
    BasicFileAttributes fooBytecodeAttributes = Files.readAttributes(fooBytecode.toPath(), BasicFileAttributes.class);
    BasicFileAttributes barBytecodeAttributes = Files.readAttributes(barBytecode.toPath(), BasicFileAttributes.class);
    assertTrue(fooBytecode.exists());
    assertTrue(barBytecode.exists());

    System.out.println("barBytecodeAttributes.creationTime(): " + barBytecodeAttributes.creationTime().toMillis());
    System.out.println("barOriginalTimestamp                : " + barOriginalTimestamp.creationTime().toMillis());

    assertTrue("critical: Foo.class should not have been touched; the creation times should be identical", fooBytecodeAttributes.creationTime().toMillis() == fooOriginalAttributes.creationTime().toMillis());
    assertTrue("critical: Foo.class should not have been touched; the modification times should be identical", fooBytecodeAttributes.lastModifiedTime().toMillis() == fooOriginalAttributes.lastModifiedTime().toMillis());
    if(!Os.isFamily(Os.FAMILY_WINDOWS)) { //FIXME skip this test on Windows
      assertTrue("Bar should have been recompiled; the creation time will have changed", barBytecodeAttributes.creationTime().toMillis() > barOriginalTimestamp.creationTime().toMillis());
    }
  }


}

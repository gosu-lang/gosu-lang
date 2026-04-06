package gw.internal.gosu.parser;

import gw.lang.gosuc.GosucDependency;
import gw.lang.gosuc.GosucModule;

import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests the strategy-selection logic used by ExecutionEnvironment.reinitializeCompiler():
 * - GosucModule equality (no-op detection)
 * - Classpath superset/prefix detection (augment vs full rebuild)
 */
public class ReinitStrategyTest
{
  // --- GosucModule equality (Strategy 1: no-op) ---

  @Test
  public void testIdenticalModulesAreEqual()
  {
    GosucModule a = makeModule( sources( "src" ), classpath( "a.jar", "b.jar" ), "out" );
    GosucModule b = makeModule( sources( "src" ), classpath( "a.jar", "b.jar" ), "out" );
    assertEquals( "Identical modules should be equal", a, b );
    assertEquals( "Identical modules should have same hashCode", a.hashCode(), b.hashCode() );
  }

  @Test
  public void testDifferentClasspathNotEqual()
  {
    GosucModule a = makeModule( sources( "src" ), classpath( "a.jar" ), "out" );
    GosucModule b = makeModule( sources( "src" ), classpath( "a.jar", "b.jar" ), "out" );
    assertNotEquals( "Modules with different classpaths should not be equal", a, b );
  }

  @Test
  public void testDifferentSourcesNotEqual()
  {
    GosucModule a = makeModule( sources( "src1" ), classpath( "a.jar" ), "out" );
    GosucModule b = makeModule( sources( "src2" ), classpath( "a.jar" ), "out" );
    assertNotEquals( "Modules with different sources should not be equal", a, b );
  }

  @Test
  public void testDifferentOutputNotEqual()
  {
    GosucModule a = makeModule( sources( "src" ), classpath( "a.jar" ), "out1" );
    GosucModule b = makeModule( sources( "src" ), classpath( "a.jar" ), "out2" );
    assertNotEquals( "Modules with different output paths should not be equal", a, b );
  }

  // --- Classpath superset detection (Strategy 2: augment vs Strategy 4: full rebuild) ---

  @Test
  public void testClasspathAppendIsSuperset()
  {
    GosucModule old = makeModule( sources( "src" ), classpath( "a.jar", "b.jar" ), "out" );
    GosucModule updated = makeModule( sources( "src" ), classpath( "a.jar", "b.jar", "c.jar" ), "out" );

    List<String> oldCp = old.getClasspath();
    List<String> newCp = updated.getClasspath();

    boolean isSuperset = newCp.size() >= oldCp.size()
        && newCp.subList( 0, oldCp.size() ).equals( oldCp );

    assertTrue( "Appended classpath should be detected as superset", isSuperset );

    List<String> added = newCp.subList( oldCp.size(), newCp.size() );
    assertEquals( "Should detect exactly one added entry", 1, added.size() );
  }

  @Test
  public void testClasspathReorderIsNotSuperset()
  {
    GosucModule old = makeModule( sources( "src" ), classpath( "a.jar", "b.jar" ), "out" );
    GosucModule updated = makeModule( sources( "src" ), classpath( "b.jar", "a.jar" ), "out" );

    List<String> oldCp = old.getClasspath();
    List<String> newCp = updated.getClasspath();

    boolean isSuperset = newCp.size() >= oldCp.size()
        && newCp.subList( 0, oldCp.size() ).equals( oldCp );

    assertFalse( "Reordered classpath should not be detected as superset", isSuperset );
  }

  @Test
  public void testClasspathRemovalIsNotSuperset()
  {
    GosucModule old = makeModule( sources( "src" ), classpath( "a.jar", "b.jar", "c.jar" ), "out" );
    GosucModule updated = makeModule( sources( "src" ), classpath( "a.jar", "c.jar" ), "out" );

    List<String> oldCp = old.getClasspath();
    List<String> newCp = updated.getClasspath();

    boolean isSuperset = newCp.size() >= oldCp.size()
        && newCp.subList( 0, oldCp.size() ).equals( oldCp );

    assertFalse( "Classpath with removal should not be detected as superset", isSuperset );
  }

  @Test
  public void testIdenticalClasspathIsSupersetWithNoAdditions()
  {
    GosucModule old = makeModule( sources( "src" ), classpath( "a.jar", "b.jar" ), "out" );
    GosucModule updated = makeModule( sources( "src2" ), classpath( "a.jar", "b.jar" ), "out2" );

    List<String> oldCp = old.getClasspath();
    List<String> newCp = updated.getClasspath();

    boolean isSuperset = newCp.size() >= oldCp.size()
        && newCp.subList( 0, oldCp.size() ).equals( oldCp );

    assertTrue( "Identical classpath should be detected as superset (zero additions)", isSuperset );

    List<String> added = newCp.subList( oldCp.size(), newCp.size() );
    assertTrue( "No entries should be added", added.isEmpty() );
  }

  @Test
  public void testMultipleAdditionsDetected()
  {
    GosucModule old = makeModule( sources( "src" ), classpath( "a.jar" ), "out" );
    GosucModule updated = makeModule( sources( "src" ), classpath( "a.jar", "b.jar", "c.jar", "d.jar" ), "out" );

    List<String> oldCp = old.getClasspath();
    List<String> newCp = updated.getClasspath();

    boolean isSuperset = newCp.size() >= oldCp.size()
        && newCp.subList( 0, oldCp.size() ).equals( oldCp );

    assertTrue( "Multiple appends should be detected as superset", isSuperset );

    List<String> added = newCp.subList( oldCp.size(), newCp.size() );
    assertEquals( "Should detect three added entries", 3, added.size() );
  }

  // --- Helpers ---

  private static GosucModule makeModule( List<String> sources, List<String> classpath, String output )
  {
    return new GosucModule(
        "TestModule",
        sources,
        classpath,
        Collections.emptyList(),
        output,
        Collections.<GosucDependency>emptyList(),
        Collections.<String>emptyList()
    );
  }

  private static List<String> sources( String... paths )
  {
    return toAbsolutePaths( paths );
  }

  private static List<String> classpath( String... paths )
  {
    return toAbsolutePaths( paths );
  }

  /**
   * GosucModule converts paths to URIs via File, so we need real-looking absolute paths.
   */
  private static List<String> toAbsolutePaths( String... names )
  {
    String tmpDir = System.getProperty( "java.io.tmpdir" );
    String[] paths = new String[names.length];
    for( int i = 0; i < names.length; i++ )
    {
      paths[i] = new File( tmpDir, names[i] ).getAbsolutePath();
    }
    return Arrays.asList( paths );
  }
}

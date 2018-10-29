package org.gosulang.plexus.compiler.gosu;

import org.codehaus.plexus.compiler.AbstractCompilerTest;
import org.codehaus.plexus.compiler.Compiler;
import org.codehaus.plexus.compiler.CompilerConfiguration;
import org.codehaus.plexus.compiler.CompilerMessage;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class GosuCompilerTest extends AbstractCompilerTest {

  @Override
  protected String getRoleHint() {
    return "gosuc";
  }

  @Override
  protected int expectedWarnings() {
    return 1; // Person.gs has one intentional warning
  }

  @Override
  protected int expectedErrors() {
    return 1; // Bad.gs has one intentional error
  }

  protected Collection<String> expectedOutputFiles()
  {
    return Arrays.asList(
        "org/gosulang/foo/ExternalDeps.class",
        "org/gosulang/foo/FromJava.class",
        "org/gosulang/foo/FromJavaSubclass.class",
        "org/gosulang/foo/MyGenericGosu.class",
        "org/gosulang/foo/MyGenericGosu.gs",
        "org/gosulang/foo/MyGosu.class",
        "org/gosulang/foo/MyGosu.gs",
        "org/gosulang/foo/Person.class",
        "org/gosulang/foo/PersonEnhancement.class",
        "org/gosulang/foo/ExternalDeps.gs",
        "org/gosulang/foo/Person.gs",
        "org/gosulang/foo/PersonEnhancement.gsx");
  }

  @Override
  public void testCompilingSources() throws Exception
  {
    List<CompilerMessage> messages = new ArrayList<>();
    Collection<String> files = new TreeSet<>();

    for ( CompilerConfiguration compilerConfig : getCompilerConfigurations() )
    {
      File outputDir = new File( compilerConfig.getOutputLocation() );

      org.codehaus.plexus.compiler.Compiler compiler = (Compiler) lookup( Compiler.ROLE, getRoleHint() );

      messages.addAll( compiler.performCompile( compilerConfig ).getCompilerMessages() );

      if ( outputDir.isDirectory() )
      {
        files.addAll( normalizePaths( FileUtils.getFileNames(outputDir, null, null, false) ) );
      }
    }

    int numCompilerErrors = compilerErrorCount( messages );

    int numCompilerWarnings = messages.size() - numCompilerErrors;

    if ( expectedErrors() != numCompilerErrors )
    {
      System.out.println( numCompilerErrors + " error(s) found:" );
      for ( CompilerMessage error : messages )
      {
        if ( !error.isError() )
        {
          continue;
        }

        System.out.println( "----" );
        System.out.println( error.getFile() );
        System.out.println( error.getMessage() );
        System.out.println( "----" );
      }

      assertEquals( "Wrong number of compilation errors.", expectedErrors(), numCompilerErrors );
    }

    if ( expectedWarnings() != numCompilerWarnings )
    {
      System.out.println( numCompilerWarnings + " warning(s) found:" );
      for ( CompilerMessage warning : messages )
      {
        if ( warning.isError() )
        {
          continue;
        }

        System.out.println( "----" );
        System.out.println( warning.getFile() );
        System.out.println( warning.getMessage() );
        System.out.println( "----" );
      }

      assertEquals( "Wrong number of compilation warnings.", expectedWarnings(), numCompilerWarnings );
    }

    assertEquals(new TreeSet<>(normalizePaths(expectedOutputFiles())), files);
  }

  private List<CompilerConfiguration> getCompilerConfigurations() throws Exception {
    String sourceDir = getBasedir() + "/src/test-input/src/main/gosu";

    @SuppressWarnings("unchecked") List<String> filenames =
        FileUtils.getFileNames(new File(sourceDir), "**/*.gs,**/*.gsx,**/*.gst,**/*.java", null, false, true);
    Collections.sort(filenames);

    List<CompilerConfiguration> compilerConfigurations = new ArrayList<>();

    int index = 0;
    for (Iterator<String> it = filenames.iterator(); it.hasNext(); index++) {
      String filename = it.next();

      CompilerConfiguration compilerConfig = new CompilerConfiguration();

      compilerConfig.setClasspathEntries(getClasspath());

      compilerConfig.addSourceLocation(sourceDir);

      compilerConfig.addInclude(filename);

      compilerConfig.setSourceFiles(Collections.singleton(new File(sourceDir + File.separator + filename)));

      compilerConfig.setOutputLocation(getBasedir() + "/target/" + getRoleHint() + "/classes-" + index);

      FileUtils.deleteDirectory(compilerConfig.getOutputLocation());

      compilerConfig.setFork(false);

      compilerConfig.setVerbose(true);

      compilerConfigurations.add(compilerConfig);

    }

    return compilerConfigurations;
  }

  private List<String> normalizePaths( Collection<String> relativePaths ) {
    List<String> normalizedPaths = new ArrayList<>();
    for ( String relativePath : relativePaths ) {
      normalizedPaths.add( relativePath.replace( File.separatorChar, '/' ) );
    }
    return normalizedPaths;
  }


}

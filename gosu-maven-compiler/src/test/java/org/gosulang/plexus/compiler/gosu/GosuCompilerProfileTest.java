package org.gosulang.plexus.compiler.gosu;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.codehaus.plexus.compiler.AbstractCompilerTest;
import org.codehaus.plexus.compiler.Compiler;
import org.codehaus.plexus.compiler.CompilerConfiguration;
import org.codehaus.plexus.compiler.CompilerMessage;
import org.codehaus.plexus.util.FileUtils;

public class GosuCompilerProfileTest extends AbstractCompilerTest
{

  @Override
  protected String getRoleHint()
  {
    return "gosuc";
  }

  protected Collection<String> expectedOutputFiles()
  {
    return Arrays.asList(
      "life/AbstractHandler.gs",
      "life/Board.gs",
      "life/BoardFrame.gs",
      "life/Cell.gs",
      "life/CellHandler.gs",
      "life/ColorScheme.gs",
      "life/ControlBoard.gs",
      "life/DrawHandler.gs",
      "life/GameOfLife.gsp",
      "life/InfinitiCell.gs",
      "life/LifeModel.gs",
      "life/Populator.gs",
      "life/ScrollHandler.gs",
      "life/SelectionHandler.gs",
      "life/StatusBoard.gs",
      "life/util/ModalEventQueue.gs",
      "life/ZoomHandler.gs",

      "life/AbstractHandler.class",
      "life/Board.class",
      "life/BoardFrame.class",
      "life/Cell.class",
      "life/CellHandler.class",
      "life/ColorScheme.class",
      "life/ControlBoard.class",
      "life/DrawHandler.class",
      "life/GameOfLife.class",
      "life/InfinitiCell.class",
      "life/LifeModel.class",
      "life/Populator.class",
      "life/ScrollHandler.class",
      "life/SelectionHandler.class",
      "life/StatusBoard.class",
      "life/util/ModalEventQueue.class",
      "life/ZoomHandler.class"

//      "life/images/color.png",
//      "life/images/density.png",
//      "life/images/frequency.png",
//      "life/images/gol.png",
//      "life/images/infiniti.png",
//      "life/images/parallel.png",
//      "life/images/pencil.png",
//      "life/images/power.png",
//      "life/images/resolution.png",
//      "life/images/scroll.png",
//      "life/images/select.png"
    );
  }

  @Override
  public void testCompilingSources() throws Exception
  {
    System.out.println( "### Uncomment ###");
//    while( true )
//    {
//      doCompile();
//    }
  }

  private void doCompile() throws Exception
  {
    CompilerConfiguration compilerConfig = getCompilerConfigurations();
    File outputDir = new File( compilerConfig.getOutputLocation() );
    Compiler compiler = (Compiler)lookup( Compiler.ROLE, getRoleHint() );
    List<CompilerMessage> messages = new ArrayList<>( compiler.performCompile( compilerConfig ).getCompilerMessages() );
    Collection<String> files = new TreeSet<>();
    if( outputDir.isDirectory() )
    {
      files.addAll( normalizePaths( FileUtils.getFileNames( outputDir, null, null, false ) ) );
    }

    int numCompilerErrors = compilerErrorCount( messages );

//    int numCompilerWarnings = messages.size() - numCompilerErrors;

    if( expectedErrors() != numCompilerErrors )
    {
      System.out.println( numCompilerErrors + " error(s) found:" );
      for( CompilerMessage error: messages )
      {
        if( !error.isError() )
        {
          continue;
        }

        System.out.println( "----" );
        System.out.println( error.getFile() );
        System.out.println( error.getMessage() );
        System.out.println( "----" );
      }

      assertEquals( "Wrong number of compilation errors.", 0, numCompilerErrors );
    }

//    if ( expectedWarnings() != numCompilerWarnings )
//    {
//      System.out.println( numCompilerWarnings + " warning(s) found:" );
//      for ( CompilerMessage warning : messages )
//      {
//        if ( warning.isError() )
//        {
//          continue;
//        }
//
//        System.out.println( "----" );
//        System.out.println( warning.getFile() );
//        System.out.println( warning.getMessage() );
//        System.out.println( "----" );
//      }
//
//      assertEquals( "Wrong number of compilation warnings.", expectedWarnings(), numCompilerWarnings );
//    }

    assertTrue( files.containsAll( expectedOutputFiles() ) );
  }

  private CompilerConfiguration getCompilerConfigurations() throws Exception
  {
    String sourceDir = getBasedir() + "/src/profile-test-input/src/main/gosu";

    @SuppressWarnings("unchecked") List<String> filenames =
      FileUtils.getFileNames( new File( sourceDir ), "**/*.gs,**/*.gsp,**/*.gsx,**/*.gst,**/*.java", null, false, true );
    Collections.sort( filenames );

    List<CompilerConfiguration> compilerConfigurations = new ArrayList<>();

    CompilerConfiguration compilerConfig = new CompilerConfiguration();
    compilerConfig.setClasspathEntries( getClasspath() );
    compilerConfig.addSourceLocation( sourceDir );

    Set<File> files = new HashSet<>();
    for( String filename: filenames )
    {
      files.add( new File( sourceDir + File.separator + filename ) );
    }
    compilerConfig.setSourceFiles( files );

    compilerConfig.setOutputLocation( getBasedir() + "/target/" + getRoleHint() + "/classes-profile-test" );
    FileUtils.deleteDirectory( compilerConfig.getOutputLocation() );
    compilerConfig.setFork( false );
    compilerConfig.setVerbose( true );
    return compilerConfig;
  }

  private List<String> normalizePaths( Collection<String> relativePaths )
  {
    List<String> normalizedPaths = new ArrayList<>();
    for( String relativePath: relativePaths )
    {
      normalizedPaths.add( relativePath.replace( File.separatorChar, '/' ) );
    }
    return normalizedPaths;
  }


}

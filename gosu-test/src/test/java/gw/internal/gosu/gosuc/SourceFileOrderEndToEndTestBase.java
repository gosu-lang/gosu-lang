/*
 * Copyright 2026 Guidewire Software, Inc.
 */

package gw.internal.gosu.gosuc;

import gw.internal.ext.org.objectweb.asm.ClassReader;
import gw.internal.ext.org.objectweb.asm.util.TraceClassVisitor;
import gw.lang.gosuc.cli.CommandLineCompiler;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Base class of the end-to-end tests that the class file gosuc writes for a source file does not depend on the
 * order in which the source files are handed to the compiler. A test writes actual Gosu source files, compiles
 * them once per order with the command line compiler, and compares the class files of the two compiles.
 */
public abstract class SourceFileOrderEndToEndTestBase
{
  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();

  protected File createSourceFile( String relativePath, String content ) throws IOException
  {
    Path file = sourceDir().resolve( relativePath );
    Files.createDirectories( file.getParent() );
    Files.write( file, content.getBytes( StandardCharsets.UTF_8 ) );
    return file.toFile();
  }

  private Path sourceDir()
  {
    return tempFolder.getRoot().toPath().resolve( "src" );
  }

  /**
   * Runs gosuc on the source files, in the order given, and returns the directory the class files were written
   * to. Every call is a full compile into a directory of its own.
   */
  protected Path compile( String outputDirName, File... sourceFiles ) throws IOException
  {
    Path outputDir = tempFolder.getRoot().toPath().resolve( outputDirName );
    Files.createDirectories( outputDir );

    List<String> args = new ArrayList<>();
    args.add( "-classpath" );
    args.add( System.getProperty( "java.class.path" ) );
    args.add( "-d" );
    args.add( outputDir.toFile().getAbsolutePath() );
    args.add( "-sourcepath" );
    args.add( sourceDir().toFile().getAbsolutePath() );
    for( File sourceFile : sourceFiles )
    {
      args.add( sourceFile.getAbsolutePath() );
    }

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    PrintStream originalErr = System.err;
    int exitCode;
    try
    {
      PrintStream captured = new PrintStream( output );
      System.setOut( captured );
      System.setErr( captured );
      exitCode = CommandLineCompiler.runCompiler( args.toArray( new String[0] ) );
    }
    finally
    {
      System.setOut( originalOut );
      System.setErr( originalErr );
    }
    assertEquals( "gosuc failed compiling " + outputDirName + ":\n" + output, 0, exitCode );
    return outputDir;
  }

  /**
   * Runs the main function of a class that gosuc compiled and returns what it printed. The class runs in a JVM
   * of its own: a compiled Gosu class starts the Gosu runtime when it is loaded, and the compiler has shut the
   * type system of this JVM down.
   */
  protected String run( Path classDir, String className ) throws IOException, InterruptedException
  {
    File output = tempFolder.newFile();
    File errorOutput = tempFolder.newFile();
    Process process = new ProcessBuilder( new File( System.getProperty( "java.home" ), "bin/java" ).getPath(),
                                          "-classpath",
                                          classDir + File.pathSeparator + System.getProperty( "java.class.path" ),
                                          className )
      .redirectOutput( output )
      .redirectError( errorOutput )
      .start();
    int exitCode = process.waitFor();
    assertEquals( className + " of " + classDir.getFileName() + " failed:\n" + Files.readString( errorOutput.toPath() ),
                  0, exitCode );
    return Files.readString( output.toPath() ).trim();
  }

  /**
   * The class files are compared as text first, so that a failure shows what differs.
   */
  protected static void assertSameClassFile( String relativePath, Path expectedDir, Path actualDir ) throws IOException
  {
    byte[] expected = Files.readAllBytes( expectedDir.resolve( relativePath ) );
    byte[] actual = Files.readAllBytes( actualDir.resolve( relativePath ) );
    String message = relativePath + " of " + expectedDir.getFileName() + " (expected) and of " +
                     actualDir.getFileName() + " (actual)";
    assertEquals( message, classFileAsText( expected ), classFileAsText( actual ) );
    assertArrayEquals( message, expected, actual );
  }

  private static String classFileAsText( byte[] classBytes )
  {
    StringWriter text = new StringWriter();
    new ClassReader( classBytes ).accept( new TraceClassVisitor( new PrintWriter( text ) ), 0 );
    return text.toString();
  }
}

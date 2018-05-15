package org.gosulang.plexus.compiler.gosu;

import org.codehaus.plexus.compiler.CompilerConfiguration;
import org.codehaus.plexus.compiler.CompilerException;
import org.codehaus.plexus.compiler.CompilerResult;
import org.codehaus.plexus.compiler.javac.JavacCompiler;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GosuCompiler extends JavacCompiler {

  @Override
  public String[] createCommandLine(CompilerConfiguration config) throws CompilerException {
    return buildGosuCompilerArguments( config );
  }

  /**
   * Adds javac arguments "-Xplugin:Manifold static" and "-J-Dgosu.source.list=_tmpdir_manifold-additional-sources.txt"
   * @param config the compiler configuration values received from Maven
   * @return the raw list of arguments; these will later be embedded in an @argfile
   */
  public String[] buildGosuCompilerArguments( CompilerConfiguration config) throws CompilerException {
    Set<File> gosuSourceFiles = new HashSet<>();
    Set<File> javaSourceFiles = new HashSet<>();

    for( File file : config.getSourceFiles() ) {
      String path = file.getAbsolutePath();
      if (path.endsWith(".java")) {
        javaSourceFiles.add(file);
      } else {
        gosuSourceFiles.add(file);
      }
    }

    String[] javaSourcePaths = javaSourceFiles.stream().map(File::getAbsolutePath).toArray(String[]::new);
    final Map<String, String> customCompilerArgs = config.getCustomCompilerArgumentsAsMap();
    customCompilerArgs.put("-Xplugin:Manifold static", null);

    if(!gosuSourceFiles.isEmpty()) {
      config.setSourceFiles(javaSourceFiles);
      File sourcesFile = createGosuSourcesList( gosuSourceFiles, config.getOutputLocation() );
      customCompilerArgs.put( "-J-Dgosu.source.list=" + sourcesFile.getAbsolutePath(), null );
    }

    config.setCustomCompilerArgumentsAsMap(customCompilerArgs);
    return buildCompilerArguments(config, javaSourcePaths);
  }

  private File createGosuSourcesList( Set<File> gosuSources, String outputLocation ) throws CompilerException {
    final String suffix = "manifold-additional-sources.txt";
    File sourcesFile;

    File outputDir = new File( outputLocation );
    if(!outputDir.exists()) {
      //noinspection ResultOfMethodCallIgnored
      outputDir.mkdirs();
    }
    try {
      if ((getLogger() != null) && getLogger().isDebugEnabled()) {
        sourcesFile = File.createTempFile(GosuCompiler.class.getName(), suffix, outputDir);
      } else {
        sourcesFile = File.createTempFile(GosuCompiler.class.getName(), suffix);
        sourcesFile.deleteOnExit();
      }
    } catch (IOException e) {
      throw new CompilerException("Error creating argfile with gosuc arguments", e);
    }

    List<String> sources = gosuSources.stream().map(File::getAbsolutePath).collect(Collectors.toList());

    try {
      Files.write(sourcesFile.toPath(), sources, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new CompilerException(String.format("Unable to write source list to %s", sourcesFile), e);
    }

    return sourcesFile;
  }

  @Override
  public CompilerResult performCompile( CompilerConfiguration config )
          throws CompilerException
  {

    File destinationDir = new File( config.getOutputLocation() );

    if ( !destinationDir.exists() )
    {
      destinationDir.mkdirs();
    }

    String[] sourceFiles = getSourceFiles( config );

    if ( ( sourceFiles == null ) || ( sourceFiles.length == 0 ) )
    {
      return new CompilerResult();
    }

    if ( ( getLogger() != null ) && getLogger().isInfoEnabled() )
    {
      getLogger().info( "Compiling " + sourceFiles.length + " " +
              "source file" + ( sourceFiles.length == 1 ? "" : "s" ) +
              " to " + destinationDir.getAbsolutePath() );
    }

    String[] args = buildGosuCompilerArguments( config );

    CompilerResult result;

    if ( config.isFork() )
    {
      String executable = config.getExecutable();

      if ( StringUtils.isEmpty( executable ) )
      {
        try
        {
          Method getJavacExecutable = JavacCompiler.class.getDeclaredMethod("getJavacExecutable");
          getJavacExecutable.setAccessible(true);
          executable = (String) getJavacExecutable.invoke(null);
        }
        catch ( Exception e )
        {
          getLogger().warn( "Unable to autodetect 'javac' path, using 'javac' from the environment." );
          executable = "javac";
        }
      }

      result = compileOutOfProcess( config, executable, args );
    }
    else
    {
      throw new UnsupportedOperationException("gosuc requires forking compilation.");
//      if ( isJava16() && !config.isForceJavacCompilerUse() )
//      {
//        // use fqcn to prevent loading of the class on 1.5 environment !
//        result =
//                org.codehaus.plexus.compiler.javac.JavaxToolsCompiler.compileInProcess( args, config, sourceFiles );
//      }
//      else
//      {
//        result = compileInProcess( args, config );
//      }
    }

    return result;
  }

  @Override
  public String getInputFileEnding( CompilerConfiguration configuration ) throws CompilerException {
    return "";
  }

}

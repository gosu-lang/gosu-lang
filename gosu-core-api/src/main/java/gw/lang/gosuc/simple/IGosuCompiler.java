package gw.lang.gosuc.simple;

import gw.lang.gosuc.cli.CommandLineOptions;
import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * @author dpetrusca
 */
public interface IGosuCompiler {

  default long initializeGosu( List<String> sourceFolders, List<String> classpath, String outputPath ) {
    return initializeGosu( sourceFolders, classpath, Collections.emptyList(), outputPath );
  }
  
  long initializeGosu( List<String> sourceFolders, List<String> classpath, List<String> backingSourcePath, String outputPath );

  /**
   * Reinitialize the compiler with new configuration, reusing as much cached state as possible.
   * If the classpath has only been appended to, this augments the existing caches incrementally.
   * Otherwise falls back to full uninit/reinit.
   *
   * @return time in milliseconds for the reinitialization
   */
  default long reinitializeGosu( List<String> sourceFolders, List<String> classpath, String outputPath ) {
    return reinitializeGosu( sourceFolders, classpath, Collections.emptyList(), outputPath );
  }

  long reinitializeGosu( List<String> sourceFolders, List<String> classpath, List<String> backingSourcePath, String outputPath );

  /**
   * Keeping method for backwards-compatibility with external tooling.
   * @deprecated Use uninitializeGosu() instead
   */
  default void unitializeGosu() {
    uninitializeGosu();
  }
  
  void uninitializeGosu();

  boolean isPathIgnored(String sourceFile);

  boolean compile(File sourceFile, ICompilerDriver driver) throws Exception;
  boolean compile( CommandLineOptions options, ICompilerDriver driver );
}

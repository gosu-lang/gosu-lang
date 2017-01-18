package gw.lang.gosuc.simple;

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
   * Keeping method for backwards-compatibility with external tooling.
   * @deprecated Use uninitializeGosu() instead
   */
  default void unitializeGosu() {
    uninitializeGosu();
  }
  
  void uninitializeGosu();

  boolean isPathIgnored(String sourceFile);

  boolean compile(File sourceFile, ICompilerDriver driver) throws Exception;

}

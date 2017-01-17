package gw.lang.gosuc.simple;

import java.io.File;
import java.util.List;

/**
 * @author dpetrusca
 */
public interface IGosuCompiler {

  long initializeGosu( List<String> sourceFolders, List<String> classpath, List<String> backingSourcePath, String outputPath );

  void unitializeGosu();

  boolean isPathIgnored(String sourceFile);

  boolean compile(File sourceFile, ICompilerDriver driver) throws Exception;

}

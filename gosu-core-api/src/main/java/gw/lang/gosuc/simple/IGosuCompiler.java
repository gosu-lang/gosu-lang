package gw.lang.gosuc.simple;

import java.io.File;
import java.util.List;

/**
 * @author dpetrusca
 */
public interface IGosuCompiler {

  long initializeGosu(List<String> contentRoots, List<File> cfaModules, List<String> sourceFolders,
                      List<String> classpath, String outputPath);

  void unitializeGosu();

  boolean isPathIgnored(String sourceFile);

  boolean compile(File sourceFile) throws Exception;

}

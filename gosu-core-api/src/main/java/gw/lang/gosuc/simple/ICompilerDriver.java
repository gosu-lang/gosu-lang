package gw.lang.gosuc.simple;

import java.io.File;

/**
 * @author dpetrusca
 */
public interface ICompilerDriver {
  public static final int ERROR = 0;
  public static final int WARNING = 1;

  void sendCompileIssue(File file, int category, long offset, long line, long column, String message);

  void registerOutput(File sourceFile, File outputFile);

}

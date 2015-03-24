package gw.lang.gosuc.simple;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SoutCompilerDriver implements ICompilerDriver {
  private List<String> errors = new ArrayList<>();
  private List<String> warnings = new ArrayList<>();

  @Override
  public void sendCompileIssue(File file, int category, long offset, long line, long column, String message) {
    if (category == WARNING) {
      warnings.add(String.format("%s:[%s,%s] warning: %s", file.getAbsolutePath(), line, column, message));
    } else if (category == ERROR) {
      errors.add(String.format("%s:[%s,%s] error: %s", file.getAbsolutePath(), line, column, message));
    }
  }

  @Override
  public void registerOutput(File sourceFile, File outputFile) {
    // nothing to do
  }

  public boolean hasErrors() {
    return errors.size() > 0;
  }

  public List<String> getErrors() {
    return errors;
  }

  public List<String> getWarnings() {
    return warnings;
  }
}

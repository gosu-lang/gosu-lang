/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.suite;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.runner.Result;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IntelliJTestScanner extends AbstractIntelliJTestScanner {

  public IntelliJTestScanner(File... dirsToScan) {
    super(dirsToScan);
  }

  public IntelliJTestScanner(@Nullable String filterDesc, File... dirsToScan) {
    super(filterDesc, dirsToScan);
  }

  public static void main(@NotNull String[] args) throws Exception {
    System.out.println("MaxMemory : " + Runtime.getRuntime().maxMemory());
    List<File> fileList = new ArrayList<>();
    String filteredBy = null;
    for (int i = 0, argsLength = args.length; i < argsLength; i++) {
      if("-runtests".equals(args[i])) {
        if(i < argsLength - 1) {
          i++;
          filteredBy = args[i];
        }
      } else {
        fileList.add(new File(args[i]));
      }
    }
    File [] files = fileList.toArray(new File[fileList.size()]);
    Result result = new IntelliJTestScanner(filteredBy, files).runTests();
    System.exit(result.wasSuccessful() ? 0 : -1);
  }

  protected boolean shouldExclude(@NotNull String typeName) {
    return typeName.startsWith("gw.plugin.ij.project");
  }

}


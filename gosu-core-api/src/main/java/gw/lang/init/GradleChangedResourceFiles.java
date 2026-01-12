package gw.lang.init;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gradle-specific implementation for tracking changed resource files during incremental compilation.
 * This class is reflectively accessed by Manifold's ManifoldJavaFileManager.getChangedResourceFiles()
 * to determine which files have changed and need recompilation.
 *
 * Thread-safe for use across multiple compilation tasks.
 */
public class GradleChangedResourceFiles
{
  private static final ThreadLocal<List<File>> CHANGED_FILES = new ThreadLocal<>();

  /**
   * Sets the list of changed files for the current compilation.
   * Called by Gradle before javac compilation begins.
   *
   * @param changedFiles List of changed .gs/.gsx files
   */
  public static void setChangedFiles( List<File> changedFiles )
  {
    CHANGED_FILES.set( changedFiles != null ? new ArrayList<>( changedFiles ) : Collections.emptyList() );
  }

  /**
   * Gets the list of changed files for incremental compilation.
   * Called reflectively by Manifold's ManifoldJavaFileManager.
   *
   * @return List of changed files, or empty list if not set
   */
  public static List<File> getChangedFiles()
  {
    List<File> files = CHANGED_FILES.get();
    return files != null ? files : Collections.emptyList();
  }

  /**
   * Clears the changed files for the current thread.
   * Should be called after compilation completes.
   */
  public static void clear()
  {
    CHANGED_FILES.remove();
  }
}

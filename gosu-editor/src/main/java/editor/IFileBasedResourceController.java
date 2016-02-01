package editor;

import java.io.File;

/**
 * @author cgross
 */
public interface IFileBasedResourceController
{
  /**
   * Saves the string to the given resource
   */
  void commitChanges( File file, String text );
}
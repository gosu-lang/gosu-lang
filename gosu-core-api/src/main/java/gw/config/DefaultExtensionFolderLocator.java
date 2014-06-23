/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;

import java.io.File;
import java.net.URL;

public class DefaultExtensionFolderLocator extends BaseService implements IExtensionFolderLocator {

  @Override
  public File getExtensionFolderPath() {
    try {
      URL loc = Class.forName( "gw.NativeLibraryLocationMarker" ).getProtectionDomain().getCodeSource().getLocation();
      if (loc == null) {
        return null;
      }
      File path = new File(new File(loc.getPath()).getParentFile().getParentFile(), "ext");
      return path;
    } catch (ClassNotFoundException e) {
      System.err.println("Cannot locate native-library-marker.jar.");
      return null;
    }
  }

}

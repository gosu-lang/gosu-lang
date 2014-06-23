/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs.physical.generic;

import gw.fs.ResourcePath;
import gw.fs.physical.IPhysicalFileSystem;
import gw.fs.physical.IFileMetadata;
import gw.lang.UnstableAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@UnstableAPI
public class GenericPhysicalFileSystem implements IPhysicalFileSystem {

  @Override
  public List<? extends IFileMetadata> listFiles(ResourcePath directoryPath) {
    File file = toJavaFile(directoryPath);
    File[] files = file.listFiles();
    List<GenericFileMetadata> fileInfos = new ArrayList<GenericFileMetadata>();
    if (files != null) {
      for (File f : files) {
        fileInfos.add(new GenericFileMetadata(f));
      }
    }

    return fileInfos;
  }

  @Override
  public IFileMetadata getFileMetadata(ResourcePath filePath) {
    return new GenericFileMetadata(toJavaFile(filePath));
  }

  @Override
  public boolean exists(ResourcePath filePath) {
    return toJavaFile(filePath).exists();
  }

  @Override
  public boolean delete(ResourcePath filePath) {
    return toJavaFile(filePath).delete();
  }

  @Override
  public boolean mkdir(ResourcePath dirPath) {
    return toJavaFile(dirPath).mkdir();
  }

  @Override
  public void clearDirectoryCaches(ResourcePath dirPath) {
    // Do nothing
  }

  @Override
  public void clearAllCaches() {
    // Do nothing
  }

  private File toJavaFile(ResourcePath directoryPath) {
    return new File(directoryPath.getFileSystemPathString());
  }
}

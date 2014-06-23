/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs.physical.win32;

import gw.fs.ResourcePath;
import gw.fs.physical.IFileMetadata;
import gw.fs.physical.IPhysicalFileSystem;
import gw.fs.physical.generic.GenericFileMetadata;
import gw.lang.UnstableAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@UnstableAPI
public class Win32PhysicalFileSystem implements IPhysicalFileSystem {
  
  @Override
  public List<? extends IFileMetadata> listFiles(ResourcePath directoryPath) {
    List<Win32FindData> findDatas = NativeWin32API.listDir(directoryPath.getFileSystemPathString());
    List<IFileMetadata> fileMD = new ArrayList<IFileMetadata>();
    for (Win32FindData findData : findDatas) {
      fileMD.add(new Win32FileMetadata(findData));
    }
    return fileMD;
  }

  @Override
  public IFileMetadata getFileMetadata(ResourcePath filePath) {
    Win32FindData findData = NativeWin32API.fileMetadata(filePath.getFileSystemPathString());
    if (findData != null) {
      return new Win32FileMetadata(findData);
    } else {
      return new GenericFileMetadata(toJavaFile(filePath));
    }
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

/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.filesystem;

import com.google.common.collect.Maps;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.impl.jar.JarFileSystemImpl;
import com.intellij.openapi.vfs.impl.local.LocalFileSystemImpl;
import com.intellij.testFramework.LightVirtualFile;
import gw.config.BaseService;
import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.fs.IResource;
import gw.fs.jar.JarFileDirectoryImpl;
import gw.fs.url.URLFileImpl;
import gw.lang.reflect.module.IFileSystem;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.util.GosuModuleUtil;
import gw.plugin.ij.util.LightVirtualFileWithModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.jar.JarFile;

public class IDEAFileSystem extends BaseService implements IFileSystem
{
  private static final Object CACHED_FILE_SYSTEM_LOCK = new Object();

  private final Map<File, IDirectory> _cachedDirInfo = Maps.newHashMap();
  private final IDirectoryResourceExtractor _iDirectoryResourceExtractor = new IDirectoryResourceExtractor();
  private final IFileResourceExtractor _iFileResourceExtractor = new IFileResourceExtractor();

  public IDEAFileSystem() {
  }

  @NotNull
  @Override
  public IDirectory getIDirectory(@NotNull File dir) {
    String pathString = dir.getAbsolutePath().replace(File.separatorChar, '/');
    return getIDirectory(pathString);
  }

  @NotNull
  public IDirectory getIDirectory(@NotNull String pathString) {
    VirtualFile file = LocalFileSystemImpl.getInstance().findFileByPath(pathString);
    if (file != null && pathString.endsWith(".jar")) {
      file = JarFileSystem.getInstance().getJarRootForLocalFile(file);
      if (file == null) {
        throw new RuntimeException("Cannot load Jar file for: " + pathString);
      }
      return new IDEAJarDirectory(file);
    }
    return file != null ? new IDEADirectory(file) : new IDEADirectory(pathString);
  }

  @NotNull
  @Override
  public IFile getIFile(@NotNull File file) {
    String pathString = file.getAbsolutePath().replace(File.separatorChar, '/');
    return getIFile(pathString);
  }

  @NotNull
  public IFile getIFile(@NotNull String pathString) {
    VirtualFile file = LocalFileSystemImpl.getInstance().findFileByPath(pathString);
    if (file == null && pathString.contains(".jar!")) {
      file = JarFileSystemImpl.getInstance().findFileByPath(pathString);
    }
    return file != null ? new IDEAFile(file) : new IDEAFile(pathString);
  }

  @NotNull
  public IDEAFile getIFile(VirtualFile file) {
    return new IDEAFile(file);
  }

  @NotNull
  public IDEADirectory getIDirectory(VirtualFile file) {
    return new IDEADirectory(file);
  }

  @Nullable
  @Override
  public IDirectory getIDirectory(URL url) {
    return _iDirectoryResourceExtractor.getClassResource(url);
  }

  @Nullable
  @Override
  public IFile getIFile(URL url) {
    return _iFileResourceExtractor.getClassResource(url);
  }

  @NotNull
  @Override
  public IFile getFakeFile(@NotNull URL url, IModule module) {
    LightVirtualFile virtualFile = LightVirtualFileWithModule.create(url.getFile(), module);
    virtualFile.putUserData(ModuleUtil.KEY_MODULE, GosuModuleUtil.getModule( module ));
    return new IDEAFile(virtualFile);
  }

  @NotNull
  private IDirectory createDir(@NotNull File dir) {
    if (dir.getName().endsWith(".jar") && dir.isFile()) {
      return new JarFileDirectoryImpl(dir);
    } else {
      return getIDirectory(dir);
    }
  }

  @Override
  public void setCachingMode(CachingMode cachingMode) {
  }

  @Override
  public void clearAllCaches() {
  }

  private abstract class ResourceExtractor<J extends IResource> {
    @Nullable
    J getClassResource(@Nullable URL _url) {
      if (_url == null) {
        return null;
      }

      String protocol = _url.getProtocol();

      switch (protocol) {
        case "file":
          return getIResourceFromJavaFile(_url);
        case "jar":
          JarURLConnection urlConnection;
          JarFile jarFile;
          try {
            urlConnection = (JarURLConnection) _url.openConnection();
            jarFile = urlConnection.getJarFile();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
          File dir = new File(jarFile.getName());

          IDirectory jarFileDirectory;
          synchronized (CACHED_FILE_SYSTEM_LOCK) {
            jarFileDirectory = _cachedDirInfo.get(dir);
            if (jarFileDirectory == null) {
              jarFileDirectory = createDir(dir);
              _cachedDirInfo.put(dir, jarFileDirectory);
            }
          }
          return getIResourceFromJarDirectoryAndEntryName(jarFileDirectory, urlConnection.getEntryName());
        case "http":
        case "https":
          return getRemoteFile(_url);
        default:
          throw new RuntimeException("Unrecognized protocol: " + protocol);
      }
    }

    abstract J getIResourceFromJarDirectoryAndEntryName(IDirectory jarFS, String entryName);

    abstract J getIResourceFromJavaFile(URL location);

    abstract J getRemoteFile(URL location);

    @NotNull
    protected File getFileFromURL(@NotNull URL url) {
      try {
        URI uri = url.toURI();
        if (uri.getFragment() != null) {
          uri = new URI(uri.getScheme(), uri.getSchemeSpecificPart(), null);
        }
        return new File(uri);
      } catch (URISyntaxException ex) {
        throw new RuntimeException(ex);
      } catch (IllegalArgumentException ex) {
        // debug getting IAE only in TH - unable to parse URL with fragment identifier
        throw new IllegalArgumentException("Unable to parse URL " + url.toExternalForm(), ex);
      }
    }

  }

  private class IFileResourceExtractor extends ResourceExtractor<IFile> {
    IFile getIResourceFromJarDirectoryAndEntryName(@NotNull IDirectory jarFS, String entryName) {
      return jarFS.file(entryName);
    }

    IFile getIResourceFromJavaFile(@NotNull URL location) {
      return CommonServices.getFileSystem().getIFile(getFileFromURL(location));
    }

    @Override
    IFile getRemoteFile(URL location) {
      return new URLFileImpl(location);
    }
  }

  private class IDirectoryResourceExtractor extends ResourceExtractor<IDirectory> {
    protected IDirectory getIResourceFromJarDirectoryAndEntryName(@NotNull IDirectory jarFS, String entryName) {
      return jarFS.dir(entryName);
    }

    protected IDirectory getIResourceFromJavaFile(@NotNull URL location) {
      return CommonServices.getFileSystem().getIDirectory(getFileFromURL(location));
    }

    @Override
    IDirectory getRemoteFile(URL location) {
      throw new UnsupportedOperationException("unable to obtain directory via remote protocol. " + location);
    }
  }
}

/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.compiler;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Closeables;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class FileDependencyCache {
  private final Map<VirtualFile, FileDependencyInfo> cache = Maps.newHashMap();

  private final Map<VirtualFile, Set<VirtualFile>> dependsOnCache = Maps.newHashMap();

  private final Map<String, Set<VirtualFile>> displayKeyDependsOnCache = Maps.newHashMap();

  private final Map<VirtualFile, Set<String>> displayKeys = Maps.newHashMap();

  public int size() {
    return cache.size();
  }

  public FileDependencyInfo get(VirtualFile file) {
    return cache.get(checkNotNull(file));
  }

  public Set<VirtualFile> keySet() {
    return cache.keySet();
  }

  public Collection<FileDependencyInfo> values() {
    return cache.values();
  }

  public FileDependencyInfo put(@NotNull FileDependencyInfo info) {
    checkNotNull(info);
    final VirtualFile file = info.getFile();
    for (VirtualFile fileDependency : info.getDependencies()) {
      getDependentsOn(fileDependency).add(file);
    }
    for (String keyDependency : info.getDisplayKeys()) {
      getDependentsOnByDisplayKey(keyDependency).add(file);
    }
    if ("display.properties".equals(file.getName())) {
      handleDisplayKeys(file);
    }
    return cache.put(file, info);
  }

  public void remove(VirtualFile file) {
    final FileDependencyInfo info = cache.remove(file);
    if (info != null) {
      for (VirtualFile fileDependency : info.getDependencies()) {
        getDependentsOn(fileDependency).remove(file);
      }

      for (String keyDependency : info.getDisplayKeys()) {
        getDependentsOnByDisplayKey(keyDependency).remove(file);
      }
    }
  }

  public void clear() {
    cache.clear();
    dependsOnCache.clear();
    displayKeyDependsOnCache.clear();
    displayKeys.clear();
  }

  public long getFingerprint(VirtualFile file) {
    final FileDependencyInfo info = get(file);
    return info != null ? info.getFingerprint() : 0;
  }

  public Set<VirtualFile> getDependencies(VirtualFile file) {
    final FileDependencyInfo info = get(file);
    return info != null ? info.getDependencies() : Collections.<VirtualFile>emptySet();
  }

  public Set<VirtualFile> getDependentsOn(VirtualFile file) {
    Set<VirtualFile> virtualFiles = dependsOnCache.get(file);
    if (virtualFiles == null) {
      virtualFiles = Sets.newHashSet();
      dependsOnCache.put(file, virtualFiles);
    }
    return virtualFiles;
  }

  public Set<VirtualFile> getDependentsOnByDisplayKey(String key) {
    Set<VirtualFile> virtualFiles = displayKeyDependsOnCache.get(key);
    if (virtualFiles == null) {
      virtualFiles = Sets.newHashSet();
      displayKeyDependsOnCache.put(key, virtualFiles);
    }
    return virtualFiles;
  }

  public Set<String> getDisplayKeys(VirtualFile file) {
    Set<String> keys = displayKeys.get(file);
    if (keys == null) {
      keys = Sets.newHashSet();
      displayKeys.put(file, keys);
    }
    return keys;
  }

  public void save(@NotNull File file) throws IOException {
    FileUtil.createParentDirs(file);
    DataOutputStream out = null;
    try {
      out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

      // Size
      out.writeInt(size());

      // Values
      for (FileDependencyInfo info : cache.values()) {
        FileDependencyInfo.write(info, out);
      }
    } finally {
      Closeables.closeQuietly(out);
    }
  }

  public void load(@NotNull Project project, File file) throws IOException {
    DataInputStream in = null;
    try {
      in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
      final int size = in.readInt();
      for (int i = 0; i < size; ++i) {
        final FileDependencyInfo info = FileDependencyInfo.read(in);
        if (info != null) {
          put(info);
        }
      }
    } finally {
      Closeables.closeQuietly(in);
    }
  }

  private void handleDisplayKeys(VirtualFile file) {
    final Set<String> keys = getDisplayKeys(file);
    keys.clear();
    try (InputStream inputStream = file.getInputStream()) {
      PropertyKeys props = new PropertyKeys(keys);
      props.load(new InputStreamReader(inputStream));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private class PropertyKeys extends Properties {
    Set<String> keys;

    public PropertyKeys(Set<String> keys) {
      this.keys = keys;
    }

    @Override
    public synchronized Object put(Object key, Object value) {
      keys.add((String) key);
      return null;
    }
  }
}

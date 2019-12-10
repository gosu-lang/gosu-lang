/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.properties;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.internal.gosu.util.StringUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.util.Pair;
import gw.util.concurrent.LockingLazyVar;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * A property set based on an underlying {@link Properties} object. Knows how to finds Properties
 * files within a module and to converts them into PropertySet objects.
 */
public class PropertiesPropertySet implements PropertySet {

  /**
   * Knows how to find all the property files in a module and create PropertiesPropertySets
   * from them.
   */
  public static class Source implements PropertySetSource {

    private static final String EXTENSION = ".properties";

    private final LockingLazyVar<Map<String, IFile>> _filesByTypeName = LockingLazyVar.make( () -> {
      List<Pair<String, IFile>> propertiesFiles = findAllFilesByExtension( EXTENSION );
      final int initialCapacity = propertiesFiles.size();
      Map<String, IFile> result = new HashMap<>( initialCapacity );
      for (Pair<String, IFile> pair : propertiesFiles) {
        String fileName = pair.getFirst();
        if( !PropertiesTypeLoader.isDisplayPropertiesFile( pair.getSecond().getName() ) ) {
          String typeName = fileName.substring(0, fileName.length() - EXTENSION.length()).replace('/', '.');
          if (isValidTypeName(typeName)) {
            result.putIfAbsent(typeName, pair.getSecond());
          }
        }
      }
      return result;
    });

    public List<Pair<String, IFile>> findAllFilesByExtension(String extension) {
      List<Pair<String, IFile>> results = new ArrayList<>();

      for (IDirectory sourceEntry : TypeSystem.getModule().getSourcePath()) {
        if (sourceEntry.exists()) {
          String prefix = sourceEntry.getName().equals(IModule.CONFIG_RESOURCE_PREFIX) ? IModule.CONFIG_RESOURCE_PREFIX : "";
          addAllLocalResourceFilesByExtensionInternal(prefix, sourceEntry, extension, results);
        }
      }
      return results;
    }

    private void addAllLocalResourceFilesByExtensionInternal(String relativePath, IDirectory dir, String extension, List<Pair<String, IFile>> results) {
      List<IDirectory> excludedPath = Arrays.asList(TypeSystem.getModule().getFileRepository().getExcludedPath());
      if ( excludedPath.contains( dir )) {
        return;
      }
      if (!CommonServices.getPlatformHelper().isPathIgnored(relativePath)) {
        for (IFile file : dir.listFiles()) {
          if (file.getName().endsWith(extension)) {
            String path = appendResourceNameToPath(relativePath, file.getName());
            results.add( new Pair<>( path, file ));
          }
        }
        for (IDirectory subdir : dir.listDirs()) {
          String path = appendResourceNameToPath(relativePath, subdir.getName());
          addAllLocalResourceFilesByExtensionInternal(path, subdir, extension, results);
        }
      }
    }

    private static String appendResourceNameToPath( String relativePath, String resourceName ) {
      String path;
      if ( relativePath.length() > 0 ) {
        path = relativePath + '/' + resourceName;
      }
      else {
        path = resourceName;
      }
      return path;
    }

    @Override
    public PropertySet getPropertySetForFile(IFile file) {
      for (Map.Entry<String, IFile> entry : _filesByTypeName.get().entrySet()){
        IFile value = entry.getValue();
        if (value.equals(file)) {
          return getPropertySet(entry.getKey());
        }
      }
      return null;
    }

    @Override
    public IFile getFile(String name) {
      return _filesByTypeName.get().get(name);
    }

    public Source() {
    }

    @Override
    public Set<String> getPropertySetNames() {
      return _filesByTypeName.get().keySet();
    }

    @Override
    public PropertySet getPropertySet(String name) {
      IFile file = _filesByTypeName.get().get(name);
      if (file == null) {
        throw new IllegalArgumentException(name + " is not the name of a properties file property set");
      }
      PropertySet result = new EmptyPropertySet(name);
      if (file.exists()) {
        try {
          InputStream propertiesStream = file.openInputStream();
          Properties properties = new Properties();
          try {
            properties.load(propertiesStream);
            result = new PropertiesPropertySet(name, properties);
          } finally {
            closeSafely(propertiesStream);
          }
        } catch (IOException e) {
          CommonServices.getEntityAccess().getLogger().error(String.format("Could not read property file %s", file), e);
        }
      }
      return result;
    }

    private static boolean isValidTypeName(String typeName) {
      List<String> nameParts = StringUtil.tokenizeToList(typeName, '.');
      if (nameParts == null || nameParts.isEmpty()) {
        return false;
      }
      for (String namePart : nameParts) {
        if (!PropertyNode.isGosuIdentifier(namePart)) {
          return false;
        }
      }
      return true;
    }

    private static void closeSafely(InputStream inputStream) {
      try {
        inputStream.close();
      } catch (IOException e) {
        // Ignore
      }
    }
  }

  private final String _name;
  private final Set<String> _keys;
  private final Properties _properties;
  
  public PropertiesPropertySet(String name, Properties properties) {
    _name = name;
    _properties = properties;
    _keys = getStringPropertyNames(properties);
  }

  @Override
  public Set<String> getKeys() {
    return _keys;
  }

  @Override
  public String getName() {
    return _name;
  }

  @Override
  public String getValue(String key) {
    return _properties.getProperty(key);
  }

  private Set<String> getStringPropertyNames(Properties properties) {
    // Can't use properties.stringPropertyNames() because it's not available in Java 1.5
    Set<String> result = new TreeSet<>();
    for (Map.Entry<Object, Object> entry : properties.entrySet()) {
      if (entry.getKey() instanceof String && entry.getValue() instanceof String) {
        result.add((String) entry.getKey());
      }
    }
    return Collections.unmodifiableSet(result);
  }

}

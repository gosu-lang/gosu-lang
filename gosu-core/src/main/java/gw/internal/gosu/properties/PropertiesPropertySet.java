/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.properties;

import gw.fs.IFile;
import gw.internal.gosu.util.StringUtil;
import gw.lang.reflect.module.IModule;
import gw.util.GosuLoggerFactory;
import gw.util.Pair;
import gw.util.concurrent.LockingLazyVar;

import java.io.IOException;
import java.io.InputStream;
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

    private final IModule _module;
    private final LockingLazyVar<Map<String,IFile>> _filesByTypeName = new LockingLazyVar<Map<String, IFile>>() {
      @Override
      protected Map<String, IFile> init() {
        List<Pair<String, IFile>> propertiesFiles = _module.getFileRepository().findAllFilesByExtension(EXTENSION);
        final int initialCapacity = propertiesFiles.size();
        Map<String,IFile> result = new HashMap<String, IFile>( initialCapacity );
        for (Pair<String,IFile> pair : propertiesFiles) {
          String fileName = pair.getFirst();
          if( !PropertiesTypeLoader.isDisplayPropertiesFile( pair.getSecond().getName() ) ) {
            String typeName = fileName.substring(0, fileName.length() - EXTENSION.length()).replace('/', '.');
            if (isValidTypeName(typeName)) {
              result.put(typeName, pair.getSecond());
            }
          }
        }
        return result;
      }
    };

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

    public Source(IModule module) {
      _module = module;
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
          GosuLoggerFactory.getLogger().error(String.format("Could not read property file %s", file), e);
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
    Set<String> result = new TreeSet<String>();
    for (Map.Entry<Object, Object> entry : properties.entrySet()) {
      if (entry.getKey() instanceof String && entry.getValue() instanceof String) {
        result.add((String) entry.getKey());
      }
    }
    return Collections.unmodifiableSet(result);
  }

}

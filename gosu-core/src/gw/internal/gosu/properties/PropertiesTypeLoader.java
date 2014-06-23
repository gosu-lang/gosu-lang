/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.properties;

import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.reflect.IType;
import gw.lang.reflect.RefreshKind;
import gw.lang.reflect.TypeLoaderBase;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.util.concurrent.LockingLazyVar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PropertiesTypeLoader extends TypeLoaderBase {
  public static final Set<String> EXTENSIONS = Collections.singleton("properties");
  private static final String FILE_EXTENSION = "properties";
  private static final String DISPLAY_PROPERTIES = "display.properties";

  protected Set<String> _namespaces;

  private List<PropertySetSource> _sources;
  private final LockingLazyVar<Map<PropertySetSource,TypeNameSet>> _rootTypeNames = new LockingLazyVar<Map<PropertySetSource,TypeNameSet>>() {
    @Override
    protected Map<PropertySetSource,TypeNameSet> init() {
      Map<PropertySetSource,TypeNameSet> result = new HashMap<PropertySetSource, TypeNameSet>();
      for (PropertySetSource source : _sources) {
        result.put(source, new TypeNameSet(source.getPropertySetNames()));
      }
      return result;
    }
  };

  public PropertiesTypeLoader(IModule module) {
    super(module);
    initSources(module);
  }

  private void initSources(IModule module) {
    _sources = Arrays.asList(
            SystemPropertiesPropertySet.SOURCE,
            new PropertiesPropertySet.Source(module)
    );
  }

  @Override
  public Set<String> computeTypeNames() {
    Set<String> result = new HashSet<String>();
    for (TypeNameSet names : _rootTypeNames.get().values()) {
      names.addTo(result);
    }
    return result;
  }

  @Override
  public List<String> getHandledPrefixes() {
    return Collections.emptyList();
  }

  @Override
  public boolean handlesNonPrefixLoads() {
      return true;
  }

  @Override
  public boolean handlesFile( IFile file ) {
    return FILE_EXTENSION.equalsIgnoreCase( file.getExtension() ) &&
           !isDisplayPropertiesFile( file.getName() );
  }

  public static boolean isDisplayPropertiesFile( String fileName ) {
    return fileName.toLowerCase().endsWith( DISPLAY_PROPERTIES );
  }

  @Override
  public IType getType(String fullyQualifiedName) {
      // hack to not allow property file in jline to shadow the java type
      if (fullyQualifiedName.startsWith("jline.")) {
        return null;
      }
    for (PropertySetSource source : _sources) {
      TypeNameSet typeNameSet = _rootTypeNames.get().get(source);
      if (typeNameSet != null) {
        List<String> possibleMatches = typeNameSet.findMatchesFor(fullyQualifiedName);
        for (String possibleMatch : possibleMatches) {
          Map<String, IType> propertySetTypes = createPropertyTypesForPropertySetWithName(source, possibleMatch);
          if (propertySetTypes.containsKey(fullyQualifiedName)) {
            return propertySetTypes.get(fullyQualifiedName);
          }
        }
      }
    }
    return null;
  }

  @Override
  public void refreshedImpl() {
    _rootTypeNames.clear();
  }

  private Map<String,IType> createPropertyTypesForPropertySetWithName(PropertySetSource source, String name) {
    HashMap<String, IType> resultMap = new HashMap<String, IType>();
    createPropertyTypesFromPropertyNodeTree(resultMap, PropertyNode.buildTree(source.getPropertySet(name)), source.getFile(name));
    return resultMap;
  }

  private IType createPropertyTypesFromPropertyNodeTree( HashMap<String, IType> resultMap, PropertyNode node, IFile file) {
    IType result = TypeSystem.getOrCreateTypeReference(new PropertiesType(this, node, file));
    resultMap.put(result.getName(), result);
    for (PropertyNode child : node.getChildren()) {
      if (!child.isLeaf()) {
        createPropertyTypesFromPropertyNodeTree(resultMap, child, file);
      }
    }
    return result;
  }

  @Override
  public String[] getTypesForFile(IFile file) {
    List<String> types = new ArrayList<String>();
    boolean foundSource = false;
    for (PropertySetSource source : _sources) {
      PropertySet ps = source.getPropertySetForFile(file);
      if (ps != null) {
        foundSource = true;
        HashMap<String, IType> resultMap = new HashMap<String, IType>();
        createPropertyTypesFromPropertyNodeTree(resultMap, PropertyNode.buildTree(ps), file);
        for (IType type : resultMap.values()) {
          types.add(type.getName());
        }
      }
    }
    if(!foundSource) {
      initSources(_module);
      _rootTypeNames.clear();
    }
    return types.toArray(new String[types.size()]);
  }

  @Override
  public RefreshKind refreshedFile(IFile file, String[] types, RefreshKind kind) {
    _rootTypeNames.clear();
    return kind;
  }

  /**
   * Set of case insensitive type names with operation to quickly find which type names in the set are possible
   * matches for a full type name. A match is a prefix which is either an exact match or matches up to the
   * package separator (.). So, for example, given the full name one.two.three then one, one.two and one.two.three
   * are all matches.
   */
  static class TypeNameSet {

    private final String[] _names;

    public TypeNameSet(Set<String> names) {
      _names = names.toArray(new String[names.size()]);
      Arrays.sort(_names, String.CASE_INSENSITIVE_ORDER);
    }

    public void addTo(Collection<String> names) {
      names.addAll(Arrays.asList(_names));
    }

    public List<String> findMatchesFor(String fullName) {
      List<String> result = Collections.emptyList();
      int index = getIndexOfLastPossibleMatch(fullName);
      while (index >= 0 && isPrefix(_names[index], fullName)) {
        if (isMatch(_names[index], fullName)) {
          if (result.isEmpty()) {
            result = new ArrayList<String>();
          }
          result.add(_names[index]);
        }
        index--;
      }
      return result;
    }

    private int getIndexOfLastPossibleMatch(String fullName) {
      int index = Arrays.binarySearch(_names, fullName, String.CASE_INSENSITIVE_ORDER);
      if (index < 0) {
        int insertionPoint = -index - 1;
        index = insertionPoint - 1;
      }
      return index;
    }

    private boolean isPrefix(String possiblePrefix, String fullName) {
      return fullName.regionMatches(true, 0, possiblePrefix, 0, possiblePrefix.length());
    }

    private boolean isMatch(String prefix, String fullName) {
      return fullName.equalsIgnoreCase(prefix) || fullName.charAt(prefix.length()) == '.';
    }
  }

  @Override
  public boolean hasNamespace(String namespace) {
    return getAllNamespaces().contains(namespace);
  }

  @Override
  public Set<String> getAllNamespaces() {
    if (_namespaces == null) {
      try {
        _namespaces = TypeSystem.getNamespacesFromTypeNames(getAllTypeNames(), new HashSet<String>());
      } catch (NullPointerException e) {
        //!! hack to get past dependency issue with tests
        return Collections.emptySet();
      }
    }
    return _namespaces;
  }

  @Override
  public void refreshedNamespace(String namespace, IDirectory dir, RefreshKind kind) {
    if (_namespaces != null) {
      if (kind == RefreshKind.CREATION) {
        _namespaces.add(namespace);
      } else if (kind == RefreshKind.DELETION) {
        _namespaces.remove(namespace);
      }
    }
  }
}

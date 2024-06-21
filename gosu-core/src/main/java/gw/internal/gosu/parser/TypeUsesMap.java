/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.TypeSystem;
import gw.util.DynamicArray;
import gw.util.GosuClassUtil;
import gw.internal.gosu.util.StringPool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 */
public class TypeUsesMap implements ITypeUsesMap
{
  private boolean _bSupportRelativePackageResolution;
  private HashMap<String, String> _specialTypeUsesByRelativeName;
  private HashMap<String, String> _typeUsesByRelativeName;
  private DynamicArray<String> _defaultNamespaces;
  private DynamicArray<String> _namespaces;
  private DynamicArray<String> _specialNamespaces; // Namespaces that get searched by default
  private DynamicArray<String> _featureSpaces;
  private DynamicArray<IFeatureInfo> _featureLiterals;
  private HashMap<String, IUsesStatement> _usesStmts;
  private boolean _locked = false;

  public TypeUsesMap( List<String> specialTypeUses )
  {
    this();
    for (String typeUse : specialTypeUses) {
      addToSpecialTypeUses(typeUse);
    }
  }

  public TypeUsesMap()
  {
    _specialTypeUsesByRelativeName = new HashMap<>();
    _typeUsesByRelativeName = new HashMap<>();
    _namespaces = new DynamicArray<>();
    _featureSpaces = new DynamicArray<>();
    _featureLiterals = new DynamicArray<>();
    _usesStmts = new HashMap<>();
    _defaultNamespaces = new DynamicArray<>();
    _specialNamespaces = new DynamicArray<>();
    _defaultNamespaces.add( "gw.lang." );
    _defaultNamespaces.add( "java.lang." );
    _defaultNamespaces.add( "java.util." );
    _defaultNamespaces.add( "dynamic." );
    _defaultNamespaces.add( "" );
  }

  @SuppressWarnings({"unchecked"})
  public ITypeUsesMap copy()
  {
    TypeUsesMap copy = new TypeUsesMap();
    copy._bSupportRelativePackageResolution = _bSupportRelativePackageResolution;
    copy._specialTypeUsesByRelativeName = new HashMap<>( _specialTypeUsesByRelativeName );
    copy._defaultNamespaces = _defaultNamespaces.copy();
    copy._specialNamespaces = _specialNamespaces.copy();
    copy._typeUsesByRelativeName = new HashMap<>( _typeUsesByRelativeName );
    copy._namespaces = _namespaces.copy();
    copy._featureSpaces = _featureSpaces.copy();
    copy._featureLiterals = _featureLiterals.copy();
    return copy;
  }

  @SuppressWarnings({"unchecked"})
  public ITypeUsesMap copyLocalScope()
  {
    TypeUsesMap copy = new TypeUsesMap();
    copy._bSupportRelativePackageResolution = _bSupportRelativePackageResolution;
    copy._specialTypeUsesByRelativeName = _specialTypeUsesByRelativeName;
    copy._defaultNamespaces = _defaultNamespaces.copy();
    copy._specialNamespaces = _specialNamespaces.copy();
    copy._typeUsesByRelativeName = new HashMap<>( _typeUsesByRelativeName );
    copy._usesStmts = new HashMap<>( _usesStmts );
    copy._namespaces = _namespaces.copy();
    copy._featureSpaces = _featureSpaces.copy();
    copy._featureLiterals = _featureLiterals.copy();
    return copy;
  }

  public ITypeUsesMap lock()
  {
    _locked = true;
    return this;
  }

  @Override
  public boolean containsType(String typeName) {
    for (Object o : getTypeUses()) {
      String used = (String) o;
      if (used.endsWith(".")) {
        if (used.lastIndexOf('.') == typeName.lastIndexOf('.')) {
          return typeName.startsWith(used);
        }
      } else if (used.equals(typeName)) {
        return true;
      }
    }
    return false;
  }

  public Set<String> getTypeUses()
  {
    Set<String> combined = getNamespaces();
    combined.addAll( _specialTypeUsesByRelativeName.values() );
    combined.addAll( _typeUsesByRelativeName.values() );
    return combined;
  }

  @Override
  public Set<String> getNamespaces() {
    Set<String> combined = new LinkedHashSet<String>();
    combined.addAll( _namespaces );
    combined.addAll( _defaultNamespaces );
    combined.addAll( _specialNamespaces );
    return combined;
  }

  @Override
  public Set<String> getFeatureSpaces() {
    Set<String> combined = new LinkedHashSet<>();
    combined.addAll( _featureSpaces );
    return combined;
  }

  @Override
  public Set<IFeatureInfo> getFeatureLiterals() {
    Set<IFeatureInfo> combined = new LinkedHashSet<>();
    combined.addAll( _featureLiterals );
    return combined;
  }

  public void addToTypeUses( String strType )
  {
    checkLocked();
    if( strType != null )
    {
      strType = strType.replace( '$', '.' );
      try
      {
        addToTypeUses( strType, _typeUsesByRelativeName, _namespaces );
      }
      catch( ClassNotFoundException e )
      {
        throw new RuntimeException( e );
      }
    }
  }

  public void addToFeatureSpaces( String strType )
  {
    checkLocked();
    if( strType != null )
    {
      strType = strType.replace( '$', '.' );
      // Store them so that they end with a dot
      String ns = StringPool.get( strType );
      if( !_featureSpaces.contains( ns ) )
      {
        _featureSpaces.add( ns );
      }
    }
  }

  public void addToFeatureLiterals( IFeatureInfo fi )
  {
    checkLocked();
    if( fi != null )
    {
      _featureLiterals.add( fi );
    }
  }

  public void addToTypeUses( IUsesStatement usesSmt )
  {
    String strType = usesSmt.getTypeName();
    if( usesSmt.isFeatureSpace() )
    {
      addToFeatureSpaces( strType );
    }
    else
    {
      IFeatureInfo fi = usesSmt.getFeatureInfo();
      if( fi != null )
      {
        addToFeatureLiterals( fi );
      }
      else
      {
        addToTypeUses( strType );
      }
    }
    _usesStmts.put( StringPool.get( strType ), usesSmt );
  }

  public Set<IUsesStatement> getUsesStatements()
  {
    return new HashSet<IUsesStatement>( _usesStmts.values() );
  }

  public void addToDefaultTypeUses( String strType )
  {
    checkLocked();
    if( strType.endsWith( ".*" ) )
    {
      // Store them so that they end with a dot
      _defaultNamespaces.add( StringPool.get( strType.substring( 0, strType.length() - 1 ) ) );
    } else {
      _defaultNamespaces.add( StringPool.get( strType ) );
    }
  }

  private void checkLocked()
  {
    if( _locked )
    {
      throw new IllegalStateException( "You cannot add to a locked TypeUsesMap.  You must make a copy of this map " +
                                       "using the .copy() method in order to mutate it." );
    }
  }

  public void addToSpecialTypeUses( String strType )
  {
    checkLocked();
    strType = strType.replace( '$', '.' );
    try
    {
      addToTypeUses( strType, _specialTypeUsesByRelativeName, _specialNamespaces );
    }
    catch( ClassNotFoundException e )
    {
      throw new RuntimeException( e );
    }
  }

  public IType resolveType( String strRelativeName )
  {
    IType result = null;
    if (strRelativeName.indexOf('.') != -1 ) {
      // Ok, if it might be absolute, ask for it by full name first.
      result = TypeLoaderAccess.instance().getByFullNameIfValid(strRelativeName);
    }
    if (result == null)
    {
      if( _specialTypeUsesByRelativeName.containsKey( strRelativeName ) )
      {
        result = TypeLoaderAccess.instance().getByFullNameIfValid(_specialTypeUsesByRelativeName.get(strRelativeName));
      }
      else if( _typeUsesByRelativeName.containsKey( strRelativeName ) )
      {
        result = TypeLoaderAccess.instance().getByFullNameIfValid(_typeUsesByRelativeName.get(strRelativeName));
      }
      else
      {
        result = resolveTypesInAllNamespaces( strRelativeName );
      }

      if (result == null) {
        result = TypeLoaderAccess.instance().getByFullNameIfValid(strRelativeName);
      }
    }

    if( result == null )
    {
      result = resolveSubType( strRelativeName );
    }
    return result;
  }

  private IType resolveSubType( String strRelativeName )
  {
    int iDot = strRelativeName.lastIndexOf( '.' );
    if( iDot > 0 && strRelativeName.length() > iDot + 1 )
    {
      String strPrefix = strRelativeName.substring( 0, iDot );
      String strRemaining = strRelativeName.substring( iDot );
      IType enclosingType = resolveType( strPrefix );
      if( enclosingType != null )
      {
        return TypeLoaderAccess.instance().getByFullNameIfValid(enclosingType.getName() + strRemaining);
      }
    }
    return null;
  }

  public void clearNonDefaultTypeUses()
  {
    _typeUsesByRelativeName.clear();
    _namespaces.clear();
    _featureSpaces.clear();
    _featureLiterals.clear();
    _usesStmts.clear();
  }

  private IType resolveTypesInAllNamespaces( String strRelativeName )
  {
    if (strRelativeName.indexOf('.') < 0) {
      for (int i = 0; i < _specialNamespaces.size; i++) {
        IType type = resolveType( strRelativeName, (String) _specialNamespaces.data[i]);
        if( type != null ) {
          return type;
        }
      }
    }

    for (int i = 0; i < _namespaces.size; i++) {
      IType type = resolveType( strRelativeName, (String) _namespaces.data[i] );
      if( type != null ) {
        return type;
      }
    }

    for (int i = 0; i < _defaultNamespaces.size; i++) {
      IType type = resolveType( strRelativeName, (String) _defaultNamespaces.data[i] );
      if( type != null ) {
        return type;
      }
    }

    return null;
  }

  private IType resolveType( String strRelativeName, String strNs ) {
    String strQualifiedName = strNs + strRelativeName;
    IType type = TypeLoaderAccess.instance().getByFullNameIfValid(strQualifiedName);
    if( type != null && !isSupportRelativePackageResolution() )
    {
      type = verifyTypeNameDoesNotHaveRelativePackage( type, strNs, strRelativeName );
    }
    return type;
  }

  // For example,
  //   type = "com.abc.Foo"
  //   strNs = "com."
  //   strRelativeName = "abc.Foo"
  //
  //   ns = abc
  //   token = abc
  //   token2 = abc
  //   return null
  private IType verifyTypeNameDoesNotHaveRelativePackage(IType type, String strNs, String strRelativeName) {
    String ns = type.getNamespace();
    if( ns != null && ns.contains(strNs) )  {
      ns = ns.substring( strNs.length() );
      //noinspection LoopStatementThatDoesntLoop
      for( StringTokenizer tokenizer = new StringTokenizer( ns, "." ); tokenizer.hasMoreTokens(); ) {
        String token = tokenizer.nextToken();
        for( StringTokenizer t2 = new StringTokenizer( strRelativeName, "." ); t2.hasMoreTokens(); ) {
          String token2 = t2.nextToken();
          if( token.equals( token2 ) ) {
            return null;
          }
        }
        return type;
      }
    }
    return type;
  }

  public INamespaceType resolveRelativeNamespaceInAllNamespaces( String strRelativeName )
  {
    if( !isSupportRelativePackageResolution() )
    {
      return null;
    }

    for (int i = 0; i < _specialNamespaces.size; i++) {
      String strQualifiedName = _specialNamespaces.data[i] + strRelativeName;
      INamespaceType type = TypeSystem.getNamespace(strQualifiedName);
      if (type != null) {
        return type;
      }
    }

    for (int i = 0; i < _namespaces.size; i++) {
      String strQualifiedName = _namespaces.data[i] + strRelativeName;
      INamespaceType type = TypeSystem.getNamespace(strQualifiedName);
      if (type != null) {
        return type;
      }
    }

    for (int i = 0; i < _defaultNamespaces.size; i++) {
      String strQualifiedName = _defaultNamespaces.data[i] + strRelativeName;
      INamespaceType type = TypeSystem.getNamespace(strQualifiedName);
      if (type != null) {
        return type;
      }
    }

    return null;
  }

  public boolean isSupportRelativePackageResolution() {
    return _bSupportRelativePackageResolution;
  }
  public void setSupportRelativePackageResolution( boolean bSupportRelativePackageResolution ) {
    _bSupportRelativePackageResolution = bSupportRelativePackageResolution;
  }

  private void addToTypeUses( String strQualifiedType, Map<String, String> mapQualifiedNameByRelativeName, List<String> namespacesSet ) throws ClassNotFoundException
  {
    checkLocked();
    if( strQualifiedType.endsWith( ".*" ) )
    {
      // Store them so that they end with a dot
      String ns = StringPool.get( strQualifiedType.substring( 0, strQualifiedType.length() - 1 ) );
      if( !namespacesSet.contains( ns ) )
      {
        namespacesSet.add( ns );
      }
    }
    else
    {
      String strRelativeName = GosuClassUtil.getNameNoPackage( strQualifiedType );
      strRelativeName = strRelativeName.replace( '$', '.' );
      mapQualifiedNameByRelativeName.put( StringPool.get( strRelativeName ), strQualifiedType );
    }
  }

}

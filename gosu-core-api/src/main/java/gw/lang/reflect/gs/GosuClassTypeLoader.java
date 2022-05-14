/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.fs.IDirectory;
import gw.lang.GosuShop;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.reflect.FragmentCache;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.RefreshKind;
import gw.lang.reflect.SimpleTypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.module.IModule;
import gw.util.concurrent.LockingLazyVar;
import manifold.api.util.DebugLogUtil;
import manifold.util.ReflectUtil;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class GosuClassTypeLoader extends SimpleTypeLoader
{
  public static final String GOSU_CLASS_FILE_EXT = ".gs";
  public static final String GOSU_ENHANCEMENT_FILE_EXT = ".gsx";
  public static final String GOSU_PROGRAM_FILE_EXT = ".gsp";
  public static final String GOSU_TEMPLATE_FILE_EXT = ".gst";
  public static final String GOSU_RULE_EXT = ".gr";
  public static final String GOSU_RULE_SET_EXT = ".grs";

  public static final String[] ALL_EXTS = {GOSU_CLASS_FILE_EXT, GOSU_ENHANCEMENT_FILE_EXT, GOSU_PROGRAM_FILE_EXT, GOSU_TEMPLATE_FILE_EXT, GOSU_RULE_EXT, GOSU_RULE_SET_EXT};
  public static final Set<String> ALL_EXTS_SET = new HashSet<String>(Arrays.asList(GOSU_CLASS_FILE_EXT, GOSU_ENHANCEMENT_FILE_EXT, GOSU_PROGRAM_FILE_EXT, GOSU_TEMPLATE_FILE_EXT, GOSU_RULE_EXT, GOSU_RULE_SET_EXT));
  public static final Set<String> EXTENSIONS = new HashSet<String>(Arrays.asList("gs", "gsx", "gsp", "gst", "gr", "grs"));

  // These constants are only here because api can't depend on impl currently; they shouldn't be considered
  // part of the API proper
  public static final String BLOCK_PREFIX = "block_";
  public static final String INNER_BLOCK_PREFIX = "." + BLOCK_PREFIX;
  public static final String BLOCK_POSTFIX = "_";

  private IGosuClassRepository _repository;
  private LockingLazyVar<IEnhancementIndex> _enhancementIndex;
  protected Set<String> _namespaces;

  public static GosuClassTypeLoader getDefaultClassLoader()
  {
    return TypeSystem.getTypeLoader( GosuClassTypeLoader.class );
  }

  public static GosuClassTypeLoader getDefaultClassLoader(IModule module)
  {
    return TypeSystem.getTypeLoader( GosuClassTypeLoader.class, module );
  }

  public GosuClassTypeLoader( IGosuClassRepository repository )
  {
    this( repository.getModule(), repository );
  }

  public GosuClassTypeLoader( IModule module, IGosuClassRepository repository )
  {
    super( module );
    _repository = repository;
    makeEnhancementIndex();
  }

  private void makeEnhancementIndex()
  {
    _enhancementIndex = new LockingLazyVar<IEnhancementIndex>() {
      private boolean _initializing;
      protected IEnhancementIndex init()
      {
        if( _initializing ) {
          return null;
        }
        _initializing = true;
        IEnhancementIndex index = GosuShop.createEnhancementIndex( GosuClassTypeLoader.this );
        index.maybeLoadEnhancementIndex();
        return index;
      }
    };
  }

  public IGosuClassRepository getRepository()
  {
    return _repository;
  }

  public IEnhancementIndex getEnhancementIndex()
  {
    return _enhancementIndex.get();
  }

  @Override
  public boolean isCaseSensitive()
  {
    return true;
  }

  @Override
  public ICompilableType getType( String strFullyQualifiedName )
  {
    _enhancementIndex.get(); // enusre enhancement index loads before any types

    int iDollar = strFullyQualifiedName.indexOf( '$' );
    if( iDollar > 0 )
    {
      // The name has a $ in it, which is the java inner class separator, but because a gosu class can be
      // precompiled this name could be coming from a gosu class file, thus we need to find the toplevel
      // type and see if it's a gosu class (note this scenario is indicative of some external tool use-case)
      IType topLevelType = TypeSystem.getByFullNameIfValid( strFullyQualifiedName.substring( 0, iDollar ) );
      if( topLevelType instanceof IGosuClass )
      {
        // The toplevel type is a gosu class, so we can assume the name is a gosu inner class name
        strFullyQualifiedName = strFullyQualifiedName.replace( '$', '.' );
      }
      else if( topLevelType != null )
      {
        // Toplevel type is not a gosu class, bail quick
        return null;
      }
    }

    IGosuClass adapterClass = getAdapterClass( strFullyQualifiedName );
    if( adapterClass != null )
    {
      return adapterClass;
    }

    if( !isBlock( strFullyQualifiedName ) )
    {
      IGosuFragment fragment = FragmentCache.instance().get( strFullyQualifiedName );
      if( fragment != null )
      {
        return fragment;
      }
    }

    IType type = TypeSystem.getCompilingType( strFullyQualifiedName );
    ITypeLoader typeLoader = type != null ? type.getTypeLoader() : null;
    if( type != null && typeLoader != this )
    {
      return null;
    }

    if( type == null )
    {
      return findClass( strFullyQualifiedName );
    }

    return (IGosuClass)type;
  }

  private IGosuClass getAdapterClass( String strFullyQualifiedName )
  {
    if( getClass() == GosuClassTypeLoader.class )
    {
      if( strFullyQualifiedName.length() > IGosuClass.PROXY_PREFIX.length() &&
          strFullyQualifiedName.startsWith( IGosuClass.PROXY_PREFIX ) )
      {
        IType javaType = TypeSystem.getByFullNameIfValid( IGosuClass.ProxyUtil.getNameSansProxy( strFullyQualifiedName ) );
        if( javaType instanceof IJavaType )
        {
          IGosuClass adapterClass = ((IJavaType)javaType).getAdapterClass();
          if( adapterClass == null )
          {
            adapterClass = ((IJavaType)javaType).createAdapterClass();
          }
          return adapterClass;
        }
      }
    }
    return null;
  }

  @Override
  public Set<String> getAllNamespaces()
  {
    if( _namespaces == null )
    {
      try
      {
        _namespaces = TypeSystem.getNamespacesFromTypeNames( getAllTypeNames(), new HashSet<String>() );
        _namespaces.add( "Libraries" );
      }
      catch( NullPointerException e )
      {
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
    _repository.namespaceRefreshed(namespace, dir, kind);
  }

  @Override
  public URL getResource(String name) {
    return _repository.findResource(name);
  }

  @Override
  public Set<String> computeTypeNames()
  {
    return _repository.getAllTypeNames(getAllExtensions());
  }

  @Override
  public void refreshedImpl()
  {
    _namespaces = null;
    _repository.typesRefreshed(null);
  }

  @Override
  public List<String> getHandledPrefixes()
  {
    return Collections.emptyList();
  }

  @Override
  public boolean handlesNonPrefixLoads() {
    return true;
  }

  public IGosuClass makeNewClass( ISourceFileHandle sourceFile )
  {
    return makeNewClass( sourceFile, null );
  }

  public IGosuClass makeNewClass( ISourceFileHandle sourceFile, ISymbolTable programSymTable )
  {
    switch( sourceFile.getClassType() )
    {
      case Class:
      case Interface:
      case Annotation:
      case Structure:
      case Enum:
        return GosuShop.createClass( sourceFile.getTypeNamespace(), sourceFile.getRelativeName(), this, sourceFile, getTypeUsesMap() );

      case Enhancement:
        return GosuShop.createEnhancement( sourceFile.getTypeNamespace(), sourceFile.getRelativeName(), this, sourceFile, getTypeUsesMap() );

      case Program:
        return GosuShop.createProgram( sourceFile.getTypeNamespace(), sourceFile.getRelativeName(), this, sourceFile, getTypeUsesMap(), programSymTable );

      case Template:
        return GosuShop.createTemplate( sourceFile.getTypeNamespace(), sourceFile.getRelativeName(), this, sourceFile, getTypeUsesMap(), programSymTable );

      case Eval:
        return GosuShop.createProgramForEval( sourceFile.getTypeNamespace(), sourceFile.getRelativeName(), this, sourceFile, getTypeUsesMap(), programSymTable );

      default:
        throw new IllegalStateException( "Unhandled class type: " + sourceFile.getClassType() );
    }
  }

  private IGosuClass getBlockType( String strName )
  {
    IGosuClass classInternal = null;
    if( isBlock( strName ) )
    {
      try
      {
        String strippedName = strName.substring( 0, strName.length() - BLOCK_POSTFIX.length() );
        int iStr = strippedName.lastIndexOf( INNER_BLOCK_PREFIX );
        String indexStr = strippedName.substring( iStr + INNER_BLOCK_PREFIX.length(), strippedName.length() );
        int i = Integer.parseInt( indexStr );
        String enclosingClassStr = strippedName.substring( 0, iStr );

        IType type = getBlockType( enclosingClassStr );
        if( type == null )
        {
          type = TypeSystem.getByFullNameIfValid( enclosingClassStr.replace( '$', '.' ), getModule() );
        }

        // this module check is to make sure that the block class is in the same module as the parent class
        // otherwise the Rule loader loads block classes!
        if( type instanceof ICompilableType && type.getTypeLoader().getModule() == _module )
        {
          classInternal = ((ICompilableType)type).getBlock( i );
        }
      }
      catch( NumberFormatException e )
      {
        //ignore
      }
      catch( IndexOutOfBoundsException e )
      {
        //ignore
      }
    }
    return classInternal;
  }

  private boolean isBlock( String strName )
  {
    return strName.endsWith( BLOCK_POSTFIX ) && strName.contains( INNER_BLOCK_PREFIX );
  }


  private IGosuClass findClass( String strQualifiedClassName )
  {
    IGosuClass blockType = getBlockType( strQualifiedClassName );
    if( blockType != null )
    {
      return blockType;
    }

    ISourceFileHandle sourceFile = _repository.findClass( strQualifiedClassName, getAllExtensions());

    if( sourceFile == null || !isValidSourceFileHandle( sourceFile ) || !sourceFile.isValid() || !strQualifiedClassName.endsWith( sourceFile.getRelativeName() ) )
    {
      return null;
    }

    final IGosuClass gsClass;
    if( sourceFile.getParentType() != null )
    {
      // It's an inner class
      final IType type = TypeSystem.getByFullNameIfValid(sourceFile.getParentType());
      //we have to check instance of type as it can return JavaType
      //this happens when you have Gosu class which has the same name as any java package
      //in this case you shadow entire java package with gosu class
      if (type instanceof IGosuClass) {
        IGosuClass enclosingType = (IGosuClass) type;
        gsClass = enclosingType.getInnerClass(sourceFile.getRelativeName());
      }
      else
      {
        gsClass = null;
      }
    }
    else
    {
      // It's a top-level class
      gsClass = makeNewClass( sourceFile );
    }

    return gsClass;
  }

  protected boolean isValidSourceFileHandle( ISourceFileHandle sourceFile )
  {
    return sourceFile.getClassType().isGosu();
  }

  protected String[] getAllExtensions()
  {
    return ALL_EXTS;
  }

  protected ITypeUsesMap getTypeUsesMap()
  {
    return null;
  }

  @Override
  public void refreshedTypesImpl(RefreshRequest request) {
    _repository.typesRefreshed(request);
    _enhancementIndex.get().refreshedTypes(request);
  }

  public boolean shouldKeepDebugInfo( IGosuClass gsClass )
  {
    return gsClass.shouldKeepDebugInfo();
  }

  @Override
  public Set<String> getExtensions() {
    return EXTENSIONS;
  }

  @Override
  public Set<TypeName> getTypeNames(String namespace) {
    return _repository.getTypeNames(namespace, ALL_EXTS_SET, this);
  }

  @Override
  public boolean hasNamespace(String namespace) {
    return _repository.hasNamespace(namespace) > 0;
  }

}

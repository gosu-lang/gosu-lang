/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WildcardTree;
import com.sun.source.util.DocTrees;
import com.sun.source.util.SourcePositions;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeInfo;
import gw.config.ExecutionMode;
import gw.internal.gosu.parser.TypeUsesMap;
import gw.internal.gosu.parser.java.compiler.JavaStubGenerator;
import gw.lang.GosuShop;
import gw.lang.javac.ClassJavaFileObject;
import gw.lang.javac.IJavaParser;
import gw.lang.javac.JavaCompileIssuesException;
import gw.lang.javadoc.IClassDocNode;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.EnumValuePlaceholder;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IDefaultTypeLoader;
import gw.lang.reflect.IEnumValue;
import gw.lang.reflect.ILocationInfo;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.gs.LazyStringSourceFileHandle;
import gw.lang.reflect.gs.StringSourceFileHandle;
import gw.lang.reflect.java.AbstractJavaClassInfo;
import gw.lang.reflect.java.ErrorJavaClassInfo;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaClassField;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassTypeVariable;
import gw.lang.reflect.java.IJavaClassWildcardType;
import gw.lang.reflect.java.IJavaMethodDescriptor;
import gw.lang.reflect.java.IJavaPropertyDescriptor;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.ITypeInfoResolver;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;
import gw.util.GosuClassUtil;
import gw.util.GosuExceptionUtil;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

public abstract class JavaSourceType extends AbstractJavaClassInfo implements ITypeInfoResolver
{
  public static final int IGNORE_NONE = 0;
  public static final int IGNORE_INTERFACES = 1;
  public static final int IGNORE_SUPERCLASS = 2;

  private static Map<String, Class> PRIMITIVES = new HashMap<>();

  static
  {
    PRIMITIVES.put( int.class.getName(), int.class );
    PRIMITIVES.put( byte.class.getName(), byte.class );
    PRIMITIVES.put( char.class.getName(), char.class );
    PRIMITIVES.put( short.class.getName(), short.class );
    PRIMITIVES.put( long.class.getName(), long.class );
    PRIMITIVES.put( float.class.getName(), float.class );
    PRIMITIVES.put( double.class.getName(), double.class );
    PRIMITIVES.put( boolean.class.getName(), boolean.class );
    PRIMITIVES.put( void.class.getName(), void.class );
  }

  private static final Object CACHE_MISS = new Object()
  {
    public String toString()
    {
      return "cache miss";
    }
  };

  protected IModule _gosuModule;
  protected String _fullyQualifiedName;
  protected String _namespace;
  protected String _simpleName;
  protected List<String> _importList;
  protected JavaSourceType _enclosingClass;
  protected IJavaClassField[] _fields;
  protected IJavaClassMethod[] _methods;
  protected JavaSourceModifierList _modifiersList;
  protected IJavaClassInfo[] _innerClasses;
  protected IJavaClassConstructor[] _constructors;
  protected IJavaClassInfo[] _interfaces;
  protected IJavaClassType _genericSuperClass;
  protected IJavaClassTypeVariable[] _typeParameters;
  protected IJavaClassType[] _genericInterfaces;
  private IJavaClassField[] _allFields;
  private Object[] _enumConstants;
  private IJavaPropertyDescriptor[] _propertyDescriptors;
  private IJavaClassInfo _superClass;
  private IJavaMethodDescriptor[] _methodDescriptors;
  protected Map<String, Object> _cache;
  private ISourceFileHandle _fileHandle;
  private List<String> _staticImportList;
  private CompilationUnitTree _compilationUnitTree;
  private SourcePositions _sourcePositions;
  private ClassTree _typeDecl;
  private IType _javaType;

  public static IJavaClassInfo createTopLevel( ISourceFileHandle fileHandle, IModule gosuModule )
  {
    return createTopLevel( fileHandle, gosuModule, null );
  }
  public static IJavaClassInfo createTopLevel( ISourceFileHandle fileHandle, IModule gosuModule, DiagnosticCollector<JavaFileObject> errorHandler )
  {
    List<CompilationUnitTree> trees = new ArrayList<>();
    SourcePositions[] sourcePositions = {null};
    if( !parseJavaFile( fileHandle, trees, sourcePositions, errorHandler, null ) )
    {
      return new JavaSourceUnresolvedClass( fileHandle, gosuModule );
    }

    ClassTree def = getTopLevelDefinition( trees, fileHandle.getRelativeName() );
    if( def != null )
    {
      JavaSourceType result = null;
      final Tree.Kind kind = def.getKind();
      final List<? extends ImportTree> imports = trees.get( 0 ).getImports();
      switch( kind )
      {
        case CLASS:
          result = new JavaSourceClass( fileHandle, trees.get( 0 ), def, imports, sourcePositions[0], gosuModule );
          break;
        case INTERFACE:
          result = new JavaSourceInterface( fileHandle, trees.get( 0 ), def, imports, sourcePositions[0], gosuModule );
          break;
        case ENUM:
          result = new JavaSourceEnum( fileHandle, trees.get( 0 ), def, imports, sourcePositions[0], gosuModule );
          break;
        case ANNOTATION_TYPE:
          result = new JavaSourceAnnotation( fileHandle, trees.get( 0 ), def, imports, sourcePositions[0], gosuModule );
          break;
      }
      return result;
    }
    else
    {
      return new JavaSourceUnresolvedClass( fileHandle, gosuModule );
    }
  }

  private static ClassTree getTopLevelDefinition( List<CompilationUnitTree> trees, String relativeName )
  {
    for( CompilationUnitTree cut: trees )
    {
      //## todo: support multiple top-level classes declared in a single java source file
      for( Tree t : cut.getTypeDecls() )
      {
        if( t instanceof ClassTree &&
            relativeName.equals( ((ClassTree)t).getSimpleName().toString() ) )
        {
          return (ClassTree)t;
        }
      }
    }
    return null;
  }

  private static boolean parseJavaFile( ISourceFileHandle src, List<CompilationUnitTree> trees, SourcePositions[] sourcePositions, DiagnosticCollector<JavaFileObject> errorHandler, DocTrees[] docTrees )
  {
    IJavaParser javaParser = GosuParserFactory.getInterface( IJavaParser.class );
    return javaParser.parseText( src.getSource().getSource().replace( "\r\n", "\n" ), trees, sp -> sourcePositions[0] = sp, dc -> {if( docTrees != null ) docTrees[0] = dc;}, errorHandler );
  }

  private static JavaSourceType createInner( ClassTree typeDecl, JavaSourceType containingClass )
  {
    final Tree.Kind kind = typeDecl.getKind();
    switch( kind )
    {
      case CLASS:
        return new JavaSourceClass( typeDecl, containingClass );
      case INTERFACE:
        return new JavaSourceInterface( typeDecl, containingClass );
      case ENUM:
        return new JavaSourceEnum( typeDecl, containingClass );
      case ANNOTATION_TYPE:
        return new JavaSourceAnnotation( typeDecl, containingClass );
      default:
        throw new RuntimeException( "unsupported node type" );
    }
  }

  private static IJavaClassType resolveParameterizedArrayType( ITypeInfoResolver typeResolver, Tree tree, String typeName )
  {
    if( !(tree instanceof ArrayTypeTree) )
    {
      return resolveParameterizedType( typeResolver, tree, typeName );
    }
    IJavaClassType type = resolveParameterizedArrayType( typeResolver, ((ArrayTypeTree)tree).getType(), typeName.substring( 0, typeName.length() - 2 ) );
    return new JavaSourceArrayType( type );
  }

  public static IJavaClassType createType( ITypeInfoResolver typeResolver, Tree tree )
  {
    final String typeName = getTypeName( tree );

    if( tree instanceof ArrayTypeTree &&
        ((ArrayTypeTree)tree).getType() instanceof ParameterizedTypeTree )
    {
      return resolveParameterizedArrayType( typeResolver, tree, typeName );
    }
    else if( tree instanceof ParameterizedTypeTree )
    {
      return resolveParameterizedType( typeResolver, tree, typeName );
    }
    else if( tree instanceof WildcardTree )
    {
      WildcardTree wildcardTree = (WildcardTree)tree;
      final Tree.Kind kind = wildcardTree.getKind();
      if( kind == Tree.Kind.UNBOUNDED_WILDCARD )
      {
        // NULL_TYPE is just a placeholder so the call can use the context type to which this wildcard is an argument to make the actual wildcard type
        return new JavaWildcardType( NULL_TYPE, false );
      }
      return new JavaWildcardType( createType( typeResolver, wildcardTree.getBound() ), kind == Tree.Kind.SUPER_WILDCARD );
    }
    else if( tree instanceof IdentifierTree ||
             tree instanceof PrimitiveTypeTree ||
             tree instanceof ArrayTypeTree ||
             tree instanceof MemberSelectTree )
    {
      return createType( typeResolver, typeName, IGNORE_NONE );
    }
    throw new RuntimeException( "Type tree '" + tree + "' of kind: " + tree.getKind() + " has not been handled" );
  }

  private static IJavaClassType resolveParameterizedType( ITypeInfoResolver typeResolver, Tree typeNode, String typeName )
  {
    final List<? extends Tree> typeArguments = ((ParameterizedTypeTree)typeNode).getTypeArguments();
    IJavaClassType[] typeParameters = new IJavaClassType[typeArguments.size()];
    IJavaClassType concreteType = createType( typeResolver, typeName, IGNORE_NONE );
    if( concreteType == null || concreteType instanceof ErrorJavaClassInfo )
    {
      return ERROR_TYPE;
    }
    IJavaClassTypeVariable[] parameters = ((IJavaClassInfo)concreteType).getTypeParameters();
    if( parameters.length != typeParameters.length )
    {
      throw new IllegalStateException();
    }
    for( int i = 0; i < typeParameters.length; i++ )
    {
      Tree tree = typeArguments.get( i );
      typeParameters[i] = createType( typeResolver, tree );
      if( typeParameters[i] instanceof IJavaClassWildcardType )
      {
        IJavaClassType bound = ((JavaWildcardType)typeParameters[i]).getUpperBound();
        if( bound == IJavaClassType.NULL_TYPE )
        {
          bound = ((IJavaClassInfo)concreteType).getTypeParameters()[i].getBounds()[0];
          ((JavaWildcardType)typeParameters[i]).setBound( bound );
        }
      }
    }
    return new JavaParameterizedType( typeParameters, concreteType );
  }

  public static IJavaClassType createType( ITypeInfoResolver typeResolver, String typeName, int ignoreFlags )
  {
    if( typeName.endsWith( "[]" ) )
    {
      String typeNameNoArray = typeName.substring( 0, typeName.length() - 2 );
      IJavaClassType componentType = createType( typeResolver, typeNameNoArray, ignoreFlags );
      if( componentType instanceof IJavaClassInfo )
      {
        return new JavaArrayClassInfo( (IJavaClassInfo)componentType );
      }
      else
      {
        return new JavaSourceArrayType( componentType );
      }
    }
    else
    {
      IJavaClassType type = typeResolver.resolveType( typeName, ignoreFlags );
      return type == null ? ERROR_TYPE : type;
    }
  }

  /**
   * For top level classes.
   */
  protected JavaSourceType( ISourceFileHandle fileHandle, CompilationUnitTree compilationUnitTree, ClassTree typeDecl, List<? extends ImportTree> imports, SourcePositions sourcePositions, IModule gosuModule )
  {
    _fileHandle = fileHandle;
    _namespace = fileHandle.getNamespace();
    _simpleName = fileHandle.getRelativeName();
    _gosuModule = gosuModule;
    makeImportList( imports );
    _typeDecl = typeDecl;
    _compilationUnitTree = compilationUnitTree;
    _sourcePositions = sourcePositions;
    _cache = new HashMap<>();
  }

  /**
   * For inner classes.
   */
  protected JavaSourceType( ClassTree typeDecl, JavaSourceType enclosingClass )
  {
    _enclosingClass = enclosingClass;
    _gosuModule = enclosingClass.getModule();
    _cache = new HashMap<>();
    _namespace = enclosingClass.getNamespace();
    _typeDecl = typeDecl;
    _simpleName = _typeDecl.getSimpleName().toString();
  }

  protected void makeImportList( List<? extends ImportTree> imports )
  {
    List<String> staticImportList = new ArrayList<>( imports.size() );
    List<String> importList = new ArrayList<>( imports.size() );

    importList.add( "java.lang.*" );
    for( ImportTree it : imports )
    {
      final JCTree qualifiedIdentifier = (JCTree)it.getQualifiedIdentifier();
      final String importStr = TreeInfo.fullName( qualifiedIdentifier ).toString();
      importList.add( importStr );
      if( it.isStatic() )
      {
        staticImportList.add( importStr );
      }
    }
    importList.sort( ( s1, s2 ) -> {
      if( s1.endsWith( "*" ) && !s2.endsWith( "*" ) )
      {
        return +1;
      }
      if( s2.endsWith( "*" ) && !s1.endsWith( "*" ) )
      {
        return -1;
      }
      return s1.compareTo(s2);
    } );
    _importList = importList;
    _staticImportList = staticImportList;
  }

  public IModule getModule()
  {
    return _gosuModule;
  }

  @Override
  public String getNameSignature()
  {
    return GosuShop.toSignature( getName() );
  }

  @Override
  public String getRelativeName()
  {
    return getName().substring( getNamespace().length() + 1 );
  }

  @Override
  public String getDisplayName()
  {
    return getSimpleName();
  }

  @Override
  public String getSimpleName()
  {
    return _simpleName;
  }

  @Override
  public boolean isArray()
  {
    return false;
  }

  public String getName()
  {
    if( _fullyQualifiedName == null )
    {
      if( _enclosingClass == null )
      {
        _fullyQualifiedName = _namespace + "." + getSimpleName();
      }
      else
      {
        _fullyQualifiedName = _enclosingClass.getName() + "." + getSimpleName();
      }
    }
    return _fullyQualifiedName;
  }

  @Override
  public IJavaClassInfo[] getInterfaces()
  {
    if( _interfaces == null )
    {
      TypeSystem.lock();
      try
      {
        if( _interfaces == null )
        {
          final List<? extends Tree> implementsClause = _typeDecl.getImplementsClause();
          if( implementsClause == null )
          {
            _interfaces = IJavaClassType.EMPTY_ARRAY;
            return _interfaces;
          }
          IJavaClassInfo[] interfaces = new IJavaClassInfo[implementsClause.size()];
          for( int i = 0; i < interfaces.length; i++ )
          {
            Tree clause = implementsClause.get( i );
            String typeName = getTypeName( clause );
            IJavaClassInfo classInfo = (IJavaClassInfo)createType( this, typeName, IGNORE_SUPERCLASS | IGNORE_INTERFACES );
            interfaces[i] = classInfo;
          }
          _interfaces = interfaces;
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return _interfaces;
  }


  static public String getTypeName( Tree tree )
  {
    return tree.toString().replaceAll( "<.*>", "" );
  }

  @Override
  public IJavaClassType[] getGenericInterfaces()
  {
    TypeSystem.lock();
    try
    {
      if( _genericInterfaces == null )
      {
        final List<? extends Tree> implementsClause = _typeDecl.getImplementsClause();
        if( implementsClause.isEmpty() )
        {
          _genericInterfaces = IJavaClassType.EMPTY_ARRAY;
          return _genericInterfaces;
        }
        IJavaClassType[] genericInterfaces = new IJavaClassType[implementsClause.size()];
        for( int i = 0; i < genericInterfaces.length; i++ )
        {
          genericInterfaces[i] = createType( this, implementsClause.get( i ) );
        }
        _genericInterfaces = genericInterfaces;
      }
      return _genericInterfaces;
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  @Override
  public IJavaClassInfo getSuperclass()
  {
    if( _superClass == null )
    {
      if( isInterface() )
      {
        _superClass = NULL_TYPE;
      }
      else if( isEnum() )
      {
        _superClass = JavaTypes.ENUM().getBackingClassInfo();
      }
      else
      {
        final Tree extendsClause = _typeDecl.getExtendsClause();
        _superClass = extendsClause != null
                      ? (IJavaClassInfo)createType( this, getTypeName( extendsClause ), IGNORE_SUPERCLASS | IGNORE_INTERFACES )
                      : JavaTypes.OBJECT().getBackingClassInfo();
      }
      if( hasCyclicInheritance( _superClass ) )
      {
        _superClass = NULL_TYPE;
      }
    }
    return _superClass == NULL_TYPE ? null : _superClass;
  }

  private boolean hasCyclicInheritance( IJavaClassInfo superClass )
  {
    Set<IJavaClassInfo> visited = new HashSet<>();
    visited.add( this );
    while( superClass != null && !visited.contains( superClass ) )
    {
      visited.add( superClass );
      superClass = superClass.getSuperclass();
    }
    return superClass != null;
  }

  @Override
  public IJavaClassType getGenericSuperclass()
  {
    if( _genericSuperClass == null )
    {
      if( isInterface() )
      {
        _genericSuperClass = NULL_TYPE;
      }
      else if( isEnum() )
      {
        _genericSuperClass = JavaTypes.ENUM().getBackingClassInfo();
      }
      else
      {
        final Tree extendsClause = _typeDecl.getExtendsClause();
        _genericSuperClass = extendsClause != null ? createType( this, extendsClause ) : NULL_TYPE;
      }
    }
    return _genericSuperClass == NULL_TYPE ? null : _genericSuperClass;
  }

  public void initMethodsAndConstructors()
  {
    final List<? extends Tree> members = _typeDecl.getMembers();
    List<IJavaClassMethod> methods = new ArrayList<>();
    List<IJavaClassConstructor> constructors = new ArrayList<>();
    //noinspection Convert2streamapi
    for( Tree t : members )
    {
      if( t.getKind() == Tree.Kind.METHOD )
      {
        final MethodTree m = (MethodTree)t;
        JavaSourceMethod method = JavaSourceMethod.create( m, this );
        if( method != null )
        {
          if( method.isConstructor() )
          {
            constructors.add( (IJavaClassConstructor)method );
          }
          else
          {
            methods.add( method );
          }
        }
      }
    }
    if( isEnum() )
    {
      methods.add( new SyntheticJavaMethod( this, this, this, "valueOf",
                                            Modifier.STATIC | Modifier.PUBLIC, new IJavaClassInfo[]{
        TypeSystem.getJavaClassInfo( String.class )
      }, new IJavaClassInfo[0] ) );
      methods.add( new SyntheticJavaMethod( this, this.getArrayType(), this.getArrayType(), "values",
                                            Modifier.STATIC | Modifier.PUBLIC, new IJavaClassInfo[0], new IJavaClassInfo[0] ) );
    }
    if( isClass() && constructors.size() == 0 )
    {
      constructors.add( new JavaSourceDefaultConstructor( this ) );
    }
    _methods = methods.toArray( new IJavaClassMethod[methods.size()] );
    _constructors = constructors.toArray( new IJavaClassConstructor[constructors.size()] );
  }

  public IJavaClassMethod[] getDeclaredMethods()
  {
    if( _methods == null )
    {
      initMethodsAndConstructors();
    }
    return _methods;
  }

  public IJavaClassConstructor[] getDeclaredConstructors()
  {
    if( _constructors == null )
    {
      initMethodsAndConstructors();
    }
    return _constructors;
  }

  public IJavaClassConstructor getConstructor( IJavaClassInfo... paramTypes ) throws NoSuchMethodException
  {
    outer:
    for( IJavaClassConstructor ctor : getDeclaredConstructors() )
    {
      IJavaClassInfo[] methodParamTypes = ctor.getParameterTypes();
      if( paramTypes.length != methodParamTypes.length )
      {
        continue;
      }
      for( int i = 0; i < paramTypes.length; i++ )
      {
        if( !paramTypes[i].equals( methodParamTypes[i] ) )
        {
          continue outer;
        }
      }
      return ctor;
    }
    throw new NoSuchMethodException();
  }

  public IJavaClassField[] getDeclaredFields()
  {
    if( _fields == null )
    {
      List<VariableTree> fieldTrees = new ArrayList<>();
      final List<? extends Tree> members = _typeDecl.getMembers();
      //noinspection Convert2streamapi
      for( Tree t : members )
      {
        if( t.getKind() == Tree.Kind.VARIABLE )
        {
          fieldTrees.add( (VariableTree)t );
        }
      }
      _fields = new JavaSourceField[fieldTrees.size()];
      for( int i = 0; i < _fields.length; i++ )
      {
        _fields[i] = JavaSourceField.create( fieldTrees.get( i ), this );
      }
    }
    return _fields;
  }

  public IJavaClassField[] getFields()
  {
    if( _allFields == null )
    {
      List<IJavaClassField> fields = new ArrayList<>();
      IJavaClassField[] declaredFields = getDeclaredFields();
      for( int i = 0; i < declaredFields.length; i++ )
      {
        IJavaClassField field = declaredFields[i];
        if( Modifier.isPublic( field.getModifiers() ) )
        {
          fields.add( field );
        }
      }
      IJavaClassInfo superclass = getSuperclass();
      if( superclass != null )
      {
        fields.addAll( Arrays.asList( superclass.getFields() ) );
      }
      _allFields = fields.toArray( new IJavaClassField[fields.size()] );
    }
    return _allFields;
  }

  @Override
  public Object[] getEnumConstants()
  {
    if( _enumConstants == null )
    {
      List<IEnumValue> enums = new ArrayList<>();
      IJavaClassField[] fields = getFields();
      for( IJavaClassField field : fields )
      {
        if( field.isEnumConstant() )
        {
          enums.add( new EnumValuePlaceholder( field.getName() ) );
        }
      }
      _enumConstants = enums.toArray( new IEnumValue[enums.size()] );
    }
    return _enumConstants;
  }

  @Override
  public IJavaPropertyDescriptor[] getPropertyDescriptors()
  {
    if( _propertyDescriptors == null )
    {
      _propertyDescriptors = PropertyDeriver.initPropertyDescriptors( this );
    }
    return _propertyDescriptors;
  }

  @Override
  public IType getJavaType()
  {
    return _javaType == null ? (_javaType = TypeSystem.get( this )) : _javaType;
  }
  public void setJavaType( IJavaType javaType )
  {
    _javaType = javaType;
  }

  public IJavaClassTypeVariable[] getTypeParameters()
  {
    if( _typeParameters == null )
    {
      List<? extends TypeParameterTree> typeParameters = _typeDecl.getTypeParameters();
      if( !typeParameters.isEmpty() )
      {
        _typeParameters = new IJavaClassTypeVariable[typeParameters.size()];
        for( int i = 0; i < _typeParameters.length; i++ )
        {
          _typeParameters[i] = JavaSourceTypeVariable.create( this, typeParameters.get( i ) );
        }
      }
      else
      {
        _typeParameters = JavaSourceTypeVariable.EMPTY;
      }
    }
    return _typeParameters;
  }

  @Override
  public IClassDocNode createClassDocNode()
  {
    return null;
  }

  @Override
  public boolean hasCustomBeanInfo()
  {
    return false;
  }

  @Override
  public boolean isVisibleViaFeatureDescriptor( IScriptabilityModifier constraint )
  {
    return true;
  }

  @Override
  public boolean isHiddenViaFeatureDescriptor()
  {
    return false;
  }

  @Override
  public IJavaClassInfo getComponentType()
  {
    return null;
  }

  @Override
  public int getModifiers()
  {
    return getModifierList().getModifiers() | ((isInterface() && getEnclosingClass() != null) ? Modifier.STATIC : 0);
  }

  public IModifierList getModifierList()
  {
    if( _modifiersList == null )
    {
      _modifiersList = new JavaSourceModifierList( this, _typeDecl.getModifiers() );
    }
    return _modifiersList;
  }

  public IType getEnclosingType()
  {
    return _enclosingClass == null ? null : TypeSystem.get( _enclosingClass );
  }

  public String getNamespace()
  {
    return _namespace;
  }

  @Override
  public IJavaClassInfo getArrayType()
  {
    return new JavaArrayClassInfo( this );
  }

  @Override
  public IJavaClassInfo[] getDeclaredClasses()
  {
    if( _innerClasses == null )
    {
      final List<? extends Tree> members = _typeDecl.getMembers();
      List<ClassTree> innerTrees = new ArrayList<>( members.size() );
      //noinspection Convert2streamapi
      for( Tree t : members )
      {
        if( t.getKind() == Tree.Kind.CLASS ||
            t.getKind() == Tree.Kind.INTERFACE ||
            t.getKind() == Tree.Kind.ENUM ||
            t.getKind() == Tree.Kind.ANNOTATION_TYPE )
        {
          innerTrees.add( (ClassTree)t );
        }
      }
      List<JavaSourceType> innerClasses = new ArrayList<>( innerTrees.size() );
      //noinspection ForLoopReplaceableByForEach
      for( int i = 0; i < innerTrees.size(); i++ )
      {
        JavaSourceType inner = JavaSourceType.createInner( innerTrees.get( i ), this );
        innerClasses.add( inner );
      }
      _innerClasses = innerClasses.toArray( new JavaSourceType[innerClasses.size()] );
    }
    return _innerClasses;
  }

  @Override
  public Class getBackingClass()
  {
    if( !ExecutionMode.isRuntime() )
    {
      return null;
    }

    try
    {
      return Class.forName( getJavaName(), false, TypeSystem.getCurrentModule().getModuleClassLoader() );
    }
    catch( ClassNotFoundException e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  private String getJavaName()
  {
    if( _enclosingClass != null )
    {
      return _enclosingClass.getJavaName() + '$' + getSimpleName();
    }
    return getName();
  }

  public IJavaClassInfo getInnerClass( String relativeName )
  {
    for( IJavaClassInfo innerClass : getDeclaredClasses() )
    {
      if( innerClass.getSimpleName().equals( relativeName ) )
      {
        return innerClass;
      }
    }
    return null;
  }

  @Override
  public IType getActualType( TypeVarToTypeMap typeMap )
  {
    return getActualType( typeMap, false );
  }

  @Override
  public IType getActualType( TypeVarToTypeMap typeMap, boolean bKeepTypeVars )
  {
    return TypeSystem.getActualType( getJavaType(), typeMap, bKeepTypeVars );
  }

  @Override
  public IJavaClassType getConcreteType()
  {
    return this;
  }

  public boolean isClass()
  {
    return this instanceof JavaSourceClass;
  }

  public boolean isInterface()
  {
    return this instanceof JavaSourceInterface;
  }

  @Override
  public Object newInstance() throws InstantiationException, IllegalAccessException
  {
    return null;
  }

  @Override
  public IJavaClassMethod getMethod( String methodName, IJavaClassInfo... paramTypes ) throws NoSuchMethodException
  {
    outer:
    for( IJavaClassMethod method : getDeclaredMethods() )
    {
      if( !method.getName().equals( methodName ) )
      {
        continue;
      }
      IJavaClassInfo[] methodParamTypes = method.getParameterTypes();
      if( paramTypes.length != methodParamTypes.length )
      {
        continue;
      }
      for( int i = 0; i < paramTypes.length; i++ )
      {
        if( !paramTypes[i].equals( methodParamTypes[i] ) )
        {
          continue outer;
        }
      }
      return method;
    }
    throw new NoSuchMethodException();
  }

  public IJavaClassMethod getDeclaredMethod( String methodName, IJavaClassInfo... paramTypes ) throws NoSuchMethodException
  {
    return getMethod( methodName, paramTypes );
  }

  @Override
  public IJavaMethodDescriptor[] getMethodDescriptors()
  {
    if( _methodDescriptors == null )
    {
      IJavaClassMethod[] declaredMethods = getDeclaredMethods();
      _methodDescriptors = new IJavaMethodDescriptor[declaredMethods.length];
      for( int i = 0; i < declaredMethods.length; i++ )
      {
        _methodDescriptors[i] = new JavaSourceMethodDescriptor( declaredMethods[i] );
      }
    }
    return _methodDescriptors;
  }

  public boolean isEnum()
  {
    return this instanceof JavaSourceEnum;
  }

  @Override
  public boolean isPrimitive()
  {
    return false;
  }

  public boolean isAnnotation()
  {
    return this instanceof JavaSourceAnnotation;
  }

  public boolean isPublic()
  {
    return getModifierList().hasModifier( Modifier.PUBLIC );
  }

  public boolean isProtected()
  {
    return getModifierList().hasModifier( Modifier.PROTECTED );
  }

  public boolean isPrivate()
  {
    return getModifierList().hasModifier( Modifier.PRIVATE );
  }

  public boolean isInternal()
  {
    return getModifierList().hasModifier( Modifier.INTERNAL ) ||
           (!getModifierList().hasModifier( Modifier.PUBLIC ) &&
            !getModifierList().hasModifier( Modifier.PROTECTED ) &&
            !getModifierList().hasModifier( Modifier.PRIVATE ));
  }

  public IJavaClassType resolveType( String relativeName, int ignoreFlags )
  {
    return resolveType( relativeName, this, ignoreFlags );
  }

  public IJavaClassType resolveType( String relativeName, IJavaClassInfo whosAskin, int ignoreFlags )
  {
    Object cachedOrCyclicType = getCachedDetectCyclicType( relativeName );
    if( cachedOrCyclicType != null )
    {
      return cachedOrCyclicType == CACHE_MISS ? null : (IJavaClassType)cachedOrCyclicType;
    }
    IJavaClassType type = null;
    try
    {

      // Primitives
      if( PRIMITIVES.containsKey( relativeName ) )
      {
        return type = JavaTypes.getJreType( PRIMITIVES.get( relativeName ) ).getBackingClassInfo();
      }

      // Qualified or Semi-qualified Type e.g., Outer.Inner
      int iDot = relativeName.indexOf( "." );
      if( iDot == 0 )
      {
        return type = null;
      }
      else if( iDot >= 0 )
      {
        return type = resolveQualifiedOrSemiQualifiedType( relativeName, whosAskin, iDot, ignoreFlags );
      }

      // Identity
      if( relativeName.equals( getSimpleName() ) )
      {
        return type = this;
      }

      // Direct Inner Classes (no recursion)
      IJavaClassType innerClass = JavaSourceUtil.resolveInnerClass( this, relativeName, whosAskin );
      if( innerClass != null )
      {
        return type = innerClass;
      }

      // Imported Class
      IJavaClassType importedType = resolveImport( relativeName );
      if( importedType != null )
      {
        return type = importedType;
      }

      // type variable
      for( IJavaClassTypeVariable typeParameter : getTypeParameters() )
      {
        if( relativeName.equals( typeParameter.getName() ) )
        {
          if( typeParameter instanceof JavaSourceTypeVariable )
          {
            typeParameter = ((JavaSourceTypeVariable)typeParameter).copy();
          }
          return type = typeParameter;
        }
      }

      // Enclosing Class
      IJavaClassInfo enclosingClass = getEnclosingClass();
      if( enclosingClass != null )
      {
        IJavaClassType outerClsCtx = enclosingClass.resolveType( relativeName, whosAskin, IGNORE_NONE );
        if( outerClsCtx != null )
        {
          return type = outerClsCtx;
        }
      }

      // Class in Same Package
      IJavaClassType neighborClass = resolveClassInSamePackage( relativeName );
      if( neighborClass != null )
      {
        return type = neighborClass;
      }

      // Super Ancestry
      if( (ignoreFlags & IGNORE_SUPERCLASS) == 0 )
      {
        IJavaClassInfo superClass = getSuperclass();
        if( superClass != null )
        {
          IJavaClassType outerClsCtx = superClass.resolveType( relativeName, whosAskin, ignoreFlags );
          if( outerClsCtx != null )
          {
            return type = outerClsCtx;
          }
        }
      }

      // Interface Ancestry
      if( (ignoreFlags & IGNORE_INTERFACES) == 0 )
      {
        for( IJavaClassInfo ifaceType : getInterfaces() )
        {
          IJavaClassType innerClassCtx = ifaceType.resolveType( relativeName, whosAskin, ignoreFlags );
          if( innerClassCtx != null )
          {
            return type = innerClassCtx;
          }
        }
      }

      // Not found
      return type = null;
    }
    finally
    {
      if( type == null )
      {
        _cache.put( relativeName, CACHE_MISS );
      }
      else
      {
        _cache.put( relativeName, type );
      }
    }
  }

  private Object getCachedDetectCyclicType( String relativeName )
  {
    // Short-circuit type cycles
    Object type = _cache.get( relativeName );
    if( type != null )
    {
      return type;
    }
//    _cache.put( relativeName, CACHE_MISS );
    return null;
  }

  private IJavaClassType resolveQualifiedOrSemiQualifiedType( String typeName, IJavaClassInfo whosAskin, int iDot, int ignoreFlags )
  {
    // typeName is either:
    // 1) A fully qualified name e.g., com.abc.Foo
    // 2) A semi-qualified inner class name e.g., Foo.Inner
    // 3) A fully qualified inner class name e.g., com.abc.Foo.Inner
    String rootName = typeName.substring( 0, iDot );
    // First try to resolve a relative root type e.g., the Foo in Foo.Inner (for case #2)
    IJavaClassType rootType = resolveType( rootName, whosAskin, ignoreFlags );
    if( rootType == null )
    {
      // Now try to resolve a root qualified type, with or without an inner class (for cases #1 and #3)
      rootType = resolveRootQualifiedType( typeName );
      if( rootType != null )
      {
        if( rootType.getName().replace( '$', '.' ).equals( typeName ) )
        {
          // Case #1, no inner class
          return rootType;
        }
      }
      else
      {
        return null;
      }
    }
    // Case #2 or #3
    return resolveTrailingInnerClass( rootType, typeName, whosAskin );
  }

  private IJavaClassType resolveRootQualifiedType( String qname )
  {
    while( true )
    {
      IJavaClassType rootType = getClassInfo( qname );
      if( rootType != null )
      {
        return rootType;
      }
      int iLastDot = qname.lastIndexOf( '.' );
      if( iLastDot > 0 )
      {
        qname = qname.substring( 0, iLastDot );
      }
      else
      {
        return null;
      }
    }
  }

  private IJavaClassInfo getClassInfo( String fqn )
  {
    IJavaClassInfo classInfo = JavaSourceUtil.getClassInfo( fqn, _gosuModule );
    if( classInfo != null )
    {
      return classInfo;
    }

    return maybeLoadJavaStubIfGosuType( fqn );
  }

  // Java can reference Gosu directly from source and visa versa, therefore we must handle the case were
  // a Gosu class references a Java class that in turn references a Gosu class:
  //
  //  // from MyJavaClass.java
  //  public MyGosuClass getMyGosu() {...}
  //
  //  // from MyGosuClass.gs
  //  property MyJava: MyJavaClass
  //
  //  // from MyOtherGosuClass.gs
  //  var MyGosuClass = new MyJavaClass().MyGosu  // <~~~ this is of type MyGosuClass, which must resolve in terms of an IJavaClassInfo
  //
  private IJavaClassInfo maybeLoadJavaStubIfGosuType( String fqn )
  {
    IType type = TypeSystem.getByFullNameIfValidNoJava( fqn );
    if( type instanceof IGosuClass )
    {
      StringSourceFileHandle sfh =
        new LazyStringSourceFileHandle( GosuClassUtil.getPackage( fqn ), fqn,
          () -> JavaStubGenerator.instance().genStub( (IGosuClass)type ),
          ((IGosuClass)type).getClassType() );
      return JavaSourceType.createTopLevel( sfh, TypeSystem.getCurrentModule() );
    }

    return null;
  }

  private IJavaClassType resolveTrailingInnerClass( IJavaClassType rootType, String fullNameIncludingRoot, IJavaClassInfo whosAskin )
  {
    String rootName;
    if( fullNameIncludingRoot.startsWith( rootType.getName() ) )
    {
      rootName = rootType.getName();
    }
    else
    {
      rootName = rootType.getSimpleName().replace( '$', '.' );
    }
    String innerSuffix = fullNameIncludingRoot.substring( rootName.length() + 1 );
    String[] innerNames = innerSuffix.split( "\\." );
    for( String innerName : innerNames )
    {
      IJavaClassType innerClass = JavaSourceUtil.resolveInnerClass( (IJavaClassInfo)rootType.getConcreteType(), innerName, whosAskin );
      if( innerClass == null )
      {
        return null;
      }
      rootType = innerClass;
    }
    return rootType;
  }

  private IJavaClassType resolveClassInSamePackage( String relativeName )
  {
    String packageName = getNamespace();
    if( packageName.length() > 0 )
    {
      relativeName = packageName + '.' + relativeName;
    }
    return getClassInfo( relativeName );
  }

  public IJavaClassType resolveImport( String relativeName )
  {
    if( _importList == null )
    {
      return null;
    }
    for( String importText : _importList )
    {
      int iStar = importText.lastIndexOf( "*" );
      if( iStar > 0 )
      {
        IJavaClassType type = getClassInfo( importText.substring( 0, iStar ) + relativeName );
        if( type != null )
        {
          return type;
        }
      }
      else if( importText.endsWith( '.' + relativeName ) )
      {
        IJavaClassType type = getClassInfo( importText );
        if( type != null )
        {
          return type;
        }
      }
    }
    return null;
  }

  @Override
  public IJavaClassInfo getEnclosingClass()
  {
    return _enclosingClass;
  }

  @Override
  public IJavaClassInfo getDeclaringClass()
  {
    return this;
  }

  public JavaSourceType getDeepestClassAtOffset( int offset )
  {
    ILocationInfo loc = getLocationInfo();
    if( loc.contains( offset ) )
    {
      for( IJavaClassInfo ci: getDeclaredClasses() )
      {
        JavaSourceType deepest = ((JavaSourceType)ci).getDeepestClassAtOffset( offset );
        if( deepest != null )
        {
          return deepest;
        }
      }
      return this;
    }
    return null;
  }

  public String toString()
  {
    return getName();
  }

  @Override
  public boolean isAnnotationPresent( Class<? extends Annotation> annotationClass )
  {
    return getModifierList().isAnnotationPresent( annotationClass );
  }

  @Override
  public IAnnotationInfo getAnnotation( Class annotationClass )
  {
    return getModifierList().getAnnotation( annotationClass );
  }

  @Override
  public IAnnotationInfo[] getDeclaredAnnotations()
  {
    return getModifierList().getAnnotations();
  }

  public TypeUsesMap getTypeUsesMap()
  {
    TypeUsesMap typeUsesMap = new TypeUsesMap();
    typeUsesMap.addToTypeUses( getNamespace() + ".*" );
    if( _importList != null )
    {
      _importList.forEach( typeUsesMap::addToTypeUses );
    }
    return typeUsesMap;
  }

  public List<String> getImportList()
  {
    return _importList;
  }

  public List<String> getStaticImports()
  {
    return _staticImportList;
  }

  @Override
  public ISourceFileHandle getSourceFileHandle()
  {
    if( _fileHandle == null )
    {
      IDefaultTypeLoader loader = _enclosingClass.getModule().getTypeLoaders( IDefaultTypeLoader.class ).get( 0 );
      _fileHandle = loader.getSourceFileHandle( getName() );
      if( _fileHandle == null )
      {
        _fileHandle = getEnclosingClass().getSourceFileHandle();
      }
    }
    return _fileHandle;
  }

  @Override
  public boolean isCompilable()
  {
    return true;
  }

  @Override
  public byte[] compile()
  {
    IJavaParser javaParser = GosuParserFactory.getInterface( IJavaParser.class );
    DiagnosticCollector<JavaFileObject> errorHandler = new DiagnosticCollector<>();
    ClassJavaFileObject fileObj = javaParser.compile( getName(), Arrays.asList( "-g", "-Xlint:unchecked", "-parameters" ), errorHandler );
    if( fileObj != null )
    {
      return fileObj.getBytes();
    }
    throw new JavaCompileIssuesException( errorHandler );
  }

  @Override
  public ClassTree getTree()
  {
    return _typeDecl;
  }

  public CompilationUnitTree getCompilationUnitTree()
  {
    if( _enclosingClass instanceof JavaSourceType )
    {
      return _enclosingClass.getCompilationUnitTree();
    }
    return _compilationUnitTree;
  }

  public SourcePositions getSourcePositions()
  {
    if( _enclosingClass instanceof JavaSourceType )
    {
      return _enclosingClass.getSourcePositions();
    }
    return _sourcePositions;
  }

  @Override
  public int getStartPosition()
  {
    return (int)getSourcePositions().getStartPosition( getCompilationUnitTree(), getTree() );
  }

  @Override
  public int getEndPosition()
  {
    return (int)getSourcePositions().getEndPosition( getCompilationUnitTree(), getTree() );
  }
}

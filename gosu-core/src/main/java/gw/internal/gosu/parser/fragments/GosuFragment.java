/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.fragments;

import gw.internal.gosu.compiler.GosuClassLoader;
import gw.internal.gosu.parser.ClassJavaClassInfo;
import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.DynamicPropertySymbol;
import gw.internal.gosu.parser.GosuClassParseInfo;
import gw.internal.gosu.parser.GosuParser;
import gw.internal.gosu.parser.ICompilableTypeInternal;
import gw.internal.gosu.parser.IGosuAnnotation;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.statements.ClassStatement;
import gw.internal.gosu.parser.statements.StatementList;
import gw.internal.gosu.parser.statements.VarStatement;
import gw.lang.parser.IBlockClass;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.IExpression;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.AbstractType;
import gw.lang.reflect.DefaultArrayType;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.FragmentInstance;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IExternalSymbolMap;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuFragment;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;
import gw.util.concurrent.LockingLazyVar;

import java.io.ObjectStreamException;
import gw.util.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @deprecated DO NOT USE THIS CLASS! This is here exclusively for PCF types.
 */
public class GosuFragment extends AbstractType implements IGosuFragment, ICompilableTypeInternal {

  public static final String FRAGMENT_NAME_PREFIX = "__Fragment__";
  private static final IGenericTypeVariable[] EMTPY_TYPE_VARIABLE_ARRAY = new IGenericTypeVariable[0];
  private static final IType[] EMTPY_TYPE_ARRAY = new IType[0];

  private List<IGosuClass> _blocks;
  private GosuFragmentTypeInfo _typeInfo;
  private boolean _discarded = false;
  private ITypeUsesMap _typeUsesMap;
  private LockingLazyVar<IType> _arrayType = new LockingLazyVar<IType>() {
    @Override
    protected IType init() {
      return new DefaultArrayType(GosuFragment.this, getBackingClassInfo(), getTypeLoader());
    }
  };

  private IExpression _expression;
  private String _name;
  private HashMap<String, ISymbol> _externalSymbols;
  private IType _supertype;
  private Set<IType> _allTypesInHierarchy;

  private LockingLazyVar<IJavaClassInfo> _javaClass = new LockingLazyVar<IJavaClassInfo>() {
    @Override
    protected IJavaClassInfo init() {
      return defineClass();
    }
  };

  private LockingLazyVar<FragmentInstance> _sharedInstance = new LockingLazyVar<FragmentInstance>() {
    @Override
    protected FragmentInstance init() {
      return createNewInstance();
    }
  };

  public GosuFragment(String name, HashMap<String, ISymbol> externalSymbols, ITypeUsesMap typeUsesMap) {
    _typeInfo = createTypeInfo();
    _typeUsesMap = typeUsesMap;
    _name = name;
    _externalSymbols = externalSymbols;
    _supertype = JavaTypes.FRAGMENT_INSTANCE();
    _allTypesInHierarchy = new HashSet<IType>();
    _allTypesInHierarchy.addAll(_supertype.getAllTypesInHierarchy());
    _allTypesInHierarchy.add(this);
  }

  // ==========================================================================
  //                     Methods with interesting implementations
  // ==========================================================================

  @Override
  public int getBlockCount() {
    return _blocks == null ? 0 : _blocks.size();
  }

  @Override
  public void addBlock(IBlockClass block) {
    if (_blocks == null) {
      _blocks = new ArrayList<IGosuClass>();
    }
    _blocks.add(block);
  }
  public void removeBlock( IBlockClass block ) {
    if( !_blocks.isEmpty() ) {
      _blocks.remove( block );
    }
  }

  @Override
  public IGosuClass getBlock(int i) {
    return _blocks.get(i);
  }

  @Override
  public IRelativeTypeInfo getTypeInfo() {
    return _typeInfo;
  }

  @Override
  public boolean isFinal() {
    return true;
  }

  @Override
  public GosuClassTypeLoader getTypeLoader() {
    return GosuClassTypeLoader.getDefaultClassLoader();
  }

  @Override
  public ITypeUsesMap getTypeUsesMap() {
    return _typeUsesMap;
  }

  @Override
  public void assignTypeUsesMap(GosuParser parser) {
    if (_typeUsesMap != null) {
      parser.setTypeUsesMap(_typeUsesMap);
    }
  }

  @Override
  public boolean isValid() {
    // Assume that these synthetic classes don't get created/get thrown away if they're invalid,
    // so the existence of the class implies that it's valid
    return true;
  }

  @Override
  public IType getArrayType() {
    return _arrayType.get();
  }

  // ==========================================================================
  //                            Methods to double-check
  // ==========================================================================

  @Override
  public ISourceFileHandle getSourceFileHandle() {
    // TODO
    return null;
  }

  @Override
  public IClassStatement getClassStatement() {
    return new ClassStatement(null);
  }

  // ==========================================================================
  //                               No-op methods
  // ==========================================================================

  @Override
  public List<DynamicFunctionSymbol> getMemberFunctions( String names) {
    return Collections.emptyList();
  }

  @Override
  public IType resolveRelativeInnerClass( String strRelativeInnerClassName, boolean bForce ) {
    return null;
  }

  @Override
  public IGosuClassInternal getEnclosingType() {
    return null;
  }


  @Override
  public IType getEnclosingNonBlockType() {
    return null;
  }

  @Override
  public DynamicPropertySymbol getStaticProperty( String name) {
    return null;
  }

  @Override
  public DynamicPropertySymbol getMemberProperty( String name) {
    return null;
  }

  @Override
  public Map<String, ICapturedSymbol> getCapturedSymbols() {
    return Collections.emptyMap();
  }

  @Override
  public ICapturedSymbol getCapturedSymbol( String strName) {
    return null;
  }

  @Override
  public VarStatement getMemberField( String charSequence) {
    return null;
  }

  @Override
  public boolean isStatic() {
    return false;
  }

  @Override
  public boolean isAnonymous() {
    return false;
  }

  @Override
  public IType getGenericType() {
    return null;
  }

  @Override
  public boolean isInterface() {
    return false;
  }

  @Override
  public boolean isEnum() {
    return false;
  }

  @Override
  public IType[] getInterfaces() {
    return EMPTY_TYPE_ARRAY;
  }

  @Override
  public boolean isParameterizedType() {
    return false;
  }

  @Override
  public boolean isGenericType() {
    return false;
  }

  @Override
  public IGosuParser getParser() {
    return null;
  }

  @Override
  public void addCapturedSymbol(ICapturedSymbol sym) {
    // Do nothing
  }

  @Override
  public IGenericTypeVariable[] getGenericTypeVariables() {
    return EMTPY_TYPE_VARIABLE_ARRAY;
  }

  @Override
  public IType getParameterizedType(IType... ofType) {
    return null;
  }

  @Override
  public IType[] getTypeParameters() {
    return EMTPY_TYPE_ARRAY;
  }

  @Override
  public boolean isArray() {
    return false;
  }

  @Override
  public boolean isPrimitive() {
    return false;
  }

  @Override
  public Object makeArrayInstance(int iLength) {
    return Array.newInstance( getBackingClassInfo().getBackingClass(), iLength );
  }

  @Override
  public Object getArrayComponent(Object array, int iIndex) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
    return Array.get( array, iIndex );
  }

  @Override
  public void setArrayComponent(Object array, int iIndex, Object value) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
    Array.set( array, iIndex, value );
  }

  @Override
  public int getArrayLength(Object array) throws IllegalArgumentException {
    return Array.getLength( array );
  }

  @Override
  public IType getComponentType() {
    return null;
  }

  @Override
  public boolean isAssignableFrom(IType type) {
    // Synthetic types are final singletons
    return type == this;
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  @Override
  public void unloadTypeInfo() {
    // Do nothing
  }

  @Override
  public Object readResolve() throws ObjectStreamException {
    return TypeSystem.getByFullName( getName() );
  }

  @Override
  public int getModifiers() {
    return Modifier.STATIC | Modifier.PUBLIC;
  }

  @Override
  public boolean isAbstract() {
    return false;
  }

  @Override
  public boolean isDiscarded() {
    return _discarded;
  }

  @Override
  public void setDiscarded(boolean bDiscarded) {
    _discarded = bDiscarded;
  }

  @Override
  public boolean isCompoundType() {
    return false;
  }

  @Override
  public Set<IType> getCompoundTypeComponents() {
    return Collections.emptySet();
  }

  @Override
  public IType getInnerClass(CharSequence strTypeName) {
    return null;
  }

  @Override
  public int getDepth() {
    return 0;
  }

  @Override
  public void compileDeclarationsIfNeeded() {
    // Do nothing
  }

  @Override
  public void compileDefinitionsIfNeeded( boolean bForce ) {
    // Do nothing
  }

  @Override
  public void compileHeaderIfNeeded() {
    // Do nothing
  }

  @Override
  public void putClassMembers(GosuParser owner, ISymbolTable table, IGosuClassInternal gsContextClass, boolean bStatic) {
    // Do nothing
  }

  public void putClassMembers(GosuClassTypeLoader loader, GosuParser owner, ISymbolTable table, IGosuClassInternal gsContextClass, boolean bStatic) {
    // Do nothing
  }

  @Override
  public boolean isCreateEditorParser() {
    return false;
  }

  @Override
  public int getAnonymousInnerClassCount() {
    return 0;
  }

  @Override
  public List<? extends IGosuAnnotation> getGosuAnnotations() {
    return Collections.emptyList();
  }

  @Override
  public boolean shouldFullyCompileAnnotations()
  {
    return true;
  }

  @Override
  public List<? extends IVarStatement> getMemberFields()
  {
    return Collections.emptyList();
  }

  @Override
  public List<IVarStatement> getStaticFields()
  {
    return Collections.emptyList();
  }

  public void setExpression(IExpression expression) {
    _expression = expression;
  }

  protected GosuFragmentTypeInfo createTypeInfo() {
    return new GosuFragmentTypeInfo(this);
  }

  @Override
  public IExpression getExpression() {
    return _expression;
  }

  public boolean isExternalSymbol( String name ) {
    return _externalSymbols.containsKey( name );
  }

  @Override
  public Object evaluate(IExternalSymbolMap externalSymbols) {
    try
    {
      return _sharedInstance.get().evaluate(externalSymbols);
    }
    catch( Exception e )
    {
      String s = null;
      try
      {
        s = _expression.toString();
      }
      catch( Exception e1 )
      {
        //ignore
      }
      throw GosuExceptionUtil.forceThrow( e, s );
    }
  }

  @Override
  public Object evaluateRoot(IExternalSymbolMap externalSymbols) {
    return _sharedInstance.get().evaluateRootExpression(externalSymbols);
  }

  @Override
  public Class getBackingClass() {
    return getBackingClassInfo().getBackingClass();
  }

  public IJavaClassInfo getBackingClassInfo() {
    return _javaClass.get();
  }

  public String getRelativeName() {
    if (_name.startsWith("pcf.")) {
      return _name.substring(4);
    }
    return _name;
  }

  public String getName() {
    if (_name.startsWith("pcf.")) {
      return _name;
    }
    return FRAGMENT_PACKAGE + "." + _name;
  }

  public String getNamespace() {
    if (_name.startsWith("pcf.")) {
      return "pcf";
    }
    return FRAGMENT_PACKAGE;
  }

  @Override
  public String getDisplayName() {
    return _name;
  }

  @Override
  public IType getSupertype() {
    return _supertype;
  }

  @Override
  public Set<? extends IType> getAllTypesInHierarchy() {
    return _allTypesInHierarchy;
  }

  @Override
  public ISymbol getExternalSymbol(String strName) {
    return _externalSymbols.get( strName );
  }

  // -------------------- Private helper methods

  private FragmentInstance createNewInstance() {
    try {
      return (FragmentInstance) getBackingClass().newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private IJavaClassInfo defineClass() {
    try {
      Class aClass = GosuClassLoader.instance().defineClass(this, true); //getRelativeName().startsWith( FRAGMENT_NAME_PREFIX ) );
      return new ClassJavaClassInfo(aClass, getTypeLoader().getModule());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException( e );
    }
  }

  @Override
  public String getSource()
  {
    return null;
  }

  @Override
  public GosuClassParseInfo getParseInfo() {
    throw new RuntimeException();
  }

  @Override
  public boolean hasAssertions()
  {
    return false;
  }

  @Override
  public ClassType getClassType() {
    return ClassType.Class;
  }

  @Override
  public List<? extends IType> getInnerClasses() {
    return null;
  }

  @Override
  public List<? extends IType> getLoadedInnerClasses() {
    return Collections.emptyList();
  }

  @Override
  public String toString() {
    return getName();
  }

  @Override
  public boolean isCompilable() {
    return false;
  }
  @Override
  public byte[] compile() {
    throw new UnsupportedOperationException();
  }
}

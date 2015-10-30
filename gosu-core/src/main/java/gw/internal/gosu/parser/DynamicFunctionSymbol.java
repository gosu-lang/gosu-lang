/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.statements.FunctionStatement;
import gw.internal.gosu.parser.statements.MethodCallStatement;
import gw.lang.parser.*;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.*;
import gw.util.GosuExceptionUtil;

import java.util.Collections;
import java.util.List;


/**
 * Represents a parsed function as specified in the Gosu spec.
 */
public class DynamicFunctionSymbol extends AbstractDynamicSymbol implements IDynamicFunctionSymbol
{
  private static final Object DEFINITION_CLEARED = new Object() {public String toString() {return "<cleared>";}};
  private static final Object NULL_MI = new Object() { public String toString() {return "null_mi";}};

  private String _strDisplayName;
  private List<ISymbol> _args;
  private MethodCallStatement _initializer;
  private FunctionStatement _functionStmt;
  private DynamicFunctionSymbol _superDfs;
  private boolean _bLoopImplicitReturn;
  private IExpression _annotationDefault;
  private volatile Object _mi;

  /**
   * Constructs a DynamicFunctionSymbol for use with an IGosuParser's ISymbolTable.
   *
   * @param symTable The symbol table.
   * @param strName  The symbol name.
   * @param type     The IGosuParser specific type.
   * @param args     The symbols for the function's arguments (or null).
   * @param value    A statement for the body of the function (or null).
   */
  public DynamicFunctionSymbol( ISymbolTable symTable, CharSequence strName, IFunctionType type, List<ISymbol> args, IStatement value )
  {
    this( symTable, strName, type, args, (Object)value );
  }

  /**
   * Constructs a DynamicFunctionSymbol for use with an IGosuParser's ISymbolTable.
   *
   * @param symTable The symbol table.
   * @param strName  The symbol name.
   * @param type     The IGosuParser specific type.
   * @param args     The symbols for the function's arguments (or null).
   * @param value    An expression for the body of the function (or null).
   */
  public DynamicFunctionSymbol( ISymbolTable symTable, CharSequence strName, IFunctionType type, List<ISymbol> args, IExpression value )
  {
    this(symTable, strName, type, args, (Object) value);
  }

  protected DynamicFunctionSymbol( ISymbolTable symTable, CharSequence name, IFunctionType type, List<ISymbol> args, Object value )
  {
    super( symTable, name, type );
    _value = value;
    assert value instanceof Statement || value instanceof Symbol || value == null || value instanceof Expression:
      "Invalid type for DFS value: " + value.getClass();

    if( args == null || args.isEmpty() )
    {
      _args = Collections.emptyList();
    }
    else
    {
      _args = args;
    }

    _strDisplayName = name.toString();
    setName( getSignatureName( name ) );
  }

  public DynamicFunctionSymbol( DynamicFunctionSymbol dfs )
  {
    super( dfs._symTable, dfs.getName(), dfs.getType() );
    _value = dfs._value;
    _args = dfs._args;
    _initializer = dfs._initializer;
    setName( dfs.getName() );
    _scriptPartId = dfs._scriptPartId;
    replaceModifierInfo(dfs.getModifierInfo());
  }

  public void renameAsErrantDuplicate( int iIndex )
  {
    setName( getSignatureName( iIndex + "_duplicate_" + _strDisplayName ) );
  }

  public String getDisplayName()
  {
    return _strDisplayName;
  }
  protected void setDisplayName( String strDisplayName )
  {
    _strDisplayName = strDisplayName;
  }

  /**
   * @return The Symbols for the arguments to this function.
   */
  public List<ISymbol> getArgs()
  {
    return _args;
  }

  public IType[] getArgTypes()
  {
    IType[] types = new IType[_args == null ? 0 : _args.size()];
    for( int i = 0; i < types.length; i++ )
    {
      assert _args != null;
      types[i] = _args.get( i ).getType();
    }
    return types;
  }

  public IType getReturnType()
  {
    return ((FunctionType)getType()).getReturnType();
  }

  /**
   * Invokes the dynamic function.
   */
  public Object invoke( Object[] args )
  {
    return invokeFromBytecode( args );
  }

  private Object invokeFromBytecode( Object[] args )
  {
    //## todo: maybe cache the method info?
    IParsedElement stmts = (IParsedElement)getValueDirectly();
    if( stmts == null )
    {
      ensureDeclaringClassIsCompiled();
      stmts = (IParsedElement)getValueDirectly();
    }

    IGosuClass gsClass = findGosuClassOrProgram( stmts );
    if( gsClass == null )
    {
      throw new IllegalStateException( "Did not find Gosu Class/Program" );
    }
    Class<?> javaClass = gsClass.getBackingClass();
    IProgramInstance instance = null;
    if( gsClass instanceof IGosuProgram )
    {
      try
      {
        instance = (IProgramInstance)javaClass.newInstance();
        instance.evaluate(null);
      }
      catch( Exception e )
      {
        throw GosuExceptionUtil.forceThrow( e );
      }
    }
    IMethodInfo mi = gsClass.getTypeInfo().getMethod( gsClass, getDisplayName(), getArgTypes() );
    return mi.getCallHandler().handleCall( instance, args );

  }

  private void ensureDeclaringClassIsCompiled()
  {
    IGosuClassInternal gsClass = getGosuClass();
    if( gsClass != null )
    {
      gsClass.isValid();
    }
  }

  private IGosuClass findGosuClassOrProgram( IParsedElement pe )
  {
    if( pe == null )
    {
      return null;
    }

    IGosuClass gsClass = pe.getGosuProgram();
    if( gsClass != null )
    {
      return gsClass;
    }

    gsClass = getGosuClass();
    if( gsClass != null )
    {
      return gsClass;
    }

    return findGosuClassOrProgram( pe.getParent() );
  }

  /**
   * @return the canonical, generic name of this function
   */
  protected String getCannonicalName()
  {
    return getName();
  }

  public boolean isClassMember()
  {
    return super.isClassMember() && !isConstructor();
  }

  @Override
  public void clearDebugInfo()
  {
    super.clearDebugInfo();
    if (_functionStmt != null) {
      _functionStmt.clearParseTreeInformation();
    }
  }

  public boolean isConstructor()
  {
    return getInitializer() != null;
  }

  public String getSignatureDescription()
  {
    return getMethodSignature();
  }

  public ISymbol getLightWeightReference()
  {
    return this;
  }

  public String getMethodSignature()
  {
    return getDisplayName() + getParameterDisplay( true ) + " : " + TypeInfoUtil.getTypeName( ((FunctionType)getType()).getReturnType() );
  }

  public static String getSignatureName( CharSequence strName, List<ISymbol> args )
  {
    return strName + getUniqueNameForParameters( args );
  }

  protected String getSignatureName( CharSequence strName )
  {
    return getSignatureName( strName, getArgs() );
  }

  public String getParameterDisplay( boolean bRelative )
  {
    IType[] paramTypes = ((FunctionType)getType()).getParameterTypes();
    if( paramTypes == null || paramTypes.length == 0 )
    {
      return "()";
    }

    String strParams = "(";
    for( int i = 0; i < paramTypes.length; i++ )
    {
      strParams += (i == 0 ? "" : ", " ) + (bRelative ? paramTypes[i].getRelativeName() : paramTypes[i].getName());
    }
    strParams += ")";

    return strParams;
  }

  private static String getUniqueNameForParameters( List<ISymbol> args )
  {
    if( args == null || args.size() == 0 )
    {
      return "()";
    }

    String strParams = "(";
    for( int i = 0; i < args.size(); i++ )
    {
      strParams += (i == 0 ? "" : ", " ) + args.get( i ).getType().getName();
    }
    strParams += ")";

    return strParams;
  }

  protected DynamicFunctionSymbol getFunctionSymbol()
  {
    ISymbol symbol = _symTable.getSymbol( getName() );
    if( symbol != null )
    {
      return (DynamicFunctionSymbol)symbol;
    }

    return this;
  }

  public void setInitializer( MethodCallStatement initializer )
  {
    _initializer = initializer;
  }

  public MethodCallStatement getInitializer()
  {
    return _initializer;
  }

  public boolean isAbstract()
  {
    return Modifier.isAbstract( getModifiers() );
  }
  public void setAbstract( boolean bAbstract )
  {
    setModifiers( Modifier.setAbstract( getModifiers(), bAbstract ) );
  }

  public boolean isFinal()
  {
    return Modifier.isFinal( getModifiers() );
  }

  public void setFinal( boolean bFinal )
  {
    setModifiers( Modifier.setFinal(getModifiers(), bFinal) );
  }

  public IDynamicFunctionSymbol getBackingDfs()
  {
    return this;
  }

  public IAttributedFeatureInfo getMethodOrConstructorInfo( boolean acceptNone )
  {
    IAttributedFeatureInfo methodOrConstructorInfo = getMethodOrConstructorInfo();
    if( (methodOrConstructorInfo == null) && !acceptNone )
    {
      throw new IllegalStateException( "Should have found method/ctor info for " + getName() );
    }
    else
    {
      return methodOrConstructorInfo;
    }
  }

  public IAttributedFeatureInfo getMethodOrConstructorInfo()
  {
    if( _mi != null )
    {
      return _mi == NULL_MI ? null : (IAttributedFeatureInfo)_mi;
    }

    IAttributedFeatureInfo cachedMi = null;

    try
    {
      IScriptPartId scriptPart = getScriptPart();
      IType declaringType = scriptPart == null ? null : scriptPart.getContainingType();
      if( declaringType != null )
      {
        if( declaringType instanceof IGosuClass && ((IGosuClass)declaringType).isCompilingDeclarations() )
        {
          System.out.println( "!!! Attempted to acquire declarations while compiling declarations" );
        }

        ITypeInfo typeInfo = declaringType.getTypeInfo();

        List<? extends IMethodInfo> methods;
        if( typeInfo instanceof IRelativeTypeInfo )
        {
          methods = ((IRelativeTypeInfo)typeInfo).getMethods( declaringType );
        }
        else
        {
          methods = typeInfo.getMethods();
        }
        IReducedDynamicFunctionSymbol thisRS = createReducedSymbol();
        for( IMethodInfo mi : methods )
        {
          if( mi instanceof IDFSBackedFeatureInfo )
          {
            IReducedDynamicFunctionSymbol dfs = ((IDFSBackedFeatureInfo)mi).getDfs();
            if( thisRS.equals( dfs ) || thisRS.getBackingDfs().equals( dfs ) )
            {
              return cachedMi = mi;
            }
          }
        }

        List<? extends IConstructorInfo> ctors;
        if( typeInfo instanceof IRelativeTypeInfo )
        {
          ctors = ((IRelativeTypeInfo)typeInfo).getConstructors( declaringType );
        }
        else
        {
          ctors = typeInfo.getConstructors();
        }
        for( IConstructorInfo ci : ctors )
        {
          if( ci instanceof IDFSBackedFeatureInfo )
          {
            IReducedDynamicFunctionSymbol dfs = ((IDFSBackedFeatureInfo)ci).getDfs();
            if( thisRS.equals( dfs ) || thisRS.getBackingDfs().equals( dfs ) )
            {
              return cachedMi = ci;
            }
            else if( ((this instanceof SuperConstructorFunctionSymbol) ||
                      (this instanceof ThisConstructorFunctionSymbol)) &&
                     (dfs.getArgs().equals( getArgs() )) )
            {
              return cachedMi = ci;
            }
          }
        }
      }
    }
    finally
    {
      _mi = cachedMi == null ? NULL_MI : cachedMi;
    }
    return cachedMi;
  }

  public ITypeInfo getDeclaringTypeInfo()
  {
    IType type = getScriptPart() == null ? null : getScriptPart().getContainingType();
    if( type == null )
    {
      return null;
    }
    return type.getTypeInfo();
  }

  public String getFullDescription()
  {
    return getModifierInfo() == null ? "" : getModifierInfo().getDescription();
  }

  public void setValue(Object value) {
    assert value == null || (value instanceof Statement || value instanceof Symbol || value instanceof Expression) :
      "Invalid type for DFS value: " + value.getClass();
    super.setValue(value);
  }

  public FunctionStatement getDeclFunctionStmt()
  {
    return _functionStmt;
  }

  public void setDeclFunctionStmt( FunctionStatement declFunctionStmt )
  {
    _functionStmt = declFunctionStmt;
  }

  @Override
  public int hashCode()
  {
    return getName().hashCode();
  }

  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( o == null || !(o instanceof DynamicFunctionSymbol))
    {
      return false;
    }

    DynamicFunctionSymbol that = (DynamicFunctionSymbol)o;

    String strName = getName();
    return !(strName != null ? !strName.equals( that.getName() ) : that.getName() != null);
  }

  public DynamicFunctionSymbol getParameterizedVersion( IGosuClass gsClass )
  {
    return new ParameterizedDynamicFunctionSymbol( this, gsClass );
  }

  /**
   * Used to remove definition compiled info from this class.
   */
  public void clearDefn()
  {
    setValueDirectly( DEFINITION_CLEARED );
  }

  public void setArgs( List<ISymbol> args )
  {
    _args = args;
  }

  public void setSuperDfs( DynamicFunctionSymbol superDfs )
  {
    _superDfs = superDfs;
  }
  public DynamicFunctionSymbol getSuperDfs()
  {
    return _superDfs;
  }

  public boolean hasTypeVariables()
  {
    IGenericTypeVariable[] tvs = getType().getGenericTypeVariables();
    return tvs != null && tvs.length != 0;
  }

  public boolean hasOptionalParameters()
  {
    List<ISymbol> args = getArgs();
    if( args != null )
    {
      for( ISymbol arg : args )
      {
        if( arg.getDefaultValueExpression() != null )
        {
          return true;
        }
      }
    }
    return false;
  }
  
  public IReducedDynamicFunctionSymbol createReducedSymbol()
  {
    return new ReducedDynamicFunctionSymbol( this );
  }

  public void setLoopImplicitReturn( boolean bLoopImplicitReturn )
  {
    _bLoopImplicitReturn = bLoopImplicitReturn;
  }

  public boolean isLoopImplicitReturn()
  {
    return _bLoopImplicitReturn;
  }

  public IExpression getAnnotationDefault()
  {
    return _annotationDefault;
  }
  public void setAnnotationDefault( IExpression annotationDefault )
  {
    _annotationDefault = annotationDefault;
  }
}

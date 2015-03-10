/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.ExecutionMode;
import gw.internal.gosu.parser.expressions.BlockExpression;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.internal.gosu.parser.expressions.MethodCallExpression;
import gw.internal.gosu.parser.statements.ClassFileStatement;
import gw.internal.gosu.parser.statements.ClassStatement;
import gw.internal.gosu.parser.statements.MethodCallStatement;
import gw.internal.gosu.parser.statements.NoOpStatement;
import gw.internal.gosu.parser.statements.VarStatement;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.IReducedSymbol;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.Keyword;
import gw.lang.parser.ScriptPartId;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuConstructorInfo;
import gw.lang.reflect.java.GosuTypes;
import gw.lang.reflect.java.JavaTypes;
import gw.util.fingerprint.FP64;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 */
public class GosuClassParseInfo {
  transient private IGosuClassInternal _gosuClass;
  transient private ClassStatement _classStmt;
  transient private ParseResultsException _pe;
  transient private List<DynamicFunctionSymbol> _listStaticFunctions;
  transient private Map<String, DynamicFunctionSymbol> _mapMemberFunctions;
  transient private Map<String, DynamicFunctionSymbol> _mapConstructorFunctions;
  transient private List<DynamicPropertySymbol> _listStaticProperties;
  transient private Map<String, DynamicPropertySymbol> _mapMemberProperties;
  transient private Map<String, VarStatement> _mapStaticFields;
  transient private Map<String, VarStatement> _mapMemberFields;
  transient private Map<CharSequence, ISymbol> _memberFieldIndexByName;
  transient private Symbol _thisSymbol;
  transient private Map<String, ICapturedSymbol> _capturedSymbols;
  transient private long _sourceFingerprint;

  transient private BlockExpression _block;

  public GosuClassParseInfo(IGosuClassInternal gosuClass) {
    _gosuClass = gosuClass;
    _classStmt = new ClassStatement(gosuClass);
    _listStaticFunctions = Collections.emptyList();
    _mapMemberFunctions = Collections.emptyMap();
    _mapConstructorFunctions = Collections.emptyMap();
    _listStaticProperties = Collections.emptyList();
    _mapMemberProperties = Collections.emptyMap();
    _mapStaticFields = Collections.emptyMap();
    _mapMemberFields = Collections.emptyMap();
    _memberFieldIndexByName = Collections.emptyMap();
    _capturedSymbols = Collections.emptyMap();
  }

  public ClassStatement getClassStatement() {
    return _classStmt;
  }

  public ClassFileStatement getClassFileStatement() {
    return (ClassFileStatement) _classStmt.getParent();
  }

  public void setParseResultsException(ParseResultsException pe) {
    _pe = pe;
  }

  public ParseResultsException getParseResultsException() {
    return _pe;
  }

  public void addStaticFunction( DynamicFunctionSymbol function )
  {
    assert function.isClassMember();
    if( _listStaticFunctions == Collections.EMPTY_LIST )
    {
      _listStaticFunctions = new ArrayList<DynamicFunctionSymbol>( 2 );
    }
    else
    {
      // Remove duplicates. Remain consistent with how non-static functions are maintained (i.e., Map.put() replaces duplicate)
      _listStaticFunctions.remove( function );
    }

    _listStaticFunctions.add( function );
  }

  private void clearDebugInfoOnFunctions( Collection<DynamicFunctionSymbol> mapFunctions )
  {
    for( DynamicFunctionSymbol function : mapFunctions )
    {
      function.clearDebugInfo();
    }
  }

  private void clearDebugInfoOnProperties( Collection<DynamicPropertySymbol> mapProperties )
  {
    for( DynamicPropertySymbol property : mapProperties )
    {
      property.clearDebugInfo();
    }
  }

  private void clearDebugInfoOnAnnotations( List<IGosuAnnotation> annotations )
  {
    if( annotations != null )
    {
      for( IGosuAnnotation annotation : annotations )
      {
        if( annotation instanceof GosuAnnotation)
        {
          ((GosuAnnotation)annotation).clearDebugInfo();
        }
      }
    }
  }

  private void clearDebugInfoOnFields( Collection<VarStatement> fields )
  {
    for( VarStatement field : fields )
    {
      field.clearParseTreeInformation();
    }
  }

  public List<DynamicFunctionSymbol> getStaticFunctions() {
    return _listStaticFunctions;
  }

  public Map<String, DynamicFunctionSymbol> getMemberFunctions() {
    return _mapMemberFunctions;
  }

  public void addMemberFunction( DynamicFunctionSymbol function )
  {
    assert function.isClassMember();
    if( function.isStatic() )
    {
      addStaticFunction( function );
      return;
    }
    if( _mapMemberFunctions == Collections.EMPTY_MAP )
    {
      //noinspection unchecked
      _mapMemberFunctions = new LinkedHashMap( 2 );
    }
    _mapMemberFunctions.put( function.getName(), function );
  }

  public Map<String, DynamicFunctionSymbol> getConstructorFunctions() {
    return _mapConstructorFunctions;
  }

  public void addConstructorFunction( DynamicFunctionSymbol function )
  {
    if( _mapConstructorFunctions == Collections.EMPTY_MAP )
    {
      //noinspection unchecked
      _mapConstructorFunctions = new LinkedHashMap(2);
    }
    _mapConstructorFunctions.put( function.getName(), function);
  }

  protected boolean addDefaultConstructor( ISymbolTable symbolTable )
  {
    NoOpStatement value = new NoOpStatement();
    value.initEmptyParseTree();
    DynamicFunctionSymbol dfsCtor =
      new DynamicFunctionSymbol( symbolTable, _gosuClass.getRelativeName(), GosuTypes.DEF_CTOR_TYPE(), null, value );
    dfsCtor.setScriptPart( new ScriptPartId(_gosuClass, null ) );
    if( _gosuClass.getSuperClass() != null )
    {
      // If the supertype is Enum, we need to look for a super constructor that takes (String, int) instead of a no-arg one
      // The appropriate arguments to super() will be generated by the compiler and won't appear in the parse tree
      DynamicFunctionSymbol defCtorFromSuper;
      if( _gosuClass.getSupertype().getGenericType() == JavaTypes.ENUM() )
      {
        defCtorFromSuper = _gosuClass.getSuperClass().getConstructorFunction( "Enum(java.lang.String, int)" );
      }
      else
      {
        defCtorFromSuper = _gosuClass.getSuperClass().getDefaultConstructor();
      }

      if( defCtorFromSuper == null || !_gosuClass.getSuperClass().isAccessible( _gosuClass, defCtorFromSuper ) )
      {
        return false;
      }
      MethodCallExpression e = new MethodCallExpression();
      e.setFunctionSymbol( new SuperConstructorFunctionSymbol( defCtorFromSuper ) );
      e.setArgs( null );
      e.setType( GosuParserTypes.NULL_TYPE() );
      MethodCallStatement initializer = new MethodCallStatement();
      initializer.setMethodCall( e );
      dfsCtor.setInitializer( initializer );
    }
    else
    {
      MethodCallExpression e = new MethodCallExpression();
      e.setFunctionSymbol( new InitConstructorFunctionSymbol( symbolTable ) );
      e.setArgs( null );
      e.setType( GosuParserTypes.NULL_TYPE() );
      MethodCallStatement initializer = new MethodCallStatement();
      initializer.setMethodCall( e );
      dfsCtor.setInitializer( initializer );
    }
    if( _mapConstructorFunctions == Collections.EMPTY_MAP )
    {
      //noinspection unchecked
      _mapConstructorFunctions = new LinkedHashMap(2);
    }
    if( _gosuClass.isEnum() )
    {
      dfsCtor.setPrivate( true );
    }
    _mapConstructorFunctions.put( dfsCtor.getName(), dfsCtor );
    return true;
  }

  boolean addAnonymousConstructor( ISymbolTable symTable, GosuConstructorInfo superCtor )
  {
    symTable.pushIsolatedScope(new GosuClassTransparentActivationContext(_gosuClass, true));
    try
    {
      NoOpStatement value = new NoOpStatement();
      value.initEmptyParseTree();
      List<ISymbol> argSymbols = makeArgSymbols( superCtor, symTable );
      DynamicFunctionSymbol dfsCtor =
        new DynamicFunctionSymbol( symTable, _gosuClass.getRelativeName(),
                                   new FunctionType( GosuTypes.DEF_CTOR_TYPE().getDisplayName(), JavaTypes.pVOID(), typesFromSymbols( argSymbols ) ),
                                   argSymbols, value );
      dfsCtor.setScriptPart( new ScriptPartId(_gosuClass, null ) );
      IGosuConstructorInfo ctorFromSuper = superCtor;
      while( ctorFromSuper instanceof ParameterizedGosuConstructorInfo )
      {
        ctorFromSuper = ctorFromSuper.getBackingConstructorInfo();
      }
      MethodCallExpression e = new MethodCallExpression();
      DynamicFunctionSymbol dfsCtorFromSuper;
      if( ctorFromSuper == null || !_gosuClass.getSuperClass().isAccessible( _gosuClass, dfsCtorFromSuper = getSuperDfsFromSuperCtor( ctorFromSuper ) ) )
      {
        return false;
      }
      e.setFunctionSymbol( new SuperConstructorFunctionSymbol( dfsCtorFromSuper ) );
      e.setArgs( makeArgs( argSymbols, symTable ) );
      e.setType( GosuParserTypes.NULL_TYPE() );
      MethodCallStatement initializer = new MethodCallStatement();
      initializer.setMethodCall( e );
      dfsCtor.setInitializer( initializer );

      if( _mapConstructorFunctions == Collections.EMPTY_MAP )
      {
        //noinspection unchecked
        _mapConstructorFunctions = new LinkedHashMap(2);
      }
      _mapConstructorFunctions.put( dfsCtor.getName(), dfsCtor );
      return true;
    }
    finally
    {
      symTable.popScope();
    }
  }

  private DynamicFunctionSymbol getSuperDfsFromSuperCtor(IGosuConstructorInfo ctorFromSuper) {
    IGosuClassInternal gsClass = (IGosuClassInternal) ctorFromSuper.getOwnersType();
    List<DynamicFunctionSymbol> constructors = gsClass.getConstructorFunctions();
    for (DynamicFunctionSymbol constructor : constructors) {
      if (equals(constructor, ctorFromSuper)) {
        return constructor;
      }
    }
    return null;
  }

  private boolean equals(DynamicFunctionSymbol constructor, IGosuConstructorInfo ctorFromSuper) {
    IType[] args1 = constructor.getArgTypes();
    List<IReducedSymbol> args2 = ctorFromSuper.getArgs();
    if (args1.length != args2.size()) {
      return false;
    }
    for (int i = 0; i < args1.length; i++) {
      if (!args1[i].equals(args2.get(i).getType())) {
        return false;
      }
    }
    return true;
  }

  private Identifier[] makeArgs( List<ISymbol> argSymbols, ISymbolTable symTable )
  {
    Identifier[] args = new Identifier[argSymbols.size()];
    for( int i = 0; i < args.length; i++ )
    {
      ISymbol sym = argSymbols.get( i );
      Identifier id = new Identifier();
      id.setSymbol( sym, symTable );
      id.setType( sym.getType() );
      args[i] = id;
    }
    return args;
  }

  private List<ISymbol> makeArgSymbols( GosuConstructorInfo ci, ISymbolTable symTable )
  {
    if(ci == null)
    {
      return Collections.emptyList();
    }
    int i = 0;
    List<ISymbol> args = new ArrayList<ISymbol>( ci.getParameters().length );
    for( IParameterInfo pi : ci.getParameters() )
    {
      Symbol sym = new Symbol( "p" + i++, pi.getFeatureType(), symTable, null );
      symTable.putSymbol( sym );
      args.add( sym );
    }
    return args;
  }

  private IType[] typesFromSymbols( List<ISymbol> argSymbols )
  {
    if( argSymbols.size() == 0 )
    {
      return IType.EMPTY_ARRAY;
    }

    IType[] types = new IType[argSymbols.size()];
    for( int i = 0; i < types.length; i++ )
    {
      types[i] = argSymbols.get( i ).getType();
    }
    return types;
  }

  public void addStaticProperty( DynamicPropertySymbol property )
  {
    if( _listStaticProperties == Collections.EMPTY_LIST )
    {
      _listStaticProperties = new ArrayList<DynamicPropertySymbol>( 2 );
    }
    _listStaticProperties.add( property );
  }

  public List<DynamicPropertySymbol> getStaticProperties() {
    return _listStaticProperties;
  }

  public Map<String, VarStatement> getMemberFields() {
    return _mapMemberFields;
  }

  public void addMemberProperty( DynamicPropertySymbol property )
  {
    if( property.isStatic() )
    {
      addStaticProperty(property);
      return;
    }
    if( _mapMemberProperties == Collections.EMPTY_MAP )
    {
      //noinspection unchecked
      _mapMemberProperties = new LinkedHashMap(2);
    }
    _mapMemberProperties.put( property.getName(), property );
  }

  private void addStaticField( VarStatement varStmt )
  {
    // Static vars must be maintained in the order declared. They must evaluate in that order.
    if( _mapStaticFields == Collections.EMPTY_MAP )
    {
      //noinspection unchecked
      _mapStaticFields = new LinkedHashMap(2);
    }
    _mapStaticFields.put( varStmt.getIdentifierName(), varStmt );
  }

  public Map<String, DynamicPropertySymbol> getMemberProperties() {
    return _mapMemberProperties;
  }

  public Map<String, VarStatement> getStaticFields() {
    return _mapStaticFields;
  }

  public void addMemberField( VarStatement varStmt )
  {
    if( varStmt.isStatic() )
    {
      addStaticField( varStmt );
      return;
    }
    if( _mapMemberFields == Collections.EMPTY_MAP )
    {
      //noinspection unchecked
      _mapMemberFields = new LinkedHashMap(2);
    }
    // Static vars must be maintained in the order declared. They must evaluate in that order.
    String varName = varStmt.getIdentifierName();
    _mapMemberFields.put( varName, varStmt );
    if( !_memberFieldIndexByName.containsKey( varName ) )
    {
      int iIndex = _memberFieldIndexByName.size();
      if( _memberFieldIndexByName == Collections.EMPTY_MAP )
      {
        _memberFieldIndexByName = new HashMap<CharSequence, ISymbol>( 4 );
      }
      _memberFieldIndexByName.put( varName, new MemberFieldSymbol( iIndex, varName ) );
    }

    if (_gosuClass instanceof GosuProgram) {
      // Remove initializers, fields are assigned in the programs entry point function
      varStmt.setAsExpression( null );
    }
  }

  public Map<CharSequence, ISymbol> getMemberFieldIndexByName() {
    return _memberFieldIndexByName;
  }

  public Symbol getStaticThisSymbol()
  {
    if( _thisSymbol == null )
    {
      if( _gosuClass.isParameterizedType() )
      {
        GosuClass genericType = (GosuClass) _gosuClass.getGenericType();
        _thisSymbol = ((GosuClass) genericType.dontEverCallThis()).getStaticThisSymbol();
      }
      else
      {
        _thisSymbol = makeThisSymbol();
      }
    }
    return _thisSymbol;
  }

  private Symbol makeThisSymbol()
  {
    return new ReadOnlySymbol( Keyword.KW_this.getName(), TypeLord.getConcreteType( _gosuClass ), Symbol.MEMBER_STACK_PROVIDER, null );
  }

  public Map<String, ICapturedSymbol> getCapturedSymbols() {
    return _capturedSymbols;
  }

  public void addCapturedSymbolSilent( ICapturedSymbol sym )
  {
    if( _capturedSymbols.isEmpty() )
    {
      _capturedSymbols = new HashMap<String, ICapturedSymbol>( 2 );
    }
    _capturedSymbols.put( sym.getName(), sym );
  }

  public static void clear() {
//    int n = 0;
//    for (GosuClassParseInfo info : new ArrayList<GosuClassParseInfo>(_allParseInfos)) {
//      IGosuClassInternal gosuClass = info._gosuClass;
//      for (IGosuClassInternal parameterizedType : gosuClass.getParameterizedTypes()) {
//        parameterizedType.clearParseInfo();
//        n++;
//      }
//      gosuClass.clearParseInfo();
//      n++;
//    }
//    System.out.println("Dropped " + n);
//    _allParseInfos = null;
  }

  public void setBlock(BlockExpression blk) {
    _block = blk;
  }

  public BlockExpression getBlock() {
    return _block;
  }

  public void maybeClearDebugInfo() {
    if (ExecutionMode.isRuntime()) {
      TypeSystem.lock();
      try {
        if (!_gosuClass.getTypeLoader().shouldKeepDebugInfo(_gosuClass)) {
          clearDebugInfoOnFields(_mapStaticFields.values());
          clearDebugInfoOnFields(_mapMemberFields.values());
          clearDebugInfoOnProperties(_listStaticProperties);
          clearDebugInfoOnProperties(_mapMemberProperties.values());
          clearDebugInfoOnFunctions(_mapMemberFunctions.values());
          clearDebugInfoOnFunctions(_listStaticFunctions);
          clearDebugInfoOnFunctions(_mapConstructorFunctions.values());
          clearDebugInfoOnAnnotations(_gosuClass.getModifierInfo().getAnnotations());
          getClassStatement().clearParseTreeInformation();
          Set<IUsesStatement> usesStatements = _gosuClass.getTypeUsesMap() == null ? null : _gosuClass.getTypeUsesMap().getUsesStatements();
          if (usesStatements != null) {
            for (IUsesStatement usesStatement : usesStatements) {
              usesStatement.clearParseTreeInformation();
            }
          }
        }
      } finally {
        TypeSystem.unlock();
      }
    }
  }

  public void updateSource(String source) {
    _sourceFingerprint = new FP64(source).getRawFingerprint();
  }

  public long getSourceFingerprint() {
    return _sourceFingerprint;
  }
}

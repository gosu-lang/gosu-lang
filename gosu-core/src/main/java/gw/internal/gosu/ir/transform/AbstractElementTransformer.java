/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform;

import gw.config.CommonServices;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.Type;
import gw.internal.gosu.coercer.FunctionToInterfaceClassGenerator;
import gw.internal.gosu.ir.nodes.GosuClassIRType;
import gw.internal.gosu.ir.nodes.IRMethod;
import gw.internal.gosu.ir.nodes.IRMethodFactory;
import gw.internal.gosu.ir.nodes.IRProperty;
import gw.internal.gosu.ir.nodes.JavaClassIRType;
import gw.internal.gosu.ir.nodes.SyntheticIRMethod;
import gw.internal.gosu.ir.transform.util.AccessibilityUtil;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.internal.gosu.ir.transform.util.RequiresReflectionDeterminer;
import gw.internal.gosu.parser.AbstractDynamicSymbol;
import gw.internal.gosu.parser.BeanAccess;
import gw.internal.gosu.parser.CompoundType;
import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.GosuAnnotationInfo;
import gw.internal.gosu.parser.GosuParser;
import gw.internal.gosu.parser.ICompilableTypeInternal;
import gw.internal.gosu.parser.IGosuAnnotation;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.IGosuTemplateInternal;
import gw.internal.gosu.parser.MetaType;
import gw.internal.gosu.parser.NewIntrospector;
import gw.internal.gosu.parser.ReducedDynamicFunctionSymbol;
import gw.internal.gosu.parser.Symbol;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.TypeVariableArrayType;
import gw.internal.gosu.parser.TypeVariableType;
import gw.internal.gosu.parser.expressions.BlockType;
import gw.internal.gosu.parser.fragments.GosuFragment;
import gw.internal.gosu.runtime.GosuRuntimeMethods;
import gw.lang.IDimension;
import gw.lang.ir.IRElement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.ir.builder.IRArgConverter;
import gw.lang.ir.expression.IRArithmeticExpression;
import gw.lang.ir.expression.IRArrayLengthExpression;
import gw.lang.ir.expression.IRArrayLoadExpression;
import gw.lang.ir.expression.IRBooleanLiteral;
import gw.lang.ir.expression.IRCastExpression;
import gw.lang.ir.expression.IRCharacterLiteral;
import gw.lang.ir.expression.IRClassLiteral;
import gw.lang.ir.expression.IRCompositeExpression;
import gw.lang.ir.expression.IREqualityExpression;
import gw.lang.ir.expression.IRFieldGetExpression;
import gw.lang.ir.expression.IRIdentifier;
import gw.lang.ir.expression.IRLazyTypeMethodCallExpression;
import gw.lang.ir.expression.IRMethodCallExpression;
import gw.lang.ir.expression.IRNegationExpression;
import gw.lang.ir.expression.IRNewArrayExpression;
import gw.lang.ir.expression.IRNewExpression;
import gw.lang.ir.expression.IRNullLiteral;
import gw.lang.ir.expression.IRNumericLiteral;
import gw.lang.ir.expression.IRPrimitiveTypeConversion;
import gw.lang.ir.expression.IRRelationalExpression;
import gw.lang.ir.expression.IRStringLiteralExpression;
import gw.lang.ir.expression.IRTernaryExpression;
import gw.lang.ir.statement.IRArrayStoreStatement;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.ir.statement.IRFieldSetStatement;
import gw.lang.ir.statement.IRIfStatement;
import gw.lang.ir.statement.IRMethodCallStatement;
import gw.lang.ir.statement.IRMethodStatement;
import gw.lang.ir.statement.IRNewStatement;
import gw.lang.ir.statement.IRReturnStatement;
import gw.lang.ir.statement.IRStatementList;
import gw.lang.ir.statement.IRThrowStatement;
import gw.lang.parser.IBlockClass;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.ICoercionManager;
import gw.lang.parser.ICustomExpressionRuntime;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IExpression;
import gw.lang.parser.ILanguageLevel;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IReducedSymbol;
import gw.lang.parser.Keyword;
import gw.lang.parser.StandardCoercionManager;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.reflect.ClassLazyTypeResolver;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IEnumConstant;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.LazyTypeResolver;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.ParameterizedFunctionType;
import gw.lang.reflect.PropertyInfoDelegate;
import gw.lang.reflect.SimpleTypeLazyTypeResolver;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.BytecodeOptions;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.gs.IExternalSymbolMap;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuConstructorInfo;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuFragment;
import gw.lang.reflect.gs.IGosuMethodInfo;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.IGosuVarPropertyInfo;
import gw.lang.reflect.gs.StringSourceFileHandle;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaConstructorInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;
import gw.util.concurrent.LocklessLazyVar;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
public abstract class AbstractElementTransformer<T extends IParsedElement>
{
  public static final String CTX_SYMBOL_SUFFIX = "_instr_ctx";
  public static final String CTX_SYMBOL = FunctionBodyTransformationContext.TEMP_VAR_PREFIX + CTX_SYMBOL_SUFFIX;
  public static final Map<String, ICustomExpressionRuntime> CUSTOM_RUNTIMES = new ConcurrentHashMap<String, ICustomExpressionRuntime>();

  public static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
  public static final String OUTER_ACCESS = "access$0";
  public static final String CAPTURED_VAR_PREFIX = "val$";
  public static final String ENHANCEMENT_TYPE_PARAM_PREFIX = "etypeparam$";
  public static final String TYPE_PARAM_PREFIX = "typeparam$";
  public static final String ENUM_PARAM_PREFIX = "enum$";
  public static final Type OBJECT_TYPE = Type.getType( Object.class );
  public static final String ENHANCEMENT_THIS_REF = "$that$";

  private TopLevelTransformationContext _cc;
  private T _parsedElement;
  private static LocklessLazyVar<Boolean> _checkedArithmetic = new LocklessLazyVar<Boolean>() {
                                                                                                @Override
                                                                                                protected Boolean init()
                                                                                                {
                                                                                                  return Boolean.valueOf( System.getProperty("checkedArithmetic") );
                                                                                                }
                                                                                              };

  public AbstractElementTransformer( TopLevelTransformationContext cc, T parsedElem )
  {
    _cc = cc;
    _parsedElement = parsedElem;
  }

  public static void clearCustomRuntimes() {
    CUSTOM_RUNTIMES.clear();
  }

  public TopLevelTransformationContext _cc()
  {
    return _cc;
  }
  protected void setCc( TopLevelTransformationContext cc )
  {
    _cc = cc;
  }

  protected T getParsedElement()
  {
    return _parsedElement;
  }

  public ICompilableTypeInternal getGosuClass()
  {
    return _cc().getGosuClass();
  }

  public IRExpression callStaticMethod( Class cls, String strMethod, Class[] paramTypes, List<IRExpression> args )
  {
    return callMethod( cls, strMethod, paramTypes, null, args );
  }

  //TODO-sm Remove this method.
  public IRExpression callMethod( Class cls, String strMethod, Class[] paramTypes, IRExpression root, List<IRExpression> args )
  {
    return callMethod( IRMethodFactory.createIRMethod( cls, strMethod, paramTypes ), root, args );
  }

  public IRExpression callMethod( IJavaClassInfo cls, String strMethod, Class[] paramTypes, IRExpression root, List<IRExpression> args )
  {
    return callMethod( IRMethodFactory.createIRMethod( cls, strMethod, paramTypes ), root, args );
  }

  public IRExpression callMethod( IRMethod method, IRExpression root, List<IRExpression> explicitArgs )
  {
    return callMethod( method.getOwningIRType(), method, root, Collections.<IRExpression>emptyList(), explicitArgs, null, false );
  }

  public IRExpression callMethod( IRMethod method, IRExpression root, List<IRExpression> explicitArgs, int[] namedArgOrder )
  {
    return callMethod( method.getOwningIRType(), method, root, Collections.<IRExpression>emptyList(), explicitArgs, namedArgOrder, false );
  }

  public IRExpression callMethod( IRMethod method, IRExpression root, List<IRExpression> explicitArgs, int[] namedArgOrder, boolean special )
  {
    return callMethod( method.getOwningIRType(), method, root, Collections.<IRExpression>emptyList(), explicitArgs, namedArgOrder, special );
  }

  public IRExpression callSpecialMethod( IRType rootType, IRMethod method, IRExpression root, List<IRExpression> explicitArgs )
  {
    return callMethod( rootType, method, root, Collections.<IRExpression>emptyList(), explicitArgs, null, true );
  }
  public IRExpression callSpecialMethod( IRType rootType, IRMethod method, IRExpression root, List<IRExpression> explicitArgs, int[] namedArgOrder )
  {
    return callSpecialMethod( rootType, method, root, Collections.<IRExpression>emptyList(), explicitArgs, namedArgOrder );
  }
  public IRExpression callSpecialMethod( IRType rootType, IRMethod method, IRExpression root, List<IRExpression> implicitArgs, List<IRExpression> explicitArgs, int[] namedArgOrder )
  {
    return callMethod( rootType, method, root, implicitArgs, explicitArgs, namedArgOrder, true );
  }

  private IRExpression callMethod( IRType rootType, IRMethod method, IRExpression root,
                                   List<IRExpression> implicitArgs,
                                   List<IRExpression> explicitArgs, int[] namedArgOrder,
                                   boolean special )
  {
    IType owner = method.getOwningIType();

    List<IRExpression> actualArgs = new ArrayList<IRExpression>();

    List<IRElement> namedArgElements = handleNamedArgs( explicitArgs, namedArgOrder );

    if( owner instanceof IGosuEnhancement && !method.isStatic() )
    {
      // For enhancements, we want argument/root evaluation to happen the same as for a normal method
      // So we evaluate all the arguments to temp variables, then evaluate the root and null check-it,
      // and lastly invoke the method
      List<IRExpression> tempArgs = new ArrayList<IRExpression>();
      pushEnhancementTypeParams( owner, tempArgs );
      pushTypeParams( method, tempArgs );
      if( shouldAddExternalSymbolsMapToCall( method ) )
      {
        actualArgs.add( pushExternalSymbolsMap() );
      }
      tempArgs.addAll( implicitArgs );
      tempArgs.addAll( explicitArgs );

      List<IRElement> compositeElements = new ArrayList<IRElement>();
      // Store the root to a temp variable and null-check it
      IRSymbol tempRoot = _cc().makeAndIndexTempSymbol( root.getType() );
      compositeElements.add( buildAssignment( tempRoot, root ) );

      // Add the temp named arg assignments (if any)
      compositeElements.addAll( namedArgElements );

      // Now store all the arguments to temp variables
      for( IRExpression tempArg : tempArgs )
      {
        IRSymbol tempArgSymbol = _cc().makeAndIndexTempSymbol( tempArg.getType() );
        compositeElements.add( buildAssignment( tempArgSymbol, tempArg ) );
        actualArgs.add( identifier( tempArgSymbol ) );
      }

      // Null-check the temp root
      compositeElements.add( nullCheckVar( tempRoot ) );

      // Push the root expression on as the first argument to the method
      actualArgs.add( 0, identifier( tempRoot ) );

      // Now call the method as if it were a static method
      compositeElements.add( callMethod( method, null, special, owner, actualArgs ) );
      return new IRCompositeExpression( compositeElements );
    }
    else
    {
      // Otherwise, add in any implicit type parameter arguments, then call the method normally
      pushTypeParams( method, actualArgs );
      if( shouldAddExternalSymbolsMapToCall( method ) )
      {
        actualArgs.add( pushExternalSymbolsMap() );
      }
      actualArgs.addAll( implicitArgs );
      actualArgs.addAll( explicitArgs );

      if( !namedArgElements.isEmpty() )
      {
        List<IRElement> compositeElements = new ArrayList<IRElement>();

        // Add the temp named arg assignments (if any)
        compositeElements.addAll( namedArgElements );
        compositeElements.add( callMethod( method, root, special, owner, actualArgs ) );
        return new IRCompositeExpression( compositeElements );
      }
      else
      {
        return callMethod( method, root, special, owner, actualArgs );
      }
    }
  }

  /**
   * Facilitates evaluation of named args in lexical order.  E.g.,
   * <pre>
   *   callMe( :param3 = expr1, :param1 = expr2, :param2 = expr3 )
   * </pre>
   * is parsed as:
   * <pre>
   *   callMe( expr2, expr3, expr1 )
   * </pre>
   * which is compiled to:
   * <pre>
   *   temp1 = expr1
   *   temp2 = expr2
   *   temp3 = expr3
   *   callMe( temp2, temp3, temp1 )
   * </pre>
   * @param explicitArgs  The arg expressions in proper order for the target call. The expressions are replaced with the temp vars.
   * @param namedArgOrder The lexical ordering of the args
   * @return The temp var assignments in lexical order
   */
  public List<IRElement> handleNamedArgs( List<IRExpression> explicitArgs, int[] namedArgOrder )
  {
    List<IRElement> namedArgElements = new ArrayList<IRElement>();
    if( namedArgOrder != null && namedArgOrder.length > 0 )
    {
      boolean b = namedArgOrder.length == explicitArgs.size();
      assert b;
      //noinspection ForLoopReplaceableByForEach
      for( int i = 0; i < namedArgOrder.length; i++ )
      {
        IRExpression argExpr = explicitArgs.get( namedArgOrder[i] );
        IRSymbol tempArgSymbol = _cc().makeAndIndexTempSymbol( argExpr.getType() );
        namedArgElements.add( buildAssignment( tempArgSymbol, argExpr ) );
        explicitArgs.set( namedArgOrder[i], identifier( tempArgSymbol ) );
      }
    }
    return namedArgElements;
  }

  private boolean shouldAddExternalSymbolsMapToCall( IRMethod method )
  {
    return (method.getOwningIType() instanceof IGosuProgram ||
            (method.isStatic() && isProgramOrEnclosedInProgram( method.getOwningIType() ))) &&
           !method.getName().equals("access$0") &&
           !method.isGeneratedEnumMethod() &&
           !(method.getOwningIType() instanceof IGosuTemplateInternal) &&
           !(method instanceof SyntheticIRMethod);
  }

  private IRExpression callMethod( IRMethod method, IRExpression root, boolean special, IType owner, List<IRExpression> actualArgs ) {
    IRType rootType = root == null ? null : root.getType();
    if( rootType == null && method.isStatic() )
    {
      rootType = method.getOwningIRType();
    }
    if( !special && _cc().shouldUseReflection( owner, rootType, method.getAccessibility() ) )
    {
      return callMethodReflectively( owner, method.getName(), method.getReturnType(), method.getAllParameterTypes(), root, actualArgs );
    }
    else
    {
      return callMethodDirectly( method, root, special, owner, actualArgs );
    }
  }

  private IRMethodCallExpression callMethodDirectly( IRMethod method, IRExpression root, boolean special, IType owner, List<IRExpression> actualArgs )
  {
    List<IRExpression> convertedArgs = new ArrayList<IRExpression>();
    List<IRType> paramTypes = method.getAllParameterTypes();
    for (int i = 0; i < actualArgs.size(); i++) {
      convertedArgs.add( IRArgConverter.castOrConvertIfNecessary( paramTypes.get( i ), actualArgs.get( i ) ) );
    }
    IRMethodCallExpression result = buildMethodCall(method.getOwningIRType(), method.getName(), owner.isInterface(), method.getReturnType(), paramTypes, root, convertedArgs);
    if ( special ) {
      result.setSpecial( true );
    }
    return result;
  }

  private void pushEnhancementTypeParams( IType enhancementType, List<IRExpression> args )
  {
    if( enhancementType.isParameterizedType() )
    {
      for( IType typeParam : enhancementType.getTypeParameters() )
      {
        args.add( pushLazyType( typeParam ) );
      }
    }
    else if( enhancementType.isGenericType() )
    {
      for( IGenericTypeVariable typeVariable : enhancementType.getGenericTypeVariables() )
      {
        args.add( pushLazyType( typeVariable.getTypeVariableDefinition().getType() ) );
      }
    }
  }

  private IRExpression callMethodReflectively( IType owner, String strMethod, IRType returnType, List<IRType> paramTypes,
                                               IRExpression root, List<IRExpression> args)
  {
    // Our three arguments to getDeclaredMethods are the owners Class, the name of the method, and
    // a Class[] containing the types of the parameters
    List<IRExpression> argExprs = new ArrayList<IRExpression>();
    IRType irOwnerType = getDescriptor( owner );
    argExprs.add( classLiteral( irOwnerType ) );
    argExprs.add( stringLiteral( strMethod ) );

    List<IRExpression> paramClasses = new ArrayList<IRExpression>();
    for (IRType paramType : paramTypes) {
      paramClasses.add( classLiteral( IRElement.maybeEraseStructuralType( irOwnerType, paramType ) ) );
    }
    argExprs.add( buildInitializedArray( getDescriptor( Class.class ), paramClasses ) );

    // So we're returning a call to "invoke" where the root is the result of call built to getDeclaredMethod,
    // and the arguments are 1) the root expression of the method call (or the null literal, if that's null)
    // and 2) an Object[] containing the values of all the arguments to the method

    // We need to make sure all the args are boxed, since they're going into an Object[]
    List<IRExpression> boxedArgs = new ArrayList<IRExpression>();
    for (IRExpression arg : args) {
      boxedArgs.add( boxValue( arg.getType(), arg ) );
    }
    argExprs.add( root == null ? pushNull() : root );
    argExprs.add( buildInitializedArray(IRTypeConstants.OBJECT(), boxedArgs ) );

    IRExpression invokeCall = buildMethodCall( GosuRuntimeMethods.class, "invokeMethod", Object.class, new Class[]{Class.class, String.class, Class[].class, Object.class, Object[].class},
                                               null, argExprs );
    if( returnType.isVoid() )
    {
      // As a side note, we don't need to worry about POPing anything here:  if the method had a void return type, it should be wrapped in a statement
      // anyway.  The statement compiler will see that the underlying return type of its expression is Object instead of void,
      // and will generate the POP instruction itself.
      return invokeCall;
    } else {
      return unboxValueToType( returnType, invokeCall );
    }
  }

  private void pushTypeParams( IRMethod irMethod, List<IRExpression> args )
  {
    IFunctionType funcType = irMethod.getFunctionType();
    if( funcType == null || !irMethod.couldHaveTypeVariables() )
    {
      return;
    }

    if( funcType.isParameterizedType() )
    {
      IType[] typeParams = funcType.getTypeParameters();
      for( IType typeParam : typeParams )
      {
        args.add( pushLazyType( typeParam ) );
      }
    }
    else if( irMethod.getTypeVariables() != null )
    {
      IGenericTypeVariable[] typeVars = irMethod.getTypeVariables();
      for( IGenericTypeVariable typeVariable : typeVars )
      {
        args.add( pushLazyType( typeVariable.getBoundingType() ) );
      }
    }
    else if( funcType.isGenericType() )
    {
      for( IGenericTypeVariable typeVariable : funcType.getGenericTypeVariables() )
      {
        args.add( pushLazyType( typeVariable.getTypeVariableDefinition().getType().getBoundingType() ) );
      }
    }
  }

  public IRStatement nullCheckVar( IRSymbol symbol )
  {
    return new IRIfStatement( new IREqualityExpression( identifier( symbol ), nullLiteral(), true ),
                              new IRThrowStatement( buildNewExpression( getDescriptor(NullPointerException.class), Collections.<IRType>emptyList(), exprList())),
                              null);
  }

  public IRExpression pushLazyType( IType type )
  {
    return pushLazyType( type, null );
  }
  public IRExpression pushLazyType( IType type, IGenericTypeVariable[] tvs )
  {
    if( tvs == null || tvs.length == 0 )
    {
      IRExpression fastAccessType = maybePushFastLazyType( type );
      if( fastAccessType != null ) {
        return fastAccessType;
      }
    }
    return pushLazyTypeWithInvokeDynamic( type, tvs );
  }

  private IRExpression pushLazyTypeWithInvokeDynamic( IType type, IGenericTypeVariable[] tvs ) {
    IRMethodStatement method = makeLazyTypeMethod( type, tvs );
    _cc().getIrClass().addMethod( method );
    return buildNewExpression( LazyTypeResolver.class, new Class[]{LazyTypeResolver.ITypeResolver.class},
                               Collections.<IRExpression>singletonList( buildLazyTypeResolverCall( method, tvs ) ) );
  }

  private IRExpression maybePushFastLazyType( IType type )
  {
    IType genType = TypeLord.getPureGenericType( type );

    if( genType == type &&
        (!type.isArray() || !TypeLord.getCoreType( type ).isParameterizedType()) &&
        Modifier.isPublic( TypeLord.getCoreType( type ).getModifiers() ) )
    {
      // Non-generic Java or Gosu type (can be array)...

      if( type instanceof IJavaType )
      {
        Class backingClass = ((IJavaType)type).getBackingClass();
        if( backingClass != null )
        {
          ClassLoader cl = backingClass.getClassLoader();
          if( cl == null || cl == ClassLoader.getSystemClassLoader() )
          {
            String fieldName = ClassLazyTypeResolver.getCachedFieldName( backingClass );
            if( fieldName != null )
            {
              // Frequently used Java system classes

              // Extra Fast (static field access) lazy resolves using cached frequenlty used Java type
              return buildFieldGet( getDescriptor( ClassLazyTypeResolver.class ), fieldName, getDescriptor( ClassLazyTypeResolver.class ), null );
            }
            else
            {
              // Other Java system classes

              // Fast -- (new expr) lazy resolves with class-based lookup
              return buildNewExpression( ClassLazyTypeResolver.class, new Class[]{Class.class}, Collections.singletonList( classLiteral( getDescriptor( type ) ) ) );
            }
          }
        }
      }

      if( type instanceof IGosuClass )
      {
        // Fast -- (new expr) lazy resolves using fqn + module name lookup
        return pushLazyTypeByFqn( type );
      }

      else if( genType instanceof TypeVariableType )
      {
        // OMG Fast, straight reference (ALoad or GetField access) to existing LazyTypeResolver for type var
        return getRuntimeTypeParameter( (TypeVariableType)genType );
      }
    }

    // Fast Enough -- (invokedynamic) lazy resolves by calling into generated method on enclosing class, which in turn looks up by name etc. (basically the code generated by pushType() is what is executed inside the generated itype$X method)
    return null;
  }

  private IRCompositeExpression buildLazyTypeResolverCall( IRMethodStatement method, IGenericTypeVariable[] tvs ) {
    DynamicFunctionSymbol compilingDfs = _cc().getCurrentFunction();
    if( _cc().isStatic() ||
        (compilingDfs != null && compilingDfs.isStatic()) ||
        !_cc().hasSuperBeenInvoked() ||
        _cc().getGosuClass() instanceof IGosuFragment ) {
      return buildStaticLazyTypeResolverCall( method );
    }
    else if( getGosuClass() instanceof IGosuEnhancement ) {
      return buildEnhancementLazyTypeResolverCall( method );
    }
    else if( tvs != null ) {
      return buildSuperCallLazyTypeResolverCall( method, tvs );
    }
    else {
      return buildInstanceLazyTypeResolverCall( method );
    }
  }
  private IRCompositeExpression buildStaticLazyTypeResolverCall( IRMethodStatement method ) {
    IGenericTypeVariable[] tvs = getGenericFunctionTypeVariables( _cc().getCurrentFunction() );
    IRElement[] exprs = new IRElement[1 + (tvs == null ? 0 : tvs.length)];
    int i = 0;
    if( tvs != null ) {
      for( IGenericTypeVariable tv: tvs ) {
        exprs[i++] = identifier( _cc().getSymbol( getTypeVarParamName( tv ) ) );
      }
    }
    exprs[i] = new IRLazyTypeMethodCallExpression( method.getName(), getDescriptor( getGosuClass() ), getGosuClass().getName(), tvs == null ? 0 : tvs.length, true );
    return buildComposite( exprs );
  }
  private IRCompositeExpression buildEnhancementLazyTypeResolverCall( IRMethodStatement method ) {
    IGenericTypeVariable[] tvs = getGenericFunctionTypeVariables( _cc().getCurrentFunction() );
    IRElement[] exprs = new IRElement[1 + (tvs == null ? 0 : tvs.length)];
    int i = 0;
    if( tvs != null ) {
      for( IGenericTypeVariable tv: tvs ) {
        exprs[i++] = identifier( _cc().getSymbol( getTypeVarParamName( tv ) ) );
      }
    }
    exprs[i] = new IRLazyTypeMethodCallExpression( method.getName(), getDescriptor( getGosuClass() ), getGosuClass().getName(), tvs == null ? 0 : tvs.length, true );
    return buildComposite( exprs );
  }
  private IRCompositeExpression buildInstanceLazyTypeResolverCall( IRMethodStatement method ) {
    IGenericTypeVariable[] tvs = getGenericFunctionTypeVariables( _cc().getCurrentFunction() );
    IRElement[] exprs = new IRElement[2 + (tvs == null ? 0 : tvs.length)];
    int i = 0;
    exprs[i++] = pushThis();
    if( tvs != null ) {
      for( IGenericTypeVariable tv: tvs ) {
        exprs[i++] = identifier( _cc().getSymbol( getTypeVarParamName( tv ) ) );
      }
    }
    exprs[i] = new IRLazyTypeMethodCallExpression( method.getName(), getDescriptor( getGosuClass() ), getGosuClass().getName(), tvs == null ? 0 : tvs.length, false );
    return buildComposite( exprs );
  }
  private IRCompositeExpression buildSuperCallLazyTypeResolverCall( IRMethodStatement method, IGenericTypeVariable[] tvs ) {
    tvs = tvs != null ? tvs : getGenericFunctionTypeVariables( _cc().getCurrentFunction() );
    IRElement[] exprs = new IRElement[1 + (tvs == null ? 0 : tvs.length)];
    int i = 0;
    if( tvs != null ) {
      for( IGenericTypeVariable tv: tvs ) {
        exprs[i++] = identifier( _cc().getSymbol( getTypeVarParamName( tv ) ) );
      }
    }
    exprs[i] = new IRLazyTypeMethodCallExpression( method.getName(), getDescriptor( getGosuClass() ), getGosuClass().getName(), tvs == null ? 0 : tvs.length, true );
    return buildComposite( exprs );
  }

  private IRMethodStatement makeLazyTypeMethod( IType type, IGenericTypeVariable[] tvs ) {
    DynamicFunctionSymbol compilingDfs = _cc().getCurrentFunction();
    if( _cc().isStatic() ||
        (compilingDfs != null && compilingDfs.isStatic()) ||
        !_cc().hasSuperBeenInvoked() ||
        _cc().getGosuClass() instanceof IGosuFragment ) {
      return makeStaticLazyTypeMethod( type );
    }
    else if( getGosuClass() instanceof IGosuEnhancement ) {
      return makeEnhancementLazyTypeMethod( type );
    }
    else if( tvs != null ) {
      return makeSuperCallLazyTypeMethod( type, tvs );
    }
    else {
      return makeInstanceLazyTypeMethod( type );
    }
  }
  private IRMethodStatement makeStaticLazyTypeMethod( IType type ) {
    IGenericTypeVariable[] tvs = getGenericFunctionTypeVariables( _cc().getCurrentFunction() );
    List<IRSymbol> functionTypeVarParams = new ArrayList<IRSymbol>();
    if( tvs != null ) {
      for( IGenericTypeVariable tv: tvs ) {
          functionTypeVarParams.add( _cc().getSymbol( getTypeVarParamName( tv ) ) );
      }
    }
    int iIndex = _cc().incrementLazyTypeMethodCount();
    String methodName = "itype$" + iIndex;

    return new IRMethodStatement(
      new IRStatementList( true, new IRReturnStatement( null, pushType( type ) ) ),
      methodName,
      Opcodes.ACC_PRIVATE | Opcodes.ACC_SYNTHETIC | Opcodes.ACC_STATIC,
      false, getDescriptor( IType.class ),
      functionTypeVarParams );
  }
  private IRMethodStatement makeEnhancementLazyTypeMethod( IType type ) {
    IGenericTypeVariable[] tvs = getGenericFunctionTypeVariables( _cc().getCurrentFunction() );
    List<IRSymbol> functionTypeVarParams = new ArrayList<IRSymbol>();
    if( tvs != null ) {
      for( IGenericTypeVariable tv: tvs ) {
          functionTypeVarParams.add( _cc().getSymbol( getTypeVarParamName( tv ) ) );
      }
    }
    int iIndex = _cc().incrementLazyTypeMethodCount();
    String methodName = "itype$" + iIndex;

    return new IRMethodStatement(
      new IRStatementList( true, new IRReturnStatement( null, pushType( type ) ) ),
      methodName,
      Opcodes.ACC_PRIVATE | Opcodes.ACC_SYNTHETIC | Opcodes.ACC_STATIC,
      false, getDescriptor( IType.class ),
      functionTypeVarParams );
  }
  private IRMethodStatement makeSuperCallLazyTypeMethod( IType type, IGenericTypeVariable[] tvs ) {
    boolean bStatic = tvs != null;
    tvs = tvs == null ? getGenericFunctionTypeVariables( _cc().getCurrentFunction() ) : tvs;
    List<IRSymbol> functionTypeVarParams = new ArrayList<IRSymbol>();
    if( tvs != null ) {
      for( IGenericTypeVariable tv: tvs ) {
          functionTypeVarParams.add( _cc().getSymbol( getTypeVarParamName( tv ) ) );
      }
    }
    int iIndex = _cc().incrementLazyTypeMethodCount();
    String methodName = "itype$" + iIndex;

    return new IRMethodStatement(
      new IRStatementList( true, new IRReturnStatement( null, pushType( type ) ) ),
      methodName,
      Opcodes.ACC_PRIVATE | Opcodes.ACC_SYNTHETIC | (bStatic ? Opcodes.ACC_STATIC : 0),
      false, getDescriptor( IType.class ),
      functionTypeVarParams );
  }
  private IRMethodStatement makeInstanceLazyTypeMethod( IType type ) {
    IGenericTypeVariable[] tvs = getGenericFunctionTypeVariables( _cc().getCurrentFunction() );
    List<IRSymbol> functionTypeVarParams = new ArrayList<IRSymbol>();
    if( tvs != null ) {
      for( IGenericTypeVariable tv: tvs ) {
        functionTypeVarParams.add( _cc().getSymbol( getTypeVarParamName( tv ) ) );
      }
    }
    int iIndex = _cc().incrementLazyTypeMethodCount();
    String methodName = "itype$" + iIndex;

    return new IRMethodStatement(
      new IRStatementList( true, new IRReturnStatement( null, pushType( type ) ) ),
      methodName,
      Opcodes.ACC_PRIVATE | Opcodes.ACC_SYNTHETIC,
      false, getDescriptor( IType.class ),
      functionTypeVarParams );
  }

  private IGenericTypeVariable[] getGenericFunctionTypeVariables( IDynamicFunctionSymbol currentFunction ) {
    if( currentFunction == null ) {
      return null;
    }
    IDynamicFunctionSymbol csr = currentFunction;
    while( csr != null && csr.getType() instanceof ParameterizedFunctionType ) {
      currentFunction = currentFunction.getBackingDfs();
    }
    List<IGenericTypeVariable> typeVarsForDFS = getTypeVarsForDFS( currentFunction );
    return typeVarsForDFS.toArray( new IGenericTypeVariable[typeVarsForDFS.size()] );
  }

  public IRExpression pushType( IType type )
  {
    return pushType( type, false );
  }

  public IRExpression pushType( IType type, boolean bKeepLiteralType )
  {
    IType genType = TypeLord.getPureGenericType( type );

    if( genType == type &&
        type instanceof IJavaType &&
        (!type.isArray() || !TypeLord.getCoreType( type ).isParameterizedType()) &&
        Modifier.isPublic( TypeLord.getCoreType( type ).getModifiers() ) )
    {
      Class backingClass = ((IJavaType)type).getBackingClass();
      if( backingClass != null )
      {
        ClassLoader cl = backingClass.getClassLoader();
        if( cl == null || cl == ClassLoader.getSystemClassLoader() )
        {
          // Push the class constant (better performing than resolving by name)
          return callStaticMethod( TypeSystem.class, "get", new Class[] {Class.class},
                                   exprList( pushConstant( backingClass ) ) );
        }
      }
    }

    if (type.isArray())
    {
      IRExpression componentType = pushType( type.getComponentType(), bKeepLiteralType );
      return callMethod(IType.class, "getArrayType", new Class[0], componentType, exprList());
    }
    else if( genType instanceof TypeVariableType ||
             genType instanceof TypeVariableArrayType )
    {
      return pushRuntimeTypeOfTypeVar( genType );
    }
    else if( type instanceof MetaType )
    {
      if( bKeepLiteralType && ((MetaType)type).isLiteral() )
      {
        return callStaticMethod( MetaType.class, "getLiteral", new Class[]{IType.class},
                Collections.singletonList( pushType( ((MetaType)type).getType() ) ) );
      }
      else
      {
        return callStaticMethod( MetaType.class, "get", new Class[]{IType.class},
                Collections.singletonList( pushType( ((MetaType)type).getType() ) ) );
      }
    }
    else if( type instanceof IBlockType )
    {
      IBlockType blockType = (IBlockType)type;
      List<IRExpression> args = new ArrayList<IRExpression>();

      args.add( pushType( blockType.getReturnType() ) );
      args.add( pushArrayOfTypes( blockType.getParameterTypes() ) );
      args.add( pushArrayOfString( blockType.getParameterNames() ) );
      //## todo: this won't work, IExpression is not a constant type
      args.add( pushArrayOfDefValueExpr( blockType.getDefaultValueExpressions() ) );
      return buildNewExpression( getDescriptor( BlockType.class ),
                                 Arrays.asList( IRTypeConstants.ITYPE(), getDescriptor( IType[].class ), getDescriptor( String[].class ), getDescriptor( IExpression[].class ) ),
                                 args );
    }
    else if( type instanceof IFunctionType )
    {
      IFunctionType funcType = (IFunctionType)type;
      List<IRExpression> args = new ArrayList<IRExpression>();
      args.add( pushConstant(funcType.getName() ) );
      args.add( pushType( funcType.getReturnType() ) );
      args.add( pushArrayOfTypes( funcType.getParameterTypes() ) );
      return buildNewExpression( getDescriptor( FunctionType.class ),
                                 Arrays.asList( IRTypeConstants.STRING(), IRTypeConstants.ITYPE(), getDescriptor( IType[].class ) ),
                                 args );
    }
    else if( type instanceof CompoundType )
    {
      CompoundType compoundType = (CompoundType)type;

      List<IRElement> elements = new ArrayList<IRElement>();
      IRSymbol tempSet = _cc().makeAndIndexTempSymbol( getDescriptor( HashSet.class ) );

      // Create a HashSet
      elements.add( buildAssignment( tempSet, buildNewExpression(getDescriptor( HashSet.class ), Collections.<IRType>emptyList(), Collections.<IRExpression>emptyList()) ) );

      //Fill it in with the component types
      for( IType iType : compoundType.getTypes() )
      {
        pushType( iType );
        elements.add(new IRMethodCallStatement(
                callMethod( Set.class, "add", new Class[]{Object.class},
                identifier(tempSet),
                Collections.singletonList( pushType( iType ) ) ) ) );
      }

      // The last expression is a reference to the temp symbol
      elements.add( identifier( tempSet ) );

      IRExpression setCreation = new IRCompositeExpression(elements);
      return callStaticMethod( CompoundType.class, "get", new Class[]{Set.class},
              Collections.singletonList(setCreation));
    }
    else
    {
      IModule module = getModule(genType);
      IRExpression result = callStaticMethod( TypeSystem.class, "getByFullName", new Class[]{String.class, String.class},
              Arrays.asList(pushConstant( genType.getName() ), module == null ? pushNull() : pushConstant( module.getName() ) ) );

      if( type.isParameterizedType() && !TypeLord.isRecursiveType( type ) && !isRecursiveTypeParsing( type ) )
      {
        result = callMethod( IType.class, "getParameterizedType", new Class[]{IType[].class},
                result,
                Collections.singletonList(pushArrayOfTypes( type.getTypeParameters() ) ) );
      }
      return result;
    }
  }

  private boolean isRecursiveTypeParsing( IType type )
  {
    for( IType typeParam : type.getTypeParameters() )
    {
      if( typeParam == GosuParser.PENDING_BOUNDING_TYPE )
      {
        // type is errant because at least one of it's type variables recurse,
        // therefore we can't load the default parameterized type for it.
        return true;
      }
    }
    return false;
  }

  protected IRExpression pushArrayOfString( String[] array )
  {
    if( array != null )
    {
      List<IRExpression> elements = new ArrayList<IRExpression>( array.length );
      for( String o : array )
      {
        elements.add( pushConstant( o ) );
      }
      return buildInitializedArray( getDescriptor( array.getClass().getComponentType() ), elements );
    }
    return pushNull();
  }

  protected IRExpression pushArrayOfDefValueExpr( IExpression[] array )
  {
    if( array != null )
    {
      List<IRExpression> elements = new ArrayList<IRExpression>( array.length );
      for( IExpression o : array )
      {
        elements.add( pushConstant( o ) );
      }
      return buildInitializedArray( getDescriptor( array.getClass().getComponentType() ), elements );
    }
    return pushNull();
  }

  public IRExpression pushArrayOfTypes( IType[] types )
  {
    List<IRExpression> values = new ArrayList<IRExpression>();
    for( IType type : types ) {
      values.add( pushType( type ) );
    }
    return buildInitializedArray( IRTypeConstants.ITYPE(), values );
  }

  public static boolean requiresImplicitEnhancementArg( ReducedDynamicFunctionSymbol dfs )
  {
    return isEnhancementType( dfs.getGosuClass() ) && !dfs.isStatic();
  }

  public static boolean requiresImplicitEnhancementArg(IGosuMethodInfo mi)
  {
    return isEnhancementType( mi.getOwnersType() ) && !mi.isStatic();
  }

  public IJavaClassInfo[] getClassInfos( IType[] parameters )
  {
    IJavaClassInfo[] paramTypes = new IJavaClassInfo[parameters.length];
    for( int i = 0; i < parameters.length; i++ )
    {
      IType paramType = parameters[i];
      if ((paramType instanceof IJavaType)) {
        paramTypes[i] = ((IJavaType)paramType).getBackingClassInfo();
      } else {
        IRType descriptor = getDescriptor( paramType );
        int iDims = 0;
        while( descriptor.isArray() ) {
          descriptor = descriptor.getComponentType();
          iDims++;
        }
        paramTypes[i] = TypeSystem.getJavaClassInfo( descriptor.getName(), getModule( paramType ));
        while( iDims-- > 0 ) {
          paramTypes[i] = paramTypes[i].getArrayType();
        }
      }
    }
    return paramTypes;
  }

  private IModule getModule(IType type) {
    return TypeSystem.getGlobalModule();
  }

  public IType getConcreteType( IType type )
  {
    if( type != null )
    {
      if( type instanceof TypeVariableType )
      {
        return getConcreteType( ((TypeVariableType)type).getBoundingType() );
      }
      else if( type instanceof TypeVariableArrayType )
      {
        return getConcreteType( type.getComponentType() ).getArrayType();
      }
    }
    return type;
  }

  public static IRType getDescriptor( IType type) {
    return IRTypeResolver.getDescriptor( type );
  }

  public static IRType getDescriptor( IType type, boolean getConcreteTypeForMetaType)
  {
    return IRTypeResolver.getDescriptor( type, getConcreteTypeForMetaType );
  }

  public static IRType getDescriptor( Class cls )
  {
    return IRTypeResolver.getDescriptor( cls );
  }

  public static IRType getDescriptor( IJavaClassInfo cls )
  {
    return IRTypeResolver.getDescriptor( cls );
  }

  public IRExpression getDefaultConstIns( IType type )
  {
    if( type == JavaTypes.pBYTE()) {
      return numericLiteral(Byte.valueOf((byte) 0));
    }
    else if (type == JavaTypes.pCHAR()) {
      return numericLiteral(0);
    }
    else if (type == JavaTypes.pSHORT()) {
      return numericLiteral(Short.valueOf((short) 0));
    }
    else if (type == JavaTypes.pINT()) {
      return numericLiteral(0);
    }
    else if (type == JavaTypes.pBOOLEAN()) {
      return booleanLiteral(false);
    }
    else if( type == JavaTypes.pLONG() )
    {
      return numericLiteral(Long.valueOf(0));
    }
    else if( type == JavaTypes.pFLOAT() )
    {
      return numericLiteral(Float.valueOf(0));
    }
    else if( type == JavaTypes.pDOUBLE() )
    {
      return numericLiteral(Double.valueOf(0));
    }
    else
    {
      return nullLiteral();
    }
  }

  public int getModifiers( Symbol symbol )
  {
    int iAccModifiers;
    int iSymModifiers = symbol.getModifiers();

    if( BytecodeOptions.isSingleServingLoader() )
    {
      if ( Modifier.isPrivate( iSymModifiers ) && isReadObjectOrWriteObjectMethod( symbol ))
      {
        // Serialization demands that readObject and writeObject be private methods,
        // so if we're compiling one of them we've got to actually make the thing private
        iAccModifiers = Opcodes.ACC_PRIVATE;
      }
      else
      {
        // Otherwise, we generate everything as "public" so we don't have to worry about all the crazy
        // classloader issues
        iAccModifiers = Opcodes.ACC_PUBLIC;
      }
    }
    else if( Modifier.isPublic( iSymModifiers ) || ( Modifier.isProtected( iSymModifiers ) && isCompilingEnhancement() ) )
    {
      // We compile protected enhancement methods as public so they'll be visible to subclasses without needing
      // reflective hacks.  Protected methods on enhancements should be discouraged:  see PL-10398  
      iAccModifiers = Opcodes.ACC_PUBLIC;
    }
    else if( Modifier.isProtected( iSymModifiers ) )
    {
      iAccModifiers = Opcodes.ACC_PROTECTED;
    }
    else if( Modifier.isInternal( iSymModifiers ) )
    {
      iAccModifiers = 0;
    }
    else if( Modifier.isPrivate( iSymModifiers ) )
    {
      if ( isReadObjectOrWriteObjectMethod( symbol ))
      {
        // Serialization demands that readObject and writeObject be private methods,
        // so if we're compiling one of them we've got to actually make the thing private
        iAccModifiers = Opcodes.ACC_PRIVATE;
      }
      else
      {
        // Otherwise, we generate private methods as "internal" access so that we don't have to
        // generate bridge methods for the sake of inner class accesses to private members.  Because
        // bridge methods are an abomination and a moral outrage.  We're angry about it.  You should be too.
        iAccModifiers = 0; // internal access
      }
    }
    else
    {
      iAccModifiers = Opcodes.ACC_PUBLIC;
    }

    if( Modifier.isFinal( iSymModifiers ) )
    {
      iAccModifiers |= Opcodes.ACC_FINAL;
    }

    if( Modifier.isStatic( iSymModifiers ) )
    {
      iAccModifiers |= Opcodes.ACC_STATIC;
    }

    if( Modifier.isAbstract( iSymModifiers ) )
    {
      iAccModifiers |= Opcodes.ACC_ABSTRACT;
    }

    if( Modifier.isEnum( iSymModifiers ) )
    {
      iAccModifiers |= Opcodes.ACC_ENUM;
    }

    if( Modifier.isTransient( iSymModifiers ) )
    {
      iAccModifiers |= Opcodes.ACC_TRANSIENT;
    }

    if( Modifier.isDeprecated( iSymModifiers ) )
    {
      iAccModifiers |= Opcodes.ACC_DEPRECATED;
    }

    if( isCompilingEnhancement() ) // enhancement methods are always static
    {
      iAccModifiers |= Opcodes.ACC_STATIC;
    }

    return iAccModifiers;
  }

  // Determines if this is the special "readObject" or "writeObject" method used by serialization
  // Returns true if that is the case, false otherwise
  private boolean isReadObjectOrWriteObjectMethod( Symbol symbol ) {
    if ( symbol instanceof DynamicFunctionSymbol ) {
      DynamicFunctionSymbol dfs = (DynamicFunctionSymbol) symbol;
      if (dfs.getDisplayName().equals("readObject")) {
        IType[] argTypes = dfs.getArgTypes();
        return argTypes != null &&
               argTypes.length == 1 &&
               argTypes[0].equals(JavaTypes.getJreType(java.io.ObjectInputStream.class)) &&
               dfs.getReturnType().equals(JavaTypes.pVOID());

      } else if (dfs.getDisplayName().equals("writeObject")) {
        IType[] argTypes = dfs.getArgTypes();
        return argTypes != null &&
               argTypes.length == 1 &&
               argTypes[0].equals(JavaTypes.getJreType(java.io.ObjectOutputStream.class)) &&
               dfs.getReturnType().equals(JavaTypes.pVOID());
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  public boolean isCompilingEnhancement()
  {
    return _cc.compilingEnhancement();
  }

  public boolean isProgramOrEnclosedInProgram( IType type )
  {
    if( type == null )
    {
      return false;
    }
    if( type instanceof IGosuProgram )
    {
      return true;
    }
    return isProgramOrEnclosedInProgram( type.getEnclosingType() );
  }

  public IRExpression boxValue( IType lhsType, IRExpression root )
  {
    // If it's not a primitive, or the type is void (likely because it's a null expression), just return the expression back out
    if( !lhsType.isPrimitive() || lhsType == JavaTypes.pVOID() )
    {
      return root;
    }

    if( lhsType == JavaTypes.pBOOLEAN() )
    {
      return callStaticMethod( Boolean.class, "valueOf", new Class[]{boolean.class}, Collections.singletonList(root) );
    }
    if( lhsType == JavaTypes.pBYTE() )
    {
      return callStaticMethod( Byte.class, "valueOf", new Class[]{byte.class}, Collections.singletonList(root) );
    }
    if( lhsType == JavaTypes.pCHAR() )
    {
      return callStaticMethod( AbstractElementTransformer.class, "valueOf", new Class[]{char.class}, Collections.singletonList(root) );
    }
    if( lhsType == JavaTypes.pSHORT() )
    {
      return callStaticMethod( Short.class, "valueOf", new Class[]{short.class}, Collections.singletonList(root) );
    }
    if( lhsType == JavaTypes.pINT() )
    {
      return callStaticMethod( Integer.class, "valueOf", new Class[]{int.class}, Collections.singletonList(root) );
    }
    else if( lhsType == JavaTypes.pLONG() )
    {
      return callStaticMethod( Long.class, "valueOf", new Class[]{long.class}, Collections.singletonList(root) );
    }
    else if( lhsType == JavaTypes.pFLOAT() )
    {
      return callStaticMethod( Float.class, "valueOf", new Class[]{float.class}, Collections.singletonList(root) );
    }
    else if( lhsType == JavaTypes.pDOUBLE() )
    {
      return callStaticMethod( Double.class, "valueOf", new Class[]{double.class}, Collections.singletonList(root) );
    } else {
      throw new IllegalArgumentException("Unexpected type " + lhsType.getName());
    }
  }

  // Necessary so that unicode chars that are in the negative integer range
  // don't cause an IndexOutOfBoundsException e.g., Character.valueOf( char )
  // chokes on negative chars
  public static Character valueOf( char c )
  {
    if( c >= 0 && c <= 127 )
    {
      return c;
    }
    //noinspection UnnecessaryBoxing
    return new Character( c );
  }

  public IRExpression boxValue( IRType lhsType, IRExpression root )
  {
    // If it's not a primitive, or the type is void (likely because it's a null expression), just return the expression back out
    if( !lhsType.isPrimitive() || lhsType.isVoid() )
    {
      return root;
    }

    if( lhsType.isBoolean() )
    {
      return callStaticMethod( Boolean.class, "valueOf", new Class[]{boolean.class}, Collections.singletonList(root) );
    }
    if( lhsType.isByte() )
    {
      return callStaticMethod( Byte.class, "valueOf", new Class[]{byte.class}, Collections.singletonList(root) );
    }
    if( lhsType.isChar() )
    {
      return callStaticMethod( AbstractElementTransformer.class, "valueOf", new Class[]{char.class}, Collections.singletonList(root) );
    }
    if( lhsType.isShort() )
    {
      return callStaticMethod( Short.class, "valueOf", new Class[]{short.class}, Collections.singletonList(root) );
    }
    if( lhsType.isInt() )
    {
      return callStaticMethod( Integer.class, "valueOf", new Class[]{int.class}, Collections.singletonList(root) );
    }
    if( lhsType.isLong() )
    {
      return callStaticMethod( Long.class, "valueOf", new Class[]{long.class}, Collections.singletonList(root) );
    }
    if( lhsType.isFloat() )
    {
      return callStaticMethod( Float.class, "valueOf", new Class[]{float.class}, Collections.singletonList(root) );
    }
    if( lhsType.isDouble() )
    {
      return callStaticMethod( Double.class, "valueOf", new Class[]{double.class}, Collections.singletonList(root) );
    }
    throw new IllegalArgumentException("Unexpected type " + lhsType.getName());
  }

  public IRExpression boxValueToType( IType toType, IRExpression root )
  {
    if( toType.isPrimitive() )
    {
      return boxValue( toType, root );
    }

    String VALUE_OF = "valueOf";
    if( toType == JavaTypes.BOOLEAN() )
    {
      return callStaticMethod( Boolean.class, VALUE_OF, new Class[]{boolean.class}, Collections.singletonList(root) );
    }
    if( toType == JavaTypes.BYTE() )
    {
      return callStaticMethod( Byte.class, VALUE_OF, new Class[]{byte.class}, Collections.singletonList(root) );
    }
    if( toType == JavaTypes.CHARACTER() )
    {
      return callStaticMethod( Character.class, VALUE_OF, new Class[]{char.class}, Collections.singletonList(root) );
    }
    if( toType == JavaTypes.SHORT() )
    {
      return callStaticMethod( Short.class, VALUE_OF, new Class[]{short.class}, Collections.singletonList(root) );
    }
    if( toType == JavaTypes.INTEGER() )
    {
      return callStaticMethod( Integer.class, VALUE_OF, new Class[]{int.class}, Collections.singletonList(root) );
    }
    else if( toType == JavaTypes.LONG() )
    {
      return callStaticMethod( Long.class, VALUE_OF, new Class[]{long.class}, Collections.singletonList(root) );
    }
    else if( toType == JavaTypes.FLOAT() )
    {
      return callStaticMethod( Float.class, VALUE_OF, new Class[]{float.class}, Collections.singletonList(root) );
    }
    else if( toType == JavaTypes.DOUBLE() )
    {
      return callStaticMethod( Double.class, VALUE_OF, new Class[]{double.class}, Collections.singletonList(root) );
    }
    else
    {
      // It's not a boxed type, so just return the expression back out
      return root;
    }
  }

  public IRExpression unboxValueToType( IType lhsType, IRExpression expression )
  {
    if( !lhsType.isPrimitive() )
    {
      return checkCast( lhsType, expression );
    }

    if( lhsType == JavaTypes.pBOOLEAN() )
    {
      return callMethod(Boolean.class, "booleanValue", new Class[]{},
              checkCast(Boolean.class, expression), Collections.<IRExpression>emptyList());
    }
    if( lhsType == JavaTypes.pBYTE() )
    {
      return callMethod(Byte.class, "byteValue", new Class[]{},
              checkCast(Byte.class, expression), Collections.<IRExpression>emptyList());
    }
    if( lhsType == JavaTypes.pCHAR() )
    {
      return callMethod(Character.class, "charValue", new Class[]{},
              checkCast(Character.class, expression), Collections.<IRExpression>emptyList());
    }
    if( lhsType == JavaTypes.pSHORT() )
    {
      return callMethod(Short.class, "shortValue", new Class[]{},
              checkCast(Short.class, expression), Collections.<IRExpression>emptyList());
    }
    if( lhsType == JavaTypes.pINT() )
    {
      return callMethod(Integer.class, "intValue", new Class[]{},
              checkCast(Integer.class, expression), Collections.<IRExpression>emptyList());
    }
    else if( lhsType == JavaTypes.pLONG() )
    {
      return callMethod(Long.class, "longValue", new Class[]{},
              checkCast(Long.class, expression), Collections.<IRExpression>emptyList());
    }
    else if( lhsType == JavaTypes.pFLOAT() )
    {
      return callMethod(Float.class, "floatValue", new Class[]{},
              checkCast(Float.class, expression), Collections.<IRExpression>emptyList());
    }
    else if( lhsType == JavaTypes.pDOUBLE() )
    {
      return callMethod(Double.class, "doubleValue", new Class[]{},
              checkCast(Double.class, expression), Collections.<IRExpression>emptyList());
    }
    else if (lhsType == JavaTypes.pVOID() )
    {
      return expression;
    }
    else {
      throw new IllegalArgumentException("Unexpected type " + lhsType.getName());
    }
  }

  public IRExpression unboxValueToType( IRType lhsType, IRExpression expression )
  {
    if( !lhsType.isPrimitive() )
    {
      return buildCast( lhsType, expression );
    }

    if( lhsType.isBoolean() )
    {
      return callMethod(Boolean.class, "booleanValue", new Class[]{},
              checkCast(Boolean.class, expression), Collections.<IRExpression>emptyList());
    }
    if( lhsType.isByte() )
    {
      return callMethod(Byte.class, "byteValue", new Class[]{},
              checkCast(Byte.class, expression), Collections.<IRExpression>emptyList());
    }
    if( lhsType.isChar() )
    {
      return callMethod(Character.class, "charValue", new Class[]{},
              checkCast(Character.class, expression), Collections.<IRExpression>emptyList());
    }
    if( lhsType.isShort() )
    {
      return callMethod(Short.class, "shortValue", new Class[]{},
              checkCast(Short.class, expression), Collections.<IRExpression>emptyList());
    }
    if( lhsType.isInt() )
    {
      return callMethod(Integer.class, "intValue", new Class[]{},
              checkCast(Integer.class, expression), Collections.<IRExpression>emptyList());
    }
    else if( lhsType.isLong() )
    {
      return callMethod(Long.class, "longValue", new Class[]{},
              checkCast(Long.class, expression), Collections.<IRExpression>emptyList());
    }
    else if( lhsType.isFloat() )
    {
      return callMethod(Float.class, "floatValue", new Class[]{},
              checkCast(Float.class, expression), Collections.<IRExpression>emptyList());
    }
    else if( lhsType.isDouble() )
    {
      return callMethod(Double.class, "doubleValue", new Class[]{},
              checkCast(Double.class, expression), Collections.<IRExpression>emptyList());
    }
    else if (lhsType.isVoid() )
    {
      return expression;
    }
    else {
      throw new IllegalArgumentException("Unexpected type " + lhsType.getName());
    }
  }

  public IRExpression unboxValueFromType( IType boxedType, IRExpression root )
  {
    if( boxedType.isPrimitive() )
    {
      return root;
    }

    if( boxedType == JavaTypes.BOOLEAN() )
    {
      return callMethod( Boolean.class, "booleanValue", new Class[0],
              checkCast( Boolean.class, root ), Collections.<IRExpression>emptyList() );
    }
    if( boxedType == JavaTypes.BYTE() )
    {
      return callMethod( Byte.class, "byteValue", new Class[0],
              checkCast( Byte.class, root ), Collections.<IRExpression>emptyList() );
    }
    if( boxedType == JavaTypes.CHARACTER() )
    {
      return callMethod( Character.class, "charValue", new Class[0],
              checkCast( Character.class, root ), Collections.<IRExpression>emptyList() );
    }
    if( boxedType == JavaTypes.SHORT() )
    {
      return callMethod( Short.class, "shortValue", new Class[0],
              checkCast( Short.class, root ), Collections.<IRExpression>emptyList() );
    }
    if( boxedType == JavaTypes.INTEGER() )
    {
      return callMethod( Integer.class, "intValue", new Class[0],
              checkCast( Integer.class, root ), Collections.<IRExpression>emptyList() );
    }
    else if( boxedType == JavaTypes.LONG() )
    {
      return callMethod( Long.class, "longValue", new Class[0],
              checkCast( Long.class, root ), Collections.<IRExpression>emptyList() );
    }
    else if( boxedType == JavaTypes.FLOAT() )
    {
      return callMethod( Float.class, "floatValue", new Class[0],
              checkCast( Float.class, root ), Collections.<IRExpression>emptyList() );
    }
    else if( boxedType == JavaTypes.DOUBLE() )
    {
      return callMethod( Double.class, "doubleValue", new Class[0],
              checkCast( Double.class, root ), Collections.<IRExpression>emptyList() );
    } else {
      throw new IllegalArgumentException("Unexpected type " + boxedType.getName());
    }
  }

  public boolean isProgram( IType type )
  {
    return type instanceof IGosuProgram;
  }

  public static boolean isEnhancementType( IType type )
  {
    return type instanceof IGosuEnhancement;
  }

  public static boolean isBytecodeType( IType type )
  {
    return TypeSystem.isBytecodeType( type );
  }

  public static boolean isBytecodeType( IConstructorInfo ci )
  {
    if( ci instanceof IJavaConstructorInfo )
    {
      return true;
    }
    else if( ci instanceof IGosuConstructorInfo )
    {
      return true;
    }
    return false;
  }

  public static IType[] getTypes( IParameterInfo[] parameters )
  {
    IType[] paramTypes = new IType[parameters.length];
    for( int i = 0; i < parameters.length; i++ )
    {
      paramTypes[i] = parameters[i].getFeatureType();
    }
    return paramTypes;
  }

  protected IRExpression pushString( IExpression expr )
  {
    if( expr.getType() != JavaTypes.STRING() )
    {
      return callMethod( ICoercionManager.class, "makeStringFrom", new Class[]{Object.class},
              callStaticMethod( CommonServices.class, "getCoercionManager", new Class[0], Collections.<IRExpression>emptyList() ),
              Collections.singletonList(ExpressionTransformer.compile( expr, _cc() )));
    }
    else
    {
      return ExpressionTransformer.compile( expr, _cc() );
    }
  }

  protected IRExpression pushParamTypes( IParameterInfo[] parameters )
  {
    if( parameters.length == 0 )
    {
      return pushNull();
    }
    else
    {
      return pushArrayOfTypes( getTypes( parameters ) );
    }
  }

  protected IRExpression pushNull()
  {
    return nullLiteral();
  }

  protected IRExpression pushConstant( Object constant )
  {
    if( constant instanceof Boolean )
    {
      return booleanLiteral( (Boolean)constant );
    }
    else if( constant instanceof Character )
    {
      return charLiteral( (Character)constant );
    }
    else if( constant instanceof Number )
    {
      return numericLiteral( (Number)constant );
    }
    else if( constant instanceof String )
    {
      return stringLiteral( (String)constant );
    }
    else if( constant instanceof Class )
    {
      return classLiteral( (Class)constant );
    }
    else if( constant instanceof IJavaType )
    {
      return classLiteral( ((IJavaType)constant).getBackingClassInfo() );
    }
    else if( constant instanceof IJavaClassInfo )
    {
      return classLiteral( (IJavaClassInfo)constant );
    }
    else if( constant instanceof IRType )
    {
      return classLiteral( (IRType)constant );
    }
    else if( constant == null )
    {
      return pushNull();
    }
    else
    {
      throw new IllegalArgumentException( constant.getClass() + " is not a supported constant type" );
    }
  }

  protected IRExpression convertBoxedNullToPrimitive( IType boxedType )
  {
    if( boxedType == JavaTypes.BOOLEAN() )
    {
      return convertNullToPrimitive( JavaTypes.pBOOLEAN() );
    }
    else if( boxedType == JavaTypes.BYTE() )
    {
      return convertNullToPrimitive( JavaTypes.pBYTE() );
    }
    else if( boxedType == JavaTypes.CHARACTER() )
    {
      return convertNullToPrimitive( JavaTypes.pCHAR() );
    }
    else if( boxedType == JavaTypes.SHORT() )
    {
      return convertNullToPrimitive( JavaTypes.pSHORT() );
    }
    else if( boxedType == JavaTypes.INTEGER() )
    {
      return convertNullToPrimitive( JavaTypes.pINT() );
    }
    else if( boxedType == JavaTypes.LONG() )
    {
      return convertNullToPrimitive( JavaTypes.pLONG() );
    }
    else if( boxedType == JavaTypes.FLOAT() )
    {
      return convertNullToPrimitive( JavaTypes.pFLOAT() );
    }
    else if( boxedType == JavaTypes.DOUBLE() )
    {
      return convertNullToPrimitive( JavaTypes.pDOUBLE() );
    }
    else
    {
      throw new IllegalArgumentException("Unexpected type " + boxedType.getName());
    }
  }

  protected IRExpression convertNullToPrimitive( IType primitive )
  {
    // Convert null value to primitive constant
    if( primitive == JavaTypes.pBOOLEAN() )
    {
      return booleanLiteral(false);
    }
    else if ( primitive == JavaTypes.pBYTE() )
    {
      return numericLiteral( Byte.valueOf( (byte) 0 ) );
    }
    else if ( primitive == JavaTypes.pSHORT() )
    {
      return numericLiteral( Short.valueOf( (short) 0 ) );
    }
    else if (
        primitive == JavaTypes.pCHAR() ||
        primitive == JavaTypes.pINT() )
    {
      return numericLiteral( 0 );
    }
    else if( primitive == JavaTypes.pLONG() )
    {
      return numericLiteral( Long.valueOf(0) );
    }
    else if( primitive == JavaTypes.pFLOAT() )
    {
      return numericLiteral( Float.valueOf(0) );
    }
    else if( primitive == JavaTypes.pDOUBLE() )
    {
      return numericLiteral( Double.valueOf(0) );
    }
    else
    {
      throw new IllegalStateException( "Unexpected type: " + primitive );
    }
  }

  protected IRExpression checkCast( Class cls, IRExpression expression )
  {
    return checkCast( TypeSystem.get( cls ), expression );
  }
  protected IRExpression checkCast( IType type, IRExpression expression )
  {
    return checkCast( type, expression, true );
  }
  protected IRExpression checkCast( IType type, IRExpression expression, boolean bGetConcreteTypeForMetaType )
  {
    if( type instanceof CompoundType )
    {
      CompoundType ct = (CompoundType)type;
      Set<IType> types = ct.getTypes();
      List<IType> ltypes = new ArrayList<IType>( types );
      Collections.sort( ltypes,
                        new Comparator<IType>()
                        {
                          public int compare( IType t1, IType t2 )
                          {
                            return t1.isInterface() ? 0 : 1;
                          }
                        } );
      IRExpression last = expression;
      // Nest the casts, with the concrete type last
      for( IType t : ltypes )
      {
        last = checkCast( t, last, !(t instanceof IMetaType) );
      }
      return last;
    }
    else
    {
      return buildCast( getDescriptor( type, bGetConcreteTypeForMetaType ), expression );
    }
  }

  protected IRAssignmentStatement convertOperandToBig( IType bigType, Class bigClass, IType operandType, IRExpression operand, IRSymbol tempRet ) {
    IRAssignmentStatement tempOperandAssn;
    if( operandType == bigType ) {
      tempOperandAssn = buildAssignment( tempRet, checkCast( bigType, operand ) );
    }
    else {
      IType dimensionType = findDimensionType( operandType );
      if( dimensionType != null ) {
        return convertOperandToBig( bigType, bigClass, dimensionType, callMethod( IDimension.class, "toNumber", new Class[]{}, operand, Collections.<IRExpression>emptyList() ), tempRet );
      }

      if( operandType == JavaTypes.BIG_INTEGER() ) {
        tempOperandAssn = buildAssignment( tempRet, buildNewExpression( BigDecimal.class, new Class[]{BigInteger.class}, Collections.<IRExpression>singletonList( operand ) ) );
      }
      else if( operandType == JavaTypes.BIG_DECIMAL() ) {
        tempOperandAssn = buildAssignment( tempRet, buildMethodCall( BigDecimal.class, "toBigInteger", BigInteger.class, new Class[]{}, operand, Collections.<IRExpression>emptyList() ) );
      }
      else if( StandardCoercionManager.isBoxed( operandType ) ) {
        if( bigClass == BigInteger.class || isBoxedIntType( operandType ) || operandType == JavaTypes.LONG() ) {
          if( operandType == JavaTypes.CHARACTER() ) {
            tempOperandAssn = buildAssignment( tempRet, callStaticMethod( bigClass, "valueOf", new Class[] {long.class},
                                                                      Collections.<IRExpression>singletonList( numberConvert( getDescriptor( char.class ), getDescriptor( long.class ),
                                                                                                                              buildMethodCall( getDescriptor( operandType ), "charValue", false,
                                                                                                                                               getDescriptor( char.class ), Collections.<IRType>emptyList(), operand,
                                                                                                                                               Collections.<IRExpression>emptyList() ) ) ) ) );
          }
          else {
            tempOperandAssn = buildAssignment( tempRet, callStaticMethod( bigClass, "valueOf", new Class[] {long.class},
                                                                      Collections.<IRExpression>singletonList( buildMethodCall( getDescriptor( operandType ), "longValue", false,
                                                                                                                  getDescriptor( long.class ), Collections.<IRType>emptyList(), operand,
                                                                                                                  Collections.<IRExpression>emptyList() ) ) ) );
          }
        }
        else {
          if( operandType == JavaTypes.CHARACTER() ) {
            tempOperandAssn = buildAssignment( tempRet, callStaticMethod( bigClass, "valueOf", new Class[] {double.class},
                                                                      Collections.<IRExpression>singletonList( numberConvert( getDescriptor( char.class ), getDescriptor( double.class ),
                                                                                                                 buildMethodCall( getDescriptor( operandType ), "charValue", false,
                                                                                                                                  getDescriptor( char.class ), Collections.<IRType>emptyList(), operand,
                                                                                                                                  Collections.<IRExpression>emptyList() ) ) ) ) );
          }
          else if( operandType == JavaTypes.FLOAT() ) {
            tempOperandAssn = buildAssignment( tempRet, buildNewExpression( bigClass, new Class[] {String.class},
                                                Collections.<IRExpression>singletonList( buildMethodCall( getDescriptor( Float.class ), "toString", false,
                                                                                                          getDescriptor( String.class ), Collections.<IRType>emptyList(), operand,
                                                                                                          Collections.<IRExpression>emptyList() ) ) ) );
          }
          else {
            tempOperandAssn = buildAssignment( tempRet, callStaticMethod( bigClass, "valueOf", new Class[] {double.class},
                                                                      Collections.<IRExpression>singletonList( buildMethodCall( getDescriptor( operandType ), "doubleValue", false,
                                                                                                                  getDescriptor( double.class ), Collections.<IRType>emptyList(), operand,
                                                                                                                  Collections.<IRExpression>emptyList() ) ) ) );
          }
        }
      }
      else if( operandType.isPrimitive() ) {
        if( bigClass == BigDecimal.class && operandType == JavaTypes.pFLOAT() ) {
          tempOperandAssn = buildAssignment( tempRet,  buildNewExpression( bigClass, new Class[]{String.class}, Collections.singletonList( callStaticMethod( String.class, "valueOf", new Class[]{float.class}, Collections.<IRExpression>singletonList( operand ) ) ) ) );
        }
        else if( bigClass == BigInteger.class || isIntType( operandType ) || operandType == JavaTypes.pLONG() ) {
          tempOperandAssn = buildAssignment( tempRet, callStaticMethod( bigClass, "valueOf", new Class[] {long.class}, Collections.singletonList( numberConvert( operandType, JavaTypes.pLONG(), operand ) ) ) );
        }
        else {
          tempOperandAssn = buildAssignment( tempRet, callStaticMethod( BigDecimal.class, "valueOf", new Class[] {double.class}, Collections.singletonList( numberConvert( operandType, JavaTypes.pDOUBLE(), operand ) ) ) );
        }
      }
      else {
        throw new IllegalStateException( "Unhandled type: " + operandType.getName() );
      }
    }
    return tempOperandAssn;
  }

  protected IRAssignmentStatement convertOperandToPrimitive( IType primitiveType, IType operandType, IRExpression operand, IRSymbol tempRet ) {
    IRAssignmentStatement tempOperandAssn;
    if( operandType == primitiveType ) {
      tempOperandAssn = buildAssignment( tempRet, operand );
    }
    else {
      IType dimensionType = findDimensionType( operandType );
      if( dimensionType != null ) {
        return convertOperandToPrimitive( primitiveType, dimensionType, callMethod( IDimension.class, "toNumber", new Class[]{}, operand, Collections.<IRExpression>emptyList() ), tempRet );
      }

      if( operandType == JavaTypes.BIG_INTEGER() ) {
        tempOperandAssn = buildAssignment( tempRet, numberConvert( JavaTypes.pLONG(), primitiveType,
                                                                   callMethod( BigInteger.class, "longValue", new Class[]{}, operand, Collections.<IRExpression>emptyList() ) ) );
      }
      else if( operandType == JavaTypes.BIG_DECIMAL() ) {
        tempOperandAssn = buildAssignment( tempRet, numberConvert( JavaTypes.pDOUBLE(), primitiveType,
                                                                   callMethod( BigDecimal.class, "doubleValue", new Class[]{}, operand, Collections.<IRExpression>emptyList() ) ) );
      }
      else if( StandardCoercionManager.isBoxed( operandType ) ) {
        if( operandType == JavaTypes.CHARACTER() ) {
          tempOperandAssn = buildAssignment( tempRet, numberConvert( JavaTypes.pCHAR(), primitiveType,
                                                                     callMethod( Character.class, "charValue", new Class[]{}, operand, Collections.<IRExpression>emptyList() ) ) );
        }
        else if( isBoxedIntType( operandType ) || operandType == JavaTypes.LONG() ) {
          tempOperandAssn = buildAssignment( tempRet, numberConvert( JavaTypes.pLONG(), primitiveType,
                                                                     callMethod( Number.class, "longValue", new Class[]{}, operand, Collections.<IRExpression>emptyList() ) ) );
        }
        else if( operandType == JavaTypes.FLOAT() ) {
          tempOperandAssn = buildAssignment( tempRet, numberConvert( JavaTypes.pFLOAT(), primitiveType,
                                                                     callMethod( Float.class, "floatValue", new Class[]{}, operand, Collections.<IRExpression>emptyList() ) ) );
        }
        else if( operandType == JavaTypes.DOUBLE() ) {
          tempOperandAssn = buildAssignment( tempRet, numberConvert( JavaTypes.pDOUBLE(), primitiveType,
                                                                     callMethod( Float.class, "doubleValue", new Class[]{}, operand, Collections.<IRExpression>emptyList() ) ) );
        }
        else {
          throw new IllegalStateException( "Unhandled type: " + operandType.getName() );
        }
      }
      else if( operandType.isPrimitive() ) {
        tempOperandAssn = buildAssignment( tempRet, numberConvert( operandType, primitiveType, operand ) );
      }
      else {
        throw new IllegalStateException( "Unhandled type: " + operandType.getName() );
      }
    }
    return tempOperandAssn;
  }

  protected IRExpression numberConvert( IType from, IType to, IRExpression root )
  {
    if (from.equals(to)) {
      return root;
    } else {
      return new IRPrimitiveTypeConversion( root, getDescriptor( from ), getDescriptor( to ) );
    }
  }

  protected IRExpression numberConvert( IRType from, IRType to, IRExpression root )
  {
    if (from.equals(to)) {
      return root;
    } else {
      return new IRPrimitiveTypeConversion( root, from, to );
    }
  }

  public static boolean isIntType( IType from )
  {
    return from == JavaTypes.pBYTE() ||
           from == JavaTypes.pCHAR() ||
           from == JavaTypes.pSHORT() ||
           from == JavaTypes.pINT();
  }

  protected boolean isPrimitiveNumberType( IType type )
  {
    return type.isPrimitive() && BeanAccess.isNumericType( type );
  }

  public static boolean isBigType( IType type )
  {
    return type == JavaTypes.BIG_DECIMAL() || type == JavaTypes.BIG_INTEGER();
  }

  public static boolean isNonBigBoxedNumberType( IType type )
  {
    return
      type == JavaTypes.BYTE() ||
      type == JavaTypes.CHARACTER() ||
      type == JavaTypes.SHORT() ||
      type == JavaTypes.INTEGER() ||
      type == JavaTypes.LONG() ||
      type == JavaTypes.FLOAT() ||
      type == JavaTypes.DOUBLE();
  }

  public static boolean isBoxedIntType( IType type )
  {
    return
      type == JavaTypes.BYTE() ||
      type == JavaTypes.CHARACTER() ||
      type == JavaTypes.SHORT() ||
      type == JavaTypes.INTEGER();
  }

  public static boolean isNumberType( IType type )
  {
    return type != JavaTypes.pBOOLEAN() &&
           type != JavaTypes.BOOLEAN() &&
           CommonServices.getCoercionManager().isPrimitiveOrBoxed( type );
  }

  // The symbol is considered to be on an enclosing type if the symbol is defined on a class that encloses this
  // class OR is defined on a supertype or interface of an enclosing class
  protected ICompilableTypeInternal isMemberOnEnclosingType(IReducedSymbol symbol)
  {
    if( !_cc().isNonStaticInnerClass() )
    {
      return null;
    }

    // If the symbol is on this class, or any ancestors, it's not enclosed
    //noinspection SuspiciousMethodCalls
    IType symbolClass = maybeUnwrapProxy( symbol.getGosuClass() );
    if( getGosuClass().getAllTypesInHierarchy().contains( symbolClass ) )
    {
      return null;
    }

    ICompilableTypeInternal enclosingClass = _cc().getEnclosingType();

    if( !(TypeLord.getOuterMostEnclosingClass( _cc().getEnclosingType() ) instanceof IGosuEnhancement) &&
        symbolClass instanceof IGosuEnhancement )
    {
      symbolClass = ((IGosuEnhancement)symbolClass).getEnhancedType();
    }

    while( enclosingClass != null )
    {
      //noinspection SuspiciousMethodCalls
      if( enclosingClass.getAllTypesInHierarchy().contains( symbolClass ) )
      {
        return enclosingClass;
      }
      enclosingClass = enclosingClass.getEnclosingType();
    }

    return null;
  }

  // The symbol is considered to be on an enclosing type if the symbol is defined on a class that encloses this
  // class OR is defined on a supertype or interface of an enclosing class
  protected boolean isMemberOnEnhancementOfEnclosingType( AbstractDynamicSymbol symbol )
  {
    if (!_cc().isNonStaticInnerClass()) {
      return false;
    }

    IType enhancement = symbol.getGosuClass();
    if (! ( enhancement instanceof IGosuEnhancement ) ) {
      return false;
    }
    IType enhancedType = ((IGosuEnhancement) enhancement).getEnhancedType();

    // If the symbol is on this class, or any ancestors, it's not enclosed
    //noinspection SuspiciousMethodCalls
    if (getGosuClass().getAllTypesInHierarchy().contains( enhancedType )) {
      return false;
    }

    ICompilableTypeInternal enclosingClass = _cc().getEnclosingType();
    while( enclosingClass != null )
    {
      //noinspection SuspiciousMethodCalls
      if( enclosingClass.getAllTypesInHierarchy().contains( enhancedType ) )
      {
        return true;
      }
      enclosingClass = enclosingClass.getEnclosingType();
    }

    return false;
  }

  private static IType maybeUnwrapProxy( IType type )
  {
    if( type != null && type.isParameterizedType() )
    {
      type = type.getGenericType();
    }
    return type == null ? null : IGosuClass.ProxyUtil.getProxiedType( type );
  }

  protected IRExpression pushThis()
  {
    String symbolName = (isCompilingEnhancement() ? GosuClassTransformer.ENHANCEMENT_THIS_REF : Keyword.KW_this.getName() );
    return identifier( _cc.getSymbol( symbolName ) );
  }

  protected IRExpression pushThisOrOuter( IType currentType )
  {
    if( currentType.isAssignableFrom( getGosuClass() ) )
    {
      return pushThis();
    }
    else if( currentType instanceof IGosuEnhancement &&
             currentType.isAssignableFrom( ((IGosuEnhancement)currentType).getEnhancedType() ) )
    {
      return pushThis();
    }
    else
    {
      return pushOuter( currentType );
    }
  }

  protected IRExpression pushOuter()
  {
    if( _cc().getCurrentFunction() != null && _cc().getCurrentFunction().isConstructor() )
    {
      return identifier( _cc().getSymbol( _cc().getOuterThisParamName() ) );
    }
    else
    {
      return getInstanceField( getGosuClass(), _cc().getOuterThisFieldName(), getDescriptor( getRuntimeEnclosingType( getGosuClass() ) ), AccessibilityUtil.forOuter(), pushThis() );
    }
  }

  protected IRExpression pushOuter( IType outerTarget )
  {
    return pushOuter( maybeUnwrapProxy( outerTarget ), maybeUnwrapProxy( _cc().getEnclosingType() ), pushOuter() );
  }

  protected IRExpression pushOuter( IType outerTarget, IType currentOuter, IRExpression root )
  {
    IRExpression result = root;
    while( !outerTarget.isAssignableFrom( currentOuter ) )
    {
      IRMethod irMethod = IRMethodFactory.createIRMethod( currentOuter, OUTER_ACCESS, getRuntimeEnclosingType( currentOuter ), new IType[]{currentOuter}, AccessibilityUtil.forOuter(), true);
      result = callMethod( irMethod, null, Collections.singletonList(result));
      currentOuter = maybeUnwrapProxy( currentOuter.getEnclosingType() );
    }
    return result;
  }

  protected IRExpression getInstanceField( IType owner, String strField, IRType fieldType, IRelativeTypeInfo.Accessibility accessibility, IRExpression root )
  {
    return getField( owner,
              strField,
              fieldType,
              accessibility,
              root);
  }

  protected IRExpression getStaticField( IType owner, String strField, IRType fieldType, IRelativeTypeInfo.Accessibility accessibility )
  {
    return getField( owner,
              strField,
              fieldType,
              accessibility,
              null);
  }

  protected IRExpression getField( IRProperty field, IRExpression root )
  {
    return getField( field.getOwningIType(),
              field.getName(),
              field.getType(),
              field.getAccessibility(),
              root);
  }

  protected IRStatement setField( IRProperty field, IRExpression root, IRExpression value )
  {
    return setField( field.getOwningIType(),
              field.getName(),
              field.getType(),
              field.getAccessibility(),
              root,
              value);
  }

  protected IRStatement setInstanceField( IType owner, String strField, IRType fieldType, IRelativeTypeInfo.Accessibility accessibility,
                                          IRExpression root, IRExpression value)
  {
    return setField( owner,
              strField,
              fieldType,
              accessibility,
              root,
              value);
  }

  protected IRStatement setStaticField( IType owner, String strField, IRType fieldType, IRelativeTypeInfo.Accessibility accessibility,
                                        IRExpression value)
  {
    return setField( owner,
              strField,
              fieldType,
              accessibility,
              null,
              value);
  }

  private IRExpression getField( IType owner, String strField, IRType fieldType, IRelativeTypeInfo.Accessibility accessibility, IRExpression root )
  {
    IRType rootType = root == null ? null : root.getType();
    if( rootType == null )
    {
      rootType = getDescriptor( owner );
    }
    if( _cc().shouldUseReflection( owner, rootType, accessibility ) )
    {
      // Can't gen bytecode for protected call otherwise verify error
      return getFieldReflectively( owner, strField, fieldType, root );
    }

    return buildFieldGet( getDescriptor( owner ), strField, fieldType, root );
  }

  protected boolean avoidVerifyError( IType owner, IRType rootType, IRelativeTypeInfo.Accessibility accessibility )
  {
    return _cc().isIllegalProtectedCall( owner, rootType, accessibility ) ||
        AccessibilityUtil.forType( owner ) == IRelativeTypeInfo.Accessibility.INTERNAL;
  }

  private IRExpression getFieldReflectively( IType owner, String strField, IRType fieldType, IRExpression root )
  {
    // Call getDeclaredField using the owner's Class and the field name as arguments
    IRExpression getDeclaredFieldCall = buildMethodCall(AbstractElementTransformer.class, "getDeclaredField", Field.class, new Class[]{Class.class, String.class},
            null,
            exprList( classLiteral( getDescriptor( owner ) ),
                      stringLiteral( strField ) ) );
    // Call "get" on the result of that call, passing in the root expression or the "null" constant if there is none
    IRExpression getCall = buildMethodCall( Field.class, "get", Object.class, new Class[]{Object.class},
            getDeclaredFieldCall,
            exprList( (root == null ? pushNull() : root) ) );
    // Unbox it on the way out if needed
    return unboxValueToType( fieldType, getCall );
  }

  protected IRStatement setField( IType owner, String strField, IRType fieldType, IRelativeTypeInfo.Accessibility accessibility,
                                IRExpression root, IRExpression value )
  {
    IRType rootType = root == null ? null : root.getType();
    if( rootType == null )
    {
      rootType = getDescriptor( owner );
    }
    if( _cc().shouldUseReflection( owner, rootType, accessibility ) )
    {
      return setFieldReflectively( owner, strField, root, value );
    }

    return buildFieldSet( getDescriptor( owner ), strField, fieldType, root, value );
  }

  private IRStatement setFieldReflectively( IType owner, String strField,
                                            IRExpression root, IRExpression value)
  {
    // Call getDeclaredField using the owner's Class and the field name as arguments
    IRExpression getDeclaredFieldCall = buildMethodCall(AbstractElementTransformer.class, "getDeclaredField", Field.class, new Class[]{Class.class, String.class},
            null,
            exprList( classLiteral( getDescriptor( owner ) ),
                      stringLiteral( strField ) ) );
    // Call "set" on the result of that call, passing in the root expression or the "null" constant if there is none and the value
    IRExpression setCall = buildMethodCall( Field.class, "set", void.class, new Class[]{Object.class, Object.class},
            getDeclaredFieldCall,
            exprList( (root == null ? pushNull() : root),
                      boxValue( value.getType(), value ) ) );

    return new IRMethodCallStatement( setCall );
  }

  protected static boolean isEvalProgram( IGosuClassInternal gsClass )
  {
    return gsClass instanceof IGosuProgram &&
           gsClass.isAnonymous();
  }

  public static Field getDeclaredField( Class cls, String strName ) {
    Field f = getDeclaredFieldImpl(cls, strName);
    if( f == null )
    {
      throw new IllegalStateException( "Failed to find field: " + strName + " starting from class " + cls);
    }
    return f;
  }

  public static Field getDeclaredFieldImpl( Class cls, String strName )
  {
    if( cls == null )
    {
      return null;
    }
    for( Field f : cls.getDeclaredFields() )
    {
      if( f.getName().equals( strName ) )
      {
        f.setAccessible( true );
        return f;
      }
    }

    // If we didn't find the method, recurse up through the superclass, then any implemented interfaces,
    // and lastly check any enclosing classes
    Field f = getDeclaredFieldImpl( cls.getSuperclass(), strName );
    if (f != null) {
      return f;
    }

    for (Class iface : cls.getInterfaces()) {
      f = getDeclaredFieldImpl( iface, strName );
      if (f != null) {
        return f;
      }
    }

    return null;
  }

  public static Method getDeclaredMethod( Class cls, String strName, Class... params ) {
    Method m = getDeclaredMethodImpl(cls, strName, params);
    if( m == null )
    {
      throw new IllegalStateException( "Failed to find method: " + strName  + "(" + Arrays.asList(params) + ") starting from class " + cls);
    }
    return m;
  }

  public static IJavaClassMethod getDeclaredMethod(IJavaClassInfo cls, String strName, Class... params) {
    IJavaClassMethod m = getDeclaredMethodImpl(cls, strName, params);
    if( m == null )
    {
      throw new IllegalStateException( "Failed to find method: " + strName  + "(" + Arrays.asList(params) + ") starting from class " + cls);
    }
    return m;
  }

  private static Method getDeclaredMethodImpl( Class cls, String strName, Class... params )
  {
    if( cls == null )
    {
      return null;
    }
    for( Method m : NewIntrospector.getDeclaredMethods( cls ) )
    {
      if( m.getName().equals( strName ) )
      {
        Class<?>[] mParams = m.getParameterTypes();
        if( mParams.length == params.length )
        {
          boolean bFound = true;
          for( int i = 0; i < mParams.length; i++ )
          {
            if( !mParams[i].getName().equals( params[i].getName() ) )
            {
              bFound = false;
              break;
            }
          }
          if( bFound )
          {
            m.setAccessible( true );
            return m;
          }
        }
      }
    }

    // If we didn't find the method, recurse up through the superclass, then any implemented interfaces,
    // and lastly check any enclosing classes
    Method m = getDeclaredMethodImpl( cls.getSuperclass(), strName, params );
    if (m != null) {
      return m;
    }

    for (Class iface : cls.getInterfaces()) {
      m = getDeclaredMethodImpl( iface, strName, params );
      if (m != null) {
        return m;
      }
    }

    return null;
  }

  private static IJavaClassMethod getDeclaredMethodImpl(IJavaClassInfo cls, String strName, Class... params)
  {
    if( cls == null )
    {
      return null;
    }
    for( IJavaClassMethod m : cls.getDeclaredMethods() )
    {
      if( m.getName().equals( strName ) )
      {
        IJavaClassInfo[] mParams = m.getParameterTypes();
        if( mParams.length == params.length )
        {
          boolean bFound = true;
          for( int i = 0; i < mParams.length; i++ )
          {
            if( !mParams[i].getName().equals( params[i].getName() ) )
            {
              bFound = false;
              break;
            }
          }
          if( bFound )
          {
            return m;
          }
        }
      }
    }

    // If we didn't find the method, recurse up through the superclass, then any implemented interfaces,
    // and lastly check any enclosing classes
    IJavaClassMethod m = getDeclaredMethodImpl(cls.getSuperclass(), strName, params);
    if (m != null) {
      return m;
    }

    for (IJavaClassInfo iface : cls.getInterfaces()) {
      m = getDeclaredMethodImpl( iface, strName, params );
      if (m != null) {
        return m;
      }
    }

    return null;
  }

  protected IRExpression newArray( IRType atomicType, IRExpression sizeExpression )
  {
    return new IRNewArrayExpression( atomicType, sizeExpression );
  }
//
//  private String getTypeForNewArray( IType atomicType )
//  {
//    return atomicType.isArray()
//           ? getDescriptor( atomicType )
//           : getSlashName( atomicType );
//  }
//
  protected IRExpression makeArrayViaTypeInfo( IType atomicType, List<Expression> valueExpressions )
  {
    _cc().pushScope( false );
    try
    {
      // atomicType
      // .makeArrayInstance( size )

      pushType( atomicType );
      pushConstant( valueExpressions.size() );
      IRExpression arrayCreation = callMethod( IType.class, "makeArrayInstance", new Class[]{int.class},
              pushType( atomicType ),
              exprList( pushConstant( valueExpressions.size() ) ) );

      if (valueExpressions.isEmpty()) {
        // If there are no values, just cast the array after creating it
        return checkCast( atomicType.getArrayType(), arrayCreation );
      } else {
        // If it needs to be initialized, we trap the creation in a temp variable, then assign to it,
        // then load it and cast it at the end
        IRSymbol temp = _cc().makeAndIndexTempSymbol(IRTypeConstants.OBJECT());
        List<IRElement> statements = new ArrayList<IRElement>();
        statements.add( buildAssignment( temp, arrayCreation ) );

        // For each value, we need to call setArrayComponent on the type, using this value
        for( int i = 0; i < valueExpressions.size(); i++ )
        {
          Expression expression = valueExpressions.get( i );
          IRExpression call = callMethod( IType.class, "setArrayComponent", new Class[]{Object.class, int.class, Object.class},
                  pushType( atomicType ),
                  exprList( identifier( temp ),
                            numericLiteral( i ),
                            boxValue( expression.getType(), ExpressionTransformer.compile( expression, _cc() ) ) ) );
          statements.add(new IRMethodCallStatement( call ) );
        }
        statements.add( checkCast( atomicType.getArrayType(), identifier ( temp ) ) );
        return new IRCompositeExpression( statements );
      }
    }
    finally
    {
      _cc().popScope();
    }
  }

  protected IRExpression makeEmptyArrayViaTypeInfo( IType atomicType, List<Expression> sizeExpressions )
  {
    _cc().pushScope( false );
    try
    {
      // (atomicType[]) AbstractElementTransformer.initMultiArray(atomicType, atomicType.makeArrayInstance( sizeExpressions.get(0) ),
      //                                           sizeExpressions.size(),
      //                                           sizeExpressions)

      IRExpression arrayCreation = callMethod( IType.class, "makeArrayInstance",  new Class[]{int.class},
              pushType( atomicType ),
              exprList( ExpressionTransformer.compile( sizeExpressions.get( 0 ), _cc() ) ) );

      List<IRExpression> sizes = new ArrayList<IRExpression>();
      sizes.add(numericLiteral(0)); // First one doesn't matter
      for (int i = 1; i < sizeExpressions.size(); i++) {
        sizes.add( ExpressionTransformer.compile( sizeExpressions.get(i), _cc() ) );
      }
      IRExpression sizeArrays = buildInitializedArray(IRTypeConstants.pINT(), sizes);
      IRExpression initCall = callStaticMethod( getClass(), "initMultiArray", new Class[]{IType.class, Object.class, int.class, int[].class},
              exprList( pushType( atomicType ),
                        arrayCreation,
                        numericLiteral( 1 ),
                        sizeArrays) );

      return checkCast( atomicType.getArrayType(), initCall );
    }
    finally
    {
      _cc().popScope();
    }
  }

  public static Object initMultiArray( IType componentType, Object instance, int iDimension, int[] sizes )
  {
    if( sizes.length <= iDimension )
    {
      return instance;
    }

    int iLength = componentType.getArrayLength( instance );
    componentType = componentType.getComponentType();
    for( int i = 0; i < iLength; i++ )
    {
      Object component = componentType.makeArrayInstance( sizes[iDimension] );
      initMultiArray( componentType, component, iDimension + 1, sizes );
      componentType.setArrayComponent( instance, i, component );
    }
    return instance;
  }

  public IRExpression pushRuntimeTypeOfTypeVar( IType type )
  {
    IType rtType = type;

    if( rtType instanceof TypeVariableType )
    {
      return buildCast( getDescriptor( IType.class ), buildMethodCall( LazyTypeResolver.class, "get", Object.class, new Class[0], getRuntimeTypeParameter( (TypeVariableType)rtType ), Collections.<IRExpression>emptyList() ) );
    }
    else if( rtType instanceof TypeVariableArrayType )
    {
      // TypeSystem.getByFullName( runtimeType.getName().concat(brackets) )
      StringBuilder brackets = new StringBuilder();
      while( rtType.isArray() )
      {
        brackets.append( '[' );
        brackets.append( ']' );
        rtType = rtType.getComponentType();
      }
      IRExpression typeNameExpression = callMethod( String.class, "concat", new Class[]{String.class},
        callMethod( IType.class, "getName", new Class[0],
                    pushRuntimeTypeOfTypeVar( rtType ),
                    Collections.<IRExpression>emptyList() ),
        Collections.singletonList( pushConstant( brackets.toString() ) ) );
      return callStaticMethod( TypeSystem.class, "getByFullName", new Class[]{String.class}, Collections.singletonList( typeNameExpression ) );
    }
    else
    {
      throw new IllegalArgumentException( "Only type variable types allowed in this method" );
    }
  }

  private IRExpression getRuntimeTypeParameter( TypeVariableType type )
  {

    IRSymbol iTypeParamSymbol = _cc().getTypeParamIndex( type );
    if( iTypeParamSymbol != null )
    {
      // The type param is passed as an arg to the enclosing function/constructor, pass directly from local var
      return identifier( iTypeParamSymbol );
    }

    if( type.isFunctionStatement() )
    {
      // The type-var is defined on a function e.g., function foo<F>()
      // In this case the type parameter is passed into the function directly, before the proper args.

      IFunctionType funcType = (IFunctionType)type.getEnclosingType();
      if( equivalentTypes( funcType.getScriptPart().getContainingType(), getGosuClass() ) )
      {
        IGenericTypeVariable[] genTypeVars = funcType.getGenericTypeVariables();
        for( int i = 0; i < genTypeVars.length; i++ )
        {
          IGenericTypeVariable gv = genTypeVars[i];
          if( gv.getName().equals( type.getName() ) )
          {
            return identifier( _cc().getSymbol( getTypeVarParamName( gv ) ) );
          }
        }
      }

      // May fail to find if the type var does not belong to this immediate function (see Case #2 below)
    }

    // 1) The type var is defined on 'this' e.g., class Foo<T>.
    // In this case the type parameter is stored on the class e.g., typeparam$T
    // ...or...
    // 2) The type var is on a function *but* the function encloses
    // an aonymous class which references the type var.
    // In this case the type parameter is stored on the anonymous class just
    // like a class type var e.g., typeparam$E

    // Case #.5 - enhancement type parameters
    IType enclosingType = type.getEnclosingType();
    // The type-var is defined on an enhancement
    // In this case the type parameter is passed into the function directly, before the function type args.
    if( enclosingType instanceof IGosuEnhancement && equivalentTypes( enclosingType, _cc().getGosuClass() ) )
    {
      IGenericTypeVariable[] typeVariables = enclosingType.getGenericTypeVariables();
      for( int i = 0; i < typeVariables.length; i++ )
      {
        ITypeVariableType variableType = typeVariables[i].getTypeVariableDefinition().getType();
        if( type.equals( variableType ) )
        {
          return identifier( _cc().getSymbol( getTypeVarParamName( typeVariables[i] ) ) );
        }
      }
    }

    //
    // Case #1
    //
    String strTypeVarField = TYPE_PARAM_PREFIX + type.getRelativeName();
    for( IGenericTypeVariable gv : getGosuClass().getGenericTypeVariables() )
    {
      if( gv.getName().equals( type.getName() ) )
      {
        return getInstanceField( getGosuClass(), strTypeVarField, getDescriptor( LazyTypeResolver.class ), AccessibilityUtil.forTypeParameter(), pushThis() );
      }
    }

    //
    // Case #2
    //
    IRExpression fromEnclosingFunction = maybeGetFromEnclosingFunction( getGosuClass(), type, strTypeVarField );
    if( fromEnclosingFunction != null )
    {
      return fromEnclosingFunction;
    }

    //
    // Now repeat #1 and #2 for each outer/enclosing class
    //
    for( ICompilableType gsClass = getGosuClass().getEnclosingType();
         gsClass != null;
         gsClass = gsClass.getEnclosingType() )
    {
      //
      // Case #1
      //
      for( IGenericTypeVariable gv : gsClass.getGenericTypeVariables() )
      {
        if( gv.getName().equals( type.getName() ) )
        {
          return getInstanceField( gsClass, strTypeVarField, getDescriptor( LazyTypeResolver.class ), AccessibilityUtil.forTypeParameter(), pushOuter( gsClass ) );
        }
      }

      //
      // Case #2
      //
      fromEnclosingFunction = maybeGetFromEnclosingFunction( gsClass, type, strTypeVarField );
      if( fromEnclosingFunction != null )
      {
        return fromEnclosingFunction;
      }
    }

    // lazy resolves using fqn + module name lookup
    IType boundingType = type.getBoundingType();
    if( boundingType instanceof TypeVariableType )
    {
      return getRuntimeTypeParameter( (TypeVariableType)boundingType );
    }
    return pushLazyTypeByFqn( boundingType );
  }

  private IRExpression pushLazyTypeByFqn( IType boundingType )
  {
    boundingType = TypeLord.getPureGenericType( boundingType );
    IModule module = getModule( boundingType );
    return buildNewExpression( SimpleTypeLazyTypeResolver.class, new Class[]{String.class, String.class},
                               Arrays.<IRExpression>asList( pushConstant( boundingType.getName() ), module == null ? pushNull() : pushConstant( module.getName() ) ) );
  }

  private boolean equivalentTypes( IType type1, IType type2 )
  {
    return TypeLord.getPureGenericType( type1 ) == TypeLord.getPureGenericType( type2 );
  }

  /**
   * An anonymous class enclosed in a generic function has as a synthetic field the type parameter[s] from the function.
   */
  private IRExpression maybeGetFromEnclosingFunction( ICompilableType gsClass, TypeVariableType type, String strTypeVarField )
  {
    if( gsClass.isAnonymous() )
    {
      IDynamicFunctionSymbol dfs = getEnclosingDFS( gsClass );
      if( dfs != null && dfs != getEnclosingDFS( gsClass.getEnclosingType() ) )
      {
        for( IGenericTypeVariable gv : getTypeVarsForDFS( dfs ) )
        {
          if( gv.getName().equals( type.getName() ) )
          {
            return getInstanceField( gsClass, strTypeVarField, getDescriptor( LazyTypeResolver.class ), AccessibilityUtil.forTypeParameter(), pushThisOrOuter( gsClass ) );
          }
        }
      }
    }
    return null;
  }

  public static IDynamicFunctionSymbol getEnclosingDFS( ICompilableType gsClass )
  {
    IParsedElement pe;
    if( gsClass instanceof IGosuProgram )
    {
      pe = ((IGosuProgram)gsClass).getEnclosingEvalExpression();
    }
    else if( gsClass instanceof IBlockClass )
    {
      pe = ((IBlockClass)gsClass).getBlock();
    }
    else
    {
      pe = gsClass.getClassStatement();
    }
    // It can be null e.g., if the new expr is a field initializer (not defined in a function)
    if( pe == null || pe.getLocation() == null || pe.getLocation().getEnclosingFunctionStatement() == null )
    {
      return null;
    }
    else
    {
      return pe.getLocation().getEnclosingFunctionStatement().getDynamicFunctionSymbol();
    }
  }

  public static List<IGenericTypeVariable> getTypeVarsForDFS( IDynamicFunctionSymbol dfs )
  {
    ArrayList<IGenericTypeVariable> typeVars = new ArrayList<IGenericTypeVariable>();
    if( !dfs.isStatic() && isEnhancementType( dfs.getGosuClass() ) )
    {
      typeVars.addAll( Arrays.asList( getTypeVarsForEnhancement( dfs ) ) );
    }
    if( dfs.getType().isGenericType() )
    {
      typeVars.addAll( Arrays.asList( dfs.getType().getGenericTypeVariables() ) );
    }
    else if( dfs.isConstructor() )
    {
      IType declaringType = TypeLord.getPureGenericType( dfs.getDeclaringTypeInfo().getOwnersType() );
      if( declaringType.isGenericType() )
      {
        typeVars.addAll( Arrays.asList( declaringType.getGenericTypeVariables() ) );
      }
    }
    return typeVars;
  }

  private static IGenericTypeVariable[] getTypeVarsForEnhancement( IDynamicFunctionSymbol dfs )
  {
    IGosuClass aClass = dfs.getGosuClass();
    if( aClass.isParameterizedType() )
    {
      aClass = (IGosuClass)aClass.getGenericType();
    }
    return aClass.getGenericTypeVariables();
  }

  /**
   * Parameters are order like so:
   * ctor( [OuterThis,] [This,] [CapturedSymbols,] [TypeParams,] params )
   */
  protected IRType[] getConstructorParamTypes( IType[] declaredParams, int iTypeParams, IType type )
  {
    List<IRType> params = new ArrayList<IRType>();

    //
    // Insert outer 'this'
    //
    if( isNonStaticInnerClass( type ) )
    {
      params.add( getDescriptor( getRuntimeEnclosingType( type ) ) );
    }

    //
    // Insert captured symbols
    //
    if( type instanceof IGosuClassInternal && type.isValid() ) //&& ((IGosuClassInternal)type).isAnonymous() )
    {
      Map<String, ICapturedSymbol> capturedSymbols = ((IGosuClassInternal)type).getCapturedSymbols();
      if( capturedSymbols != null )
      {
        for( ICapturedSymbol sym : capturedSymbols.values() )
        {
          params.add( getDescriptor( sym.getType().getArrayType() ) );
        }
      }
    }

    // The external symbols are always treated as captured
    if (requiresExternalSymbolCapture( type ) ) {
      params.add( getDescriptor( IExternalSymbolMap.class ) );
    }

    //
    // Insert type-parameter types
    //
    if( iTypeParams > 0 ) {
      for( int i = 0; i < iTypeParams; i++ ) {
        params.add( getDescriptor( LazyTypeResolver.class ) );
      }
    }

    //
    // Enums have name and ordinal arguments implicitly added to their constructors
    //
    if( type.isEnum() ) {
      params.add( IRTypeConstants.STRING() );
      params.add( IRTypeConstants.pINT() );
    }

    //
    // Add declared parameters
    //
    for( IType declaredParam : declaredParams ) {
      params.add( getDescriptor( declaredParam ) );
    }

    return params.toArray( new IRType[params.size()] );
  }

  protected int pushTypeParametersForConstructor( IExpression expr, IType type, List<IRExpression> args, boolean bSuperCall )
  {
    if( !(type instanceof IGosuClassInternal) )
    {
      return 0;
    }

    int iCount = 0;
    if( type.isParameterizedType() )
    {
      for( IType typeParam : type.getTypeParameters() )
      {
        args.add( bSuperCall
                  ? pushLazyType( typeParam, TypeLord.getPureGenericType( getGosuClass() ).getGenericTypeVariables() )
                  : pushLazyType( typeParam ) );
        iCount++;
      }
    }
    else if( type.isGenericType() ) {
      // We should only be here if it's a this() call, so we grab the type parameters out of the current scope,
      // i.e. just pass through whatever was passed to this constructor
      for ( IGenericTypeVariable typeVariable : type.getGenericTypeVariables() ) {
        args.add( identifier( new IRSymbol( getTypeVarParamName( typeVariable ), getDescriptor( LazyTypeResolver.class ), false) ) );
        iCount++;
      }
    }

    iCount = pushEnclosingFunctionTypeParams( expr, type, iCount, args );

    return iCount;
  }

  protected void pushEnumSuperConstructorArguments( List<IRExpression> args ) {
    if(_cc().compilingEnum()) {
      // The name and ordinal arguments are always the next ones on the stack after the "this" pointer,
      // since generics can't be non-static inner classes or generified
      args.add( identifier( _cc().getSymbol( ENUM_PARAM_PREFIX + "name" ) ) );
      args.add( identifier( _cc().getSymbol( ENUM_PARAM_PREFIX + "ordinal" ) ) );
    }
  }

  private int pushEnclosingFunctionTypeParams( IParsedElement pe, IType type, int iCount, List<IRExpression> args )
  {
    ICompilableTypeInternal gsClass = (ICompilableTypeInternal)type;
    while( gsClass.isAnonymous() )
    {
      // If the class is anonymous and it's constructed within a generic method,
      // it may need the type params of the of method.

      if( pe != null && pe.getLocation() != null )
      {
        IFunctionStatement funcStmt = pe.getLocation().getEnclosingFunctionStatement();
        if( funcStmt != null ) // can be null e.g., anonymous classes can be constructed as field initializers
        {
          IDynamicFunctionSymbol dfs = funcStmt.getDynamicFunctionSymbol();
          List<IGenericTypeVariable> genTypeVars = getTypeVarsForDFS( dfs );

          // unlike "normal" anonymous classes block classes can be nested under the same
          // function statement, so we need to roll out to the outermost one under this dfs
          while( getEnclosingDFS( gsClass.getEnclosingType() ) == dfs )
          {
            gsClass = gsClass.getEnclosingType();
          }

          for( int i = 0; i < genTypeVars.size(); i++ )
          {
            if( gsClass == type )
            {
              args.add( identifier( _cc().getSymbol( getTypeVarParamName( genTypeVars.get(i) ) ) ) );
            }
            else
            {
              args.add( getInstanceField( gsClass, TYPE_PARAM_PREFIX + genTypeVars.get( i ).getName(),
                                          getDescriptor( LazyTypeResolver.class ), AccessibilityUtil.forTypeParameter(),
                        pushThisOrOuter( gsClass ) ) );
            }
            iCount++;
          }
        }
      }
      gsClass = gsClass.getEnclosingType();
      if( gsClass.isAnonymous() )
      {
        pe = gsClass instanceof IGosuProgram
             ? ((IGosuProgram)gsClass).getEnclosingEvalExpression()
             : gsClass.getClassStatement();
      }
    }
    return iCount;
  }

  protected IRAssignmentStatement initLocalVarWithDefault( Symbol varSym )
  {
    return initLocalVar( varSym, getDefaultConstIns( varSym.getType() ) );
  }

  protected IRAssignmentStatement initLocalVar( Symbol varSym, IRExpression initialValue )
  {
    IRSymbol symbol = makeIRSymbol( varSym );

    IRExpression actualValue;
    if( varSym.isValueBoxed() )
    {
      // We need to get the component because the type of the boxed symbol will already be the array type,
      // i.e., int[] instead of just int
      actualValue = buildInitializedArray( symbol.getType().getComponentType(), exprList( initialValue ) );
    }
    else
    {
      actualValue = initialValue;
    }
    return buildAssignment( symbol, actualValue );
  }

  protected IRSymbol makeIRSymbol( Symbol varSym )
  {
    IType type = varSym.getType();
    if( varSym.isValueBoxed() )
    {
      type = type.getArrayType();
    }
    // This is pretty brutal.  It's here because ForEachStatementTransformer takes in a
    // Symbol to its helper methods, and sometimes we really want that to be a temp symbol
    IRSymbol symbol = new IRSymbol( varSym.getName(), getDescriptor( type ), varSym.getName().startsWith("*temp") );
    _cc().putSymbol( symbol );
    return symbol;
  }

  protected void pushCapturedSymbols( IType type, List<IRExpression> args, boolean ignoreExternalSymbols )
  {
    if( type instanceof IGosuClassInternal && type.isValid() ) //&& ((IGosuClassInternal)type).isAnonymous() )
    {
      Map<String, ICapturedSymbol> capturedSymbols = ((IGosuClassInternal)type).getCapturedSymbols();
      if( capturedSymbols != null )
      {
        for( ICapturedSymbol sym : capturedSymbols.values() )
        {
          if( isCapturedOnEnclosingAnonymousClass( sym, (IGosuClassInternal)type ) )
          {
            args.add( getInstanceField( getGosuClass(), CAPTURED_VAR_PREFIX + sym.getName(), getDescriptor( sym.getType().getArrayType() ), AccessibilityUtil.forSymbol((IReducedSymbol) sym),
                    pushThis()));
          }
          else
          {
            args.add( identifier( _cc().getSymbol(sym.getName() ) ) );
          }
        }
      }
    }

    if ( !ignoreExternalSymbols && requiresExternalSymbolCapture( type ) /* && !inStaticContext() */ ) {
      args.add( pushExternalSymbolsMap() );
    }
  }

  private boolean isCapturedOnEnclosingAnonymousClass( ICapturedSymbol sym, IGosuClassInternal gsClass )
  {
    ICompilableTypeInternal enclosingType = gsClass.getEnclosingType();
    if( enclosingType.isAnonymous() )
    {
      return enclosingType.getCapturedSymbols().containsKey( sym.getName() );
    }
    return false;
  }


  public IRExpression collectArgsIntoObjArray( List<IRExpression> args )
  {
    List<IRExpression> values = new ArrayList<IRExpression>();
    if( args != null )
    {
      for( IRExpression arg : args )
      {
        if( arg.getType().isPrimitive() )
        {
          arg = boxValue( arg.getType(), arg );
        }
        values.add( arg );
      }
    }
    return buildInitializedArray(IRTypeConstants.OBJECT(), values);
  }

  protected IGosuVarPropertyInfo getActualPropertyInfo(IPropertyInfo pi)
  {
    if( pi instanceof PropertyInfoDelegate )
    {
      pi = ((PropertyInfoDelegate) pi).getDelegatePI();
    }
    return (IGosuVarPropertyInfo)pi;
  }

  protected IRExpression castResultingTypeIfNecessary( IRType expectedType, IRType actualReturnType, IRExpression root ) {
    if (!expectedType.isVoid() && !expectedType.isPrimitive() && !expectedType.isAssignableFrom(actualReturnType)) {
      return buildCast( expectedType, root );
    } else {
      return root;
    }
  }

  /**
   * @param type
   * @return the actual runtime enclosing type of this type (handles the case of enhancements, when
   * the "enclosing type" at runtime will be the enhanced object, rather than the acutal enclosing type)
   */
  public static IType getRuntimeEnclosingType( IType type )
  {
    IType enclosingType = maybeUnwrapProxy( type.getEnclosingType() );
    if( enclosingType instanceof IGosuEnhancement )
    {
      IGosuEnhancement enhancement = (IGosuEnhancement)enclosingType;
      enclosingType = enhancement.getEnhancedType();
    }
    return enclosingType;
  }

  public IType maybeUnwrapMetaType( IType rootType )
  {
    if( rootType instanceof IMetaType )
    {
      rootType = ((IMetaType)rootType).getType();
    }
    return rootType;
  }

  @Deprecated // Use the IJavaClassInfo version -- we should not be loading classes during compilation
  protected IRExpression classLiteral( Class value )
  {
    return classLiteral( getDescriptor( value ) );
  }

  protected IRExpression classLiteral( IJavaClassInfo value ) {
    return classLiteral( JavaClassIRType.get( value ) );
  }

  protected IRExpression classLiteral( IRType value ) {
    IType type;
    if (value instanceof JavaClassIRType) {
      type = ((JavaClassIRType) value).getType();
    } else if (value instanceof GosuClassIRType) {
      type = ((GosuClassIRType) value).getType();
    } else {
      throw new RuntimeException("Unsupported IRType " + value.getClass());
    }
    return pushClassLiteral( value, type );
  }

  private IRExpression pushClassLiteral( IRType value, IType type ) {
    //## todo: this should be fixed now that Gosu's loader is the app loader i.e., we can probably just use the IRClassLiteral

    // Unfortunately, it's possible for a class literal to be illegal in a given context:  if that literal refers to an
    // internal or private type, for example, and classloader issues result in the caller being in a different package/classloader
    // at runtime, the type needs to be looked up reflectively instead.  For purposes of this method call, the feature itself is public
    if( type != null && RequiresReflectionDeterminer.shouldUseReflection( type, _cc.getGosuClass(), null, IRelativeTypeInfo.Accessibility.PUBLIC ) ) {
      return callMethod( GosuRuntimeMethods.class, "lookUpClass", new Class[]{String.class}, null, exprList( stringLiteral( value.getDescriptor() ) ) );
    } else {
      return new IRClassLiteral( value );
    }
  }

  public static ICustomExpressionRuntime getCustomRuntime( String id, IType enclosingClass ) {
    ICustomExpressionRuntime runtime = CUSTOM_RUNTIMES.get( id );
    if( runtime == null && enclosingClass instanceof ICompilableType ) {
      ((ICompilableType)enclosingClass).compile(); // force compilation of enclosing class indirectly compiles custom runtime expr which caches itself
      runtime = CUSTOM_RUNTIMES.get( id );
    }

    return runtime;
  }

  protected IRExpression handleCustomExpressionRuntime( ICustomExpressionRuntime customRuntime, IType expectedType ) {
    String customRuntimeId;
    int iLine = getParsedElement().getLineNum();
    int iColumnNum = getParsedElement().getColumn();
    customRuntimeId = makeCustomRuntimeKey( getGosuClass(), iLine, iColumnNum );
    CUSTOM_RUNTIMES.put( customRuntimeId, customRuntime );

    IRExpression getCustomExpression = callMethod( AbstractElementTransformer.class, "getCustomRuntime", new Class[]{String.class, IType.class}, null, exprList( pushConstant( customRuntimeId ), pushType( getGosuClass() ) ) );
    IRExpression result = callMethod( ICustomExpressionRuntime.class, "evaluate", new Class[0], getCustomExpression, exprList() );
    return unboxValueToType( expectedType, result );
  }

  private static String makeCustomRuntimeKey( IType enclosingClass, int iLineNum, int iColumnNum ) {
    return enclosingClass.getName() + '.' + IGosuProgram.NAME_PREFIX + "customRuntime_" + iLineNum + ":" + iColumnNum;
  }

  protected IRExpression pushExternalSymbolsMap()
  {
    // If this class also requires external symbol capture...

    if( _cc().hasSymbol( GosuFragmentTransformer.SYMBOLS_PARAM_ARG_NAME ) ) 
    {
      // Passed into a constructor, forwarding to super.
      // Normally this is handled in MethodCallExpressionTransformer#callSuperOrThisConstructorSymbol, however we have to handle it
      // here e.g., for the case where we're building the default ctor body.
      return identifier( _cc().getSymbol( GosuFragmentTransformer.SYMBOLS_PARAM_ARG_NAME ) );
    }
    else if( requiresExternalSymbolCapture( _cc().getGosuClass() ) && !inStaticContext() )
    {
      // Calling from an inner class enclosed in a program or fragment; pass along the already captured external map
      return buildFieldGet( _cc().getIRTypeForCurrentClass(), GosuFragmentTransformer.SYMBOLS_PARAM_NAME, getDescriptor( IExternalSymbolMap.class ), pushThis() );
    }
    else
    {
      // Otherwise, the symbol should be in scope in the current function
      return identifier( _cc().getSymbol( GosuFragmentTransformer.SYMBOLS_PARAM_NAME ) );
    }
  }

  public static boolean requiresExternalSymbolCapture( IType type ) {
    IType enclosingType = type.getEnclosingType();
    if( enclosingType == null ) {
      return false;
    }
    else if( type instanceof IGosuClass &&
             !(type instanceof IBlockClass) &&
             ((IGosuClass)type).getSourceFileHandle() instanceof StringSourceFileHandle &&
             type.getRelativeName().startsWith( FunctionToInterfaceClassGenerator.PROXY_FOR ) ) {
      // synthetic
      return false;
    }
    else if( enclosingType instanceof GosuFragment || enclosingType instanceof IGosuProgram ) {
      return true;
    }
    else {
      return requiresExternalSymbolCapture( enclosingType );
    }
  }

  // ---------------------------------------- Newly-Added helpers

  protected IRMethodCallExpression buildMethodCall(IRType ownersType, String name, boolean isInterface, IRType returnType, List<IRType> paramTypes, IRExpression root, List<IRExpression> args) {
    return new IRMethodCallExpression(name, ownersType, isInterface, returnType, paramTypes, root, args);
  }

  protected IRMethodCallExpression buildMethodCall(Class ownersType, String name, Class returnType, Class[] paramTypes, IRExpression root, List<IRExpression> args) {
    return new IRMethodCallExpression(name, getDescriptor(ownersType), ownersType.isInterface(), getDescriptor(returnType), getIRTypes(paramTypes), root, args);
  }

  protected IRExpression buildCast(IRType castType, IRExpression expression) {
    if( castType.isPrimitive() && expression.getType().equals( JavaClassIRType.get( Object.class ) ) ) {
      return unboxValueToType( castType, expression );
    }
    return new IRCastExpression(expression, castType);
  }

  protected IRExpression numericLiteral(Number value) {
    return new IRNumericLiteral(value);
  }

  protected IRExpression charLiteral(char c) {
    return new IRCharacterLiteral(c);
  }

  protected IRExpression booleanLiteral(boolean value) {
    return new IRBooleanLiteral(value);
  }

  protected IRExpression nullLiteral() {
    return new IRNullLiteral();
  }

  protected IRExpression stringLiteral( String value ) {
    return new IRStringLiteralExpression(value);
  }

  protected IRExpression buildArrayLoad(IRExpression root, int index, IRType componentType) {
    return new IRArrayLoadExpression(root, numericLiteral(index), componentType);
  }

  protected IRExpression buildArrayLoad(IRExpression root, IRExpression index, IRType componentType) {
    return new IRArrayLoadExpression(root, index, componentType);
  }

  protected IRExpression buildFieldGet(IRType owner, String fieldName, IRType fieldType, IRExpression root) {
    return new IRFieldGetExpression(root, fieldName, fieldType, owner );
  }

  protected IRStatement buildFieldSet(IRType owner, String fieldName, IRType fieldType, IRExpression root, IRExpression value) {
    return new IRFieldSetStatement(root, value, fieldName, fieldType, owner);
  }

  protected IRAssignmentStatement buildAssignment( IRSymbol symbol, IRExpression value ) {
    return new IRAssignmentStatement(symbol, value );
  }

  protected IRIdentifier identifier( IRSymbol symbol ) {
    return new IRIdentifier( symbol );
  }

  protected IRStatement buildArrayStore( IRExpression lhs, IRExpression index, IRExpression value, IRType componentType ) {
    return new IRArrayStoreStatement( lhs, index, value, componentType );
  }

  protected IRExpression buildInitializedArray( IRType componentType, List<IRExpression> values ) {
    componentType = IRElement.maybeEraseStructuralType( componentType );
    List<IRElement> elements = new ArrayList<IRElement>();
    IRSymbol tempArray = _cc.makeAndIndexTempSymbol( componentType.getArrayType() );
    elements.add( buildAssignment( tempArray, newArray( componentType, numericLiteral( values.size() ) ) ) );
    for (int i = 0; i < values.size(); i++) {
      IRExpression value = values.get(i);
      // Make sure the components of the array are all actually assignable to the component type.
      if (!(value instanceof IRNullLiteral) && !componentType.isAssignableFrom( value.getType() ) ) {
        value = buildCast( componentType, value );
      }
      elements.add( buildArrayStore(
              identifier( tempArray ),
              numericLiteral( i ),
              value,
              componentType) );
    }
    elements.add( identifier( tempArray ) );
    return new IRCompositeExpression( elements );
  }

  protected IRExpression buildNewExpression( IRType type, List<IRType> parameterTypes, List<IRExpression> args ) {
    return new IRNewExpression( type, parameterTypes, args );
  }

  protected IRExpression buildNewExpression( Class type, Class[] parameterTypes, List<IRExpression> args ) {
    return new IRNewExpression( getDescriptor(type), getIRTypes(parameterTypes), args );
  }

  protected final String getTypeVarParamName( IGenericTypeVariable typeVar ) {
    return TYPE_PARAM_PREFIX + typeVar.getName();
  }

  protected String getCapturedSymbolParameterName( ICapturedSymbol sym ) {
    return CAPTURED_VAR_PREFIX + sym.getName();
  }

  protected static List<IRExpression> exprList(IRExpression... expressions) {
    return Arrays.asList(expressions);
  }

  protected static List<IRType> getIRTypes(Class[] classes) {
    List<IRType> types = new ArrayList<IRType>(classes.length);
    for (int i = 0; i < classes.length; i++) {
      types.add(getDescriptor(classes[i]));
    }
    return types;
  }

  protected static List<IRType> getIRTypes(IJavaClassInfo[] iJavaClassInfos) {
    List<IRType> types = new ArrayList<IRType>(iJavaClassInfos.length);
    for (int i = 0; i < iJavaClassInfos.length; i++) {
      types.add(getDescriptor(iJavaClassInfos[i]));
    }
    return types;
  }

  protected IRExpression buildNullCheckTernary(IRExpression root, IRExpression ifNull, IRExpression ifNotNull) {
    return new IRTernaryExpression(
            buildEquals( root, nullLiteral() ),
            ifNull,
            ifNotNull,
            ifNotNull.getType()
    );
  }

  protected IRThrowStatement buildThrow( IRExpression exception ) {
    return new IRThrowStatement( exception );
  }

  protected IRIfStatement buildIf( IRExpression test, IRStatement ifStatement ) {
    return new IRIfStatement( test, ifStatement, null);
  }

  protected IRIfStatement buildIfElse( IRExpression test, IRStatement ifStatement, IRStatement elseStatement) {
    return new IRIfStatement( test, ifStatement, elseStatement);
  }

  protected IRCompositeExpression buildComposite(IRElement... elements) {
    return new IRCompositeExpression( elements );
  }

  protected IRCompositeExpression buildComposite(List<IRElement> elements) {
    return new IRCompositeExpression( elements );
  }

  protected IRMethodCallStatement buildMethodCall( IRExpression methodCall ) {
    return new IRMethodCallStatement( methodCall );
  }

  protected IRNewStatement buildNewExpression( IRNewExpression newExpression ) {
    return new IRNewStatement( newExpression );
  }

  protected IRStatement buildReturn() {
    return new IRReturnStatement();
  }

  protected IREqualityExpression buildEquals(IRExpression lhs, IRExpression rhs) {
    return new IREqualityExpression( lhs, rhs, true );
  }

  protected IREqualityExpression buildNotEquals( IRExpression lhs, IRExpression rhs ) {
    return new IREqualityExpression( lhs, rhs, false );
  }

  protected IRRelationalExpression buildGreaterThan( IRExpression lhs, IRExpression rhs ) {
    return new IRRelationalExpression( lhs, rhs, IRRelationalExpression.Operation.GT );
  }

  protected IRExpression buildTernary( IRExpression test, IRExpression trueValue, IRExpression falseValue, IRType resultType ) {
    return new IRTernaryExpression( IRArgConverter.castOrConvertIfNecessary(IRTypeConstants.pBOOLEAN(), test ), trueValue, falseValue, resultType );
  }

  protected IRExpression buildAddition( IRExpression lhs, IRExpression rhs) {
    return buildArithmetic( lhs, rhs, IRArithmeticExpression.Operation.Addition );
  }

  protected IRExpression buildSubtraction( IRExpression lhs, IRExpression rhs) {
    return buildArithmetic( lhs, rhs, IRArithmeticExpression.Operation.Subtraction );
  }

  protected IRExpression buildArithmetic( IRExpression lhs, IRExpression rhs, IRArithmeticExpression.Operation operation) {
    if (!lhs.getType().equals(rhs.getType())) {
      throw new IllegalArgumentException("Arithmetic must be between identical types.  Found " + lhs.getType().getName() + " and " + rhs.getType().getName());
    }
    return new IRArithmeticExpression( lhs.getType(), lhs, rhs, operation );
  }

  protected IRExpression buildNegation( IRExpression root ) {
    return new IRNegationExpression( root );
  }

  protected IRExpression buildArrayLength( IRExpression root ) {
    return new IRArrayLengthExpression( root );
  }

  protected boolean inStaticContext() {
    return _cc().isCurrentFunctionStatic();
  }

  protected List<GosuAnnotationInfo> makeAnnotationInfos( List<IGosuAnnotation> gosuAnnotations, IFeatureInfo fiAnnotated )
  {
    List<GosuAnnotationInfo> annotationInfos = new ArrayList<GosuAnnotationInfo>();
    for( int i = 0; i < gosuAnnotations.size(); i++ )
    {
      IGosuAnnotation ga = gosuAnnotations.get( i );
      annotationInfos.add( new GosuAnnotationInfo( ga, fiAnnotated, (IGosuClassInternal)ga.getOwnersType() ) );
    }
    return annotationInfos;
  }

  protected boolean isCheckedArithmeticEnabled()
  {
    DynamicFunctionSymbol currentFunction = _cc().getCurrentFunction();
    return _checkedArithmetic.get() && currentFunction != null && !"hashCode()".equals( currentFunction.getName() );
  }

  // --------------------- Methods moved from GosuClassTransformer

  public static boolean isNonStaticInnerClass( IType type )
  {
    return type != null && type.getEnclosingType() != null && !Modifier.isStatic( type.getModifiers() );
  }

  protected IRExpression getField_new( IRProperty irProp, IRExpression root, IRType expectedType )
  {
    IRType resultType;
    IRExpression result = getFieldImpl_new( irProp, root );
    if ( irProp.isCaptured() ) {
      result = buildArrayLoad( result, 0, irProp.getType().getComponentType() );
      resultType = irProp.getType().getComponentType();
    } else {
      resultType = irProp.getType();
    }

    // If the owner is parameterized, we only know that the field is of the reified type, so we must checkcast
    if( !expectedType.isAssignableFrom( resultType ) )
    {
      result = buildCast( expectedType, result );
    }
    return result;
  }

  private IRExpression getFieldImpl_new( IRProperty irProp, IRExpression root )
  {
    IRType rootType = root == null ? null : root.getType();
    if( rootType == null )
    {
      rootType = irProp.getOwningIRType();
    }
    if( _cc().shouldUseReflection( irProp.getOwningIType(), rootType, irProp.getAccessibility() ) )
    {
      return getFieldReflectively_new( irProp, root );
    }
    else
    {
      return buildFieldGet( irProp.getOwningIRType(), irProp.getName(), irProp.getType(), root );
    }
  }

  private IRExpression getFieldReflectively_new( IRProperty irProp, IRExpression root )
  {
    // Call getDeclaredField using the owner's Class and the field name as arguments
    IRExpression getDeclaredFieldCall = buildMethodCall(AbstractElementTransformer.class, "getDeclaredField", Field.class, new Class[]{Class.class, String.class},
            null,
            exprList( classLiteral( irProp.getOwningIRType() ),
                      stringLiteral( irProp.getName() ) ) );
    // Call "get" on the result of that call, passing in the root expression or the "null" constant if there is none
    IRExpression getCall = buildMethodCall( Field.class, "get", Object.class, new Class[]{Object.class},
            getDeclaredFieldCall,
            exprList( (root == null ? pushNull() : root) ) );
    // Unbox it on the way out if needed
    return unboxValueToType( irProp.getType(), getCall );
  }

  protected void assignStructuralTypeOwner( IExpression rootExpr, IRExpression irMethodCall )
  {
    if( rootExpr != null && rootExpr.getType() instanceof IGosuClass && ((IGosuClass)rootExpr.getType()).isStructure() )
    {
      IRExpression mc = irMethodCall;
      while( mc instanceof IRCompositeExpression )
      {
        List<IRElement> elements = ((IRCompositeExpression)mc).getElements();
        mc = (IRExpression)elements.get( elements.size() -1 );
      }
      ((IRMethodCallExpression)mc).setStructuralTypeOwner( GosuClassIRType.get( TypeLord.getPureGenericType( rootExpr.getType() ) ) );
    }
  }

  protected IRExpression fastStringCoercion( IRExpression expr, IType operandType ) {
    IRExpression stringValueExpr;
    if( !operandType.isPrimitive() ) {
      if( JavaTypes.pCHAR().getArrayType().isAssignableFrom( operandType ) ) {
        stringValueExpr = callStaticMethod( String.class, "valueOf", new Class[]{char[].class}, Collections.singletonList( expr ) );
      }
      else if( !isHandledByCustomCoercion( operandType ) ) {
        stringValueExpr = callMethod( Object.class, "toString", new Class[0], expr, Collections.<IRExpression>emptyList() );
      }
      else {
        stringValueExpr = callMethod( ICoercionManager.class, "makeStringFrom", new Class[]{Object.class},
                       callStaticMethod( CommonServices.class, "getCoercionManager", new Class[]{}, Collections.<IRExpression>emptyList() ),
                       Collections.singletonList( expr ) );
      }
    }
    else {
      Class primitiveClass = getDescriptor( operandType ).getJavaClass();
      if( !isHandledByCustomCoercion( operandType ) ) {
        primitiveClass = primitiveClass == short.class || primitiveClass == byte.class
                         ? int.class
                         : primitiveClass;
        stringValueExpr = primitiveClass == void.class
                          ? nullLiteral()
                          : callStaticMethod( String.class, "valueOf", new Class[]{primitiveClass}, Collections.<IRExpression>singletonList( expr ) );
      }
      else {
        // This is really stupid, but the pl coercion mgr converts double/float to non-decimal strings if the value is non-fractional, so instead of printing 1.0 for a normal _decimal_ value it prints 1  (fart)
        stringValueExpr = callMethod( ICoercionManager.class, "makeStringFrom", new Class[]{Object.class},
                       callStaticMethod( CommonServices.class, "getCoercionManager", new Class[]{}, Collections.<IRExpression>emptyList() ),
                       Collections.singletonList( boxValue( getDescriptor( primitiveClass ), expr ) ) );
      }
    }
    return stringValueExpr;
  }

  protected boolean isHandledByCustomCoercion( IType operandType ) {
    if( ILanguageLevel.Util.STANDARD_GOSU() ) {
      return false;
    }
    return
      operandType == JavaTypes.BIG_DECIMAL() ||
      operandType == JavaTypes.FLOAT() ||
      operandType == JavaTypes.pFLOAT() ||
      operandType == JavaTypes.DOUBLE() ||
      operandType == JavaTypes.pDOUBLE() ||
      operandType == JavaTypes.DATE() ||
      operandType == JavaTypes.OBJECT() ||
      TypeSystem.get( IEnumConstant.class ).isAssignableFrom( operandType ) ||
      CommonServices.getEntityAccess().isEntityClass( operandType );
  }

  public static IType findDimensionType( IType type ) {
    if( !JavaTypes.IDIMENSION().isAssignableFrom( type ) ) {
      return null;
    }
    IType dimType = TypeLord.findParameterizedType( type, JavaTypes.IDIMENSION() );
    return dimType.isGenericType() ? null : dimType.getTypeParameters()[1];
  }

  final protected IType findComparableParamType( IType type ) {
    if( !JavaTypes.COMPARABLE().isAssignableFrom( type ) ) {
      return null;
    }
    type = TypeLord.getPureGenericType( type );
    return findCompareToParamType( type );
  }

  private IType findCompareToParamType( IType type ) {
    if( type == null ) {
      return null;
    }
    type = TypeLord.getPureGenericType( type );
    ITypeInfo ti = type.getTypeInfo();
    if( ti instanceof IRelativeTypeInfo ) {
      for( IMethodInfo csr : ((IRelativeTypeInfo)ti).getDeclaredMethods() ) {
        if( "compareTo".equals( csr.getDisplayName() ) ) {
          IParameterInfo[] params = csr.getParameters();
          if( params != null && params.length == 1 ) {
            IType paramType = TypeLord.getPureGenericType( TypeLord.getDefaultParameterizedTypeWithTypeVars( params[0].getFeatureType() ) );
            if( paramType.isAssignableFrom( type ) ) {
              return paramType;
            }
          }
        }
      }
    }
    if( !type.isInterface() ) {
      IType paramType = findCompareToParamType( type.getSupertype() );
      if( paramType != null ) {
        return paramType;
      }
    }
    IType[] interfaces = type.getInterfaces();
    if( interfaces != null ) {
      for( IType iface: interfaces ) {
        IType paramType = findCompareToParamType( iface );
        if( paramType != null ) {
          return paramType;
        }
      }
    }
    return null;
  }
}

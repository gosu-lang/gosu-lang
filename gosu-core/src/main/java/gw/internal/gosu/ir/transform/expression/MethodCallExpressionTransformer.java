/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.nodes.IRMethod;
import gw.internal.gosu.ir.nodes.IRMethodFactory;
import gw.internal.gosu.ir.nodes.JavaClassIRType;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.GosuFragmentTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.InitConstructorFunctionSymbol;
import gw.internal.gosu.parser.SuperConstructorFunctionSymbol;
import gw.internal.gosu.parser.ThisConstructorFunctionSymbol;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.internal.gosu.parser.expressions.MethodCallExpression;
import gw.internal.gosu.parser.statements.BeanMethodCallStatement;
import gw.internal.gosu.template.TemplateGenerator;
import gw.lang.function.IBlock;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.parser.IExpression;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.ILockedDownSymbol;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPlaceholder;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IExternalSymbolMap;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.java.JavaTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class MethodCallExpressionTransformer extends AbstractExpressionTransformer<MethodCallExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, MethodCallExpression expr )
  {
    MethodCallExpressionTransformer compiler = new MethodCallExpressionTransformer( cc, expr );
    return compiler.compile();
  }

  private MethodCallExpressionTransformer( TopLevelTransformationContext cc, MethodCallExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IFunctionSymbol symbol = _expr().getFunctionSymbol();

    if( symbol instanceof ILockedDownSymbol )
    {
      // 'Global' static function call e.g., print()
      return callGlobalStaticFunction( symbol );
    }
    else if( _cc().isExternalSymbol( symbol.getName() ) )
    {
      return callExternalProgramSymbol(symbol);
    }
    else if( symbol instanceof DynamicFunctionSymbol )
    {
      return callDynamicFunctionSymbol(symbol);
    }
    else if( symbol.getType() instanceof IPlaceholder && ((IPlaceholder)symbol.getType()).isPlaceholder() )
    {
      return callBlockViaDynamicType( symbol );
    }
    else
    {
      throw new UnsupportedOperationException( "Don't know how to compile symbol: " + symbol.getType() );
    }
  }

  private IRExpression callBlockViaDynamicType( IFunctionSymbol symbol )
  {
    if( !(symbol.getType() instanceof IPlaceholder) )
    {
      throw new IllegalArgumentException( "Expecting symbol to have dynamic type" );
    }

    // Generates: ((IBlock)symbolValue).invokeWithArgs( args )

    Identifier identifier = new Identifier();
    identifier.setSymbol( symbol, symbol.getDynamicSymbolTable() );
    IRExpression idExpr = IdentifierTransformer.compile( _cc(), identifier );
    idExpr = buildCast( JavaClassIRType.get( IBlock.class ), idExpr );
    List<IRExpression> irArgs = new ArrayList<IRExpression>();
    pushArgumentsNoCasting( null, _expr().getArgs(), irArgs );
    IRExpression objArray = collectArgsIntoObjArray( irArgs );
    return callMethod( IBlock.class, "invokeWithArgs", new Class[]{Object[].class}, idExpr, Collections.singletonList( objArray ) );
  }

  private IRExpression callExternalProgramSymbol(IFunctionSymbol symbol) {
    List<IRExpression> argValues = new ArrayList<IRExpression>();
    Expression[] args = _expr().getArgs();
    if (args != null) {
      for( int i = 0; i < args.length; i++ )
      {
        IExpression arg = args[i];
        IRExpression irArg = ExpressionTransformer.compile( arg, _cc() );
        if( arg.getType().isPrimitive() )
        {
          irArg = boxValue( arg.getType(), irArg );
        }
        argValues.add( irArg );
      }
    }

    IRExpression methodCall = callMethod( IExternalSymbolMap.class, "invoke", new Class[]{String.class, Object[].class},
            pushExternalSymbolsMap(),
            exprList( pushConstant( symbol.getName() ), buildInitializedArray(IRTypeConstants.OBJECT(), argValues ) ) );

    IType returnType = ((IFunctionType)symbol.getType()).getReturnType();
    if( returnType != JavaTypes.pVOID() )
    {
      return unboxValueToType( returnType, methodCall );
    }
    else
    {
      return methodCall;
    }
  }

  private IRExpression callDynamicFunctionSymbol(IFunctionSymbol symbol) {
    DynamicFunctionSymbol dfs = (DynamicFunctionSymbol)symbol;
    IRExpression result;
    if( dfs instanceof ThisConstructorFunctionSymbol ||
        dfs instanceof SuperConstructorFunctionSymbol ||
        dfs instanceof InitConstructorFunctionSymbol)
    {
      // Call 'this( xxx )' or 'super( xxx )' from ctor
      result = callSuperOrThisConstructorSymbol( dfs,
                                                 dfs instanceof SuperConstructorFunctionSymbol ||
                                                 dfs instanceof InitConstructorFunctionSymbol,
                                                 dfs instanceof ThisConstructorFunctionSymbol && ((ThisConstructorFunctionSymbol)dfs).isGenericJavaInterop() );
    }
    else
    {
      // It's a normal method call, either static or instance:  determine the root appropriately
      IRExpression root;
      boolean isOnThis = false;
      if (dfs.isStatic()) {
        root = null;
      }
      else if( isMemberOnEnhancementOfEnclosingType( dfs ) ) {
        root = pushOuter( ((IGosuEnhancement) dfs.getGosuClass()).getEnhancedType() );
      }
      else if( isMemberOnEnclosingType( dfs ) != null ) {
        root = pushOuter( dfs.getGosuClass() );
      }
      else {
        root = pushThis();
        isOnThis = true;
      }

      IAttributedFeatureInfo methodOrConstructorInfo = dfs.getMethodOrConstructorInfo();
      if( methodOrConstructorInfo instanceof IMethodInfo)
      {
        IRMethod mi = IRMethodFactory.createIRMethod( (IMethodInfo)methodOrConstructorInfo, _expr().getFunctionType() );
        if (isOnThis && mi.getAccessibility() == IRelativeTypeInfo.Accessibility.PRIVATE) {
          // private methods are always invoked as special
          result = callMethod( mi, root, pushArguments( mi ), _expr().getNamedArgOrder(), true );
        } else {
          result = callMethod( mi, root, pushArguments( mi ), _expr().getNamedArgOrder() );
        }
      }
      else
      {
        throw new UnsupportedOperationException();
      }
    }

    return castIfReturnTypeDerivedFromTypeVariable( dfs, result );
  }

  private IRExpression callSuperOrThisConstructorSymbol( DynamicFunctionSymbol dfs, boolean bSuper, boolean genericJavaInterop )
  {
    IRExpression result;
    List<IRStatement> bonusStatements = new ArrayList<>();
    _cc().maybeAssignOuterRef( bonusStatements );

    // Assemble the args
    List<IRExpression> implicitArgs = new ArrayList<>();
    IType targetType;
    _cc().markInvokingSuper();
    if( bSuper )
    {
      // In the case of a super call, we want to push the appropriate enclosing arguments
      _cc().maybePushSupersEnclosingThisRef( implicitArgs );
      targetType = _cc().getSuperType();
    }
    else
    {
      // In the case of a this call, we want to pass along the implicit outer argument, if any
      if( _cc().isNonStaticInnerClass() )
      {
        implicitArgs.add( identifier( _cc().getSymbol( _cc().getOuterThisParamName() ) ) );
      }
      targetType = getGosuClass();
    }

    pushCapturedSymbols( _cc().getSuperType(), implicitArgs, true );

    if( bSuper && requiresExternalSymbolCapture( _cc().getSuperType() ) ||
        !bSuper && requiresExternalSymbolCapture( getGosuClass() ) )
    {
      implicitArgs.add( identifier( _cc().getSymbol( GosuFragmentTransformer.SYMBOLS_PARAM_NAME + "arg" ) ) );
    }
    int iTypeParams = pushTypeParametersForConstructor( _expr(), targetType, implicitArgs, bSuper, genericJavaInterop );
    pushEnumSuperConstructorArguments( implicitArgs );
    IRMethod irMethod = IRMethodFactory.createConstructorIRMethod( targetType, dfs, iTypeParams );
    List<IRExpression> explicitArgs = pushArguments( irMethod );
    IRExpression methodCall = callSpecialMethod( getDescriptor( targetType ), irMethod, pushThis(),
                                                 implicitArgs, explicitArgs, _expr().getNamedArgOrder() );
    _cc().markSuperInvoked();

    if( bonusStatements.isEmpty() )
    {
      result = methodCall;
    }
    else
    {
      result = buildComposite( bonusStatements.get( 0 ), methodCall );
    }
    return result;
  }

  private IRExpression callGlobalStaticFunction( IFunctionSymbol symbol ) {
    IRSymbol currentTemplate = TemplateStringLiteralTransformer.getCurrentTemplateSymbol();
    if( currentTemplate != null && symbol == TemplateGenerator.PRINT_CONTENT_SYMBOL.get() )
    {
      // Special case printContent() implementation for StringLiteral template
      // where we optimize by appending to the StringBuilder for the string.
      return callMethod( StringBuilder.class, "append", new Class[]{Object.class},
        identifier( currentTemplate ),
        exprList( ExpressionTransformer.compile( _expr().getArgs()[0], _cc() ) ) );
    }
    else
    {
      // Call the Method value of the Symbol directly
      IRMethod method = IRMethodFactory.createIRMethod( (java.lang.reflect.Method)symbol.getValue() );
      return callMethod( method, null, pushArguments( method ) );
    }
  }

  private IRExpression castIfReturnTypeDerivedFromTypeVariable( DynamicFunctionSymbol dfs, IRExpression root )
  {
    if( !(_expr().getParent() instanceof BeanMethodCallStatement) )
    {
      IType retType = _expr().getReturnType();
      if( retType != JavaTypes.pVOID() && !retType.isPrimitive() )
      {
        IAttributedFeatureInfo methodOrConstructorInfo = dfs.getMethodOrConstructorInfo();
        if( methodOrConstructorInfo instanceof IMethodInfo )
        {
          IMethodInfo mi = (IMethodInfo)methodOrConstructorInfo;
          IRMethod irMethod = IRMethodFactory.createIRMethod( mi, null );
          if( !getDescriptor( retType ).isAssignableFrom( irMethod.getReturnType() ) )
          {
            return checkCast( retType, root );
          }
        }
      }
    }

    return root;
  }

  private List<IRExpression> pushArguments( IRMethod irMethod )
  {
    List<IRExpression> irArgs = new ArrayList<IRExpression>();
    Expression[] args = _expr().getArgs();
    if( args != null )
    {
      List<IRType> explicitParamTypes = irMethod.getExplicitParameterTypes();
      for( int i = 0; i < args.length; i++ )
      {
        IExpression arg = args[i];
        IRExpression irArg = ExpressionTransformer.compile( arg, _cc() );
        // Maybe cast if not directly assignable (e.g., cross cast)
        IRType paramClass = explicitParamTypes.get( i );
        if( !paramClass.isAssignableFrom( irArg.getType() ) )
        {
          irArg = buildCast( paramClass, irArg );
        }
        irArgs.add( irArg );
      }
    }
    return irArgs;
  }

}

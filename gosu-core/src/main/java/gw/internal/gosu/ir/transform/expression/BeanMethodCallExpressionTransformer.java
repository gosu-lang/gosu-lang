/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.nodes.IRMethod;
import gw.internal.gosu.ir.nodes.IRMethodFactory;
import gw.internal.gosu.ir.nodes.IRMethodFromMethodInfo;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.ArrayExpansionMethodInfo;
import gw.internal.gosu.parser.expressions.BeanMethodCallExpression;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.internal.gosu.parser.expressions.SuperAccess;
import gw.internal.gosu.parser.statements.BeanMethodCallStatement;
import gw.internal.gosu.runtime.GosuRuntimeMethods;
import gw.lang.ir.IRElement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.ir.expression.IRCompositeExpression;
import gw.lang.parser.ICustomExpressionRuntime;
import gw.lang.parser.IExpression;
import gw.lang.parser.Keyword;
import gw.lang.parser.MemberAccessKind;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfoMethodInfo;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.java.JavaTypes;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class BeanMethodCallExpressionTransformer extends AbstractExpressionTransformer<BeanMethodCallExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, BeanMethodCallExpression expr )
  {
    BeanMethodCallExpressionTransformer compiler = new BeanMethodCallExpressionTransformer( cc, expr );
    return compiler.compile();
  }

  public BeanMethodCallExpressionTransformer( TopLevelTransformationContext cc, BeanMethodCallExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    if( _expr().isExpansion() )
    {
      return BeanMethodCallExpansionTransformer.compile( _cc(), _expr() );
    }

    IRExpression result;
    IExpression rootExpr = _expr().getRootExpression();
    IMethodInfo mi = getMethodInfo();
    IRMethodFromMethodInfo irMethod;
    if (_expr().getExpressionRuntime() instanceof ICustomExpressionRuntime)
    {
      return handleCustomExpressionRuntime( (ICustomExpressionRuntime) _expr().getExpressionRuntime(), _expr().getType() );
    }
    else if( mi instanceof ITypeInfoMethodInfo )
    {
      ITypeInfoMethodInfo metaMethod = (ITypeInfoMethodInfo)mi;
      IMethodInfo backingMethod = metaMethod.getBackingMethodInfo();
      irMethod = IRMethodFactory.createIRMethod(backingMethod, _expr().getFunctionType() );
      result = callInstanceMethod( rootExpr, irMethod, _expr().getNamedArgOrder() );
    }
    else if( mi.isStatic() )
    {
      irMethod = IRMethodFactory.createIRMethod( mi, _expr().getFunctionType() );
      result = callStaticMethod( rootExpr, irMethod, _expr().getNamedArgOrder() );
    }
    else
    {
      irMethod = IRMethodFactory.createIRMethod( mi, _expr().getFunctionType() );
      result = callInstanceMethod( rootExpr, irMethod, _expr().getNamedArgOrder() );
    }

    return castIfReturnTypeDerivedFromTypeVariable( irMethod, result );
  }

  private IRExpression callInstanceMethod( IExpression rootExpr, IRMethodFromMethodInfo irMethod, int[] namedArgOrder )
  {
    if( isArrayExpansionMethod( irMethod.getOriginalMethod() ) )
    {
      return BeanMethodCallExpansionTransformer.compile( _cc(), _expr() );
    }
    else
    {
      IRExpression irRootRaw = pushRootExpression( irMethod, rootExpr );
      IRExpression irRoot = irRootRaw;
      IRExpression irMethodCall;
      IRSymbol rootSymbol = null;
      boolean bShouldNullShortCircuitCheck = shouldNullShortCircuit();
      if( bShouldNullShortCircuitCheck )
      {
        rootSymbol = _cc().makeAndIndexTempSymbol( irRoot.getType() );
        irRoot = identifier( rootSymbol );
      }

      if( irMethod.isBytecodeMethod() )
      {
        List<IRExpression> irArgs = new ArrayList<IRExpression>();
        pushArgumentsWithCasting( irMethod, _expr().getArgs(), irArgs );
        if( rootExpr instanceof SuperAccess )
        {
          irMethodCall = callSpecialMethod( getDescriptor( rootExpr.getType() ), irMethod, irRoot, irArgs, namedArgOrder );
        }
        else if( isSuperCall( rootExpr ) )
        {
          irMethodCall = callSpecialMethod( getDescriptor( _cc().getSuperType() ), irMethod, irRoot, irArgs, namedArgOrder );
        }
        else
        {
          irMethodCall = callMethod( irMethod, irRoot, irArgs, namedArgOrder );
          if( irMethod.getFunctionType() == null || !(irMethod.getFunctionType().getEnclosingType() instanceof IGosuEnhancement) )
          {
            assignStructuralTypeOwner( rootExpr, irMethodCall );
          }
        }
      }
      else
      {
        irMethodCall = callMethodInfo( irMethod, irRoot, namedArgOrder );
      }

      if( shouldNullShortCircuit() )
      {
        if( _expr().getType() == JavaTypes.pVOID() )
        {
          irMethodCall = buildComposite(
            buildAssignment( rootSymbol, irRootRaw ),
            buildIf( buildNotEquals( identifier( rootSymbol ), nullLiteral() ),
                     buildMethodCall( irMethodCall ) ) );
        }
        else
        {
          irMethodCall = buildComposite(
            buildAssignment( rootSymbol, irRootRaw ),
            buildNullCheckTernary( irRoot,
                                   shortCircuitValue( irMethodCall.getType() ),
                                   irMethodCall ) );
        }
      }
      return irMethodCall;
    }
  }

  private boolean shouldNullShortCircuit()
  {
    return _expr().getMemberAccessKind() == MemberAccessKind.NULL_SAFE;
  }

  private boolean isArrayExpansionMethod( IMethodInfo mi )
  {
    return mi instanceof ArrayExpansionMethodInfo;
  }

  private IRExpression callStaticMethod( IExpression rootExpr, IRMethodFromMethodInfo irMethod, int[] namedArgOrder )
  {
    if( irMethod.isBytecodeMethod() )
    {
      List<IRExpression> args = new ArrayList<IRExpression>();
      pushArgumentsWithCasting( irMethod, _expr().getArgs(), args );
      return callMethod( irMethod, null, args, namedArgOrder );
    }
    else
    {
      return callMethodInfo( irMethod, pushRootExpression( irMethod, rootExpr ), namedArgOrder );
    }
  }

  private IRExpression castIfReturnTypeDerivedFromTypeVariable( IRMethod mi, IRExpression root )
  {
    if( !(_expr().getParent() instanceof BeanMethodCallStatement) )
    {
      IType retType = _expr().getReturnType();
      if( retType != JavaTypes.pVOID() && !retType.isPrimitive() )
      {
        if( !mi.getReturnType().equals( getDescriptor( retType ) ) )
        {
          return checkCast( retType, root );
        }
      }
    }
    return root;
  }

  private IMethodInfo getMethodInfo()
  {
    IMethodInfo methodInfo = _expr().getMethodDescriptor();
    if (methodInfo == null) {
      throw new NullPointerException("Null method descriptor for expression: " + _expr());
    }

    // If we get back a method from IGosuObject that is merely overridding a method on Object,
    // we need to replace it with a call to Object instead.  This is necessary to handle super calls correctly,
    // like super.hashCode(), that have to be made against a concrete type, and it also avoids doing
    // unnecessary INVOKEINTERFACE calls on these methods.
    if (methodInfo.getOwnersType().getName().equals("gw.lang.reflect.gs.IGosuObject")) {
      if (methodInfo.getDisplayName().equals("equals")) {
        methodInfo = JavaTypes.OBJECT().getTypeInfo().getMethod("equals", JavaTypes.OBJECT());
      } else if (methodInfo.getDisplayName().equals("hashCode")) {
        methodInfo = JavaTypes.OBJECT().getTypeInfo().getMethod("hashCode");
      } else if (methodInfo.getDisplayName().equals("toString")) {
        methodInfo = JavaTypes.OBJECT().getTypeInfo().getMethod("toString");
      }
    }

    return methodInfo;
  }

  private boolean isSuperCall( IExpression rootExpr )
  {
    return rootExpr instanceof Identifier && Keyword.KW_super.equals( ((Identifier)rootExpr).getSymbol().getName() );
  }

  private IRExpression callMethodInfo( IRMethodFromMethodInfo irMethod, IRExpression irRootExpr, int[] namedArgOrder )
  {
    IMethodInfo mi = irMethod.getTerminalMethod();

    List<IRExpression> explicitArgs = new ArrayList<IRExpression>();
    pushArgumentsNoCasting( irMethod, _expr().getArgs(), explicitArgs );
    List<IRElement> callElements = handleNamedArgs( explicitArgs, namedArgOrder );

    IRExpression irRoot;
    if( !irMethod.isStatic() )
    {
      IRSymbol rootSymbol = _cc().makeAndIndexTempSymbol( irRootExpr.getType() );
      callElements.add( buildAssignment( rootSymbol, irRootExpr ) );
      callElements.add( nullCheckVar( rootSymbol ) );
      irRoot = identifier( rootSymbol );
    }
    else
    {
      irRoot = irRootExpr;
    }

    IRExpression miCall = callStaticMethod( GosuRuntimeMethods.class, "invokeMethodInfo", new Class[]{IType.class, String.class, IType[].class, Object.class, Object[].class},
            exprList(
                    pushType( mi.getOwnersType(), true ),
                    pushConstant( mi.getDisplayName() ),
                    pushParamTypes( mi.getParameters() ),
                    irRoot,
                    pushArgumentsAsArray( explicitArgs ) ) );
    miCall = unboxValueToType( mi.getReturnType(), miCall );
    if( callElements.size() > 0 )
    {
      // Include temp var assignments so named args are evaluated in lexical order before the call
      callElements.add( miCall );
      miCall = new IRCompositeExpression( callElements );
    }
    return miCall;
  }

  private IRExpression pushRootExpression( IRMethod irMethod, IExpression rootExpr )
  {
    IRExpression root = ExpressionTransformer.compile( rootExpr, _cc() );
    //## todo: is this necessary? s/b typed properly now with implicit typeas
    if (irMethod != null) {
      root = boxValue( irMethod.getOwningIRType(), root );
    }

    if( irMethod != null && !irMethod.isStatic() )
    {
      IRType type = irMethod.getTargetRootIRType();
      if( !type.isAssignableFrom( root.getType() ) && (!(rootExpr.getType() instanceof IGosuClass) || !((IGosuClass)rootExpr.getType()).isStructure()) )
      {
        root = buildCast( type, root );
      }
    }

    return root;
  }

  private IRExpression pushArgumentsAsArray( List<IRExpression> explicitArgs )
  {
    List<IRExpression> irArgs = new ArrayList<IRExpression>();

    if( explicitArgs != null )
    {
      for( IRExpression arg : explicitArgs )
      {
        if( arg.getType().isPrimitive() )
        {
          arg = boxValue( arg.getType(), arg );
        }
        irArgs.add( arg );
      }
    }
    return buildInitializedArray(IRTypeConstants.OBJECT(), irArgs );
  }

}

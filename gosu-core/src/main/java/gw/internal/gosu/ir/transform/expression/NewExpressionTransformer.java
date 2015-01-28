/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.nodes.IRMethod;
import gw.internal.gosu.ir.nodes.IRMethodFactory;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.TypeVariableType;
import gw.internal.gosu.parser.expressions.NewExpression;
import gw.lang.ir.IRElement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.expression.IRCompositeExpression;
import gw.lang.ir.expression.IRNewMultiDimensionalArrayExpression;
import gw.lang.parser.IExpression;
import gw.lang.parser.expressions.IInitializerExpression;
import gw.lang.reflect.IConstructorHandler;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class NewExpressionTransformer extends AbstractExpressionTransformer<NewExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, NewExpression expr )
  {
    NewExpressionTransformer compiler = new NewExpressionTransformer( cc, expr );
    return compiler.compile();
  }

  private NewExpressionTransformer( TopLevelTransformationContext cc, NewExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IConstructorInfo ci = _expr().getConstructor();
    if( !_expr().getType().isArray() )
    {
      if( _expr().getType() instanceof TypeVariableType )
      {
        // TypeVar Constructor invocation
        // e.g., new T( <params> )
        return compileTypeVarConstructorCall();
      }
      else
      {
        // Constructor invocation
        // e.g., new Foo()
        return compileConstructorCall( ci );
      }
    }
    else if( _expr().getValueExpressions() != null )
    {
      // Array construction with intialization
      // e.g., new String[] {"a", "b", "c"}

      return compileArrayInitialization( );
    }
    else if( _expr().getSizeExpressions() != null )
    {
      // Array construction with size[s]
      // e.g., new long[5][]
      // e.g., new String[5][8][]

      return compileArrayConstruction( );
    }
    else
    {
      // Array construction with empty intialization
      // e.g., new String[] {}

      IType atomicType = _expr().getType().getComponentType();

      if( isBytecodeType( atomicType ) )
      {
        return newArray( getDescriptor( atomicType ), pushConstant( 0 ) );
      }
      else
      {
        return makeArrayViaTypeInfo( atomicType, Collections.<Expression>emptyList() );
      }
    }
  }

  private IRExpression compileArrayConstruction( )
  {
    List<Expression> sizeExpressions = _expr().getSizeExpressions();
    IType atomicType = _expr().getType().getComponentType();

    if( isBytecodeType( atomicType ) )
    {
      if( sizeExpressions.size() == 1 )
      {
        Expression sizeExpr = sizeExpressions.get( 0 );
        return newArray( getDescriptor( atomicType ), ExpressionTransformer.compile( sizeExpr, _cc() ) );
      }
      else
      {
        List<IRExpression> irSizeExpressions = new ArrayList<IRExpression>();
        for( int i = 0; i < sizeExpressions.size(); i++ )
        {
          irSizeExpressions.add( ExpressionTransformer.compile( sizeExpressions.get( i ), _cc() ) );
        }
        return new IRNewMultiDimensionalArrayExpression( getDescriptor( _expr().getType() ), irSizeExpressions );
      }
    }
    else
    {
      return makeEmptyArrayViaTypeInfo( atomicType, sizeExpressions );
    }
  }

  private IRExpression compileArrayInitialization( )
  {
    List<Expression> valueExpressions = _expr().getValueExpressions();
    IType atomicType = _expr().getType().getComponentType();

    if( isBytecodeType( atomicType ) )
    {
      List<IRExpression> irValueExpressions = new ArrayList<IRExpression>();
      for( int i = 0; i < valueExpressions.size(); i++ )
      {
        irValueExpressions.add( ExpressionTransformer.compile( valueExpressions.get( i ), _cc() ) );
      }
      return buildInitializedArray( getDescriptor( atomicType ), irValueExpressions );
    }
    else
    {
      return makeArrayViaTypeInfo( atomicType, valueExpressions );
    }
  }

  private IRExpression compileConstructorCall( IConstructorInfo ci )
  {
    IType type = _expr().getType();
    IRMethod irConstructor = IRMethodFactory.createIRMethod( ci );

    IRExpression constructorCall;

    List<IRElement> newExprElements;

    if( irConstructor.isBytecodeMethod() &&
        isBytecodeType( type ) && 
        !_cc().shouldUseReflection( irConstructor.getOwningIType(), irConstructor.getAccessibility() ) )
    {
      List<IRExpression> explicitArgs = new ArrayList<IRExpression>();
      pushArgumentsWithCasting( irConstructor, _expr().getArgs(), explicitArgs );
      newExprElements = handleNamedArgs( explicitArgs, _expr().getNamedArgOrder() );

      // Invoke the constructor
      List<IRExpression> args = new ArrayList<IRExpression>();
      if( isNonStaticInnerClass( type ) )
      {
        args.add( pushThisOrOuter( type.getEnclosingType() ) );
      }
      pushCapturedSymbols( type, args, false );
      pushTypeParametersForConstructor( _expr(), type, args );
      _cc().pushEnumNameAndOrdinal( type, args );
      args.addAll( explicitArgs );
      constructorCall = buildNewExpression( getDescriptor( type ), irConstructor.getAllParameterTypes(), args );
    }
    else
    {
      List<IRExpression> explicitArgs = new ArrayList<IRExpression>();
      pushArgumentsNoCasting( irConstructor, _expr().getArgs(), explicitArgs );
      newExprElements = handleNamedArgs( explicitArgs, _expr().getNamedArgOrder() );

      // Call the IConstructorInfo dynamically
      constructorCall = callConstructorInfo( type, ci, explicitArgs );
    }

    if( newExprElements.size() > 0 )
    {
      // Include temp var assignments so named args are evaluated in lexical order before the ctor call
      newExprElements.add( constructorCall );
      constructorCall = new IRCompositeExpression( newExprElements );
    }

    IInitializerExpression initializer = _expr().getInitializer();
    if( initializer != null )
    {
      // If there's an initializer, save the result of the constructor to a temp symbol, execute the initializer
      // statements, and then load the temp symbol back so it's the result of the expression
      IRSymbol tempSymbol = _cc().makeAndIndexTempSymbol( constructorCall.getType() );
      List<IRElement> constructorElements = new ArrayList<IRElement>();
      constructorElements.add( buildAssignment( tempSymbol, constructorCall ) );
      constructorElements.addAll( ExpressionTransformer.compileInitializer( initializer, _cc(), identifier( tempSymbol ) ) );
      constructorElements.add( identifier( tempSymbol ) );
      constructorCall = new IRCompositeExpression( constructorElements );
    }

    return constructorCall;
  }

  private IRExpression compileTypeVarConstructorCall()
  {
    IType type = _expr().getType();

    IRExpression constructorCall;

    List<IRElement> newExprElements;

    List<IRExpression> explicitArgs = new ArrayList<IRExpression>();
    pushArgumentsDirectly( _expr().getArgs(), explicitArgs );
    newExprElements = handleNamedArgs( explicitArgs, _expr().getNamedArgOrder() );

    // Call the IConstructorInfo dynamically
    constructorCall = callTypeVarConstructorInfo( type, _expr().getArgs(), explicitArgs );

    if( newExprElements.size() > 0 )
    {
      // Include temp var assignments so named args are evaluated in lexical order before the ctor call
      newExprElements.add( constructorCall );
      constructorCall = new IRCompositeExpression( newExprElements );
    }

    IInitializerExpression initializer = _expr().getInitializer();
    if( initializer != null )
    {
      // If there's an initializer, save the result of the constructor to a temp symbol, execute the initializer
      // statements, and then load the temp symbol back so it's the result of the expression
      IRSymbol tempSymbol = _cc().makeAndIndexTempSymbol( constructorCall.getType() );
      List<IRElement> constructorElements = new ArrayList<IRElement>();
      constructorElements.add( buildAssignment( tempSymbol, constructorCall ) );
      constructorElements.addAll( ExpressionTransformer.compileInitializer( initializer, _cc(), identifier( tempSymbol ) ) );
      constructorElements.add( identifier( tempSymbol ) );
      constructorCall = new IRCompositeExpression( constructorElements );
    }

    return constructorCall;
  }

  private void pushArgumentsDirectly( IExpression[] args, List<IRExpression> irArgs )
  {
    if( args != null )
    {
      for( int i = 0; i < args.length; i++ )
      {
        IExpression arg = args[i];
        IRExpression irArg = ExpressionTransformer.compile( arg, _cc() );
        irArgs.add( irArg );
      }
    }
  }

  private IRExpression callConstructorInfo( IType rootType, IConstructorInfo ci, List<IRExpression> explicitArgs )
  {
    //
    // rootType
    // .getTypeInfo()
    // .getConstructor( argTypes )
    // .getConstructor()
    // .newInstance( args )
    //

    IRExpression typeInfo = callMethod( IType.class, "getTypeInfo", new Class[0], pushType( rootType ), exprList() );

    IRExpression constructorInfo;
    boolean relativeTypeInfo = ci.getOwnersType().getTypeInfo() instanceof IRelativeTypeInfo;
    if( relativeTypeInfo )
    {
      typeInfo = checkCast( IRelativeTypeInfo.class, typeInfo );
      constructorInfo = callMethod( IRelativeTypeInfo.class, "getConstructor", new Class[]{IType.class, IType[].class},
                                    typeInfo,
                                    exprList( pushType( rootType ), pushParamTypes( ci.getParameters() )));
    }
    else
    {
      constructorInfo = callMethod( ITypeInfo.class, "getConstructor", new Class[]{IType[].class},
                                    typeInfo,
                                    exprList( pushParamTypes( ci.getParameters() ) ) );
    }

    IRExpression constructorHandler = callMethod( IConstructorInfo.class, "getConstructor", new Class[0], constructorInfo, exprList() );

    IRExpression instance = callMethod( IConstructorHandler.class, "newInstance", new Class[]{Object[].class},
                                        constructorHandler,
                                        exprList( collectArgsIntoObjArray( explicitArgs ) ) );

    return checkCast( rootType, instance );
  }

  private IRExpression callTypeVarConstructorInfo( IType rootType, IExpression[] params, List<IRExpression> explicitArgs )
  {
    //
    // rootType
    // .getTypeInfo()
    // .getConstructor( argTypes )
    // .getConstructor()
    // .newInstance( args )
    //

    IRExpression typeInfo = callMethod( IType.class, "getTypeInfo", new Class[0], pushType( rootType ), exprList() );

    IRExpression constructorInfo;

    typeInfo = checkCast( IRelativeTypeInfo.class, typeInfo );
    constructorInfo = callMethod( IRelativeTypeInfo.class, "getConstructor", new Class[]{IType.class, IType[].class},
                                  typeInfo,
                                  exprList( pushType( rootType ), pushParamTypes( params )));

    IRExpression constructorHandler = callMethod( IConstructorInfo.class, "getConstructor", new Class[0], constructorInfo, exprList() );

    IRExpression instance = callMethod( IConstructorHandler.class, "newInstance", new Class[]{Object[].class},
                                        constructorHandler,
                                        exprList( collectArgsIntoObjArray( explicitArgs ) ) );

    return checkCast( rootType, instance );
  }

  private IRExpression pushParamTypes( IExpression[] params )
  {
    if( params == null || params.length == 0 )
    {
      return pushNull();
    }
    else
    {
      return pushArrayOfTypes( getTypes( params ) );
    }
  }

  private IType[] getTypes( IExpression[] params )
  {
    IType[] types = new IType[params.length];
    for( int i = 0; i < params.length; i++ )
    {
      types[i] = params[i].getType();
    }
    return types;
  }
}

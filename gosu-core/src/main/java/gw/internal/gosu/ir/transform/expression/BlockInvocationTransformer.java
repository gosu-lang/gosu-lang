/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.BlockInvocation;
import gw.lang.ir.IRElement;
import gw.lang.ir.IRExpression;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.compiler.FunctionClassUtil;
import gw.lang.ir.expression.IRCompositeExpression;
import gw.lang.parser.IExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaType;

import java.util.ArrayList;
import java.util.List;

public class BlockInvocationTransformer extends AbstractExpressionTransformer<BlockInvocation>
{
  public static IRExpression compile( TopLevelTransformationContext cc, BlockInvocation expr )
  {
    BlockInvocationTransformer compiler = new BlockInvocationTransformer( cc, expr );
    return compiler.compile();
  }

  private BlockInvocationTransformer( TopLevelTransformationContext cc, BlockInvocation expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    List<IRExpression> explicitArgs = boxArgs();
    List<IRElement> callElements = handleNamedArgs( explicitArgs, _expr().getNamedArgOrder() );

    IRExpression root = ExpressionTransformer.compile( _expr().getRoot(), _cc() );
    IJavaType interfaceForArity = FunctionClassUtil.getFunctionInterfaceForArity(_expr().getArgs().size());
    IRExpression call = callMethod(interfaceForArity.getBackingClassInfo(),
        "invoke", FunctionClassUtil.getArgArrayForArity(_expr().getArgs().size()),
        root, explicitArgs);
    IType returnType = _expr().getType();
    if( returnType.isPrimitive() )
    {
      call = unboxValueToType( returnType, call );
    }
    else
    {
      call = checkCast( returnType, call );
    }
    if( callElements.size() > 0 )
    {
      // Include temp var assignments so named args are evaluated in lexical order before the call
      callElements.add( call );
      call = new IRCompositeExpression( callElements );
    }
    return call;
  }

  private List<IRExpression> boxArgs()
  {
    List<IRExpression> irArgs = new ArrayList<IRExpression>();
    for( IExpression arg : _expr().getArgs() )
    {
      IRExpression irArg = ExpressionTransformer.compile( arg, _cc() );
      irArgs.add( boxValue( arg.getType(), irArg ) );
    }
    return irArgs;
  }
}

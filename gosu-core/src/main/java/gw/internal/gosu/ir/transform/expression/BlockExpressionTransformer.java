/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.nodes.JavaClassIRType;
import gw.internal.gosu.parser.expressions.BlockExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.expression.IRCompositeExpression;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.parser.IBlockClass;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.parser.expressions.IBlockExpression;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IType;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


public class BlockExpressionTransformer extends AbstractExpressionTransformer<IBlockExpression>
{
  public BlockExpressionTransformer( TopLevelTransformationContext cc, IBlockExpression parsedElem )
  {
    super( cc, parsedElem );
  }

  protected IRExpression compile_impl()
  {
    IBlockExpression blk = getParsedElement();

    IBlockClass blockClazz = blk.getBlockGosuClass();

    List<IRExpression> args = new ArrayList<IRExpression>();
    if( !blockClazz.isStatic() )
    {
      args.add( pushThisOrOuter( blockClazz.getEnclosingType() ) );
    }
    pushCapturedSymbols( blockClazz, args, false );
    int typeParams = pushTypeParametersForConstructor( _expr(), blockClazz, args );

    List<IRType> paramTypes = Arrays.asList( getConstructorParamTypes( new IType[0], typeParams, blockClazz ) );

    IRExpression newExpr = buildNewExpression( getDescriptor( blockClazz ), paramTypes, args );

    newExpr.setImplicit( true );
    return newExpr;
  }

  public static IRExpression compile( TopLevelTransformationContext cc, BlockExpression blockExpression )
  {
    BlockExpressionTransformer compiler = new BlockExpressionTransformer( cc, blockExpression );
    return compiler.compile();
  }
}

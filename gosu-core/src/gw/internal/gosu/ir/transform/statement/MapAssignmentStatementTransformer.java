/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.parser.statements.MapAssignmentStatement;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;

import java.util.AbstractMap;
import java.util.Map;

/**
 */
public class MapAssignmentStatementTransformer extends AbstractStatementTransformer<MapAssignmentStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, MapAssignmentStatement stmt )
  {
    MapAssignmentStatementTransformer gen = new MapAssignmentStatementTransformer( cc, stmt );
    return gen.compile();
  }

  private MapAssignmentStatementTransformer( TopLevelTransformationContext cc, MapAssignmentStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    // Push the Map value
    IRExpression root = ExpressionTransformer.compile( _stmt().getMapAccessExpression().getRootExpression(), _cc() );

    // Null check
//    Label endLabel = new Label();
//    mv.visitInsn( Opcodes.DUP );
//    Label nullLabel = new Label();
//    mv.visitJumpInsn( Opcodes.IFNULL, nullLabel );

    // Call Map.put( k, v )
    Class clsMap = getMapType();
    IRExpression key = ExpressionTransformer.compile( _stmt().getMapAccessExpression().getKeyExpression(), _cc() );
    IRExpression value = ExpressionTransformer.compile( _stmt().getExpression(), _cc() );
    IRExpression putCall = callMethod( clsMap, "put", new Class[]{Object.class, Object.class},
            root,
            exprList( key, value ) );
    return buildMethodCall( putCall );

//    mv.visitInsn( Opcodes.POP );  // put returns a value, so it must be popped
//    mv.visitJumpInsn( Opcodes.GOTO, endLabel );

    // Throw exception for null map
//    mv.visitLabel( nullLabel );
//    mv.visitInsn( Opcodes.POP );
//    pushConstant( "Null reference for map." );
//    callStaticMethod( EvaluationException.class, "create", String.class );
//    mv.visitInsn( Opcodes.ATHROW );
//
//    mv.visitLabel( endLabel );
  }

  private Class getMapType()
  {
    Class clsMap;
    if( JavaTypes.getJreType(AbstractMap.class).isAssignableFrom( _stmt().getMapAccessExpression().getRootExpression().getType() ) )
    {
      // Use class instead of interface i.e., invokevirtual is faster than invokeinterface
      clsMap = AbstractMap.class;
    }
    else
    {
      clsMap = Map.class;
    }
    return clsMap;
  }}
/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRType;
import gw.internal.gosu.ir.nodes.IRProperty;
import gw.internal.gosu.ir.nodes.IRPropertyFactory;
import gw.lang.ir.expression.IRNullLiteral;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.CapturedSymbol;
import gw.internal.gosu.parser.DynamicPropertySymbol;
import gw.internal.gosu.parser.DynamicSymbol;
import gw.internal.gosu.parser.ScopedDynamicSymbol;
import gw.internal.gosu.parser.AbstractDynamicSymbol;
import gw.lang.parser.ISymbol;
import gw.lang.parser.statements.IAssignmentStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IExternalSymbolMap;

import java.util.Arrays;

/**
 */
public class AssignmentStatementTransformer extends AbstractStatementTransformer<IAssignmentStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, IAssignmentStatement stmt )
  {
    AssignmentStatementTransformer gen = new AssignmentStatementTransformer( cc, stmt );
    return gen.compile();
  }

  private AssignmentStatementTransformer( TopLevelTransformationContext cc, IAssignmentStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    ISymbol symbol = _stmt().getIdentifier().getSymbol();
    IType type = symbol.getType();

    if ( _cc().isExternalSymbol( symbol.getName() ) )
    {
      IRExpression setterCall = callMethod( IExternalSymbolMap.class, "setValue", new Class[]{String.class, Object.class},
                      pushExternalSymbolsMap(),
                      Arrays.asList( pushConstant( symbol.getName() ),
                                     boxValue( _stmt().getExpression().getType(), ExpressionTransformer.compile( _stmt().getExpression(), _cc() ) ) ) );
      return buildMethodCall( setterCall );
    }
    else if( symbol instanceof ScopedDynamicSymbol )
    {
      return setScopedSymbolValue( symbol, _stmt().getExpression() );
    }
    else if( symbol instanceof DynamicPropertySymbol )
    {
      // Lhs is a Property
      final DynamicPropertySymbol dps = (DynamicPropertySymbol)symbol;
      IRProperty irProperty = IRPropertyFactory.createIRProperty(dps);
      IRExpression rhsValue = transformRHS( symbol );
      IRExpression root= pushRoot(dps, irProperty);
      return buildMethodCall( callMethod( irProperty.getSetterMethod(), root, exprList( rhsValue ) ) );
    }
    else
    {
      IRExpression rhsValue = transformRHS( symbol );
      if( symbol instanceof DynamicSymbol )
      {
        // Lhs is a Field
        DynamicSymbol field = (DynamicSymbol)symbol;
        IRProperty irProperty = IRPropertyFactory.createIRProperty( symbol );
        IRExpression root = pushRoot(field, irProperty);
        return setField( irProperty, root, rhsValue );
      }
      else if( symbol instanceof CapturedSymbol )
      {
        // Captured symbol is stored as a Field on an anonymous inner class (one elem array of symbol's type)
        // e.g., val$myFiield[0] = value
        IRProperty irProp = IRPropertyFactory.createIRProperty( getGosuClass(), symbol );
        return buildArrayStore( getField( irProp, pushThis() ),
              numericLiteral(0), rhsValue, irProp.getType().getComponentType() );
      }
      else if( symbol.getIndex() >= 0 )
      {
        // Lhs is a local var

        if( symbol.isValueBoxed() )
        {
          // Local var is captured in an anonymous inner class.
          // Symbol's value maintained as a one elem array of symbol's type.
          return buildArrayStore( identifier( _cc().getSymbol( symbol.getName() ) ),
                                  numericLiteral( 0 ),
                                  rhsValue,
                                  getDescriptor( type ) );
        }
        else
        {
          // Simple local var
          return buildAssignment( _cc().getSymbol( symbol.getName() ),
                                  rhsValue );
        }
      } else {
        throw new IllegalStateException("Found a symbol that we didn't know how to handle");
      }
    }
  }

  private IRExpression pushRoot(AbstractDynamicSymbol dps, IRProperty irProperty) {
    IRExpression root;
    if ( irProperty.isStatic() ) {
      root = null;
    } else if ( isMemberOnEnclosingType( dps ) != null ) {
      root = pushOuter( dps.getGosuClass() );
    } else {
      root = pushThis();
    }
    return root;
  }

  private IRExpression transformRHS(ISymbol symbol) {
    IRExpression rhsValue = ExpressionTransformer.compile( _stmt().getExpression(), _cc() );
    IRType symbolType = getDescriptor(symbol.getType());
    if (!(rhsValue instanceof IRNullLiteral) && !symbolType.isAssignableFrom(rhsValue.getType())) {
      rhsValue = buildCast( symbolType, rhsValue );
    }
    return rhsValue;
  }
}
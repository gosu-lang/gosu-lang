/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.ExistsExpression;
import gw.internal.gosu.parser.Expression;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRTypeConstants;
import gw.lang.ir.expression.IRCompositeExpression;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.ir.statement.IRBreakStatement;
import gw.lang.ir.statement.IRForEachStatement;
import gw.lang.ir.statement.IRStatementList;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.ir.transform.statement.ForEachStatementTransformer;

public class ExistsExpressionTransformer extends AbstractExpressionTransformer<ExistsExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, ExistsExpression expr )
  {
    ExistsExpressionTransformer compiler = new ExistsExpressionTransformer( cc, expr );
    return compiler.compile();
  }

  private ExistsExpressionTransformer( TopLevelTransformationContext cc, ExistsExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    _cc().pushScope( false );
    try
    {

      // boolean to hold if an element matches
      IRSymbol booleanVal = _cc().makeAndIndexTempSymbol(IRTypeConstants.pBOOLEAN());
      IRAssignmentStatement initBoolean = buildAssignment( booleanVal, booleanLiteral( false ) );


      // construct for loop
      Expression inExpression = _expr().getInExpression();
      IRForEachStatement loop = ForEachStatementTransformer.makeLoop( _cc(),
                                                                      ExpressionTransformer.compile( inExpression, _cc() ),
                                                                      inExpression.getType(),
                                                                      _expr().getIdentifier(),
                                                                      _expr().getIndexIdentifier() );
      
      // body of loop: if the value matches, set boolean to true and break
      loop.setBody( buildIf( ExpressionTransformer.compile( _expr().getWhereExpression(), _cc() ),
                                    new IRStatementList( false, buildAssignment( booleanVal, booleanLiteral( true ) ),
                                                            new IRBreakStatement() ) ) );

      //Tie it all up in a Composite expression
      IRCompositeExpression expression = new IRCompositeExpression();
      expression.addElement( initBoolean );
      expression.addElement( loop );
      expression.addElement( identifier( booleanVal ) ); // the return value is the value of the boolean after the loop has executed
      return expression;
    }
    finally
    {
      _cc().popScope();
    }
  }
}

/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.parser.BeanAccess;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.statements.CaseClause;
import gw.internal.gosu.parser.statements.SwitchStatement;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.statements.VarInitializationVerifier;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRExpression;
import gw.lang.ir.statement.IRBreakStatement;
import gw.lang.ir.statement.IRCaseClause;
import gw.lang.ir.statement.IRSwitchStatement;
import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;
import gw.lang.parser.statements.ICaseClause;
import gw.lang.parser.statements.ISwitchStatement;
import gw.lang.reflect.IEnumType;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 */
public class SwitchStatementTransformer extends AbstractStatementTransformer<SwitchStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, SwitchStatement stmt )
  {
    SwitchStatementTransformer compiler = new SwitchStatementTransformer( cc, stmt );
    return compiler.compile();
  }

  private SwitchStatementTransformer( TopLevelTransformationContext cc, SwitchStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    IType switchType = _stmt().getSwitchExpression().getType();

    // To initialize the switch statement, we assign the root to a temp variable
    IRExpression root = ExpressionTransformer.compile( _stmt().getSwitchExpression(), _cc() );
    IRSymbol tempRoot = _cc().makeAndIndexTempSymbol( root.getType() );
    IRStatement init = buildAssignment( tempRoot, root );

    List<IRCaseClause> irCases = new ArrayList<IRCaseClause>();

    CaseClause[] cases = _stmt().getCases();
    if( cases != null && cases.length > 0 )
    {
      for( int i = 0; i < cases.length; i++ )
      {
        Expression caseExpression = cases[i].getExpression();
        IRExpression caseTest;
        if( (isIntType( switchType ) || switchType == JavaTypes.pBOOLEAN()) &&
            (isIntType( caseExpression.getType() ) || caseExpression.getType() == JavaTypes.pBOOLEAN()) )
        {
          caseTest = compileCaseExpr_int( tempRoot, caseExpression );
        }
        else
        {
          caseTest = compileCaseExpr_ref( switchType, tempRoot, caseExpression );
        }

        List<Statement> caseStatements = cases[i].getStatements();
        List<IRStatement> irCaseStatements = new ArrayList<IRStatement>();
        if( caseStatements != null )
        {
          _cc().pushScope( false );
          try
          {
            for( Statement caseStatement : caseStatements )
            {
              irCaseStatements.add( _cc().compile( caseStatement ) );
            }
          }
          finally
          {
            _cc().popScope();
          }
        }

        irCases.add( new IRCaseClause( caseTest, irCaseStatements ) );
      }
    }

    List<Statement> defaultStmts = _stmt().getDefaultStatements();
    List<IRStatement> irDefaultStatements = new ArrayList<IRStatement>();
    if( defaultStmts != null )
    {
      // Push a scope so that locals don't bleed through to the next case
      for( Statement defaultStatement : defaultStmts )
      {
        irDefaultStatements.add( _cc().compile( defaultStatement ) );
      }
    }
    else if( _stmt().isCoveredEnumSwitch() )
    {
      // If all enum constants are covered by case-clauses and no default case is explicitly provided,
      // generate a default to throw NPE for unhandled null case and IllegalStateException for new constants
      // potentially added later
      IRCaseClause lastIrCase = irCases.get( irCases.size() - 1 );
      ICaseClause lastCase = cases[cases.length-1];
      List<? extends IStatement> statements = lastCase.getStatements();
      if( !VarInitializationVerifier.doStatementsTerminate( statements ) )
      {
        // Jump over implicit default clause we're about to generate
        lastIrCase.getStatements().add( new IRBreakStatement() );
      }
      irDefaultStatements.add( buildIfElse( buildEquals( identifier( tempRoot ), pushNull() ),
                                            buildThrow( buildNewExpression( NullPointerException.class, new Class[] {String.class}, Collections.singletonList( pushConstant( "null case unhandled for enum type" ) ) ) ),
                                            buildThrow( buildNewExpression( IllegalStateException.class, new Class[] {String.class}, Collections.singletonList( pushConstant( "Enum constant unhandled, recompile with new version of enum class" ) ) ) ) ) );
    }

    return new IRSwitchStatement( init, irCases, irDefaultStatements );
  }

  private IRExpression compileCaseExpr_int( IRSymbol tempRoot, Expression caseExpression )
  {
    return buildEquals( identifier( tempRoot ), ExpressionTransformer.compile( caseExpression, _cc() ) );
  }

  private IRExpression compileCaseExpr_ref( IType switchType, IRSymbol tempRoot, Expression caseExpression )
  {
    if( switchType == caseExpression.getType() && isBytecodeType( switchType ) )
    {
      return callStaticMethod( getClass(), "areEqual", new Class[]{Object.class, Object.class},
                exprList( identifier( tempRoot ), ExpressionTransformer.compile( caseExpression, _cc() ) ) );
    }
    else
    {
      return callStaticMethod( BeanAccess.class, "areValuesEqual", new Class[]{IType.class, Object.class, IType.class, Object.class},
              exprList(
                      pushType( switchType ),
                      identifier( tempRoot ),
                      pushType( caseExpression.getType() ),
                      ExpressionTransformer.compile( caseExpression, _cc() )
              ));
    }
  }

  public static boolean areEqual( Object p1, Object p2 )
  {
    return (p1 == null && p2 == null) ||
           (p1 != null && p2 != null && p1.equals( p2 ));
  }
}
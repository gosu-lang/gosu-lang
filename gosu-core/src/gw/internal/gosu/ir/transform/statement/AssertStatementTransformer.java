/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.statements.AssertStatement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRTypeConstants;

import java.util.Collections;

/**
 */
public class AssertStatementTransformer extends AbstractStatementTransformer<AssertStatement>
{
  public static final String $_ASSERTIONS_DISABLED = "$assertionsDisabled";

  public static IRStatement compile( TopLevelTransformationContext cc, AssertStatement stmt )
  {
    AssertStatementTransformer compiler = new AssertStatementTransformer( cc, stmt );
    return compiler.compile();
  }

  private AssertStatementTransformer( TopLevelTransformationContext cc, AssertStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    // Ensure our enclosing class has the static $assertionsDisabled field defined
    _cc().addAssertionsStaticField();

    // if( !MyClass.$assertionsDisabled ) {
    //   if( !<assert-condition> ) {
    //     throw new AssertionError( [assert-detail] )
    //   }
    // }
    return
      buildIf( buildEquals( buildFieldGet( _cc().getIRTypeForCurrentClass(), $_ASSERTIONS_DISABLED, IRTypeConstants.pBOOLEAN(), null ), booleanLiteral( false ) ),
               buildIf( buildEquals( ExpressionTransformer.compile( _stmt().getCondition(), _cc() ), booleanLiteral( false ) ),
                       _stmt().getDetail() == null
                       ? buildThrow( buildNewExpression( AssertionError.class, new Class[0],
                                                         Collections.<IRExpression>emptyList() ) )
                       : buildThrow( buildNewExpression( AssertionError.class, new Class[] {Object.class},
                                                         Collections.singletonList( ExpressionTransformer.compile( _stmt().getDetail(), _cc() ) ) ) ) ) );
  }
}
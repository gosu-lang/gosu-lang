/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.java;

import gw.lang.ir.IRClass;
import gw.lang.ir.IRElement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.lang.ir.expression.IRArithmeticExpression;
import gw.lang.ir.expression.IRArrayLengthExpression;
import gw.lang.ir.expression.IRArrayLoadExpression;
import gw.lang.ir.expression.IRBooleanLiteral;
import gw.lang.ir.expression.IRCastExpression;
import gw.lang.ir.expression.IRCharacterLiteral;
import gw.lang.ir.expression.IRClassLiteral;
import gw.lang.ir.expression.IRCompositeExpression;
import gw.lang.ir.expression.IRConditionalAndExpression;
import gw.lang.ir.expression.IRConditionalOrExpression;
import gw.lang.ir.expression.IREqualityExpression;
import gw.lang.ir.expression.IRFieldGetExpression;
import gw.lang.ir.expression.IRIdentifier;
import gw.lang.ir.expression.IRInstanceOfExpression;
import gw.lang.ir.expression.IRLazyTypeMethodCallExpression;
import gw.lang.ir.expression.IRMethodCallExpression;
import gw.lang.ir.expression.IRNegationExpression;
import gw.lang.ir.expression.IRNewArrayExpression;
import gw.lang.ir.expression.IRNewExpression;
import gw.lang.ir.expression.IRNewMultiDimensionalArrayExpression;
import gw.lang.ir.expression.IRNoOpExpression;
import gw.lang.ir.expression.IRNotExpression;
import gw.lang.ir.expression.IRNullLiteral;
import gw.lang.ir.expression.IRNumericLiteral;
import gw.lang.ir.expression.IRPrimitiveTypeConversion;
import gw.lang.ir.expression.IRRelationalExpression;
import gw.lang.ir.expression.IRStringLiteralExpression;
import gw.lang.ir.expression.IRTernaryExpression;
import gw.lang.ir.statement.IRArrayStoreStatement;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.ir.statement.IRBreakStatement;
import gw.lang.ir.statement.IRCaseClause;
import gw.lang.ir.statement.IRCatchClause;
import gw.lang.ir.statement.IRContinueStatement;
import gw.lang.ir.statement.IRDoWhileStatement;
import gw.lang.ir.statement.IREvalStatement;
import gw.lang.ir.statement.IRFieldDecl;
import gw.lang.ir.statement.IRFieldSetStatement;
import gw.lang.ir.statement.IRForEachStatement;
import gw.lang.ir.statement.IRIfStatement;
import gw.lang.ir.statement.IRMethodCallStatement;
import gw.lang.ir.statement.IRMethodStatement;
import gw.lang.ir.statement.IRMonitorLockAcquireStatement;
import gw.lang.ir.statement.IRMonitorLockReleaseStatement;
import gw.lang.ir.statement.IRNewStatement;
import gw.lang.ir.statement.IRNoOpStatement;
import gw.lang.ir.statement.IRReturnStatement;
import gw.lang.ir.statement.IRStatementList;
import gw.lang.ir.statement.IRSwitchStatement;
import gw.lang.ir.statement.IRSyntheticStatement;
import gw.lang.ir.statement.IRThrowStatement;
import gw.lang.ir.statement.IRTryCatchFinallyStatement;
import gw.lang.ir.statement.IRWhileStatement;

import java.lang.reflect.Modifier;
import java.util.List;

public class IRJavaCompiler {
  private StringBuilder _output = new StringBuilder();
  private int _indent;

  public IRJavaCompiler() {
  }

  public StringBuilder getOutput() {
    return _output;
  }

  public void compileClassStatement(IRClass irClass) {
    _output.append("// Compiled from ").append(irClass.getSourceFile()).append("\n");
    _output.append(getModifierString(irClass.getModifiers())).append(irClass.getName());
    if (irClass.getSuperType() != null) {
      _output.append(" extends ").append(irClass.getSuperType().getRelativeName());
    }

    if (!irClass.getInterfaces().isEmpty()) {
      _output.append(" implements ").append(joinTypeNames(irClass.getInterfaces()));
      _output.append(" ");
    }

    _output.append("{\n");

    // Inner classes

    pushIndent();

    // Fields
    for (IRFieldDecl fieldDecl : irClass.getFields()) {
      compileIRFieldDecl(fieldDecl);
    }

    _output.append("\n");

    // Methods
    for (IRMethodStatement methodStatement : irClass.getMethods()) {
      compileIRMethodStatement(methodStatement);
      _output.append("\n");
    }

    popIndent();

    _output.append("}");
  }

  private void compileIRElement(IRElement element) {
    if (element instanceof IRStatement) {
      compileIRStatement((IRStatement) element);
    } else {
      compileIRExpression((IRExpression) element);
    }
  }

  private void compileIRStatement(IRStatement statement) {
    if (statement == null) {
      return;
    }

    if (statement.getOriginalSourceStatement() != null) {
      appendOriginalSourceComment(statement.getOriginalSourceStatement());
    }

    if (statement instanceof IRAssignmentStatement) {
      compileIRAssignmentStatement((IRAssignmentStatement) statement);
    } else if (statement instanceof IRFieldDecl) {
      compileIRFieldDecl((IRFieldDecl) statement);
    } else if (statement instanceof IRFieldSetStatement) {
      compileIRFieldSetStatement((IRFieldSetStatement) statement);
    } else if (statement instanceof IRIfStatement) {
      compileIRIfStatement((IRIfStatement) statement);
    } else if (statement instanceof IRMethodCallStatement) {
      compileIRMethodCallStatement((IRMethodCallStatement) statement);
    } else if (statement instanceof IRNewStatement ) {
      compileIRNewStatement( (IRNewStatement)statement );
    } else if (statement instanceof IRMethodStatement) {
      compileIRMethodStatement( (IRMethodStatement)statement );
    } else if (statement instanceof IRNoOpStatement) {
      compileIRNoOpStatement( (IRNoOpStatement)statement );
    } else if (statement instanceof IRReturnStatement) {
      compileIRReturnStatement( (IRReturnStatement)statement );
    } else if (statement instanceof IRStatementList) {
      compileIRStatementList( (IRStatementList)statement );
    } else if (statement instanceof IRArrayStoreStatement) {
      compileIRArrayStoreStatement( (IRArrayStoreStatement)statement );
    } else if (statement instanceof IRThrowStatement) {
      compileIRThrowStatement( (IRThrowStatement)statement );
    } else if (statement instanceof IRTryCatchFinallyStatement) {
      compileIRTryCatchFinallyStatement( (IRTryCatchFinallyStatement)statement );
    } else if (statement instanceof IRSyntheticStatement) {
      compileIRSyntheticStatement( (IRSyntheticStatement)statement );
    } else if (statement instanceof IRWhileStatement) {
      compileIRWhileStatement( (IRWhileStatement)statement );
    } else if (statement instanceof IRDoWhileStatement) {
      compileIRDoWhileStatement( (IRDoWhileStatement)statement );
    } else if (statement instanceof IRForEachStatement) {
      compileIRForEachStatement( (IRForEachStatement)statement );
    } else if (statement instanceof IRSwitchStatement) {
      compileIRSwitchStatement( (IRSwitchStatement)statement );
    } else if (statement instanceof IRBreakStatement) {
      compileIRBreak( (IRBreakStatement)statement );
    } else if (statement instanceof IRContinueStatement) {
      compileIRContinue( (IRContinueStatement)statement );
    } else if (statement instanceof IREvalStatement) {
      compileIREvalStatement( (IREvalStatement)statement );
    } else if (statement instanceof IRMonitorLockAcquireStatement) {
      compileIRMonitorLockAcquireStatement((IRMonitorLockAcquireStatement) statement);
    } else if (statement instanceof IRMonitorLockReleaseStatement ) {
      compileIRMonitorLockReleaseStatement((IRMonitorLockReleaseStatement) statement);
    } else {
      throw new IllegalArgumentException("Unrecognized statement of type " + statement.getClass());
    }
  }

  private void compileIREvalStatement( IREvalStatement statement ) {
    appendIndent();
    _output.append( "eval( " );
    compileIRElement( statement.getExpression() );
    _output.append( " )" );
  }

  private void compileIRDoWhileStatement(IRDoWhileStatement irDoWhileStatement) {
    appendIndent();
    _output.append("do {\n");
    pushIndent();
    compileIRElement(irDoWhileStatement.getBody());
    popIndent();
    appendIndent();
    _output.append("} while (");
    compileIRElement(irDoWhileStatement.getLoopTest());
    _output.append(")\n");
  }

  private void compileIRBreak( IRBreakStatement irBreakStatement )
  {
    appendIndent( );
    _output.append( "break;" );
  }

  private void compileIRContinue( IRContinueStatement irBreakStatement )
  {
    appendIndent( );
    _output.append( "continue;" );
  }

  private void compileIRMonitorLockAcquireStatement( IRMonitorLockAcquireStatement irMonitorLockAcquireStatement )
  {
    appendIndent( );
    _output.append( "synchronized( " );
    compileIRElement( irMonitorLockAcquireStatement.getMonitoredObject() );
    _output.append( " )" );
  }

  private void compileIRMonitorLockReleaseStatement( IRMonitorLockReleaseStatement irMonitorLockReleaseStatement )
  {
  }

  private void compileIRSwitchStatement(IRSwitchStatement irSwitchStatement ) {
    appendIndent();
    _output.append("switch(");
    compileIRElement(irSwitchStatement.getInit());
    _output.append(") {\n");

    pushIndent();
    for (IRCaseClause caseClause : irSwitchStatement.getCases()) {
      appendIndent();
      _output.append("case ");
      compileIRElement( caseClause.getCondition() );
      _output.append(":\n");

      
      pushIndent();
      for ( IRStatement statement : caseClause.getStatements() ) {
        compileIRElement( statement );
      }
      popIndent();
      
    }

    if (!irSwitchStatement.getDefaultStatements().isEmpty()) {
      appendIndent();
      _output.append("default:\n");
      pushIndent();
      for (IRStatement statement : irSwitchStatement.getDefaultStatements()) {
        compileIRElement( statement );
      }
      popIndent();
    }
    popIndent();
    appendIndent();
    _output.append("}\n");
  }

  private void compileIRForEachStatement(IRForEachStatement irForEachStatement ) {
    appendIndent( );
    _output.append( "/*foreach*/\n" );
    for(IRStatement initializer : irForEachStatement.getInitializers()) {
      appendIndent();
      compileIRElement(initializer);
      _output.append( "\n" );
    }
    if( irForEachStatement.hasIdentifierToNullCheck() )
    {
      appendIndent();
      _output.append( "if (" ).append( irForEachStatement.getIdentifierToNullCheck().getSymbol().getName() ).append( " != null) {\n" );
      pushIndent();
    }
    appendIndent();
    _output.append("while (");
    compileIRElement(irForEachStatement.getLoopTest());
    _output.append(") {\n");
    pushIndent();
    for(IRStatement initializer : irForEachStatement.getIncrementors()) {
      compileIRElement(initializer);
      _output.append( "\n" );
    }
    compileIRElement(irForEachStatement.getBody());
    popIndent();
    appendIndent();
    _output.append("}\n");
    if( irForEachStatement.hasIdentifierToNullCheck() )
    {
      popIndent();
      appendIndent();
      _output.append( "}\n" );
    }
  }

  private void compileIRWhileStatement(IRWhileStatement irWhileStatement) {
    appendIndent();
    _output.append("while (");
    compileIRElement(irWhileStatement.getLoopTest());
    _output.append(") {\n");
    pushIndent();
    compileIRElement(irWhileStatement.getBody());
    popIndent();
    appendIndent();
    _output.append("}\n");
  }

  private void compileIRSyntheticStatement(IRSyntheticStatement irSyntheticStatement) {
    appendIndent();
    compileIRElement(irSyntheticStatement.getExpression());
    _output.append(";\n");
  }

  private void compileIRTryCatchFinallyStatement(IRTryCatchFinallyStatement irTryCatchFinallyStatement) {
    appendIndent();
    _output.append("try {\n");
    pushIndent();
    compileIRElement(irTryCatchFinallyStatement.getTryBody());
    popIndent();
    appendIndent();
    _output.append("}\n");

    for (IRCatchClause catchClause : irTryCatchFinallyStatement.getCatchStatements()) {
      appendIndent();
      _output.append("catch( ").append(catchClause.getIdentifier().getType().getRelativeName()).append(" ").append(catchClause.getIdentifier().getName()).append(") {\n");
      pushIndent();
      compileIRElement(catchClause.getBody());
      popIndent();
      appendIndent();
      _output.append("}\n");
    }

    if (irTryCatchFinallyStatement.getFinallyBody() != null) {
      appendIndent();
      _output.append("finally {\n");
      pushIndent();
      compileIRElement(irTryCatchFinallyStatement.getFinallyBody());
      popIndent();
      appendIndent();
      _output.append("}\n");
    }
  }

  private void compileIRThrowStatement(IRThrowStatement irThrowStatement) {
    appendIndent();
    _output.append("throw ");
    compileIRElement(irThrowStatement.getException());
    _output.append(";\n");
  }

  private void compileIRArrayStoreStatement(IRArrayStoreStatement irArrayStoreStatement) {
    appendIndent();
    compileIRElement(irArrayStoreStatement.getTarget());
    _output.append("[");
    compileIRElement(irArrayStoreStatement.getIndex());
    _output.append("] = ");
    compileIRElement(irArrayStoreStatement.getValue());
    _output.append(";\n");
  }

  private void compileIRFieldSetStatement(IRFieldSetStatement irFieldSetStatement) {
    appendIndent();
    if (irFieldSetStatement.getLhs() == null) {
      _output.append(irFieldSetStatement.getOwnersType().getRelativeName());
    } else {
      compileIRElement(irFieldSetStatement.getLhs());
    }
    _output.append(".");
    _output.append(irFieldSetStatement.getName());
    _output.append(" = ");
    compileIRElement(irFieldSetStatement.getRhs());

    _output.append(";\n");
  }

  private void compileIRIfStatement(IRIfStatement irIfStatement) {
    appendIndent();
    _output.append("if (");
    compileIRElement(irIfStatement.getExpression());
    _output.append(") {\n");
    pushIndent();
    compileIRElement(irIfStatement.getIfStatement());
    popIndent();
    appendIndent();
    _output.append("}");
    if (irIfStatement.getElseStatement() != null) {
      _output.append(" else {\n");
      pushIndent();
      compileIRElement(irIfStatement.getElseStatement());
      popIndent();
      appendIndent();
      _output.append("}");
    }
    _output.append("\n");
  }

  private void compileIRMethodCallStatement(IRMethodCallStatement irMethodCallStatement) {
    appendIndent();
    compileIRElement(irMethodCallStatement.getExpression());
    _output.append(";\n");
  }

  private void compileIRNewStatement(IRNewStatement irNewExpr ) {
    appendIndent();
    compileIRElement( irNewExpr.getNewExpression());
    _output.append(";\n");
  }

  private void compileIRNoOpStatement(IRNoOpStatement irNoOpStatement) {
    _output.append("\n");
  }

  private void compileIRReturnStatement(IRReturnStatement irReturnStatement) {
    appendIndent();
    _output.append("return");
    if (irReturnStatement.getReturnValue() != null) {
      _output.append(" ");
      compileIRElement(irReturnStatement.getReturnValue());
    }
    _output.append(";\n");
  }

  private void compileIRStatementList(IRStatementList irStatementList) {
    for (IRStatement statement : irStatementList.getStatements()) {
      compileIRStatement(statement);
    }
  }

  private void compileIRAssignmentStatement(IRAssignmentStatement irAssignmentStatement) {
    appendIndent();
    _output.append(irAssignmentStatement.getSymbol().getName());
    _output.append(" = ");
    compileIRElement(irAssignmentStatement.getValue());
    _output.append(";\n");
  }

  private void compileIRExpression(IRExpression expression) {
    if (expression instanceof IRArithmeticExpression) {
      compileIRAdditiveExpression((IRArithmeticExpression) expression);
    } else if (expression instanceof IRArrayLoadExpression) {
      compileIRArrayLoadExpression((IRArrayLoadExpression) expression);
    } else if (expression instanceof IRBooleanLiteral) {
      compileIRBooleanLiteral((IRBooleanLiteral) expression);
    } else if (expression instanceof IRCompositeExpression) {
      compileIRCompositeExpression((IRCompositeExpression) expression);
    } else if (expression instanceof IREqualityExpression) {
      compileIREqualityExpression((IREqualityExpression) expression);
    } else if (expression instanceof IRFieldGetExpression) {
      compileIRFieldGetExpression((IRFieldGetExpression) expression);
    } else if (expression instanceof IRIdentifier) {
      compileIRIdentifier((IRIdentifier) expression);
    } else if (expression instanceof IRMethodCallExpression) {
      compileIRMethodCallExpression((IRMethodCallExpression) expression);
    } else if (expression instanceof IRLazyTypeMethodCallExpression ) {
      compileIRLazyTypeMethodCallExpression((IRLazyTypeMethodCallExpression) expression);
    } else if (expression instanceof IRNullLiteral) {
      compileIRNullLiteral((IRNullLiteral) expression);
    } else if (expression instanceof IRPrimitiveTypeConversion) {
      compileIRPrimitiveTypeConversion((IRPrimitiveTypeConversion) expression);
    } else if (expression instanceof IRTernaryExpression) {
      compileIRTernaryExpression((IRTernaryExpression) expression);
    } else if (expression instanceof IRNumericLiteral) {
      compileIRNumericLiteral((IRNumericLiteral) expression);
    } else if (expression instanceof IRStringLiteralExpression) {
      compileIRStringLiteralExpression((IRStringLiteralExpression) expression);
    } else if (expression instanceof IRNewArrayExpression) {
      compileIRNewArrayExpression((IRNewArrayExpression) expression);
    } else if (expression instanceof IRNewMultiDimensionalArrayExpression) {
      compileIRNewMultiDimenstionalArrayExpression( (IRNewMultiDimensionalArrayExpression)expression );
    } else if (expression instanceof IRArrayLengthExpression) {
      compileIRArrayLengthExpression( (IRArrayLengthExpression)expression );
    } else if (expression instanceof IRCastExpression) {
      compileIRCastExpression( (IRCastExpression)expression );
    } else if (expression instanceof IRNewExpression) {
      compileIRNewExpression( (IRNewExpression)expression );
    } else if (expression instanceof IRRelationalExpression) {
      compileIRRelationalExpression( (IRRelationalExpression)expression );
    } else if (expression instanceof IRClassLiteral) {
      compileIRClassLiteral( (IRClassLiteral)expression );
    } else if (expression instanceof IRNegationExpression) {
      compileIRNegationExpression( (IRNegationExpression)expression );
    } else if (expression instanceof IRConditionalOrExpression) {
      compileIRConditionalOrExpression( (IRConditionalOrExpression)expression );
    } else if (expression instanceof IRConditionalAndExpression) {
      compileIRConditionalAndExpression( (IRConditionalAndExpression)expression );
    } else if (expression instanceof IRNotExpression) {
      compileIRNotExpression( (IRNotExpression)expression );
    } else if (expression instanceof IRInstanceOfExpression) {
      compileIRInstanceOfExpression( (IRInstanceOfExpression)expression );
    } else if (expression instanceof IRCharacterLiteral) {
      compileIRCharacterLiteral((IRCharacterLiteral) expression);
    } else if (expression instanceof IRNoOpExpression) {
      // Do nothing
    } else {
      throw new IllegalArgumentException("Unrecognized expression of type " + expression.getClass());
    }
  }

  private void compileIRInstanceOfExpression(IRInstanceOfExpression irInstanceOfExpression) {
    _output.append("(");
    compileIRElement( irInstanceOfExpression.getRoot());
    _output.append(" instanceof ");
    _output.append(irInstanceOfExpression.getTestType().getRelativeName());
    _output.append(")");
  }

  private void compileIRNotExpression(IRNotExpression irNotExpression) {
    _output.append("!(");
    compileIRElement(irNotExpression.getRoot());
    _output.append(")");
  }

  private void compileIRConditionalAndExpression(IRConditionalAndExpression irConditionalAndExpression) {
    _output.append("(");
    compileIRElement(irConditionalAndExpression.getLhs());
    _output.append(" && ");
    compileIRElement(irConditionalAndExpression.getRhs());
    _output.append(")");
  }

  private void compileIRConditionalOrExpression(IRConditionalOrExpression irConditionalOrExpression) {
    _output.append("(");
    compileIRElement(irConditionalOrExpression.getLhs());
    _output.append(" || ");
    compileIRElement(irConditionalOrExpression.getRhs());
    _output.append(")");
  }

  private void compileIRNegationExpression(IRNegationExpression irNegationExpression) {
    _output.append("-");
    compileIRElement(irNegationExpression.getRoot());
  }

  private void compileIRClassLiteral(IRClassLiteral irClassLiteral) {
    _output.append(irClassLiteral.getLiteralType().getName()).append(".class");
  }

  private void compileIRRelationalExpression(IRRelationalExpression irRelationalExpression) {
    String op;
    switch(irRelationalExpression.getOp()) {
      case GT:
        op = " > ";
        break;
      case GTE:
        op = " >= ";
        break;
      case LT:
        op = " < ";
        break;
      case LTE:
        op = " <= ";
        break;
      default:
        throw new IllegalArgumentException("Unexpected operation " + irRelationalExpression.getOp());
    }
    compileIRElement(irRelationalExpression.getLhs());
    _output.append( op );
    compileIRElement(irRelationalExpression.getRhs());
  }

  private void compileIRNewExpression(IRNewExpression irNewExpression) {
    _output.append("new " + irNewExpression.getOwnersType().getRelativeName() + "(");
    for (int i = 0; i < irNewExpression.getArgs().size(); i++) {
      if (i > 0) {
        _output.append(", ");  
      }
      compileIRElement(irNewExpression.getArgs().get(i));
    }
    _output.append(")");
  }

  private void compileIRCastExpression(IRCastExpression irCastExpression) {
    _output.append("((").append(irCastExpression.getType().getRelativeName()).append(") ");
    compileIRElement(irCastExpression.getRoot());
    _output.append(")");
  }

  private void compileIRNewArrayExpression(IRNewArrayExpression irNewArrayExpression) {
    _output.append("new ").append(irNewArrayExpression.getComponentType().getRelativeName()).append("[");
    compileIRElement(irNewArrayExpression.getSizeExpression());
    _output.append("]");
  }

  private void compileIRNewMultiDimenstionalArrayExpression(IRNewMultiDimensionalArrayExpression expr ) {
    _output.append("new ").append( expr.getResultType().getRelativeName());
    for( IRExpression e: expr.getSizeExpressions() ) {
      _output.append( "[" );
      compileIRElement( e );
      _output.append("]");
    }
  }

  private void compileIRArrayLengthExpression(IRArrayLengthExpression irNewArrayExpression) {
    compileIRElement(irNewArrayExpression.getRoot());
    _output.append(".length");
  }

  private void compileIRStringLiteralExpression(IRStringLiteralExpression irStringLiteralExpression) {
    _output.append("\"").append(irStringLiteralExpression.getValue()).append("\"");
  }

  private void compileIRCharacterLiteral(IRCharacterLiteral irCharacterLiteral) {
    _output.append('\'');
    char literal = irCharacterLiteral.getValue();
    if(literal == '\'' || literal == '\\') {
      _output.append('\\');
    }
    _output.append(literal);
    _output.append('\'');
  }

  private void compileIRNumericLiteral(IRNumericLiteral irNumericLiteral) {
    _output.append(irNumericLiteral.getValue());
  }

  private void compileIRAdditiveExpression(IRArithmeticExpression irAdditiveExpression) {
    compileIRElement(irAdditiveExpression.getLhs());
    switch(irAdditiveExpression.getOp()) {
      case Addition:
        _output.append(" + ");
        break;
      case Subtraction:
        _output.append(" - ");
        break;
      case Division:
        _output.append(" / ");
        break;
      case Multiplication:
        _output.append(" * ");
        break;
      case Remainder:
        _output.append(" % ");
        break;
      case ShiftLeft:
        _output.append(" << ");
        break;
      case ShiftRight:
        _output.append(" >> ");
        break;
      case UnsignedShiftRight:
        _output.append(" >>> ");
        break;
      case BitwiseAnd:
        _output.append(" & ");
        break;
      case BitwiseXor:
        _output.append(" ^ ");
        break;
      case BitwiseOr:
        _output.append(" | ");
        break;
      default:
        throw new IllegalArgumentException("Unexpected operation " + irAdditiveExpression.getOp().toString());
    }
    compileIRElement(irAdditiveExpression.getRhs());
  }

  private void compileIRArrayLoadExpression(IRArrayLoadExpression irArrayLoadExpression) {
    compileIRElement(irArrayLoadExpression.getRoot());
    _output.append("[");
    compileIRElement(irArrayLoadExpression.getIndex());
    _output.append("]");
  }

  private void compileIRBooleanLiteral(IRBooleanLiteral irBooleanLiteral) {
    _output.append(irBooleanLiteral.getValue());
  }

  private void compileIRCompositeExpression(IRCompositeExpression irCompositeExpression) {
    _output.append("[[\n");
    pushIndent();
    for (IRElement element : irCompositeExpression.getElements()) {
      // Expressions don't do their own indentation.  Since the last element will be an expression, we need to do it ourselves
      // for that last statement.  Otherwise, allow the statement compilation to handle it.
      if (element instanceof IRExpression) {
        appendIndent();
      }
      compileIRElement(element);
    }
    _output.append("\n");
    popIndent();
    appendIndent();
    _output.append("]]");
  }

  private void compileIREqualityExpression(IREqualityExpression irEqualityExpression) {
    compileIRExpression(irEqualityExpression.getLhs());
    _output.append(irEqualityExpression.isEquals() ? " == " : " != ");
    compileIRExpression(irEqualityExpression.getRhs());
  }

  private void compileIRFieldGetExpression(IRFieldGetExpression irFieldGetExpression) {
    if (irFieldGetExpression.getLhs() == null) {
      _output.append(irFieldGetExpression.getOwnersType().getRelativeName());
    } else {
      compileIRElement(irFieldGetExpression.getLhs());
    }
    _output.append(".");
    _output.append(irFieldGetExpression.getName());
  }

  private void compileIRIdentifier(IRIdentifier irIdentifier) {
    _output.append(irIdentifier.getSymbol().getName());
  }

  private void compileIRMethodCallExpression(IRMethodCallExpression irMethodCallExpression) {
    if (irMethodCallExpression.getRoot() == null) {
      _output.append(irMethodCallExpression.getOwnersType().getRelativeName());
    } else {
      compileIRExpression(irMethodCallExpression.getRoot());
    }
    _output.append(".");
    _output.append(irMethodCallExpression.getName());
    _output.append("(");
    for (int i = 0; i < irMethodCallExpression.getArgs().size(); i++) {
      if (i > 0) {
        _output.append(", ");
      }
      compileIRElement(irMethodCallExpression.getArgs().get(i));
    }
    _output.append(")");
  }

  private void compileIRLazyTypeMethodCallExpression(IRLazyTypeMethodCallExpression irMethodCallExpression) {
    _output.append(irMethodCallExpression.getOwnerTypeName()).append( '.' ).append(irMethodCallExpression.getName());
  }

  private void compileIRNullLiteral(IRNullLiteral irNullLiteral) {
    _output.append("null");
  }

  private void compileIRPrimitiveTypeConversion(IRPrimitiveTypeConversion irPrimitiveTypeConversion) {
    _output.append("(").append(irPrimitiveTypeConversion.getToType().getRelativeName()).append(") ");
    compileIRElement(irPrimitiveTypeConversion.getRoot());
  }

  private void compileIRTernaryExpression(IRTernaryExpression irTernaryExpression) {
    _output.append("(");
    compileIRElement(irTernaryExpression.getTest());
    _output.append(" ? ");
    compileIRElement(irTernaryExpression.getTrueValue());
    _output.append(" : ");
    compileIRElement(irTernaryExpression.getFalseValue());
    _output.append(")");
  }

  private void compileIRFieldDecl(IRFieldDecl fieldDecl) {
    appendIndent();
    _output.append(getModifierString(fieldDecl.getModifiers())).append(fieldDecl.getType().getRelativeName()).append(" ").append(fieldDecl.getName());
    if (fieldDecl.getValue() != null) {
      _output.append(" = ").append(fieldDecl.getValue());
    }
    _output.append(";\n");
  }

  private void compileIRMethodStatement(IRMethodStatement methodStatement) {
    appendIndent();
    _output.append(getModifierString(methodStatement.getModifiers()));
    _output.append(methodStatement.getReturnType().getRelativeName()).append(" ");
    _output.append(methodStatement.getName());
    _output.append("(");
    _output.append(joinParameters(methodStatement.getParameters()));
    _output.append(") {\n");

    pushIndent();
    compileIRStatement(methodStatement.getMethodBody());
    popIndent();

    appendIndent();
    _output.append("}\n");
  }

  static final int BRIDGE = 0x00000040;
  static final int VARARGS = 0x00000080;
  static final int SYNTHETIC = 0x00001000;
  static final int ANNOTATION = 0x00002000;
  static final int ENUM = 0x00004000;

  private String getModifierString(int modifiers) {
    StringBuilder sb = new StringBuilder();
    if (Modifier.isPublic(modifiers)) {
      sb.append("public ");
    } else if (Modifier.isPrivate(modifiers)) {
      sb.append("private ");
    } else if (Modifier.isProtected(modifiers)) {
      sb.append("protected ");
    } else {
      sb.append("internal ");
    }

    if (Modifier.isAbstract(modifiers)) {
      sb.append("abstract ");
    }

    if (Modifier.isFinal(modifiers)) {
      sb.append("final ");
    }

    if (Modifier.isInterface(modifiers)) {
      sb.append("interface ");
    }

    if (Modifier.isNative(modifiers)) {
      sb.append("native ");
    }

    if (Modifier.isStatic(modifiers)) {
      sb.append("static ");
    }

//    if (Modifier.isSynchronized(modifiers)) {
//      sb.append("synchronized ");
//    }

    if (Modifier.isTransient(modifiers)) {
      sb.append("transient ");
    }

    if (Modifier.isVolatile(modifiers)) {
      sb.append("volatile ");
    }

    if ((modifiers & BRIDGE) != 0) {
      sb.append("bridge ");
    }

    if ((modifiers & VARARGS) != 0) {
      sb.append("varargs ");
    }

    if ((modifiers & SYNTHETIC) != 0) {
      sb.append("synthetic ");
    }

    if ((modifiers & ANNOTATION) != 0) {
      sb.append("annotation ");
    }

    if ((modifiers & ENUM) != 0) {
      sb.append("enum ");
    }

    return sb.toString();
  }

  private String joinTypeNames(List<IRType> types) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < types.size(); i++) {
      if (i > 0) {
        sb.append(", ");
      }
      sb.append(types.get(i).getRelativeName());
    }
    return sb.toString();
  }

  private String joinParameters(List<IRSymbol> symbols) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < symbols.size(); i++) {
      if (i > 0) {
        sb.append(", ");
      }
      sb.append(symbols.get(i).getType().getRelativeName()).append(" ").append(symbols.get(i).getName());
    }
    return sb.toString();
  }

  private void pushIndent() {
    _indent+=2;
  }

  private void popIndent() {
    _indent-=2;
  }

  private void appendIndent() {
    for (int i = 0; i < _indent; i++) {
      _output.append(" ");
    }
  }

  private void appendOriginalSourceComment(String originalSource) {
//    _output.append("\n");
//    appendIndent(indent);
//    _output.append("// ");
//    // Just append the first line
//    int lineBreak = originalSource.indexOf('\n');
//    if (lineBreak == -1) {
//      _output.append(originalSource);
//    } else {
//      _output.append(originalSource.substring(0, lineBreak));
//    }
//    _output.append("\n");
  }
}

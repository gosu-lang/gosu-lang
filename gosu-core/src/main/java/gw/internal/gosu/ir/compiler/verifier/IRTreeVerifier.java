/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.verifier;

import gw.lang.ir.IRClass;
import gw.lang.ir.IRType;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRElement;
import gw.lang.ir.IRTypeConstants;
import gw.lang.ir.expression.IRArithmeticExpression;
import gw.lang.ir.expression.IRArrayLengthExpression;
import gw.lang.ir.expression.IRArrayLoadExpression;
import gw.lang.ir.expression.IRBooleanLiteral;
import gw.lang.ir.expression.IRCharacterLiteral;
import gw.lang.ir.expression.IRIdentifier;
import gw.lang.ir.expression.IRFieldGetExpression;
import gw.lang.ir.expression.IREqualityExpression;
import gw.lang.ir.expression.IRCompositeExpression;
import gw.lang.ir.expression.IRMethodCallExpression;
import gw.lang.ir.expression.IRNullLiteral;
import gw.lang.ir.expression.IRPrimitiveTypeConversion;
import gw.lang.ir.expression.IRTernaryExpression;
import gw.lang.ir.expression.IRNumericLiteral;
import gw.lang.ir.expression.IRStringLiteralExpression;
import gw.lang.ir.expression.IRNewArrayExpression;
import gw.lang.ir.expression.IRCastExpression;
import gw.lang.ir.expression.IRNewExpression;
import gw.lang.ir.expression.IRRelationalExpression;
import gw.lang.ir.expression.IRNegationExpression;
import gw.lang.ir.expression.IRConditionalOrExpression;
import gw.lang.ir.expression.IRConditionalAndExpression;
import gw.lang.ir.expression.IRClassLiteral;
import gw.lang.ir.expression.IRNotExpression;
import gw.lang.ir.expression.IRInstanceOfExpression;
import gw.lang.ir.expression.IRNewMultiDimensionalArrayExpression;
import gw.lang.ir.expression.IRNoOpExpression;
import gw.lang.ir.statement.IREvalStatement;
import gw.lang.ir.statement.IRFieldDecl;
import gw.lang.ir.statement.IRMethodStatement;
import gw.lang.ir.statement.IRMethodCallStatement;
import gw.lang.ir.statement.IRIfStatement;
import gw.lang.ir.statement.IRFieldSetStatement;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.ir.statement.IRNoOpStatement;
import gw.lang.ir.statement.IRReturnStatement;
import gw.lang.ir.statement.IRStatementList;
import gw.lang.ir.statement.IRArrayStoreStatement;
import gw.lang.ir.statement.IRBreakStatement;
import gw.lang.ir.statement.IRThrowStatement;
import gw.lang.ir.statement.IRTryCatchFinallyStatement;
import gw.lang.ir.statement.IRCatchClause;
import gw.lang.ir.statement.IRSyntheticStatement;
import gw.lang.ir.statement.IRCaseClause;
import gw.lang.ir.statement.IRContinueStatement;
import gw.lang.ir.statement.IRForEachStatement;
import gw.lang.ir.statement.IRWhileStatement;
import gw.lang.ir.statement.IRDoWhileStatement;
import gw.lang.ir.statement.IRMonitorLockAcquireStatement;
import gw.lang.ir.statement.IRMonitorLockReleaseStatement;
import gw.lang.ir.statement.IRSwitchStatement;
import gw.internal.gosu.ir.compiler.tree.IRTreeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRClassCompiler;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class IRTreeVerifier {

  private List<String> _errors = new ArrayList<String>();
  private IRMethodStatement _enclosingMethodStatement;
  private IRClass _enclosingClass;

  public IRTreeVerifier() {
  }

  public List<String> getErrors() {
    return _errors;
  }

  public void printErrors() {
    if (_errors.isEmpty()) {
      System.out.println("No errors detected");
    } else {
      for (String error : _errors) {
        System.out.println("***ERROR: " + error);
      }
    }
  }

  public void verifyClassStatement(IRClass irClass) {
    _enclosingClass = irClass;
    for (IRFieldDecl fieldDecl : irClass.getFields()) {
      verifyIRFieldDecl(fieldDecl);
    }

    Set<String> methodSignatures = new HashSet<String>();
    for (IRMethodStatement methodStatement : irClass.getMethods()) {
      String signature = methodStatement.getName() + IRClassCompiler.getMethodDescriptor(methodStatement);
      if (methodSignatures.add(signature)) {
        verifyIRMethodStatement(methodStatement);
      } else {
        error("Duplicate methods found with signature " + signature, methodStatement);
      }
    }
  }

  private void verifyIRElement(IRElement element) {
    if (element instanceof IRStatement) {
      verifyIRStatement((IRStatement) element);
    } else {
      verifyIRExpression((IRExpression) element);
    }
  }

  private void verifyIRStatement(IRStatement statement) {
    if (statement == null) {
      return;
    }

    verifyParentIsSet( statement );

    // Java 21 switch pattern matching - more concise and compiler-verified exhaustiveness
    switch (statement) {
      case IRAssignmentStatement s -> verifyIRAssignmentStatement(s);
      case IRFieldDecl s -> verifyIRFieldDecl(s);
      case IRFieldSetStatement s -> verifyIRFieldSetStatement(s);
      case IRIfStatement s -> verifyIRIfStatement(s);
      case IRMethodCallStatement s -> verifyIRMethodCallStatement(s);
      case IRMethodStatement s -> verifyIRMethodStatement(s);
      case IRNoOpStatement s -> verifyIRNoOpStatement(s);
      case IRReturnStatement s -> verifyIRReturnStatement(s);
      case IRStatementList s -> verifyIRStatementList(s);
      case IRArrayStoreStatement s -> verifyIRArrayStoreStatement(s);
      case IRThrowStatement s -> verifyIRThrowStatement(s);
      case IRTryCatchFinallyStatement s -> verifyIRTryCatchFinallyStatement(s);
      case IRSyntheticStatement s -> verifyIRSyntheticStatement(s);
      case IRForEachStatement s -> verifyIRForEachStatement(s);
      case IRBreakStatement s -> verifyIRBreakStatement(s);
      case IRContinueStatement s -> verifyIRContinueStatement(s);
      case IRWhileStatement s -> verifyIRWhileStatement(s);
      case IRDoWhileStatement s -> verifyIRDoWhileStatement(s);
      case IRMonitorLockAcquireStatement s -> verifyIRIRMonitorLockAcquireStatement(s);
      case IRMonitorLockReleaseStatement s -> verifyIRIRMonitorLockReleaseStatement(s);
      case IRSwitchStatement s -> verifyIRSwitchStatement(s);
      case IREvalStatement s -> verifyIREvalStatement(s);
      case null -> throw new IllegalArgumentException("Null statement");
      default -> throw new IllegalArgumentException("Unrecognized statement of type " + statement.getClass());
    }
  }

  private void verifyIRSwitchStatement(IRSwitchStatement irSwitchStatement) {
    verifyIRStatement(irSwitchStatement.getInit());
    for (IRCaseClause caseClause : irSwitchStatement.getCases()) {
      verifyIRExpression( caseClause.getCondition() );
      for (IRStatement caseStatement : caseClause.getStatements() ) {
        verifyIRStatement( caseStatement );
      }
    }

    for (IRStatement defaultStatement : irSwitchStatement.getDefaultStatements() ) {
      verifyIRStatement( defaultStatement );
    }
  }

  private void verifyIRIRMonitorLockAcquireStatement( IRMonitorLockAcquireStatement irMonitorLockAcquireStatement ) {
  }

  private void verifyIRIRMonitorLockReleaseStatement( IRMonitorLockReleaseStatement irMonitorLockReleaseStatement ) {
  }

  private void verifyIRDoWhileStatement(IRDoWhileStatement irDoWhileLoopStatement) {
    verifyIRExpression( irDoWhileLoopStatement.getLoopTest() );
    verifyIRStatement( irDoWhileLoopStatement.getBody() );
  }

  private void verifyIRWhileStatement(IRWhileStatement irWhileLoopStatement) {
    verifyIRExpression( irWhileLoopStatement.getLoopTest() );
    verifyIRStatement( irWhileLoopStatement.getBody() );
  }

  private void verifyIRContinueStatement(IRContinueStatement irContinueStatement) {
    // Nothing
  }

  private void verifyIRBreakStatement(IRBreakStatement irBreakStatement) {
    // Nothing
  }

  private void verifyIREvalStatement( IREvalStatement evalStmt ) {
    verifyIRExpression( evalStmt.getExpression() );
  }

  private void verifyIRForEachStatement(IRForEachStatement irForLoopStatement) {
    for (IRStatement s : irForLoopStatement.getInitializers()) {
      verifyIRStatement( s );
    }
    verifyIRExpression( irForLoopStatement.getLoopTest() );
    for (IRStatement s : irForLoopStatement.getIncrementors()) {
      verifyIRStatement( s );
    }
    verifyIRStatement( irForLoopStatement.getBody() );
  }

  private void verifyIRSyntheticStatement(IRSyntheticStatement irSyntheticStatement) {
    verifyIRExpression(irSyntheticStatement.getExpression());
  }

  private void verifyIRTryCatchFinallyStatement(IRTryCatchFinallyStatement irTryCatchFinallyStatement) {
    verifyIRStatement( irTryCatchFinallyStatement.getTryBody() );
    for (IRCatchClause catchClause : irTryCatchFinallyStatement.getCatchStatements()) {
      verifyIRStatement(catchClause.getBody());
    }
    if (irTryCatchFinallyStatement.getFinallyBody() != null) {
      verifyIRStatement( irTryCatchFinallyStatement.getFinallyBody() );
    }
  }

  private void verifyIRThrowStatement(IRThrowStatement irThrowStatement) {
    verifyIRExpression( irThrowStatement.getException() );
  }

  private void verifyIRArrayStoreStatement(IRArrayStoreStatement irArrayStoreStatement) {
    if (!verifyExpressionIsOfType( irArrayStoreStatement.getTarget().getType().getComponentType(), irArrayStoreStatement.getValue())) {
      error("Attempting to store a value of type " + irArrayStoreStatement.getValue().getType().getName() + " in an array of type " + irArrayStoreStatement.getTarget().getType().getRelativeName(), irArrayStoreStatement);
    }

    verifyIRExpression( irArrayStoreStatement.getTarget() );
    verifyIRExpression( irArrayStoreStatement.getIndex() );
    verifyIRExpression( irArrayStoreStatement.getValue() );
  }

  private void verifyIRFieldSetStatement(IRFieldSetStatement irFieldSetStatement) {
    verifyIRExpression( irFieldSetStatement.getLhs() );
    verifyIRExpression( irFieldSetStatement.getRhs() );
  }

  private void verifyIRIfStatement(IRIfStatement irIfStatement) {
    verifyIRExpression( irIfStatement.getExpression() );
    verifyIRStatement( irIfStatement.getIfStatement() );
    verifyIRStatement( irIfStatement.getElseStatement() );
  }

  private void verifyIRMethodCallStatement(IRMethodCallStatement irMethodCallStatement) {
    verifyIRExpression( irMethodCallStatement.getExpression() );
  }

  private void verifyIRNoOpStatement(IRNoOpStatement irNoOpStatement) {
  }

  private void verifyIRReturnStatement(IRReturnStatement irReturnStatement) {
    if (_enclosingMethodStatement.getReturnType().isVoid()) {
      if (irReturnStatement.getReturnValue() != null) {
        error("Method " + _enclosingMethodStatement.getName() + " has a void return type, but a return statement returned a value", irReturnStatement);
      }
    } else if (irReturnStatement.getReturnValue() == null) {
      error("Method " + _enclosingMethodStatement.getName() + " has a non-void return type, but a return statement has no value", irReturnStatement);
    } else {
      if (!verifyExpressionIsOfType(_enclosingMethodStatement.getReturnType(), irReturnStatement.getReturnValue())) {
        error("Method " + _enclosingMethodStatement.getName() + " has a return type of " + _enclosingMethodStatement.getReturnType().getName() + " but a return statement has a value of type " + irReturnStatement.getReturnValue().getType().getName(), irReturnStatement);
      }
    }

    verifyIRExpression( irReturnStatement.getReturnValue() );
  }

  private void verifyIRStatementList(IRStatementList irStatementList) {
    for (IRStatement statement : irStatementList.getStatements()) {
      verifyIRStatement(statement);
    }
  }

  private void verifyIRAssignmentStatement(IRAssignmentStatement irAssignmentStatement) {
    if (!verifyExpressionIsOfType( irAssignmentStatement.getSymbol().getType(), irAssignmentStatement.getValue())) {
      error("Attempted to assign a value of type " + irAssignmentStatement.getValue().getType().getName() + " to a symbol of type " + irAssignmentStatement.getSymbol().getType().getName(), irAssignmentStatement);
    }

    verifyIRExpression( irAssignmentStatement.getValue() );
  }

  private void verifyIRExpression(IRExpression expression) {
    if (expression == null) {
      return;
    }

    verifyParentIsSet( expression );

    if (expression instanceof IRArithmeticExpression) {
      verifyIRAdditiveExpression((IRArithmeticExpression) expression);
    } else if (expression instanceof IRArrayLoadExpression) {
      verifyIRArrayLoadExpression((IRArrayLoadExpression) expression);
    } else if (expression instanceof IRBooleanLiteral) {
      verifyIRBooleanLiteral((IRBooleanLiteral) expression);
    } else if (expression instanceof IRCompositeExpression) {
      verifyIRCompositeExpression((IRCompositeExpression) expression);
    } else if (expression instanceof IREqualityExpression) {
      verifyIREqualityExpression((IREqualityExpression) expression);
    } else if (expression instanceof IRFieldGetExpression) {
      verifyIRFieldGetExpression((IRFieldGetExpression) expression);
    } else if (expression instanceof IRIdentifier) {
      verifyIRIdentifier((IRIdentifier) expression);
    } else if (expression instanceof IRMethodCallExpression) {
      verifyIRMethodCallExpression((IRMethodCallExpression) expression);
    } else if (expression instanceof IRNullLiteral) {
      verifyIRNullLiteral((IRNullLiteral) expression);
    } else if (expression instanceof IRPrimitiveTypeConversion) {
      verifyIRPrimitiveTypeConversion((IRPrimitiveTypeConversion) expression);
    } else if (expression instanceof IRTernaryExpression) {
      verifyIRTernaryExpression((IRTernaryExpression) expression);
    } else if (expression instanceof IRNumericLiteral) {
      verifyIRNumericLiteral((IRNumericLiteral) expression);
    } else if (expression instanceof IRCharacterLiteral ) {
      verifyIRCharacterLiteral((IRCharacterLiteral) expression);
    } else if (expression instanceof IRStringLiteralExpression) {
      verifyIRStringLiteralExpression((IRStringLiteralExpression) expression);
    } else if (expression instanceof IRNewArrayExpression) {
      verifyIRNewArrayExpression((IRNewArrayExpression) expression);
    } else if (expression instanceof IRCastExpression) {
      verifyIRCastExpression((IRCastExpression) expression);
    } else if (expression instanceof IRNewExpression) {
      verifyIRNewExpression((IRNewExpression) expression);
    } else if (expression instanceof IRRelationalExpression) {
      verifyIRRelationalExpression((IRRelationalExpression) expression);
    } else if (expression instanceof IRNegationExpression) {
      verifyIRNegationExpression((IRNegationExpression) expression);
    } else if (expression instanceof IRConditionalOrExpression) {
      verifyIRConditionalOrExpression((IRConditionalOrExpression) expression);
    } else if (expression instanceof IRConditionalAndExpression) {
      verifyIRConditionalAndExpression((IRConditionalAndExpression) expression);
    } else if (expression instanceof IRArrayLengthExpression) {
      verifyIRArrayLengthExpression((IRArrayLengthExpression) expression);
    } else if (expression instanceof IRClassLiteral) {
      verifyIRClassLiteral((IRClassLiteral) expression);
    } else if (expression instanceof IRNotExpression) {
      verifyIRNotExpression((IRNotExpression) expression);
    } else if (expression instanceof IRInstanceOfExpression) {
      verifyIRInstanceOfExpression((IRInstanceOfExpression) expression);
    } else if (expression instanceof IRNewMultiDimensionalArrayExpression) {
      verifyIRNewMultiDimensionalArrayExpression((IRNewMultiDimensionalArrayExpression) expression);
    } else if (expression instanceof IRNoOpExpression) {
      // Do nothing
    } else {
      throw new IllegalArgumentException("Unrecognized expression of type " + expression.getClass());
    }
  }

  private void verifyIRNewMultiDimensionalArrayExpression(IRNewMultiDimensionalArrayExpression irNewMultiDimensionalArrayExpression) {
    for (IRExpression irExpression : irNewMultiDimensionalArrayExpression.getSizeExpressions()) {
      verifyIRExpression(irExpression);
    }
  }

  private void verifyIRInstanceOfExpression(IRInstanceOfExpression irInstanceOfExpression) {
    verifyIRExpression(irInstanceOfExpression.getRoot());
  }

  private void verifyIRNotExpression(IRNotExpression irNotExpression) {
    verifyIRExpression(irNotExpression.getRoot());
  }

  private void verifyIRClassLiteral(IRClassLiteral irClassLiteral) {
    // Nothing for now
  }

  private void verifyIRArrayLengthExpression(IRArrayLengthExpression irArrayLengthExpression) {
    if (!irArrayLengthExpression.getRoot().getType().isArray()) {
      error("Array length expression root must be an array type, but was " + irArrayLengthExpression.getRoot().getType().getName(), irArrayLengthExpression);
    }

    verifyIRExpression(irArrayLengthExpression.getRoot());
  }

  private void verifyIRConditionalAndExpression(IRConditionalAndExpression irConditionalAndExpression) {
    verifyIRExpression(irConditionalAndExpression.getLhs());
    verifyIRExpression(irConditionalAndExpression.getRhs());
  }

  private void verifyIRConditionalOrExpression(IRConditionalOrExpression irConditionalOrExpression) {
    verifyIRExpression(irConditionalOrExpression.getLhs());
    verifyIRExpression(irConditionalOrExpression.getRhs());
  }

  private void verifyIRNegationExpression(IRNegationExpression irNegationExpression) {
    verifyIRExpression(irNegationExpression.getRoot());
  }

  private void verifyIRRelationalExpression(IRRelationalExpression irRelationalExpression) {
    verifyIRExpression( irRelationalExpression.getLhs() );
    verifyIRExpression( irRelationalExpression.getRhs() );
  }

  private void verifyIRNewExpression(IRNewExpression irNewExpression) {
    verifyTypeAgreement(irNewExpression.getParameterTypes(), irNewExpression.getArgs(), irNewExpression);

    for (IRExpression arg : irNewExpression.getArgs()) {
      verifyIRExpression(arg);
    }
  }

  private void verifyIRCastExpression(IRCastExpression irCastExpression) {
    verifyIRExpression( irCastExpression.getRoot() );
  }

  private void verifyIRNewArrayExpression(IRNewArrayExpression irNewArrayExpression) {
    verifyIRExpression( irNewArrayExpression.getSizeExpression() );
  }

  private void verifyIRStringLiteralExpression(IRStringLiteralExpression irStringLiteralExpression) {

  }

  private void verifyIRNumericLiteral(IRNumericLiteral irNumericLiteral) {

  }

  private void verifyIRCharacterLiteral(IRCharacterLiteral irNumericLiteral) {

  }

  private void verifyIRAdditiveExpression(IRArithmeticExpression irAdditiveExpression) {
    verifyIRExpression( irAdditiveExpression.getLhs() );
    verifyIRExpression( irAdditiveExpression.getRhs() );
  }

  private void verifyIRArrayLoadExpression(IRArrayLoadExpression irArrayLoadExpression) {
    verifyIRExpression( irArrayLoadExpression.getRoot() );
    verifyIRExpression( irArrayLoadExpression.getIndex() );
  }

  private void verifyIRBooleanLiteral(IRBooleanLiteral irBooleanLiteral) {
  }

  private void verifyIRCompositeExpression(IRCompositeExpression irCompositeExpression) {
    for (int i = 0; i < irCompositeExpression.getElements().size(); i++) {
      IRElement element = irCompositeExpression.getElements().get(i);
      if (i < irCompositeExpression.getElements().size() - 1) {
        if (!(element instanceof IRStatement)) {
          error("Composite does not consist of statements followed by a single expression: " + printElement( irCompositeExpression ), irCompositeExpression );
        }
      } else {
        if (!(element instanceof IRExpression)) {
          error("Composite does not consist of statements followed by a single expression: " + printElement( irCompositeExpression ), irCompositeExpression );
        }
      }
    }

    for (IRElement element : irCompositeExpression.getElements()) {
      verifyIRElement(element);
    }
  }

  private void verifyIREqualityExpression(IREqualityExpression irEqualityExpression) {
    // TODO - Verify the types are the same?
    verifyIRElement( irEqualityExpression.getLhs() );
    verifyIRElement( irEqualityExpression.getRhs() );
  }

  private void verifyIRFieldGetExpression(IRFieldGetExpression irFieldGetExpression) {
    verifyIRElement( irFieldGetExpression.getLhs() );
  }

  private void verifyIRIdentifier(IRIdentifier irIdentifier) {
  }

  private void verifyIRMethodCallExpression(IRMethodCallExpression irMethodCallExpression) {
    verifyTypeAgreement(irMethodCallExpression.getParameterTypes(), irMethodCallExpression.getArgs(), irMethodCallExpression);
    if (irMethodCallExpression.getRoot() != null && !verifyExpressionIsOfType(irMethodCallExpression.getOwnersType(), irMethodCallExpression.getRoot())) {
      error("Owner type mismatch.  Owner is of type " + irMethodCallExpression.getOwnersType().getName() + " but root expression is of type " + irMethodCallExpression.getRoot().getType().getName() + ".  Error is in " + printElement(irMethodCallExpression), irMethodCallExpression);
    }

    verifyIRExpression( irMethodCallExpression.getRoot() );
    for (IRExpression arg : irMethodCallExpression.getArgs()) {
      verifyIRExpression(arg);
    }
  }

  private void verifyIRNullLiteral(IRNullLiteral irNullLiteral) {
  }

  private void verifyIRPrimitiveTypeConversion(IRPrimitiveTypeConversion irPrimitiveTypeConversion) {
    verifyIRExpression( irPrimitiveTypeConversion.getRoot() );
  }

  private void verifyIRTernaryExpression(IRTernaryExpression irTernaryExpression) {
    if (!irTernaryExpression.getTest().getType().equals(IRTypeConstants.pBOOLEAN())) {
      error("Type mismatch for ternary test.  Must be of type boolean, but was of type " + irTernaryExpression.getTest().getType().getName(), irTernaryExpression);
    }
    if (!verifyExpressionIsOfType(irTernaryExpression.getType(), irTernaryExpression.getTrueValue())) {
      error("Type mismatch in ternary expression.  Overall expression is of type " + irTernaryExpression.getType().getName() + " but true expression is of type " + irTernaryExpression.getTrueValue().getType().getName(), irTernaryExpression);
    }
    if (!verifyExpressionIsOfType(irTernaryExpression.getType(), irTernaryExpression.getFalseValue())) {
      error("Type mismatch in ternary expression.  Overall expression is of type " + irTernaryExpression.getType().getName() + " but false expression is of type " + irTernaryExpression.getTrueValue().getType().getName(), irTernaryExpression);
    }

    verifyIRExpression( irTernaryExpression.getTest() );
    verifyIRExpression( irTernaryExpression.getTrueValue() );
    verifyIRExpression( irTernaryExpression.getFalseValue() );
  }

  private void verifyIRFieldDecl(IRFieldDecl fieldDecl) {
  }

  private void verifyIRMethodStatement(IRMethodStatement methodStatement) {
    _enclosingMethodStatement = methodStatement;
    try {
      verifyIRStatement(methodStatement.getMethodBody());
    } finally {
      _enclosingMethodStatement = null;
    }
  }

  private String printElement( IRElement element ) {
    IRTreeCompiler compiler = new IRTreeCompiler();
    compiler.compileIRElement(element, 0);
    return compiler.getOutput().toString();
  }

  private void error( String message, IRElement element ) {
    String enclosingStatement = getEnclosingStatement(element);
    if (enclosingStatement != null) {
      message = "Error found near " + enclosingStatement + "\n" + message;
    } else if (_enclosingMethodStatement != null) {
      message = "Error found in " + _enclosingClass.getName() + "." + _enclosingMethodStatement.getName() + "\n" + message;
    } else if (_enclosingClass != null) {
      message = "Error found in " + _enclosingClass.getName() + "\n" + message;
    }
    _errors.add( message );
  }

  private void verifyTypeAgreement( List<IRType> parameterTypes, List<IRExpression> arguments, IRElement element) {
    if (parameterTypes.size() != arguments.size()) {
      error("Argument number mismatch in " + printElement( element ), element );
    } else {
      for (int i = 0; i < parameterTypes.size(); i++) {
        IRType paramType = parameterTypes.get(i);
        IRExpression argument = arguments.get(i);
        if (!verifyExpressionIsOfType(paramType, argument)) {
          error("Argument type mismatch.  Parameter is of type " + paramType.getName() + " but argument is of type " + argument.getType().getName() + ".  Error is in " + printElement(element), element);
        }
      }
    }
  }

  private void verifyParentIsSet( IRElement element ) {
    if ( element.getParent() == null) {
      error("Null parent for element " + printElement( element ), element);
    }
  }

  private boolean verifyExpressionIsOfType(IRType type, IRExpression expression) {
    boolean match = (expression instanceof IRNullLiteral && !type.isPrimitive()) ||
            type.isAssignableFrom(expression.getType()) ||
            (isIntType( type ) && isIntType( expression.getType() ) ); // Allow assignment between types that map to the same thing in bytecode

    // Right now we sometimes have multiple casts, so if we've got a cast expression
    // and it doesn't match, try unwrapping it 
    if (!match && expression instanceof IRCastExpression) {
      return verifyExpressionIsOfType( type, ((IRCastExpression) expression).getRoot());
    } else {
      return match;
    }
  }

  // Short, char, byte and int are all the same thing in bytecode and can be assigned 
  private boolean isIntType( IRType type ) {
    return type.isShort() || type.isChar() || type.isInt() || type.isByte();
  }

  private String getEnclosingStatement( IRElement originalElement ) {
    IRElement element = originalElement;
    while (element != null) {
      if (element instanceof IRStatement) {
        IRStatement statement = (IRStatement) element;
        if (statement.getLineNumber() != -1 && statement.getOriginalSourceStatement() != null) {
          return statement.getOriginalSourceStatement();
        }
      }
      element = element.getParent();
    }

    return null;
  }
}
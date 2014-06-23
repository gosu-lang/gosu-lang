/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.tree;

import gw.lang.ir.IRClass;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRElement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.lang.ir.expression.IRArithmeticExpression;
import gw.lang.ir.expression.IRArrayLoadExpression;
import gw.lang.ir.expression.IRBooleanLiteral;
import gw.lang.ir.expression.IRCastExpression;
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
import gw.lang.ir.expression.IRClassLiteral;
import gw.lang.ir.expression.IRNewExpression;
import gw.lang.ir.expression.IRRelationalExpression;
import gw.lang.ir.expression.IRNegationExpression;
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
import gw.lang.ir.statement.IRContinueStatement;
import gw.lang.ir.statement.IRForEachStatement;
import gw.lang.ir.statement.IRSyntheticStatement;

import java.util.List;
import java.lang.reflect.Modifier;

public class IRTreeCompiler {

  private StringBuilder _output = new StringBuilder();

  public IRTreeCompiler() {
  }

  public StringBuilder getOutput() {
    return _output;
  }

  public void compileClassStatement(IRClass irClass) {
    _output.append("IRClass[\n");
    appendAttribute(2, "name", irClass.getName());
    appendAttribute(2, "modifiers", getModifierString(irClass.getModifiers()));
    appendAttribute(2, "supertype", irClass.getSuperType().getName());
    appendAttribute(2, "interfaces", joinTypeNames(irClass.getInterfaces()));
    appendAttribute(2, "sourceFile", irClass.getSourceFile());

    // Inner classes
    if (irClass.getInnerClasses().isEmpty()) {
      appendAttribute(2, "innerclasses", "[]");
    }

    // Fields
    if (irClass.getFields().isEmpty()) {
      appendAttribute(2, "fields", "[]");
    } else {
      appendIndent(2).append("fields: \n");
      for (IRFieldDecl fieldDecl : irClass.getFields()) {
        compileIRFieldDecl(fieldDecl, 4);
      }
    }

    // Methods
    if (irClass.getMethods().isEmpty()) {
      appendAttribute(2, "methods", "[]");
    } else {
      appendIndent(2).append("methods: \n");
      for (IRMethodStatement methodStatement : irClass.getMethods()) {
        compileIRMethodStatement(methodStatement, 4);
      }
    }


    _output.append("]");
  }

  public void compileIRElement(IRElement element, int indent) {
    if (element instanceof IRStatement) {
      compileIRStatement((IRStatement) element, indent);
    } else {
      compileIRExpression((IRExpression) element, indent);
    }
  }
  
  public void compileIRStatement(IRStatement statement, int indent) {
    if (statement instanceof IRAssignmentStatement) {
      compileIRAssignmentStatement((IRAssignmentStatement) statement, indent);
    } else if (statement instanceof IRFieldDecl) {
      compileIRFieldDecl((IRFieldDecl) statement, indent);
    } else if (statement instanceof IRFieldSetStatement) {
      compileIRFieldSetStatement((IRFieldSetStatement) statement, indent);
    } else if (statement instanceof IRIfStatement) {
      compileIRIfStatement((IRIfStatement) statement, indent);
    } else if (statement instanceof IRMethodCallStatement) {
      compileIRMethodCallStatement((IRMethodCallStatement) statement, indent);
    } else if (statement instanceof IRMethodStatement) {
      compileIRMethodStatement((IRMethodStatement) statement, indent);
    } else if (statement instanceof IRNoOpStatement) {
      compileIRNoOpStatement((IRNoOpStatement) statement, indent);
    } else if (statement instanceof IRReturnStatement) {
      compileIRReturnStatement((IRReturnStatement) statement, indent);
    } else if (statement instanceof IRStatementList) {
      compileIRStatementList((IRStatementList) statement, indent);
    } else if (statement instanceof IRArrayStoreStatement) {
      compileIRArrayStoreStatement((IRArrayStoreStatement) statement, indent);
    } else if (statement instanceof IRThrowStatement) {
      compileIRThrowStatement((IRThrowStatement) statement, indent);
    } else if (statement instanceof IRTryCatchFinallyStatement) {
      compileIRTryCatchFinallyStatement((IRTryCatchFinallyStatement) statement, indent);
    } else if (statement instanceof IRBreakStatement) {
      compileIRBreakStatement((IRBreakStatement) statement, indent);
    } else if (statement instanceof IRContinueStatement) {
      compileIRContinueStatement((IRContinueStatement) statement, indent);
    } else if (statement instanceof IRForEachStatement) {
      compileIRForEachStatement((IRForEachStatement) statement, indent);
    } else if (statement instanceof IRSyntheticStatement ) {
      compileIRSyntheticStatement((IRSyntheticStatement) statement, indent);
    } else {
      throw new IllegalArgumentException("Unrecognized statement of type " + statement.getClass());
    }
  }

  private void compileIRSyntheticStatement( IRSyntheticStatement syntheticStatement, int indent )
  {
    compileIRExpression( syntheticStatement.getExpression(), indent );
  }

  private void compileIRForEachStatement(IRForEachStatement irForEachStatement, int indent) {
    /* // init
  private List<IRStatement> _initializers = new ArrayList<IRStatement>();

  // test
  private IRExpression _test;

  // increment
  private List<IRStatement> _incrementors = new ArrayList<IRStatement>();

  // body
  private IRStatement _body;

  // identifier, if any, to null check
  private IRIdentifier _identifierToNullCheck;*/
    appendIndent(indent).append("IRForEachStatement[\n");
    for (IRStatement initializer : irForEachStatement.getInitializers()) {
      appendNestedElement(indent, "initializer", initializer);
    }
    appendNestedElement(indent, "test", irForEachStatement.getLoopTest());
    appendNestedElement(indent, "identifierToNullCheck", irForEachStatement.getIdentifierToNullCheck());
    for (IRStatement incrementor : irForEachStatement.getIncrementors()) {
      appendNestedElement(indent, "incrementor", incrementor);
    }
    appendNestedElement(indent, "body", irForEachStatement.getBody());
    appendIndent(indent).append("]\n");
  }

  private void compileIRContinueStatement(IRContinueStatement ircontinueStatement, int indent) {
    appendIndent(indent).append("IRContinueStatement[]\n");
  }

  private void compileIRBreakStatement(IRBreakStatement irBreakStatement, int indent) {
    appendIndent(indent).append("IRBreakStatement[]\n");
  }

  private void compileIRTryCatchFinallyStatement(IRTryCatchFinallyStatement irTryCatchFinallyStatement, int indent) {
    appendIndent(indent).append("IRTryCatchFinallyStatement[\n");
    appendNestedElement(indent, "try", irTryCatchFinallyStatement.getTryBody());

    for (IRCatchClause catchClause : irTryCatchFinallyStatement.getCatchStatements()) {
      appendAttribute(indent, "catchsymbol", getSymbolString(catchClause.getIdentifier()));
      appendNestedElement(indent, "catchbody", catchClause.getBody());
    }

    appendNestedElement(indent, "finally", irTryCatchFinallyStatement.getFinallyBody());
    appendIndent(indent).append("]\n");
  }

  public void compileIRThrowStatement(IRThrowStatement irThrowStatement, int indent) {
    appendIndent(indent).append("IRThrowStatement[\n");
    appendNestedElement(indent, "exception", irThrowStatement.getException());
    appendIndent(indent).append("]\n");
  }

  public void compileIRArrayStoreStatement(IRArrayStoreStatement irArrayStoreStatement, int indent) {
    appendIndent(indent).append("IRArrayStoreStatement[\n");
    appendAttribute(indent, "componenttype", irArrayStoreStatement.getComponentType().getName());
    appendNestedElement(indent, "target", irArrayStoreStatement.getTarget());
    appendNestedElement(indent, "index", irArrayStoreStatement.getIndex());
    appendNestedElement(indent, "value", irArrayStoreStatement.getValue());
    appendIndent(indent).append("]\n");
  }

  public void compileIRFieldSetStatement(IRFieldSetStatement irFieldSetStatement, int indent) {
    appendIndent(indent).append("IRFieldSetStatement[\n");
    appendAttribute(indent, "name", irFieldSetStatement.getName());
    appendAttribute(indent, "fieldtype", irFieldSetStatement.getFieldType().getName());
    appendAttribute(indent, "ownerstype", irFieldSetStatement.getOwnersType().getName());
    appendNestedElement(indent, "lhs", irFieldSetStatement.getLhs());
    appendNestedElement(indent, "rhs", irFieldSetStatement.getRhs());
    appendIndent(indent).append("]\n");
  }

  public void compileIRIfStatement(IRIfStatement irIfStatement, int indent) {
    appendIndent(indent).append("IRIfStatement[\n");
    appendNestedElement(indent, "condition", irIfStatement.getExpression());
    appendNestedElement(indent, "if", irIfStatement.getIfStatement());
    appendNestedElement(indent, "else", irIfStatement.getElseStatement());
    appendIndent(indent).append("]\n");
  }

  public void compileIRMethodCallStatement(IRMethodCallStatement irMethodCallStatement, int indent) {
    appendIndent(indent).append("IRMethodCallStatement[\n");
    appendNestedElement(indent, "methodcall", irMethodCallStatement.getExpression());
    appendIndent(indent).append("]\n");
  }

  public void compileIRNoOpStatement(IRNoOpStatement irNoOpStatement, int indent) {
    appendIndent(indent).append("IRNoOpStatement[\n");
    appendIndent(indent).append("]\n");
  }

  public void compileIRReturnStatement(IRReturnStatement irReturnStatement, int indent) {
    appendIndent(indent).append("IRReturnStatement[\n");
    appendNestedElement(indent, "returnvalue", irReturnStatement.getReturnValue());
    appendIndent(indent).append("]\n");
  }

  public void compileIRStatementList(IRStatementList irStatementList, int indent) {
    appendIndent(indent).append("IRStatementList[\n");
    appendAttribute(indent, "lineNumber", "" + irStatementList.getLineNumber());
    appendIndent(indent + 2).append("statements").append(": \n");
    for (IRStatement statement : irStatementList.getStatements()) {
      compileIRStatement(statement, indent + 4);
    }
    appendIndent(indent).append("]\n");
  }

  public void compileIRAssignmentStatement(IRAssignmentStatement irAssignmentStatement, int indent) {
    appendIndent(indent).append("IRAssignmentStatement[\n");
    appendAttribute(indent, "symbol", getSymbolString(irAssignmentStatement.getSymbol()));
    appendNestedElement(indent, "value", irAssignmentStatement.getValue());
    appendIndent(indent).append("]\n");
  }

  public void compileIRExpression(IRExpression expression, int indent) {
    if (expression instanceof IRArithmeticExpression) {
      compileIRAdditiveExpression((IRArithmeticExpression) expression, indent);
    } else if (expression instanceof IRArrayLoadExpression) {
      compileIRArrayLoadExpression((IRArrayLoadExpression) expression, indent);
    } else if (expression instanceof IRBooleanLiteral) {
      compileIRBooleanLiteral((IRBooleanLiteral) expression, indent);
    } else if (expression instanceof IRCompositeExpression) {
      compileIRCompositeExpression((IRCompositeExpression) expression, indent);
    } else if (expression instanceof IREqualityExpression) {
      compileIREqualityExpression((IREqualityExpression) expression, indent);
    } else if (expression instanceof IRFieldGetExpression) {
      compileIRFieldGetExpression((IRFieldGetExpression) expression, indent);
    } else if (expression instanceof IRIdentifier) {
      compileIRIdentifier((IRIdentifier) expression, indent);
    } else if (expression instanceof IRMethodCallExpression) {
      compileIRMethodCallExpression((IRMethodCallExpression) expression, indent);
    } else if (expression instanceof IRNullLiteral) {
      compileIRNullLiteral((IRNullLiteral) expression, indent);
    } else if (expression instanceof IRPrimitiveTypeConversion) {
      compileIRPrimitiveTypeConversion((IRPrimitiveTypeConversion) expression, indent);
    } else if (expression instanceof IRTernaryExpression) {
      compileIRTernaryExpression((IRTernaryExpression) expression, indent);
    } else if (expression instanceof IRNumericLiteral) {
      compileIRNumericLiteral((IRNumericLiteral) expression, indent);
    } else if (expression instanceof IRStringLiteralExpression) {
      compileIRStringLiteralExpression((IRStringLiteralExpression) expression, indent);
    } else if (expression instanceof IRNewArrayExpression) {
      compileIRNewArrayExpression((IRNewArrayExpression) expression, indent);
    } else if (expression instanceof IRCastExpression) {
      compileIRCastExpression((IRCastExpression) expression, indent);
    } else if (expression instanceof IRNewExpression) {
      compileIRNewExpression((IRNewExpression) expression, indent);
    } else if (expression instanceof IRRelationalExpression) {
      compileIRRelationalExpression((IRRelationalExpression) expression, indent);
    } else if (expression instanceof IRNegationExpression) {
      compileIRNegationExpression((IRNegationExpression) expression, indent);
    } else if (expression instanceof IRClassLiteral) {
      compileIRClassLiteral((IRClassLiteral) expression, indent);
    } else {
      throw new IllegalArgumentException("Unrecognized expression of type " + expression.getClass());
    }
  }

  private void compileIRClassLiteral(IRClassLiteral irClassLiteral, int indent) {
    appendIndent(indent).append("IRClassLiteral[\n");
    appendAttribute(indent, "type", irClassLiteral.getLiteralType().getName());
    appendIndent(indent).append("]\n");
  }

  private void compileIRNegationExpression(IRNegationExpression irNegationExpression, int indent) {
    appendIndent(indent).append("IRNegationExpression[\n");
    appendNestedElement(indent, "root", irNegationExpression.getRoot());
    appendIndent(indent).append("]\n");
  }

  public void compileIRRelationalExpression(IRRelationalExpression irRelationalExpression, int indent) {
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

    appendIndent(indent).append("IRRelationalExpression[\n");
    appendAttribute(indent, "op", op);
    appendNestedElement(indent, "lhs", irRelationalExpression.getLhs());
    appendNestedElement(indent, "rhs", irRelationalExpression.getRhs());
    appendIndent(indent).append("]\n");
  }

  public void compileIRNewExpression(IRNewExpression irNewExpression, int indent) {
    appendIndent(indent).append("IRNewExpression[\n");
    appendAttribute(indent, "owner", irNewExpression.getOwnersType().getName());
    appendAttribute(indent, "parametertypes", joinTypeNames(irNewExpression.getParameterTypes()));
    appendIndent(indent + 2).append("args").append(": \n");
    for (IRElement element : irNewExpression.getArgs()) {
      compileIRElement(element, indent + 4);
    }
    appendIndent(indent).append("]\n");
  }

  public void compileIRCastExpression(IRCastExpression irCastExpression, int indent) {
    appendIndent(indent).append("IRCastExpression[\n");
    appendAttribute(indent, "type", irCastExpression.getType().getName());
    appendNestedElement(indent, "root", irCastExpression.getRoot());
    appendIndent(indent).append("]\n");
  }

  public void compileIRNewArrayExpression(IRNewArrayExpression irNewArrayExpression, int indent) {
    appendIndent(indent).append("IRNewArrayExpression[\n");
    appendAttribute(indent, "type", irNewArrayExpression.getType().getName());
    appendNestedElement(indent, "size", irNewArrayExpression.getSizeExpression());
    appendIndent(indent).append("]\n");
  }

  public void compileIRStringLiteralExpression(IRStringLiteralExpression irStringLiteralExpression, int indent) {
    appendIndent(indent).append("IRStringLiteralExpression[\n");
    appendAttribute(indent, "value", irStringLiteralExpression.getValue());
    appendIndent(indent).append("]\n");
  }

  public void compileIRNumericLiteral(IRNumericLiteral irNumericLiteral, int indent) {
    appendIndent(indent).append("IRNumericLiteral[\n");
    appendAttribute(indent, "type", irNumericLiteral.getValue().getClass().getName());
    appendAttribute(indent, "value", irNumericLiteral.getValue().toString());
    appendIndent(indent).append("]\n");
  }

  public void compileIRAdditiveExpression(IRArithmeticExpression irAdditiveExpression, int indent) {
    appendIndent(indent).append("IRAdditiveExpression[\n");
    appendAttribute(indent, "type", irAdditiveExpression.getType().getName());
    appendAttribute(indent, "op", irAdditiveExpression.getOp().toString());
    appendNestedElement(indent, "lhs", irAdditiveExpression.getLhs());
    appendNestedElement(indent, "rhs", irAdditiveExpression.getRhs());
    appendIndent(indent).append("]\n");
  }

  public void compileIRArrayLoadExpression(IRArrayLoadExpression irArrayLoadExpression, int indent) {
    appendIndent(indent).append("IRArrayLoadExpression[\n");
    appendAttribute(indent, "componenttype", irArrayLoadExpression.getComponentType().getName());
    appendNestedElement(indent, "root", irArrayLoadExpression.getRoot());
    appendNestedElement(indent, "index", irArrayLoadExpression.getIndex());
    appendIndent(indent).append("]\n");
  }

  public void compileIRBooleanLiteral(IRBooleanLiteral irBooleanLiteral, int indent) {
    appendIndent(indent).append("IRBooleanLiteral[\n");
    appendAttribute(indent, "value", "" + irBooleanLiteral.getValue());
    appendIndent(indent).append("]\n");
  }

  public void compileIRCompositeExpression(IRCompositeExpression irCompositeExpression, int indent) {
    appendIndent(indent).append("IRCompositeExpression[\n");
    appendIndent(indent + 2).append("expressions").append(": \n");
    for (IRElement element : irCompositeExpression.getElements()) {
      compileIRElement(element, indent + 4);
    }
    appendIndent(indent).append("]\n");
  }

  public void compileIREqualityExpression(IREqualityExpression irEqualityExpression, int indent) {
    appendIndent(indent).append("IREqualityExpression[\n");
    appendNestedElement(indent, "lhs", irEqualityExpression.getLhs());
    appendNestedElement(indent, "rhs", irEqualityExpression.getRhs());
    appendIndent(indent).append("]\n");
  }

  public void compileIRFieldGetExpression(IRFieldGetExpression irFieldGetExpression, int indent) {
    appendIndent(indent).append("IRFieldGetExpression[\n");
    appendAttribute(indent, "name", irFieldGetExpression.getName());
    appendAttribute(indent, "type", irFieldGetExpression.getFieldType().getName());
    appendAttribute(indent, "owner", irFieldGetExpression.getOwnersType().getName());
    appendNestedElement(indent, "lhs", irFieldGetExpression.getLhs());
    appendIndent(indent).append("]\n");
  }

  public void compileIRIdentifier(IRIdentifier irIdentifier, int indent) {
    appendIndent(indent).append("IRIdentifier[\n");
    appendAttribute(indent, "symbol", getSymbolString(irIdentifier.getSymbol()));
    appendIndent(indent).append("]\n");
  }

  public void compileIRMethodCallExpression(IRMethodCallExpression irMethodCallExpression, int indent) {
    appendIndent(indent).append("IRMethodCallExpression[\n");
    appendAttribute(indent, "name", irMethodCallExpression.getName());
    appendAttribute(indent, "owner", irMethodCallExpression.getOwnersType().getName());
    appendAttribute(indent, "returntype", irMethodCallExpression.getReturnType().getName());
    appendAttribute(indent, "parametertypes", joinTypeNames(irMethodCallExpression.getParameterTypes()));
    appendNestedElement(indent, "root", irMethodCallExpression.getRoot());
    appendIndent(indent + 2).append("args").append(": \n");
    for (IRElement element : irMethodCallExpression.getArgs()) {
      compileIRElement(element, indent + 4);
    }
    appendIndent(indent).append("]\n");

  }

  public void compileIRNullLiteral(IRNullLiteral irNullLiteral, int indent) {
    appendIndent(indent).append("IRNullLiteral[\n");
    appendIndent(indent).append("]\n");
  }

  public void compileIRPrimitiveTypeConversion(IRPrimitiveTypeConversion irPrimitiveTypeConversion, int indent) {
    appendIndent(indent).append("IRPrimitiveTypeConversion[\n");
    appendAttribute(indent, "from", irPrimitiveTypeConversion.getFromType().getName());
    appendAttribute(indent, "to", irPrimitiveTypeConversion.getToType().getName());
    appendNestedElement(indent, "root", irPrimitiveTypeConversion.getRoot());
    appendIndent(indent).append("]\n");
  }

  public void compileIRTernaryExpression(IRTernaryExpression irTernaryExpression, int indent) {
    appendIndent(indent).append("IRTernaryExpression[\n");
    appendNestedElement(indent, "test", irTernaryExpression.getTest());
    appendNestedElement(indent, "truevalue", irTernaryExpression.getTrueValue());
    appendNestedElement(indent, "falsevalue", irTernaryExpression.getFalseValue());
    appendIndent(indent).append("]\n");
  }

  public void compileIRFieldDecl(IRFieldDecl fieldDecl, int indent) {
    appendIndent(indent).append("IRFieldDecl[\n");
    appendAttribute(indent, "name", fieldDecl.getName());
    appendAttribute(indent, "type", fieldDecl.getType().getName());
    appendAttribute(indent, "modifiers", getModifierString(fieldDecl.getModifiers()));
    if (fieldDecl.getValue() != null) {
      appendAttribute(indent, "value", fieldDecl.getValue().toString());
    }
    appendIndent(indent).append("]\n");
  }

  public void compileIRMethodStatement(IRMethodStatement methodStatement, int indent) {
    appendIndent(indent).append("IRMethodStatement[\n");
    appendAttribute(indent, "name", methodStatement.getName());
    appendAttribute(indent, "params", joinSymbols(methodStatement.getParameters()));
    appendAttribute(indent, "returntype", methodStatement.getReturnType().getName());
    appendAttribute(indent, "modifiers", getModifierString(methodStatement.getModifiers()));
    appendIndent(indent + 2).append("body: \n");
    compileIRStatement(methodStatement.getMethodBody(), indent + 4);
    appendIndent(indent).append("]\n");
  }

  static final int BRIDGE = 0x00000040;
  static final int VARARGS = 0x00000080;
  static final int SYNTHETIC = 0x00001000;
  static final int ANNOTATION = 0x00002000;
  static final int ENUM = 0x00004000;

  private String getModifierString(int modifiers) {
    StringBuilder sb = new StringBuilder();
    sb.append("(").append(modifiers).append(") ");
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
      sb.append(types.get(i).getName());
    }
    return sb.toString();
  }

  private String joinSymbols(List<IRSymbol> symbols) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < symbols.size(); i++) {
      if (i > 0) {
        sb.append(", ");
      }
      sb.append(getSymbolString(symbols.get(i)));
    }
    return sb.toString();
  }

  private String getSymbolString(IRSymbol symbol) {
    return symbol.getName() + " : " + symbol.getType().getName();
  }

  private void appendAttribute(int indent, String name, String value) {
    appendIndent(indent + 2).append(name).append(": ").append(value).append("\n");
  }
  
  private void appendNestedElement(int indent, String name, IRElement element) {
    if (element == null) {
      appendAttribute(indent, name, "[]");
    } else {
      appendIndent(indent + 2).append(name).append(": \n");
      compileIRElement(element, indent + 4);
    }
  }

  private StringBuilder appendIndent(int indent) {
    for (int i = 0; i < indent; i++) {
      _output.append(" ");
    }
    return _output;
  }
}

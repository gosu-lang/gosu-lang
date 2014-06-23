/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util.transform.java.Visitor;

import com.sun.source.tree.*;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Convert;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import java.util.*;
import java.util.regex.Pattern;


public class GosuVisitor implements TreeVisitor<String, Void> {
  StringBuilder output;
  private int ident = 0;
  private static int TAB_SIZE ;
  private static String TAB;
  private SymbolTable symTable;
  private String currentEnumIdent;
  private List<String> currentResourcesIdents;
  private Mode mode;
  private boolean isEnum;
  private boolean isInterface;
  boolean skipBlockScope;
  private boolean skipSymConversion;

  private enum Mode {NORMAL, USING_NO_MODIFIERS, CATCH_PARAM, USING_CAST, METHOD_PARAM, ADD_RESOURCES_FINALLY_BLOCK, CLASS_VAR}

  public StringBuilder getOutput() {
    return output;
  }

  public GosuVisitor(int tabSize) {
    TAB_SIZE = tabSize;
    TAB = genTabSpaces(TAB_SIZE);
    mode = Mode.NORMAL;
    symTable = new SymbolTable();
    output = new StringBuilder();
    skipBlockScope = false;
    skipSymConversion = false;
  }

  private String genTabSpaces(int x) {
    String tab = "";
    while(x > 0) {
      tab += " ";
      x--;
    }
    return tab;
  }

  public String visitImport(ImportTree node, Void v) {
    StringBuilder out = new StringBuilder();
    Tree qualId = node.getQualifiedIdentifier();
    out.append("uses ");
    out.append(qualId.accept(this, v));
    out.append("\n");
    return out.toString();
  }

  public String visitCompilationUnit(CompilationUnitTree node, Void v) {
    ident = 0;

    if (!node.getPackageAnnotations().isEmpty()) {
      throw new AssertionError("Annotations on packages not supported");
    }
    ExpressionTree packageName = node.getPackageName();
    if (packageName != null) {
      output.append("package ");
      output.append(packageName);
      output.append("\n\n");
    }
    output.append("uses java.lang.*\n");
    List<? extends ImportTree> imports = node.getImports();
    for (ImportTree imp : imports) {
      output.append(imp.accept(this, v));
    }
    output.append("\n");
    List<? extends Tree> typeDecls = node.getTypeDecls();
    for (Tree typeDecl : typeDecls) {
      output.append(typeDecl.accept(this, v));
    }
    return output.toString();
  }


  public String visitClass(ClassTree node, Void v) {
    StringBuilder out = new StringBuilder();

    mode = Mode.NORMAL;
    Name name = node.getSimpleName();
    List<? extends TypeParameterTree> typeParameters = node.getTypeParameters();
    String declType = null;
    Tree.Kind kind = node.getKind();
    isEnum = false;
    isInterface = false;
    switch (kind) {
      case CLASS:
        declType = "class";
        break;
      case ENUM:
        declType = "enum";
        isEnum = true;
        currentEnumIdent = name.toString();
        break;
      case INTERFACE:
        declType = "interface";
        isInterface = true;
        break;
      case ANNOTATION_TYPE:
        appendAsComment(out, node.toString());
        //throw new AssertionError("Annotation declaration not supported in the conversion");
        return out.toString();
        //break;
      default:
        declType = "???";
        break;
    }
    ModifiersTree modifiers = node.getModifiers();
    out.append(modifiers.accept(this, v));
    out.append(declType);
    out.append(" ");
    out.append(name);
    if(!typeParameters.isEmpty()) {
      out.append("<");
      boolean first = true;
      for(Tree t : typeParameters) {
        if(!first) {
          out.append(", ");
        }
        first = false;
        out.append(t.accept(this, v));
      }
      out.append(">");
    }
    out.append(" ");
    Tree extendsClause = node.getExtendsClause();
    if (extendsClause != null) {
      out.append("extends ");
      out.append(extendsClause.accept(this, v));
      out.append(" ");
    }
    List<? extends Tree> implementsClause = node.getImplementsClause();
    if (!implementsClause.isEmpty()) {
      if (isInterface) {
        out.append("extends ");
      } else {
        out.append("implements ");
      }
      boolean first = true;
      for (Tree implement : implementsClause) {
        if (first) {
          out.append(implement.accept(this, v));
          first = false;
        } else {
          out.append(", ");
          out.append(implement.accept(this, v));
        }
      }
    }
    out.append(" {\n");
    pushIndent();
    symTable.pushGlobalScope(name.toString());
    List<? extends Tree> members = node.getMembers();

    addGlobalVariables(members);

    boolean first = true;
    boolean isLastVar = false;
    for (Tree member : members) {
      isLastVar = false;
      if (isEnum && isAEnumConstant(member)) {
        if (!first) {
          out.append(",\n");
        }
        first = false;
        appendIndent(out);
        String enumConst = member.accept(this, v);
        out.append(enumConst);
      } else {
        out.append("\n");
        appendIndent(out);
        if(member instanceof VariableTree) {
          Mode old = mode;
          mode = Mode.CLASS_VAR;
          out.append(member.accept(this, v));
          mode = old;
          isLastVar = true;
        } else if (member instanceof BlockTree){
          appendAsComment(out, member.toString());
        } else {
          out.append(member.accept(this, v));
        }
      }

    }

    if(isLastVar) {
      out.append("\n");
    }
    if (isEnum) {
      out.append("\n");
      isEnum = false;
    }
    if (isInterface) {
      isInterface = false;
    }
    mode = Mode.NORMAL;
    symTable.popGlobalScope();
    popIndent();
    out.append("\n");
    appendIndent(out);
    out.append("}\n");
    return out.toString();
  }

  private void addGlobalVariables(List<? extends Tree> members) {
    for (Tree member : members) {
      if (member instanceof VariableTree) {
        String ident = ((VariableTree) member).getName().toString();
        symTable.addGlobally(ident);
      }
    }
  }

  public String visitReturn(ReturnTree node, Void v) {
    StringBuilder out = new StringBuilder();
    ExpressionTree expression = node.getExpression();

    out.append("return");
    if(expression != null) {
      out.append(" ");
      out.append(expression.accept(this, v));
    }
    return out.toString();
  }

  public String visitTry(TryTree node, Void v) {
    StringBuilder out = new StringBuilder();
    List<? extends Tree> resources = node.getResources();
    BlockTree block = node.getBlock();
    List<? extends CatchTree> catches = node.getCatches();
    BlockTree finallyBlock = node.getFinallyBlock();
    List<String> resIdents = new ArrayList<String>();

    if(catches.isEmpty() && !resources.isEmpty()) {
      out.append("using (");
      boolean first = true;
      Mode oldMode = mode;
      mode = Mode.USING_NO_MODIFIERS;
      for(Tree res : resources) {
        if (!first) {
           out.append(", ");
         }
        first = false;
        out.append(res.accept(this, v));
      }
      mode = oldMode;
      out.append(")");
      out.append(block.accept(this, v));
      if(finallyBlock != null) {
        appendIndent(out);
        out.append("finally ");
        out.append(finallyBlock.accept(this, v));
      }
      return out.toString();
    }
    if(!resources.isEmpty()) {
      for(Tree res : resources) {
        out.append("\n");
        appendIndent(out);
        out.append(res.accept(this, v));
        String ident = ((JCTree.JCVariableDecl) res).getName().toString();
        resIdents.add(ident);
      }
    }
    out.append("\n");
    appendIndent(out);
    out.append("try");
    out.append(block.accept(this, v));
    if(!catches.isEmpty()) {
      for(CatchTree c : catches) {
        out.append(c.accept(this, v));
      }
    }
    if(finallyBlock != null || !resIdents.isEmpty()) {
      Mode oldMode = mode;
      mode = Mode.ADD_RESOURCES_FINALLY_BLOCK;
      currentResourcesIdents = resIdents;
      appendIndent(out);
      out.append("finally ");
      if(finallyBlock == null) {
        finallyBlock  = new DummyBlock();
      }
      out.append(finallyBlock.accept(this, v));
      mode = oldMode;
    }
    return out.toString();
  }


  public String visitCatch(CatchTree node, Void v) {
    StringBuilder out = new StringBuilder();
    VariableTree parameter = node.getParameter();
    BlockTree block = node.getBlock();

    Tree type = parameter.getType();
    if(type instanceof JCTree.JCTypeUnion) {
      String name = parameter.getName().toString();
      for(JCTree.JCExpression expr : ((JCTree.JCTypeUnion) type).getTypeAlternatives()) {
        appendIndent(out);
        out.append("catch (");
        skipBlockScope = true;
        symTable.pushLocalScope();
        String newName = symTable.addLocally(name);
        out.append(newName).append(" : ");
        out.append(expr.accept(this, v));
        out.append(")");
        out.append(block.accept(this, v));
        symTable.popLocalScope();
      }
    } else {
      appendIndent(out);
      out.append("catch (");
      skipBlockScope = true;
      Mode oldMode = mode;
      mode = Mode.CATCH_PARAM;
      symTable.pushLocalScope();
      out.append(parameter.accept(this, v));
      mode = oldMode;
      out.append(")");
      out.append(block.accept(this, v));
      symTable.popLocalScope();
    }
    return out.toString();
  }


  public String visitLabeledStatement(LabeledStatementTree node, Void v) {
    StringBuilder out = new StringBuilder();
    StatementTree stmt = node.getStatement();
    String label = node.getLabel().toString() + ": ";
    appendAsComment(out, label);
    appendIndent(out);
    out.append(stmt.accept(this, v));
    return out.toString();
  }


  public String visitBlock(BlockTree node, Void v) {
    StringBuilder out = new StringBuilder();
    // todo handle static blocks
    if (node.isStatic()) {
      //throw new AssertionError("Static blocks not supported in Gosu");
      appendAsComment(out, node.toString());
      return out.toString();
    }
    out.append(" {\n");
    pushIndent();
    boolean skipPopScope = false;
    if (!skipBlockScope) {
      symTable.pushLocalScope();
    }  else {
      skipBlockScope = false;
      skipPopScope = true;
    }
    if(mode == Mode.ADD_RESOURCES_FINALLY_BLOCK) {
      int i = currentResourcesIdents.size()-1;
      while(i >= 0) {
        String id =  currentResourcesIdents.get(i);
        appendIndent(out);
        String newId = symTable.convertLocalSymbol(id);
        out.append("if (").append(newId).append(" != null) ").append(newId).append(".close()\n");
        i--;
      }
    }
    List<? extends StatementTree> statements = node.getStatements();
    for (StatementTree stm : statements) {
      appendIndent(out);
      out.append(stm.accept(this, v));
      out.append("\n");
    }

    if (!skipPopScope) {
      symTable.popLocalScope();
    }
    popIndent();
    appendIndent(out);
    out.append("}\n");

    return out.toString();
  }

  public String visitSwitch(SwitchTree node, Void v) {
    StringBuilder out = new StringBuilder();
    List<? extends CaseTree> cases = node.getCases();
    ExpressionTree expression = node.getExpression();
    out.append("switch ");
    out.append(expression.accept(this, v));
    out.append(" {\n");
    pushIndent();
    symTable.pushLocalScope();
    for(CaseTree c : cases) {
      out.append(c.accept(this, v));
      out.append("\n");
    }
    symTable.popLocalScope();
    popIndent();
    appendIndent(out);
    out.append("}\n");
    return out.toString();
  }


  public String visitCase(CaseTree node, Void v) {
    StringBuilder out = new StringBuilder();
    ExpressionTree expr = node.getExpression();
    List<? extends StatementTree> statements = node.getStatements();
    appendIndent(out);
    if(expr != null) {
      out.append("case ");
      out.append(expr.accept(this, v));
    } else {
      out.append("default");
    }
    out.append(":\n");
    pushIndent();
    for(StatementTree stm : statements) {
      appendIndent(out);
      out.append(stm.accept(this, v));
      out.append("\n");
    }
    popIndent();
    appendIndent(out);
    return out.toString();
  }


  public String visitEnhancedForLoop(EnhancedForLoopTree node, Void v) {
    StringBuilder out = new StringBuilder();
    String variablename = node.getVariable().getName().toString();
    ExpressionTree expr = node.getExpression();
    StatementTree stmt = node.getStatement();

    skipBlockScope = true;
    symTable.pushLocalScope();
    out.append("for (");
    String newName = symTable.addLocally(variablename);
    out.append(newName);
    out.append(" in ");
    out.append(expr.accept(this, v));
    out.append(")");
    if(stmt instanceof BlockTree) {
      out.append(stmt.accept(this, v));
    } else {
      out.append(" {\n");
      pushIndent();
      appendIndent(out);
      out.append(stmt.accept(this, v));
      popIndent();
      out.append("\n");
      appendIndent(out);
      out.append("}\n");
    }
    symTable.popLocalScope();
    return out.toString();
  }


  public String visitEmptyStatement(EmptyStatementTree node, Void v) {
    return "";
  }


  public String visitExpressionStatement(ExpressionStatementTree node, Void v) {
    StringBuilder out = new StringBuilder();
    out.append(node.getExpression().accept(this, v));
    return out.toString();
  }


  public String visitArrayAccess(ArrayAccessTree node, Void v) {
    StringBuilder out = new StringBuilder();
    ExpressionTree expr = node.getExpression();
    ExpressionTree index = node.getIndex();

    out.append(expr.accept(this, v));
    out.append("[");
    out.append(index.accept(this, v));
    out.append("]");
    return out.toString();
  }


  public String visitArrayType(ArrayTypeTree node, Void v) {
    return node.getType().accept(this, v) + "[]";
  }


  public String visitThrow(ThrowTree node, Void v) {
    return "throw " + node.getExpression().accept(this, v);
  }


  public String visitVariable(VariableTree node, Void v) {
    StringBuilder out = new StringBuilder();
    String name = node.getName().toString();
    ExpressionTree initializer = node.getInitializer();
    if (mode != Mode.CLASS_VAR && !isAEnumConstant(node)) {
      name = symTable.addLocally(name);
    }
    if (isEnum && isAEnumConstant(node)) {
      out.append(name);
      if (initializer != null) {
        out.append(initializer.accept(this, v));
      }
      return out.toString();
    }
    if (mode != Mode.CATCH_PARAM && mode != Mode.USING_NO_MODIFIERS) {
      ModifiersTree modifiers = node.getModifiers();
      out.append(modifiers.accept(this, v));
    }
    Tree type = node.getType();
    boolean appedVar = !(mode == Mode.METHOD_PARAM || mode == Mode.CATCH_PARAM);
    if (appedVar) {
      out.append("var ");
    }
    out.append(name);
    skipSymConversion = true;
    String varType = type.accept(this, v);
    skipSymConversion = false;
    String iniz = null;
    String genType = null;
    if (initializer != null) {
      iniz = " = " + initializer.accept(this, v);
      if (iniz.contains("<>")) {
        genType = extractGenericType(varType);
        iniz = iniz.replaceAll("<>", genType);
      }
    }
    if (appedVar && initializer != null && mode != Mode.CLASS_VAR) {
      String infer = typeInference(initializer, varType, genType, iniz);
      if(infer != null) {
        out.append(infer);
        return out.toString();
      }
    }
    out.append(" : ");
    out.append(varType);
    if (iniz != null) {
      out.append(iniz);
    }
    return out.toString();
  }

  private String extractGenericType(String varType) {
    String out = varType;
    int b = varType.indexOf("<");
    int e = varType.lastIndexOf(">");
    if(b != -1 && e != -1) {
      out = varType.substring(b, e + 1);
    }
    return out;
  }

  private String typeInference(ExpressionTree initializer, String varType, String genType, String iniz) {
    String initType = null;
    boolean literal = false;
    if (initializer instanceof NewArrayTree) {
      Tree arrayType = ((NewArrayTree) initializer).getType();
      if(arrayType != null) {
        initType = arrayType.accept(this, null);
        initType = initType.replaceAll(Pattern.quote("[]"), "");
        varType = varType.replaceAll(Pattern.quote("[]"), "");
      }
    } else if (initializer instanceof NewClassTree) {
      initType = ((NewClassTree) initializer).getIdentifier().accept(this, null);
      if (genType != null) {
        initType = initType.replaceAll("<>", genType);
      }
    } else if(initializer instanceof LiteralTree &&
            initializer.getKind() != Tree.Kind.NULL_LITERAL &&
            !varType.equals("Object"))
    {
      literal = true;
      if(varType.equals("byte")) {
        iniz = iniz + "b";
      } else if(varType.equals("short")) {
        iniz = iniz + "s";
      } else if(varType.equals("float") && initializer.getKind() == Tree.Kind.INT_LITERAL ) {
        iniz = iniz + "f";
      } else if(varType.equals("double") && initializer.getKind() == Tree.Kind.INT_LITERAL ) {
        iniz = iniz + ".0";
      } else if(varType.equals("long") && initializer.getKind() == Tree.Kind.INT_LITERAL ) {
        iniz = iniz + "L";
      }
    }
    if((initType != null && varType.equals(initType)) || literal) {
      return iniz;
    }
    return null;
  }

  private boolean isAEnumConstant(Tree node) {
    return ((JCTree)node).getTag() == JCTree.VARDEF && (((JCTree.JCVariableDecl) node).mods.flags & Flags.ENUM) != 0;
  }


  public String visitParameterizedType(ParameterizedTypeTree node, Void v) {
    StringBuilder out = new StringBuilder();
    Tree type = node.getType();
    List<? extends Tree> typeArguments = node.getTypeArguments();
    out.append(type.accept(this, v));
    out.append("<");
    boolean first = true;
    for(Tree t : typeArguments) {
      if(!first) {
        out.append(", ");
      }
      first = false;
      out.append(t.accept(this, v));
    }
    out.append(">");
    return out.toString();
  }


  public String visitMemberSelect(MemberSelectTree node, Void v) {
    StringBuilder out = new StringBuilder();

    String expr = node.getExpression().accept(this, v);
    String id = node.getIdentifier().toString();

    if (expr.endsWith(".this")) {
      int e = expr.indexOf(".this");
      int dot = expr.substring(0, e).lastIndexOf(".");
      int s = dot != -1 ? dot + 1 : 0;

      String clazz = expr.substring(s, e);
      int level = symTable.getClassLevelFromCurrent(clazz);
      if (level == 0) {
        return id;
      } else {
        out.append("outer");
        level--;
        while (level > 0) {
          out.append(".outer");
          level--;
        }
      }
    } else {
      out.append(expr);
    }

    if (!"class".equals(id)) {
      out.append(".");
      out.append(id);
    }
    return out.toString();
  }


  public String visitWildcard(WildcardTree node, Void v) {
    String out;
    Tree bound = node.getBound();
    String boundStr = "";
    if(bound != null) {
      boundStr = bound.accept(this, v);
    }
    Tree.Kind kind = node.getKind();
    if(kind == Tree.Kind.EXTENDS_WILDCARD) {
      out = boundStr;
    } else if(kind == Tree.Kind.SUPER_WILDCARD) {
      out = "? super " + boundStr;
    } else {//if(kind == Tree.Kind.UNBOUNDED_WILDCARD) {
      out = "?";
    }
    return out;
  }


  public String visitBinary(BinaryTree node, Void v) {
    StringBuilder out = new StringBuilder();
    ExpressionTree lOp = node.getLeftOperand();
    ExpressionTree rOp = node.getRightOperand();
    int operator = ((JCTree.JCBinary) node).getTag();
    out.append(lOp.accept(this, v));
    out.append(" ").append(operatorName(operator)).append(" ");
    out.append(rOp.accept(this, v));
    return out.toString();
  }


  public String visitParenthesized(ParenthesizedTree node, Void v) {
    StringBuilder out = new StringBuilder();

    out.append("(");
    out.append(node.getExpression().accept(this, v));
    if(mode == Mode.USING_CAST) {
      out.append(" as IMonitorLock");
    }
    out.append(")");
    return out.toString();
  }


  public String visitNewArray(NewArrayTree node, Void v) {
    StringBuilder out = new StringBuilder();
    List<? extends ExpressionTree> dim = node.getDimensions();
    List<? extends ExpressionTree> inits = node.getInitializers();
    Tree type = node.getType();
    if (type != null) {
      out.append("new ");
      skipSymConversion = true;
      out.append(type.accept(this, v));
      skipSymConversion = false;
    }
    if (!dim.isEmpty()) {
      if(type != null && type instanceof JCTree.JCArrayTypeTree) {
        int end = out.length();
        out.delete(end - 2, end);
      }
      for (ExpressionTree d : dim) {
        out.append("[");
        out.append(d.accept(this, v));
        out.append("]");
      }
      if(type != null && type instanceof JCTree.JCArrayTypeTree) {
        out.append("[]");
      }
    } else if (type != null) {
      out.append("[]");
    }
    if (inits != null) {
      boolean first = true;
      out.append("{");
      for (ExpressionTree init : inits) {
        if (!first) {
          out.append(", ");
        }
        first = false;
        out.append(init.accept(this, v));
      }
      out.append("}");
    }
    return out.toString();
  }


  public String visitOther(Tree node, Void v) {
    return "OTHER";
  }


  public String visitErroneous(ErroneousTree node, Void v) {
    throw new AssertionError("We don't support erroneous Java files");
  }


  public String visitSynchronized(SynchronizedTree node, Void v) {
    StringBuilder out = new StringBuilder();
    ExpressionTree expr = node.getExpression();
    BlockTree block = node.getBlock();
    out.append("using ");
    Mode oldMode = mode;
    mode = Mode.USING_CAST;
    out.append(expr.accept(this, v));
    mode = oldMode;
    out.append(block.accept(this, v));
    return out.toString();
  }


  public String visitBreak(BreakTree node, Void v) {
    StringBuilder out = new StringBuilder();
    out.append("break");
    Name label = node.getLabel();
    if(label != null) {
       out.append(" ").append(label);
    }
    return out.toString();
  }


  public String visitContinue(ContinueTree node, Void v) {
    StringBuilder out = new StringBuilder();
    out.append("continue");
    Name label = node.getLabel();
    if(label != null) {
      out.append(" ").append(label);
    }
    return out.toString();
  }


  public String visitDoWhileLoop(DoWhileLoopTree node, Void v) {
    StringBuilder out = new StringBuilder();
    ExpressionTree condition = node.getCondition();
    StatementTree statement = node.getStatement();
    out.append("do");
    if(statement instanceof BlockTree) {
      out.append(statement.accept(this, v));
    } else {
      out.append(" {\n");
      pushIndent();
      symTable.pushLocalScope();
      appendIndent(out);
      out.append(statement.accept(this, v));
      symTable.popLocalScope();
      popIndent();
      out.append("\n");
      appendIndent(out);
      out.append("}\n");
    }
    appendIndent(out);
    out.append("while ");
    out.append(condition.accept(this, v));
    return out.toString();
  }


  public String visitWhileLoop(WhileLoopTree node, Void v) {
    StringBuilder out = new StringBuilder();
    ExpressionTree condition = node.getCondition();
    StatementTree statement = node.getStatement();
    out.append("while ");
    out.append(condition.accept(this, v));
    if(statement instanceof BlockTree) {
      out.append(statement.accept(this, v));
    } else {
      out.append(" {\n");
      pushIndent();
      symTable.pushLocalScope();
      appendIndent(out);
      out.append(statement.accept(this, v));
      symTable.popLocalScope();
      popIndent();
      out.append("\n");
      appendIndent(out);
      out.append("}\n");
    }
    return out.toString();
  }


  public String visitIf(IfTree node, Void v) {
    StringBuilder out = new StringBuilder();
    ExpressionTree cond = node.getCondition();
    StatementTree thenStm = node.getThenStatement();
    StatementTree elseStm = node.getElseStatement();
    out.append("if ");
    out.append(cond.accept(this, v));
    if(thenStm instanceof BlockTree) {
      out.append(thenStm.accept(this, v));
      out.deleteCharAt(out.length()-1);
    } else {
      out.append(" {\n");
      pushIndent();
      symTable.pushLocalScope();
      appendIndent(out);
      out.append(thenStm.accept(this, v));
      symTable.popLocalScope();
      popIndent();
      out.append("\n");
      appendIndent(out);
      out.append("}");
    }
    if(elseStm != null) {
      out.append(" else");
      if(elseStm instanceof BlockTree || elseStm instanceof IfTree) {
        if(elseStm instanceof IfTree) {
          out.append(" ");
        }
        out.append(elseStm.accept(this, v));
        if(elseStm instanceof BlockTree) {
          out.deleteCharAt(out.length()-1);
        }
      } else {
        out.append(" {\n");
        pushIndent();
        symTable.pushLocalScope();
        appendIndent(out);
        out.append(elseStm.accept(this, v));
        symTable.popLocalScope();
        popIndent();
        out.append("\n");
        appendIndent(out);
        out.append("}");
      }
    }
    return out.toString();
  }


  public String visitInstanceOf(InstanceOfTree node, Void v) {
    StringBuilder out = new StringBuilder();
    ExpressionTree expr = node.getExpression();
    Tree type = node.getType();
    out.append(expr.accept(this, v));
    out.append(" typeis ");
    out.append(type.accept(this, v));
    return out.toString();
  }


  public String visitUnary(UnaryTree node, Void v) {
    int op = ((JCTree.JCUnary) node).getTag();
    String opName = operatorName(op);
    String expr = node.getExpression().accept(this, v);
    if(op == JCTree.PREDEC  ||
       op == JCTree.PREINC  ||
       op == JCTree.POSTDEC ||
       op == JCTree.POSTINC) {
      return expr + opName;
    }
    return opName + expr;
  }


  public String visitAssert(AssertTree node, Void v) {
    StringBuilder out = new StringBuilder();
    ExpressionTree condition = node.getCondition();
    ExpressionTree detail = node.getDetail();
    out.append("assert ");
    out.append(condition.accept(this, v));
    if (detail != null) {
      out.append(" : ");
      out.append(detail.accept(this, v));
    }
    return out.toString();
  }


  public String visitForLoop(ForLoopTree node, Void v) {
    StringBuilder out = new StringBuilder();
    List<? extends StatementTree> initializer = node.getInitializer();
    ExpressionTree condition = node.getCondition();
    StatementTree statement = node.getStatement();
    List<? extends ExpressionStatementTree> update = node.getUpdate();

    String gosuForLoop = maybeTranfromToGosuFor(node, v);
    if(gosuForLoop != null) {
      return gosuForLoop;
    }

    if(!initializer.isEmpty()) {
      for(StatementTree stm : initializer) {
        out.append(stm.accept(this, v));
        out.append("\n");
        appendIndent(out);
      }
    }
    out.append("while (");
    if(condition == null) {
      out.append("true");
    } else {
      out.append(condition.accept(this, v));
    }

    out.append(") {\n");
    pushIndent();
    symTable.pushLocalScope();
    if(statement instanceof BlockTree) {
      List<? extends StatementTree> statements = ((BlockTree) statement).getStatements();
      for(StatementTree stm : statements) {
        appendIndent(out);
        out.append(stm.accept(this, v));
        out.append("\n");
      }
    } else {
      appendIndent(out);
      out.append(statement.accept(this, v));
      out.append("\n");
    }
    if(!update.isEmpty()) {
      for(ExpressionStatementTree stm : update) {
        appendIndent(out);
        out.append(stm.accept(this, v));
        out.append("\n");
      }
    }
    symTable.popLocalScope();
    popIndent();
    appendIndent(out);
    out.append("}\n");
    return out.toString();
  }

  private String maybeTranfromToGosuFor(ForLoopTree node, Void v) {
    StringBuilder out = new StringBuilder();
    List<? extends StatementTree> initializer = node.getInitializer();
    ExpressionTree condition = node.getCondition();
    StatementTree statement = node.getStatement();
    List<? extends ExpressionStatementTree> update = node.getUpdate();

    if (initializer.size() == 1 && update.size() == 1 && condition instanceof JCTree.JCBinary) {
      StatementTree inizST = initializer.get(0);
      ExpressionTree expression = update.get(0).getExpression();
      JCTree.JCBinary cond = (JCTree.JCBinary) condition;
      if (cond.getTag() == JCTree.LT && inizST instanceof JCTree.JCVariableDecl &&
          expression instanceof JCTree.JCUnary)
      {
        JCTree.JCVariableDecl iniz = (JCTree.JCVariableDecl) inizST;
        JCTree.JCUnary up = (JCTree.JCUnary) expression;
        String var = cond.getLeftOperand().accept(this, v);
        if (var.equals(up.getExpression().accept(this, v)) &&
            var.equals(iniz.getName().toString()) &&
            (up.getTag() == JCTree.PREINC || up.getTag() == JCTree.POSTINC) &&
            "0".equals(iniz.getInitializer().accept(this, v)))
        {
          JCTree.JCExpression condRight = cond.getRightOperand();
          boolean validLimit = false;
          String condR = condRight.accept(this, v);
          if (condRight instanceof JCTree.JCLiteral) {
            try {
              Integer val = Integer.valueOf(condR);
              if (val >= 0) {
                validLimit = true;
              }
            } catch (NumberFormatException ex) {}
          } else if (condR.endsWith(".size()") || condR.endsWith(".length()") || condR.endsWith(".length")) {
            validLimit = true;
          }
          if(validLimit) {
            skipBlockScope = true;
            symTable.pushLocalScope();
            String newName = symTable.addLocally(var);
            out.append("for (");
            out.append(newName);
            out.append(" in 0..|");
            out.append(condR);
            out.append(")");
            if(statement instanceof BlockTree) {
              out.append(statement.accept(this, v));
            } else {
              out.append(" {\n");
              pushIndent();
              appendIndent(out);
              out.append(statement.accept(this, v));
              popIndent();
              out.append("\n");
              appendIndent(out);
              out.append("}\n");
            }
            symTable.popLocalScope();
            return out.toString();
          }
        }
      }
    }
    return null;
  }


  public String visitConditionalExpression(ConditionalExpressionTree node, Void v) {
    StringBuilder out = new StringBuilder();

    ExpressionTree cond = node.getCondition();
    ExpressionTree falseExp = node.getFalseExpression();
    ExpressionTree trueEx = node.getTrueExpression();
    out.append(cond.accept(this, v));
    out.append(" ? ");
    out.append(trueEx.accept(this, v));
    out.append(" : ");
    out.append(falseExp.accept(this, v));
    return out.toString();
  }


  public String visitMethodInvocation(MethodInvocationTree node, Void v) {
    StringBuilder out = new StringBuilder();
    List<? extends ExpressionTree> arguments = node.getArguments();
    ExpressionTree methodSelect = node.getMethodSelect();
    if(methodSelect instanceof IdentifierTree) {
      skipSymConversion = true;
      out.append(methodSelect.accept(this, v));
      skipSymConversion = false;
    } else {
      out.append(methodSelect.accept(this, v));
    }
    List<? extends Tree> typeArguments = node.getTypeArguments();
    if (!typeArguments.isEmpty()) {
      out.append("<");
      boolean first = true;
      for(Tree t : typeArguments) {
        if(!first) {
          out.append(", ");
        }
        first = false;
        out.append(t.accept(this, v));
      }
      out.append(">");
    }
    out.append("(");
    if (!arguments.isEmpty()) {
      boolean first = true;
      for (ExpressionTree arg : arguments) {
        if (!first) {
          out.append(", ");
        }
        first = false;
        out.append(arg.accept(this, v));
      }
    }
    out.append(")");
    return out.toString();
  }


  public String visitNewClass(NewClassTree node, Void v) {
    StringBuilder out = new StringBuilder();
    ExpressionTree identifier = node.getIdentifier();
    List<? extends ExpressionTree> arguments = node.getArguments();
    ClassTree classBody = node.getClassBody();
    ExpressionTree enclosingExpression = node.getEnclosingExpression();

    //List<? extends Tree> typeArguments = node.getTypeArguments();
    //not needed in gosu

    if (enclosingExpression != null) {
      out.append(enclosingExpression.accept(this, v));
      out.append(".");
      //throw new AssertionError("Annotation declaration not supported in the conversion");
    }

    skipSymConversion = true;
    String identOut = identifier.accept(this, v);
    skipSymConversion = false;
    boolean isEnumConst = isEnum && currentEnumIdent.equals(identOut);
    if (!isEnumConst) {
      out.append("new ");
      out.append(identOut);
    }
    if (!isEnumConst || !arguments.isEmpty()) {
      out.append("(");
    }
    if (!arguments.isEmpty()) {
      boolean first = true;
      for (ExpressionTree arg : arguments) {
        if (!first) {
          out.append(", ");
        }
        first = false;
        out.append(arg.accept(this, v));
      }
    }
    if (!isEnumConst || !arguments.isEmpty()) {
      out.append(")");
    }
    if (classBody != null) {
      out.append(" {\n");
      pushIndent();
      symTable.pushGlobalScope(identOut);
      List<? extends Tree> members = classBody.getMembers();

      addGlobalVariables(members);

      for (Tree member : members) {
        out.append("\n");
        appendIndent(out);
        if(member instanceof VariableTree) {
          Mode old = mode;
          mode = Mode.CLASS_VAR;
          out.append(member.accept(this, v));
          mode = old;
        } else if (member instanceof BlockTree){
          appendAsComment(out, member.toString());
        } else {
          out.append(member.accept(this, v));
        }
      }
      symTable.popGlobalScope();
      popIndent();
      out.append("\n");
      appendIndent(out);
      out.append("}\n");
    }
    return out.toString();
  }


  public String visitPrimitiveType(PrimitiveTypeTree node, Void v) {
    return node.toString();
  }


  public String visitMethod(MethodTree node, Void v) {
    StringBuilder out = new StringBuilder();
    BlockTree body = node.getBody();
    Tree defaultValue = node.getDefaultValue();
    if (defaultValue != null) {
      throw new AssertionError("default values for annotations not supported");
    }
    ModifiersTree modifiers = node.getModifiers();
    String name = node.getName().toString();
    List<? extends VariableTree> parameters = node.getParameters();
    Tree returnType = node.getReturnType();
    List<? extends TypeParameterTree> typeParameters = node.getTypeParameters();
    //List<? extends ExpressionTree> lThrows = node.getThrows(); not needed
    HashMap<String, String> constructorTypes = null;


    out.append(modifiers.accept(this, v));
    if (name.equals("<init>")) {
      if(isEnum && !modifiers.getFlags().contains(Modifier.PRIVATE) ) {
        out.append("private ");
      }
      out.append("construct");
      if(!typeParameters.isEmpty()) {
        constructorTypes = computeConstructorTypes(typeParameters);
      }
    } else {
      out.append("function ");
      out.append(name);
      if(!typeParameters.isEmpty()) {
        out.append("<");
        boolean first = true;
        for(Tree t : typeParameters) {
          if(!first) {
            out.append(", ");
          }
          first = false;
          out.append(t.accept(this, v));
        }
        out.append(">");
      }
    }
    Mode oldMode = mode;
    mode = Mode.METHOD_PARAM;
    out.append("(");
    symTable.pushLocalScope();
    boolean first = true;
    for (VariableTree par : parameters) {
      if (!first) {
        out.append(", ");
      }
      first = false;
      out.append(par.accept(this, v));
    }
    out.append(")");
    if(constructorTypes != null) {
      String newOut = out.toString();
      for(String key : constructorTypes.keySet()) {
        newOut = newOut.replace(": " + key, ": " + constructorTypes.get(key));
      }
      out = new StringBuilder(newOut);
    }
    mode = oldMode;
    if (returnType != null) {
      out.append(" : ");
      out.append(returnType.accept(this, v));
    }
    if (body != null) {
      skipBlockScope = true;
      out.append(body.accept(this, v));
    }
    symTable.popLocalScope();
    return out.toString();
  }


  private HashMap<String, String> computeConstructorTypes(List<? extends TypeParameterTree> typeParameters) {
    HashMap<String, String> constructorTypes = new HashMap<String, String>();
    for(TypeParameterTree t : typeParameters) {
      String key = t.getName().toString();
      String value;
      List<? extends Tree> bounds = t.getBounds();
      if(bounds.isEmpty()) {
        value = "Object";
      } else {
        StringBuilder out = new StringBuilder();
        if(!bounds.isEmpty()) {
          boolean first = true;
          for(Tree b : bounds) {
            if(!first) {
              out.append(" & ");
            }
            first = false;
            out.append(b.accept(this, null));
          }
        }
        value = out.toString();
      }
      constructorTypes.put(key, value);
    }
    return constructorTypes;
  }


  public String visitModifiers(ModifiersTree node, Void v) {
    StringBuilder out = new StringBuilder();
    String ann;
    boolean isOverridden = false;
    for (AnnotationTree annotation : node.getAnnotations()) {
      ann = annotation.accept(this, v);
      if("override".equals(ann)) {
        isOverridden = true;
      } else {
        out.append(ann);
        out.append("\n");
      }
    }
    if (!node.getAnnotations().isEmpty() && !isOverridden) {
      appendIndent(out);
    }
    if(isOverridden) {
      if(node.getAnnotations().size() > 1) {
        appendIndent(out);
      }
      out.append("override ");
    }
    for (Modifier modifier : node.getFlags()) {
        out.append(modifier);
        out.append(" ");
    }
    String modifiers = out.toString();
    if(mode == Mode.CLASS_VAR &&
       !isInterface &&
       !modifiers.contains("public") &&
       !modifiers.contains("protected") &&
       !modifiers.contains("private"))
    {
      out.append("internal ");
    }
    return out.toString();
  }


  public String visitTypeParameter(TypeParameterTree node, Void v) {
    StringBuilder out = new StringBuilder();
    Name name = node.getName();
    List<? extends Tree> bounds = node.getBounds();
    out.append(name);
    if(!bounds.isEmpty()) {
      out.append(" extends ");
      boolean first = true;
      for(Tree b : bounds) {
        if(!first) {
          out.append(" & ");
        }
        first = false;
        out.append(b.accept(this, v));
      }
    }
    return out.toString();
  }


  public String visitIdentifier(IdentifierTree node, Void v) {
    String ident = node.getName().toString();
    if(!skipSymConversion) {
      ident = symTable.convertLocalSymbol(ident);
    }
    return ident;
  }


  public String visitLiteral(LiteralTree node, Void v) {
    StringBuilder out = new StringBuilder();
    Object value = node.getValue();
    switch (node.getKind()) {
      case INT_LITERAL:
        out.append(value);
        break;
      case LONG_LITERAL:
        out.append(value);
        out.append("L");
        break;
      case FLOAT_LITERAL:
        out.append(value);
        out.append("f");
        break;
      case DOUBLE_LITERAL:
        out.append(value);
        break;
      case BOOLEAN_LITERAL:
      case NULL_LITERAL:
        out.append(value);
        break;
      case CHAR_LITERAL:
        out.append("\'").append(Convert.quote(String.valueOf(value))).append("\'");
        break;
      case STRING_LITERAL:
        out.append("\"").append(Convert.quote(value.toString())).append("\"");
        break;
    }
    return out.toString();
  }


  public String visitTypeCast(TypeCastTree node, Void v) {
    StringBuilder out = new StringBuilder();
    out.append(node.getExpression().accept(this, v));
    out.append(" as ");
    out.append(node.getType().accept(this, v));
    return out.toString();
  }


  public String visitAssignment(AssignmentTree node, Void v) {
    StringBuilder out = new StringBuilder();
    ExpressionTree variable = node.getVariable();
    ExpressionTree expression = node.getExpression();
    out.append(variable.accept(this, v));
    out.append(" = ");
    out.append(expression.accept(this, v));
    return out.toString();
  }


  public String visitCompoundAssignment(CompoundAssignmentTree node, Void v) {
    StringBuilder out = new StringBuilder();
    ExpressionTree variable = node.getVariable();
    ExpressionTree expression = node.getExpression();
    int operator = ((JCTree.JCAssignOp) node).getTag();
    out.append(variable.accept(this, v));
    out.append(" ").append(operatorName(operator)).append(" ");
    out.append(expression.accept(this, v));
    return out.toString();
  }


  public String visitAnnotation(AnnotationTree node, Void v) {
    StringBuilder out = new StringBuilder();
    Tree type = node.getAnnotationType();
    String typeStr = type.accept(this, v);
    if("Override".equals(typeStr)) {
      return "override";
    }
    out.append("@");
    out.append(typeStr);
    List<? extends ExpressionTree> arguments = node.getArguments();
    if (!arguments.isEmpty()) {
      out.append("(");
      boolean first = true;
      for (ExpressionTree arg : arguments) {
        if (!first) {
          out.append(", ");
        }
        first = false;
        if (arg instanceof AssignmentTree) {
          out.append(":");
        }
        out.append(arg.accept(this, v));
      }
      out.append(")");
    }
    return out.toString();
  }


  public String visitUnionType(UnionTypeTree node, Void v) {
    /* handled in the visitCatch()*/
    return null;
  }


  private void pushIndent() {
    ident += TAB_SIZE;
  }


  private void popIndent() {
    ident -= TAB_SIZE;
  }


  private void appendIndent(StringBuilder out) {
    for (int i = 0; i < ident; i += TAB_SIZE) {
      out.append(TAB);
    }
  }


  private void appendAsComment(StringBuilder out, String code) {
    out.append("/*\n");
    pushIndent();
    for (String line : code.split("\\r?\\n")) {
      appendIndent(out);
      out.append(line);
      out.append("\n");
    }
    popIndent();
    appendIndent(out);
    out.append("*/\n");
  }


  public String operatorName(int tag) {
    switch (tag) {
      case JCTree.POS:
        return "+";
      case JCTree.NEG:
        return "-";
      case JCTree.NOT:
        return "!";
      case JCTree.COMPL:
        return "~";
      case JCTree.PREINC:
        return "++";
      case JCTree.PREDEC:
        return "--";
      case JCTree.POSTINC:
        return "++";
      case JCTree.POSTDEC:
        return "--";
      case JCTree.NULLCHK:
        return "<*nullchk*>";
      case JCTree.OR:
        return "or";
      case JCTree.AND:
        return "and";
      case JCTree.BITOR:
        return "|";
      case JCTree.BITXOR:
        return "^";
      case JCTree.BITAND:
        return "&";
      case JCTree.EQ:
        return "==";
      case JCTree.NE:
        return "!=";
      case JCTree.LT:
        return "<";
      case JCTree.GT:
        return ">";
      case JCTree.LE:
        return "<=";
      case JCTree.GE:
        return ">=";
      case JCTree.SL:
        return "<<";
      case JCTree.SR:
        return ">>";
      case JCTree.USR:
        return ">>>";
      case JCTree.PLUS:
        return "+";
      case JCTree.MINUS:
        return "-";
      case JCTree.MUL:
        return "*";
      case JCTree.DIV:
        return "/";
      case JCTree.MOD:
        return "%";
      case JCTree.BITOR_ASG:
        return "|=";
      case JCTree.BITXOR_ASG:
        return "^=";
      case JCTree.BITAND_ASG:
        return "&=";
      case JCTree.SL_ASG:
        return "<<=";
      case JCTree.SR_ASG:
        return ">>=";
      case JCTree.USR_ASG:
        return ">>>=";
      case JCTree.PLUS_ASG:
        return "+=";
      case JCTree.MINUS_ASG:
        return "-=";
      case JCTree.MUL_ASG:
        return "*=";
      case JCTree.DIV_ASG:
        return "/=";
      case JCTree.MOD_ASG:
        return "%=";
      default:
        throw new Error();
    }
  }
}

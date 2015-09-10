/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.parser;

import com.google.common.base.Strings;
import com.google.common.collect.Ordering;
import com.intellij.lang.ASTFactory;
import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.javadoc.PsiDocCommentImpl;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.LazyParseableElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.util.containers.SortedList;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IProgramClassFunctionSymbol;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.IStatement;
import gw.lang.parser.IToken;
import gw.lang.parser.Keyword;
import gw.lang.parser.expressions.*;
import gw.lang.parser.statements.IArrayAssignmentStatement;
import gw.lang.parser.statements.IAssignmentStatement;
import gw.lang.parser.statements.IBeanMethodCallStatement;
import gw.lang.parser.statements.IBlockInvocationStatement;
import gw.lang.parser.statements.IBreakStatement;
import gw.lang.parser.statements.ICaseClause;
import gw.lang.parser.statements.ICatchClause;
import gw.lang.parser.statements.IClassDeclaration;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.parser.statements.IClasspathStatement;
import gw.lang.parser.statements.IConstructorStatement;
import gw.lang.parser.statements.IContinueStatement;
import gw.lang.parser.statements.IDoWhileStatement;
import gw.lang.parser.statements.IEvalStatement;
import gw.lang.parser.statements.IForEachStatement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.parser.statements.IHideFieldNoOpStatement;
import gw.lang.parser.statements.IIfStatement;
import gw.lang.parser.statements.IInterfacesClause;
import gw.lang.parser.statements.IMapAssignmentStatement;
import gw.lang.parser.statements.IMemberAssignmentStatement;
import gw.lang.parser.statements.IMethodCallStatement;
import gw.lang.parser.statements.INamespaceStatement;
import gw.lang.parser.statements.INewStatement;
import gw.lang.parser.statements.INoOpStatement;
import gw.lang.parser.statements.INotAStatement;
import gw.lang.parser.statements.IPropertyStatement;
import gw.lang.parser.statements.IReturnStatement;
import gw.lang.parser.statements.IStatementList;
import gw.lang.parser.statements.ISuperTypeClause;
import gw.lang.parser.statements.ISwitchStatement;
import gw.lang.parser.statements.ISyntheticFunctionStatement;
import gw.lang.parser.statements.ISyntheticMemberAccessStatement;
import gw.lang.parser.statements.IThrowStatement;
import gw.lang.parser.statements.ITryCatchFinallyStatement;
import gw.lang.parser.statements.ITypeVariableExtendsListClause;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.parser.statements.IUsesStatementList;
import gw.lang.parser.statements.IUsingStatement;
import gw.lang.parser.statements.IWhileStatement;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.java.JavaTypes;
import gw.plugin.ij.lang.GosuCommentImpl;
import gw.plugin.ij.lang.GosuTokenImpl;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.IGosuElementType;
import gw.plugin.ij.lang.psi.impl.expressions.GosuIdentifierImpl;
import gw.plugin.ij.lang.psi.impl.expressions.LightGosuIdentifierImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class GosuAstTransformer {
  private static final GosuAstTransformer INSTANCE = new GosuAstTransformer();

  @NotNull
  public static GosuAstTransformer instance() {
    return INSTANCE;
  }

  @NotNull
  public ASTNode transformClass(@NotNull ASTNode chameleon, @NotNull IGosuClass gosuClass) {
    LazyParseableElement rootFileNode = ASTFactory.lazy((IFileElementType) chameleon.getElementType(), null);
    gosuClass.isValid();

    IClassFileStatement cfs = gosuClass.getClassStatement().getClassFileStatement();
//    ParseTreeUtil.dumpParseTree(cfs, "");
    makeAstTree(rootFileNode, cfs);
    return rootFileNode;
  }

  @NotNull
  public ASTNode transformProgram(@NotNull ASTNode chameleon, @NotNull IGosuProgram gosuProgram) {
    LazyParseableElement rootFileNode = ASTFactory.lazy((IFileElementType) chameleon.getElementType(), null);
    gosuProgram.isValid();

    // Wrap the program in a nice warm Gosu class. This effectively makes program files appear as toplevel psi classes
    CompositeElement classStmtNode = new GosuSyntheticCompositeElement(GosuElementTypes.CLASS_DEFINITION);
    rootFileNode.rawAddChildrenWithoutNotifications(classStmtNode);

    CompositeElement synthModifierList = new GosuSyntheticCompositeElement(GosuElementTypes.ELEM_TYPE_ModifierListClause);
    classStmtNode.rawAddChildrenWithoutNotifications(synthModifierList);

    CompositeElement programClassDecl = new LightGosuIdentifierImpl(gosuProgram.getRelativeName());
    classStmtNode.rawAddChildrenWithoutNotifications(programClassDecl);

    IClassStatement cs = gosuProgram.getClassStatement();
    addTokens(classStmtNode, cs, null);

    final List topLevelStatements = getOrderedTopLevelStatements(gosuProgram);
    for (Object toplevelThing : topLevelStatements) {
      if (toplevelThing instanceof IParsedElement) {
        final IParsedElement topLevelStmt = (IParsedElement) toplevelThing;
        CompositeElement node = makeAstTree(classStmtNode, topLevelStmt);
        if (node != null) {
          // childNode can be null e.g., PropertyStatement (the corresponding function stmt is added inside makeAstTree())
          classStmtNode.rawAddChildrenWithoutNotifications(node);
        }
        addTokens(classStmtNode, cs, topLevelStmt.getLocation());
      } else {
        final IToken token = (IToken) toplevelThing;
        addToken(token.getTokenStart() >= cs.getLocation().getExtent()
                 ? rootFileNode
                 : classStmtNode, token, token.getAfter(), false);
      }
    }

    final IParseTree evaluateStatement = findEvaluateFunction(gosuProgram);
    if (evaluateStatement != null) {
      addTokens(classStmtNode, cs, evaluateStatement);
    }
    return rootFileNode;
  }

  public List<IParseTree> getChildrenSorted(@NotNull IParseTree tree) {
    return Ordering.from(new Comparator<IParseTree>() {
      public int compare(@NotNull IParseTree o1, @NotNull IParseTree o2) {
        return o1.getOffset() - o2.getOffset();
      }
    }).immutableSortedCopy(tree.getChildren());
  }

  @NotNull
  private List getOrderedTopLevelStatements(@NotNull IGosuProgram gsProg) {
    @SuppressWarnings({"unchecked"})
    final List elems = new SortedList(new Comparator() {
      private int getOffset(@NotNull Object p) {
         if (p instanceof IParsedElement) {
          return ((IParsedElement) p).getLocation().getOffset();
        } else {
          return ((IToken) p).getTokenStart();
        }
      }

      public int compare(@NotNull Object p1, @NotNull Object p2) {
        return getOffset(p1) - getOffset(p2);
      }
    });

    for (IParseTree childLoc : getChildrenSorted(gsProg.getClassStatement().getLocation())) {
      final IParsedElement child = childLoc.getParsedElement();
      if (child instanceof IFunctionStatement) {
        IDynamicFunctionSymbol dfs = ((IFunctionStatement) child).getDynamicFunctionSymbol();
        if (dfs instanceof IProgramClassFunctionSymbol) {
          if (dfs.getDisplayName().equals("evaluate")) {
            IStatement body = (IStatement) dfs.getValueDirectly();
            if (body instanceof IStatementList) {
              for (IStatement stmt : ((IStatementList) body).getStatements()) {
                if ((!(stmt instanceof IReturnStatement) || stmt.getLocation() != null) &&
                    !(stmt instanceof IHideFieldNoOpStatement) && (!(stmt instanceof INoOpStatement) || !stmt.getTokens().isEmpty())) {
                  elems.add(stmt);
                } else {
                  reportPotentiallyMissedTokens(elems, stmt);
                }
              }
              // Add expressions that are direct children of the the body. This can happen with templates e.g., all the <%= xxx %> expressions
              for (IParseTree csr : body.getLocation().getChildren()) {
                if (csr.getParsedElement() instanceof IExpression) {
                  elems.add(csr.getParsedElement());
                }
              }

              elems.addAll(body.getTokens());

              IParsedElement otherStmtList = child.getLocation().getChildren().get(0).getParsedElement();
              if (otherStmtList != body) {
                elems.addAll(otherStmtList.getTokens());
              }
            } else {
              elems.add(body);
            }
          }
        } else {
          elems.add(child);
        }
      } else {
        elems.add(child);
      }
    }
    for (IToken token : gsProg.getClassStatement().getClassFileStatement().getTokens()) {
      elems.add(token);
    }
    return elems;
  }

  private void reportPotentiallyMissedTokens(List elems, @NotNull IParsedElement pe) {
    if (pe.getLocation() != null) {
      for (IParseTree child : pe.getLocation().getChildren()) {
        reportPotentiallyMissedTokens(elems, child.getParsedElement());
      }
    }
    for (IToken token : pe.getTokens()) {
      throw new IllegalStateException("Unaccounted for token: " + token);
    }
  }

  @Nullable
  private CompositeElement makeAstTree(CompositeElement astParent, @NotNull IParsedElement peChild) {
    CompositeElement astParentNew = makeAst(peChild);
    CompositeElement result = astParentNew;
    if (astParentNew == null) {
      astParentNew = astParent;
    }
    addTokens(astParentNew, peChild, null);
    for (IParseTree child : getChildrenSorted(peChild.getLocation())) {
      CompositeElement childNode = makeAstTree(astParentNew, child.getParsedElement());
      if (childNode != null) {
        astParentNew.rawAddChildrenWithoutNotifications(childNode);
      }
      addTokens(astParentNew, peChild, child);
    }
    return result;
  }

  @NotNull
  private CompositeElement makeAstForGosuClass(@NotNull IGosuClass gosuClass) {
    if (gosuClass.isInterface()) {
      return new GosuCompositeElement(GosuElementTypes.INTERFACE_DEFINITION);
    } else if (gosuClass.isEnum()) {
      return new GosuCompositeElement(GosuElementTypes.ENUM_DEFINITION);
    } else if (gosuClass instanceof IGosuEnhancement) {
      return new GosuCompositeElement(GosuElementTypes.ENHANCEMENT_DEFINITION);
    } else if (gosuClass.getAllTypesInHierarchy().contains(JavaTypes.IANNOTATION())) {
      return new GosuCompositeElement(GosuElementTypes.ANNOTATION_DEFINITION);
    } else if (gosuClass.isAnonymous()) {
      final GosuCompositeElement node = new GosuCompositeElement(GosuElementTypes.ANONYMOUS_CLASS_DEFINITION);
      final CompositeElement programClassDecl = new LightGosuIdentifierImpl(gosuClass.getRelativeName());
      node.rawAddChildrenWithoutNotifications(programClassDecl);
      return node;
    } else {
      return new GosuCompositeElement(GosuElementTypes.CLASS_DEFINITION);
    }
  }

  @Nullable
  private CompositeElement makeAst(@NotNull IParsedElement pe) {
    //## todo: at some point we'll want to support language injection for string templates
    if (descendsFromTemplateStringLiteral(pe)) {
      return null;
    }

    CompositeElement node;
    if (pe instanceof INamespaceStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_NamespaceStatement);
    } else if (pe instanceof IClassFileStatement) {
      return null;
    } else if (pe instanceof IClassStatement) {
      if (isProgram(pe)) {
        return null;
      }
      node = makeAstForGosuClass(pe.getGosuClass());
    } else if (pe instanceof IClassDeclaration) {
      if (isProgram(pe)) {
        return null;
      }

      if (pe.getLocation().getLength() == 0) {
        node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_ClassDeclaration);
      } else {
        return null;
      }
    } else if (pe instanceof ITypeVariableExtendsListClause) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_TypeVariableExtendsListClause);
    } else if (pe instanceof ISuperTypeClause) {
      if (isProgram(pe)) {
        return null;
      }

      node = new GosuCompositeElement(GosuElementTypes.EXTENDS_CLAUSE);
    } else if (pe instanceof IInterfacesClause) {
      if (isProgram(pe)) {
        return null;
      }

      node = new GosuCompositeElement(GosuElementTypes.IMPLEMENTS_CLAUSE);
    } else if (pe instanceof IFunctionStatement) {
      if (isProgram(pe) &&
          ((IFunctionStatement) pe).getDynamicFunctionSymbol() instanceof IProgramClassFunctionSymbol) {
        return null;
      }

      node = new GosuCompositeElement(GosuElementTypes.METHOD_DEFINITION);
    } else if (pe instanceof IPropertyStatement) {
      node = null;
    } else if (pe instanceof IParameterListClause) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_ParameterListClause);
    } else if (pe instanceof IModifierListClause) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_ModifierListClause);
    } else if (pe instanceof IArgumentListClause) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_ArgumentListClause);
    } else if (pe instanceof INameInDeclaration) {
      if (isFieldProperty((INameInDeclaration) pe)) {
        node = new GosuCompositeElement(GosuElementTypes.FIELD_PROPERTY);
        String strPropName = ((INameInDeclaration)pe).getName();
        if( strPropName == null || strPropName.length() == 0 ) {
          node.rawAddChildrenWithoutNotifications( new LightGosuIdentifierImpl( "" ) );
        }
      } else if (isBlockParameter((INameInDeclaration) pe)) {
        node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_ParameterDeclaration);
      } else {
        final String strName = ((INameInDeclaration) pe).getName();
        final List<IToken> tokens = pe.getTokens();
        if (Strings.isNullOrEmpty(strName) ||
            tokens.isEmpty() ||
            (tokens.size() == 1 && tokens.get(0).getType() != ISourceCodeTokenizer.TT_WORD && !Keyword.isValueKeyword( tokens.get( 0 ).getText() ) && !(pe.getParent() instanceof IFunctionStatement)) ||
            pe.getParent() instanceof IConstructorStatement) {
          node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_NameInDeclaration);
        } else {
          // Skip over the NameInDeclaration and use the Identifier token directly (structurally easier to use with IJ if PsiIdentifier's parent is the Var or Method declaration)
          node = null;
        }
      }
    } else if (pe instanceof IStatementList) {
      if (isProgram(pe) &&
          pe.getParent() instanceof IFunctionStatement &&
          ((IFunctionStatement) pe.getParent()).getDynamicFunctionSymbol() instanceof IProgramClassFunctionSymbol) {
        return null;
      }
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_StatementList);
    } else if (pe instanceof IVarStatement) {
      IVarStatement stmt = (IVarStatement) pe;
      if (stmt.isEnumConstant()) {
        node = new GosuCompositeElement(GosuElementTypes.ENUM_CONSTANT);
      } else if (stmt.isFieldDeclaration()) {
        node = new GosuCompositeElement(GosuElementTypes.FIELD);
      } else {
        node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_VarStatement);
      }
    } else if (pe instanceof IAssignmentStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_AssignmentStatement);
    } else if (pe instanceof IMemberAssignmentStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_MemberAssignmentStatement);
    } else if (pe instanceof IArrayAssignmentStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_ArrayAssignmentStatement);
    } else if (pe instanceof IMapAssignmentStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_MapAssignmentStatement);
    } else if (pe instanceof IMethodCallStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_MethodCallStatement);
    } else if (pe instanceof IBlockInvocationStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_BlockInvocationStatement);
    } else if (pe instanceof IBeanMethodCallStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_BeanMethodCallStatement);
    } else if (pe instanceof IReturnStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_ReturnStatement);
    } else if (pe instanceof IBreakStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_BreakStatement);
    } else if (pe instanceof IContinueStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_ContinueStatement);
    } else if (pe instanceof IIfStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_IfStatement);
    } else if (pe instanceof IWhileStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_WhileStatement);
    } else if (pe instanceof IDoWhileStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_DoWhileStatement);
    } else if (pe instanceof IForEachStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_ForEachStatement);
    } else if (pe instanceof ISwitchStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_SwitchStatement);
    } else if (pe instanceof ICaseClause) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_CaseClause);
    } else if (pe instanceof ITryCatchFinallyStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_TryCatchFinallyStatement);
    } else if (pe instanceof ICatchClause) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_CatchClause);
    } else if (pe instanceof IThrowStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_ThrowStatement);
    } else if (pe instanceof IUsingStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_UsingStatement);
    } else if (pe instanceof IEvalStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_EvalStatement);
    } else if (pe instanceof ISyntheticFunctionStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_SyntheticFunctionStatement);
    } else if (pe instanceof ISyntheticMemberAccessStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_SyntheticMemberAccessStatement);
    } else if (pe instanceof INoOpStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_NoOpStatement);
    } else if (pe instanceof IClasspathStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_ClasspathStatement);
    } else if (pe instanceof IUsesStatementList) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_UsesStatementList);
    } else if (pe instanceof IUsesStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_UsesStatement);
    } else if (pe instanceof IIdentifierExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_IdentifierExpression);
    } else if (pe instanceof ITypeAsExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_TypeAsExpression);
    } else if (pe instanceof ITypeIsExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_TypeIsExpression);
    } else if (pe instanceof ITypeOfExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_TypeOfExpression);
    } else if (pe instanceof IStaticTypeOfExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_StaticTypeOfExpression);
    } else if (pe instanceof IStringLiteralExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_StringLiteral);
    } else if (pe instanceof ICharLiteralExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_CharLiteral);
    } else if (pe instanceof INumericLiteralExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_NumericLiteral);
    } else if (pe instanceof ITypeLiteralExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_TypeLiteral);
    } else if (pe instanceof ITypeParameterListClause) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_TypeParameterListClause);
    } else if (pe instanceof ITypeVariableDefinitionExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_TypeVariableDefinitionExpression);
    } else if (pe instanceof ITypeVariableListClause) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_TypeVariableListClause);
    } else if (pe instanceof IBooleanLiteralExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_BooleanLiteral);
    } else if (pe instanceof IUnaryExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_UnaryExpression);
    } else if (pe instanceof IUnaryNotPlusMinusExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_UnaryNotPlusMinusExpression);
    } else if (pe instanceof IEqualityExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_EqualityExpression);
    } else if (pe instanceof IIdentityExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_IdentityExpression);
    } else if (pe instanceof IRelationalExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_RelationalExpression);
    } else if (pe instanceof IConditionalOrExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_ConditionalOrExpression);
    } else if (pe instanceof IConditionalAndExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_ConditionalAndExpression);
    } else if (pe instanceof IAdditiveExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_AdditiveExpression);
    } else if (pe instanceof IMultiplicativeExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_MultiplicativeExpression);
    } else if (pe instanceof IBitshiftExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_BitshiftExpression);
    } else if (pe instanceof IBitwiseOrExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_BitwiseOrExpression);
    } else if (pe instanceof IBitwiseXorExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_BitwiseXorExpression);
    } else if (pe instanceof IBitwiseAndExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_BitwiseAndExpression);
    } else if (pe instanceof IBeanMethodCallExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_BeanMethodCallExpression);
    } else if (pe instanceof IMethodCallExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_MethodCallExpression);
    } else if (pe instanceof IMemberExpansionExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_MemberExpansionAccess);
    } else if (pe instanceof IMemberAccessExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_MemberAccess);
    } else if (pe instanceof IFieldAccessExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_FieldAccess);
    } else if (pe instanceof IAnnotationExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_AnnotationExpression);
    } else if (pe instanceof INewExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_NewExpression);
    } else if (pe instanceof INewStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_NewStatement);
    } else if (pe instanceof IEvalExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_EvalExpression);
    } else if (pe instanceof IConditionalTernaryExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_ConditionalTernaryExpression);
    } else if (pe instanceof IArrayAccessExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_ArrayAccess);
    } else if (pe instanceof IMapAccessExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_MapAccess);
    } else if (pe instanceof IIntervalExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_IntervalExpression);
    } else if (pe instanceof IParenthesizedExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_ParenthesizedExpression);
    } else if (pe instanceof INullExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_NullExpression);
    } else if (pe instanceof IBlockExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_BlockExpression);
    } else if (pe instanceof ITemplateStringLiteral) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_TemplateStringLiteral);
    } else if (pe instanceof IBlockInvocation) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_BlockInvocation);
    } else if (pe instanceof IFeatureLiteralExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_FeatureLiteral);
    } else if (pe instanceof IParameterDeclaration) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_ParameterDeclaration);
    } else if (pe instanceof ICollectionInitializerExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_CollectionInitializerExpression);
    } else if (pe instanceof IMapInitializerExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_MapInitializerExpression);
    } else if (pe instanceof IObjectInitializerExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_ObjectInitializerExpression);
    } else if (pe instanceof INotAStatement) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_NotAStatement);
    } else if (pe instanceof INotAWordExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_NotAWordExpression);
    } else if (pe instanceof ILocalVarDeclaration) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_LocalVarDeclaration);
    } else if (pe instanceof IDirectiveExpression) {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE_DirectiveExpression);
    } else {
      node = new GosuCompositeElement(GosuElementTypes.ELEM_TYPE____UnhandledParsedElement);
      //throw new UnsupportedOperationException( "AST Transformer not yet implemented for: " + pe.getClass().getName() );
    }

    return node;
  }

  private boolean isFieldProperty(@NotNull INameInDeclaration pe) {
    final IParsedElement parent = pe.getParent();
    if (parent instanceof IVarStatement) {
      final IVarStatement varStatement = (IVarStatement) parent;
      final String identifierName = varStatement.getIdentifierName();
      return varStatement.isFieldDeclaration() &&
              (identifierName == null ||
               (!pe.getName().equals(identifierName.toString()) &&
                // _duplicate_<id> is indicative of a duplicate variable name, which distinguishes all duplicates from the first of the duplicates
                !(identifierName.toString().endsWith("_duplicate_" + pe.getName()))));
    }
    return false;
  }

  private boolean isBlockParameter(@NotNull INameInDeclaration pe) {
    final IParsedElement parent = pe.getParent();
    return parent instanceof IParameterListClause && parent.getParent() instanceof IBlockExpression;
  }

  private boolean descendsFromTemplateStringLiteral(@Nullable IParsedElement pe) {
    if (pe == null) {
      return false;
    }

    if (pe.getParent() instanceof ITemplateStringLiteral) {
      return true;
    }
    return descendsFromTemplateStringLiteral(pe.getParent());
  }

  private boolean isProgram(@NotNull IParsedElement pe) {
    return pe.getGosuClass() instanceof IGosuProgram;
  }

  private void addTokens(@NotNull CompositeElement node, @NotNull IParsedElement pe, IParseTree after) {
    for (IToken token : pe.getTokens()) {
      addToken(node, token, after, pe instanceof INameInDeclaration );
    }
  }

  private void addToken( @NotNull CompositeElement node, @NotNull IToken token, @Nullable IParseTree after, boolean bIdentfier ) {
    final int tokenType = token.getType();
    final String tokenText = token.getText();
    final IParseTree tokenAfter = token.getAfter();

    if (after == tokenAfter || (after != null && after.isAncestor(tokenAfter))) {
      if (tokenType == ISourceCodeTokenizer.TT_WHITESPACE) {
        node.rawAddChildrenWithoutNotifications(ASTFactory.leaf(GosuTokenTypes.TT_WHITESPACE, tokenText));
      } else if (tokenType == ISourceCodeTokenizer.TT_EOF) {
        // skip
      } else {
        final IElementType elementType = GosuTokenTypes.getTypeFrom(token);
        if (tokenType == ISourceCodeTokenizer.TT_WORD || bIdentfier || isPrimitive(tokenText) ) {
          node.rawAddChildrenWithoutNotifications(new GosuIdentifierImpl(elementType, tokenText));
        } else {
          if (elementType == GosuTokenTypes.TT_COMMENT_MULTILINE && tokenText.startsWith("/**")) {
            node.rawAddChildrenWithoutNotifications(new PsiDocCommentImpl(tokenText));
          } else {
            if( elementType instanceof IGosuElementType ) {
              if( elementType == GosuTokenTypes.TT_COMMENT_MULTILINE ||
                  elementType == GosuTokenTypes.TT_COMMENT_LINE ) {
                node.rawAddChildrenWithoutNotifications( new GosuCommentImpl( elementType, tokenText ) );
              }
              else {
                node.rawAddChildrenWithoutNotifications( new GosuTokenImpl( elementType, tokenText ) );
              }
            }
            else {
              node.rawAddChildrenWithoutNotifications(ASTFactory.leaf(elementType, tokenText));
            }
          }
        }
      }
    }
  }

  private boolean isPrimitive(String text) {
    return text.equals("void") ||
           text.equals("boolean" ) ||
           text.equals("char" ) ||
           text.equals( "byte" ) ||
           text.equals("short" ) ||
           text.equals("int" ) ||
           text.equals("long" ) ||
           text.equals("float" ) ||
           text.equals("double" );
  }

  @Nullable
  private IParseTree findEvaluateFunction(@NotNull IGosuProgram program) {
    IClassStatement classStatement = program.getClassStatement();
    IParseTree location = classStatement.getLocation();
    if(location==null){
      return null;
    }
    for (IParseTree pt : location.getChildren()) {
      if (isEvaluateFunction(pt.getParsedElement())) {
        return pt;
      }
    }
    return null;
  }

  private boolean isEvaluateFunction(@NotNull IParsedElement pe) {
    if (pe instanceof IFunctionStatement) {
      final IDynamicFunctionSymbol dfs = ((IFunctionStatement) pe).getDynamicFunctionSymbol();
      if (dfs instanceof IProgramClassFunctionSymbol) {
        if (dfs.getDisplayName().equals("evaluate")) {
          return true;
        }
      }
    }
    return false;
  }
}

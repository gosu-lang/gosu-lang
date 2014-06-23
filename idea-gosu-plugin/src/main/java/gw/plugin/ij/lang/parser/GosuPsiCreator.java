/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.parser;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import gw.lang.parser.IExpression;
import gw.lang.parser.expressions.ILocalVarDeclaration;
import gw.lang.parser.expressions.IVarStatement;
import gw.plugin.ij.lang.GosuElementType;
import gw.plugin.ij.lang.psi.impl.GosuFragmentFileImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuAnnotationExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuBeanMethodCallExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuBlockExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuDirectiveExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuExpressionListImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuFeatureLiteralExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuFieldAccessExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuIdentifierExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuMemberExpansionExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuMethodCallExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuNameInDeclarationImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuNewExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuParenthesizedExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuPropertyMemberAccessExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuStringLiteralImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeAsExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuAssignmentStatementImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuDoWhileStatementImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuFieldImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuFieldPropertyImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuForEachStatementImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuIfStatementImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuMemberAssignmentStatementImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuModifierListImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuNotAStatementImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuPackageDefinitionImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuStatementListImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuSyntheticModifierListImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuUsesStatementImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuUsesStatementListImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuUsingStatementImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuVariableImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuWhileStatementImpl;
import gw.plugin.ij.lang.psi.impl.statements.params.GosuParameterImpl;
import gw.plugin.ij.lang.psi.impl.statements.params.GosuParameterListImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuAnnotationDefinitionImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuAnonymousClassDefinitionImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuClassDefinitionImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuEnhancementDefinitionImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuEnumConstantImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuEnumDefinitionImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuExtendsClauseImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuImplementsClauseImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuInterfaceDefinitionImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuSyntheticClassDefinitionImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodImpl;
import gw.plugin.ij.lang.psi.impl.types.GosuTypeParameterListImpl;
import gw.plugin.ij.lang.psi.impl.types.GosuTypeVariableExtendsListImpl;
import gw.plugin.ij.lang.psi.impl.types.GosuTypeVariableImpl;
import gw.plugin.ij.lang.psi.impl.types.GosuTypeVariableListImpl;
import org.jetbrains.annotations.NotNull;

import static gw.plugin.ij.lang.psi.impl.GosuFragmentFileImpl.FILE_NAME;
import static gw.plugin.ij.lang.psi.impl.GosuFragmentFileImpl.PSI_CONTEXT;

public class GosuPsiCreator extends GosuElementTypes {
  /**
   * Creates Gosu PSI element by given AST node
   *
   * @return Respective PSI element
   */
  @NotNull
  public static PsiElement createElement(final ASTNode rawNode) {
    IElementType elem = rawNode.getElementType();

    if (elem instanceof GosuFragmentFileImpl.FragmentElementType) {
      PsiElement psi = PSI_CONTEXT.get(rawNode);
      String name = FILE_NAME.get(rawNode);
      GosuFragmentFileImpl psiFile = new GosuFragmentFileImpl( psi.getProject(), rawNode.getText(), name, psi );
      return psiFile;
    }

    GosuCompositeElement node = null;
    if (rawNode instanceof GosuCompositeElement) {
      node = (GosuCompositeElement) rawNode;
    }

    if (elem == GosuElementTypes.ELEM_TYPE____UnhandledParsedElement) {
      return new GosuRawPsiElement(node);
    }
    if (elem.equals(ELEM_TYPE_BlockExpression)) {
      return new GosuBlockExpressionImpl(node);
    }
    if (elem.equals(ELEM_TYPE_NamespaceStatement)) {
      return new GosuPackageDefinitionImpl(node);
    }
    if (elem.equals(ELEM_TYPE_UsesStatement)) {
      return new GosuUsesStatementImpl(node);
    }
    if (elem.equals(ELEM_TYPE_UsesStatementList)) {
      return new GosuUsesStatementListImpl(node);
    }
    if (elem.equals(ENUM_CONSTANT)) {
      return new GosuEnumConstantImpl(node);
    }
    if (elem.equals(FIELD)) {
      return new GosuFieldImpl(node);
    }
    if (elem.equals(FIELD_PROPERTY)) {
      return new GosuFieldPropertyImpl(node);
    }
    if (elem.equals(CLASS_DEFINITION)) {
      if (node instanceof GosuSyntheticCompositeElement) {
        return new GosuSyntheticClassDefinitionImpl((GosuSyntheticCompositeElement) node);
      } else {
        return new GosuClassDefinitionImpl(node);
      }
    }
    if (elem.equals(ANONYMOUS_CLASS_DEFINITION)) {
      return new GosuAnonymousClassDefinitionImpl(node);
    }
    if (elem.equals(INTERFACE_DEFINITION)) {
      return new GosuInterfaceDefinitionImpl(node);
    }
    if (elem.equals(ENUM_DEFINITION)) {
      return new GosuEnumDefinitionImpl(node);
    }
    if (elem.equals(ENHANCEMENT_DEFINITION)) {
      return new GosuEnhancementDefinitionImpl(node);
    }
    if (elem.equals(ANNOTATION_DEFINITION)) {
      return new GosuAnnotationDefinitionImpl(node);
    }
    if (elem.equals(IMPLEMENTS_CLAUSE)) {
      return new GosuImplementsClauseImpl(node);
    }
    if (elem.equals(EXTENDS_CLAUSE)) {
      return new GosuExtendsClauseImpl(node);
    }
    if (elem.equals(METHOD_DEFINITION)) {
      return new GosuMethodImpl(node);
    }
    if (elem.equals(ELEM_TYPE_NameInDeclaration)) {
      return new GosuNameInDeclarationImpl(node);
    }
    if (elem.equals(ELEM_TYPE_ParameterListClause)) {
      return new GosuParameterListImpl(node);
    }
    if (elem.equals(ELEM_TYPE_ModifierListClause)) {
      if( node instanceof GosuSyntheticCompositeElement) {
        return new GosuSyntheticModifierListImpl( (GosuSyntheticCompositeElement)node );
      }
      return new GosuModifierListImpl(node);
    }
    if (elem.equals(ELEM_TYPE_ArgumentListClause)) {
      return new GosuExpressionListImpl(node);
    }
    if (elem.equals(ELEM_TYPE_ParameterDeclaration)) {
      return new GosuParameterImpl(node);
    }
    if (elem.equals(ELEM_TYPE_TypeVariableDefinitionExpression)) {
      return new GosuTypeVariableImpl(node);
    }
    if (elem.equals(ELEM_TYPE_TypeVariableListClause)) {
      return new GosuTypeVariableListImpl(node);
    }
    if (elem.equals(ELEM_TYPE_TypeVariableExtendsListClause)) {
      return new GosuTypeVariableExtendsListImpl(node);
    }
    if (elem.equals(ELEM_TYPE_ParenthesizedExpression)) {
      return new GosuParenthesizedExpressionImpl(node);
    }
    if (elem.equals(ELEM_TYPE_TypeAsExpression)) {
      return new GosuTypeAsExpressionImpl(node);
    }
    if (elem.equals(ELEM_TYPE_TypeLiteral)) {
      return new GosuTypeLiteralImpl(node);
    }
    if (elem.equals(ELEM_TYPE_StringLiteral)) {
      return new GosuStringLiteralImpl(node);
    }
    if (elem.equals(ELEM_TYPE_TypeParameterListClause)) {
      return new GosuTypeParameterListImpl(node);
    }
    if (elem.equals(ELEM_TYPE_VarStatement)) {
      return new GosuVariableImpl<IVarStatement>(node);
    }
    if (elem.equals(ELEM_TYPE_LocalVarDeclaration)) {
      return new GosuVariableImpl<ILocalVarDeclaration>(node);
    }
    if (elem.equals(ELEM_TYPE_IdentifierExpression)) {
      return new GosuIdentifierExpressionImpl(node);
    }
    if (elem.equals(ELEM_TYPE_BeanMethodCallExpression)) {
      return new GosuBeanMethodCallExpressionImpl(node);
    }
    if (elem.equals(ELEM_TYPE_BeanMethodCallStatement)) {
      return new GosuBeanMethodCallStatementImpl(node);
    }
    if (elem.equals(ELEM_TYPE_FieldAccess)) {
      return new GosuFieldAccessExpressionImpl(node);
    }
    if (elem.equals(ELEM_TYPE_MemberAccess)) {
      return new GosuPropertyMemberAccessExpressionImpl(node);
    }
    if (elem.equals(ELEM_TYPE_MethodCallStatement)) {
      return new GosuMethodCallStatementImpl(node);
    }
    if (elem.equals(ELEM_TYPE_MethodCallExpression)) {
      return new GosuMethodCallExpressionImpl(node);
    }
    if (elem.equals(ELEM_TYPE_MemberExpansionAccess)) {
      return new GosuMemberExpansionExpressionImpl(node);
    }
    if (elem.equals(ELEM_TYPE_AnnotationExpression)) {
      return new GosuAnnotationExpressionImpl(node);
    }
    if (elem.equals(ELEM_TYPE_NewExpression)) {
      return new GosuNewExpressionImpl(node);
    }
    if (elem.equals(ELEM_TYPE_NewStatement)) {
      return new GosuNewStatementImpl(node);
    }
    if (elem.equals(ELEM_TYPE_DirectiveExpression)) {
      return new GosuDirectiveExpressionImpl(node);
    }
    if (elem.equals(ELEM_TYPE_ClassDeclaration)) {
      return new GosuNameInDeclarationImpl(node);
    }
    if (elem.equals(ELEM_TYPE_StatementList)) {
      return new GosuStatementListImpl(node);
    }
    if (elem.equals(ELEM_TYPE_AssignmentStatement)) {
      return new GosuAssignmentStatementImpl(node);
    }
    if (elem.equals(ELEM_TYPE_MemberAssignmentStatement)) {
      return new GosuMemberAssignmentStatementImpl(node);
    }
    if (elem.equals(ELEM_TYPE_ForEachStatement)) {
      return new GosuForEachStatementImpl(node);
    }
    if (elem.equals(ELEM_TYPE_IfStatement)) {
      return new GosuIfStatementImpl(node);
    }
    if (elem.equals(ELEM_TYPE_WhileStatement)) {
      return new GosuWhileStatementImpl(node);
    }
    if (elem.equals(ELEM_TYPE_DoWhileStatement)) {
      return new GosuDoWhileStatementImpl(node);
    }
    if (elem.equals(ELEM_TYPE_UsingStatement)) {
      return new GosuUsingStatementImpl(node);
    }
    if (elem.equals(ELEM_TYPE_FeatureLiteral)) {
      return new GosuFeatureLiteralExpressionImpl(node);
    }
    if (elem.equals(ELEM_TYPE_BlockInvocation)) {
      return new GosuBlockInvocationImpl(node);
    }
    if (elem.equals(ELEM_TYPE_NotAStatement)) {
      return new GosuNotAStatementImpl(node);
    }

    if (elem instanceof GosuElementType &&
        IExpression.class.isAssignableFrom(((GosuElementType) elem).getParsedElementType())) {
      return new GosuRawExpression(node);
    } else {
      return new GosuRawPsiElement(node);
    }
  }
}

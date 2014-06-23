/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.psi.PsiElementVisitor;
import gw.plugin.ij.lang.psi.IGosuFileBase;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.IGosuPackageDefinition;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifierList;
import gw.plugin.ij.lang.psi.api.auxilary.annotation.IGosuAnnotation;
import gw.plugin.ij.lang.psi.api.expressions.IGosuExpression;
import gw.plugin.ij.lang.psi.api.expressions.IGosuIdentifier;
import gw.plugin.ij.lang.psi.api.expressions.IGosuReferenceExpression;
import gw.plugin.ij.lang.psi.api.statements.*;
import gw.plugin.ij.lang.psi.api.statements.params.IGosuParameter;
import gw.plugin.ij.lang.psi.api.statements.params.IGosuParameterList;
import gw.plugin.ij.lang.psi.api.statements.typedef.*;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeParameterList;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeVariable;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeVariableList;
import gw.plugin.ij.lang.psi.impl.expressions.*;
import gw.plugin.ij.lang.psi.impl.statements.*;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.ThrowsReferenceList;
import gw.plugin.ij.lang.psi.impl.types.GosuTypeVariableExtendsListImpl;

public abstract class GosuElementVisitor extends PsiElementVisitor
{
  public void visitElement( IGosuPsiElement element )
  {
  }

  public void visitFile( IGosuFileBase file )
  {
    visitElement( file );
  }

  public void visitField( IGosuField field )
  {
    visitVariable( field );
  }

  public void visitNameInDeclaration( GosuNameInDeclarationImpl name )
  {
    visitElement( name );
  }

  public void visitVariable( IGosuVariable variable )
  {
    visitElement( variable );
  }

  public void visitMethod( IGosuMethod method )
  {
    visitElement( method );
  }

  public void visitExpression( IGosuExpression expression )
  {
    visitElement( expression );
  }

  public void visitParameterList( IGosuParameterList parameterList )
  {
    visitElement( parameterList );
  }

  public void visitParameter( IGosuParameter parameter )
  {
    visitVariable( parameter );
  }

  public void visitEnumConstant( IGosuEnumConstant enumConstant )
  {
    visitField( enumConstant );
  }

  public void visitPackageDefinition( IGosuPackageDefinition packageDefinition )
  {
    visitElement( packageDefinition );
  }

  public void visitTypeDefinition( IGosuTypeDefinition typeDefinition )
  {
    visitElement( typeDefinition );
  }

  public void visitImplementsClause( IGosuImplementsClause implementsClause )
  {
    visitElement( implementsClause );
  }

  public void visitExtendsClause( IGosuExtendsClause extendsClause )
  {
    visitElement( extendsClause );
  }

  public void visitModifierList( IGosuModifierList modifierList )
  {
    visitElement( modifierList );
  }

  public void visitUsesStatement( IGosuUsesStatement gosuUsesStatement )
  {
    visitElement( gosuUsesStatement );
  }

  public void visitUsesStatementList( IGosuUsesStatementList gosuUsesStatement )
  {
    visitElement( gosuUsesStatement );
  }

  public void visitBeanMethodCallExpression( GosuBeanMethodCallExpressionImpl callExpression )
  {
    visitElement( callExpression );
  }

  public void visitBlockExpression( GosuBlockExpressionImpl blockExpression )
  {
    visitElement( blockExpression );
  }

  public void visitNewExpression( GosuNewExpressionImpl newExpression )
  {
    visitElement( newExpression );
  }

  public void visitReferenceExpression( IGosuReferenceExpression referenceExpression )
  {
    visitElement( referenceExpression );
  }

  public void visitParenthesizedExpression( GosuParenthesizedExpressionImpl expression )
  {
    visitElement( expression );
  }

  public void visitAnnotation( IGosuAnnotation annotation )
  {
    visitElement( annotation );
  }

  public void visitIfStatement( GosuIfStatementImpl ifStatement )
  {
    visitElement( ifStatement );
  }

  public void visitWhileStatement( GosuWhileStatementImpl whileStatement )
  {
    visitElement( whileStatement );
  }

  public void visitDirectiveExpression( GosuDirectiveExpressionImpl directiveExpression )
  {
    visitElement( directiveExpression );
  }

  public void visitExpressionList( GosuExpressionListImpl expressionList )
  {
    visitElement( expressionList );
  }

  public void visitFeatureLiteralExpression( GosuFeatureLiteralExpressionImpl featureLiteralExpression )
  {
    visitElement( featureLiteralExpression );
  }

  public void visitIdentifierExpression( GosuIdentifierExpressionImpl identifierExpression )
  {
    visitElement( identifierExpression );
  }

  public void visitIdentifier( IGosuIdentifier identifier )
  {
    visitElement( identifier );
  }

  public void visitMemberExpansionExpression( GosuMemberExpansionExpressionImpl memberExpansionExpression )
  {
    visitElement( memberExpansionExpression );
  }

  public void visitMethodCallExpression( GosuMethodCallExpressionImpl methodCallExpression )
  {
    visitElement( methodCallExpression );
  }

  public void visitFieldAccessExpression(GosuFieldAccessExpressionImpl propertyMemberAccessExpression)
  {
    visitElement( propertyMemberAccessExpression );
  }

  public void visitTypeAsExpression( GosuTypeAsExpressionImpl typeAsExpression )
  {
    visitElement( typeAsExpression );
  }

  public void visitTypeLiteral( GosuTypeLiteralImpl typeLiteral )
  {
    visitElement( typeLiteral );
  }

  public void visitStringLiteral( GosuStringLiteralImpl expr )
  {
    visitElement( expr );
  }

  public void visitAssignmentStatement( GosuAssignmentStatementImpl assignmentStatement )
  {
    visitElement( assignmentStatement );
  }


  public void visitDoWhileStatement( GosuDoWhileStatementImpl doWhileStatement )
  {
    visitElement( doWhileStatement );
  }

  public void visitFieldProperty( IGosuFieldProperty fieldProperty )
  {
    visitElement( fieldProperty );
  }

  public void visitForEachStatement( GosuForEachStatementImpl forEachStatement )
  {
    visitElement( forEachStatement );
  }

  public void visitMemberAssignmentStatement( GosuMemberAssignmentStatementImpl memberAssignmentStatement )
  {
    visitElement( memberAssignmentStatement );
  }

  public void visitStatementList( IGosuStatementList statementList )
  {
    visitElement( statementList );
  }

  public void visitUsingStatement( GosuUsingStatementImpl usingStatement )
  {
    visitElement( usingStatement );
  }

  public void visitThrowsReferenceList( ThrowsReferenceList throwsReferenceList )
  {
    visitElement( throwsReferenceList );
  }

  public void visitAnnotationDefinition( IGosuAnnotationDefinition annotationDefinition )
  {
    visitElement( annotationDefinition );
  }

  public void visitAnonymousClassDefinition( IGosuAnonymousClassDefinition anonymousClassDefinition )
  {
    visitElement( anonymousClassDefinition );
  }

  public void visitClassDefinition( IGosuClassDefinition classDefinition )
  {
    visitElement( classDefinition );
  }

  public void visitEnhancementDefinition( IGosuEnhancementDefinition enhancementDefinition )
  {
    visitElement( enhancementDefinition );
  }

  public void visitEnumDefinition( IGosuEnumDefinition enumDefinition )
  {
    visitElement( enumDefinition );
  }

  public void visitInterfaceDefinition( IGosuInterfaceDefinition interfaceDefinition )
  {
    visitElement( interfaceDefinition );
  }


  public void visitTypeParameterList( IGosuTypeParameterList typeParameterList )
  {
    visitElement( typeParameterList );
  }


  public void visitTypeVariableExtendsList( GosuTypeVariableExtendsListImpl typeVariableExtendsList )
  {
    visitElement( typeVariableExtendsList );
  }

  public void visitTypeVariable( IGosuTypeVariable typeVariable )
  {
    visitElement( typeVariable );
  }

  public void visitTypeVariableList( IGosuTypeVariableList typeVariableList )
  {
    visitElement( typeVariableList );
  }

  public void visitClassFile( GosuClassFileImpl classFile )
  {
    visitElement( classFile );
  }
}

/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.parser;

import com.intellij.psi.tree.TokenSet;
import gw.lang.parser.expressions.*;
import gw.lang.parser.statements.IArrayAssignmentStatement;
import gw.lang.parser.statements.IAssignmentStatement;
import gw.lang.parser.statements.IBeanMethodCallStatement;
import gw.lang.parser.statements.IBlockInvocationStatement;
import gw.lang.parser.statements.IBreakStatement;
import gw.lang.parser.statements.ICaseClause;
import gw.lang.parser.statements.ICatchClause;
import gw.lang.parser.statements.IClassDeclaration;
import gw.lang.parser.statements.IClasspathStatement;
import gw.lang.parser.statements.IContinueStatement;
import gw.lang.parser.statements.IDoWhileStatement;
import gw.lang.parser.statements.IEvalStatement;
import gw.lang.parser.statements.IForEachStatement;
import gw.lang.parser.statements.IIfStatement;
import gw.lang.parser.statements.IMapAssignmentStatement;
import gw.lang.parser.statements.IMemberAssignmentStatement;
import gw.lang.parser.statements.IMethodCallStatement;
import gw.lang.parser.statements.INamespaceStatement;
import gw.lang.parser.statements.INewStatement;
import gw.lang.parser.statements.INoOpStatement;
import gw.lang.parser.statements.INotAStatement;
import gw.lang.parser.statements.IReturnStatement;
import gw.lang.parser.statements.IStatementList;
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
import gw.plugin.ij.lang.GosuElementType;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.psi.GosuStubElementType;
import gw.plugin.ij.lang.psi.api.statements.IGosuField;
import gw.plugin.ij.lang.psi.api.statements.IGosuFieldProperty;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuAnnotationDefinition;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuAnonymousClassDefinition;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuClassDefinition;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuEnhancementDefinition;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuEnumConstant;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuEnumDefinition;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuExtendsClause;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuImplementsClause;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuInterfaceDefinition;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import gw.plugin.ij.lang.psi.stubs.GosuFieldPropertyStub;
import gw.plugin.ij.lang.psi.stubs.GosuFieldStub;
import gw.plugin.ij.lang.psi.stubs.GosuMethodStub;
import gw.plugin.ij.lang.psi.stubs.GosuReferenceListStub;
import gw.plugin.ij.lang.psi.stubs.GosuTypeDefinitionStub;
import gw.plugin.ij.lang.psi.stubs.elements.GosuAnnotationDefinitionElementType;
import gw.plugin.ij.lang.psi.stubs.elements.GosuAnonymousClassDefinitionElementType;
import gw.plugin.ij.lang.psi.stubs.elements.GosuClassDefinitionElementType;
import gw.plugin.ij.lang.psi.stubs.elements.GosuEnhancementDefinitionElementType;
import gw.plugin.ij.lang.psi.stubs.elements.GosuEnumConstantElementType;
import gw.plugin.ij.lang.psi.stubs.elements.GosuEnumDefinitionElementType;
import gw.plugin.ij.lang.psi.stubs.elements.GosuExtendsClauseElementType;
import gw.plugin.ij.lang.psi.stubs.elements.GosuFieldElementType;
import gw.plugin.ij.lang.psi.stubs.elements.GosuFieldPropertyElementType;
import gw.plugin.ij.lang.psi.stubs.elements.GosuImplementsClauseElementType;
import gw.plugin.ij.lang.psi.stubs.elements.GosuInterfaceDefinitionElementType;
import gw.plugin.ij.lang.psi.stubs.elements.GosuMethodElementType;

public class GosuElementTypes extends GosuTokenTypes {
  // type definitions
  public static final GosuStubElementType<GosuTypeDefinitionStub, IGosuClassDefinition> CLASS_DEFINITION = new GosuClassDefinitionElementType();
  public static final GosuStubElementType<GosuTypeDefinitionStub, IGosuAnonymousClassDefinition> ANONYMOUS_CLASS_DEFINITION = new GosuAnonymousClassDefinitionElementType();
  public static final GosuStubElementType<GosuTypeDefinitionStub, IGosuInterfaceDefinition> INTERFACE_DEFINITION = new GosuInterfaceDefinitionElementType();
  public static final GosuStubElementType<GosuTypeDefinitionStub, IGosuEnumDefinition> ENUM_DEFINITION = new GosuEnumDefinitionElementType();
  public static final GosuStubElementType<GosuTypeDefinitionStub, IGosuEnhancementDefinition> ENHANCEMENT_DEFINITION = new GosuEnhancementDefinitionElementType();
  public static final GosuStubElementType<GosuTypeDefinitionStub, IGosuAnnotationDefinition> ANNOTATION_DEFINITION = new GosuAnnotationDefinitionElementType();

  // clauses
  public static final GosuStubElementType<GosuReferenceListStub, IGosuImplementsClause> IMPLEMENTS_CLAUSE = new GosuImplementsClauseElementType();
  public static final GosuStubElementType<GosuReferenceListStub, IGosuExtendsClause> EXTENDS_CLAUSE = new GosuExtendsClauseElementType();

  // declarations
  public static final GosuStubElementType<GosuFieldStub, IGosuEnumConstant> ENUM_CONSTANT = new GosuEnumConstantElementType();
  public static final GosuStubElementType<GosuFieldStub, IGosuField> FIELD = new GosuFieldElementType();
  public static final GosuStubElementType<GosuFieldPropertyStub, IGosuFieldProperty> FIELD_PROPERTY = new GosuFieldPropertyElementType();
  public static final GosuStubElementType<GosuMethodStub, IGosuMethod> METHOD_DEFINITION = new GosuMethodElementType();
  //## todo:  public GosuStubElementType<GosuAnnotationMethodStub, GosuAnnotationMethod> ANNOTATION_METHOD = new GosuAnnotationMethodElementType();

  // various
  public static final GosuElementType REFERENCE_ELEMENT = new GosuElementType("reference element");
  public static final GosuElementType ELEM_TYPE____UnhandledParsedElement = new GosuElementType("UnhandledParsedElement");

  // basic
  public static final GosuElementType ELEM_TYPE_NamespaceStatement = new GosuElementType(INamespaceStatement.class);
  public static final GosuElementType ELEM_TYPE_ClassDeclaration = new GosuElementType(IClassDeclaration.class);
  public static final GosuElementType ELEM_TYPE_StatementList = new GosuElementType(IStatementList.class);
  public static final GosuElementType ELEM_TYPE_NameInDeclaration = new GosuElementType(INameInDeclaration.class);
  public static final GosuElementType ELEM_TYPE_VarStatement = new GosuElementType(IVarStatement.class);
  public static final GosuElementType ELEM_TYPE_AssignmentStatement = new GosuElementType(IAssignmentStatement.class);
  public static final GosuElementType ELEM_TYPE_MemberAssignmentStatement = new GosuElementType(IMemberAssignmentStatement.class);
  public static final GosuElementType ELEM_TYPE_ArrayAssignmentStatement = new GosuElementType(IArrayAssignmentStatement.class);
  public static final GosuElementType ELEM_TYPE_MapAssignmentStatement = new GosuElementType(IMapAssignmentStatement.class);
  public static final GosuElementType ELEM_TYPE_MethodCallStatement = new GosuElementType(IMethodCallStatement.class);
  public static final GosuElementType ELEM_TYPE_BlockInvocationStatement = new GosuElementType(IBlockInvocationStatement.class);
  public static final GosuElementType ELEM_TYPE_BeanMethodCallStatement = new GosuElementType(IBeanMethodCallStatement.class);
  public static final GosuElementType ELEM_TYPE_ReturnStatement = new GosuElementType(IReturnStatement.class);
  public static final GosuElementType ELEM_TYPE_BreakStatement = new GosuElementType(IBreakStatement.class);
  public static final GosuElementType ELEM_TYPE_ContinueStatement = new GosuElementType(IContinueStatement.class);
  public static final GosuElementType ELEM_TYPE_IfStatement = new GosuElementType(IIfStatement.class);
  public static final GosuElementType ELEM_TYPE_WhileStatement = new GosuElementType(IWhileStatement.class);
  public static final GosuElementType ELEM_TYPE_DoWhileStatement = new GosuElementType(IDoWhileStatement.class);
  public static final GosuElementType ELEM_TYPE_ForEachStatement = new GosuElementType(IForEachStatement.class);
  public static final GosuElementType ELEM_TYPE_SwitchStatement = new GosuElementType(ISwitchStatement.class);
  public static final GosuElementType ELEM_TYPE_CaseClause = new GosuElementType(ICaseClause.class);
  public static final GosuElementType ELEM_TYPE_TryCatchFinallyStatement = new GosuElementType(ITryCatchFinallyStatement.class);
  public static final GosuElementType ELEM_TYPE_CatchClause = new GosuElementType(ICatchClause.class);
  public static final GosuElementType ELEM_TYPE_ThrowStatement = new GosuElementType(IThrowStatement.class);
  public static final GosuElementType ELEM_TYPE_UsingStatement = new GosuElementType(IUsingStatement.class);
  public static final GosuElementType ELEM_TYPE_EvalStatement = new GosuElementType(IEvalStatement.class);
  public static final GosuElementType ELEM_TYPE_SyntheticFunctionStatement = new GosuElementType(ISyntheticFunctionStatement.class);
  public static final GosuElementType ELEM_TYPE_SyntheticMemberAccessStatement = new GosuElementType(ISyntheticMemberAccessStatement.class);
  public static final GosuElementType ELEM_TYPE_NoOpStatement = new GosuElementType(INoOpStatement.class);
  public static final GosuElementType ELEM_TYPE_ClasspathStatement = new GosuElementType(IClasspathStatement.class);
  public static final GosuElementType ELEM_TYPE_UsesStatementList = new GosuElementType(IUsesStatementList.class);
  public static final GosuElementType ELEM_TYPE_UsesStatement = new GosuElementType(IUsesStatement.class);
  public static final GosuElementType ELEM_TYPE_IdentifierExpression = new GosuElementType(IIdentifierExpression.class);
  public static final GosuElementType ELEM_TYPE_TypeAsExpression = new GosuElementType(ITypeAsExpression.class);
  public static final GosuElementType ELEM_TYPE_TypeIsExpression = new GosuElementType(ITypeIsExpression.class);
  public static final GosuElementType ELEM_TYPE_TypeOfExpression = new GosuElementType(ITypeOfExpression.class);
  public static final GosuElementType ELEM_TYPE_StaticTypeOfExpression = new GosuElementType(IStaticTypeOfExpression.class);
  public static final GosuElementType ELEM_TYPE_StringLiteral = new GosuElementType(IStringLiteralExpression.class);
  public static final GosuElementType ELEM_TYPE_CharLiteral = new GosuElementType(ICharLiteralExpression.class);
  public static final GosuElementType ELEM_TYPE_NumericLiteral = new GosuElementType(INumericLiteralExpression.class);
  public static final GosuElementType ELEM_TYPE_TypeLiteral = new GosuElementType(ITypeLiteralExpression.class);
  public static final GosuElementType ELEM_TYPE_TypeParameterListClause = new GosuElementType(ITypeParameterListClause.class);
  public static final GosuElementType ELEM_TYPE_TypeVariableDefinitionExpression = new GosuElementType(ITypeVariableDefinitionExpression.class);
  public static final GosuElementType ELEM_TYPE_TypeVariableListClause = new GosuElementType(ITypeVariableListClause.class);
  public static final GosuElementType ELEM_TYPE_TypeVariableExtendsListClause = new GosuElementType(ITypeVariableExtendsListClause.class);
  public static final GosuElementType ELEM_TYPE_BooleanLiteral = new GosuElementType(IBooleanLiteralExpression.class);
  //##todo  public static GosuElementType ELEM_TYPE_DefaultArgLiteral = new GosuElementType(IDefaultArgLiteral.class);
  public static final GosuElementType ELEM_TYPE_UnaryExpression = new GosuElementType(IUnaryExpression.class);
  public static final GosuElementType ELEM_TYPE_UnaryNotPlusMinusExpression = new GosuElementType(IUnaryNotPlusMinusExpression.class);
  public static final GosuElementType ELEM_TYPE_EqualityExpression = new GosuElementType(IEqualityExpression.class);
  public static final GosuElementType ELEM_TYPE_IdentityExpression = new GosuElementType(IIdentityExpression.class);
  public static final GosuElementType ELEM_TYPE_RelationalExpression = new GosuElementType(IRelationalExpression.class);
  public static final GosuElementType ELEM_TYPE_ConditionalOrExpression = new GosuElementType(IConditionalOrExpression.class);
  public static final GosuElementType ELEM_TYPE_ConditionalAndExpression = new GosuElementType(IConditionalAndExpression.class);
  public static final GosuElementType ELEM_TYPE_AdditiveExpression = new GosuElementType(IAdditiveExpression.class);
  public static final GosuElementType ELEM_TYPE_MultiplicativeExpression = new GosuElementType(IMultiplicativeExpression.class);
  public static final GosuElementType ELEM_TYPE_BitshiftExpression = new GosuElementType(IBitshiftExpression.class);
  public static final GosuElementType ELEM_TYPE_BitwiseOrExpression = new GosuElementType(IBitwiseOrExpression.class);
  public static final GosuElementType ELEM_TYPE_BitwiseXorExpression = new GosuElementType(IBitwiseXorExpression.class);
  public static final GosuElementType ELEM_TYPE_BitwiseAndExpression = new GosuElementType(IBitwiseAndExpression.class);
  public static final GosuElementType ELEM_TYPE_BeanMethodCallExpression = new GosuElementType(IBeanMethodCallExpression.class);
  public static final GosuElementType ELEM_TYPE_MethodCallExpression = new GosuElementType(IMethodCallExpression.class);
  public static final GosuElementType ELEM_TYPE_MemberExpansionAccess = new GosuElementType(IMemberExpansionExpression.class);
  public static final GosuElementType ELEM_TYPE_FieldAccess = new GosuElementType(IFieldAccessExpression.class);
  public static final GosuElementType ELEM_TYPE_MemberAccess = new GosuElementType(IMemberAccessExpression.class);
  public static final GosuElementType ELEM_TYPE_NewExpression = new GosuElementType(INewExpression.class);
  public static final GosuElementType ELEM_TYPE_NewStatement = new GosuElementType(INewStatement.class);
  public static final GosuElementType ELEM_TYPE_AnnotationExpression = new GosuElementType(IAnnotationExpression.class);
  public static final GosuElementType ELEM_TYPE_EvalExpression = new GosuElementType(IEvalExpression.class);
  public static final GosuElementType ELEM_TYPE_QueryExpression = new GosuElementType(IQueryExpression.class);
  public static final GosuElementType ELEM_TYPE_ConditionalTernaryExpression = new GosuElementType(IConditionalTernaryExpression.class);
  public static final GosuElementType ELEM_TYPE_ArrayAccess = new GosuElementType(IArrayAccessExpression.class);
  public static final GosuElementType ELEM_TYPE_MapAccess = new GosuElementType(IMapAccessExpression.class);
  public static final GosuElementType ELEM_TYPE_IntervalExpression = new GosuElementType(IIntervalExpression.class);
  public static final GosuElementType ELEM_TYPE_ParenthesizedExpression = new GosuElementType(IParenthesizedExpression.class);
  public static final GosuElementType ELEM_TYPE_NullExpression = new GosuElementType(INullExpression.class);
  public static final GosuElementType ELEM_TYPE_BlockExpression = new GosuElementType(IBlockExpression.class);
  public static final GosuElementType ELEM_TYPE_TemplateStringLiteral = new GosuElementType(ITemplateStringLiteral.class);
  public static final GosuElementType ELEM_TYPE_ExistsExpression = new GosuElementType(IExistsExpression.class);
  public static final GosuElementType ELEM_TYPE_BlockInvocation = new GosuElementType(IBlockInvocation.class);
  public static final GosuElementType ELEM_TYPE_FeatureLiteral = new GosuElementType(IFeatureLiteralExpression.class);
  public static final GosuElementType ELEM_TYPE_ParameterDeclaration = new GosuElementType(IParameterDeclaration.class);
  public static final GosuElementType ELEM_TYPE_ParameterListClause = new GosuElementType(IParameterListClause.class);
  public static final GosuElementType ELEM_TYPE_ModifierListClause = new GosuElementType(IModifierListClause.class);
  public static final GosuElementType ELEM_TYPE_ArgumentListClause = new GosuElementType(IArgumentListClause.class);
  public static final GosuElementType ELEM_TYPE_CollectionInitializerExpression = new GosuElementType(ICollectionInitializerExpression.class);
  public static final GosuElementType ELEM_TYPE_MapInitializerExpression = new GosuElementType(IMapInitializerExpression.class);
  public static final GosuElementType ELEM_TYPE_ObjectInitializerExpression = new GosuElementType(IObjectInitializerExpression.class);
  public static final GosuElementType ELEM_TYPE_NotAStatement = new GosuElementType(INotAStatement.class);
  public static final GosuElementType ELEM_TYPE_LocalVarDeclaration = new GosuElementType(ILocalVarDeclaration.class);
  public static final GosuElementType ELEM_TYPE_DirectiveExpression = new GosuElementType(IDirectiveExpression.class);
  public static final GosuElementType ELEM_TYPE_NotAWordExpression = new GosuElementType(INotAWordExpression.class);

  public static final TokenSet TYPE_DEFINITION_TYPES = TokenSet.create(
      CLASS_DEFINITION,
      ENHANCEMENT_DEFINITION,
      INTERFACE_DEFINITION,
      ENUM_DEFINITION,
      ANNOTATION_DEFINITION
  );
}
/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.codeInspection.expression;


import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.ex.BaseLocalInspectionTool;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.impl.DirectoryIndex;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.PsiDocumentManagerImpl;
import com.intellij.psi.scope.util.PsiScopesUtil;
import com.intellij.util.Query;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.ThisSymbol;
import gw.internal.gosu.parser.TypeLoaderAccess;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.statements.ClassStatement;
import gw.lang.GosuShop;
import gw.lang.parser.IExpression;
import gw.lang.parser.IHasInnerClass;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IScope;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.Keyword;
import gw.lang.parser.expressions.IMemberAccessExpression;
import gw.lang.parser.expressions.IMemberExpansionExpression;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.parser.expressions.IStringLiteralExpression;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.resources.Res;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.parser.statements.INamespaceStatement;
import gw.lang.parser.statements.INoOpStatement;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.reflect.IEnumType;
import gw.lang.reflect.IEnumValue;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.TypeName;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.intentions.CaseMismatchSimpleQuickFix;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.parser.GosuRawPsiElement;
import gw.plugin.ij.lang.psi.api.IGosuPackageDefinition;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuParserConfigurer;
import gw.plugin.ij.lang.psi.impl.expressions.*;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodImpl;
import gw.plugin.ij.util.GosuBundle;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class GosuCaseMismatchReferenceInspection extends BaseLocalInspectionTool {

  @Nls
  @NotNull
  @Override
  public String getGroupDisplayName() {
    return GosuBundle.message( "inspection.group.name.case.mismatch.issues" );
  }

  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return GosuBundle.message( "inspection.case.mismatch.reference.plain" );
  }

  @Override
  public boolean isEnabledByDefault() {
    // Must turn this on explicitly, too much of an impact on performance for regular use
    return false;
  }

  @NotNull
  @Override
  public PsiElementVisitor buildVisitor( @NotNull final ProblemsHolder holder, boolean isOnTheFly, @NotNull LocalInspectionToolSession session ) {
    return new GosuElementVisitor() {

      @Override
      public void visitElement( PsiElement element ) {
        if( element instanceof GosuRawPsiElement ) {
          IParsedElement pe = ((GosuRawPsiElement)element).getParsedElement();

          if( isDoNotVerify( pe ) ) {
            return;
          }

          if( pe instanceof INoOpStatement ) {
            IParseTree loc = pe.getLocation();
            if( loc != null ) {
              List<IParseTree> children = loc.getChildren();
              if( children.size() == 0 ) {
                String wrongName = loc.getTextFromTokens();
                if( wrongName != null && !wrongName.isEmpty() ) {
                  handleIdentifierOrMethodCall( element, element.getFirstChild(), pe );
                }
              }
            }
          }
        }
        else if( element instanceof GosuIdentifierImpl ) {

          // Special handling for property get/set mismatch...

          PsiElement parent = element.getParent();
          if( parent instanceof GosuMethodImpl ) {
            IFunctionStatement pe = ((GosuMethodImpl)parent).getParsedElement();
            if( pe != null && pe.hasParseException( Res.MSG_EXPECTING_PROPERTY_GET_OR_SET_MODIFIER ) ) {
              if( isPrecededByPropertyKeyword( element ) ) {
                String correctName = findCorrectCaseKeyword( element.getText() );
                if( correctName != null ) {
                  holder.registerProblem( element, GosuBundle.message( "inspection.case.mismatch.reference", "Keyword: " + correctName ), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new CaseMismatchFix( element, correctName ) );
                }
              }
            }
          }
          // Special handling for top level class/enum with wrong keyword case
          else if( parent instanceof GosuRawPsiElement ) {
            IParsedElement pe = ((GosuRawPsiElement)parent).getParsedElement();
            if (pe instanceof INoOpStatement && pe.getParent() instanceof ClassStatement ) {
              String correctName = findCorrectCaseKeyword( element.getText() );
              if( correctName != null ) {
                holder.registerProblem( element, GosuBundle.message( "inspection.case.mismatch.reference", "Keyword: " + correctName ), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new CaseMismatchFix( element, correctName ) );
              }
            }
          }
        }
      }

      private boolean isPrecededByPropertyKeyword( PsiElement element ) {
        element = element.getPrevSibling();
        while( element instanceof PsiWhiteSpace ) {
          element = element.getPrevSibling();
        }
        return element != null && element.getText().equalsIgnoreCase( Keyword.KW_property.getName() );
      }

      private boolean isDoNotVerify( IParsedElement pe ) {
        if( pe == null ) {
          return false;
        }

        IGosuClass gsClass = pe.getGosuClass();
        if( gsClass == null ) {
          return false;
        }

        IModule module = gsClass.getTypeLoader().getModule();
        if( module == null ) {
          return false;
        }

        TypeSystem.pushModule( module );
        try {
          IType outer = TypeLord.getOuterMostEnclosingClass( gsClass );
          IType type = TypeSystem.getByFullNameIfValid( "gw.testharness.DoNotVerifyResource" );
          return type != null && outer.getTypeInfo().hasAnnotation( type );
        }
        finally {
          TypeSystem.popModule( module );
        }
      }

      @Override
      public void visitStringLiteral( GosuStringLiteralImpl expr ) {
        IExpression pe = expr.getParsedElement();
        if( pe == null ) {
          return;
        }
        if( isDoNotVerify( pe ) ) {
          return;
        }
        // Fix typekey string literal case mismatch
        if( pe instanceof IStringLiteralExpression &&
            pe.hasImmediateParseIssue( Res.MSG_VALUE_MISMATCH ) ) {
          IType type = pe.getImmediateParseIssue( Res.MSG_VALUE_MISMATCH ).getExpectedType();
          if( type instanceof IEnumType ) {
            String wrongName = expr.getText();
            String quote = String.valueOf( wrongName.charAt( 0 ) );
            if( wrongName.startsWith( "&quot;" ) ) {
              quote = "&quot;";
            }
            for( IEnumValue value : ((IEnumType)type).getEnumValues() ) {
              String strCorrectCase = quote + value.getCode() + quote;
              if( equalsDifferentCase( strCorrectCase, wrongName ) ) {
                holder.registerProblem( expr, GosuBundle.message( "inspection.case.mismatch.reference", "Enum constant:" + strCorrectCase ), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new CaseMismatchFix( expr, strCorrectCase ) );
              }
            }
          }
        }
      }

      @Override
      public void visitIdentifierExpression( GosuIdentifierExpressionImpl expr ) {
        IParsedElement pe = expr.getParsedElement();
        if( pe == null ) {
          return;
        }
        if( !pe.hasParseExceptions() ) {
          return;
        }
        if( isDoNotVerify( pe ) ) {
          return;
        }
        handleIdentifierOrMethodCall( expr, expr.getReferenceNameElement(), pe );
      }

      @Override
      public void visitMethodCallExpression( GosuMethodCallExpressionImpl expr ) {
        IMethodCallExpression pe = expr.getParsedElement();
        if( pe == null ) {
          return;
        }
        if( !pe.hasImmediateParseIssue( Res.MSG_NO_SUCH_FUNCTION ) ) {
          return;
        }
        if( isDoNotVerify( pe ) ) {
          return;
        }
        handleIdentifierOrMethodCall( expr, expr.getReferenceNameElement(), pe );
      }

      @Override
      public void visitPackageDefinition( IGosuPackageDefinition expr ) {
        INamespaceStatement stmt = (INamespaceStatement)expr.getParsedElement();
        if( stmt == null ) {
          return;
        }
        if( isDoNotVerify( stmt ) ) {
          return;
        }
        IGosuClass gsClass = stmt.getGosuClass();
        if( gsClass == null ) {
          return;
        }
        IClassStatement classStmt = gsClass.getClassStatement();
        if( !classStmt.hasImmediateParseIssue( Res.MSG_WRONG_NAMESPACE ) &&
            !classStmt.hasImmediateParseIssue( Res.MSG_WRONG_CLASSNAME ) ) {
          return;
        }
        IGosuCodeReferenceElement packageExpr = expr.getPackageReference();
        if( packageExpr == null ) {
          return;
        }
        String strCorrectCase = gsClass.getNamespace();
        if( strCorrectCase != null &&
            !strCorrectCase.equals( packageExpr.getText() ) &&
            strCorrectCase.equalsIgnoreCase( packageExpr.getText() ) ) {
          holder.registerProblem( packageExpr, GosuBundle.message( "inspection.case.mismatch.reference", "Package :" + strCorrectCase ), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new CaseMismatchFix( packageExpr, strCorrectCase ) );
        }
      }

      private void handleIdentifierOrMethodCall( PsiElement elem, PsiElement refElem, IParsedElement pe ) {
        if( refElem == null ) {
          return;
        }
        if( pe == null ) {
          return;
        }
        IGosuClassInternal gsClass = (IGosuClassInternal)pe.getGosuClass();
        if( gsClass == null ) {
          return;
        }
        if( isNoOpOnSameLineAsExistingProblem( refElem ) ) {
          // Avoid trying to fix multiple "unexpecte token" related problems,
          // chances are the first such error needs to be fixed, but not subsequent ones
          return;
        }
        IModule module = gsClass.getTypeLoader().getModule();
        if( module == null ) {
          return;
        }
        TypeSystem.pushModule( module );
        try {
          String caseKind = "Symbol";
          String wrongName = refElem.getText();
          String strCorrectCase = findCorrectSymbolFromSymTableCase( elem, gsClass, wrongName );
          if( strCorrectCase == null ) {
            caseKind = "Relative type";
            strCorrectCase = findCorrectRelativeTypeCase( elem, (IGosuClassInternal)pe.getGosuClass(), wrongName );
            if( strCorrectCase == null ) {
              caseKind = "Enum constant";
              strCorrectCase = findCorrectEnumConstCase( pe, wrongName );
              if( strCorrectCase == null ) {
                caseKind = "Keyword";
                strCorrectCase = findCorrectCaseKeyword( wrongName );
              }
            }
          }
          if( strCorrectCase != null ) {
            holder.registerProblem( refElem, GosuBundle.message( "inspection.case.mismatch.reference", caseKind + ":" + strCorrectCase ), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new CaseMismatchFix( refElem, strCorrectCase ) );
          }
        }
        finally {
          TypeSystem.popModule( module );
        }
      }

      private boolean isNoOpOnSameLineAsExistingProblem( PsiElement refElem )
      {
        if( !(refElem instanceof GosuIdentifierImpl) ||
             refElem.getParent().getNode().getElementType() != GosuElementTypes.ELEM_TYPE_NoOpStatement ) {
          return false;
        }
        int lineNumber = findLineNumber( refElem );
        boolean bRet = false;
        for( ProblemDescriptor p : holder.getResults() ) {
          holder.registerProblem( p );
          if( !bRet &&
              p.getPsiElement().getContainingFile() == refElem.getContainingFile() &&
              p.getLineNumber() == lineNumber ) {
            bRet = true;
          }
        }
        return bRet;
      }

      private int findLineNumber( PsiElement psi ) {
        final Document document = PsiDocumentManagerImpl.getInstance( psi.getProject() ).getDocument( psi.getContainingFile() );
        return document.getLineNumber( psi.getTextOffset() ) + 1;
      }

      private String findLocalVarCorrectCase( PsiElement expr, String wrongName ) {
        FindLocalVarInScope localFinder = new FindLocalVarInScope( wrongName );
        PsiScopesUtil.treeWalkUp( localFinder, expr, null );
        PsiLocalVariable localFound = localFinder.getLocalFound();
        if( localFound != null ) {
          String text = localFound.getNameIdentifier().getText();
          if( equalsDifferentCase( text, wrongName ) ) {
            return text;
          }
        }
        return null;
      }

      @Override
      public void visitTypeLiteral( GosuTypeLiteralImpl expr ) {
        ITypeLiteralExpression pe = expr.getParsedElement();
        if( pe == null ) {
          return;
        }
        if( !pe.hasImmediateParseIssue( Res.MSG_INVALID_TYPE ) &&
            !pe.hasImmediateParseIssue( Res.MSG_TYPE_MISMATCH ) ) {
          return;
        }
        if( isDoNotVerify( pe ) ) {
          return;
        }
        IModule module = GosuModuleUtil.findModuleForPsiElement( expr );
        if( module == null ) {
          return;
        }
        TypeSystem.pushModule( module );
        try {
          PsiElement referenceNameElement = expr.getReferenceNameElement();
          if( referenceNameElement == null ) {
            return;
          }
          String wrongName = referenceNameElement.getText();
          if( pe.hasImmediateParseIssue( Res.MSG_INVALID_TYPE ) ) {
            String caseKind = "Type literal";
            String strCorrectCase = findCorrectRelativeTypeCase( expr, (IGosuClassInternal)pe.getGosuClass(), wrongName );
            if( strCorrectCase == null ) {
              caseKind = "Keyword";
              strCorrectCase = findCorrectCaseKeyword( wrongName );
            }
            if( strCorrectCase != null ) {
              holder.registerProblem(referenceNameElement, GosuBundle.message( "inspection.case.mismatch.reference", caseKind + ": " + strCorrectCase ), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new CaseMismatchFix(referenceNameElement, strCorrectCase ) );
            }
          }
          else {
            String strCorrectCase = findCorrectSymbolFromSymTableCase(expr, (IGosuClassInternal) pe.getGosuClass(), referenceNameElement.getText());
            if( strCorrectCase != null ) {
              holder.registerProblem(referenceNameElement, GosuBundle.message( "inspection.case.mismatch.reference", "Symbol: " + strCorrectCase ), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new CaseMismatchFix(referenceNameElement, strCorrectCase ) );
            }
          }
        }
        finally {
          TypeSystem.popModule( module );
        }
      }


      @Override
      public void visitFieldAccessExpression(GosuFieldAccessExpressionImpl expr) {
        IMemberAccessExpression pe = expr.getParsedElement();
        if( pe == null ) {
          return;
        }

        IParseIssue noPropErr = pe.getImmediateParseIssue( Res.MSG_NO_PROPERTY_DESCRIPTOR_FOUND );
        IParseIssue noTypeErr = pe.getImmediateParseIssue( Res.MSG_NO_TYPE_ON_NAMESPACE );
        if( noPropErr == null && noTypeErr == null ) {
          return;
        }

        if( isDoNotVerify( pe ) ) {
          return;
        }

        IModule module = GosuModuleUtil.findModuleForPsiElement( expr );
        if( module == null ) {
          return;
        }
        TypeSystem.pushModule( module );
        try {
          String strCorrectCase = noPropErr != null
                                  ? findCorrectPropertyCase( pe, pe.getMemberName() )
                                  : findCorrectTypeCase( pe, pe.getMemberName() );
          if( strCorrectCase != null ) {
            holder.registerProblem( expr.getReferenceNameElement(), GosuBundle.message( "inspection.case.mismatch.reference", "Member reference: " + strCorrectCase ), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new CaseMismatchFix( expr.getReferenceNameElement(), strCorrectCase ) );
          }
          else if( noTypeErr != null && expr.getQualifier() != null ) {
            // It may be that a qualifier is mistaken for a namespace e.g,. contact.TaxID s/b Contact.TaxID
            strCorrectCase = findCorrectSymbolFromSymTableCase( expr.getQualifier(), (IGosuClassInternal)pe.getGosuClass(), expr.getQualifier().getText() );
            if( strCorrectCase != null ) {
              holder.registerProblem( expr.getQualifier(), GosuBundle.message( "inspection.case.mismatch.reference", "Symbol: " + strCorrectCase ), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new CaseMismatchFix( expr.getQualifier(), strCorrectCase ) );
            }
          }
          else if( noPropErr != null && noPropErr.getPlainMessage().contains( Keyword.KW_static.getName() ) ) {
            // It may be that a "no static property" error is really masking a "no inner class" error...
            strCorrectCase = findCorrectTypeCase( pe, pe.getMemberName() );
            if( strCorrectCase != null ) {
              holder.registerProblem( expr.getReferenceNameElement(), GosuBundle.message( "inspection.case.mismatch.reference", "Inner class: " + strCorrectCase ), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new CaseMismatchFix( expr.getReferenceNameElement(), strCorrectCase ) );
            }
            else {
              // Fix case where improperly cased identifier is confused for relative type e.g., Claim.someNonstaticMethod() s/b claim.someNonstaticMethod()
              PsiExpression refElem = expr.getQualifier();
              strCorrectCase = findCorrectSymbolFromSymTableCase( refElem, (IGosuClassInternal)pe.getGosuClass(), expr.getQualifier().getText() );
              if( strCorrectCase != null ) {
                holder.registerProblem( refElem, GosuBundle.message( "inspection.case.mismatch.reference", "Symbol reference: " + strCorrectCase ), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new CaseMismatchFix( refElem, strCorrectCase ) );
              }
            }
          }
        }
        finally {
          TypeSystem.popModule( module );
        }
      }

      @Override
      public void visitMemberExpansionExpression( GosuMemberExpansionExpressionImpl expr ) {
        IMemberExpansionExpression pe = expr.getParsedElement();
        if( pe == null ) {
          return;
        }
        if( !pe.hasImmediateParseIssue( Res.MSG_NO_PROPERTY_DESCRIPTOR_FOUND ) ) {
          return;
        }
        if( isDoNotVerify( pe ) ) {
          return;
        }
        IModule module = GosuModuleUtil.findModuleForPsiElement( expr );
        if( module == null ) {
          return;
        }
        TypeSystem.pushModule( module );
        try {
          String strCorrectCase = findCorrectPropertyCase( pe, pe.getMemberName() );
          if( strCorrectCase != null ) {
            holder.registerProblem( expr.getReferenceNameElement(), GosuBundle.message( "inspection.case.mismatch.reference", "Member expansion: " + strCorrectCase ), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new CaseMismatchFix( expr.getReferenceNameElement(), strCorrectCase ) );
          }
        }
        finally {
          TypeSystem.popModule( module );
        }
      }

      @Override
      public void visitBeanMethodCallExpression( GosuBeanMethodCallExpressionImpl expr ) {
        IMemberAccessExpression pe = expr.getParsedElement();
        if( pe == null ) {
          return;
        }
        PsiElement refElem = expr.getReferenceNameElement();
        if( refElem == null ) {
          return;
        }
        IModule module = GosuModuleUtil.findModuleForPsiElement( expr );
        if( module == null ) {
          return;
        }
        if( isDoNotVerify( pe ) ) {
          return;
        }
        TypeSystem.pushModule( module );
        try {
          String strCorrectCase = null;
          if( pe.hasImmediateParseIssue( Res.MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD ) ) {
            strCorrectCase = findCorrectMethodCase( pe, refElem.getText() );
            if( strCorrectCase != null ) {
              holder.registerProblem( expr, GosuBundle.message( "inspection.case.mismatch.reference", "Method reference: " + strCorrectCase ), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new CaseMismatchFix( refElem, strCorrectCase ) );
            }
          }
          if( strCorrectCase == null ) {
            if( pe.hasImmediateParseIssue( Res.MSG_METHOD_IS_NOT_STATIC ) ||
                pe.hasImmediateParseIssue( Res.MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD ) ) {
              // Fix case where improperly cased identifier is confused for relative type e.g., Claim.someNonstaticMethod() s/b claim.someNonstaticMethod()
              refElem = expr.getQualifier();
              if( refElem != null ) {
                strCorrectCase = findCorrectSymbolFromSymTableCase( refElem, (IGosuClassInternal)pe.getGosuClass(), refElem.getText() );
                if( strCorrectCase != null ) {
                  holder.registerProblem( refElem, GosuBundle.message( "inspection.case.mismatch.reference", "Symbol reference: " + strCorrectCase ), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new CaseMismatchFix( refElem, strCorrectCase ) );
                }
              }
            }
          }
        }
        finally {
          TypeSystem.popModule( module );
        }
      }

      private String findCorrectPropertyCase( IMemberAccessExpression pe, String wrongName ) {
        ITypeInfo rootTypeInfo = pe instanceof IMemberExpansionExpression
                                 ? pe.getRootType().getComponentType().getTypeInfo()
                                 : pe.getRootType().getTypeInfo();
        List<? extends IPropertyInfo> props = rootTypeInfo instanceof IRelativeTypeInfo
                                              ? ((IRelativeTypeInfo)rootTypeInfo).getProperties( pe.getGosuClass() )
                                              : rootTypeInfo.getProperties();
        for( IPropertyInfo pi : props ) {
          if( equalsDifferentCase( pi.getName(), wrongName ) ) {
            return pi.getName();
          }
        }
        return null;
      }

      private String findCorrectTypeCase( IMemberAccessExpression pe, String wrongName ) {
        IType rootType = pe.getRootType();
        if( rootType instanceof INamespaceType ) {
          for( TypeName tn : ((INamespaceType)rootType).getChildren( pe.getGosuClass() ) ) {
            String fullName = tn.name.replace( '$', '.' );
            String name = StringUtil.getShortName( fullName );
            if( equalsDifferentCase( wrongName, name ) ) {
              return name;
            }
          }
        }
        else if( rootType instanceof IMetaType ) {
          IType type = TypeLord.getPureGenericType( ((IMetaType)rootType).getType() );
          if( type instanceof IHasInnerClass ) {
            for( IType inner : ((IHasInnerClass)type).getInnerClasses() ) {
              String relativeName = StringUtil.getShortName( inner.getRelativeName() );
              if( equalsDifferentCase( wrongName, relativeName ) ) {
                return relativeName;
              }
            }
          }
        }
        return null;
      }

      private boolean equalsDifferentCase( String name1, String name2 ) {
        return name2 != null && name1 != null && !name2.equals( name1 ) && name2.equalsIgnoreCase( name1 );
      }

      private String findCorrectMethodCase( IMemberAccessExpression pe, String wrongName ) {
        // Note, this doesn't take into account overloading. But the worst that can happen
        // is that we replace a "method not found" error with a "illegal arg" error.  Too
        // few of those to make a difference.
        ITypeInfo rootTypeInfo = pe.getRootType().getTypeInfo();
        List<? extends IMethodInfo> methods = rootTypeInfo instanceof IRelativeTypeInfo
                                              ? ((IRelativeTypeInfo)rootTypeInfo).getMethods( pe.getGosuClass() )
                                              : rootTypeInfo.getMethods();
        for( IMethodInfo mi : methods ) {
          if( equalsDifferentCase( mi.getDisplayName(), wrongName ) ) {
            return mi.getDisplayName();
          }
        }
        return null;
      }

      private String findCorrectCaseKeyword( String text ) {
        for( String kw : Keyword.getAll() ) {
          if( equalsDifferentCase( kw, text ) ) {
            return kw;
          }
        }
        return null;
      }

      private String findCorrectSymbolFromSymTableCase( PsiElement elem, IGosuClassInternal gsClass, String wrongName ) {
        String correctName = findLocalVarCorrectCase( elem, wrongName );
        if( correctName != null ) {
          return correctName;
        }
        ISymbolTable symTable = GosuParserConfigurer.getSymbolTable( (AbstractGosuClassFileImpl)elem.getContainingFile() );
        symTable = symTable == null ? GosuShop.createSymbolTable() : symTable;
        IScope scope = symTable.pushScope();
        try {
          gsClass.putClassMembers( null, symTable, gsClass, true );
          gsClass.putClassMembers( null, symTable, gsClass, false );
          symTable.putSymbol( new ThisSymbol( JavaTypes.OBJECT(), symTable ) );
          putFunctionParameters( symTable, elem );
          for( Object sym : symTable.getSymbols().values() ) {
            String displayName = ((ISymbol)sym).getDisplayName();
            if( sym != null && equalsDifferentCase( displayName, wrongName ) ) {
              return displayName;
            }
          }
          return null;
        }
        finally {
          symTable.popScope( scope );
        }
      }

      private void putFunctionParameters( ISymbolTable symTable, PsiElement elem ) {
        PsiElement csr = elem;
        while( csr != null && !(csr instanceof GosuMethodImpl) ) {
          csr = csr.getParent();
        }
        if( csr != null ) {
          IFunctionStatement pe = ((GosuMethodImpl)csr).getParsedElement();
          if( pe != null && pe.getDynamicFunctionSymbol() != null ) {
            List<ISymbol> args = pe.getDynamicFunctionSymbol().getArgs();
            for( ISymbol sym : args ) {
              symTable.putSymbol( sym );
            }
          }
        }
      }

      private String findCorrectEnumConstCase( IParsedElement pe, String wrongName ) {
        if( pe.getParseExceptions().isEmpty() ) {
          return null;
        }
        IType expectedType = pe.getParseExceptions().get( 0 ).getExpectedType();
        if( expectedType != null ) {
          expectedType = TypeLord.getCoreType( expectedType );
          if( expectedType.isEnum() ) {
            for( String value : ((IEnumType)expectedType).getEnumConstants() ) {
              if( equalsDifferentCase( value, wrongName ) ) {
                return value;
              }
            }
          }
        }
        return null;
      }

      private String findCorrectRelativeTypeCase( PsiElement elem, IGosuClassInternal gsClass, String wrongName ) {
        if( wrongName == null || wrongName.isEmpty() ) {
          return null;
        }
        String correctName = invertCapitalization( wrongName );
        if( _findCorrectRelativeTypeCase( elem, gsClass, correctName ) ) {
          return correctName;
        }
        String lowercase = wrongName.toLowerCase();
        if( !lowercase.equals( wrongName ) ) {
          if( _findCorrectRelativeTypeCase( elem, gsClass, lowercase ) ) {
            return lowercase;
          } else {
            return _findCorrectRelativeTypeCaseInCurrentPackage(elem, gsClass, lowercase);
          }
        }
        return null;
      }

      private String _findCorrectRelativeTypeCaseInCurrentPackage(PsiElement elem, IGosuClassInternal gsClass, String wrongName) {
        ITypeUsesMap typeUses = ((IGosuClassInternal) TypeLord.getOuterMostEnclosingClass(gsClass)).getTypeUsesMap();
        IModule mod = GosuModuleUtil.findModuleForPsiElement(elem);
        if (mod == null || typeUses == null) {
          return null;
        }
        Set<IUsesStatement> usesStatements = typeUses.getUsesStatements();
        for (IUsesStatement usesStmt : usesStatements) {
          String typeName = getTypeName(usesStmt.getTypeName());
          if (typeName.toLowerCase().equals(wrongName)) {
            return typeName;
          }
        }
        Set<String> namespaces = typeUses.getNamespaces();
        for (String namespace : namespaces) {
          String namespaceNoDot = maybeRemoveTrailingDot(namespace);
          if (!namespaceNoDot.equals("")) {
            INamespaceType nameSpace = TypeSystem.getNamespace(namespaceNoDot, mod);
            if (nameSpace != null) {
              Set<TypeName> typeNames = nameSpace.getChildren(null);
              for (TypeName tn : typeNames) {
                String tnLow = tn.name.toLowerCase();
                if (tnLow.equals(namespace.toLowerCase() + wrongName)) {
                  return getTypeName(tn.name);
                }
              }
            }
          }
        }
        return null;
      }

      private String getTypeName(String name) {
        int b = name.lastIndexOf(".");
        if(b == -1) {
          return null;
        }
        return name.substring(b + 1);
      }

      private String maybeRemoveTrailingDot(String s) {
        String ret = s;
        if (s.endsWith(".")) {
          ret = s.substring(0, s.length() - 1);
        }
        return ret;
      }

      private boolean _findCorrectRelativeTypeCase( PsiElement elem, IGosuClassInternal gsClass, String correctName ) {
        // Is this a relative type name?
        ITypeUsesMap typeUses = ((IGosuClassInternal)TypeLord.getOuterMostEnclosingClass( gsClass )).getTypeUsesMap();
        IType type = TypeLoaderAccess.instance().getTypeByRelativeNameIfValid_NoGenerics( correctName, typeUses );
        if( type != null ) {
          return true;
        }
        else {
          // Is this a root package name?
          IModule mod = GosuModuleUtil.findModuleForPsiElement( elem );
          if( mod != null ) {
            Query<VirtualFile> dirs = DirectoryIndex.getInstance( elem.getProject() ).getDirectoriesByPackageName( correctName, true );
            if( !dirs.findAll().isEmpty() ) {
              return true;
            }
          }
        }
        return false;
      }

      private String invertCapitalization( String wrongName ) {
        if( wrongName.length() == 0 ) {
          return null;
        }
        StringBuilder correctName = new StringBuilder( wrongName );
        if( Character.isLowerCase( wrongName.charAt( 0 ) ) ) {
          correctName.replace( 0, 1, String.valueOf( Character.toUpperCase( wrongName.charAt( 0 ) ) ) );
        }
        else {
          correctName.replace( 0, 1, String.valueOf( Character.toLowerCase( wrongName.charAt( 0 ) ) ) );
        }
        return correctName.toString();
      }
    };
  }

  private class CaseMismatchFix implements LocalQuickFix {
    private final CaseMismatchSimpleQuickFix _quickFix;

    public CaseMismatchFix( PsiElement id, String correctName ) {
      _quickFix = new CaseMismatchSimpleQuickFix( id, correctName );
    }

    @NotNull
    public String getName() {
      return _quickFix.getText();
    }

    @NotNull
    public String getFamilyName() {
      return GosuBundle.message( "inspection.group.name.case.mismatch.issues" );
    }

    public void applyFix( @NotNull Project project, @NotNull ProblemDescriptor descriptor ) {
      PsiElement element = descriptor.getPsiElement();
      if( element == null ) {
        return;
      }
      final PsiFile psiFile = element.getContainingFile();
      if( _quickFix.isAvailable( project, null, psiFile ) ) {
        _quickFix.invoke( project, null, psiFile );
      }
    }
  }
}

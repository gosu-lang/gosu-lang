/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiQualifiedNamedElement;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiMatcherImpl;
import com.intellij.psi.util.PsiMatchers;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.INotAWordExpression;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.IGosuFile;
import gw.plugin.ij.lang.psi.api.statements.IGosuUsesStatement;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeParameterList;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuFragmentFileImpl;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiTypeResolver;
import gw.plugin.ij.lang.psi.impl.statements.GosuUsesStatementListImpl;
import gw.plugin.ij.lang.psi.impl.types.GosuTypeVariableImpl;
import gw.plugin.ij.lang.psi.util.ElementTypeMatcher;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class GosuTypeLiteralImpl extends GosuReferenceExpressionImpl<ITypeLiteralExpression> implements IGosuCodeReferenceElement, IGosuTypeElement {
  public GosuTypeLiteralImpl(GosuCompositeElement node) {
    super(node);
  }

  @Nullable
  public PsiElement getReferenceNameElement() {
    return findLastChildByType(GosuElementTypes.TT_IDENTIFIER);
  }

  @Override
  public IGosuCodeReferenceElement getQualifier() {
    final PsiElement qualifier = getFirstChild();
    return qualifier instanceof IGosuCodeReferenceElement ? (IGosuCodeReferenceElement) qualifier : null;
  }

  @Override
  public void setQualifier(IGosuCodeReferenceElement newQualifier) {
    throw new UnsupportedOperationException("Men at work");
  }

  @Nullable
  protected IType getTypeReferenced() {
    IParsedElement pe = getParsedElementImpl();
    if (pe != null && !(pe instanceof ITypeLiteralExpression)) {
      if( getContainingFile() instanceof AbstractGosuClassFileImpl ) {
        // This can happen for example when a package is deleted. The annotator will try to resolve the reference which may
        // have been parsed separate from the psi. In this case we must reparse here. Not the getParsedElementImpl() normally
        // handles synchronization issues such as this by checking that the psi text is the same as the parsed text, but in
        // this case the source is the same, only the semantics are different b/c of the package deletion.
        ((AbstractGosuClassFileImpl)getContainingFile()).reparseGosuFromPsi();
        pe = getParsedElementImpl();
      }
    }
    if (pe == null || pe instanceof INotAWordExpression) {
      return null;
    } else if( pe instanceof ITypeLiteralExpression ) {
      return ((ITypeLiteralExpression) pe).getType().getType();
    } else if (pe instanceof Identifier) {
      return  ((Identifier) pe).getType();
    } else {
      return null;
    }
  }

  @Override
  public PsiElement resolve() {
    IType type = getTypeReferenced();
    if( type != null ) {
      if (type instanceof ITypeVariableType) {
        return PsiTypeResolver.resolveTypeVariable( (ITypeVariableType)type, this );
      }
      else if( type instanceof IBlockType ) {
        return null;
      }
      else {
        return PsiTypeResolver.resolveType(type, this);
      }
    }
    return null;
  }

  @NotNull
  public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
    final PsiElement elt = bindType(this, element, (AbstractGosuClassFileImpl) getContainingFile());
    return elt != null ? elt : this;
  }

  public static PsiElement bindType(@NotNull PsiExpression originalElement, @NotNull PsiElement newElement, @NotNull AbstractGosuClassFileImpl gosuFile) {
    String originalTypeName = originalElement.getText();

    //TODO-dp sometimes this throws a class cast exception because the parsed element is an identifier rather than a type literal
    try {
      if (originalElement instanceof GosuTypeLiteralImpl) {
        IType typeReferenced = ((GosuTypeLiteralImpl) originalElement).getTypeReferenced();
        originalTypeName = typeReferenced != null ? typeReferenced.getName() : originalElement.getText();
      }
    } catch (ClassCastException e) {
      // ignore
    }

    originalTypeName = removeParameterization(originalTypeName);

    final String qname;
    if (newElement instanceof GosuTypeVariableImpl) {
      qname = ((GosuTypeVariableImpl) newElement).getName();
    } else if (newElement instanceof PsiQualifiedNamedElement) {
      qname = ((PsiQualifiedNamedElement) newElement).getQualifiedName();
    } else if (newElement instanceof PsiClass) {
      qname = ((PsiClass) newElement).getQualifiedName();
    } else {
      throw new IncorrectOperationException("Unsupported element in GosuTypeLiteralImpl.bindToElement");
    }

    PsiElement newTypeLiteral;
    if (originalElement.getText().contains(".")) {
      newTypeLiteral = GosuPsiParseUtil.parseExpression(qname, originalElement.getManager());
      originalElement.replace(newTypeLiteral);
    } else {
      newTypeLiteral = GosuPsiParseUtil.parseRelativeTypeLiteral(qname, gosuFile);
      PsiElement id1 = originalElement.getFirstChild();
      PsiElement id2 = newTypeLiteral.getFirstChild();
      if (id1 != null && id2 != null && !id1.getText().equals(id2.getText())) {
        id1.replace(id2);
        gosuFile.reparseGosuFromPsi();
      }
    }

    if (shouldAddImport(gosuFile, originalElement, newElement, originalTypeName)) {
      gosuFile.addImport(qname);
      maybeRemoveStaleImport(originalTypeName, gosuFile);
      gosuFile.reparseGosuFromPsi();
    }

    return newTypeLiteral;
  }

  @NotNull
  private static String removeParameterization(@NotNull String originalTypeName) {
    if (originalTypeName.contains("<")) {
      originalTypeName = originalTypeName.substring(0, originalTypeName.indexOf("<")).trim();
    }
    return originalTypeName;
  }

  private static void maybeRemoveStaleImport(String originalTypeName, AbstractGosuClassFileImpl gosuFile) {
    final PsiElement staleUsesStmt = new PsiMatcherImpl(gosuFile)
        .descendant(new ElementTypeMatcher(GosuElementTypes.ELEM_TYPE_UsesStatementList))
        .descendant(PsiMatchers.hasText(originalTypeName))
        .dot(new ElementTypeMatcher(GosuElementTypes.ELEM_TYPE_TypeLiteral))
        .ancestor(new ElementTypeMatcher(GosuElementTypes.ELEM_TYPE_UsesStatement))
        .getElement();
    if (staleUsesStmt != null) {
      staleUsesStmt.delete();
    }
  }

  private static boolean shouldAddImport(@NotNull AbstractGosuClassFileImpl gosuFile, @NotNull PsiElement originalElement, @NotNull PsiElement newElement, String qname) {
    // no need to add imports for inner clasees
    PsiFile psiFile = PsiTreeUtil.getParentOfType(newElement, PsiFile.class);
    if (psiFile == gosuFile) {
      return false;
    }

    if( gosuFile instanceof GosuFragmentFileImpl ) {
      // Gosu framents (from the debugger expressio) add import implicitly
      ((GosuFragmentFileImpl)gosuFile).addImportsFromString( ((PsiClass)newElement).getQualifiedName() );
      return false;
    }

    GosuUsesStatementListImpl usesStatementList = PsiTreeUtil.findChildOfType(gosuFile, GosuUsesStatementListImpl.class);
    if (usesStatementList != null) {
      for (IGosuUsesStatement usesStatement : usesStatementList.getUsesStatements()) {
        if (usesStatement.getText().equals("uses " + qname)) {
          return false;
        }
      }
    }

    gosuFile.reparseGosuFromPsi();
    IClassFileStatement classFileStatement = gosuFile.getParseData().getClassFileStatement();
    if (newElement instanceof PsiClass) {
      String qualifiedName = ((PsiClass) newElement).getQualifiedName();
      if (qualifiedName != null) {
        int iLastDot = qualifiedName.lastIndexOf('.');
        String pkgName = iLastDot > 0 ? qualifiedName.substring(0, iLastDot) : qualifiedName;
        if (pkgName.equals(gosuFile.getPackageName())) {
          return false;
        } else if (((PsiClass) newElement).getName().equals(originalElement.getText())) {
          String newName = originalElement.getText();
          if (!newName.contains(".")) {
            // For the case where the type name is a relative name and is a built-in type e.g., String
            if (isBuiltInType(newName, classFileStatement)) {
              return false;
            }
            // For the case where a class refs another class in the same package by relative name and the refed class is moved outside the package
            if (classFileStatement != null) {
              IGosuClass gsClass = classFileStatement.getGosuClass();
              return !typeUsesMapHasNewTypeOrIsInPackage(gosuFile, gsClass, qualifiedName);
            }
          }
        }
      }
    }

    String rootType = originalElement.getText();
    rootType = removeParameterization(rootType);
    if (classFileStatement != null) {
      IGosuClass gsClass = classFileStatement.getGosuClass();
      ParseResultsException parseResultsException = gsClass.getParseResultsException();
      if (parseResultsException != null) {
        for (IParseIssue error : parseResultsException.getParseExceptions()) {
          if (error.getSource().toString().equals(rootType)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private static boolean isBuiltInType(String relativeName, @NotNull IClassFileStatement classFileStatement) {
    IModule module = classFileStatement.getGosuClass().getTypeLoader().getModule();
    TypeSystem.pushModule(module);
    try {
      IType type = TypeSystem.getByFullNameIfValid(relativeName);
      return type != null;
    } finally {
      TypeSystem.popModule(module);
    }
  }

  private static boolean typeUsesMapHasNewTypeOrIsInPackage(IGosuFile gosuFile, @NotNull IGosuClass gsClass, @NotNull String qualifiedName) {
    ITypeUsesMap usesMap = gsClass.getParser().getTypeUsesMap();
    int iLastDot = qualifiedName.lastIndexOf('.');
    String pkgName = iLastDot > 0 ? qualifiedName.substring(0, iLastDot) : qualifiedName;
    for (String typeUses : (Set<String>) usesMap.getTypeUses()) {
      if (typeUses.equals(qualifiedName) || typeUses.equals(pkgName + '.')) {
        return true;
      }
    }
    return false;
  }

  @Override
  public PsiType[] getTypeArguments() {
    final IGosuTypeParameterList typeParams = getTypeParameterList();
    if (typeParams != null) {
      final List<PsiType> types = Lists.newArrayList();
      for (PsiElement typeParam : typeParams.getChildren()) {
        if (typeParam instanceof GosuTypeLiteralImpl) {
          types.add(((GosuTypeLiteralImpl) typeParam).getType());
        }
      }
      return types.toArray(new PsiType[types.size()]);
    }
    return PsiType.EMPTY_ARRAY;
  }

  @Nullable
  @Override
  public IGosuTypeParameterList getTypeParameterList() {
    return findChildByClass(IGosuTypeParameterList.class);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitTypeLiteral(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}

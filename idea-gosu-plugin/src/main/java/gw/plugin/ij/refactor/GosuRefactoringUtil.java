/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor;

import com.intellij.codeInsight.ExpectedTypeInfo;
import com.intellij.codeInsight.ExpectedTypesProvider;
import com.intellij.codeInsight.daemon.impl.analysis.HighlightControlFlowUtil;
import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.lang.ASTNode;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.GenericsUtil;
import com.intellij.psi.ImplicitVariable;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.JavaRecursiveElementWalkingVisitor;
import com.intellij.psi.JavaResolveResult;
import com.intellij.psi.JavaTokenType;
import com.intellij.psi.PsiAnonymousClass;
import com.intellij.psi.PsiArrayInitializerExpression;
import com.intellij.psi.PsiArrayType;
import com.intellij.psi.PsiAssignmentExpression;
import com.intellij.psi.PsiCall;
import com.intellij.psi.PsiCallExpression;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassInitializer;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiCodeFragment;
import com.intellij.psi.PsiDeclarationStatement;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiDoWhileStatement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiEnumConstant;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiExpressionListStatement;
import com.intellij.psi.PsiExpressionStatement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiForStatement;
import com.intellij.psi.PsiForeachStatement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiIfStatement;
import com.intellij.psi.PsiImportList;
import com.intellij.psi.PsiImportStatement;
import com.intellij.psi.PsiImportStatementBase;
import com.intellij.psi.PsiImportStaticStatement;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiNewExpression;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiParenthesizedExpression;
import com.intellij.psi.PsiPostfixExpression;
import com.intellij.psi.PsiPrefixExpression;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiQualifiedExpression;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.PsiReferenceList;
import com.intellij.psi.PsiResolveHelper;
import com.intellij.psi.PsiReturnStatement;
import com.intellij.psi.PsiStatement;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.PsiSuperExpression;
import com.intellij.psi.PsiSwitchLabelStatement;
import com.intellij.psi.PsiSwitchStatement;
import com.intellij.psi.PsiThisExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeElement;
import com.intellij.psi.PsiTypeParameter;
import com.intellij.psi.PsiTypeParameterList;
import com.intellij.psi.PsiTypeVisitor;
import com.intellij.psi.PsiVariable;
import com.intellij.psi.PsiWhileStatement;
import com.intellij.psi.PsiWildcardType;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.codeStyle.VariableKind;
import com.intellij.psi.controlFlow.ControlFlowUtil;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.InheritanceUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.psi.util.TypeConversionUtil;
import com.intellij.refactoring.PackageWrapper;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.containers.HashMap;
import com.intellij.util.containers.HashSet;
import gnu.trove.THashMap;
import gw.lang.parser.IStatement;
import gw.plugin.ij.lang.GosuElementType;
import gw.plugin.ij.lang.parser.GosuRawPsiElement;
import gw.plugin.ij.lang.psi.api.expressions.IGosuExpression;
import gw.plugin.ij.lang.psi.api.expressions.IGosuReferenceExpression;
import gw.plugin.ij.lang.psi.impl.GosuEnhancementFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuFragmentFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuProgramFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuTemplateFileImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuStatementListImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.refactor.intoduceField.GosuElementToWorkOn;
import gw.plugin.ij.refactor.introduceVariable.GosuIntroduceVariableBase;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Copy of Intellij RefactoringUtil
 * <p/>
 * DO NOT CLEAN !!!
 */
public class GosuRefactoringUtil {
  private static final Logger LOG = Logger.getInstance("#com.intellij.refactoring.util.RefactoringUtil");
  public static final int EXPR_COPY_SAFE = 0;
  public static final int EXPR_COPY_UNSAFE = 1;
  public static final int EXPR_COPY_PROHIBITED = 2;

  private GosuRefactoringUtil() {
  }

  public static boolean isValidToIntroduceField(PsiElement element, @Nullable Editor editor, @NotNull String title, @Nullable String helpID) {
    PsiFile containingFile = element.getContainingFile();
    final String error = containingFile == null ? null :
            containingFile instanceof GosuProgramFileImpl ? GosuBundle.message("error.introduce.field.in.program") :
                    containingFile instanceof GosuTemplateFileImpl ? GosuBundle.message("error.introduce.field.in.template") :
                            containingFile instanceof GosuEnhancementFileImpl ? GosuBundle.message("error.introduce.field.in.enhancement") :
                                    null;
    if (error != null) {
      String message = RefactoringBundle.getCannotRefactorMessage(error);
      CommonRefactoringUtil.showErrorHint(element.getProject(), editor, message, title, helpID);
      return false;
    }
    return true;
  }

  public static boolean isSourceRoot(final PsiDirectory directory) {
    if (directory.getManager() == null) {
      return false;
    }
    final Project project = directory.getProject();
    final VirtualFile virtualFile = directory.getVirtualFile();
    final VirtualFile sourceRootForFile = ProjectRootManager.getInstance(project).getFileIndex().getSourceRootForFile(virtualFile);
    return Comparing.equal(virtualFile, sourceRootForFile);
  }

  public static boolean isInStaticContext(PsiElement element, @Nullable final PsiClass aClass) {
    return PsiUtil.getEnclosingStaticElement(element, aClass) != null;
  }

  public static boolean isResolvableType(PsiType type) {
    return type.accept(new PsiTypeVisitor<Boolean>() {
      public Boolean visitPrimitiveType(PsiPrimitiveType primitiveType) {
        return Boolean.TRUE;
      }

      public Boolean visitArrayType(PsiArrayType arrayType) {
        return arrayType.getComponentType().accept(this);
      }

      public Boolean visitClassType(PsiClassType classType) {
        if (classType.resolve() == null) {
          return Boolean.FALSE;
        }
        PsiType[] parameters = classType.getParameters();
        for (PsiType parameter : parameters) {
          if (parameter != null && !parameter.accept(this).booleanValue()) {
            return Boolean.FALSE;
          }
        }

        return Boolean.TRUE;
      }

      public Boolean visitWildcardType(PsiWildcardType wildcardType) {
        if (wildcardType.getBound() != null) {
          return wildcardType.getBound().accept(this);
        }
        return Boolean.TRUE;
      }
    }).booleanValue();
  }

  public static PsiElement replaceOccurenceWithFieldRef(PsiExpression occurrence, PsiField newField, PsiClass destinationClass)
          throws IncorrectOperationException {
    final PsiManager manager = destinationClass.getManager();
    final PsiClass newFieldClass = getThisClass(newField);
    final PsiClass occurenceClass = getThisClass(occurrence);
    final String fieldName = occurenceClass != null && newFieldClass != null && occurenceClass.equals(newFieldClass) ?
            newField.getName() : newFieldClass.getName() + "." + newField.getName();
//    final JavaPsiFacade facade = JavaPsiFacade.getInstance(manager.getProject());
//    final PsiElement element = occurrence.getUserData(GosuElementToWorkOn.PARENT);
//    final PsiVariable psiVariable = facade.getResolveHelper().resolveAccessibleReferencedVariable(fieldName, element != null ? element : occurrence);
//    if (psiVariable == null || (!psiVariable.getName().equals(fieldName))) {
    return GosuIntroduceVariableBase.replace(occurrence, (PsiExpression) GosuPsiParseUtil.parseExpression(fieldName, manager), manager.getProject());
//    } else {
//      final PsiExpression ref = (PsiExpression) GosuPsiParseUtil.parseExpression("this." + fieldName, manager);
//      if (!occurrence.isValid()) {
//        return null;
//      }
//      if (newField.hasModifierProperty(PsiModifier.STATIC)) {
    //next line inserts static reference to class
//        ref.setQualifierExpression(factory.createReferenceExpression(destinationClass));
//      }
//      return GosuIntroduceVariableBase.replace(occurrence, ref, manager.getProject());
//    }
  }

  /**
   * Cannot use method from code style manager: a collision with fieldToReplace is not a collision
   */
  public static String suggestUniqueVariableName(String baseName, PsiElement place, PsiField fieldToReplace) {
    int index = 0;
    while (true) {
      final String name = index > 0 ? baseName + index : baseName;
      index++;
      final PsiManager manager = place.getManager();
      PsiResolveHelper helper = JavaPsiFacade.getInstance(manager.getProject()).getResolveHelper();
      PsiVariable refVar = helper.resolveAccessibleReferencedVariable(name, place);
      if (refVar != null && !manager.areElementsEquivalent(refVar, fieldToReplace)) {
        continue;
      }
      class CancelException extends RuntimeException {
      }

      try {
        place.accept(new JavaRecursiveElementWalkingVisitor() {
          @Override
          public void visitClass(PsiClass aClass) {

          }

          @Override
          public void visitVariable(PsiVariable variable) {
            if (name.equals(variable.getName())) {
              throw new CancelException();
            }
          }
        });
      } catch (CancelException e) {
        continue;
      }

      return name;
    }
  }

  //order of usages accross different files is irrelevant
  public static void sortDepthFirstRightLeftOrder(final UsageInfo[] usages) {
    Arrays.sort(usages, new Comparator<UsageInfo>() {
      public int compare(final UsageInfo usage1, final UsageInfo usage2) {
        final PsiElement element1 = usage1.getElement();
        final PsiElement element2 = usage2.getElement();
        if (element1 == null || element2 == null) {
          return 0;
        }
        return element2.getTextRange().getStartOffset() - element1.getTextRange().getStartOffset();
      }
    });
  }

  @Nullable
  public static String suggestNewOverriderName(String oldOverriderName, String oldBaseName, String newBaseName) {
    if (oldOverriderName.equals(oldBaseName)) {
      return newBaseName;
    }
    int i;
    if (oldOverriderName.startsWith(oldBaseName)) {
      i = 0;
    } else {
      i = StringUtil.indexOfIgnoreCase(oldOverriderName, oldBaseName, 0);
    }
    if (i >= 0) {
      String newOverriderName = oldOverriderName.substring(0, i);
      if (Character.isUpperCase(oldOverriderName.charAt(i))) {
        newOverriderName += StringUtil.capitalize(newBaseName);
      } else {
        newOverriderName += newBaseName;
      }
      final int j = i + oldBaseName.length();
      if (j < oldOverriderName.length()) {
        newOverriderName += oldOverriderName.substring(j);
      }

      return newOverriderName;
    }
    return null;
  }

  public static boolean hasOnDemandStaticImport(final PsiElement element, final PsiClass aClass) {
    if (element.getContainingFile() instanceof PsiJavaFile) {
      final PsiImportList importList = ((PsiJavaFile) element.getContainingFile()).getImportList();
      if (importList != null) {
        final PsiImportStaticStatement[] importStaticStatements = importList.getImportStaticStatements();
        for (PsiImportStaticStatement stmt : importStaticStatements) {
          if (stmt.isOnDemand() && stmt.resolveTargetClass() == aClass) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public static boolean hasStaticImportOn(final PsiElement expr, final PsiMember member) {
    if (expr.getContainingFile() instanceof PsiJavaFile) {
      final PsiImportList importList = ((PsiJavaFile) expr.getContainingFile()).getImportList();
      if (importList != null) {
        final PsiImportStaticStatement[] importStaticStatements = importList.getImportStaticStatements();
        for (PsiImportStaticStatement stmt : importStaticStatements) {
          if (!stmt.isOnDemand() && stmt.resolveTargetClass() == member.getContainingClass() && Comparing.strEqual(stmt.getReferenceName(), member.getName())) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public static PsiElement replaceElementsWithMap(PsiElement replaceIn, final Map<PsiElement, PsiElement> elementsToReplace) throws IncorrectOperationException {
    for (Map.Entry<PsiElement, PsiElement> e : elementsToReplace.entrySet()) {
      if (e.getKey() == replaceIn) {
        return e.getKey().replace(e.getValue());
      }
      e.getKey().replace(e.getValue());
    }
    return replaceIn;
  }

  public static PsiElement getVariableScope(PsiLocalVariable localVar) {
    if (!(localVar instanceof ImplicitVariable)) {
      return localVar.getParent().getParent();
    } else {
      return ((ImplicitVariable) localVar).getDeclarationScope();
    }
  }

  public static PsiReturnStatement[] findReturnStatements(PsiMethod method) {
    ArrayList<PsiReturnStatement> vector = new ArrayList<>();
    PsiCodeBlock body = method.getBody();
    if (body != null) {
      addReturnStatements(vector, body);
    }
    return vector.toArray(new PsiReturnStatement[vector.size()]);
  }

  private static void addReturnStatements(ArrayList<PsiReturnStatement> vector, PsiElement element) {
    if (element instanceof PsiReturnStatement) {
      vector.add((PsiReturnStatement) element);
    } else if (!(element instanceof PsiClass)) {
      PsiElement[] children = element.getChildren();
      for (PsiElement child : children) {
        addReturnStatements(vector, child);
      }
    }
  }

  public static PsiElement getParentStatement(PsiElement place, boolean skipScopingStatements) {
    PsiElement parent = place;
    while (true) {
      if (GosuRefactoringUtil.isStatement(parent)) {
        break;
      }
      parent = parent.getParent();
      if (parent == null) {
        return null;
      }
    }
    PsiElement parentStatement = parent;
    parent = GosuRefactoringUtil.isStatement(parentStatement) ? parentStatement : parentStatement.getParent();
    while (GosuRefactoringUtil.isStatement(parent) && !(parent instanceof GosuStatementListImpl)) {
      if (!skipScopingStatements && ((parent instanceof PsiForStatement && parentStatement == ((PsiForStatement) parent).getBody()) || (
              parent instanceof PsiForeachStatement && parentStatement == ((PsiForeachStatement) parent).getBody()) || (
              parent instanceof PsiWhileStatement && parentStatement == ((PsiWhileStatement) parent).getBody()) || (
              parent instanceof PsiIfStatement &&
                      (parentStatement == ((PsiIfStatement) parent).getThenBranch() || parentStatement == ((PsiIfStatement) parent).getElseBranch())))) {
        return parentStatement;
      }
      parentStatement = parent;
      parent = parent.getParent();
    }
    return parentStatement;
  }

  public static PsiElement getParentExpressionAnchorElement(PsiElement place) {
    PsiElement parent = place.getUserData(GosuElementToWorkOn.PARENT);
    if (place.getUserData(GosuElementToWorkOn.OUT_OF_CODE_BLOCK) != null) {
      return parent;
    }
    if (parent == null) {
      parent = place;
    }
    while (true) {
      if (isExpressionAnchorElement(parent)) {
        return parent;
      }
      parent = parent.getParent();
      if (parent == null) {
        return null;
      }
    }
  }

  public static boolean isExpressionAnchorElement(PsiElement element) {
    return GosuRefactoringUtil.isStatement(element) || element instanceof PsiClassInitializer || element instanceof PsiField ||
            element instanceof PsiMethod;
  }

  /**
   * @param expression
   * @return loop body if expression is part of some loop's condition or for loop's increment part
   *         null otherwise
   */
  public static PsiElement getLoopForLoopCondition(PsiExpression expression) {
    PsiExpression outermost = expression;
    while (outermost.getParent() instanceof PsiExpression) {
      outermost = (PsiExpression) outermost.getParent();
    }
    if (outermost.getParent() instanceof PsiForStatement) {
      final PsiForStatement forStatement = (PsiForStatement) outermost.getParent();
      if (forStatement.getCondition() == outermost) {
        return forStatement;
      } else {
        return null;
      }
    }
    if (outermost.getParent() instanceof PsiExpressionStatement && outermost.getParent().getParent() instanceof PsiForStatement) {
      final PsiForStatement forStatement = (PsiForStatement) outermost.getParent().getParent();
      if (forStatement.getUpdate() == outermost.getParent()) {
        return forStatement;
      } else {
        return null;
      }
    }
    if (outermost.getParent() instanceof PsiWhileStatement) {
      return outermost.getParent();
    }
    if (outermost.getParent() instanceof PsiDoWhileStatement) {
      return outermost.getParent();
    }
    return null;
  }

  public static PsiClass getThisClass(PsiElement place) {
    PsiElement parent = place.getContext();
    if (parent == null) {
      return null;
    }
    PsiElement prev = null;
    while (true) {
      if (parent instanceof PsiClass) {
        if (!(parent instanceof PsiAnonymousClass && ((PsiAnonymousClass) parent).getArgumentList() == prev)) {
          return (PsiClass) parent;
        }
      }
      prev = parent;
      parent = parent.getContext();
      if (parent == null) {
        return null;
      }
    }
  }

  public static PsiClass getThisResolveClass(final PsiReferenceExpression place) {
    final JavaResolveResult resolveResult = place.advancedResolve(false);
    final PsiElement scope = resolveResult.getCurrentFileResolveScope();
    if (scope instanceof PsiClass) {
      return (PsiClass) scope;
    }
    return null;
  }

  public static PsiCall getEnclosingConstructorCall(PsiJavaCodeReferenceElement ref) {
    PsiElement parent = ref.getParent();
    if (ref instanceof PsiReferenceExpression && parent instanceof PsiMethodCallExpression) {
      return (PsiCall) parent;
    }

    if (parent instanceof PsiAnonymousClass) {
      parent = parent.getParent();
    }

    return parent instanceof PsiNewExpression ? (PsiNewExpression) parent : null;
  }

  public static PsiMethod getEnclosingMethod(PsiElement element) {
    final PsiElement container = PsiTreeUtil.getParentOfType(element, PsiMethod.class, PsiClass.class);
    return container instanceof PsiMethod ? (PsiMethod) container : null;
  }

  public static void renameVariableReferences(PsiVariable variable, String newName, SearchScope scope) throws IncorrectOperationException {
    renameVariableReferences(variable, newName, scope, false);
  }

  public static void renameVariableReferences(PsiVariable variable,
                                              String newName,
                                              SearchScope scope,
                                              final boolean ignoreAccessScope) throws IncorrectOperationException {
    for (PsiReference reference : ReferencesSearch.search(variable, scope, ignoreAccessScope)) {
      reference.handleElementRename(newName);
    }
  }

  public static boolean canBeDeclaredFinal(PsiVariable variable) {
    LOG.assertTrue(variable instanceof PsiLocalVariable || variable instanceof PsiParameter);
    final boolean isReassigned = HighlightControlFlowUtil
            .isReassigned(variable, new THashMap<PsiElement, Collection<ControlFlowUtil.VariableInfo>>());
    return !isReassigned;
  }

  public static PsiThisExpression createThisExpression(PsiManager manager, PsiClass qualifierClass) throws IncorrectOperationException {
    return GosuRefactoringUtil.createQualifiedExpression(manager, qualifierClass, "this");
  }

  public static PsiSuperExpression createSuperExpression(PsiManager manager, PsiClass qualifierClass) throws IncorrectOperationException {
    return GosuRefactoringUtil.createQualifiedExpression(manager, qualifierClass, "super");
  }

  private static <T extends PsiQualifiedExpression> T createQualifiedExpression(PsiManager manager, PsiClass qualifierClass, String qName) throws IncorrectOperationException {
    PsiElementFactory factory = JavaPsiFacade.getInstance(manager.getProject()).getElementFactory();
    if (qualifierClass != null) {
      T qualifiedThis = (T) factory.createExpressionFromText("q." + qName, null);
      qualifiedThis = (T) CodeStyleManager.getInstance(manager.getProject()).reformat(qualifiedThis);
      PsiJavaCodeReferenceElement thisQualifier = qualifiedThis.getQualifier();
      LOG.assertTrue(thisQualifier != null);
      thisQualifier.bindToElement(qualifierClass);
      return qualifiedThis;
    } else {
      return (T) factory.createExpressionFromText(qName, null);
    }
  }

  /**
   * removes a reference to the specified class from the reference list given
   *
   * @return if removed  - a reference to the class or null if there were no references to this class in the reference list
   */
  public static PsiJavaCodeReferenceElement removeFromReferenceList(PsiReferenceList refList, PsiClass aClass)
          throws IncorrectOperationException {
    PsiJavaCodeReferenceElement[] refs = refList.getReferenceElements();
    for (PsiJavaCodeReferenceElement ref : refs) {
      if (ref.isReferenceTo(aClass)) {
        PsiJavaCodeReferenceElement refCopy = (PsiJavaCodeReferenceElement) ref.copy();
        ref.delete();
        return refCopy;
      }
    }
    return null;
  }

  public static PsiJavaCodeReferenceElement findReferenceToClass(PsiReferenceList refList, PsiClass aClass) {
    PsiJavaCodeReferenceElement[] refs = refList.getReferenceElements();
    for (PsiJavaCodeReferenceElement ref : refs) {
      if (ref.isReferenceTo(aClass)) {
        return ref;
      }
    }
    return null;
  }

  public static PsiType getTypeByExpressionWithExpectedType(PsiExpression expr) {
    PsiType type = getTypeByExpression(expr);
    if (type != null) {
      return type;
    }
    ExpectedTypeInfo[] expectedTypes = ExpectedTypesProvider.getExpectedTypes(expr, false);
    if (expectedTypes.length == 1) {
      type = expectedTypes[0].getType();
      if (!type.equalsToText("java.lang.Object")) {
        return type;
      }
    }
    return null;
  }

  public static PsiType getTypeByExpression(PsiExpression expr) {
    PsiElementFactory factory = JavaPsiFacade.getInstance(expr.getProject()).getElementFactory();
    return getTypeByExpression(expr, factory);
  }

  private static PsiType getTypeByExpression(PsiExpression expr, final PsiElementFactory factory) {
    PsiType type = expr.getType();
    if (type == null) {
      if (expr instanceof PsiArrayInitializerExpression) {
        PsiExpression[] initializers = ((PsiArrayInitializerExpression) expr).getInitializers();
        if (initializers.length > 0) {
          PsiType initType = getTypeByExpression(initializers[0]);
          if (initType == null) {
            return null;
          }
          return initType.createArrayType();
        }
      }
      return null;
    }
    PsiClass refClass = PsiUtil.resolveClassInType(type);
    if (refClass instanceof PsiAnonymousClass) {
      type = ((PsiAnonymousClass) refClass).getBaseClassType();
    }
    if (PsiType.NULL.equals(type)) {
      ExpectedTypeInfo[] infos = ExpectedTypesProvider.getExpectedTypes(expr, false);
      if (infos.length == 1) {
        type = infos[0].getType();
      } else {
        type = factory.createTypeByFQClassName("java.lang.Object", expr.getResolveScope());
      }
    }

    return GenericsUtil.getVariableTypeByExpressionType(type);
  }

  public static boolean isAssignmentLHS(PsiElement element) {
    PsiElement parent = element.getParent();

    return parent instanceof PsiAssignmentExpression && element.equals(((PsiAssignmentExpression) parent).getLExpression()) ||
            isPlusPlusOrMinusMinus(parent);
  }

  public static boolean isPlusPlusOrMinusMinus(PsiElement element) {
    if (element instanceof PsiPrefixExpression) {
      return ((PsiPrefixExpression) element).getOperationTokenType() == JavaTokenType.PLUSPLUS ||
              ((PsiPrefixExpression) element).getOperationTokenType() == JavaTokenType.MINUSMINUS;
    } else if (element instanceof PsiPostfixExpression) {
      IElementType operandTokenType = ((PsiPostfixExpression) element).getOperationTokenType();
      return operandTokenType == JavaTokenType.PLUSPLUS || operandTokenType == JavaTokenType.MINUSMINUS;
    } else {
      return false;
    }
  }

  private static void removeFinalParameters(PsiMethod method) throws IncorrectOperationException {
    // Remove final parameters
    PsiParameterList paramList = method.getParameterList();
    PsiParameter[] params = paramList.getParameters();

    for (PsiParameter param : params) {
      if (param.hasModifierProperty(PsiModifier.FINAL)) {
        PsiUtil.setModifierProperty(param, PsiModifier.FINAL, false);
      }
    }
  }

  public static PsiElement getAnchorElementForMultipleExpressions(@NotNull PsiExpression[] occurrences, PsiElement scope) {
    PsiElement anchor = null;
    for (PsiExpression occurrence : occurrences) {
      //  if (!occurrence.isPhysical()) continue;
      if (scope != null && !PsiTreeUtil.isAncestor(scope, occurrence, false)) {
        continue;
      }
      PsiElement anchor1 = getParentExpressionAnchorElement(occurrence);
      if (anchor1 == null) {
        return null;
      }

      if (anchor == null) {
        anchor = anchor1;
      } else {
        PsiElement commonParent = PsiTreeUtil.findCommonParent(anchor, anchor1);
        if (commonParent == null || anchor.getTextRange() == null || anchor1.getTextRange() == null) {
          return null;
        }
        PsiElement firstAnchor = anchor.getTextRange().getStartOffset() < anchor1.getTextRange().getStartOffset() ? anchor : anchor1;
        if (commonParent.equals(firstAnchor)) {
          anchor = firstAnchor;
        } else {
          if (GosuRefactoringUtil.isStatement(commonParent) && !(commonParent instanceof GosuStatementListImpl)) {
            anchor = commonParent;
          } else {
            PsiElement parent = firstAnchor;
            while (!parent.getParent().equals(commonParent)) {
              parent = parent.getParent();
            }
            final PsiElement newAnchor = getParentExpressionAnchorElement(parent);
            if (newAnchor != null) {
              anchor = newAnchor;
            } else {
              anchor = parent;
            }
          }
        }
      }
    }

    if (occurrences.length > 1 && anchor.getParent().getParent() instanceof PsiSwitchStatement) {
      PsiSwitchStatement switchStatement = (PsiSwitchStatement) anchor.getParent().getParent();
      if (switchStatement.getBody().equals(anchor.getParent())) {
        int startOffset = occurrences[0].getTextRange().getStartOffset();
        int endOffset = occurrences[occurrences.length - 1].getTextRange().getEndOffset();
        PsiStatement[] statements = switchStatement.getBody().getStatements();
        boolean isInDifferentCases = false;
        for (PsiStatement statement : statements) {
          if (statement instanceof PsiSwitchLabelStatement) {
            int caseOffset = statement.getTextRange().getStartOffset();
            if (startOffset < caseOffset && caseOffset < endOffset) {
              isInDifferentCases = true;
              break;
            }
          }
        }
        if (isInDifferentCases) {
          anchor = switchStatement;
        }
      }
    }

    return anchor;
  }

  public static boolean isStatement(PsiElement element) {
    if (element instanceof PsiStatement) {
      return true;
    }
    if (element instanceof GosuRawPsiElement) {
      final ASTNode node = element.getNode();
      return node != null && node.getElementType() != null ?
              IStatement.class.isAssignableFrom(((GosuElementType) node.getElementType()).getParsedElementType())
              : false;
    }
    return false;
  }

  /**
   * this is gosu replacment for PsiUtil.isStatement(PsiElement)
   *
   * @return true if element specified is statement or expression statement.
   */
  public static boolean isStatementOrExpressionstatement(@NotNull PsiElement element) {
    PsiElement parent = element.getParent();

    if (element instanceof PsiExpressionListStatement) {
      // statement list allowed in for() init or update only
      if (!(parent instanceof PsiForStatement)) {
        return false;
      }
      final PsiForStatement forStatement = (PsiForStatement) parent;
      if (!(element == forStatement.getInitialization() || element == forStatement.getUpdate())) {
        return false;
      }
      final PsiExpressionList expressionList = ((PsiExpressionListStatement) element).getExpressionList();
      final PsiExpression[] expressions = expressionList.getExpressions();
      for (PsiExpression expression : expressions) {
        if (!isStatementOrExpressionstatement(expression)) {
          return false;
        }
      }
      return true;
    } else if (element instanceof PsiExpressionStatement) {
      return isStatementOrExpressionstatement(((PsiExpressionStatement) element).getExpression());
    }
    if (element instanceof PsiDeclarationStatement) {
      if (parent instanceof PsiCodeBlock) {
        return true;
      }
      if (parent instanceof PsiCodeFragment) {
        return true;
      }

      if (!(parent instanceof PsiForStatement) || ((PsiForStatement) parent).getBody() == element) {
        return false;
      }
    }

    if (isStatement(element)) {
      return true;
    }
    if (element instanceof PsiAssignmentExpression) {
      return true;
    }
    if (isIncrementDecrementOperation(element)) {
      return true;
    }
    if (element instanceof PsiMethodCallExpression) {
      return true;
    }

    if (element instanceof PsiCallExpression) {
      return true;
    }
    if (element instanceof PsiNewExpression) {
      return !(((PsiNewExpression) element).getType() instanceof PsiArrayType);
    }
    return element instanceof PsiCodeBlock;
  }

  public static boolean isIncrementDecrementOperation(@NotNull PsiElement element) {
    if (element instanceof PsiPostfixExpression) {
      final IElementType sign = ((PsiPostfixExpression) element).getOperationTokenType();
      if (sign == JavaTokenType.PLUSPLUS || sign == JavaTokenType.MINUSMINUS) {
        return true;
      }
    } else if (element instanceof PsiPrefixExpression) {
      final IElementType sign = ((PsiPrefixExpression) element).getOperationTokenType();
      if (sign == JavaTokenType.PLUSPLUS || sign == JavaTokenType.MINUSMINUS) {
        return true;
      }
    }
    return false;
  }

  public static boolean isCompileTimeConstant(PsiExpression expression) {
    if (expression instanceof IGosuExpression) {
      return ((IGosuExpression) expression).getParsedElement().isCompileTimeConstant();
    }

    return PsiUtil.isConstantExpression(expression);
  }

  public static boolean isPsiReferenceExpression(PsiElement element) {
    if (element instanceof PsiReferenceExpression || element instanceof IGosuReferenceExpression) {
      return true;
    }

    return false;
  }

  public static boolean isMethodUsage(PsiElement element) {
    if (element instanceof PsiEnumConstant) {
      return JavaLanguage.INSTANCE.equals(element.getLanguage());
    }
    if (!(element instanceof PsiJavaCodeReferenceElement)) {
      return false;
    }
    PsiElement parent = element.getParent();
    if (parent instanceof PsiCall) {
      return true;
    } else if (parent instanceof PsiAnonymousClass) {
      return element.equals(((PsiAnonymousClass) parent).getBaseClassReference());
    }
    return false;
  }

  @Nullable
  public static PsiExpressionList getArgumentListByMethodReference(PsiElement ref) {
    if (ref instanceof PsiEnumConstant) {
      return ((PsiEnumConstant) ref).getArgumentList();
    }
    PsiElement parent = ref.getParent();
    if (parent instanceof PsiCall) {
      return ((PsiCall) parent).getArgumentList();
    } else if (parent instanceof PsiAnonymousClass) {
      return ((PsiNewExpression) parent.getParent()).getArgumentList();
    }
    LOG.assertTrue(false);
    return null;
  }

  public static PsiCall getCallExpressionByMethodReference(PsiElement ref) {
    if (ref instanceof PsiEnumConstant) {
      return (PsiCall) ref;
    }
    PsiElement parent = ref.getParent();
    if (parent instanceof PsiMethodCallExpression) {
      return (PsiMethodCallExpression) parent;
    } else if (parent instanceof PsiNewExpression) {
      return (PsiNewExpression) parent;
    } else if (parent instanceof PsiAnonymousClass) {
      return (PsiNewExpression) parent.getParent();
    } else {
      LOG.assertTrue(false);
      return null;
    }
  }

  /**
   * @return List of highlighters
   */
  public static List<RangeHighlighter> highlightAllOccurrences(Project project, PsiElement[] occurrences, Editor editor) {
    ArrayList<RangeHighlighter> highlighters = new ArrayList<>();
    HighlightManager highlightManager = HighlightManager.getInstance(project);
    EditorColorsManager colorsManager = EditorColorsManager.getInstance();
    TextAttributes attributes = colorsManager.getGlobalScheme().getAttributes(EditorColors.SEARCH_RESULT_ATTRIBUTES);
    if (occurrences.length > 1) {
      for (PsiElement occurrence : occurrences) {
        final RangeMarker rangeMarker = occurrence.getUserData(GosuElementToWorkOn.TEXT_RANGE);
        if (rangeMarker != null) {
          highlightManager
                  .addRangeHighlight(editor, rangeMarker.getStartOffset(), rangeMarker.getEndOffset(), attributes, true, highlighters);
        } else {
          final TextRange textRange = occurrence.getTextRange();
          highlightManager.addRangeHighlight(editor, textRange.getStartOffset(), textRange.getEndOffset(), attributes, true, highlighters);
        }
      }
    }
    return highlighters;
  }

  public static String createTempVar(PsiExpression expr, PsiElement context, boolean declareFinal) throws IncorrectOperationException {
    PsiElement anchorStatement = getParentStatement(context, true);
    LOG.assertTrue(anchorStatement != null && anchorStatement.getParent() != null);

    Project project = expr.getProject();
    String[] suggestedNames =
            JavaCodeStyleManager.getInstance(project).suggestVariableName(VariableKind.LOCAL_VARIABLE, null, expr, null).names;
    final String prefix = suggestedNames[0];
    final String id = JavaCodeStyleManager.getInstance(project).suggestUniqueVariableName(prefix, context, true);

    PsiElementFactory factory = JavaPsiFacade.getInstance(expr.getProject()).getElementFactory();

    if (expr instanceof PsiParenthesizedExpression) {
      PsiExpression expr1 = ((PsiParenthesizedExpression) expr).getExpression();
      if (expr1 != null) {
        expr = expr1;
      }
    }
    PsiDeclarationStatement decl = factory.createVariableDeclarationStatement(id, expr.getType(), expr);
    if (declareFinal) {
      PsiUtil.setModifierProperty(((PsiLocalVariable) decl.getDeclaredElements()[0]), PsiModifier.FINAL, true);
    }
    anchorStatement.getParent().addBefore(decl, anchorStatement);

    return id;
  }

  public static int verifySafeCopyExpression(PsiElement expr) {
    return verifySafeCopyExpressionSubElement(expr);

  }

  private static int verifySafeCopyExpressionSubElement(PsiElement element) {
    int result = EXPR_COPY_SAFE;
    if (element == null) {
      return result;
    }

    if (element instanceof PsiThisExpression || element instanceof PsiSuperExpression || element instanceof PsiIdentifier) {
      return EXPR_COPY_SAFE;
    }

    if (element instanceof PsiMethodCallExpression) {
      result = EXPR_COPY_UNSAFE;
    }

    if (element instanceof PsiNewExpression) {
      return EXPR_COPY_PROHIBITED;
    }

    if (element instanceof PsiAssignmentExpression) {
      return EXPR_COPY_PROHIBITED;
    }

    if (isPlusPlusOrMinusMinus(element)) {
      return EXPR_COPY_PROHIBITED;
    }

    PsiElement[] children = element.getChildren();

    for (PsiElement child : children) {
      int childResult = verifySafeCopyExpressionSubElement(child);
      result = Math.max(result, childResult);
    }
    return result;
  }

  public static PsiExpression convertInitializerToNormalExpression(PsiExpression expression, PsiType forcedReturnType)
          throws IncorrectOperationException {
    if (expression instanceof PsiArrayInitializerExpression) {
      return createNewExpressionFromArrayInitializer((PsiArrayInitializerExpression) expression, forcedReturnType);
    }
    return expression;
  }

  private static PsiExpression createNewExpressionFromArrayInitializer(PsiArrayInitializerExpression initializer, PsiType forcedType)
          throws IncorrectOperationException {
    PsiType initializerType = null;
    if (initializer != null) {
      //        initializerType = myExpresssion.getType();
      if (forcedType != null) {
        initializerType = forcedType;
      } else {
        initializerType = getTypeByExpression(initializer);
      }
    }
    if (initializerType == null) {
      return initializer;
    }
    LOG.assertTrue(initializerType instanceof PsiArrayType);
    PsiElementFactory factory = JavaPsiFacade.getInstance(initializer.getProject()).getElementFactory();
    PsiNewExpression result =
            (PsiNewExpression) factory.createExpressionFromText("new " + initializerType.getPresentableText() + "{}", null);
    result = (PsiNewExpression) CodeStyleManager.getInstance(initializer.getProject()).reformat(result);
    PsiArrayInitializerExpression arrayInitializer = result.getArrayInitializer();
    LOG.assertTrue(arrayInitializer != null);
    arrayInitializer.replace(initializer);
    return result;
  }

  public static void abstractizeMethod(PsiClass targetClass, PsiMethod method) throws IncorrectOperationException {
    PsiCodeBlock body = method.getBody();
    if (body != null) {
      body.delete();
    }

    PsiUtil.setModifierProperty(method, PsiModifier.ABSTRACT, true);
    PsiUtil.setModifierProperty(method, PsiModifier.FINAL, false);
    PsiUtil.setModifierProperty(method, PsiModifier.SYNCHRONIZED, false);
    PsiUtil.setModifierProperty(method, PsiModifier.NATIVE, false);

    if (!targetClass.isInterface()) {
      PsiUtil.setModifierProperty(targetClass, PsiModifier.ABSTRACT, true);
    }

    removeFinalParameters(method);
  }

  public static boolean isInsideAnonymous(PsiElement element, PsiElement upTo) {
    for (PsiElement current = element; current != null && current != upTo; current = current.getParent()) {
      if (current instanceof PsiAnonymousClass) {
        return true;
      }
    }
    return false;
  }

  public static PsiExpression unparenthesizeExpression(PsiExpression expression) {
    while (expression instanceof PsiParenthesizedExpression) {
      final PsiExpression innerExpression = ((PsiParenthesizedExpression) expression).getExpression();
      if (innerExpression == null) {
        return expression;
      }
      expression = innerExpression;
    }
    return expression;
  }

  public static PsiExpression outermostParenthesizedExpression(PsiExpression expression) {
    while (expression.getParent() instanceof PsiParenthesizedExpression) {
      expression = (PsiParenthesizedExpression) expression.getParent();
    }
    return expression;
  }

  public static String getNewInnerClassName(PsiClass aClass, String oldInnerClassName, String newName) {
    if (!oldInnerClassName.endsWith(aClass.getName())) {
      return newName;
    }
    StringBuilder buffer = new StringBuilder(oldInnerClassName);
    buffer.replace(buffer.length() - aClass.getName().length(), buffer.length(), newName);
    return buffer.toString();
  }

  public static boolean isSuperOrThisCall(PsiStatement statement, boolean testForSuper, boolean testForThis) {
    if (!(statement instanceof PsiExpressionStatement)) {
      return false;
    }
    PsiExpression expression = ((PsiExpressionStatement) statement).getExpression();
    if (!(expression instanceof PsiMethodCallExpression)) {
      return false;
    }
    final PsiReferenceExpression methodExpression = ((PsiMethodCallExpression) expression).getMethodExpression();
    if (testForSuper) {
      if ("super".equals(methodExpression.getText())) {
        return true;
      }
    }
    if (testForThis) {
      if ("this".equals(methodExpression.getText())) {
        return true;
      }
    }

    return false;
  }

  public static void visitImplicitSuperConstructorUsages(PsiClass subClass,
                                                         final ImplicitConstructorUsageVisitor implicitConstructorUsageVistor,
                                                         PsiClass superClass) {
    final PsiMethod baseDefaultConstructor = findDefaultConstructor(superClass);
    final PsiMethod[] constructors = subClass.getConstructors();
    if (constructors.length > 0) {
      for (PsiMethod constructor : constructors) {
        final PsiStatement[] statements = constructor.getBody().getStatements();
        if (statements.length < 1 || !isSuperOrThisCall(statements[0], true, true)) {
          implicitConstructorUsageVistor.visitConstructor(constructor, baseDefaultConstructor);
        }
      }
    } else {
      implicitConstructorUsageVistor.visitClassWithoutConstructors(subClass);
    }
  }

  private static PsiMethod findDefaultConstructor(final PsiClass aClass) {
    final PsiMethod[] constructors = aClass.getConstructors();
    for (PsiMethod constructor : constructors) {
      if (constructor.getParameterList().getParametersCount() == 0) {
        return constructor;
      }
    }

    return null;
  }

  public static void replaceMovedMemberTypeParameters(final PsiElement member,
                                                      final Iterable<PsiTypeParameter> parametersIterable,
                                                      final PsiSubstitutor substitutor,
                                                      final PsiElementFactory factory) {
    for (PsiTypeParameter parameter : parametersIterable) {
      PsiType substitutedType = substitutor.substitute(parameter);
      if (substitutedType == null) {
        substitutedType = TypeConversionUtil.erasure(factory.createType(parameter));
      }
      for (PsiReference reference : ReferencesSearch.search(parameter, new LocalSearchScope(member))) {
        final PsiElement element = reference.getElement();
        final PsiElement parent = element.getParent();
        if (parent instanceof PsiTypeElement) {
          parent.replace(factory.createTypeElement(substitutedType));
        } else if (element instanceof PsiJavaCodeReferenceElement && substitutedType instanceof PsiClassType) {
          element.replace(factory.createReferenceElementByType((PsiClassType) substitutedType));
        }
      }
    }
  }

  @Nullable
  public static PsiMethod getChainedConstructor(PsiMethod constructor) {
    final PsiCodeBlock constructorBody = constructor.getBody();
    if (constructorBody == null) {
      return null;
    }
    final PsiStatement[] statements = constructorBody.getStatements();
    if (statements.length == 1 && statements[0] instanceof PsiExpressionStatement) {
      final PsiExpression expression = ((PsiExpressionStatement) statements[0]).getExpression();
      if (expression instanceof PsiMethodCallExpression) {
        final PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) expression;
        final PsiReferenceExpression methodExpr = methodCallExpression.getMethodExpression();
        if ("this".equals(methodExpr.getReferenceName())) {
          return (PsiMethod) methodExpr.resolve();
        }
      }
    }
    return null;
  }

  public static boolean isInMovedElement(PsiElement element, Set<PsiMember> membersToMove) {
    for (PsiMember member : membersToMove) {
      if (PsiTreeUtil.isAncestor(member, element, false)) {
        return true;
      }
    }
    return false;
  }

  public static boolean inImportStatement(PsiReference ref, PsiElement element) {
    if (PsiTreeUtil.getParentOfType(element, PsiImportStatement.class) != null) {
      return true;
    }
    final PsiFile containingFile = element.getContainingFile();
    if (containingFile instanceof PsiJavaFile) {
      final PsiImportList importList = ((PsiJavaFile) containingFile).getImportList();
      if (importList != null) {
        final TextRange refRange = ref.getRangeInElement().shiftRight(element.getTextRange().getStartOffset());
        for (PsiImportStatementBase importStatementBase : importList.getAllImportStatements()) {
          final TextRange textRange = importStatementBase.getTextRange();
          if (textRange.contains(refRange)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public static interface ImplicitConstructorUsageVisitor {
    void visitConstructor(PsiMethod constructor, PsiMethod baseConstructor);

    void visitClassWithoutConstructors(PsiClass aClass);
  }

  public interface Graph<T> {
    Set<T> getVertices();

    Set<T> getTargets(T source);
  }

  /**
   * Returns subset of <code>graph.getVertices()</code> that is a tranistive closure (by <code>graph.getTargets()<code>)
   * of the following property: initialRelation.value() of vertex or <code>graph.getTargets(vertex)</code> is true.
   * <p/>
   * Note that <code>graph.getTargets()</code> is not neccesrily a subset of <code>graph.getVertex()</code>
   *
   * @param graph
   * @param initialRelation
   * @return subset of graph.getVertices()
   */
  public static <T> Set<T> transitiveClosure(Graph<T> graph, Condition<T> initialRelation) {
    Set<T> result = new HashSet<>();

    final Set<T> vertices = graph.getVertices();
    boolean anyChanged;
    do {
      anyChanged = false;
      for (T currentVertex : vertices) {
        if (!result.contains(currentVertex)) {
          if (!initialRelation.value(currentVertex)) {
            Set<T> targets = graph.getTargets(currentVertex);
            for (T currentTarget : targets) {
              if (result.contains(currentTarget) || initialRelation.value(currentTarget)) {
                result.add(currentVertex);
                anyChanged = true;
                break;
              }
            }
          } else {
            result.add(currentVertex);
          }
        }
      }
    }
    while (anyChanged);
    return result;
  }

  public static boolean equivalentTypes(PsiType t1, PsiType t2, PsiManager manager) {
    while (t1 instanceof PsiArrayType) {
      if (!(t2 instanceof PsiArrayType)) {
        return false;
      }
      t1 = ((PsiArrayType) t1).getComponentType();
      t2 = ((PsiArrayType) t2).getComponentType();
    }

    if (t1 instanceof PsiPrimitiveType) {
      return t2 instanceof PsiPrimitiveType && t1.equals(t2);
    }

    return manager.areElementsEquivalent(PsiUtil.resolveClassInType(t1), PsiUtil.resolveClassInType(t2));
  }

  public static List<PsiVariable> collectReferencedVariables(PsiElement scope) {
    final List<PsiVariable> result = new ArrayList<>();
    scope.accept(new JavaRecursiveElementWalkingVisitor() {
      @Override
      public void visitReferenceExpression(PsiReferenceExpression expression) {
        final PsiElement element = expression.resolve();
        if (element instanceof PsiVariable) {
          result.add((PsiVariable) element);
        }
        final PsiExpression qualifier = expression.getQualifierExpression();
        if (qualifier != null) {
          qualifier.accept(this);
        }
      }
    });
    return result;
  }

  public static boolean isModifiedInScope(PsiVariable variable, PsiElement scope) {
    for (PsiReference reference : ReferencesSearch.search(variable, new LocalSearchScope(scope), false)) {
      if (isAssignmentLHS(reference.getElement())) {
        return true;
      }
    }
    return false;
  }

  private static String getNameOfReferencedParameter(PsiDocTag tag) {
    LOG.assertTrue("param".equals(tag.getName()));
    final PsiElement[] dataElements = tag.getDataElements();
    if (dataElements.length < 1) {
      return null;
    }
    return dataElements[0].getText();
  }

  public static void fixJavadocsForParams(PsiMethod method, Set<PsiParameter> newParameters) throws IncorrectOperationException {
    final PsiDocComment docComment = method.getDocComment();
    if (docComment == null) {
      return;
    }
    final PsiParameter[] parameters = method.getParameterList().getParameters();
    final PsiDocTag[] paramTags = docComment.findTagsByName("param");
    if (parameters.length > 0 && newParameters.size() < parameters.length && paramTags.length == 0) {
      return;
    }
    Map<PsiParameter, PsiDocTag> tagForParam = new HashMap<>();
    for (PsiParameter parameter : parameters) {
      boolean found = false;
      for (PsiDocTag paramTag : paramTags) {
        if (parameter.getName().equals(getNameOfReferencedParameter(paramTag))) {
          tagForParam.put(parameter, paramTag);
          found = true;
          break;
        }
      }
      if (!found && !newParameters.contains(parameter)) {
        tagForParam.put(parameter, null);
      }
    }

    List<PsiDocTag> newTags = new ArrayList<>();
    for (PsiParameter parameter : parameters) {
      if (tagForParam.containsKey(parameter)) {
        final PsiDocTag psiDocTag = tagForParam.get(parameter);
        if (psiDocTag != null) {
          newTags.add((PsiDocTag) psiDocTag.copy());
        }
      } else {
        newTags.add(JavaPsiFacade.getInstance(method.getProject()).getElementFactory().createParamTag(parameter.getName(), ""));
      }
    }
    PsiElement anchor = paramTags.length > 0 ? paramTags[0].getPrevSibling() : null;
    for (PsiDocTag paramTag : paramTags) {
      paramTag.delete();
    }
    for (PsiDocTag psiDocTag : newTags) {
      anchor = docComment.addAfter(psiDocTag, anchor);
    }
  }

  public static PsiDirectory createPackageDirectoryInSourceRoot(PackageWrapper aPackage, final VirtualFile sourceRoot)
          throws IncorrectOperationException {
    final PsiDirectory[] directories = aPackage.getDirectories();
    for (PsiDirectory directory : directories) {
      if (VfsUtil.isAncestor(sourceRoot, directory.getVirtualFile(), false)) {
        return directory;
      }
    }
    String qNameToCreate = qNameToCreateInSourceRoot(aPackage, sourceRoot);
    final String[] shortNames = qNameToCreate.split("\\.");
    PsiDirectory current = aPackage.getManager().findDirectory(sourceRoot);
    LOG.assertTrue(current != null);
    for (String shortName : shortNames) {
      PsiDirectory subdirectory = current.findSubdirectory(shortName);
      if (subdirectory == null) {
        subdirectory = current.createSubdirectory(shortName);
      }
      current = subdirectory;
    }
    return current;
  }

  public static String qNameToCreateInSourceRoot(PackageWrapper aPackage, final VirtualFile sourceRoot) throws IncorrectOperationException {
    String targetQName = aPackage.getQualifiedName();
    String sourceRootPackage =
            ProjectRootManager.getInstance(aPackage.getManager().getProject()).getFileIndex().getPackageNameByDirectory(sourceRoot);
    if (!canCreateInSourceRoot(sourceRootPackage, targetQName)) {
      throw new IncorrectOperationException(
              "Cannot create package '" + targetQName + "' in source folder " + sourceRoot.getPresentableUrl());
    }
    String result = targetQName.substring(sourceRootPackage.length());
    if (StringUtil.startsWithChar(result, '.')) {
      result = result.substring(1);  // remove initial '.'
    }
    return result;
  }

  public static boolean canCreateInSourceRoot(final String sourceRootPackage, final String targetQName) {
    if (sourceRootPackage == null || !targetQName.startsWith(sourceRootPackage)) {
      return false;
    }
    if (sourceRootPackage.length() == 0 || targetQName.length() == sourceRootPackage.length()) {
      return true;
    }
    return targetQName.charAt(sourceRootPackage.length()) == '.';
  }

  @Nullable
  public static PsiDirectory findPackageDirectoryInSourceRoot(PackageWrapper aPackage, final VirtualFile sourceRoot) {
    final PsiDirectory[] directories = aPackage.getDirectories();
    for (PsiDirectory directory : directories) {
      if (VfsUtil.isAncestor(sourceRoot, directory.getVirtualFile(), false)) {
        return directory;
      }
    }
    String qNameToCreate;
    try {
      qNameToCreate = qNameToCreateInSourceRoot(aPackage, sourceRoot);
    } catch (IncorrectOperationException e) {
      return null;
    }
    final String[] shortNames = qNameToCreate.split("\\.");
    PsiDirectory current = aPackage.getManager().findDirectory(sourceRoot);
    LOG.assertTrue(current != null);
    for (String shortName : shortNames) {
      PsiDirectory subdirectory = current.findSubdirectory(shortName);
      if (subdirectory == null) {
        return null;
      }
      current = subdirectory;
    }
    return current;
  }

  public static class ConditionCache<T> implements Condition<T> {
    private final Condition<T> myCondition;
    private final HashSet<T> myProcessedSet = new HashSet<>();
    private final HashSet<T> myTrueSet = new HashSet<>();

    public ConditionCache(Condition<T> condition) {
      myCondition = condition;
    }

    public boolean value(T object) {
      if (!myProcessedSet.contains(object)) {
        myProcessedSet.add(object);
        final boolean value = myCondition.value(object);
        if (value) {
          myTrueSet.add(object);
          return true;
        }
        return false;
      }
      return myTrueSet.contains(object);
    }
  }

  public static class IsDescendantOf implements Condition<PsiClass> {
    private final PsiClass myClass;
    private final ConditionCache<PsiClass> myConditionCache;

    public IsDescendantOf(PsiClass aClass) {
      myClass = aClass;
      myConditionCache = new ConditionCache<>(new Condition<PsiClass>() {
        public boolean value(PsiClass aClass) {
          return InheritanceUtil.isInheritorOrSelf(aClass, myClass, true);
        }
      });
    }

    public boolean value(PsiClass aClass) {
      return myConditionCache.value(aClass);
    }
  }

  @Nullable
  public static PsiTypeParameterList createTypeParameterListWithUsedTypeParameters(@NotNull final PsiElement... elements) {
    return createTypeParameterListWithUsedTypeParameters(null, elements);
  }

  @Nullable
  public static PsiTypeParameterList createTypeParameterListWithUsedTypeParameters(final PsiTypeParameterList fromList,
                                                                                   @NotNull final PsiElement... elements) {
    return createTypeParameterListWithUsedTypeParameters(fromList, Condition.TRUE, elements);
  }

  @Nullable
  public static PsiTypeParameterList createTypeParameterListWithUsedTypeParameters(final PsiTypeParameterList fromList,
                                                                                   Condition<PsiTypeParameter> filter,
                                                                                   @NotNull final PsiElement... elements) {
    if (elements.length == 0) {
      return null;
    }
    final Set<PsiTypeParameter> used = new HashSet<>();
    for (final PsiElement element : elements) {
      if (element == null) {
        continue;
      }
      collectTypeParameters(used, element, filter);  //pull up extends cls class with type params

    }

    if (fromList != null) {
      used.retainAll(Arrays.asList(fromList.getTypeParameters()));
    }

    PsiTypeParameter[] typeParameters = used.toArray(new PsiTypeParameter[used.size()]);

    Arrays.sort(typeParameters, new Comparator<PsiTypeParameter>() {
      public int compare(final PsiTypeParameter tp1, final PsiTypeParameter tp2) {
        return tp1.getTextRange().getStartOffset() - tp2.getTextRange().getStartOffset();
      }
    });

    final PsiElementFactory elementFactory = JavaPsiFacade.getInstance(elements[0].getProject()).getElementFactory();
    try {
      final PsiClass aClass = elementFactory.createClassFromText("class A {}", null);
      PsiTypeParameterList list = aClass.getTypeParameterList();
      assert list != null;
      for (final PsiTypeParameter typeParameter : typeParameters) {
        list.add(typeParameter);
      }
      return list;
    } catch (IncorrectOperationException e) {
      LOG.error(e);
      assert false;
      return null;
    }
  }

  public static void collectTypeParameters(final Set<PsiTypeParameter> used, final PsiElement element) {
    collectTypeParameters(used, element, Condition.TRUE);
  }

  public static void collectTypeParameters(final Set<PsiTypeParameter> used, final PsiElement element,
                                           final Condition<PsiTypeParameter> filter) {
    element.accept(new JavaRecursiveElementVisitor() {
      @Override
      public void visitReferenceElement(PsiJavaCodeReferenceElement reference) {
        super.visitReferenceElement(reference);
        if (!reference.isQualified()) {
          final PsiElement resolved = reference.resolve();
          if (resolved instanceof PsiTypeParameter) {
            final PsiTypeParameter typeParameter = (PsiTypeParameter) resolved;
            if (PsiTreeUtil.isAncestor(typeParameter.getOwner(), element, false) && filter.value(typeParameter)) {
              used.add(typeParameter);
            }
          }
        }
      }

      @Override
      public void visitExpression(final PsiExpression expression) {
        super.visitExpression(expression);
        final PsiType type = expression.getType();
        if (type != null) {
          final TypeParameterSearcher searcher = new TypeParameterSearcher();
          type.accept(searcher);
          for (PsiTypeParameter typeParam : searcher.myTypeParams) {
            if (PsiTreeUtil.isAncestor(typeParam.getOwner(), element, false) && filter.value(typeParam)) {
              used.add(typeParam);
            }
          }
        }
      }

      class TypeParameterSearcher extends PsiTypeVisitor<Boolean> {
        private final Set<PsiTypeParameter> myTypeParams = new java.util.HashSet<>();

        public Boolean visitType(final PsiType type) {
          return false;
        }

        public Boolean visitArrayType(final PsiArrayType arrayType) {
          return arrayType.getComponentType().accept(this);
        }

        public Boolean visitClassType(final PsiClassType classType) {
          final PsiClass aClass = classType.resolve();
          if (aClass instanceof PsiTypeParameter) {
            myTypeParams.add((PsiTypeParameter) aClass);
          }

          final PsiType[] types = classType.getParameters();
          for (final PsiType psiType : types) {
            psiType.accept(this);
          }
          return false;
        }

        public Boolean visitWildcardType(final PsiWildcardType wildcardType) {
          final PsiType bound = wildcardType.getBound();
          if (bound != null) {
            bound.accept(this);
          }
          return false;
        }
      }
    });
  }

  public static PsiType findFragmentType(PsiElement fragment) {
    GosuTypeLiteralImpl typeLiteral = PsiTreeUtil.findChildOfType(fragment, GosuTypeLiteralImpl.class);
    if (typeLiteral != null) {
      return typeLiteral.getType();
    }
    String text = fragment.getText();
    String qName = ((GosuFragmentFileImpl) fragment).getImports().get(text);
    if (qName == null) {
      qName = text;
    }
    return ((GosuTypeLiteralImpl) GosuPsiParseUtil.parseTypeLiteral(qName, fragment)).getType();
  }
}


/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor.signature;

import com.intellij.codeInsight.ExceptionUtil;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.codeStyle.VariableKind;
import com.intellij.psi.impl.source.DummyHolder;
import com.intellij.psi.scope.processor.VariablesProcessor;
import com.intellij.psi.scope.util.PsiScopesUtil;
import com.intellij.psi.util.*;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.changeSignature.*;
import com.intellij.refactoring.rename.RenameUtil;
import com.intellij.refactoring.rename.ResolveSnapshotProvider;
import com.intellij.refactoring.util.*;
import com.intellij.refactoring.util.usageInfo.DefaultConstructorImplicitUsageInfo;
import com.intellij.refactoring.util.usageInfo.NoConstructorClassUsageInfo;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.VisibilityUtil;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.HashSet;
import com.intellij.util.containers.MultiMap;
import com.jgoodies.common.base.Strings;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.lang.psi.util.HasText;
import gw.plugin.ij.lang.psi.util.LeafPsiMatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static gw.plugin.ij.lang.psi.util.GosuPsiParseUtil.createParameter;
import static gw.plugin.ij.lang.psi.util.GosuPsiParseUtil.parseIdentifier;
import static gw.plugin.ij.util.ClassLord.importAllTypes;

public class GosuChangeSignatureUsageProcessor implements ChangeSignatureUsageProcessor {
  private static final Logger LOG = Logger.getInstance("#com.intellij.refactoring.changeSignature.JavaChangeSignatureUsageProcessor");

  private static boolean isGosuUsage(UsageInfo info) {
    final PsiElement element = info.getElement();
    if (element == null) return false;
    return element.getLanguage() == GosuLanguage.instance();
  }

  public UsageInfo[] findUsages(ChangeInfo info) {
    if (info instanceof JavaChangeInfo) {
      return new GosuChangeSignatureUsageSearcher((JavaChangeInfo)info).findUsages();
    }
    else {
      return UsageInfo.EMPTY_ARRAY;
    }
  }

  public MultiMap<PsiElement, String> findConflicts(ChangeInfo info, Ref<UsageInfo[]> refUsages) {
    if (info instanceof JavaChangeInfo) {
      return new ConflictSearcher((JavaChangeInfo)info).findConflicts(refUsages);
    }
    else {
      return new MultiMap<PsiElement, String>();
    }
  }

  public boolean processUsage(ChangeInfo changeInfo, UsageInfo usage, boolean beforeMethodChange, UsageInfo[] usages) {
    if (!isGosuUsage(usage)) return false;
    if (!(changeInfo instanceof JavaChangeInfo)) return false;


    if (beforeMethodChange) {
      if (usage instanceof CallerUsageInfo) {
        final CallerUsageInfo callerUsageInfo = (CallerUsageInfo)usage;
        processCallerMethod((JavaChangeInfo)changeInfo, callerUsageInfo.getMethod(), null, callerUsageInfo.isToInsertParameter(),
                            callerUsageInfo.isToInsertException());
        return true;
      }
      else if (usage instanceof OverriderUsageInfo) {
        OverriderUsageInfo info = (OverriderUsageInfo)usage;
        final IGosuMethod method = (IGosuMethod) info.getElement();
        final PsiMethod baseMethod = info.getBaseMethod();
        if (info.isOriginalOverrider()) {
          processPrimaryMethod((JavaChangeInfo)changeInfo, method, baseMethod, false);
        }
        else {
          processCallerMethod((JavaChangeInfo)changeInfo, method, baseMethod, info.isToInsertArgs(), info.isToCatchExceptions());
        }
        return true;
      }

    }
    else {
      PsiElement element = usage.getElement();
      LOG.assertTrue(element != null);

      if (usage instanceof DefaultConstructorImplicitUsageInfo) {
        final DefaultConstructorImplicitUsageInfo defConstructorUsage = (DefaultConstructorImplicitUsageInfo)usage;
        PsiMethod constructor = defConstructorUsage.getConstructor();
        if (!constructor.isPhysical()) {
          final boolean toPropagate =
            changeInfo instanceof JavaChangeInfo && ((JavaChangeInfo)changeInfo).getMethodsToPropagateParameters().remove(constructor);
          final PsiClass containingClass = defConstructorUsage.getContainingClass();
          constructor = (PsiMethod)containingClass.add(constructor);
          PsiUtil.setModifierProperty(constructor, VisibilityUtil.getVisibilityModifier(containingClass.getModifierList()), true);
          if (toPropagate) {
            ((JavaChangeInfo)changeInfo).getMethodsToPropagateParameters().add(constructor);
          }
        }
        addSuperCall((JavaChangeInfo)changeInfo, constructor, defConstructorUsage.getBaseConstructor(), usages);
        return true;
      }
      else if (usage instanceof NoConstructorClassUsageInfo) {
        addDefaultConstructor(((JavaChangeInfo)changeInfo), ((NoConstructorClassUsageInfo)usage).getPsiClass(), usages);
        return true;
      }
      else if (usage instanceof MethodCallUsageInfo) {
        final MethodCallUsageInfo methodCallInfo = (MethodCallUsageInfo)usage;
        processMethodUsage(methodCallInfo.getElement(), (JavaChangeInfo)changeInfo, methodCallInfo.isToChangeArguments(),
                           methodCallInfo.isToCatchExceptions(), methodCallInfo.getReferencedMethod(), methodCallInfo.getSubstitutor(), usages);
        return true;
      }
      else if (usage instanceof ChangeSignatureParameterUsageInfo) {
        String newName = ((ChangeSignatureParameterUsageInfo)usage).newParameterName;
        String oldName = ((ChangeSignatureParameterUsageInfo)usage).oldParameterName;
        processParameterUsage((PsiReferenceExpression)element, oldName, newName);
        return true;
      }
      else if (usage instanceof CallReferenceUsageInfo) {
        ((CallReferenceUsageInfo)usage).getReference().handleChangeSignature(changeInfo);
        return true;
      }
      else if (element instanceof PsiEnumConstant) {
        fixActualArgumentsList(((PsiEnumConstant)element).getArgumentList(), (JavaChangeInfo)changeInfo, true, PsiSubstitutor.EMPTY);
        return true;
      }
      else if (!(usage instanceof OverriderUsageInfo)) {
        PsiReference reference = usage instanceof MoveRenameUsageInfo ? usage.getReference() : element.getReference();
        if (reference != null) {
          PsiElement target = changeInfo.getMethod();
          if (target != null) {
            if (changeInfo.isNameChanged()) {
              reference.handleElementRename(changeInfo.getNewName());
            }

            PsiElement referencedElement = reference.getElement();
            TextRange range = reference.getRangeInElement();
            PsiElement el = referencedElement.getContainingFile().findElementAt(referencedElement.getTextOffset() + range.getStartOffset());

            //constructor call case
            if (el.getParent() instanceof GosuTypeLiteralImpl) {
              el = el.getParent();
            }
            referencedElement = el;
            while (!(el == null || el instanceof PsiExpressionList)) {
              el = el.getNextSibling();
            }
            PsiExpressionList paramsList = (PsiExpressionList) el;
            PsiManager manager = referencedElement.getManager();
            if (paramsList == null) {
              paramsList = GosuPsiParseUtil.createEmptyExpressionList(manager);
              PsiElement after = null;
              PsiElement seek = referencedElement;
              while (after == null && seek != null) {
                after = new LeafPsiMatcher(seek).descendant(new HasText("(")).getElement();
                seek = seek.getNextSibling();
                if (seek != null && "(".equals(seek.getText())) {
                  after = seek;
                  break;
                }
              }
              if (after == null) {
                //fail
                return true;
              }
              paramsList = (PsiExpressionList) after.getParent().addAfter(paramsList, after);
            }

            PsiExpression[] oldParams = paramsList.getExpressions();
            List<PsiExpression> expressions = new ArrayList<>();

            DummyHolder dummyHolder = new DummyHolder(manager, null, referencedElement, null, true, GosuLanguage.instance());

            for (ParameterInfo info : changeInfo.getNewParameters()) {

              int oldIdx = info.getOldIndex();

              PsiExpression paramExpr;
              if (oldIdx >= 0 && oldIdx < oldParams.length) {
                paramExpr = (PsiExpression) dummyHolder.add(oldParams[oldIdx]);
              } else {

                String defaultValue = info.getDefaultValue();
                if (Strings.isBlank(defaultValue)) {
                  defaultValue = "null";
                }
                paramExpr = (PsiExpression) GosuPsiParseUtil.parseExpression(defaultValue, manager);
              }
              expressions.add(paramExpr);
            }

            if (oldParams.length != 0) {
              paramsList.deleteChildRange(oldParams[0], oldParams[oldParams.length - 1]);
            }

            ASTNode paramsListAstNode = paramsList.getNode();
            for (int i = 0; i < expressions.size(); ++i) {
              if (i != 0) {
                paramsListAstNode.addLeaf(GosuTokenTypes.TT_OP_comma, ",", null);
              }
              paramsList.add(expressions.get(i));
            }
            return true;
          }
        }
      }
    }
    return false;
  }

  private static void processParameterUsage(PsiReferenceExpression ref, String oldName, String newName)
    throws IncorrectOperationException {

    PsiElement last = ref.getReferenceNameElement();
    if (last instanceof PsiIdentifier && last.getText().equals(oldName)) {
      PsiElementFactory factory = JavaPsiFacade.getInstance(ref.getProject()).getElementFactory();
      PsiIdentifier newNameIdentifier = factory.createIdentifier(newName);
      last.replace(newNameIdentifier);
    }
  }


  private static void addDefaultConstructor(JavaChangeInfo changeInfo, PsiClass aClass, final UsageInfo[] usages)
    throws IncorrectOperationException {
    if (!(aClass instanceof PsiAnonymousClass)) {
      PsiElementFactory factory = JavaPsiFacade.getElementFactory(aClass.getProject());
      PsiMethod defaultConstructor = factory.createMethodFromText(aClass.getName() + "(){}", aClass);
      defaultConstructor = (PsiMethod)CodeStyleManager.getInstance(aClass.getProject()).reformat(defaultConstructor);
      defaultConstructor = (PsiMethod)aClass.add(defaultConstructor);
      PsiUtil.setModifierProperty(defaultConstructor, VisibilityUtil.getVisibilityModifier(aClass.getModifierList()), true);
      addSuperCall(changeInfo, defaultConstructor, null, usages);
    }
    else {
      final PsiElement parent = aClass.getParent();
      if (parent instanceof PsiNewExpression) {
        final PsiExpressionList argumentList = ((PsiNewExpression)parent).getArgumentList();
        final PsiClass baseClass = changeInfo.getMethod().getContainingClass();
        final PsiSubstitutor substitutor = TypeConversionUtil.getSuperClassSubstitutor(baseClass, aClass, PsiSubstitutor.EMPTY);
        fixActualArgumentsList(argumentList, changeInfo, true, substitutor);
      }
    }
  }

  private static void addSuperCall(JavaChangeInfo changeInfo, PsiMethod constructor, PsiMethod callee, final UsageInfo[] usages)
    throws IncorrectOperationException {
    final PsiElementFactory factory = JavaPsiFacade.getElementFactory(constructor.getProject());
    PsiExpressionStatement superCall = (PsiExpressionStatement)factory.createStatementFromText("super();", constructor);
    PsiCodeBlock body = constructor.getBody();
    assert body != null;
    PsiStatement[] statements = body.getStatements();
    if (statements.length > 0) {
      superCall = (PsiExpressionStatement)body.addBefore(superCall, statements[0]);
    }
    else {
      superCall = (PsiExpressionStatement)body.add(superCall);
    }
    PsiMethodCallExpression callExpression = (PsiMethodCallExpression)superCall.getExpression();
    final PsiClass aClass = constructor.getContainingClass();
    final PsiClass baseClass = changeInfo.getMethod().getContainingClass();
    final PsiSubstitutor substitutor = TypeConversionUtil.getSuperClassSubstitutor(baseClass, aClass, PsiSubstitutor.EMPTY);
    processMethodUsage(callExpression.getMethodExpression(), changeInfo, true, false, callee, substitutor, usages);
  }

  private static void processMethodUsage(PsiElement ref,
                                  JavaChangeInfo changeInfo,
                                  boolean toChangeArguments,
                                  boolean toCatchExceptions,
                                  PsiMethod callee, PsiSubstitutor subsitutor, final UsageInfo[] usages) throws IncorrectOperationException {
    if (changeInfo.isNameChanged()) {
      if (ref instanceof PsiJavaCodeReferenceElement) {
        PsiElement last = ((PsiJavaCodeReferenceElement)ref).getReferenceNameElement();
        if (last instanceof PsiIdentifier && last.getText().equals(changeInfo.getOldName())) {
          last.replace(changeInfo.getNewNameIdentifier());
        }
      }
    }

    final PsiMethod caller = RefactoringUtil.getEnclosingMethod(ref);
    if (toChangeArguments) {
      final PsiExpressionList list = RefactoringUtil.getArgumentListByMethodReference(ref);
      LOG.assertTrue(list != null);
      boolean toInsertDefaultValue = needDefaultValue(changeInfo, caller);
      if (toInsertDefaultValue && ref instanceof PsiReferenceExpression) {
        final PsiExpression qualifierExpression = ((PsiReferenceExpression)ref).getQualifierExpression();
        if (qualifierExpression instanceof PsiSuperExpression && callerSignatureIsAboutToChangeToo(caller, usages)) {
          toInsertDefaultValue = false;
        }
      }

      fixActualArgumentsList(list, changeInfo, toInsertDefaultValue, subsitutor);
    }

    if (toCatchExceptions) {
      if (!(ref instanceof PsiReferenceExpression &&
            ((PsiReferenceExpression)ref).getQualifierExpression() instanceof PsiSuperExpression)) {
        if (needToCatchExceptions(changeInfo, caller)) {
          PsiClassType[] newExceptions =
            callee != null ? getCalleeChangedExceptionInfo(callee) : getPrimaryChangedExceptionInfo(changeInfo);
          fixExceptions(ref, newExceptions);
        }
      }
    }
  }

  private static boolean callerSignatureIsAboutToChangeToo(final PsiMethod caller, final UsageInfo[] usages) {
    for (UsageInfo usage : usages) {
      if (usage instanceof MethodCallUsageInfo &&
          MethodSignatureUtil.isSuperMethod(((MethodCallUsageInfo)usage).getReferencedMethod(), caller)) {
        return true;
      }
    }
    return false;
  }

  private static PsiClassType[] getCalleeChangedExceptionInfo(final PsiMethod callee) {
    return callee.getThrowsList().getReferencedTypes(); //Callee method's throws list is already modified!
  }

  private static void fixExceptions(PsiElement ref, PsiClassType[] newExceptions) throws IncorrectOperationException {
    //methods' throws lists are already modified, may use ExceptionUtil.collectUnhandledExceptions
    newExceptions = filterCheckedExceptions(newExceptions);

    PsiElement context = PsiTreeUtil.getParentOfType(ref, PsiTryStatement.class, PsiMethod.class);
    if (context instanceof PsiTryStatement) {
      PsiTryStatement tryStatement = (PsiTryStatement)context;
      PsiCodeBlock tryBlock = tryStatement.getTryBlock();

      //Remove unused catches
      Collection<PsiClassType> classes = ExceptionUtil.collectUnhandledExceptions(tryBlock, tryBlock);
      PsiParameter[] catchParameters = tryStatement.getCatchBlockParameters();
      for (PsiParameter parameter : catchParameters) {
        final PsiType caughtType = parameter.getType();

        if (!(caughtType instanceof PsiClassType)) continue;
        if (ExceptionUtil.isUncheckedExceptionOrSuperclass((PsiClassType)caughtType)) continue;

        if (!isCatchParameterRedundant((PsiClassType)caughtType, classes)) continue;
        parameter.getParent().delete(); //delete catch section
      }

      PsiClassType[] exceptionsToAdd = filterUnhandledExceptions(newExceptions, tryBlock);
      addExceptions(exceptionsToAdd, tryStatement);

      adjustPossibleEmptyTryStatement(tryStatement);
    }
    else {
      newExceptions = filterUnhandledExceptions(newExceptions, ref);
      if (newExceptions.length > 0) {
        //Add new try statement
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(ref.getProject());
        PsiTryStatement tryStatement = (PsiTryStatement)elementFactory.createStatementFromText("try {} catch (Exception e) {}", null);
        PsiStatement anchor = PsiTreeUtil.getParentOfType(ref, PsiStatement.class);
        LOG.assertTrue(anchor != null);
        tryStatement.getTryBlock().add(anchor);
        tryStatement = (PsiTryStatement)anchor.getParent().addAfter(tryStatement, anchor);

        addExceptions(newExceptions, tryStatement);
        anchor.delete();
        tryStatement.getCatchSections()[0].delete(); //Delete dummy catch section
      }
    }
  }

  private static PsiClassType[] filterCheckedExceptions(PsiClassType[] exceptions) {
    List<PsiClassType> result = new ArrayList<PsiClassType>();
    for (PsiClassType exceptionType : exceptions) {
      if (!ExceptionUtil.isUncheckedException(exceptionType)) result.add(exceptionType);
    }
    return result.toArray(new PsiClassType[result.size()]);
  }

  private static void adjustPossibleEmptyTryStatement(PsiTryStatement tryStatement) throws IncorrectOperationException {
    PsiCodeBlock tryBlock = tryStatement.getTryBlock();
    if (tryBlock != null) {
      if (tryStatement.getCatchSections().length == 0 &&
          tryStatement.getFinallyBlock() == null) {
        PsiElement firstBodyElement = tryBlock.getFirstBodyElement();
        if (firstBodyElement != null) {
          tryStatement.getParent().addRangeAfter(firstBodyElement, tryBlock.getLastBodyElement(), tryStatement);
        }
        tryStatement.delete();
      }
    }
  }

  private static void addExceptions(PsiClassType[] exceptionsToAdd, PsiTryStatement tryStatement) throws IncorrectOperationException {
    for (PsiClassType type : exceptionsToAdd) {
      final JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(tryStatement.getProject());
      String name = styleManager.suggestVariableName(VariableKind.PARAMETER, null, null, type).names[0];
      name = styleManager.suggestUniqueVariableName(name, tryStatement, false);

      PsiCatchSection catchSection =
        JavaPsiFacade.getInstance(tryStatement.getProject()).getElementFactory().createCatchSection(type, name, tryStatement);
      tryStatement.add(catchSection);
    }
  }

  private static PsiClassType[] filterUnhandledExceptions(PsiClassType[] exceptions, PsiElement place) {
    List<PsiClassType> result = new ArrayList<PsiClassType>();
    for (PsiClassType exception : exceptions) {
      if (!ExceptionUtil.isHandled(exception, place)) result.add(exception);
    }
    return result.toArray(new PsiClassType[result.size()]);
  }

  private static boolean isCatchParameterRedundant(PsiClassType catchParamType, Collection<PsiClassType> thrownTypes) {
    for (PsiType exceptionType : thrownTypes) {
      if (exceptionType.isConvertibleFrom(catchParamType)) return false;
    }
    return true;
  }

  //This methods works equally well for primary usages as well as for propagated callers' usages
  private static void fixActualArgumentsList(PsiExpressionList list,
                                             JavaChangeInfo changeInfo,
                                             boolean toInsertDefaultValue, PsiSubstitutor substitutor) throws IncorrectOperationException {
    final PsiElementFactory factory = JavaPsiFacade.getInstance(list.getProject()).getElementFactory();
    if (changeInfo.isParameterSetOrOrderChanged()) {

      final PsiExpression[] args = list.getExpressions();
      final int nonVarargCount = getNonVarargCount(changeInfo, args);
      final int varargCount = args.length - nonVarargCount;
      if (varargCount<0) return;
      PsiExpression[] newVarargInitializers = null;

      final int newArgsLength;
      final int newNonVarargCount;
      final JavaParameterInfo[] newParms = changeInfo.getNewParameters();
      if (changeInfo.isArrayToVarargs()) {
        newNonVarargCount = newParms.length - 1;
        final JavaParameterInfo lastNewParm = newParms[newParms.length - 1];
        final PsiExpression arrayToConvert = args[lastNewParm.getOldIndex()];
        if (arrayToConvert instanceof PsiNewExpression) {
          final PsiNewExpression expression = (PsiNewExpression)arrayToConvert;
          final PsiArrayInitializerExpression arrayInitializer = expression.getArrayInitializer();
          if (arrayInitializer != null) {
            newVarargInitializers = arrayInitializer.getInitializers();
          }
        }
        newArgsLength = newVarargInitializers == null ? newParms.length : newNonVarargCount + newVarargInitializers.length;
      }
      else if (changeInfo.isRetainsVarargs()) {
        newNonVarargCount = newParms.length - 1;
        newArgsLength = newNonVarargCount + varargCount;
      }
      else if (changeInfo.isObtainsVarags()) {
        newNonVarargCount = newParms.length - 1;
        newArgsLength = newNonVarargCount;
      }
      else {
        newNonVarargCount = newParms.length;
        newArgsLength = newParms.length;
      }

      String[] oldVarargs = null;
      if (changeInfo.wasVararg() && !changeInfo.isRetainsVarargs()) {
        oldVarargs = new String[varargCount];
        for (int i = nonVarargCount; i < args.length; i++) {
          oldVarargs[i - nonVarargCount] = args[i].getText();
        }
      }

      final PsiExpression[] newArgs = new PsiExpression[newArgsLength];
      for (int i = 0; i < newNonVarargCount; i++) {
        if (newParms[i].getOldIndex() == nonVarargCount && oldVarargs != null) {
          PsiType type = newParms[i].createType(changeInfo.getMethod(), list.getManager());
          if (type instanceof PsiArrayType) {
            type = substitutor.substitute(type);
            type = TypeConversionUtil.erasure(type);
            String typeText = type.getCanonicalText();
            if (type instanceof PsiEllipsisType) {
              typeText = typeText.replace("...", "[]");
            }
            String text = "new " + typeText + "{" + StringUtil.join(oldVarargs, ",") + "}";
            newArgs[i] = factory.createExpressionFromText(text, changeInfo.getMethod());
            continue;
          }
        }
        newArgs[i] = createActualArgument(changeInfo, list, newParms[i], toInsertDefaultValue, args);
      }
      if (changeInfo.isArrayToVarargs()) {
        if (newVarargInitializers == null) {
          newArgs[newNonVarargCount] =
            createActualArgument(changeInfo, list, newParms[newNonVarargCount], toInsertDefaultValue, args);
        }
        else {
          System.arraycopy(newVarargInitializers, 0, newArgs, newNonVarargCount, newVarargInitializers.length);
        }
      }
      else {
        final int newVarargCount = newArgsLength - newNonVarargCount;
        LOG.assertTrue(newVarargCount == 0 || newVarargCount == varargCount);
        for (int i = newNonVarargCount; i < newArgsLength; i++){
          final int oldIndex = newParms[newNonVarargCount].getOldIndex();
          if (oldIndex >= 0 && oldIndex != nonVarargCount) {
            newArgs[i] = createActualArgument(changeInfo, list, newParms[newNonVarargCount], toInsertDefaultValue, args);
          } else {
            System.arraycopy(args, nonVarargCount, newArgs, newNonVarargCount, newVarargCount);
            break;
          }
        }
      }
      ChangeSignatureUtil.synchronizeList(list, Arrays.asList(newArgs), ExpressionList.INSTANCE, changeInfo.toRemoveParm());
    }
  }

  private static int getNonVarargCount(JavaChangeInfo changeInfo, PsiExpression[] args) {
    if (!changeInfo.wasVararg()) return args.length;
    return changeInfo.getOldParameterTypes().length - 1;
  }


  @Nullable
  private static PsiExpression createActualArgument(JavaChangeInfo changeInfo,
                                                    final PsiExpressionList list,
                                                    final JavaParameterInfo info,
                                                    final boolean toInsertDefaultValue,
                                                    final PsiExpression[] args) throws IncorrectOperationException {
    final PsiElementFactory factory = JavaPsiFacade.getInstance(list.getProject()).getElementFactory();
    final int index = info.getOldIndex();
    if (index >= 0) {
      return args[index];
    }
    else {
      if (toInsertDefaultValue) {
        return createDefaultValue(changeInfo, factory, info, list);
      }
      else {
        return factory.createExpressionFromText(info.getName(), list);
      }
    }
  }

  @Nullable
  private static PsiExpression createDefaultValue(JavaChangeInfo changeInfo,
                                                  final PsiElementFactory factory,
                                                  final JavaParameterInfo info,
                                                  final PsiExpressionList list)
    throws IncorrectOperationException {
    if (info.isUseAnySingleVariable()) {
      final PsiResolveHelper resolveHelper = JavaPsiFacade.getInstance(list.getProject()).getResolveHelper();
      final PsiType type = info.getTypeWrapper().getType(changeInfo.getMethod(), list.getManager());
      final VariablesProcessor processor = new VariablesProcessor(false) {
        protected boolean check(PsiVariable var, ResolveState state) {
          if (var instanceof PsiField && !resolveHelper.isAccessible((PsiField)var, list, null)) return false;
          if (var instanceof PsiLocalVariable && list.getTextRange().getStartOffset() <= var.getTextRange().getStartOffset()) return false;
          if (PsiTreeUtil.isAncestor(var, list, false)) return false;
          final PsiType varType = state.get(PsiSubstitutor.KEY).substitute(var.getType());
          return type.isAssignableFrom(varType);
        }

        public boolean execute(@NotNull PsiElement pe, ResolveState state) {
          super.execute(pe, state);
          return size() < 2;
        }
      };
      PsiScopesUtil.treeWalkUp(processor, list, null);
      if (processor.size() == 1) {
        final PsiVariable result = processor.getResult(0);
        return factory.createExpressionFromText(result.getName(), list);
      }
      if (processor.size() == 0) {
        final PsiClass parentClass = PsiTreeUtil.getParentOfType(list, PsiClass.class);
        if (parentClass != null) {
          PsiClass containingClass = parentClass;
          final Set<PsiClass> containingClasses = new HashSet<PsiClass>();
          while (containingClass != null) {
            if (type.isAssignableFrom(factory.createType(containingClass, PsiSubstitutor.EMPTY))) {
              containingClasses.add(containingClass);
            }
            containingClass = PsiTreeUtil.getParentOfType(containingClass, PsiClass.class);
          }
          if (containingClasses.size() == 1) {
            return RefactoringUtil.createThisExpression(parentClass.getManager(), containingClasses.contains(parentClass) ? null
                                                                                                                          : containingClasses.iterator().next());
          }
        }
      }
    }
    final PsiCallExpression callExpression = PsiTreeUtil.getParentOfType(list, PsiCallExpression.class);
    final String defaultValue = info.getDefaultValue();
    return callExpression != null ? info.getValue(callExpression) : defaultValue.length() > 0 ? factory.createExpressionFromText(defaultValue, list) : null;
  }


  public boolean processPrimaryMethod(ChangeInfo changeInfo) {
    if (!isGosu(changeInfo.getLanguage()) || !(changeInfo instanceof JavaChangeInfo)) return false;
    final PsiElement element = changeInfo.getMethod();
    LOG.assertTrue(element instanceof PsiMethod);
    if (changeInfo.isGenerateDelegate()) {
      generateDelegate((JavaChangeInfo)changeInfo);
    }
    processPrimaryMethod((JavaChangeInfo)changeInfo, (IGosuMethod)element, null, true);
    return true;
  }

  private static boolean isGosu(Language lang) {
    return GosuLanguage.instance() == lang;
  }

  public boolean shouldPreviewUsages(ChangeInfo changeInfo, UsageInfo[] usages) {
    return false;
  }

  @Override
  public boolean setupDefaultValues(ChangeInfo changeInfo, Ref<UsageInfo[]> refUsages, Project project) {
    if (!(changeInfo instanceof JavaChangeInfo)) return true;
    for (UsageInfo usageInfo : refUsages.get()) {
      if (usageInfo instanceof  MethodCallUsageInfo) {
        MethodCallUsageInfo methodCallUsageInfo = (MethodCallUsageInfo)usageInfo;
        if (methodCallUsageInfo.isToChangeArguments()){
          final PsiElement element = methodCallUsageInfo.getElement();
          if (element == null) continue;
          final PsiMethod caller = RefactoringUtil.getEnclosingMethod(element);
          final boolean needDefaultValue = needDefaultValue(changeInfo, caller);
          if (needDefaultValue && (caller == null || !MethodSignatureUtil.isSuperMethod(methodCallUsageInfo.getReferencedMethod(), caller))) {
            final ParameterInfo[] parameters = changeInfo.getNewParameters();
            for (ParameterInfo parameter : parameters) {
              final String defaultValue = parameter.getDefaultValue();
              if (defaultValue == null && parameter.getOldIndex() == -1) {
                ((ParameterInfoImpl)parameter).setDefaultValue("");
                if (!ApplicationManager.getApplication().isUnitTestMode()) {
                  final PsiType type = ((ParameterInfoImpl)parameter).getTypeWrapper().getType(element, element.getManager());
                  final DefaultValueChooser chooser = new DefaultValueChooser(project, parameter.getName(), PsiTypesUtil.getDefaultValueOfType(type));
                  chooser.show();
                  if (chooser.isOK()) {
                    if (chooser.feelLucky()) {
                      parameter.setUseAnySingleVariable(true);
                    } else {
                      ((ParameterInfoImpl)parameter).setDefaultValue(chooser.getDefaultValue());
                    }
                  } else {
                    return false;
                  }
                }
              }
            }
          }
        }
      }
    }
    return true;
  }

  @Override
  public void registerConflictResolvers(List<ResolveSnapshotProvider.ResolveSnapshot> snapshots, @NotNull ResolveSnapshotProvider resolveSnapshotProvider, UsageInfo[] usages, ChangeInfo changeInfo) {
  }

  private static boolean needDefaultValue(ChangeInfo changeInfo, PsiMethod method) {
    return !(changeInfo instanceof JavaChangeInfo) ||
           !((JavaChangeInfo)changeInfo).getMethodsToPropagateParameters().contains(method);
  }

  private static void generateDelegate(JavaChangeInfo changeInfo) throws IncorrectOperationException {
    final PsiMethod delegate = (PsiMethod)changeInfo.getMethod().copy();
    final PsiClass targetClass = changeInfo.getMethod().getContainingClass();
    LOG.assertTrue(!targetClass.isInterface());
    PsiElementFactory factory = JavaPsiFacade.getElementFactory(targetClass.getProject());
    ChangeSignatureProcessor.makeEmptyBody(factory, delegate);
    final PsiCallExpression callExpression = ChangeSignatureProcessor.addDelegatingCallTemplate(delegate, changeInfo.getNewName());
    addDelegateArguments(changeInfo, factory, callExpression);
    targetClass.addBefore(delegate, changeInfo.getMethod());
  }


  private static void addDelegateArguments(JavaChangeInfo changeInfo, PsiElementFactory factory, final PsiCallExpression callExpression) throws IncorrectOperationException {
    final JavaParameterInfo[] newParms = changeInfo.getNewParameters();
    final String[] oldParameterNames = changeInfo.getOldParameterNames();
    for (int i = 0; i < newParms.length; i++) {
      JavaParameterInfo newParm = newParms[i];
      final PsiExpression actualArg;
      if (newParm.getOldIndex() >= 0) {
        actualArg = factory.createExpressionFromText(oldParameterNames[newParm.getOldIndex()], callExpression);
      }
      else {
        actualArg = changeInfo.getValue(i, callExpression);
      }
      final PsiExpressionList argumentList = callExpression.getArgumentList();
      if (actualArg != null && argumentList != null) {
        JavaCodeStyleManager.getInstance(callExpression.getProject()).shortenClassReferences(argumentList.add(actualArg));
      }
    }
  }

  private static void processPrimaryMethod(JavaChangeInfo changeInfo, IGosuMethod method,
                                           PsiMethod baseMethod,
                                           boolean isOriginal) throws IncorrectOperationException {


    if (changeInfo.isVisibilityChanged()) {
      PsiModifierList modifierList = method.getModifierList();
      final String highestVisibility = isOriginal
                                       ? changeInfo.getNewVisibility()
                                       : VisibilityUtil.getHighestVisibility(changeInfo.getNewVisibility(),
                                                                             VisibilityUtil.getVisibilityModifier(modifierList));
      VisibilityUtil.setVisibility(modifierList, highestVisibility);
    }

    if (changeInfo.isNameChanged()) {
      String newName = baseMethod == null ? changeInfo.getNewName() :
                       RefactoringUtil.suggestNewOverriderName(method.getName(), baseMethod.getName(), changeInfo.getNewName());

      if (newName != null && !newName.equals(method.getName())) {
        final PsiIdentifier nameId = method.getNameIdentifier();
        assert nameId != null;
        nameId.replace(parseIdentifier(newName, method));
      }
    }

    final PsiSubstitutor substitutor =
      baseMethod == null ? PsiSubstitutor.EMPTY : ChangeSignatureProcessor.calculateSubstitutor(method, baseMethod);

    if (changeInfo.isReturnTypeChanged()) {
      PsiType newTypeElement = changeInfo.getNewReturnType().getType(changeInfo.getMethod().getParameterList(), method.getManager());
      final PsiType returnType = substitutor.substitute(newTypeElement);
      IGosuTypeElement returnTypeElement = method.getReturnTypeElementGosu();

      if (PsiType.VOID == returnType) {
        if (returnTypeElement != null) {
          PsiElement prev = returnTypeElement;
          do {
            prev = prev.getPrevSibling();
          } while (prev != null && !":".equals(prev.getText()));
          if (prev != null) {
            returnTypeElement.getParent().deleteChildRange(prev, returnTypeElement);
          } else {
            returnTypeElement.delete();
          }
        }
      } else {
        PsiElement newReturnPsiType = createTypeElement(returnType, method);
        if (returnTypeElement != null) {
          returnTypeElement.replace(newReturnPsiType);
        } else {
          PsiCodeBlock body = method.getBody();
          ASTNode astNode = method.getNode();
          if (body == null) {
            astNode.addLeaf(GosuTokenTypes.TT_OP_colon, ":", null);
            method.add(newReturnPsiType);
          } else {
            astNode.addLeaf(GosuTokenTypes.TT_OP_colon, ":", body.getNode());
            method.addBefore(newReturnPsiType, body);
          }
        }
      }
    }

    PsiParameterList list = method.getParameterList();
    PsiParameter[] parameters = list.getParameters();

    final JavaParameterInfo[] parameterInfos = changeInfo.getNewParameters();
    final int delta = baseMethod != null ? baseMethod.getParameterList().getParametersCount() - method.getParameterList().getParametersCount() : 0;
    PsiParameter[] newParms = new PsiParameter[Math.max(parameterInfos.length - delta, 0)];
    final String[] oldParameterNames = changeInfo.getOldParameterNames();
    final String[] oldParameterTypes = changeInfo.getOldParameterTypes();
    for (int i = 0; i < newParms.length; i++) {
      JavaParameterInfo info = parameterInfos[i];
      int index = info.getOldIndex();
      if (index >= 0) {
        PsiParameter parameter = parameters[index];
        newParms[i] = parameter;

        String oldName = oldParameterNames[index];
        if (!oldName.equals(info.getName()) && oldName.equals(parameter.getName())) {
          PsiIdentifier newIdentifier = parseIdentifier(info.getName(), method);
          parameter.getNameIdentifier().replace(newIdentifier);
        }

        String oldType = oldParameterTypes[index];
        if (!oldType.equals(info.getTypeText())) {
          parameter.normalizeDeclaration();
          PsiType newType = substitutor.substitute(info.createType(changeInfo.getMethod().getParameterList(), method.getManager()));
          PsiElement toReplace = createTypeElement(newType, method);
          findChildOfType(parameter, GosuTypeLiteralImpl.class).replace(toReplace);
        }
      }
      else {
        newParms[i] = createNewParameter(changeInfo, info, substitutor);
      }
    }

    resolveParameterVsFieldsConflicts(newParms, method, list, changeInfo.toRemoveParm());
    fixJavadocsForChangedMethod(method, changeInfo, newParms.length);
  }

  private static GosuTypeLiteralImpl createTypeElement(PsiType type, IGosuMethod context) {
    importAllTypes(type, context.getContainingFile());
    return findChildOfType(createParameter("foo", type, context), GosuTypeLiteralImpl.class);
  }

  private static PsiClassType[] getPrimaryChangedExceptionInfo(JavaChangeInfo changeInfo) throws IncorrectOperationException {
    final ThrownExceptionInfo[] newExceptionInfos = changeInfo.getNewExceptions();
    PsiClassType[] newExceptions = new PsiClassType[newExceptionInfos.length];
    final PsiMethod method = changeInfo.getMethod();
    for (int i = 0; i < newExceptions.length; i++) {
      newExceptions[i] =
        (PsiClassType)newExceptionInfos[i].createType(method, method.getManager()); //context really does not matter here
    }
    return newExceptions;
  }


  private static void processCallerMethod(JavaChangeInfo changeInfo, PsiMethod caller,
                                          PsiMethod baseMethod,
                                          boolean toInsertParams,
                                          boolean toInsertThrows) throws IncorrectOperationException {
    LOG.assertTrue(toInsertParams || toInsertThrows);
    if (toInsertParams) {
      List<PsiParameter> newParameters = new ArrayList<PsiParameter>();
      ContainerUtil.addAll(newParameters, caller.getParameterList().getParameters());
      final JavaParameterInfo[] primaryNewParms = changeInfo.getNewParameters();
      PsiSubstitutor substitutor =
        baseMethod == null ? PsiSubstitutor.EMPTY : ChangeSignatureProcessor.calculateSubstitutor(caller, baseMethod);
      final PsiClass aClass = changeInfo.getMethod().getContainingClass();
      final PsiClass callerContainingClass = caller.getContainingClass();
      final PsiSubstitutor psiSubstitutor = aClass != null && callerContainingClass != null && callerContainingClass.isInheritor(aClass, true) 
                                            ? TypeConversionUtil.getSuperClassSubstitutor(aClass, callerContainingClass, substitutor) 
                                            : PsiSubstitutor.EMPTY;
      for (JavaParameterInfo info : primaryNewParms) {
        if (info.getOldIndex() < 0) newParameters.add(createNewParameter(changeInfo, info, psiSubstitutor, substitutor));
      }
      PsiParameter[] arrayed = newParameters.toArray(new PsiParameter[newParameters.size()]);
      boolean[] toRemoveParm = new boolean[arrayed.length];
      Arrays.fill(toRemoveParm, false);
      resolveParameterVsFieldsConflicts(arrayed, caller, caller.getParameterList(), toRemoveParm);
    }

    if (toInsertThrows) {
      List<PsiJavaCodeReferenceElement> newThrowns = new ArrayList<PsiJavaCodeReferenceElement>();
      final PsiReferenceList throwsList = caller.getThrowsList();
      ContainerUtil.addAll(newThrowns, throwsList.getReferenceElements());
      final ThrownExceptionInfo[] primaryNewExns = changeInfo.getNewExceptions();
      for (ThrownExceptionInfo thrownExceptionInfo : primaryNewExns) {
        if (thrownExceptionInfo.getOldIndex() < 0) {
          final PsiClassType type = (PsiClassType)thrownExceptionInfo.createType(caller, caller.getManager());
          final PsiJavaCodeReferenceElement ref =
            JavaPsiFacade.getInstance(caller.getProject()).getElementFactory().createReferenceElementByType(type);
          newThrowns.add(ref);
        }
      }
      PsiJavaCodeReferenceElement[] arrayed = newThrowns.toArray(new PsiJavaCodeReferenceElement[newThrowns.size()]);
      boolean[] toRemoveParm = new boolean[arrayed.length];
      Arrays.fill(toRemoveParm, false);
      ChangeSignatureUtil.synchronizeList(throwsList, Arrays.asList(arrayed), ThrowsList.INSTANCE, toRemoveParm);
    }
  }

  private static void fixPrimaryThrowsLists(PsiMethod method, PsiClassType[] newExceptions) throws IncorrectOperationException {
    PsiElementFactory elementFactory = JavaPsiFacade.getInstance(method.getProject()).getElementFactory();
    PsiJavaCodeReferenceElement[] refs = new PsiJavaCodeReferenceElement[newExceptions.length];
    for (int i = 0; i < refs.length; i++) {
      refs[i] = elementFactory.createReferenceElementByType(newExceptions[i]);
    }
    PsiReferenceList throwsList = elementFactory.createReferenceList(refs);

    PsiReferenceList methodThrowsList = (PsiReferenceList)method.getThrowsList().replace(throwsList);
    methodThrowsList = (PsiReferenceList)JavaCodeStyleManager.getInstance(method.getProject()).shortenClassReferences(methodThrowsList);
    CodeStyleManager.getInstance(method.getManager().getProject())
        .reformatRange(method, method.getParameterList().getTextRange().getEndOffset(),
                       methodThrowsList.getTextRange().getEndOffset());
  }

  private static void fixJavadocsForChangedMethod(final PsiMethod method, final JavaChangeInfo changeInfo, int newParamsLength) throws IncorrectOperationException {
    final PsiParameter[] parameters = method.getParameterList().getParameters();
    final JavaParameterInfo[] newParms = changeInfo.getNewParameters();
    LOG.assertTrue(parameters.length <= newParamsLength);
    final Set<PsiParameter> newParameters = new HashSet<PsiParameter>();
    final String[] oldParameterNames = changeInfo.getOldParameterNames();
    for (int i = 0; i < newParamsLength; i++) {
      JavaParameterInfo newParm = newParms[i];
      if (newParm.getOldIndex() < 0 ||
          newParm.getOldIndex() == i && !(newParm.getName().equals(oldParameterNames[newParm.getOldIndex()]) && newParm.getTypeText().equals(changeInfo.getOldParameterTypes()[newParm.getOldIndex()]))) {
        newParameters.add(parameters[i]);
      }
    }
    RefactoringUtil.fixJavadocsForParams(method, newParameters, new Condition<Pair<PsiParameter, String>>() {
      @Override
      public boolean value(Pair<PsiParameter, String> pair) {
        final PsiParameter parameter = pair.first;
        final String oldParamName = pair.second;
        final int idx = Arrays.binarySearch(oldParameterNames, oldParamName);
        return idx >= 0 && idx == method.getParameterList().getParameterIndex(parameter) && changeInfo.getNewParameters()[idx].getOldIndex() == idx;
      }
    });
  }

  private static PsiParameter createNewParameter(JavaChangeInfo changeInfo, JavaParameterInfo newParm,
                                                 PsiSubstitutor... substitutor) throws IncorrectOperationException {
    PsiMethod method = changeInfo.getMethod();
    final PsiParameterList list = method.getParameterList();

    PsiType type = newParm.createType(list, list.getManager());
    for (PsiSubstitutor psiSubstitutor : substitutor) {
      type = psiSubstitutor.substitute(type);
    }
    importAllTypes(type, method.getContainingFile());
    return createParameter(newParm.getName(), type, method);
  }

  private static void resolveParameterVsFieldsConflicts(final PsiParameter[] newParms,
                                                        final PsiMethod method,
                                                        final PsiParameterList list,
                                                        boolean[] toRemoveParm) throws IncorrectOperationException {
    List<FieldConflictsResolver> conflictResolvers = new ArrayList<FieldConflictsResolver>();
    for (PsiParameter parameter : newParms) {
      conflictResolvers.add(new FieldConflictsResolver(parameter.getName(), method.getBody()));
    }
    ChangeSignatureUtil.synchronizeList(list, Arrays.asList(newParms), ParameterList.INSTANCE, toRemoveParm);
//    JavaCodeStyleManager.getInstance(list.getProject()).shortenClassReferences(list);
    for (FieldConflictsResolver fieldConflictsResolver : conflictResolvers) {
      fieldConflictsResolver.fix();
    }
  }

  private static boolean needToCatchExceptions(JavaChangeInfo changeInfo, PsiMethod caller) {
    return changeInfo.isExceptionSetOrOrderChanged() &&
           !(changeInfo instanceof JavaChangeInfo && ((JavaChangeInfo)changeInfo).getMethodsToPropagateParameters().contains(caller));
  }

  private static class ParameterList implements ChangeSignatureUtil.ChildrenGenerator<PsiParameterList, PsiParameter> {
    public static final ParameterList INSTANCE = new ParameterList();

    public List<PsiParameter> getChildren(PsiParameterList psiParameterList) {
      return Arrays.asList(psiParameterList.getParameters());
    }
  }

  private static class ThrowsList implements ChangeSignatureUtil.ChildrenGenerator<PsiReferenceList, PsiJavaCodeReferenceElement> {
    public static final ThrowsList INSTANCE = new ThrowsList();

    public List<PsiJavaCodeReferenceElement> getChildren(PsiReferenceList throwsList) {
      return Arrays.asList(throwsList.getReferenceElements());
    }
  }

  private static class ConflictSearcher {
    private final JavaChangeInfo myChangeInfo;

    private ConflictSearcher(JavaChangeInfo changeInfo) {
      this.myChangeInfo = changeInfo;
    }

    public MultiMap<PsiElement, String> findConflicts(Ref<UsageInfo[]> refUsages) {
      final MultiMap<PsiElement, String> conflictDescriptions = new MultiMap<PsiElement, String>();
      addMethodConflicts(conflictDescriptions);
      final Set<UsageInfo> usagesSet = new HashSet<UsageInfo>(Arrays.asList(refUsages.get()));
      RenameUtil.removeConflictUsages(usagesSet);
      if (myChangeInfo.isVisibilityChanged()) {

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
          public void run() {
            try {
              addInaccessibilityDescriptions(usagesSet, conflictDescriptions);
            }
            catch (IncorrectOperationException e) {
              LOG.error(e);
            }
          }
        });
      }

      for (UsageInfo usageInfo : usagesSet) {
        final PsiElement element = usageInfo.getElement();
        if (usageInfo instanceof OverriderUsageInfo) {
          final PsiMethod method = (PsiMethod)element;
          final PsiMethod baseMethod = ((OverriderUsageInfo)usageInfo).getBaseMethod();
          final int delta = baseMethod.getParameterList().getParametersCount() - method.getParameterList().getParametersCount();
          if (delta > 0) {
            final boolean[] toRemove = myChangeInfo.toRemoveParm();
            if (toRemove[toRemove.length - 1]) { //todo check if implicit parameter is not the last one
              conflictDescriptions.putValue(baseMethod, "Implicit last parameter should not be deleted");
            }
          }
        } else if (element instanceof PsiMethodReferenceExpression) {
          conflictDescriptions.putValue(element, "Changed method is used in method reference");
        }
      }

      return conflictDescriptions;
    }

    private boolean needToChangeCalls() {
      return myChangeInfo.isNameChanged() || myChangeInfo.isParameterSetOrOrderChanged() || myChangeInfo.isExceptionSetOrOrderChanged();
    }


    private void addInaccessibilityDescriptions(Set<UsageInfo> usages, MultiMap<PsiElement, String> conflictDescriptions)
      throws IncorrectOperationException {
      PsiMethod method = myChangeInfo.getMethod();
      PsiModifierList modifierList = (PsiModifierList)method.getModifierList().copy();
      VisibilityUtil.setVisibility(modifierList, myChangeInfo.getNewVisibility());

      for (Iterator<UsageInfo> iterator = usages.iterator(); iterator.hasNext();) {
        UsageInfo usageInfo = iterator.next();
        PsiElement element = usageInfo.getElement();
        if (element != null) {
          if (element instanceof PsiQualifiedReference) {
            PsiClass accessObjectClass = null;
            PsiElement qualifier = ((PsiQualifiedReference)element).getQualifier();
            if (qualifier instanceof PsiExpression) {
              accessObjectClass = (PsiClass)PsiUtil.getAccessObjectClass((PsiExpression)qualifier).getElement();
            }

            if (!JavaPsiFacade.getInstance(element.getProject()).getResolveHelper()
              .isAccessible(method, modifierList, element, accessObjectClass, null)) {
              String message =
                RefactoringBundle.message("0.with.1.visibility.is.not.accessible.from.2",
                                          RefactoringUIUtil.getDescription(method, true),
                                          VisibilityUtil.toPresentableText(myChangeInfo.getNewVisibility()),
                                          RefactoringUIUtil.getDescription(ConflictsUtil.getContainer(element), true));
              conflictDescriptions.putValue(method, message);
              if (!needToChangeCalls()) {
                iterator.remove();
              }
            }
          }
        }
      }
    }


    private void addMethodConflicts(MultiMap<PsiElement, String> conflicts) {
      String newMethodName = myChangeInfo.getNewName();
      if (!(myChangeInfo instanceof JavaChangeInfo)) {
        return;
      }
      try {
        PsiMethod prototype;
        final PsiMethod method = myChangeInfo.getMethod();
        if (!isGosu(method.getLanguage())) return;
        PsiManager manager = method.getManager();
        PsiElementFactory factory = JavaPsiFacade.getInstance(manager.getProject()).getElementFactory();
        final CanonicalTypes.Type returnType = myChangeInfo.getNewReturnType();
        if (returnType != null) {
          prototype = factory.createMethod(newMethodName, returnType.getType(method, manager));
        }
        else {
          prototype = factory.createConstructor();
          prototype.setName(newMethodName);
        }
        JavaParameterInfo[] parameters = myChangeInfo.getNewParameters();


        for (JavaParameterInfo info : parameters) {
          PsiType parameterType = info.createType(method, manager);
          if (parameterType == null) {
            parameterType =
              JavaPsiFacade.getElementFactory(method.getProject()).createTypeFromText(CommonClassNames.JAVA_LANG_OBJECT, method);
          }
          PsiParameter param = factory.createParameter(info.getName(), parameterType);
          prototype.getParameterList().add(param);
        }

        ConflictsUtil.checkMethodConflicts(method.getContainingClass(), method, prototype, conflicts);
      }
      catch (IncorrectOperationException e) {
        LOG.error(e);
      }
    }
  }

  private static class ExpressionList implements ChangeSignatureUtil.ChildrenGenerator<PsiExpressionList, PsiExpression> {
    public static final ExpressionList INSTANCE = new ExpressionList();

    public List<PsiExpression> getChildren(PsiExpressionList psiExpressionList) {
      return Arrays.asList(psiExpressionList.getExpressions());
    }
  }

}

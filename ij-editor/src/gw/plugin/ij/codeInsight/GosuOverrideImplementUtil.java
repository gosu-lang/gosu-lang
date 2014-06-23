/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.codeInsight;

import gw.plugin.ij.util.GosuModuleUtil;

import gw.lang.reflect.TypeSystem;

import com.intellij.psi.PsiFile;
import com.intellij.psi.SmartPsiElementPointer;

import com.intellij.codeInsight.generation.OverrideImplementUtil;
import com.intellij.codeInsight.generation.PsiMethodMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.infos.CandidateInfo;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.containers.hash.LinkedHashMap;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;
import gw.plugin.ij.util.ClassLord;
import gw.plugin.ij.util.GosuProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GosuOverrideImplementUtil {
  private static final Logger LOG = Logger.getInstance("gw.plugin.ij.codeInsight.GosuOverrideImplementUtil");
  private static final String JAVA_LANG_OVERRIDE = "java.lang.Override";

  private static int adjustOffsetForLBrace(@NotNull IGosuTypeDefinition aClass, int offset) {
    LeafPsiElement lBrace = aClass.getGosuLBrace();
    if (lBrace == null) {
//      throw new UnsupportedOperationException("men at work");
      return offset;
    }
    int lBraceOffset = lBrace.getTextOffset();
    return offset < lBraceOffset ? lBraceOffset + 1 : offset;
  }

  private static void moveCaret(@NotNull Editor editor, int offset) {
    editor.getCaretModel().moveToOffset(offset);
    editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
    editor.getSelectionModel().removeSelection();
  }

  public static void invokeOverrideImplement(@NotNull final Editor editor, @NotNull final PsiFile file, final boolean isImplement) {
    final int offset = editor.getCaretModel().getOffset();
    final IGosuTypeDefinition aClass = PsiTreeUtil.findElementOfClassAtOffset(file, offset, IGosuTypeDefinition.class, false);
    if (aClass != null) {
      final int usableOffset = adjustOffsetForLBrace(aClass, offset);
      final ASTNode locationNode = findImmediateChildAtOffset(aClass.getNode(), usableOffset);
      final PsiElement location = locationNode != null ? locationNode.getPsi() : null;
      if (location != null) {
        invokeOverrideImplement(editor, aClass, location, isImplement);
      }
    }
  }

  public static void invokeOverrideImplement(@NotNull final Editor editor, @NotNull final IGosuTypeDefinition aClass, final boolean isImplement) {
    ASTNode locationNode = findLastChildBefore(aClass.getNode(), GosuElementTypes.METHOD_DEFINITION, GosuElementTypes.CLASS_DEFINITION);
    locationNode = findPriorNonDocNonWSNode(locationNode);
    PsiElement location = locationNode != null ? locationNode.getPsi() : null;
    // XXX should/can 'isImplement' be dynamically computed?
    if (location != null) {
      invokeOverrideImplement(editor, aClass, location, true);
    }
  }

  public static void invokeOverrideImplement(@NotNull final Editor editor, @NotNull final IGosuTypeDefinition aClass, @NotNull PsiElement location, final boolean isImplement) {
    if (isImplement && aClass.isInterface()) {
      return;
    }

    Collection<CandidateInfo> candidates = getMethodsToOverrideImplement(aClass, isImplement);
    Collection<CandidateInfo> secondary = isImplement ? Collections.<CandidateInfo>emptyList() : getMethodsToOverrideImplement(aClass, !isImplement);

    final MemberChooser<PsiMethodMember> chooser = OverrideImplementUtil.showOverrideImplementChooser(editor, aClass, isImplement, candidates, secondary);
    if (chooser == null) {
      return;
    }

    final List<PsiMethodMember> selectedElements = chooser.getSelectedElements();
    if (selectedElements == null || selectedElements.isEmpty()) {
      return;
    }

//    //========
//    for (PsiMethodMember methodMember : selectedElements) {
//      final PsiMethod method = methodMember.getElement();
//      IModule module = GosuModuleUtil.findModuleForPsiElement(method);
//      final GosuMethodBaseImpl methodDecl = (GosuMethodBaseImpl) GosuPsiParseUtil.parseDeclaration("function test() {}", method.getManager(), module);
//
//      CodeEditUtil.setOldIndentation(methodDecl.getNode(), 1);
//
//
//      methodDecl.getNameIdentifier().replace(method.getNameIdentifier().copy());
//
//
//      for(PsiParameter param : method.getParameterList().getParameters()) {
//        //methodDecl.getParameterList().addParameterToHead();
//      }
//
//
//
//      ApplicationManager.getApplication().runWriteAction(new Runnable() {
//        public void run() {
//          aClass.add(methodDecl);
//
//          CodeStyleManager manager = CodeStyleManager.getInstance(method.getProject());
//          manager.reformat(methodDecl);
//
//        }
//      });
//    }

    final PsiFile file = aClass.getContainingFile();
    final StringBuilder sb = new StringBuilder();
    final Map<String, String> imports = new LinkedHashMap<>();
//    IGosuFile gosuFile = (IGosuFile) aClass.getContainingFile();
//    for(String fqn : imports) {
//      gosuFile.addImport(fqn);
//    }

    // XXX entire location computation should be inside a write action...
    // XXX replace this with true psi node insertion, coming soon.
    final int offset = location.getTextRange().getStartOffset();
    final int prefix = location.getNode().getTreePrev().getTextLength();
    final int suffix = location.getTextRange().getLength();
    final PsiElement[] caretNode = new PsiElement[1];
    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      public void run() {
        sb.append("\n");
        for (PsiMethodMember methodMember : selectedElements) {
          sb.append(generateImplementation(editor, file, aClass, methodMember.getElement(), methodMember.getSubstitutor(), imports));
        }
        sb.setLength(sb.length() - 2);
        editor.getDocument().insertString(offset, sb.toString());
        PsiDocumentManager.getInstance(file.getProject()).commitDocument(editor.getDocument());

        ASTNode firstMethodNode = PsiTreeUtil.findElementOfClassAtOffset(file, offset + 1, PsiElement.class, true).getNode();
        ASTNode statementList = firstMethodNode.getTreeParent().findChildByType(GosuElementTypes.ELEM_TYPE_StatementList);
        caretNode[0] = statementList.getFirstChildNode().getPsi();
      }
    });

    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      public void run() {
        //reformat only the inserted region plus adjacent elements on either side.
        CodeStyleManager.getInstance(file.getProject()).reformatRange(file, offset - prefix, offset + sb.length() + suffix);
        PsiDocumentManager.getInstance(file.getProject()).commitDocument(editor.getDocument());
        moveCaret(editor, caretNode[0].getTextOffset() + 1);
        
        TypeSystem.getByFullNameIfValid(aClass.getQualifiedName(), GosuModuleUtil.findModuleForPsiElement(file));
        for (String fqn : imports.values()) {
          ClassLord.doImport(fqn, file);
        }
      }
    });
  }

  public static String generateImplementation(@Nullable final Editor editor,
                                              final PsiFile file,
                                              final IGosuTypeDefinition aClass,
                                              @NotNull final PsiMethod method,
                                              @NotNull final PsiSubstitutor substitutor,
                                              final Map<String, String> imports) {
    final boolean isAbstract = method.hasModifierProperty(PsiModifier.ABSTRACT);

    final StringBuilder sb = new StringBuilder();
    sb.append("override ");

    final PsiModifierList modifierList = method.getModifierList();
    for (Object o : modifierList.getAnnotations()) {
      //TODO cgross implement annotations
    }

    //TODO cgross should match up with introspector

    // Name
    final String getterName = GosuProperties.getGetterName(method);
    final String setterName = GosuProperties.getSetterName(method);

    if (getterName != null) {
      sb.append("property get ");
      sb.append(getterName);
    } else if (setterName != null) {
      sb.append("property set ");
      sb.append(setterName);
    } else {
      sb.append("function ");
      sb.append(method.getName());
    }

    // Type Parameters
    final PsiTypeParameter[] typeParameters = method.getTypeParameters();
    if (typeParameters.length > 0) {
      sb.append('<');
      for (int i = 0; i < typeParameters.length; ++i) {
        if (i > 0) {
          sb.append(',');
        }
        sb.append(typeParameters[i].getName());
      }
      sb.append('>');
    }

    // Parameters
    sb.append("(");
    final PsiParameter[] parameters = method.getParameterList().getParameters();
    for (int i = 0; i < parameters.length; i++) {
      if (i > 0) {
        sb.append(',');
      }
      final PsiParameter parameter = parameters[i];
      final String parameterName = parameter.getName();
      sb.append(parameterName != null ? parameterName : "p" + i);
      sb.append(':');
      sb.append(simplifyTypes(substitutor.substitute(parameter.getType()), file, imports).replace("...", "[]"));
    }
    sb.append(")");

    // Return Type
    final PsiType returnType = substitutor.substitute(method.getReturnType());
    boolean isVoid = returnType == null || returnType.equals(PsiType.VOID);
    if (!isVoid) {
      sb.append(':');
      sb.append(simplifyTypes(returnType, file, imports));
    }

    // Body
    sb.append("{\n");
    if (!isVoid) {
      if (returnType instanceof PsiPrimitiveType) {
        if (returnType.equals(PsiType.BOOLEAN)) {
          sb.append("return false\n");
        } else {
          sb.append("return 0\n");
        }
      } else {
        sb.append("return null\n");
      }
    }
    sb.append("}\n\n");

    return sb.toString();
  }

  private static String simplifyTypes(PsiType type, PsiElement context, Map<String, String> imports) {
    if (context instanceof IGosuPsiElement) {
      return ClassLord.simplifyTypes(type.getCanonicalText(), (IGosuPsiElement) context, imports);
    } else {
      return type.getCanonicalText();
    }
  }

  private static String getTypeRefString(@NotNull PsiType type, @NotNull Set<String> imports) {
    // todo what about enums?
    // todo honor settings wrt/ inner class imports
    String result = type.getCanonicalText();
    if (type instanceof PsiClassReferenceType) {
      PsiClass resolvedType = ((PsiClassReferenceType) type).resolve();
      if (resolvedType != null && canImport(resolvedType)) {
        imports.add(resolvedType.getQualifiedName());
        result = resolvedType.getName();
      }
    }
    return result;
  }

  private static boolean canImport(PsiClass type) {
    return true;
  }

  @NotNull
  public static Collection<CandidateInfo> getMethodsToOverrideImplement(@NotNull IGosuTypeDefinition aClass, boolean isImplement) {
    final Collection<CandidateInfo> methods = OverrideImplementUtil.getMethodsToOverrideImplement(aClass, isImplement);

    final Iterator<CandidateInfo> it = methods.iterator();
    while (it.hasNext()) {
      if (isImplementedViaProperty(aClass, it.next().getElement())) {
        it.remove();
      }
    }
    return methods;
  }

  private static boolean isImplementedViaProperty(@NotNull IGosuTypeDefinition klass, PsiElement element) {
    if (element instanceof PsiMethod) {
      final PsiMethod method = (PsiMethod) element;
      final String getterName = GosuProperties.getGetterName(method);
      if (getterName != null) {
        return hasPropertyGetter(klass, getterName);
      }

      final String setterName = GosuProperties.getSetterName(method);
      if (setterName != null) {
        return hasPropertySetter(klass, setterName);
      }
    }
    return false;
  }

  private static boolean hasPropertyGetter(@NotNull IGosuTypeDefinition klass, @NotNull String property) {
    for (PsiMethod method : klass.getAllMethods()) {
      if (!method.hasModifierProperty(PsiModifier.ABSTRACT) && property.equals(GosuProperties.getGetterName(method))) {
        return true;
      }
    }
    return false;
  }

  private static boolean hasPropertySetter(@NotNull IGosuTypeDefinition klass, @NotNull String property) {
    for (PsiMethod method : klass.getAllMethods()) {
      if (!method.hasModifierProperty(PsiModifier.ABSTRACT) && property.equals(GosuProperties.getSetterName(method))) {
        return true;
      }
    }
    return false;
  }


//  private static void positionCaret(Editor editor, GrMethod result) {
//    final GrOpenBlock body = result.getBlock();
//    if (body == null) return;
//
//    final PsiElement lBrace = body.getLBrace();
//
//    assert lBrace != null;
//    PsiElement l = lBrace.getNextSibling();
//    assert l != null;
//
//    final PsiElement rBrace = body.getRBrace();
//
//    assert rBrace != null;
//    PsiElement r = rBrace.getPrevSibling();
//    assert r != null;
//
//    LOG.assertTrue(!PsiDocumentManager.getInstance(result.getProject()).isUncommited(editor.getDocument()));
//    String text = editor.getDocument().getText();
//
//    int start = l.getTextRange().getStartOffset();
//    start = CharArrayUtil.shiftForward(text, start, "\n\t ");
//    int end = r.getTextRange().getEndOffset();
//    end = CharArrayUtil.shiftBackward(text, end - 1, "\n\t ") + 1;
//
//    editor.getCaretModel().moveToOffset(Math.min(start, end));
//    editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
//    if (start < end) {
//      //Not an empty body
//      editor.getSelectionModel().setSelection(start, end);
//    }
//  }
//
//  private static boolean writeMethodModifiers(StringBuffer text, PsiModifierList modifierList, String[] modifiers) {
//    boolean wasAddedModifiers = false;
//    for (String modifierType : modifiers) {
//      if (modifierList.hasModifierProperty(modifierType) && modifierType != PsiModifier.PUBLIC) {
//        text.append(modifierType);
//        text.append(" ");
//        wasAddedModifiers = true;
//      }
//    }
//    return wasAddedModifiers;
//  }
//
//

//  @NotNull
//  private static String callSuper(PsiMethod superMethod, PsiMethod overriding) {
//    @NonNls StringBuilder buffer = new StringBuilder();
//    if (!superMethod.isConstructor() && superMethod.getReturnType() != PsiType.VOID) {
//      buffer.append("return ");
//    }
//    buffer.append("super");
//    PsiParameter[] parms = overriding.getParameterList().getParameters();
//    if (!superMethod.isConstructor()) {
//      buffer.append(".");
//      buffer.append(superMethod.getName());
//    }
//    buffer.append("(");
//    for (int i = 0; i < parms.length; i++) {
//      String name = parms[i].getName();
//      if (i > 0) buffer.append(",");
//      buffer.append(name);
//    }
//    buffer.append(")");
//    return buffer.toString();
//  }


  private static ASTNode findImmediateChildAtOffset(@NotNull ASTNode parent, int offset) {
    // XXX need to adjust for javadoc and annotation nodes.
    ASTNode candidate = parent.getFirstChildNode();
    for (ASTNode element = candidate; element != null; element = element.getTreeNext()) {
      int startOffset = element.getStartOffset();
      if (offset < startOffset) {
        // XXX how do we get here?
        break;
      }
      if (offset == startOffset) {
        candidate = element;
        break;
      }
      int endOffset = startOffset + element.getTextLength();
      if (offset <= endOffset) {
        // XXX what to return if <next> is null?
        candidate = element.getTreeNext();
        break;
      }
    }
    return candidate;
  }

  // Jumps before existing whitespace and doc nodes attached to node.
  private static ASTNode findPriorNonDocNonWSNode(ASTNode node) {
    ASTNode follower = node;
    if (follower != null) {
      ASTNode candidate = follower.getTreePrev();
      while (candidate instanceof PsiWhiteSpace || candidate instanceof PsiComment) {
        follower = candidate;
        candidate = candidate.getTreePrev();
      }
    }
    return follower;
  }

  @Nullable
  private static ASTNode findLastChildBefore(@NotNull ASTNode parent, IElementType typeToFind, IElementType typeToPrecede) {
    ASTNode candidate = null;
    ASTNode findMatch = null;
    ASTNode precedeMatch = null;
    for (ASTNode child = parent.getFirstChildNode(); child != null; child = child.getTreeNext()) {
      IElementType candidateType = child.getElementType();
      if (candidateType == typeToPrecede) {
        precedeMatch = child;
        break;
      }
      if (candidateType == typeToFind) {
        findMatch = child;
      }
    }
    if (findMatch != null) {
      candidate = findMatch.getTreeNext();
    } else if (precedeMatch != null) {
      candidate = precedeMatch.getTreePrev();
      if (!(candidate instanceof PsiWhiteSpace)) {
        // XXX is it possible to back over the open curly bracket?
        candidate = precedeMatch;
      }
    }
    if (candidate == null) {
      candidate = parent.getLastChildNode();
    }
    return candidate;
  }

  private GosuOverrideImplementUtil() {
  }
}

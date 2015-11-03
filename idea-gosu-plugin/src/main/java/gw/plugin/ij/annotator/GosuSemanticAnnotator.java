/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.annotator;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.impl.java.stubs.JavaStubElementTypes;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.IBlockLiteralExpression;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.reflect.gs.IGosuClass;
import gw.plugin.ij.highlighter.GosuHighlighterColors;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifierList;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import gw.plugin.ij.lang.psi.impl.GosuFragmentFileImpl;
import gw.plugin.ij.util.ExceptionUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GosuSemanticAnnotator implements Annotator {
  private static final Logger LOG = Logger.getInstance(GosuSemanticAnnotator.class);

  @Override
  public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
    if (PsiTreeUtil.getParentOfType(element, GosuFragmentFileImpl.class) != null) {
      return;
    }
    if (element.getChildren().length != 0) {
      return;
    }

    try {
      final TextAttributesKey key = visitPsiElement(element);
      if (key != null && key != GosuHighlighterColors.DEFAULT_ATTRKEY) {
        final Annotation annotation = holder.createInfoAnnotation(element, null);
        annotation.setTextAttributes(key);
      }
    } catch (ProcessCanceledException e) {
      throw e;
    } catch (Exception e) {
      if (ExceptionUtil.isWrappedCanceled(e)) {
        throw new ProcessCanceledException(e);
      }
      LOG.error(e);
    } catch (AssertionError e) {
      LOG.error(e);
    }
  }

  @Nullable
  public TextAttributesKey visitPsiElement(@NotNull PsiElement element) {
    if (element instanceof PsiJavaToken) {
      if (((PsiJavaToken) element).getTokenType() == GosuTokenTypes.TT_IDENTIFIER) {
        if (NATIVE_TYPES.contains(element.getText())) {
          return GosuHighlighterColors.KEYWORD;
        }

        if (element instanceof ASTNode) {
          return deriveFromParent((ASTNode) element);
        }
      }
    } else if (element instanceof LeafPsiElement) {
      final IElementType type = ((LeafPsiElement) element).getElementType();
      return COLOR_MAP.get(type);
    }
    return null;
  }

  @Nullable
  private TextAttributesKey deriveFromParent(@NotNull ASTNode child) {
    ASTNode node = child.getTreeParent();
    if (node == null || node == child) {
      return null;
    }

    IElementType type = node.getElementType();
    TextAttributesKey key;
    if (type == GosuElementTypes.ELEM_TYPE_IdentifierExpression ||
        type == GosuElementTypes.ELEM_TYPE_BeanMethodCallExpression ||
        type == GosuElementTypes.ELEM_TYPE_MemberAccess ||
        type == GosuElementTypes.ELEM_TYPE_TypeLiteral) {
      key = deriveFromReference(node);
    } else {
      key = COLOR_MAP.get(type);
      if (key == null) {
        ASTNode parent = node.getTreeParent();
        if (parent != null) {
          // todo may need to short circuit this
          key = deriveFromParent(parent);
        }
      } else {
        PsiElement psi = node.getPsi();
        if (type == GosuElementTypes.CLASS_DEFINITION) {
          if (isAbstractClass(psi)) {
            key = GosuHighlighterColors.ABSTRACT_CLASS_NAME_ATTRKEY;
          }
        } else if (type == GosuElementTypes.FIELD) {
          if (psi instanceof IGosuModifierList) {
            if (((IGosuModifierList) psi).hasModifierProperty("static")) {
              key = GosuHighlighterColors.STATIC_FIELD_ATTRKEY;
            }
          }
        } else if (type == GosuElementTypes.METHOD_DEFINITION) {
          if (psi instanceof PsiMethod && ((PsiMethod) psi).isConstructor()) {
            key = GosuHighlighterColors.CONSTRUCTOR_DECLARATION_ATTRKEY;
          } else if (psi instanceof IGosuModifierList) {
            if (((IGosuModifierList) psi).hasModifierProperty("static")) {
              key = GosuHighlighterColors.STATIC_METHOD_ATTRKEY;
            }
          }
        }
      }
    }
    return key;
  }

  @Nullable
  private TextAttributesKey deriveFromReference(@NotNull ASTNode node) {
    TextAttributesKey key = null;

    PsiElement psi = node.getPsi();
    if (psi instanceof PsiReference) {
      PsiElement resolve = null;
      try {
        resolve = ((PsiReference) psi).resolve();
      } catch (Exception ex) {
        // ignore... probably a bug, but it'll be caught and logged by some other operation.
      }

      if (resolve instanceof PsiClass) {
        // XXX better way to do this?
        ASTNode parentNode = node.getTreeParent();
        IElementType parentType = parentNode != null ? parentNode.getElementType() : null;
        if (((PsiClass) resolve).isAnnotationType()) {
          key = GosuHighlighterColors.ANNOTATION_NAME_ATTRKEY;
        } else if (parentType == GosuElementTypes.ELEM_TYPE_NewExpression && !isAnonymousClassExpr(parentNode)) {
          key = GosuHighlighterColors.CONSTRUCTOR_CALL_ATTRKEY;
        } else if (isAbstractClass(resolve)) {
          key = GosuHighlighterColors.ABSTRACT_CLASS_NAME_ATTRKEY;
        } else if (((PsiClass) resolve).isInterface()) {
          key = GosuHighlighterColors.INTERFACE_NAME_ATTRKEY;
        }
      } else if (resolve == null && psi instanceof GosuBaseElementImpl) {
        // block literals show up as psi class refs in the tree, but can't be resolved so handle them here
        IParsedElement pe = ((GosuBaseElementImpl) psi).getParsedElement();
        if (pe instanceof IBlockLiteralExpression) {
          key = GosuHighlighterColors.PARAMETER_ATTRKEY;
        }
      }

      if (key == null) {
        if (resolve != null) {
          final ASTNode resolvedNode = resolve.getNode();
          if (resolvedNode != null) {
            // IntelliJ doesn't differentiate variable references from declarations
            // If we want to, use _colorRef map here to enable reference distinction for Gosu
            key = COLOR_FROM_REF_MAP.get(resolvedNode.getElementType());
          } else if (resolve instanceof StubBasedPsiElement) {
            key = COLOR_FROM_REF_MAP.get(((StubBasedPsiElement) resolve).getElementType());
          }
        } else {
          key = deriveFromParent(node);
        }
      }
    }

    return key;
  }

  private boolean isAnonymousClassExpr(@NotNull ASTNode node) {
    final PsiElement psi = node.getPsi();
    if (psi instanceof GosuBaseElementImpl) {
      final IParsedElement pe = ((GosuBaseElementImpl) psi).getParsedElement();
      if (pe instanceof INewExpression && ((INewExpression) pe).isAnonymousClass()) {
        return true;
      }
    }
    return false;
  }

  private boolean isAbstractClass(@NotNull PsiElement psi) {
    if (psi instanceof GosuBaseElementImpl) {
      final IParsedElement pe = ((GosuBaseElementImpl) psi).getParsedElement();
      if (pe != null) {
        final IGosuClass klass = pe.getGosuClass();
        if (klass != null && klass.isAbstract()) {
          return true;
        }
      }
    }
    return false;
  }

  private static final Map<IElementType, TextAttributesKey> COLOR_MAP = new HashMap<>();

  static {
    // elements that control search flow
    COLOR_MAP.put(GosuElementTypes.ELEM_TYPE_StatementList, GosuHighlighterColors.DEFAULT_ATTRKEY); // short circuit
    COLOR_MAP.put(GosuElementTypes.ELEM_TYPE_NamespaceStatement, GosuHighlighterColors.DEFAULT_ATTRKEY); // todo use package qualifier?
    COLOR_MAP.put(GosuElementTypes.ELEM_TYPE_NewExpression, GosuHighlighterColors.DEFAULT_ATTRKEY);

    // elements with real data
    COLOR_MAP.put(GosuElementTypes.ELEM_TYPE_ClassDeclaration, GosuHighlighterColors.CLASS_NAME_ATTRKEY);
    COLOR_MAP.put(GosuElementTypes.CLASS_DEFINITION, GosuHighlighterColors.CLASS_NAME_ATTRKEY);
    COLOR_MAP.put(GosuElementTypes.INTERFACE_DEFINITION, GosuHighlighterColors.INTERFACE_NAME_ATTRKEY);
    COLOR_MAP.put(GosuElementTypes.ENUM_DEFINITION, GosuHighlighterColors.ENUM_NAME_ATTRKEY);
    COLOR_MAP.put(GosuElementTypes.FIELD, GosuHighlighterColors.INSTANCE_FIELD_ATTRKEY);
    COLOR_MAP.put(GosuElementTypes.ENUM_CONSTANT, GosuHighlighterColors.STATIC_FIELD_ATTRKEY);
    COLOR_MAP.put(GosuElementTypes.ELEM_TYPE_VarStatement, GosuHighlighterColors.LOCAL_VARIABLE_ATTRKEY);
    COLOR_MAP.put(GosuElementTypes.ELEM_TYPE_LocalVarDeclaration, GosuHighlighterColors.LOCAL_VARIABLE_ATTRKEY);
    COLOR_MAP.put(GosuElementTypes.ELEM_TYPE_ParameterDeclaration, GosuHighlighterColors.PARAMETER_ATTRKEY);
    COLOR_MAP.put(GosuElementTypes.METHOD_DEFINITION, GosuHighlighterColors.METHOD_DECLARATION_ATTRKEY);
    COLOR_MAP.put(GosuElementTypes.ELEM_TYPE_MethodCallExpression, GosuHighlighterColors.METHOD_CALL_ATTRKEY);
    COLOR_MAP.put(GosuElementTypes.ELEM_TYPE_TypeVariableDefinitionExpression, GosuHighlighterColors.TYPE_VARIABLE_ATTRKEY);

    // !PW Argh, this is awful!  Need keyword/operator/brace flag on GosuElementType
    // keywords and symbols
    COLOR_MAP.put(GosuElementTypes.TT_true, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_false, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_NaN, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_Infinity, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_and, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_or, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_not, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_null, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_length, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_exists, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_in, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_out, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_startswith, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_contains, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_where, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_find, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_var, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_delegate, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_represents, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_as, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_typeof, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_statictypeof, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_typeis, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_typeas, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_print, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_package, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_uses, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_if, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_else, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_except, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_unless, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_foreach, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_for, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_index, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_while, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_do, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_continue, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_break, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_return, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_construct, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_function, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_property, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_get, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_set, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_try, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_catch, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_finally, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_this, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_throw, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_assert, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_new, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_switch, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_case, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_default, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_eval, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_private, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_internal, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_protected, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_public, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_abstract, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_override, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_hide, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_final, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_static, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_extends, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_transient, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_implements, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_readonly, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_class, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_interface, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_annotation, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_structure, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_enum, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_super, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_outer, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_execution, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_request, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_session, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_application, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_void, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_block, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_enhancement, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_classpath, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_typeloader, GosuHighlighterColors.KEYWORD);
    COLOR_MAP.put(GosuElementTypes.TT_using, GosuHighlighterColors.KEYWORD);

    //
    // Operators
    //
    COLOR_MAP.put(GosuElementTypes.TT_OP_assign, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_greater, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_less, GosuHighlighterColors.OPERATOR);

    COLOR_MAP.put(GosuElementTypes.TT_OP_not_logical, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_not_bitwise, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_question, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_colon, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_ternary, GosuHighlighterColors.OPERATOR);

    COLOR_MAP.put(GosuElementTypes.TT_OP_equals, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_less_equals, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_not_equals, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_not_equals_for_losers, GosuHighlighterColors.OPERATOR);

    COLOR_MAP.put(GosuElementTypes.TT_OP_logical_and, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_logical_or, GosuHighlighterColors.OPERATOR);

    COLOR_MAP.put(GosuElementTypes.TT_OP_increment, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_decrement, GosuHighlighterColors.OPERATOR);

    COLOR_MAP.put(GosuElementTypes.TT_OP_identity, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_not_identity, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_expansion, GosuHighlighterColors.OPERATOR);

    // Arithmetic operators
    COLOR_MAP.put(GosuElementTypes.TT_OP_plus, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_minus, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_multiply, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_divide, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_modulo, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_bitwise_and, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_bitwise_or, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_bitwise_xor, GosuHighlighterColors.OPERATOR);

    // Null-safe arithmetic operators
    COLOR_MAP.put(GosuElementTypes.TT_OP_nullsafe_plus, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_nullsafe_minus, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_nullsafe_multiply, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_nullsafe_divide, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_nullsafe_modulo, GosuHighlighterColors.OPERATOR);

    // Unchecked overflow arithmetic operators for integers
    COLOR_MAP.put(GosuElementTypes.TT_OP_unchecked_plus, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_unchecked_minus, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_unchecked_multiply, GosuHighlighterColors.OPERATOR);

    // Compound operators
    COLOR_MAP.put(GosuElementTypes.TT_OP_assign_plus, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_assign_minus, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_assign_multiply, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_assign_divide, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_assign_modulo, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_assign_and, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_assign_logical_and, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_assing_or, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_assing_logical_or, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_assign_xor, GosuHighlighterColors.OPERATOR);

    // Block operators
    COLOR_MAP.put(GosuElementTypes.TT_OP_escape, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_assign_closure, GosuHighlighterColors.OPERATOR);

    // Member-access operators
    COLOR_MAP.put(GosuElementTypes.TT_OP_dot, GosuHighlighterColors.DOT);
    COLOR_MAP.put(GosuElementTypes.TT_OP_nullsafe_dot, GosuHighlighterColors.DOT);

    // Null-safe array access
    COLOR_MAP.put(GosuElementTypes.TT_OP_nullsafe_array_access, GosuHighlighterColors.OPERATOR);

    // Interval operators
    COLOR_MAP.put(GosuElementTypes.TT_OP_interval, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_interval_left_open, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_interval_right_open, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_interval_open, GosuHighlighterColors.OPERATOR);

    // Feature Literals
    COLOR_MAP.put(GosuElementTypes.TT_OP_feature_access, GosuHighlighterColors.OPERATOR);

    COLOR_MAP.put(GosuElementTypes.TT_OP_shift_left, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_shift_right, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_shift_right_unsigned, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_assign_shift_left, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_assign_shift_right, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_assign_shift_right_unsigned, GosuHighlighterColors.OPERATOR);

    // Delimiters
    COLOR_MAP.put(GosuElementTypes.TT_OP_brace_left, GosuHighlighterColors.BRACES);
    COLOR_MAP.put(GosuElementTypes.TT_OP_brace_right, GosuHighlighterColors.BRACES);
    COLOR_MAP.put(GosuElementTypes.TT_OP_paren_left, GosuHighlighterColors.PARENTHS);
    COLOR_MAP.put(GosuElementTypes.TT_OP_paren_right, GosuHighlighterColors.PARENTHS);
    COLOR_MAP.put(GosuElementTypes.TT_OP_bracket_left, GosuHighlighterColors.BRACKETS);
    COLOR_MAP.put(GosuElementTypes.TT_OP_bracket_right, GosuHighlighterColors.BRACKETS);

    COLOR_MAP.put(GosuElementTypes.TT_OP_quote_double, GosuHighlighterColors.OPERATOR);
    COLOR_MAP.put(GosuElementTypes.TT_OP_quote_single, GosuHighlighterColors.OPERATOR);

    // Separators
    COLOR_MAP.put(GosuElementTypes.TT_OP_at, GosuHighlighterColors.ANNOTATION_NAME_ATTRKEY);
    COLOR_MAP.put(GosuElementTypes.TT_OP_dollar, GosuHighlighterColors.OPERATOR);

    COLOR_MAP.put(GosuElementTypes.TT_OP_comma, GosuHighlighterColors.COMMA);
    COLOR_MAP.put(GosuElementTypes.TT_OP_semicolon, GosuHighlighterColors.SEMICOLON);
  }

  // Lookup via referenced elements vs direct.  Segregated if we ever want distinct colors for references (ala Eclipse)
  private static final Map<IElementType, TextAttributesKey> COLOR_FROM_REF_MAP = new HashMap<>();

  static {
    // types
    COLOR_FROM_REF_MAP.put(GosuElementTypes.CLASS_DEFINITION, GosuHighlighterColors.CLASS_NAME_ATTRKEY);
    COLOR_FROM_REF_MAP.put(JavaStubElementTypes.CLASS, GosuHighlighterColors.CLASS_NAME_ATTRKEY);
    COLOR_FROM_REF_MAP.put(GosuElementTypes.INTERFACE_DEFINITION, GosuHighlighterColors.INTERFACE_NAME_ATTRKEY);
    COLOR_FROM_REF_MAP.put(GosuElementTypes.ENUM_DEFINITION, GosuHighlighterColors.ENUM_NAME_ATTRKEY);

    // type variables
    COLOR_FROM_REF_MAP.put(GosuElementTypes.ELEM_TYPE_TypeVariableDefinitionExpression, GosuHighlighterColors.TYPE_VARIABLE_ATTRKEY);
    // methods
    COLOR_FROM_REF_MAP.put(GosuElementTypes.METHOD_DEFINITION, GosuHighlighterColors.METHOD_CALL_ATTRKEY);
    COLOR_FROM_REF_MAP.put(JavaStubElementTypes.METHOD, GosuHighlighterColors.METHOD_CALL_ATTRKEY);
    // parameters
    COLOR_FROM_REF_MAP.put(GosuElementTypes.ELEM_TYPE_ParameterDeclaration, GosuHighlighterColors.PARAMETER_ATTRKEY);
    COLOR_FROM_REF_MAP.put(JavaStubElementTypes.PARAMETER, GosuHighlighterColors.PARAMETER_ATTRKEY);
    // local variables
    COLOR_FROM_REF_MAP.put(GosuElementTypes.ELEM_TYPE_VarStatement, GosuHighlighterColors.LOCAL_VARIABLE_ATTRKEY);
    COLOR_FROM_REF_MAP.put(GosuElementTypes.ELEM_TYPE_LocalVarDeclaration, GosuHighlighterColors.LOCAL_VARIABLE_ATTRKEY);
    // fields
    COLOR_FROM_REF_MAP.put(GosuElementTypes.FIELD, GosuHighlighterColors.INSTANCE_FIELD_ATTRKEY);
    COLOR_FROM_REF_MAP.put(JavaStubElementTypes.FIELD, GosuHighlighterColors.INSTANCE_FIELD_ATTRKEY);
    // static fields
    COLOR_FROM_REF_MAP.put(GosuElementTypes.ENUM_CONSTANT, GosuHighlighterColors.STATIC_FIELD_ATTRKEY);
    COLOR_FROM_REF_MAP.put(JavaStubElementTypes.ENUM_CONSTANT, GosuHighlighterColors.STATIC_FIELD_ATTRKEY);
    // properties
  }

  private static final Map<IElementType, TextAttributesKey> COLOR_FOR_REF_MAP = Maps.newHashMap();

  static {
    COLOR_FOR_REF_MAP.put(GosuElementTypes.ELEM_TYPE_ParameterDeclaration, GosuHighlighterColors.PARAMETER_ATTRKEY);
    COLOR_FOR_REF_MAP.put(GosuElementTypes.ELEM_TYPE_VarStatement, GosuHighlighterColors.LOCAL_VARIABLE_ATTRKEY);
  }

  // TODO: use Keyword class here (or Guava immutable set)
  private static final Set<String> GOSU_KEYWORDS = ImmutableSet.of(
      "abstract", "and", "application", "as", "block",
      "break", "case", "catch", "class", "classpath", "construct", "contains", "continue",
      "default", "delegate", "do", "else", "enhancement", "enum", "eval", "except", "execution",
      "exists", "extends", "false", "final", "finally", "find", "for", "foreach", "function",
      "get", "hide", "if", "implements", "in", "out", "index", "interface", "internal", "length", "new",
      "not", "null", "or", "outer", "override", "package", "private", "property", "protected",
      "public", "readonly", "represents", "request",
      "return",
      "session", "set", "startswith", "static", "statictypeof", "super", "switch", "this", "throw",
      "transient", "true", "try", "typeas", "typeis", "typeof", "unless", "uses", "using", "var",
      "void", "where", "while", "Infinity", "NaN");

  private static final Set<String> NATIVE_TYPES = ImmutableSet.of(
      "boolean", "byte", "char", "double", "float",
      "int", "long", "short", "strictfp", "void");

  private static final Set<String> NATIVE_CONSTANTS = ImmutableSet.of(
      "true", "false", "null");
}

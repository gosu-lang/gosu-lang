/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.highlighter;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

public class GosuHighlighterColors {
  public static final TextAttributesKey WORD = DefaultLanguageHighlighterColors.IDENTIFIER;

  // Syntactic colors inferred from java
  public static final TextAttributesKey LINE_COMMENT = DefaultLanguageHighlighterColors.LINE_COMMENT;
  public static final TextAttributesKey BLOCK_COMMENT = DefaultLanguageHighlighterColors.BLOCK_COMMENT;
  public static final TextAttributesKey DOC_COMMENT_CONTENT = DefaultLanguageHighlighterColors.DOC_COMMENT;
  public static final TextAttributesKey DOC_COMMENT_TAG = DefaultLanguageHighlighterColors.DOC_COMMENT_TAG;
  public static final TextAttributesKey KEYWORD = DefaultLanguageHighlighterColors.KEYWORD;
  public static final TextAttributesKey NUMBER = DefaultLanguageHighlighterColors.NUMBER;
  public static final TextAttributesKey STRING = DefaultLanguageHighlighterColors.STRING;
  public static final TextAttributesKey PARENTHS = DefaultLanguageHighlighterColors.PARENTHESES;
  public static final TextAttributesKey BRACKETS = DefaultLanguageHighlighterColors.BRACKETS;
  public static final TextAttributesKey BRACES = DefaultLanguageHighlighterColors.BRACES;
  public static final TextAttributesKey OPERATOR = DefaultLanguageHighlighterColors.OPERATION_SIGN;
  public static final TextAttributesKey COMMA = DefaultLanguageHighlighterColors.COMMA;
  public static final TextAttributesKey SEMICOLON = DefaultLanguageHighlighterColors.SEMICOLON;
  public static final TextAttributesKey DOT = DefaultLanguageHighlighterColors.DOT;
  public static final TextAttributesKey BAD_CHARACTER = CodeInsightColors.UNMATCHED_BRACE_ATTRIBUTES;

  // Semantic colors inferred from Java
  public static final TextAttributesKey LOCAL_VARIABLE_ATTRKEY = CodeInsightColors.LOCAL_VARIABLE_ATTRIBUTES;
  public static final TextAttributesKey REASSIGNED_LOCAL_VARIABLE_ATTRKEY = CodeInsightColors.REASSIGNED_LOCAL_VARIABLE_ATTRIBUTES;
  public static final TextAttributesKey REASSIGNED_PARAMETER_ATTRKEY = CodeInsightColors.REASSIGNED_PARAMETER_ATTRIBUTES;
  public static final TextAttributesKey IMPLICIT_ANONYMOUS_PARAMETER_ATTRKEY = CodeInsightColors.IMPLICIT_ANONYMOUS_CLASS_PARAMETER_ATTRIBUTES;
  public static final TextAttributesKey INSTANCE_FIELD_ATTRKEY = CodeInsightColors.INSTANCE_FIELD_ATTRIBUTES;
  public static final TextAttributesKey STATIC_FIELD_ATTRKEY = CodeInsightColors.STATIC_FIELD_ATTRIBUTES;
  public static final TextAttributesKey PARAMETER_NAME_ATTRKEY = CodeInsightColors.TYPE_PARAMETER_NAME_ATTRIBUTES;
  public static final TextAttributesKey PARAMETER_ATTRKEY = CodeInsightColors.PARAMETER_ATTRIBUTES;
  public static final TextAttributesKey CLASS_NAME_ATTRKEY = CodeInsightColors.CLASS_NAME_ATTRIBUTES;
  public static final TextAttributesKey INTERFACE_NAME_ATTRKEY = CodeInsightColors.INTERFACE_NAME_ATTRIBUTES;
  public static final TextAttributesKey ABSTRACT_CLASS_NAME_ATTRKEY = CodeInsightColors.ABSTRACT_CLASS_NAME_ATTRIBUTES;
  public static final TextAttributesKey TYPE_VARIABLE_ATTRKEY = CodeInsightColors.TYPE_PARAMETER_NAME_ATTRIBUTES;
  public static final TextAttributesKey METHOD_CALL_ATTRKEY = CodeInsightColors.METHOD_CALL_ATTRIBUTES;
  public static final TextAttributesKey METHOD_DECLARATION_ATTRKEY = CodeInsightColors.METHOD_DECLARATION_ATTRIBUTES;
  public static final TextAttributesKey STATIC_METHOD_ATTRKEY = CodeInsightColors.STATIC_METHOD_ATTRIBUTES;
  public static final TextAttributesKey CONSTRUCTOR_CALL_ATTRKEY = CodeInsightColors.CONSTRUCTOR_CALL_ATTRIBUTES;
  public static final TextAttributesKey CONSTRUCTOR_DECLARATION_ATTRKEY = CodeInsightColors.CONSTRUCTOR_DECLARATION_ATTRIBUTES;
  public static final TextAttributesKey ANNOTATION_NAME_ATTRKEY = CodeInsightColors.ANNOTATION_NAME_ATTRIBUTES;
  public static final TextAttributesKey ANNOTATION_ATTRIBUTE_NAME_ATTRKEY = CodeInsightColors.ANNOTATION_ATTRIBUTE_NAME_ATTRIBUTES;
  public static final TextAttributesKey ANNOTATION_ATTRIBUTE_VALUE_ATTRKEY = CodeInsightColors.ANNOTATION_ATTRIBUTE_VALUE_ATTRIBUTES;

  public static final TextAttributesKey DEFAULT_ATTRKEY = new TextAttributesKey();
  public static final TextAttributesKey ENUM_NAME_ATTRKEY = CLASS_NAME_ATTRKEY;
  public static final TextAttributesKey PACKAGE_QUALIFIER_ATTRKEY = CLASS_NAME_ATTRKEY;
  public static final TextAttributesKey UNHANDLED_ATTRKEY = CodeInsightColors.PARAMETER_ATTRIBUTES;

  private GosuHighlighterColors() {
  }
}
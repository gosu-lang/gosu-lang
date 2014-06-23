/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.highlighter;

import com.google.common.collect.Maps;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.pom.java.LanguageLevel;
import com.intellij.psi.JavaTokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.xml.XmlTokenType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class GosuCodeFileHighlighter extends SyntaxHighlighterBase {
  private static final Map<IElementType, TextAttributesKey> ourMap1 = Maps.newHashMap();
  private static final Map<IElementType, TextAttributesKey> ourMap2 = Maps.newHashMap();

  static {
    fillMap(ourMap1, JavaTokenType.KEYWORD_BIT_SET, GosuHighlighterColors.KEYWORD);
    fillMap(ourMap1, JavaTokenType.OPERATION_BIT_SET, GosuHighlighterColors.OPERATOR);

//    for(IElementType type : JavaDocTokenType.ALL_JAVADOC_TOKENS.getTypes()) {
//      ourMap1.put( type, SyntaxHighlighterColors.DOC_COMMENT );
//    }

    ourMap1.put(XmlTokenType.TAG_WHITE_SPACE, GosuHighlighterColors.DOC_COMMENT_CONTENT);
    ourMap1.put(JavaTokenType.IDENTIFIER, GosuHighlighterColors.WORD);
    ourMap1.put(JavaTokenType.WHITE_SPACE, GosuHighlighterColors.WORD);
    ourMap1.put(JavaTokenType.INTEGER_LITERAL, GosuHighlighterColors.NUMBER);
    ourMap1.put(JavaTokenType.LONG_LITERAL, GosuHighlighterColors.NUMBER);
    ourMap1.put(JavaTokenType.FLOAT_LITERAL, GosuHighlighterColors.NUMBER);
    ourMap1.put(JavaTokenType.DOUBLE_LITERAL, GosuHighlighterColors.NUMBER);
    ourMap1.put(JavaTokenType.STRING_LITERAL, GosuHighlighterColors.STRING);
    ourMap1.put(JavaTokenType.CHARACTER_LITERAL, GosuHighlighterColors.STRING);
    ourMap1.put(JavaTokenType.LPARENTH, GosuHighlighterColors.BRACES);
    ourMap1.put(JavaTokenType.RPARENTH, GosuHighlighterColors.BRACES);

    ourMap1.put(JavaTokenType.LBRACE, GosuHighlighterColors.BRACES);
    ourMap1.put(JavaTokenType.RBRACE, GosuHighlighterColors.BRACES);

    ourMap1.put(JavaTokenType.LBRACKET, GosuHighlighterColors.BRACES);
    ourMap1.put(JavaTokenType.RBRACKET, GosuHighlighterColors.BRACES);

    ourMap1.put(JavaTokenType.COMMA, GosuHighlighterColors.OPERATOR);
    ourMap1.put(JavaTokenType.DOT, GosuHighlighterColors.OPERATOR);
    ourMap1.put(JavaTokenType.SEMICOLON, GosuHighlighterColors.OPERATOR);

    //ourMap1[JavaTokenType.BOOLEAN_LITERAL] = HighlighterColors.JAVA_KEYWORD;
    //ourMap1[JavaTokenType.NULL_LITERAL] = HighlighterColors.JAVA_KEYWORD;
    ourMap1.put(JavaTokenType.C_STYLE_COMMENT, GosuHighlighterColors.BLOCK_COMMENT);
//    ourMap1.put( JavaTokenType.DOC_COMMENT, GosuHighlighterColors.DOC_COMMENT_CONTENT );
    ourMap1.put(JavaTokenType.END_OF_LINE_COMMENT, GosuHighlighterColors.LINE_COMMENT);
    ourMap1.put(JavaTokenType.BAD_CHARACTER, HighlighterColors.BAD_CHARACTER);

//    ourMap1.put( JavaDocTokenType.DOC_TAG_NAME, GosuHighlighterColors.DOC_COMMENT_CONTENT );
//    ourMap2.put( JavaDocTokenType.DOC_TAG_NAME, GosuHighlighterColors.DOC_COMMENT_TAG );

//    IElementType[] javaDocMarkup = new IElementType[]{XmlTokenType.XML_START_TAG_START,
//      XmlTokenType.XML_END_TAG_START,
//      XmlTokenType.XML_TAG_END,
//      XmlTokenType.XML_EMPTY_ELEMENT_END,
//      XmlTokenType.TAG_WHITE_SPACE,
//      XmlTokenType.XML_TAG_NAME,
//      XmlTokenType.XML_NAME,
//      XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN,
//      XmlTokenType.XML_ATTRIBUTE_VALUE_START_DELIMITER,
//      XmlTokenType.XML_ATTRIBUTE_VALUE_END_DELIMITER,
//      XmlTokenType.XML_CHAR_ENTITY_REF,
//      XmlTokenType.XML_EQ};

//    for( IElementType idx : javaDocMarkup )
//    {
//      ourMap1.put( idx, GosuHighlighterColors.DOC_COMMENT_CONTENT );
//      ourMap2.put( idx, GosuHighlighterColors.DOC_COMMENT_TAG );
//    }
  }

  private final LanguageLevel _languageLevel;

  public GosuCodeFileHighlighter() {
    this(LanguageLevel.HIGHEST);
  }

  public GosuCodeFileHighlighter(LanguageLevel languageLevel) {
    _languageLevel = languageLevel;
  }

  @NotNull
  public Lexer getHighlightingLexer() {
    return new GosuHighlightingLexer(_languageLevel);
  }

  @NotNull
  public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
    return pack(ourMap1.get(tokenType), ourMap2.get(tokenType));
  }

  public LanguageLevel getLanguageLevel() {
    return _languageLevel;
  }
}
/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.highlighter;

import com.google.common.collect.ImmutableMap;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import gw.plugin.ij.icons.GosuIcons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class GosuColorsAndFontsPage implements ColorSettingsPage {
  private static final AttributesDescriptor[] ATTRS = new AttributesDescriptor[] {
      new AttributesDescriptor("Line Comment", GosuHighlighterColors.LINE_COMMENT),
      new AttributesDescriptor("Block Comment", GosuHighlighterColors.BLOCK_COMMENT),
      new AttributesDescriptor("Doc Comment", GosuHighlighterColors.DOC_COMMENT_CONTENT),
      new AttributesDescriptor("Doc Comment Tag", GosuHighlighterColors.DOC_COMMENT_TAG),
      new AttributesDescriptor("Word", GosuHighlighterColors.WORD),
      new AttributesDescriptor("Keyword", GosuHighlighterColors.KEYWORD),
      new AttributesDescriptor("Number", GosuHighlighterColors.NUMBER),
      new AttributesDescriptor("String", GosuHighlighterColors.STRING),
      new AttributesDescriptor("Braces", GosuHighlighterColors.BRACES),
      new AttributesDescriptor("Operator", GosuHighlighterColors.OPERATOR),
      new AttributesDescriptor("Character", GosuHighlighterColors.BAD_CHARACTER)};

  @NotNull
  public String getDisplayName() {
    return "Gosu";
  }

  @Nullable
  public Icon getIcon() {
    return GosuIcons.CLASS;
  }

  @NotNull
  public AttributesDescriptor[] getAttributeDescriptors() {
    return ATTRS;
  }

  @NotNull
  public ColorDescriptor[] getColorDescriptors() {
    return new ColorDescriptor[0];
  }

  @NotNull
  public SyntaxHighlighter getHighlighter() {
    return new GosuCodeFileHighlighter();
  }

  @NonNls
  @NotNull
  public String getDemoText() {
    return "uses javax.swing.JPanel\n" +
        "/**\n" +
        " * This is GosuDoc comment\n" +
        " * @see java.lang.String#equals\n" +
        " */\n" +
        "class Demo {\n" +
        "  var _foo : int //This is a line comment\n" +
        "  /* This is a block comment */\n" +
        "  static function foo( i: int ) : int { return _foo }\n" +
        "  static var _panel = new JPanel()\n" +
        "}\n" +
        "\n" +
        "Demo.panel.size = 88\n" +
        "Demo.foo( \"123${456}789\".toInteger() ) \n" +
        "var x = 1 + unresolved"
        ;
  }

  @Nullable
  public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
    return ImmutableMap.of(
        "gosudoc", GosuHighlighterColors.DOC_COMMENT_CONTENT,
        "doctag", GosuHighlighterColors.DOC_COMMENT_TAG);
  }
}

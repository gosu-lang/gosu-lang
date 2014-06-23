/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.application.options.IndentOptionsEditor;
import com.intellij.application.options.SmartIndentOptionsEditor;
import com.intellij.lang.Language;
import com.intellij.openapi.application.ApplicationBundle;
import com.intellij.openapi.project.Project;
import com.intellij.pom.java.LanguageLevel;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.LocalTimeCounter;
import gw.plugin.ij.filetypes.GosuCodeFileType;
import gw.plugin.ij.formatting.samples.GosuCodeSamples;
import gw.plugin.ij.lang.GosuLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.intellij.openapi.application.ApplicationBundle.*;

public class GosuLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {
  @NotNull
  @Override
  public Language getLanguage() {
    return GosuLanguage.instance();
  }

  public boolean usesSharedPreview() {
    return false;
  }

  @Override
  public CommonCodeStyleSettings getDefaultCommonSettings() {
    final CommonCodeStyleSettings settings = new CommonCodeStyleSettings(GosuLanguage.instance());
    final CodeStyleSettings.IndentOptions options = settings.initIndentOptions();
    options.INDENT_SIZE = 2;
    options.TAB_SIZE = 2;
    options.USE_TAB_CHARACTER = false;
    options.CONTINUATION_INDENT_SIZE = 4;

    return settings;
  }

  @Nullable
  @Override
  public String getCodeSample(@NotNull SettingsType settingsType) {
    switch (settingsType) {
      case INDENT_SETTINGS:
      case SPACING_SETTINGS:
        return GosuCodeSamples.SPACING_SAMPLE;
      case WRAPPING_AND_BRACES_SETTINGS:
        return GosuCodeSamples.WRAPPING_AND_BRACES_SAMPLE;
      case BLANK_LINES_SETTINGS:
        return GosuCodeSamples.BLANK_LINES_SAMPLE;
    }

    return null;
  }

  @Override
  public int getRightMargin(@NotNull SettingsType settingsType) {
    if (settingsType == SettingsType.WRAPPING_AND_BRACES_SETTINGS) {
      return 37;
    }
    return super.getRightMargin(settingsType);
  }

  protected void customizeSpacingSettigs(@NotNull CodeStyleSettingsCustomizable consumer) {
    consumer.showStandardOptions(
        // SPACES_BEFORE_PARENTHESES
        "SPACE_BEFORE_METHOD_PARENTHESES",
        "SPACE_BEFORE_METHOD_CALL_PARENTHESES",
        "SPACE_BEFORE_IF_PARENTHESES",
        "SPACE_BEFORE_FOR_PARENTHESES",
        "SPACE_BEFORE_WHILE_PARENTHESES",
        "SPACE_BEFORE_SWITCH_PARENTHESES",
        //"SPACE_BEFORE_TRY_PARENTHESES",
        "SPACE_BEFORE_CATCH_PARENTHESES",
        //"SPACE_BEFORE_SYNCHRONIZED_PARENTHESES",
        "SPACE_BEFORE_ANOTATION_PARAMETER_LIST",

        // SPACES_AROUND_OPERATORS
        "SPACE_AROUND_ASSIGNMENT_OPERATORS",
        "SPACE_AROUND_LOGICAL_OPERATORS",
        "SPACE_AROUND_EQUALITY_OPERATORS",
        "SPACE_AROUND_RELATIONAL_OPERATORS",
        "SPACE_AROUND_BITWISE_OPERATORS",
        "SPACE_AROUND_ADDITIVE_OPERATORS",
        "SPACE_AROUND_MULTIPLICATIVE_OPERATORS",
        "SPACE_AROUND_SHIFT_OPERATORS",
        "SPACE_AROUND_UNARY_OPERATOR",

        // SPACES_BEFORE_LEFT_BRACE
        "SPACE_BEFORE_CLASS_LBRACE",
        "SPACE_BEFORE_METHOD_LBRACE",
        "SPACE_BEFORE_IF_LBRACE",
        "SPACE_BEFORE_ELSE_LBRACE",
        "SPACE_BEFORE_FOR_LBRACE",
        "SPACE_BEFORE_WHILE_LBRACE",
        "SPACE_BEFORE_DO_LBRACE",
        "SPACE_BEFORE_SWITCH_LBRACE",
        "SPACE_BEFORE_TRY_LBRACE",
        "SPACE_BEFORE_CATCH_LBRACE",
        "SPACE_BEFORE_FINALLY_LBRACE",
        //"SPACE_BEFORE_SYNCHRONIZED_LBRACE",
        "SPACE_BEFORE_ARRAY_INITIALIZER_LBRACE",
        "SPACE_BEFORE_ANNOTATION_ARRAY_INITIALIZER_LBRACE",

        // SPACES_BEFORE_KEYWORD
        "SPACE_BEFORE_ELSE_KEYWORD",
        "SPACE_BEFORE_WHILE_KEYWORD",
        "SPACE_BEFORE_CATCH_KEYWORD",
        "SPACE_BEFORE_FINALLY_KEYWORD",

        // SPACES_WITHIN
        "SPACE_WITHIN_BRACES",
        "SPACE_WITHIN_BRACKETS",
        "SPACE_WITHIN_ARRAY_INITIALIZER_BRACES",
        "SPACE_WITHIN_PARENTHESES",
        "SPACE_WITHIN_METHOD_CALL_PARENTHESES",
        "SPACE_WITHIN_METHOD_PARENTHESES",
        "SPACE_WITHIN_IF_PARENTHESES",
        "SPACE_WITHIN_FOR_PARENTHESES",
        "SPACE_WITHIN_WHILE_PARENTHESES",
        "SPACE_WITHIN_SWITCH_PARENTHESES",
        //"SPACE_WITHIN_TRY_PARENTHESES",
        "SPACE_WITHIN_CATCH_PARENTHESES",
        //"SPACE_WITHIN_SYNCHRONIZED_PARENTHESES",
        "SPACE_WITHIN_CAST_PARENTHESES",
        "SPACE_WITHIN_ANNOTATION_PARENTHESES",

        // SPACES_IN_TERNARY_OPERATOR
        "SPACE_BEFORE_QUEST",
        "SPACE_AFTER_QUEST",
        "SPACE_BEFORE_COLON",
        "SPACE_AFTER_COLON",

        // SPACES_WITHIN_TYPE_ARGUMENTS
        "SPACE_AFTER_COMMA_IN_TYPE_ARGUMENTS",

        // SPACES_OTHER
        "SPACE_BEFORE_COMMA",
        "SPACE_AFTER_COMMA",
        //"SPACE_BEFORE_SEMICOLON",
        //"SPACE_AFTER_SEMICOLON",
        "SPACE_AFTER_TYPE_CAST"
    );

    // Around operators
    consumer.showCustomOption(GosuCodeStyleSettings.class,
        "SPACE_AROUND_INTERVAL_OPERATORS",
        "Interval operators (.., |.., ..|, |..|)",
        CodeStyleSettingsCustomizable.SPACES_AROUND_OPERATORS);

    // Before parentheses
    consumer.showCustomOption(GosuCodeStyleSettings.class,
        "SPACE_BEFORE_TYPEOF_PARENTHESES",
        "'typeof' parentheses",
        CodeStyleSettingsCustomizable.SPACES_BEFORE_PARENTHESES);

    consumer.showCustomOption(GosuCodeStyleSettings.class,
        "SPACE_BEFORE_USING_PARENTHESES",
        "'using' parentheses",
        CodeStyleSettingsCustomizable.SPACES_BEFORE_PARENTHESES);

    // Withing parentheses
    consumer.showCustomOption(GosuCodeStyleSettings.class,
        "SPACE_WITHIN_TYPEOF_PARENTHESES",
        "'typeof' parentheses",
        CodeStyleSettingsCustomizable.SPACES_WITHIN);


    // Before left brace
    consumer.showCustomOption(GosuCodeStyleSettings.class,
        "SPACE_BEFORE_USING_LBRACE",
        "'using' left brace",
        CodeStyleSettingsCustomizable.SPACES_BEFORE_LEFT_BRACE);

    // Other
    consumer.showCustomOption(GosuCodeStyleSettings.class,
        "SPACE_BEFORE_COLON",
        "Before colon",
        CodeStyleSettingsCustomizable.SPACES_OTHER);

    consumer.showCustomOption(GosuCodeStyleSettings.class,
        "SPACE_AFTER_COLON",
        "After colon",
        CodeStyleSettingsCustomizable.SPACES_OTHER);

    // Blocks
    consumer.showCustomOption(GosuCodeStyleSettings.class,
        "SPACE_AFTER_LAMBDA",
        "Blocks: After \\",
        CodeStyleSettingsCustomizable.SPACES_OTHER);

    consumer.showCustomOption(GosuCodeStyleSettings.class,
        "SPACE_AROUND_ARROW",
        "Blocks: Around ->",
        CodeStyleSettingsCustomizable.SPACES_OTHER);
  }

  protected void customizeWrappingAndBracesSettings(@NotNull CodeStyleSettingsCustomizable consumer) {
    consumer.showStandardOptions(
        "KEEP_LINE_BREAKS"
//        "KEEP_FIRST_COLUMN_COMMENT",
//        "KEEP_CONTROL_STATEMENT_IN_ONE_LINE",
//        "KEEP_MULTIPLE_EXPRESSIONS_IN_ONE_LINE"
//        "KEEP_SIMPLE_BLOCKS_IN_ONE_LINE",
//        "KEEP_SIMPLE_METHODS_IN_ONE_LINE",
//        "KEEP_SIMPLE_CLASSES_IN_ONE_LINE",
//
//        "WRAP_LONG_LINES",
//
//        "CLASS_BRACE_STYLE",
//        "METHOD_BRACE_STYLE",
//        "BRACE_STYLE",
//
//        "EXTENDS_LIST_WRAP",
//        "ALIGN_MULTILINE_EXTENDS_LIST",
//
//        "EXTENDS_KEYWORD_WRAP",
//
//        "THROWS_LIST_WRAP",
//        "ALIGN_MULTILINE_THROWS_LIST",
//        "ALIGN_THROWS_KEYWORD",
//        "THROWS_KEYWORD_WRAP",
//
//        "METHOD_PARAMETERS_WRAP",
//        "ALIGN_MULTILINE_PARAMETERS",
//        "METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE",
//        "METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE",
//
//        "CALL_PARAMETERS_WRAP",
//        "ALIGN_MULTILINE_PARAMETERS_IN_CALLS",
//        "PREFER_PARAMETERS_WRAP",
//        "CALL_PARAMETERS_LPAREN_ON_NEXT_LINE",
//        "CALL_PARAMETERS_RPAREN_ON_NEXT_LINE",
//
//        "ALIGN_MULTILINE_METHOD_BRACKETS",
//
//        "METHOD_CALL_CHAIN_WRAP",
//        "ALIGN_MULTILINE_CHAINED_METHODS",
//
//        "ALIGN_GROUP_FIELD_DECLARATIONS",
//
//        "IF_BRACE_FORCE",
//        "ELSE_ON_NEW_LINE",
//        "SPECIAL_ELSE_IF_TREATMENT",
//
//        "FOR_STATEMENT_WRAP",
//        "ALIGN_MULTILINE_FOR",
//        "FOR_STATEMENT_LPAREN_ON_NEXT_LINE",
//        "FOR_STATEMENT_RPAREN_ON_NEXT_LINE",
//        "FOR_BRACE_FORCE",
//
//        "WHILE_BRACE_FORCE",
//        "DOWHILE_BRACE_FORCE",
//        "WHILE_ON_NEW_LINE",
//
//        "INDENT_CASE_FROM_SWITCH",
//
//        "RESOURCE_LIST_WRAP",
//        "ALIGN_MULTILINE_RESOURCES",
//        "RESOURCE_LIST_LPAREN_ON_NEXT_LINE",
//        "RESOURCE_LIST_RPAREN_ON_NEXT_LINE",
//
//        "CATCH_ON_NEW_LINE",
//        "FINALLY_ON_NEW_LINE",
//
//        "BINARY_OPERATION_WRAP",
//        "ALIGN_MULTILINE_BINARY_OPERATION",
//        "BINARY_OPERATION_SIGN_ON_NEXT_LINE",
//        "ALIGN_MULTILINE_PARENTHESIZED_EXPRESSION",
//        "PARENTHESES_EXPRESSION_LPAREN_WRAP",
//        "PARENTHESES_EXPRESSION_RPAREN_WRAP",
//
//        "ASSIGNMENT_WRAP",
//        "ALIGN_MULTILINE_ASSIGNMENT",
//        "PLACE_ASSIGNMENT_SIGN_ON_NEXT_LINE",
//
//        "TERNARY_OPERATION_WRAP",
//        "ALIGN_MULTILINE_TERNARY_OPERATION",
//        "TERNARY_OPERATION_SIGNS_ON_NEXT_LINE",
//
//        "ARRAY_INITIALIZER_WRAP",
//        "ALIGN_MULTILINE_ARRAY_INITIALIZER_EXPRESSION",
//        "ARRAY_INITIALIZER_LBRACE_ON_NEXT_LINE",
//        "ARRAY_INITIALIZER_RBRACE_ON_NEXT_LINE",
//
//        "MODIFIER_LIST_WRAP",
//
//        "ASSERT_STATEMENT_WRAP",
//        "ASSERT_STATEMENT_COLON_ON_NEXT_LINE",
//
//        "CLASS_ANNOTATION_WRAP",
//        "METHOD_ANNOTATION_WRAP",
//        "FIELD_ANNOTATION_WRAP",
//        "PARAMETER_ANNOTATION_WRAP",
//        "VARIABLE_ANNOTATION_WRAP",
//        "ENUM_CONSTANTS_WRAP"
    );
  }

  protected void customizeBlankLinesSettings(@NotNull CodeStyleSettingsCustomizable consumer) {
    consumer.showAllStandardOptions();
  }

  @Override
  public void customizeSettings(@NotNull CodeStyleSettingsCustomizable consumer, @NotNull SettingsType settingsType) {
    switch (settingsType) {
      case SPACING_SETTINGS:
        customizeSpacingSettigs(consumer);
        break;
      case WRAPPING_AND_BRACES_SETTINGS:
        customizeWrappingAndBracesSettings(consumer);
        break;
      case BLANK_LINES_SETTINGS:
        customizeBlankLinesSettings(consumer);
        break;
    }
  }

  public static final String FORMATTING_FILE_NAME = "a.gs";

  @Override
  public PsiFile createFileFromText(final Project project, @NotNull final String text) {
    final PsiFile file = PsiFileFactory.getInstance(project).createFileFromText(
        FORMATTING_FILE_NAME, GosuCodeFileType.INSTANCE, text, LocalTimeCounter.currentTime(), true, false);
    file.putUserData(PsiUtil.FILE_LANGUAGE_LEVEL_KEY, LanguageLevel.HIGHEST);
    return file;
  }

  @Override
  public IndentOptionsEditor getIndentOptionsEditor() {
    return new SmartIndentOptionsEditor() {
      private JTextField myLabelIndent;
      private JLabel myLabelIndentLabel;

      private JCheckBox myLabelIndentAbsolute;

      protected void addComponents() {
        super.addComponents();

        myLabelIndent = new JTextField(4);
        add(myLabelIndentLabel = new JLabel(message("editbox.indent.label.indent")), myLabelIndent);

        myLabelIndentAbsolute = new JCheckBox(message("checkbox.indent.absolute.label.indent"));
        add(myLabelIndentAbsolute, true);
      }

      public boolean isModified(final CodeStyleSettings settings, @NotNull final CommonCodeStyleSettings.IndentOptions options) {
        boolean isModified = super.isModified(settings, options);

        isModified |= isFieldModified(myLabelIndent, options.LABEL_INDENT_SIZE);
        isModified |= isFieldModified(myLabelIndentAbsolute, options.LABEL_INDENT_ABSOLUTE);

        return isModified;
      }

      public void apply(final CodeStyleSettings settings, @NotNull final CommonCodeStyleSettings.IndentOptions options) {
        super.apply(settings, options);
        options.LABEL_INDENT_SIZE = getFieldValue(myLabelIndent, Integer.MIN_VALUE, options.LABEL_INDENT_SIZE);
        options.LABEL_INDENT_ABSOLUTE = myLabelIndentAbsolute.isSelected();
      }

      public void reset(@NotNull final CodeStyleSettings settings, @NotNull final CommonCodeStyleSettings.IndentOptions options) {
        super.reset(settings, options);
        myLabelIndent.setText(Integer.toString(options.LABEL_INDENT_SIZE));
        myLabelIndentAbsolute.setSelected(options.LABEL_INDENT_ABSOLUTE);
      }

      public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        myLabelIndent.setEnabled(enabled);
        myLabelIndentLabel.setEnabled(enabled);
        myLabelIndentAbsolute.setEnabled(enabled);
      }
    };
  }
}

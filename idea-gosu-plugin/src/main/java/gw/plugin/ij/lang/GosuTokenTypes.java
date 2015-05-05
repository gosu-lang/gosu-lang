/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.IToken;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class GosuTokenTypes {
  private static final Map<String, IElementType> TYPE_BY_TOKEN = Maps.newHashMap();
  private static final Set<GosuElementType> KEYWORDS = Sets.newHashSet();

  public static final GosuElementType TT_IDENTIFIER = addTokenType("_identifier_");

  public static final GosuElementType TT_NUMBER = addTokenType("_literal_");

  public static final GosuElementType TT_COMMENT_MULTILINE = addTokenType("_multiline_comment_");
  public static final GosuElementType TT_COMMENT_LINE = addTokenType("_line_comment_");


  public static final IElementType TT_WHITESPACE = TokenType.WHITE_SPACE;

  public static final GosuElementType TT_DOUBLE_QUOTED_STRING = addTokenType("_double_quoted_string_");
  public static final GosuElementType TT_SINGLE_QUOTED_STRING = addTokenType("_single_quoted_string_");

  //
  // Keywords
  //
  public static final GosuElementType TT_true = addKeyword("true");
  public static final GosuElementType TT_false = addKeyword("false");
  public static final GosuElementType TT_NaN = addKeyword("NaN");
  public static final GosuElementType TT_Infinity = addKeyword("Infinity");
  public static final GosuElementType TT_and = addKeyword("and");
  public static final GosuElementType TT_or = addKeyword("or");
  public static final GosuElementType TT_not = addKeyword("not");
  public static final GosuElementType TT_null = addKeyword("null");
  public static final GosuElementType TT_length = addKeyword("length");
  public static final GosuElementType TT_exists = addKeyword("exists");
  public static final GosuElementType TT_in = addKeyword("in");
  public static final GosuElementType TT_startswith = addKeyword("startswith");
  public static final GosuElementType TT_contains = addKeyword("contains");
  public static final GosuElementType TT_where = addKeyword("where");
  public static final GosuElementType TT_find = addKeyword("find");
  public static final GosuElementType TT_var = addKeyword("var");
  public static final GosuElementType TT_delegate = addKeyword("delegate");
  public static final GosuElementType TT_represents = addKeyword("represents");
  public static final GosuElementType TT_as = addKeyword("as");
  public static final GosuElementType TT_typeof = addKeyword("typeof");
  public static final GosuElementType TT_statictypeof = addKeyword("statictypeof");
  public static final GosuElementType TT_typeis = addKeyword("typeis");
  public static final GosuElementType TT_typeas = addKeyword("typeas");
  public static final GosuElementType TT_print = addKeyword("print");
  public static final GosuElementType TT_package = addKeyword("package");
  public static final GosuElementType TT_uses = addKeyword("uses");
  public static final GosuElementType TT_if = addKeyword("if");
  public static final GosuElementType TT_else = addKeyword("else");
  public static final GosuElementType TT_except = addKeyword("except");
  public static final GosuElementType TT_unless = addKeyword("unless");
  public static final GosuElementType TT_foreach = addKeyword("foreach");
  public static final GosuElementType TT_for = addKeyword("for");
  public static final GosuElementType TT_index = addKeyword("index");
  public static final GosuElementType TT_iterator = addKeyword("iterator");
  public static final GosuElementType TT_while = addKeyword("while");
  public static final GosuElementType TT_do = addKeyword("do");
  public static final GosuElementType TT_continue = addKeyword("continue");
  public static final GosuElementType TT_break = addKeyword("break");
  public static final GosuElementType TT_return = addKeyword("return");
  public static final GosuElementType TT_construct = addKeyword("construct");
  public static final GosuElementType TT_function = addKeyword("function");
  public static final GosuElementType TT_property = addKeyword("property");
  public static final GosuElementType TT_get = addKeyword("get");
  public static final GosuElementType TT_set = addKeyword("set");
  public static final GosuElementType TT_try = addKeyword("try");
  public static final GosuElementType TT_catch = addKeyword("catch");
  public static final GosuElementType TT_finally = addKeyword("finally");
  public static final GosuElementType TT_this = addKeyword("this");
  public static final GosuElementType TT_throw = addKeyword("throw");
  public static final GosuElementType TT_assert = addKeyword("assert");
  public static final GosuElementType TT_new = addKeyword("new");
  public static final GosuElementType TT_switch = addKeyword("switch");
  public static final GosuElementType TT_case = addKeyword("case");
  public static final GosuElementType TT_default = addKeyword("default");
  public static final GosuElementType TT_eval = addKeyword("eval");
  public static final GosuElementType TT_private = addKeyword("private");
  public static final GosuElementType TT_internal = addKeyword("internal");
  public static final GosuElementType TT_protected = addKeyword("protected");
  public static final GosuElementType TT_public = addKeyword("public");
  public static final GosuElementType TT_abstract = addKeyword("abstract");
  public static final GosuElementType TT_override = addKeyword("override");
  public static final GosuElementType TT_hide = addKeyword("hide");
  public static final GosuElementType TT_final = addKeyword("final");
  public static final GosuElementType TT_static = addKeyword("static");
  public static final GosuElementType TT_extends = addKeyword("extends");
  public static final GosuElementType TT_transient = addKeyword("transient");
  public static final GosuElementType TT_implements = addKeyword("implements");
  public static final GosuElementType TT_readonly = addKeyword("readonly");
  public static final GosuElementType TT_class = addKeyword("class");
  public static final GosuElementType TT_interface = addKeyword("interface");
  public static final GosuElementType TT_annotation = addKeyword("annotation");
  public static final GosuElementType TT_structure = addKeyword("structure");
  public static final GosuElementType TT_enum = addKeyword("enum");
  public static final GosuElementType TT_super = addKeyword("super");
  public static final GosuElementType TT_outer = addKeyword("outer");
  public static final GosuElementType TT_execution = addKeyword("execution");
  public static final GosuElementType TT_request = addKeyword("request");
  public static final GosuElementType TT_session = addKeyword("session");
  public static final GosuElementType TT_application = addKeyword("application");
  public static final GosuElementType TT_void = addKeyword("void");
  public static final GosuElementType TT_boolean = addKeyword("boolean");
  public static final GosuElementType TT_char = addKeyword("char");
  public static final GosuElementType TT_byte = addKeyword("byte");
  public static final GosuElementType TT_short = addKeyword("short");
  public static final GosuElementType TT_int = addKeyword("int");
  public static final GosuElementType TT_long = addKeyword("long");
  public static final GosuElementType TT_float = addKeyword("float");
  public static final GosuElementType TT_double = addKeyword("double");
  public static final GosuElementType TT_block = addKeyword("block");
  public static final GosuElementType TT_enhancement = addKeyword("enhancement");
  public static final GosuElementType TT_classpath = addKeyword("classpath");
  public static final GosuElementType TT_typeloader = addKeyword("typeloader");
  public static final GosuElementType TT_using = addKeyword("using");

  //
  // Operators
  //

  public static final GosuElementType TT_OP_assign = addTokenType("=");
  public static final GosuElementType TT_OP_greater = addTokenType(">");
  public static final GosuElementType TT_OP_less = addTokenType("<");

  public static final GosuElementType TT_OP_not_logical = addTokenType("!");
  public static final GosuElementType TT_OP_not_bitwise = addTokenType("~");
  public static final GosuElementType TT_OP_question = addTokenType("?");
  public static final GosuElementType TT_OP_colon = addTokenType(":");
  public static final GosuElementType TT_OP_ternary = addTokenType("?:");

  public static final GosuElementType TT_OP_equals = addTokenType("==");
  public static final GosuElementType TT_OP_less_equals = addTokenType("<=");
  public static final GosuElementType TT_OP_not_equals = addTokenType("!=");
  public static final GosuElementType TT_OP_not_equals_for_losers = addTokenType("<>");

  public static final GosuElementType TT_OP_logical_and = addTokenType("&&");
  public static final GosuElementType TT_OP_logical_or = addTokenType("||");

  public static final GosuElementType TT_OP_increment = addTokenType("++");
  public static final GosuElementType TT_OP_decrement = addTokenType("--");

  public static final GosuElementType TT_OP_identity = addTokenType("===");
  public static final GosuElementType TT_OP_not_identity = addTokenType("!==");
  public static final GosuElementType TT_OP_expansion = addTokenType("*.");

  // Arithmetic operators
  public static final GosuElementType TT_OP_plus = addTokenType("+");
  public static final GosuElementType TT_OP_minus = addTokenType("-");
  public static final GosuElementType TT_OP_multiply = addTokenType("*");
  public static final GosuElementType TT_OP_divide = addTokenType("/");
  public static final GosuElementType TT_OP_modulo = addTokenType("%");
  public static final GosuElementType TT_OP_bitwise_and = addTokenType("&");
  public static final GosuElementType TT_OP_bitwise_or = addTokenType("|");
  public static final GosuElementType TT_OP_bitwise_xor = addTokenType("^");

  // Null-safe arithmetic operators
  public static final GosuElementType TT_OP_nullsafe_plus = addTokenType("?+");
  public static final GosuElementType TT_OP_nullsafe_minus = addTokenType("?-");
  public static final GosuElementType TT_OP_nullsafe_multiply = addTokenType("?*");
  public static final GosuElementType TT_OP_nullsafe_divide = addTokenType("?/");
  public static final GosuElementType TT_OP_nullsafe_modulo = addTokenType("?%");

  // Unchecked overflow arithmetic operators for integers
  public static final GosuElementType TT_OP_unchecked_plus = addTokenType("!+");
  public static final GosuElementType TT_OP_unchecked_minus = addTokenType("!-");
  public static final GosuElementType TT_OP_unchecked_multiply = addTokenType("!*");

  // Compound operators
  public static final GosuElementType TT_OP_assign_plus = addTokenType("+=");
  public static final GosuElementType TT_OP_assign_minus = addTokenType("-=");
  public static final GosuElementType TT_OP_assign_multiply = addTokenType("*=");
  public static final GosuElementType TT_OP_assign_divide = addTokenType("/=");
  public static final GosuElementType TT_OP_assign_modulo = addTokenType("%=");
  public static final GosuElementType TT_OP_assign_and = addTokenType("&=");
  public static final GosuElementType TT_OP_assign_logical_and = addTokenType("&&=");
  public static final GosuElementType TT_OP_assing_or = addTokenType("|=");
  public static final GosuElementType TT_OP_assing_logical_or = addTokenType("||=");
  public static final GosuElementType TT_OP_assign_xor = addTokenType("^=");

  // Block operators
  public static final GosuElementType TT_OP_escape = addTokenType("\\");
  public static final GosuElementType TT_OP_assign_closure = addTokenType("->");

  // Member-access operators
  public static final GosuElementType TT_OP_dot = addTokenType(".");
  public static final GosuElementType TT_OP_nullsafe_dot = addTokenType("?.");

  // Null-safe array access
  public static final GosuElementType TT_OP_nullsafe_array_access = addTokenType("?[");

  // Interval operators
  public static final GosuElementType TT_OP_interval = addTokenType("..");
  public static final GosuElementType TT_OP_interval_left_open = addTokenType("|..");
  public static final GosuElementType TT_OP_interval_right_open = addTokenType("..|");
  public static final GosuElementType TT_OP_interval_open = addTokenType("|..|");

  // Feature Literals
  public static final GosuElementType TT_OP_feature_access = addTokenType("#");

  public static final GosuElementType TT_OP_shift_left = addTokenType("<<");
  public static final GosuElementType TT_OP_shift_right = addTokenType(">>");
  public static final GosuElementType TT_OP_shift_right_unsigned = addTokenType(">>>");
  public static final GosuElementType TT_OP_assign_shift_left = addTokenType("<<=");
  public static final GosuElementType TT_OP_assign_shift_right = addTokenType(">>=");
  public static final GosuElementType TT_OP_assign_shift_right_unsigned = addTokenType(">>>=");

  // Delimiters
  public static final GosuElementType TT_OP_brace_left = addTokenType("{");
  public static final GosuElementType TT_OP_brace_right = addTokenType("}");
  public static final GosuElementType TT_OP_paren_left = addTokenType("(");
  public static final GosuElementType TT_OP_paren_right = addTokenType(")");
  public static final GosuElementType TT_OP_bracket_left = addTokenType("[");
  public static final GosuElementType TT_OP_bracket_right = addTokenType("]");

  public static final GosuElementType TT_OP_quote_double = addTokenType("\"");
  public static final GosuElementType TT_OP_quote_single = addTokenType("'");

  // Separators
  public static final GosuElementType TT_OP_at = addTokenType("@");
  public static final GosuElementType TT_OP_dollar = addTokenType("$");

  public static final GosuElementType TT_OP_comma = addTokenType(",");
  public static final GosuElementType TT_OP_semicolon = addTokenType(";");
  public static final GosuElementType TT_OP_back_tick = addTokenType("`");

  @NotNull
  private static GosuElementType addTokenType(@NotNull String strToken) {
    GosuElementType type = new GosuElementType(strToken);
    TYPE_BY_TOKEN.put(strToken, type);

//## todo: copy keyword config wrt crappy mixed case from Keyword.java
//    TYPE_BY_TOKEN.put( strToken.toUpperCase(), type );
//    TYPE_BY_TOKEN.put( Character.toUpperCase( strToken.charAt( 0 ) ) + strToken.substring( 1 ).toLowerCase(), type );

    return type;
  }

  @NotNull
  private static GosuElementType addKeyword(@NotNull String strToken) {
    GosuElementType type = addTokenType(strToken);
    KEYWORDS.add(type);
    return type;
  }

  public static IElementType getTypeFrom(@NotNull IToken token) {
    switch (token.getType()) {
      case ISourceCodeTokenizer.TT_WORD:
        return TT_IDENTIFIER;

      case ISourceCodeTokenizer.TT_COMMENT:
        return token.getText().startsWith("//") ? TT_COMMENT_LINE : TT_COMMENT_MULTILINE;

      case ISourceCodeTokenizer.TT_WHITESPACE:
        return TT_WHITESPACE;

      case ISourceCodeTokenizer.TT_INTEGER:
      case ISourceCodeTokenizer.TT_NUMBER:
        return TT_NUMBER;

      case (int) '"':
      case (int) '\'':
        return TYPE_BY_TOKEN.get(String.valueOf((char) token.getType()));

      case ISourceCodeTokenizer.TT_KEYWORD:
      case ISourceCodeTokenizer.TT_OPERATOR:
      default: {
        IElementType tt = TYPE_BY_TOKEN.get(token.getText());
        if (tt == null) {
          String tText = token.getText().toLowerCase();
          tt = TYPE_BY_TOKEN.get(tText);
          if (tt == null) {
           if ("nan".equals(tText)) {
              tt = TYPE_BY_TOKEN.get("NaN");
            } else if ("infinity".equals(tText)) {
              tt = TYPE_BY_TOKEN.get("Infinity");
            } else {
              throw new IllegalStateException("Unhandled token type: " + token);
            }
          }
        }
        return tt;
      }
    }
  }

  public static boolean isKeyword(IElementType elementType) {
    return KEYWORDS.contains(elementType);
  }
}

/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang;

import com.intellij.psi.tree.TokenSet;

public abstract class GosuTokenSets extends GosuTokenTypes {
  public static final TokenSet COMMENTS_TOKEN_SET = TokenSet.create(
      TT_COMMENT_LINE,
      TT_COMMENT_MULTILINE);

  public static final TokenSet SEPARATORS = TokenSet.create(
      TT_OP_semicolon);

  public static final TokenSet VISIBILITY_MODIFIERS = TokenSet.create(
      TT_private,
      TT_internal,
      TT_protected,
      TT_public);

  public static final TokenSet MODIFIERS = TokenSet.create(
      TT_abstract,
      TT_private,
      TT_internal,
      TT_public,
      TT_protected,
      TT_static,
      TT_transient,
      TT_final,
      TT_override);

  public static final TokenSet ASSIGNMENT_OPS = TokenSet.create(
      TT_OP_assign,
      TT_OP_assign_plus,
      TT_OP_assign_minus,
      TT_OP_assign_multiply,
      TT_OP_assign_divide,
      TT_OP_assign_modulo,
      TT_OP_assign_and,
      TT_OP_assign_logical_and,
      TT_OP_assing_or,
      TT_OP_assing_logical_or,
      TT_OP_assign_xor);

  public static final TokenSet LOGICAL_OPS = TokenSet.create(
      TT_OP_logical_and,
      TT_OP_logical_or);

  public static final TokenSet EQUALITY_OPS = TokenSet.create(
      TT_OP_equals,
      TT_OP_identity,
      TT_OP_not_identity,
      TT_OP_not_equals,
      TT_OP_not_equals_for_losers);

  public static final TokenSet RELATIONAL_OPS = TokenSet.create(
      TT_OP_less,
      TT_OP_less_equals,
      TT_OP_greater);
  public static final TokenSet BITWISE_OPS = TokenSet.create(
      TT_OP_bitwise_and,
      TT_OP_bitwise_or,
      TT_OP_bitwise_xor);

  public static final TokenSet ADDITIVE_OPS = TokenSet.create(
      TT_OP_plus,
      TT_OP_nullsafe_plus,
      TT_OP_minus,
      TT_OP_nullsafe_minus);

  public static final TokenSet MULTIPLICATIVE_OPS = TokenSet.create(
      TT_OP_multiply,
      TT_OP_nullsafe_multiply,
      TT_OP_divide,
      TT_OP_nullsafe_divide,
      TT_OP_modulo,
      TT_OP_nullsafe_modulo);

  public static final TokenSet SHIFT_OPS = TokenSet.create(
      TT_OP_shift_left,
      TT_OP_shift_right,
      TT_OP_shift_right_unsigned);

  public static final TokenSet UNARY_OPS = TokenSet.create(
      TT_OP_increment,
      TT_OP_decrement,
      TT_OP_not_logical,
      TT_OP_not_bitwise);

  public static final TokenSet INTERVAL_OPS = TokenSet.create(
      TT_OP_interval,
      TT_OP_interval_left_open,
      TT_OP_interval_right_open,
      TT_OP_interval_open);

  public static final TokenSet WHITE_SPACE_TOKEN_SET = TokenSet.create(
      TT_WHITESPACE);

  public static final TokenSet WHITE_SPACES_OR_COMMENTS = TokenSet.orSet(
      WHITE_SPACE_TOKEN_SET,
      COMMENTS_TOKEN_SET);

  public static final TokenSet STRING_LITERALS = TokenSet.create(
      TT_DOUBLE_QUOTED_STRING,
      TT_SINGLE_QUOTED_STRING);
}

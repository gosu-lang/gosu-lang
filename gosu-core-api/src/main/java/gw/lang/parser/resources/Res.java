/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.lang.parser.resources;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

public class Res
{
  public static final ResourceBundle g_resources;

  public static final ResourceKey ARRAY = new ResourceKey("ARRAY");
  public static final ResourceKey BEAN = new ResourceKey("BEAN");
  public static final ResourceKey BOOLEAN = new ResourceKey("BOOLEAN");
  public static final ResourceKey DATETIME = new ResourceKey("DATETIME");
  public static final ResourceKey FUNCTION = new ResourceKey("FUNCTION");
  public static final ResourceKey IDENTIFIER = new ResourceKey("IDENTIFIER");
  public static final ResourceKey METATYPENAME = new ResourceKey("METATYPENAME");
  public static final ResourceKey NULLTYPENAME = new ResourceKey("NULLTYPENAME");
  public static final ResourceKey NUMERIC = new ResourceKey("NUMERIC");
  public static final ResourceKey OBJECT_LITERAL = new ResourceKey("OBJECT_LITERAL");
  public static final ResourceKey STRING = new ResourceKey("STRING");
  public static final ResourceKey MSG_SYNTAX_ERROR = new ResourceKey("MSG_SYNTAX_ERROR");
  public static final ResourceKey MSG_BAD_IDENTIFIER_NAME = new ResourceKey("MSG_BAD_IDENTIFIER_NAME");
  public static final ResourceKey MSG_END_OF_STMT = new ResourceKey("MSG_END_OF_STMT");
  public static final ResourceKey MSG_END_OF_EXPRESSION = new ResourceKey("MSG_END_OF_EXPRESSION");
  public static final ResourceKey MSG_EXPECTING_OPERATOR_TO_FOLLOW_EXPRESSION = new ResourceKey( "MSG_EXPECTING_OPERATOR_TO_FOLLOW_EXPRESSION" );
  public static final ResourceKey MSG_EXPECTING_ARGS = new ResourceKey("MSG_EXPECTING_ARGS");
  public static final ResourceKey MSG_EXPECTING_EXPRESSION_CLOSE = new ResourceKey("MSG_EXPECTING_EXPRESSION_CLOSE");
  public static final ResourceKey MSG_EXPECTING_FUNCTION_CLOSE = new ResourceKey("MSG_EXPECTING_FUNCTION_CLOSE");
  public static final ResourceKey MSG_EXPECTING_TYPE_NAME = new ResourceKey("MSG_EXPECTING_TYPE_NAME");
  public static final ResourceKey MSG_PARAMETERIZATION_NOT_SUPPORTED_FOR_TYPE = new ResourceKey("MSG_PARAMETERIZATION_NOT_SUPPORTED_FOR_TYPE");
  public static final ResourceKey MSG_NO_ARGUMENTS = new ResourceKey("MSG_NO_ARGUMENTS");
  public static final ResourceKey MSG_NULL_SYMBOL_TABLE = new ResourceKey("MSG_NULL_SYMBOL_TABLE");
  public static final ResourceKey MSG_TYPE_MISMATCH = new ResourceKey("MSG_TYPE_MISMATCH");
  public static final ResourceKey MSG_VALUE_MISMATCH = new ResourceKey("MSG_VALUE_MISMATCH");
  public static final ResourceKey MSG_WRONG_NUM_OF_ARGS = new ResourceKey("MSG_WRONG_NUM_OF_ARGS");
  public static final ResourceKey MSG_EXPECTING_NAME_FUNCTION_DEF = new ResourceKey("MSG_EXPECTING_NAME_FUNCTION_DEF");
  public static final ResourceKey MSG_EXPECTING_LEFTPAREN_FUNCTION_DEF = new ResourceKey("MSG_EXPECTING_LEFTPAREN_FUNCTION_DEF");
  public static final ResourceKey MSG_EXPECTING_RIGHTPAREN_FUNCTION_DEF = new ResourceKey("MSG_EXPECTING_RIGHTPAREN_FUNCTION_DEF");
  public static final ResourceKey MSG_EXPECTING_TYPE_FUNCTION_DEF = new ResourceKey("MSG_EXPECTING_TYPE_FUNCTION_DEF");
  public static final ResourceKey MSG_EXPECTING_TYPE_LITERAL_FUNCTION_DEF = new ResourceKey("MSG_EXPECTING_TYPE_LITERAL_FUNCTION_DEF");
  public static final ResourceKey MSG_EXPECTING_IDENTIFIER_FOREACH = new ResourceKey("MSG_EXPECTING_IDENTIFIER_FOREACH");
  public static final ResourceKey MSG_EXPECTING_IDENTIFIER_FOREACH_INDEX = new ResourceKey("MSG_EXPECTING_IDENTIFIER_FOREACH_INDEX");
  public static final ResourceKey MSG_EXPECTING_IDENTIFIER_FOREACH_ITERATOR = new ResourceKey("MSG_EXPECTING_IDENTIFIER_FOREACH_ITERATOR");
  public static final ResourceKey MSG_FOREACH_ITERATOR_NOT_ALLOWED = new ResourceKey("MSG_FOREACH_ITERATOR_NOT_ALLOWED");
  public static final ResourceKey MSG_ITERATOR_SYMBOL_ONLY_SUPPORTED_ON_ITERABLE_OBJECTS = new ResourceKey("MSG_ITERATOR_SYMBOL_ONLY_SUPPORTED_ON_ITERABLE_OBJECTS");
  public static final ResourceKey MSG_EXPECTING_IDENTIFIER_EXISTS = new ResourceKey("MSG_EXPECTING_IDENTIFIER_EXISTS");
  public static final ResourceKey MSG_EXPECTING_IDENTIFIER_EXISTS_INDEX = new ResourceKey("MSG_EXPECTING_IDENTIFIER_EXISTS_INDEX");
  public static final ResourceKey MSG_EXPECTING_IN_FOREACH = new ResourceKey("MSG_EXPECTING_IN_FOREACH");
  public static final ResourceKey MSG_EXPECTING_LEFTPAREN_FE = new ResourceKey("MSG_EXPECTING_LEFTPAREN_FE");
  public static final ResourceKey MSG_EXPECTING_RIGHTPAREN_FE = new ResourceKey("MSG_EXPECTING_RIGHTPAREN_FE");
  public static final ResourceKey MSG_EXPECTING_LEFTPAREN_BLOCK = new ResourceKey("MSG_EXPECTING_LEFTPAREN_BLOCK");
  public static final ResourceKey MSG_EXPECTING_RIGHTPAREN_BLOCK = new ResourceKey("MSG_EXPECTING_RIGHTPAREN_BLOCK");
  public static final ResourceKey MSG_EXPECTING_COLON_BLOCK = new ResourceKey("MSG_EXPECTING_COLON_BLOCK");
  public static final ResourceKey MSG_EXPECTING_IN_EXISTS = new ResourceKey("MSG_EXPECTING_IN_EXISTS");
  public static final ResourceKey MSG_EXPECTING_WHERE_EXISTS = new ResourceKey("MSG_EXPECTING_WHERE_EXISTS");
  public static final ResourceKey MSG_EXPECTING_LEFTPAREN_EXISTS = new ResourceKey("MSG_EXPECTING_LEFTPAREN_EXISTS");
  public static final ResourceKey MSG_EXPECTING_RIGHTPAREN_EXISTS = new ResourceKey("MSG_EXPECTING_RIGHTPAREN_EXISTS");
  public static final ResourceKey MSG_EXPECTING_LEFTPAREN_EVAL = new ResourceKey("MSG_EXPECTING_LEFTPAREN_EVAL");
  public static final ResourceKey MSG_EXPECTING_RIGHTPAREN_EVAL = new ResourceKey("MSG_EXPECTING_RIGHTPAREN_EVAL");
  public static final ResourceKey MSG_EXPECTING_LEFTPAREN_CATCH = new ResourceKey("MSG_EXPECTING_LEFTPAREN_CATCH");
  public static final ResourceKey MSG_EXPECTING_RIGHTPAREN_CATCH = new ResourceKey("MSG_EXPECTING_RIGHTPAREN_CATCH");
  public static final ResourceKey MSG_EXPECTING_IDENTIFIER_CATCH = new ResourceKey("MSG_EXPECTING_IDENTIFIER_CATCH");
  public static final ResourceKey MSG_CATCH_OR_FINALLY_REQUIRED = new ResourceKey("MSG_CATCH_OR_FINALLY_REQUIRED");
  public static final ResourceKey MSG_EXPECTING_LEFTBRACE_STMTBLOCK = new ResourceKey("MSG_EXPECTING_LEFTBRACE_STMTBLOCK");
  public static final ResourceKey MSG_EXPECTING_RIGHTBRACE_STMTBLOCK = new ResourceKey("MSG_EXPECTING_RIGHTBRACE_STMTBLOCK");
  public static final ResourceKey MSG_EXPECTING_LEFTPAREN_IF = new ResourceKey("MSG_EXPECTING_LEFTPAREN_IF");
  public static final ResourceKey MSG_EXPECTING_RIGHTPAREN_IF = new ResourceKey("MSG_EXPECTING_RIGHTPAREN_IF");
  public static final ResourceKey MSG_EXPECTING_LEFTPAREN_SWITCH = new ResourceKey("MSG_EXPECTING_LEFTPAREN_SWITCH");
  public static final ResourceKey MSG_EXPECTING_RIGHTPAREN_SWITCH = new ResourceKey("MSG_EXPECTING_RIGHTPAREN_SWITCH");
  public static final ResourceKey MSG_EXPECTING_LEFTPAREN_EXCEPT = new ResourceKey("MSG_EXPECTING_LEFTPAREN_EXCEPT");
  public static final ResourceKey MSG_EXPECTING_RIGHTPAREN_EXCEPT = new ResourceKey("MSG_EXPECTING_RIGHTPAREN_EXCEPT");
  public static final ResourceKey MSG_EXPECTING_LEFTPAREN_WHILE = new ResourceKey("MSG_EXPECTING_LEFTPAREN_WHILE");
  public static final ResourceKey MSG_EXPECTING_RIGHTPAREN_WHILE = new ResourceKey("MSG_EXPECTING_RIGHTPAREN_WHILE");
  public static final ResourceKey MSG_EXPECTING_WHILE_DO = new ResourceKey("MSG_EXPECTING_WHILE_DO");
  public static final ResourceKey MSG_LOOP_DOESNT_LOOP = new ResourceKey("MSG_LOOP_DOESNT_LOOP");
  public static final ResourceKey MSG_EXPECTING_EQUALS_ASSIGN = new ResourceKey("MSG_EXPECTING_EQUALS_ASSIGN");
  public static final ResourceKey MSG_EXPECTING_EXPRESSSION_ON_RHS = new ResourceKey("MSG_EXPECTING_EXPRESSSION_ON_RHS");
  public static final ResourceKey MSG_EXPECTING_STATEMENT = new ResourceKey("MSG_EXPECTING_STATEMENT");
  public static final ResourceKey MSG_EXPECTING_BEANTYPE = new ResourceKey("MSG_EXPECTING_BEANTYPE");
  public static final ResourceKey MSG_EXPECTING_ARRAYTYPE_FOREACH = new ResourceKey("MSG_EXPECTING_ARRAYTYPE_FOREACH");
  public static final ResourceKey MSG_EXPECTING_ARRAYTYPE_EXISTS = new ResourceKey("MSG_EXPECTING_ARRAYTYPE_EXISTS");
  public static final ResourceKey MSG_EXPECTING_IDENTIFIER_ARG_TYPE = new ResourceKey("MSG_EXPECTING_IDENTIFIER_ARG_TYPE");
  public static final ResourceKey MSG_EXPECTING_ARRAY_BRACKET = new ResourceKey("MSG_EXPECTING_ARRAY_BRACKET");
  public static final ResourceKey MSG_EXPECTING_NEW_ARRAY_OR_CTOR = new ResourceKey("MSG_EXPECTING_NEW_ARRAY_OR_CTOR");
  public static final ResourceKey MSG_EXPECTING_OPEN_BRACE_FOR_NEW_ARRAY = new ResourceKey("MSG_EXPECTING_OPEN_BRACE_FOR_NEW_ARRAY");
  public static final ResourceKey MSG_EXPECTING_CLOSE_BRACE_FOR_NEW_ARRAY = new ResourceKey("MSG_EXPECTING_CLOSE_BRACE_FOR_NEW_ARRAY");
  public static final ResourceKey MSG_EXPECTING_CLOSING_ANGLE_BRACKET_FOR_TYPE = new ResourceKey("MSG_EXPECTING_CLOSING_ANGLE_BRACKET_FOR_TYPE");
  public static final ResourceKey MSG_EXPECTING_CLOSING_ANGLE_BRACKET_FOR_TYPE_VAR_LIST = new ResourceKey("MSG_EXPECTING_CLOSING_ANGLE_BRACKET_FOR_TYPE_VAR_LIST");
  public static final ResourceKey MSG_EXPECTING_OPEN_BRACE_FOR_SWITCH = new ResourceKey("MSG_EXPECTING_OPEN_BRACE_FOR_SWITCH");
  public static final ResourceKey MSG_EXPECTING_CLOSE_BRACE_FOR_SWITCH = new ResourceKey("MSG_EXPECTING_CLOSE_BRACE_FOR_SWITCH");
  public static final ResourceKey MSG_EXPECTING_CASE_COLON = new ResourceKey("MSG_EXPECTING_CASE_COLON");
  public static final ResourceKey MSG_DUPLICATE_CASE_EXPRESSION = new ResourceKey("MSG_DUPLICATE_CASE_EXPRESSION");
  public static final ResourceKey MSG_EXPECTING_COLON_TERNARY = new ResourceKey("MSG_EXPECTING_COLON_TERNARY");
  public static final ResourceKey MSG_EXPECTING_REFERENCE_TYPE = new ResourceKey("MSG_EXPECTING_REFERENCE_TYPE");  
  public static final ResourceKey MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP = new ResourceKey("MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP");
  public static final ResourceKey MSG_EXPECTING_IDENTIFIER_VAR = new ResourceKey("MSG_EXPECTING_IDENTIFIER_VAR");
  public static final ResourceKey MSG_EXPECTING_VAR_STMT = new ResourceKey("MSG_EXPECTING_VAR_STMT");
  public static final ResourceKey MSG_EXPECTING_TYPELITERAL = new ResourceKey("MSG_EXPECTING_TYPELITERAL");
  public static final ResourceKey MSG_EXPECTING_TYPELITERAL_OR_NAMESPACE = new ResourceKey("MSG_EXPECTING_TYPELITERAL_OR_NAMESPACE");
  public static final ResourceKey MSG_PRIMITIVES_NOT_ALLOWED_HERE = new ResourceKey("MSG_PRIMITIVES_NOT_ALLOWED_HERE");
  public static final ResourceKey MSG_INCONVERTIBLE_TYPES = new ResourceKey("MSG_INCONVERTIBLE_TYPES");
  public static final ResourceKey MSG_BAD_NAMESPACE = new ResourceKey("MSG_BAD_NAMESPACE");
  public static final ResourceKey MSG_EXPECTING_ARRAYLENGTH = new ResourceKey("MSG_EXPECTING_ARRAYLENGTH");
  public static final ResourceKey MSG_ENTITYBEANS_ARE_IMMUTABLE = new ResourceKey("MSG_ENTITYBEANS_ARE_IMMUTABLE");
  public static final ResourceKey MSG_MULTIPLE_DEFAULT_CLAUSES_NOT_PERMITTED = new ResourceKey("MSG_MULTIPLE_DEFAULT_CLAUSES_NOT_PERMITTED");
  public static final ResourceKey MSG_CASE_CLAUSE_MAY_NOT_FOLLOW_DEFAULT_CLAUSE = new ResourceKey("MSG_CASE_CLAUSE_MAY_NOT_FOLLOW_DEFAULT_CLAUSE");
  public static final ResourceKey MSG_METHOD_NOT_VISIBLE = new ResourceKey("MSG_METHOD_NOT_VISIBLE");
  public static final ResourceKey MSG_PROPERTY_NOT_VISIBLE = new ResourceKey("MSG_PROPERTY_NOT_VISIBLE");
  public static final ResourceKey MSG_OBJECT_RETIRED = new ResourceKey("MSG_OBJECT_RETIRED");
  public static final ResourceKey MSG_OBJECT_LITERALS_DEPRECATED = new ResourceKey("MSG_OBJECT_LITERALS_DEPRECATED");
  public static final ResourceKey MSG_DEPRECATED_MEMBER = new ResourceKey("MSG_DEPRECATED_MEMBER");
  public static final ResourceKey MSG_EXPECTING_ENTITY_TYPE = new ResourceKey("MSG_EXPECTING_ENTITY_TYPE");
  public static final ResourceKey MSG_EXPECTING_READONLY_ENTITY_TYPE = new ResourceKey("MSG_EXPECTING_READONLY_ENTITY_TYPE");
  public static final ResourceKey MSG_STR_IMMUTABLE = new ResourceKey("MSG_STR_IMMUTABLE");
  public static final ResourceKey MSG_EXPECTING_QUERY_PATH = new ResourceKey("MSG_EXPECTING_QUERY_PATH");
  public static final ResourceKey MSG_QUERY_PATH_MUST_BEGIN_WITH = new ResourceKey("MSG_QUERY_PATH_MUST_BEGIN_WITH");
  public static final ResourceKey MSG_EXPECTING_CONDITIONAL_EXPRESSION = new ResourceKey("MSG_EXPECTING_CONDITIONAL_EXPRESSION");
  public static final ResourceKey MSG_CONDITIONAL_EXPRESSION_EXPECTS_BOOLEAN = new ResourceKey("MSG_CONDITIONAL_EXPRESSION_EXPECTS_BOOLEAN");
  public static final ResourceKey MSG_EXPECTING_IDENTIFIER_FIND = new ResourceKey("MSG_EXPECTING_IDENTIFIER_FIND");
  public static final ResourceKey MSG_EXPECTING_IN_FIND = new ResourceKey("MSG_EXPECTING_IN_FIND");
  public static final ResourceKey MSG_EXPECTING_WHERE_FIND = new ResourceKey("MSG_EXPECTING_WHERE_FIND");
  public static final ResourceKey MSG_EXPECTING_LEFTPAREN_FIND = new ResourceKey("MSG_EXPECTING_LEFTPAREN_FIND");
  public static final ResourceKey MSG_EXPECTING_RIGHTPAREN_FIND = new ResourceKey("MSG_EXPECTING_RIGHTPAREN_FIND");
  public static final ResourceKey MSG_QUERYPATH_MUST_BEGIN_WITH = new ResourceKey("MSG_QUERYPATH_MUST_BEGIN_WITH");
  public static final ResourceKey MSG_QUERY_IN_LHS_OP_NOT_ENTITY = new ResourceKey("MSG_QUERY_IN_LHS_OP_NOT_ENTITY");
  public static final ResourceKey MSG_QUERY_IN_LHS_OP_NOT_ARRAY = new ResourceKey("MSG_QUERY_IN_LHS_OP_NOT_ARRAY");
  public static final ResourceKey MSG_QUERY_EXPECTED_BOOLEAN_EXPRESSION = new ResourceKey("MSG_QUERY_EXPECTED_BOOLEAN_EXPRESSION");
  public static final ResourceKey MSG_UNTERMINATED_STRING_LITERAL = new ResourceKey("MSG_UNTERMINATED_STRING_LITERAL");
  public static final ResourceKey MSG_INVALID_CHAR_AT = new ResourceKey("MSG_INVALID_CHAR_AT");
  public static final ResourceKey MSG_UNTERMINATED_COMMENT = new ResourceKey("MSG_UNTERMINATED_COMMENT");
  public static final ResourceKey MSG_FUNCTION_ALREADY_DEFINED = new ResourceKey("MSG_FUNCTION_ALREADY_DEFINED");
  public static final ResourceKey MSG_FUNCTION_ALREADY_DEFINED_IN_EXTENDED_CLASS = new ResourceKey("MSG_FUNCTION_ALREADY_DEFINED_IN_EXTENDED_CLASS");
  public static final ResourceKey MSG_FUNCTION_CLASH = new ResourceKey("MSG_FUNCTION_CLASH");
  public static final ResourceKey MSG_CANNOT_OVERRIDE_FINAL = new ResourceKey("MSG_CANNOT_OVERRIDE_FINAL");
  public static final ResourceKey MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR = new ResourceKey("MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR");
  public static final ResourceKey MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT = new ResourceKey("MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT");
  public static final ResourceKey MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT = new ResourceKey("MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT");
  public static final ResourceKey MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT_LOOP = new ResourceKey("MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT_LOOP");
  public static final ResourceKey MSG_NO_SUCH_FUNCTION = new ResourceKey("MSG_NO_SUCH_FUNCTION");
  public static final ResourceKey MSG_DUPLICATE_CLASS_FOUND = new ResourceKey("MSG_DUPLICATE_CLASS_FOUND");
  public static final ResourceKey MSG_DUPLICATE_TYPE_FOUND = new ResourceKey("MSG_DUPLICATE_TYPE_FOUND");
  public static final ResourceKey MSG_DUPLICATE_ENHANCEMENT_FOUND = new ResourceKey("MSG_DUPLICATE_ENHANCEMENT_FOUND");
  public static final ResourceKey MSG_EXPECTING_OPEN_BRACE_FOR_CLASS_DEF = new ResourceKey("MSG_EXPECTING_OPEN_BRACE_FOR_CLASS_DEF");
  public static final ResourceKey MSG_EXPECTING_CLOSE_BRACE_FOR_CLASS_DEF = new ResourceKey("MSG_EXPECTING_CLOSE_BRACE_FOR_CLASS_DEF");
  public static final ResourceKey MSG_EXPECTING_NAME_CLASS_DEF = new ResourceKey("MSG_EXPECTING_NAME_CLASS_DEF");
  public static final ResourceKey MSG_EXPECTING_ENHANCEMENT_DEF = new ResourceKey("MSG_EXPECTING_ENHANCEMENT_DEF");
  public static final ResourceKey MSG_NAMESPACE_CLASS_CONFLICT = new ResourceKey("MSG_NAMESPACE_CLASS_CONFLICT");
  public static final ResourceKey MSG_CANNOT_EXTEND_PRIMITIVE_TYPE = new ResourceKey("MSG_CANNOT_EXTEND_PRIMITIVE_TYPE");
  public static final ResourceKey MSG_CANNOT_EXTEND_FINAL_TYPE = new ResourceKey("MSG_CANNOT_EXTEND_FINAL_TYPE");
  public static final ResourceKey MSG_ENHANCEMENTS_CANNOT_ENHANCE_OTHER_ENHANCEMENTS = new ResourceKey("MSG_ENHANCEMENTS_CANNOT_ENHANCE_OTHER_ENHANCEMENTS");
  public static final ResourceKey MSG_CYCLIC_INHERITANCE = new ResourceKey("MSG_CYCLIC_INHERITANCE");
  public static final ResourceKey MSG_EXPECTING_OPEN_BRACE_FOR_CONSTRUCTOR_DEF = new ResourceKey("MSG_EXPECTING_OPEN_BRACE_FOR_CONSTRUCTOR_DEF");
  public static final ResourceKey MSG_EXPECTING_OPEN_BRACE_FOR_FUNCTION_DEF = new ResourceKey("MSG_EXPECTING_OPEN_BRACE_FOR_FUNCTION_DEF");
  public static final ResourceKey MSG_EXPECTING_CLOSE_BRACE_FOR_FUNCTION_DEF = new ResourceKey("MSG_EXPECTING_CLOSE_BRACE_FOR_FUNCTION_DEF");
  public static final ResourceKey MSG_EXPECTING_CLOSE_BRACE_FOR_CONSTRUCTOR_DEF = new ResourceKey("MSG_EXPECTING_CLOSE_BRACE_FOR_CONSTRUCTOR_DEF");
  public static final ResourceKey MSG_NO_DEFAULT_CTOR_IN = new ResourceKey("MSG_NO_DEFAULT_CTOR_IN");
  public static final ResourceKey MSG_WRONG_NAMESPACE = new ResourceKey("MSG_WRONG_NAMESPACE");
  public static final ResourceKey MSG_WRONG_CLASSNAME = new ResourceKey("MSG_WRONG_CLASSNAME");
  public static final ResourceKey MSG_EXPECTING_PROPERTY_GET_OR_SET_MODIFIER = new ResourceKey("MSG_EXPECTING_PROPERTY_GET_OR_SET_MODIFIER");
  public static final ResourceKey MSG_EXPECTING_NAME_PROPERTY = new ResourceKey("MSG_EXPECTING_NAME_PROPERTY");
  public static final ResourceKey MSG_PROPERTY_ALREADY_DEFINED = new ResourceKey("MSG_PROPERTY_ALREADY_DEFINED");
  public static final ResourceKey MSG_GETTER_FOR_PROPERTY_ALREADY_DEFINED = new ResourceKey("MSG_GETTER_FOR_PROPERTY_ALREADY_DEFINED");
  public static final ResourceKey MSG_SETTER_FOR_PROPERTY_ALREADY_DEFINED = new ResourceKey("MSG_SETTER_FOR_PROPERTY_ALREADY_DEFINED");
  public static final ResourceKey MSG_PROPERTY_NOT_WRITABLE = new ResourceKey("MSG_PROPERTY_NOT_WRITABLE");
  public static final ResourceKey MSG_CLASS_PROPERTY_NOT_READABLE = new ResourceKey("MSG_CLASS_PROPERTY_NOT_READABLE");
  public static final ResourceKey MSG_CLASS_PROPERTY_NOT_WRITABLE = new ResourceKey("MSG_CLASS_PROPERTY_NOT_WRITABLE");
  public static final ResourceKey MSG_NO_VIEWS_FOR_YOUS = new ResourceKey("MSG_NO_VIEWS_FOR_YOUS");
  public static final ResourceKey MSG_VARIABLE_TYPE_OR_VALUE_REQUIRED = new ResourceKey("MSG_VARIABLE_TYPE_OR_VALUE_REQUIRED");
  public static final ResourceKey MSG_VARIABLE_MUST_HAVE_NON_NULL_TYPE = new ResourceKey("MSG_VARIABLE_MUST_HAVE_NON_NULL_TYPE");
  public static final ResourceKey MSG_VOID_NOT_ALLOWED = new ResourceKey("MSG_VOID_NOT_ALLOWED");
  public static final ResourceKey MSG_FUNCTIONS_NOT_ALLOWED_IN_THIS_CONTEXT =new ResourceKey( "MSG_FUNCTIONS_NOT_ALLOWED_IN_THIS_CONTEXT" );
  public static final ResourceKey MSG_CONSTRUCTORS_NOT_ALLOWD_IN_THIS_CONTEXT =new ResourceKey( "MSG_CONSTRUCTORS_NOT_ALLOWD_IN_THIS_CONTEXT" );
  public static final ResourceKey MSG_SINGLE_ANON_CTOR =new ResourceKey( "MSG_SINGLE_ANON_CTOR" );
  public static final ResourceKey MSG_ANON_CTOR_PARAMS_CONFLICT_WITH_CALL_SITE =new ResourceKey( "MSG_ANON_CTOR_PARAMS_CONFLICT_WITH_CALL_SITE" );
  public static final ResourceKey MSG_INNER_CLASSES_NOT_ALLOWED_IN_THIS_CONTEXT =new ResourceKey( "MSG_INNER_CLASSES_NOT_ALLOWED_IN_THIS_CONTEXT" );
  public static final ResourceKey MSG_VARIABLE_ALREADY_DEFINED = new ResourceKey("MSG_VARIABLE_ALREADY_DEFINED");
  public static final ResourceKey MSG_VARIABLE_REQUEST_DOES_NOT_ALLOW_VALUE = new ResourceKey("MSG_VARIABLE_REQUEST_DOES_NOT_ALLOW_VALUE");
  public static final ResourceKey MSG_NO_SOURCE_FOUND = new ResourceKey("MSG_NO_SOURCE_FOUND");
  public static final ResourceKey MSG_CANNOT_RETURN_VALUE_FROM_VOID = new ResourceKey("MSG_CANNOT_RETURN_VALUE_FROM_VOID");
  public static final ResourceKey MSG_MISSING_RETURN_VALUE = new ResourceKey("MSG_MISSING_RETURN_VALUE");
  public static final ResourceKey MSG_RETURN_NOT_ALLOWED_HERRE = new ResourceKey( "MSG_RETURN_NOT_ALLOWED_HERRE" );
  public static final ResourceKey MSG_FUNCTION_ARG_NAME_CONFLICTS_WITH_CLASS_VAR = new ResourceKey("MSG_FUNCTION_ARG_NAME_CONFLICTS_WITH_CLASS_VAR");
  public static final ResourceKey MSG_EXPECTING_DECL = new ResourceKey("MSG_EXPECTING_DECL");
  public static final ResourceKey MSG_MISSING_RETURN = new ResourceKey("MSG_MISSING_RETURN");
  public static final ResourceKey MSG_UNREACHABLE_STMT = new ResourceKey("MSG_UNREACHABLE_STMT");
  public static final ResourceKey MSG_BREAK_OUTSIDE_SWITCH_OR_LOOP = new ResourceKey("MSG_BREAK_OUTSIDE_SWITCH_OR_LOOP");
  public static final ResourceKey MSG_CONTINUE_OUTSIDE_LOOP = new ResourceKey("MSG_CONTINUE_OUTSIDE_LOOP");
  public static final ResourceKey MSG_EXPECTING_ARROW_AFTER_BLOCK_ARGS = new ResourceKey("MSG_EXPECTING_ARROW_AFTER_BLOCK_ARGS");
  public static final ResourceKey MSG_EXPECTING_CLOSE_BRACE_FOR_BLOCK = new ResourceKey("MSG_EXPECTING_CLOSE_BRACE_FOR_BLOCK");
  public static final ResourceKey MSG_UNEXPECTED_ARROW = new ResourceKey("MSG_UNEXPECTED_ARROW");
  public static final ResourceKey MSG_EXPECTING_ARROW_AFTER_MAP_KEY = new ResourceKey("MSG_EXPECTING_ARROW_AFTER_MAP_KEY");
  public static final ResourceKey MSG_EXPECTING_CLOSE_BRACE_FOR_INITIALIZER = new ResourceKey("MSG_EXPECTING_CLOSE_BRACE_FOR_INITIALIZER");
  public static final ResourceKey MSG_CANNOT_CONSTRUCT_ABSTRACT_CLASS = new ResourceKey("MSG_CANNOT_CONSTRUCT_ABSTRACT_CLASS");
  public static final ResourceKey MSG_CANNOT_CONSTRUCT_RECURSIVE_CLASS = new ResourceKey("MSG_CANNOT_CONSTRUCT_RECURSIVE_CLASS");
  public static final ResourceKey MSG_RECURSIVE_CONSTRUCTOR = new ResourceKey("MSG_RECURSIVE_CONSTRUCTOR");
  public static final ResourceKey MSG_MODIFIER_ABSTRACT_NOT_ALLOWED_HERE = new ResourceKey("MSG_MODIFIER_ABSTRACT_NOT_ALLOWED_HERE");
  public static final ResourceKey MSG_NOT_ALLOWED_IN_INTERFACE = new ResourceKey("MSG_NOT_ALLOWED_IN_INTERFACE");
  public static final ResourceKey MSG_UNIMPLEMENTED_METHOD = new ResourceKey("MSG_UNIMPLEMENTED_METHOD");
  public static final ResourceKey MSG_INTERFACE_CANNOT_EXTEND_CLASS = new ResourceKey("MSG_INTERFACE_CANNOT_EXTEND_CLASS");
  public static final ResourceKey MSG_CLASS_CANNOT_EXTEND_INTERFACE = new ResourceKey("MSG_CLASS_CANNOT_EXTEND_INTERFACE");
  public static final ResourceKey MSG_CLASS_MUST_HAVE_PACKAGE = new ResourceKey("MSG_CLASS_MUST_HAVE_PACKAGE");
  public static final ResourceKey MSG_CLASS_CANNOT_IMPLEMENT_CLASS = new ResourceKey("MSG_CLASS_CANNOT_IMPLEMENT_CLASS");
  public static final ResourceKey MSG_ENUM_CANNOT_EXTEND_CLASS = new ResourceKey("MSG_ENUM_CANNOT_EXTEND_CLASS");
  public static final ResourceKey MSG_ENUM_CANNOT_HAVE_ANNOTATIONS = new ResourceKey("MSG_ENUM_CANNOT_HAVE_ANNOTATIONS");
  public static final ResourceKey MSG_BAD_CAPTURE_TYPE = new ResourceKey("MSG_BAD_CAPTURE_TYPE");
  public static final ResourceKey MSG_POTENTIALLY_BAD_CAPTURE = new ResourceKey("MSG_POTENTIALLY_BAD_CAPTURE");
  public static final ResourceKey MSG_CANNOT_CAPTURE_SYMBOL_IN_BLOCK_IN_ANON_CLASS = new ResourceKey("MSG_CANNOT_CAPTURE_SYMBOL_IN_BLOCK_IN_ANON_CLASS");
  public static final ResourceKey MSG_CANNOT_CAPTURE_SYMBOL_IN_ANON_CLASS_IN_BLOCK = new ResourceKey("MSG_CANNOT_CAPTURE_SYMBOL_IN_ANON_CLASS_IN_BLOCK");
  public static final ResourceKey MSG_EXPECTING_ENHANCE_KEYWORD = new ResourceKey("MSG_EXPECTING_ENHANCE_KEYWORD");
  public static final ResourceKey MSG_NON_PRIVATE_MEMBERS_MUST_DECLARE_TYPE = new ResourceKey("MSG_NON_PRIVATE_MEMBERS_MUST_DECLARE_TYPE");
  public static final ResourceKey MSG_DELEGATES_CANNOT_BE_STATIC = new ResourceKey("MSG_DELEGATES_CANNOT_BE_STATIC");
  public static final ResourceKey MSG_DELEGATE_METHOD_CONFLICT = new ResourceKey("MSG_DELEGATE_METHOD_CONFLICT");
  public static final ResourceKey MSG_EXTENSION_CLASS_NOT_FOUND = new ResourceKey("MSG_EXTENSION_CLASS_NOT_FOUND");
  public static final ResourceKey MSG_NO_WILDCARDS = new ResourceKey("MSG_NO_WILDCARDS");
  public static final ResourceKey MSG_CANNOT_CALL_METHOD_WITH_WILDCARD_PARAM = new ResourceKey("MSG_CANNOT_CALL_METHOD_WITH_WILDCARD_PARAM");
  public static final ResourceKey MSG_COULD_NOT_PARAMETERIZE = new ResourceKey("MSG_COULD_NOT_PARAMETERIZE");
  public static final ResourceKey MSG_USE_EXPANSION_OPERATOR = new ResourceKey("MSG_USE_EXPANSION_OPERATOR");
  public static final ResourceKey MSG_EXPECTING_COLON_ENHANCEMENT = new ResourceKey("MSG_EXPECTING_COLON_ENHANCEMENT");
  public static final ResourceKey MSG_EXPECTING_CLOSE_BRACKET_FOR_LIST_LITERAL = new ResourceKey("MSG_EXPECTING_CLOSE_BRACKET_FOR_LIST_LITERAL");
  public static final ResourceKey MSG_CANNOT_PARAMETERIZE_NONGENERIC = new ResourceKey("MSG_CANNOT_PARAMETERIZE_NONGENERIC");
  public static final ResourceKey MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO = new ResourceKey("MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO");
  public static final ResourceKey MSG_TYPE_NOT_ANNOTATION = new ResourceKey("MSG_TYPE_NOT_ANNOTATION");
  public static final ResourceKey MSG_MISPLACED_META_ANNOTATION = new ResourceKey("MSG_MISPLACED_META_ANNOTATION");
  public static final ResourceKey MSG_MISPLACED_ANNOTATION = new ResourceKey("MSG_MISPLACED_ANNOTATION");
  public static final ResourceKey MSG_ANNOTATION_WHEN_NONE_ALLOWED = new ResourceKey("MSG_ANNOTATION_WHEN_NONE_ALLOWED");
  public static final ResourceKey MSG_TOO_MANY_ANNOTATIONS = new ResourceKey("MSG_TOO_MANY_ANNOTATIONS");
  public static final ResourceKey MSG_INVALID_TYPE = new ResourceKey("MSG_INVALID_TYPE");
  public static final ResourceKey MSG_INVALID_INNER_TYPE = new ResourceKey("MSG_INVALID_INNER_TYPE");
  public static final ResourceKey MSG_INVALID_GENERIC_EXCEPTION = new ResourceKey("MSG_INVALID_GENERIC_EXCEPTION");
  public static final ResourceKey MSG_INVALID_TYPE_WITH_QUALIFIED_NAME = new ResourceKey("MSG_INVALID_TYPE_WITH_QUALIFIED_NAME");
  public static final ResourceKey MSG_NOT_A_STATEMENT = new ResourceKey("MSG_NOT_A_STATEMENT");
  public static final ResourceKey MSG_PROPERTY_SET_MUST_HAVE_ONE_PARAMETER = new ResourceKey("MSG_PROPERTY_SET_MUST_HAVE_ONE_PARAMETER");
  public static final ResourceKey MSG_PROPERTY_SET_MUST_RETURN_VOID = new ResourceKey("MSG_PROPERTY_SET_MUST_RETURN_VOID");
  public static final ResourceKey MSG_MISSING_PROPERTY_RETURN = new ResourceKey("MSG_MISSING_PROPERTY_RETURN");
  public static final ResourceKey MSG_CANNOT_DEREF_PROPERTIES_IN_WHERE = new ResourceKey("MSG_CANNOT_DEREF_PROPERTIES_IN_WHERE");
  public static final ResourceKey MSG_IMPLICIT_COERCION_WARNING = new ResourceKey("MSG_IMPLICIT_COERCION_WARNING");
  public static final ResourceKey MSG_IMPLICIT_COERCION_ERROR = new ResourceKey("MSG_IMPLICIT_COERCION_ERROR");
  public static final ResourceKey MSG_ASYMMETRICAL_COMPARISON = new ResourceKey("MSG_ASYMMETRICAL_COMPARISON");
  public static final ResourceKey MSG_SILLY_ASSIGNMENT = new ResourceKey("MSG_SILLY_ASSIGNMENT");
  public static final ResourceKey MSG_UNUSED_VARIABLE = new ResourceKey("MSG_UNUSED_VARIABLE");
  public static final ResourceKey MSG_LIKELY_JAVA_CAST = new ResourceKey("MSG_LIKELY_JAVA_CAST");
  public static final ResourceKey MSG_UNEXPECTED_TOKEN = new ResourceKey("MSG_UNEXPECTED_TOKEN");
  public static final ResourceKey MSG_EXPECTING_INT_ACCESS_TO_ARR_ETC = new ResourceKey("MSG_EXPECTING_INT_ACCESS_TO_ARR_ETC");
  public static final ResourceKey MSG_EXPECTING_BEAN_TYPE_WITH_REFLECTION_OPERATOR = new ResourceKey("MSG_EXPECTING_BEAN_TYPE_WITH_REFLECTION_OPERATOR");
  public static final ResourceKey MSG_PROPERTY_REFLECTION_ONLY_WITH_STRINGS = new ResourceKey("MSG_PROPERTY_REFLECTION_ONLY_WITH_STRINGS");
  public static final ResourceKey MSG_EXPECTING_BRACKET_TO_CLOSE_DYNAMIC_MEMBER_ACCESS = new ResourceKey("MSG_EXPECTING_BRACKET_TO_CLOSE_DYNAMIC_MEMBER_ACCESS");
  public static final ResourceKey MSG_WRONG_NUMBER_OF_ARGS_TO_FUNCTION = new ResourceKey("MSG_WRONG_NUMBER_OF_ARGS_TO_FUNCTION");
  public static final ResourceKey MSG_WRONG_NUMBER_OF_ARGS_TO_CONSTRUCTOR = new ResourceKey("MSG_WRONG_NUMBER_OF_ARGS_TO_CONSTRUCTOR");
  public static final ResourceKey MSG_SHOULD_HAVE_FOUND_METHOD_AT_LINE = new ResourceKey("MSG_SHOULD_HAVE_FOUND_METHOD_AT_LINE");
  public static final ResourceKey MSG_VOID_EXPRESSION_NOT_ALLOWED = new ResourceKey("MSG_VOID_EXPRESSION_NOT_ALLOWED");
  public static final ResourceKey MSG_EXPECTING_RETURN_TYPE_OR_FUN_BODY = new ResourceKey("MSG_EXPECTING_RETURN_TYPE_OR_FUN_BODY");
  public static final ResourceKey MSG_INVALID_TYPE_FOR_ANNOTATION_MEMBER = new ResourceKey("MSG_INVALID_TYPE_FOR_ANNOTATION_MEMBER");
  public static final ResourceKey MSG_ABSTRACT_METHOD_CANNOT_BE_ACCESSED_DIRECTLY = new ResourceKey( "MSG_ABSTRACT_METHOD_CANNOT_BE_ACCESSED_DIRECTLY" );
  public static final ResourceKey MSG_ERRANT_TYPE_VAR = new ResourceKey("MSG_ERRANT_TYPE_VAR");
  public static final ResourceKey MSG_USING_VOID_RETURN_TYPE_FROM_NON_NULL_EXPR = new ResourceKey("MSG_USING_VOID_RETURN_TYPE_FROM_NON_NULL_EXPR");
  public static final ResourceKey MSG_FIELD_TYPE_HAS_NOT_BEEN_INFERRED = new ResourceKey("MSG_FIELD_TYPE_HAS_NOT_BEEN_INFERRED");
  public static final ResourceKey MSG_BEAN_CLASS_IS_NULL = new ResourceKey("MSG_BEAN_CLASS_IS_NULL");
  public static final ResourceKey MSG_BEAN_MEMBER_PATH_IS_NULL = new ResourceKey("MSG_BEAN_MEMBER_PATH_IS_NULL");
  public static final ResourceKey MSG_BEAN_MEMBER_PATH_IS_EMPTY = new ResourceKey("MSG_BEAN_MEMBER_PATH_IS_EMPTY");
  public static final ResourceKey MSG_NO_EXPLICIT_TYPE_INFO_FOUND = new ResourceKey("MSG_NO_EXPLICIT_TYPE_INFO_FOUND");
  public static final ResourceKey MSG_NO_CONSTRUCTOR_FOUND_FOR_CLASS = new ResourceKey("MSG_NO_CONSTRUCTOR_FOUND_FOR_CLASS");
  public static final ResourceKey MSG_WRONG_NUMBER_OF_ARGS_FOR_METHOD_ON_CLASS = new ResourceKey("MSG_WRONG_NUMBER_OF_ARGS_FOR_METHOD_ON_CLASS");
  public static final ResourceKey MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD = new ResourceKey("MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD");
  public static final ResourceKey MSG_METHOD_IS_NOT_STATIC = new ResourceKey("MSG_METHOD_IS_NOT_STATIC");
  public static final ResourceKey MSG_METHOD_IS_STATIC = new ResourceKey("MSG_METHOD_IS_STATIC");
  public static final ResourceKey MSG_NO_PROPERTY_DESCRIPTOR_FOUND = new ResourceKey("MSG_NO_PROPERTY_DESCRIPTOR_FOUND");
  public static final ResourceKey MSG_EXPECTING_MEMBER_ACCESS_PATH = new ResourceKey("MSG_EXPECTING_MEMBER_ACCESS_PATH");
  public static final ResourceKey MSG_TYPE_IS_NOT_ITERABLE = new ResourceKey("MSG_TYPE_IS_NOT_ITERABLE");
  public static final ResourceKey MSG_INTERVAL_MUST_BE_ITERABLE_HERE = new ResourceKey("MSG_INTERVAL_MUST_BE_ITERABLE_HERE");
  public static final ResourceKey MSG_EXTRA_DOT_FOUND_IN_INTERVAL = new ResourceKey("MSG_EXTRA_DOT_FOUND_IN_INTERVAL");
  public static final ResourceKey MSG_EXPECTING_NUMBER_TO_FOLLOW_DECIMAL = new ResourceKey("MSG_EXPECTING_NUMBER_TO_FOLLOW_DECIMAL");
  public static final ResourceKey MSG_IMPROPER_VALUE_FOR_NUMERIC_TYPE = new ResourceKey("MSG_IMPROPER_VALUE_FOR_NUMERIC_TYPE");
  public static final ResourceKey MSG_CANNOT_CREATE_NEW_ARRAY_FROM_TYPE_VAR = new ResourceKey("MSG_CANNOT_CREATE_NEW_ARRAY_FROM_TYPE_VAR");
  public static final ResourceKey MSG_NOT_A_VALID_EXCEPTION_TYPE = new ResourceKey("MSG_NOT_A_VALID_EXCEPTION_TYPE");
  public static final ResourceKey MSG_CANNOT_INFER_ARGUMENT_TYPE_AT_THIS_POSTION = new ResourceKey("MSG_CANNOT_INFER_ARGUMENT_TYPE_AT_THIS_POSTION");
  public static final ResourceKey MSG_OBSOLETE_CTOR_SYNTAX = new ResourceKey("MSG_OBSOLETE_CTOR_SYNTAX");
  public static final ResourceKey MSG_OBSOLETE_NOT_EQUAL_OP = new ResourceKey("MSG_OBSOLETE_NOT_EQUAL_OP");
  public static final ResourceKey MSG_MUST_BE_IN_OUTER_TO_CONSTRUCT_INNER = new ResourceKey("MSG_MUST_BE_IN_OUTER_TO_CONSTRUCT_INNER");
  public static final ResourceKey MSG_TYPE_DOES_NOT_HAVE_EMPTY_CONSTRUCTOR = new ResourceKey("MSG_TYPE_DOES_NOT_HAVE_EMPTY_CONSTRUCTOR");
  public static final ResourceKey MSG_ENHANCEMENT_DOES_NOT_ACCEPT_THIS_STATEMENT = new ResourceKey("MSG_ENHANCEMENT_DOES_NOT_ACCEPT_THIS_STATEMENT");
  public static final ResourceKey MSG_CANNOT_OVERRIDE_FUNCTIONS_IN_ENHANCEMENTS = new ResourceKey("MSG_CANNOT_OVERRIDE_FUNCTIONS_IN_ENHANCEMENTS");
  public static final ResourceKey MSG_CANNOT_OVERRIDE_PROPERTIES_IN_ENHANCEMENTS = new ResourceKey("MSG_CANNOT_OVERRIDE_PROPERTIES_IN_ENHANCEMENTS");
  public static final ResourceKey MSG_ENHANCED_TYPE_MUST_USE_ENHANCEMENT_TYPEVARS = new ResourceKey("MSG_ENHANCED_TYPE_MUST_USE_ENHANCEMENT_TYPEVARS");
  public static final ResourceKey MSG_ILLEGAL_USE_OF_MODIFIER = new ResourceKey("MSG_ILLEGAL_USE_OF_MODIFIER");
  public static final ResourceKey MSG_ILLEGAL_FORWARD_REFERENCE = new ResourceKey("MSG_ILLEGAL_FORWARD_REFERENCE");
  public static final ResourceKey MSG_ABSTRACT_MEMBER_NOT_IN_ABSTRACT_CLASS = new ResourceKey("MSG_ABSTRACT_MEMBER_NOT_IN_ABSTRACT_CLASS");
  public static final ResourceKey MSG_ENUM_CONSTRUCTOR_MUST_BE_PRIVATE = new ResourceKey("MSG_ENUM_CONSTRUCTOR_MUST_BE_PRIVATE");
  public static final ResourceKey MSG_ENUM_CONSTRUCTOR_NOT_ACCESSIBLE = new ResourceKey("MSG_ENUM_CONSTRUCTOR_NOT_ACCESSIBLE");
  public static final ResourceKey MSG_ENUM_MAY_NOT_HAVE_TYPEPARAM = new ResourceKey("MSG_ENUM_MAY_NOT_HAVE_TYPEPARAM");
  public static final ResourceKey MSG_MISSING_OVERRIDE_MODIFIER = new ResourceKey("MSG_MISSING_OVERRIDE_MODIFIER");
  public static final ResourceKey MSG_STATIC_METHOD_CANNOT_OVERRIDE = new ResourceKey("MSG_STATIC_METHOD_CANNOT_OVERRIDE");
  public static final ResourceKey MSG_NO_IMPLEMENTS_ALLOWED = new ResourceKey("MSG_NO_IMPLEMENTS_ALLOWED");
  public static final ResourceKey MSG_NO_EXTENDS_ALLOWED = new ResourceKey("MSG_NO_EXTENDS_ALLOWED");
  public static final ResourceKey MSG_FUNCTION_NOT_OVERRIDE = new ResourceKey("MSG_FUNCTION_NOT_OVERRIDE");
  public static final ResourceKey MSG_FUNCTION_NOT_OVERRIDE_PROPERTY = new ResourceKey("MSG_FUNCTION_NOT_OVERRIDE_PROPERTY");
  public static final ResourceKey MSG_SUBCLASS_OBJECT = new ResourceKey("MSG_SUBCLASS_OBJECT");
  public static final ResourceKey MSG_LIST_TO_ARRAYLIST_WARNING = new ResourceKey("MSG_LIST_TO_ARRAYLIST_WARNING");
  public static final ResourceKey MSG_NO_TYPE_ON_NAMESPACE = new ResourceKey("MSG_NO_TYPE_ON_NAMESPACE");
  public static final ResourceKey MSG_VAR_CASE_MISMATCH = new ResourceKey("MSG_VAR_CASE_MISMATCH");
  public static final ResourceKey MSG_FUNCTION_CASE_MISMATCH = new ResourceKey("MSG_FUNCTION_CASE_MISMATCH");
  public static final ResourceKey MSG_PROPERTY_CASE_MISMATCH = new ResourceKey("MSG_PROPERTY_CASE_MISMATCH");
  public static final ResourceKey MSG_TYPE_CASE_MISMATCH = new ResourceKey("MSG_TYPE_CASE_MISMATCH");
  public static final ResourceKey MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER = new ResourceKey("MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER");
  public static final ResourceKey MSG_NON_STATIC_ACCESS_WITH_IDENTIFIER_OF_STATIC_MEMBER = new ResourceKey("MSG_NON_STATIC_ACCESS_WITH_IDENTIFIER_OF_STATIC_MEMBER");
  public static final ResourceKey MSG_STATEMENT_ON_SAME_LINE = new ResourceKey("MSG_STATEMENT_ON_SAME_LINE");
  public static final ResourceKey MSG_MUST_BE_DEFINED_AS_CLASS = new ResourceKey("MSG_MUST_BE_DEFINED_AS_CLASS");
  public static final ResourceKey MSG_VOID_RETURN_IN_CTX_EXPECTING_VALUE = new ResourceKey("MSG_VOID_RETURN_IN_CTX_EXPECTING_VALUE");
  public static final ResourceKey MSG_NO_STATIC_CONSTRUCTOR = new ResourceKey("MSG_NO_STATIC_CONSTRUCTOR");
  public static final ResourceKey MSG_NO_ABSTRACT_METHOD_CALL_IN_CONSTR = new ResourceKey("MSG_NO_ABSTRACT_METHOD_CALL_IN_CONSTR");
  public static final ResourceKey MSG_AMBIGUOUS_METHOD_INVOCATION = new ResourceKey("MSG_AMBIGUOUS_METHOD_INVOCATION");
  public static final ResourceKey MSG_NUMBER_LITERAL_TOO_LARGE = new ResourceKey("MSG_NUMBER_LITERAL_TOO_LARGE");
  public static final ResourceKey MSG_RETURN_VAL_FROM_VOID_FUNCTION = new ResourceKey("MSG_RETURN_VAL_FROM_VOID_FUNCTION");
  public static final ResourceKey MSG_BAD_ANONYMOUS_CLASS_DECLARATION = new ResourceKey("MSG_BAD_ANONYMOUS_CLASS_DECLARATION");
  public static final ResourceKey MSG_ASSIGNMENT_IN_IF_STATEMENT = new ResourceKey("MSG_ASSIGNMENT_IN_IF_STATEMENT");
  public static final ResourceKey MSG_JAVA_STYLE_VARIABLE_DECLARATION = new ResourceKey("MSG_JAVA_STYLE_VARIABLE_DECLARATION");
  public static final ResourceKey MSG_NUMERIC_TYPE_EXPECTED = new ResourceKey("MSG_NUMERIC_TYPE_EXPECTED");
  public static final ResourceKey MSG_GETTER_CANNOT_HAVE_PARAMETERS = new ResourceKey("MSG_GETTER_CANNOT_HAVE_PARAMETERS");
  public static final ResourceKey MSG_BAD_TEMPLATE_DIRECTIVE = new ResourceKey("MSG_BAD_TEMPLATE_DIRECTIVE");
  public static final ResourceKey MSG_CLASSPATH_STATEMENT_EXPECTS_A_STRING = new ResourceKey("MSG_CLASSPATH_STATEMENT_EXPECTS_A_STRING");
  public static final ResourceKey MSG_COMMA_IS_THE_CLASSPATH_SEPARATOR = new ResourceKey("MSG_COMMA_IS_THE_CLASSPATH_SEPARATOR");
  public static final ResourceKey MSG_BITSHIFT_LHS_MUST_BE_INT_OR_LONG = new ResourceKey("MSG_BITSHIFT_LHS_MUST_BE_INT_OR_LONG");
  public static final ResourceKey MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG = new ResourceKey("MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG");
  public static final ResourceKey MSG_DELEGATION_NOT_ALLOWED_HERE = new ResourceKey( "MSG_DELEGATION_NOT_ALLOWED_HERE" );
  public static final ResourceKey MSG_DELEGATES_REPRESENT_INTERFACES_ONLY = new ResourceKey( "MSG_DELEGATES_REPRESENT_INTERFACES_ONLY" );
  public static final ResourceKey MSG_DELEGATES_SHOULD_NOT_SELF_DELEGATE = new ResourceKey( "MSG_DELEGATES_SHOULD_NOT_SELF_DELEGATE" );
  public static final ResourceKey MSG_CALLING_OVERRIDABLE_FROM_CTOR = new ResourceKey("MSG_CALLING_OVERRIDABLE_FROM_CTOR");
  public static final ResourceKey MSG_CLASS_DOES_NOT_IMPL = new ResourceKey( "MSG_CLASS_DOES_NOT_IMPL" );
  public static final ResourceKey MSG_EXPECTING_REPRESENTS = new ResourceKey( "MSG_EXPECTING_REPRESENTS" );
  public static final ResourceKey MSG_EXPECTING_LEFTPAREN_AGGREGATE = new ResourceKey( "MSG_EXPECTING_LEFTPAREN_AGGREGATE" );
  public static final ResourceKey MSG_EXPECTING_RIGHTPAREN_AGGREGATE = new ResourceKey( "MSG_EXPECTING_RIGHTPAREN_AGGREGATE" );
  public static final ResourceKey MSG_ONLY_INTERFACES_ALLOWED_HERE = new ResourceKey( "MSG_ONLY_INTERFACES_ALLOWED_HERE" );
  public static final ResourceKey MSG_ONLY_ONE_CLASS_IN_COMPONENT_TYPE = new ResourceKey( "MSG_ONLY_ONE_CLASS_IN_COMPONENT_TYPE" );
  public static final ResourceKey MSG_NO_ARRAY_IN_COMPONENT_TYPE = new ResourceKey( "MSG_NO_ARRAY_IN_COMPONENT_TYPE" );
  public static final ResourceKey MSG_NO_PRIMITIVE_IN_COMPONENT_TYPE = new ResourceKey( "MSG_NO_PRIMITIVE_IN_COMPONENT_TYPE" );
  public static final ResourceKey MSG_DYNAMIC_TYPE_NOT_ALLOWED_HERE = new ResourceKey( "MSG_DYNAMIC_TYPE_NOT_ALLOWED_HERE" );
  public static final ResourceKey MSG_COMPOUND_TYPE_NOT_ALLOWED_HERE = new ResourceKey( "MSG_COMPOUND_TYPE_NOT_ALLOWED_HERE" );
  public static final ResourceKey MSG_CANNOT_EXTEND_INTERNAL_JAVATYPE = new ResourceKey( "MSG_CANNOT_EXTEND_INTERNAL_JAVATYPE" );
  public static final ResourceKey MSG_ALREADY_CONTAINS_TYPE = new ResourceKey( "MSG_ALREADY_CONTAINS_TYPE" );
  public static final ResourceKey MSG_OVERRIDING_FUNCTION_MUST_HAVE_SAME_NUMBER_OF_TYPE_VARS = new ResourceKey( "MSG_OVERRIDING_FUNCTION_MUST_HAVE_SAME_NUMBER_OF_TYPE_VARS" );
  public static final ResourceKey MSG_AGGREGATES_MUST_CONTAIN_MORE = new ResourceKey( "MSG_AGGREGATES_MUST_CONTAIN_MORE" );
  public static final ResourceKey MSG_INTERFACE_REDUNDANT = new ResourceKey( "MSG_INTERFACE_REDUNDANT" );
  public static final ResourceKey MSG_NONTERMINAL_CASE_CLAUSE = new ResourceKey( "MSG_NONTERMINAL_CASE_CLAUSE" );
  public static final ResourceKey MSG_EXPECTING_LEFTPAREN_USING = new ResourceKey( "MSG_EXPECTING_LEFTPAREN_USING" );
  public static final ResourceKey MSG_EXPECTING_RIGHTPAREN_USING = new ResourceKey( "MSG_EXPECTING_RIGHTPAREN_USING" );
  public static final ResourceKey MSG_BAD_TYPE_FOR_USING_STMT = new ResourceKey( "MSG_BAD_TYPE_FOR_USING_STMT" );
  public static final ResourceKey MSG_UNNECESSARY_COERCION = new ResourceKey("MSG_UNNECESSARY_COERCION");
  public static final ResourceKey MSG_LATER_ASSIGNMENT_MAKES_EXPRESSION_ILLEGAL = new ResourceKey("MSG_LATER_ASSIGNMENT_MAKES_EXPRESSION_ILLEGAL");
  public static final ResourceKey MSG_ANONYMOUS_CLASS_NOT_ALLOWED_HERE = new ResourceKey( "MSG_ANONYMOUS_CLASS_NOT_ALLOWED_HERE" );
  public static final ResourceKey MSG_TEMPLATE_EXCEPTION_GENERATED_SOURCE = new ResourceKey("MSG_TEMPLATE_EXCEPTION_GENERATED_SOURCE");
  public static final ResourceKey MSG_TEMPLATE_MISSING_END_TAG_SCRIPTLET = new ResourceKey("MSG_TEMPLATE_MISSING_END_TAG_SCRIPTLET");
  public static final ResourceKey MSG_TEMPLATE_MISSING_END_TAG_EXPRESSION = new ResourceKey("MSG_TEMPLATE_MISSING_END_TAG_EXPRESSION");
  public static final ResourceKey MSG_TEMPLATE_MISSING_END_TAG_EXPRESSION_ALT = new ResourceKey("MSG_TEMPLATE_MISSING_END_TAG_EXPRESSION_ALT");
  public static final ResourceKey MSG_TEMPLATE_INVALID_PARAMS = new ResourceKey("MSG_TEMPLATE_INVALID_PARAMS");
  public static final ResourceKey MSG_TEMPLATE_MULTIPLE_PARAMS = new ResourceKey("MSG_TEMPLATE_MULTIPLE_PARAMS");
  public static final ResourceKey MSG_TEMPLATE_UNKNOWN_DIRECTIVE = new ResourceKey("MSG_TEMPLATE_UNKNOWN_DIRECTIVE");
  public static final ResourceKey MSG_EQUALS_FOR_INITIALIZER_EXPR = new ResourceKey( "MSG_EQUALS_FOR_INITIALIZER_EXPR" );
  public static final ResourceKey MSG_EXPECTING_NAME_VALUE_PAIR = new ResourceKey( "MSG_EXPECTING_NAME_VALUE_PAIR" );
  public static final ResourceKey MSG_REDUNTANT_INITIALIZERS = new ResourceKey( "MSG_REDUNTANT_INITIALIZERS" );
  public static final ResourceKey MSG_ONLY_ONE_COLON_IN_INITIALIZERS = new ResourceKey( "MSG_ONLY_ONE_COLON_IN_INITIALIZERS" );
  public static final ResourceKey MSG_EXPECTING_PROPERTY_NAME = new ResourceKey( "MSG_EXPECTING_PROPERTY_NAME" );
  public static final ResourceKey MSG_BLOCK_TYPES_SHOULD_HAVE_ARG_NAMES = new ResourceKey( "MSG_BLOCK_TYPES_SHOULD_HAVE_ARG_NAMES" );
  public static final ResourceKey MSG_DIMENSION_MULTIPLICATION_UNDEFINED = new ResourceKey( "MSG_DIMENSION_MULTIPLICATION_UNDEFINED" );
  public static final ResourceKey MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE = new ResourceKey( "MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE" );
  public static final ResourceKey MSG_DIMENSION_DIVIDE_SCALAR_BY_DIMENSION = new ResourceKey( "MSG_DIMENSION_DIVIDE_SCALAR_BY_DIMENSION" );
  public static final ResourceKey MSG_DIMENSION_MUST_BE_FINAL = new ResourceKey( "MSG_DIMENSION_MUST_BE_FINAL" );
  public static final ResourceKey MSG_RELATIONAL_OPERATOR_CANNOT_BE_APPLIED_TO_TYPE = new ResourceKey( "MSG_RELATIONAL_OPERATOR_CANNOT_BE_APPLIED_TO_TYPE" );
  public static final ResourceKey MSG_ASSIGNMENTS_MUST_BE_ENCLOSED_IN_CURLIES_IN_BLOCKS = new ResourceKey( "MSG_ASSIGNMENTS_MUST_BE_ENCLOSED_IN_CURLIES_IN_BLOCKS" );
  public static final ResourceKey MSG_BLOCKS_CAN_HAVE_A_MOST_SIXTEEN_ARGS = new ResourceKey( "MSG_BLOCKS_CAN_HAVE_A_MOST_SIXTEEN_ARGS" );
  public static final ResourceKey MSG_BLOCKS_LITERAL_NOT_ALLOWED_IN_NEW_EXPR = new ResourceKey( "MSG_BLOCKS_LITERAL_NOT_ALLOWED_IN_NEW_EXPR" );
  public static final ResourceKey MSG_STATEMENTS_MUST_BE_ENCLOSED_IN_CURLIES_IN_BLOCKS = new ResourceKey( "MSG_STATEMENTS_MUST_BE_ENCLOSED_IN_CURLIES_IN_BLOCKS" );
  public static final ResourceKey MSG_NOT_AN_ENHANCEABLE_TYPE = new ResourceKey( "MSG_NOT_AN_ENHANCEABLE_TYPE" );
  public static final ResourceKey MSG_CANNOT_REFERENCE_CLASS_TYPE_VAR_IN_STATIC_CONTEXT = new ResourceKey( "MSG_CANNOT_REFERENCE_CLASS_TYPE_VAR_IN_STATIC_CONTEXT" );
  public static final ResourceKey MSG_CANNOT_REFERENCE_THIS_IN_STATIC_CONTEXT = new ResourceKey( "MSG_CANNOT_REFERENCE_THIS_IN_STATIC_CONTEXT" );
  public static final ResourceKey MSG_CANNOT_CALL_NON_STATIC_METHOD_FROM_STATIC_CONTEXT = new ResourceKey( "MSG_CANNOT_CALL_NON_STATIC_METHOD_FROM_STATIC_CONTEXT" );
  public static final ResourceKey MSG_CANNOT_REFERENCE_NON_STATIC_PROPERTY_FROM_STATIC_CONTEXT = new ResourceKey( "MSG_CANNOT_REFERENCE_NON_STATIC_PROPERTY_FROM_STATIC_CONTEXT" );
  public static final ResourceKey MSG_ATTEMPTING_TO_ASSIGN_WEAKER_ACCESS_PRIVILEGES = new ResourceKey( "MSG_ATTEMPTING_TO_ASSIGN_WEAKER_ACCESS_PRIVILEGES" );
  public static final ResourceKey MSG_CTOR_HAS_XXX_ACCESS = new ResourceKey( "MSG_CTOR_HAS_XXX_ACCESS" );
  public static final ResourceKey MSG_TYPE_HAS_XXX_ACCESS = new ResourceKey( "MSG_TYPE_HAS_XXX_ACCESS" );
  public static final ResourceKey MSG_PRIMITIVE_TYPE_PARAM = new ResourceKey( "MSG_PRIMITIVE_TYPE_PARAM" );
  public static final ResourceKey USING_INTERNAL_CLASS = new ResourceKey( "USING_INTERNAL_CLASS" );
  public static final ResourceKey MSG_FUNCTION_DECL_MISSING = new ResourceKey( "MSG_FUNCTION_DECL_MISSING" );
  public static final ResourceKey MSG_EXPECTING_DEFAULT_VALUE = new ResourceKey( "MSG_EXPECTING_DEFAULT_VALUE" );
  public static final ResourceKey MSG_DEFAULT_VALUE_NOT_ALLOWED = new ResourceKey( "MSG_DEFAULT_VALUE_NOT_ALLOWED" );
  public static final ResourceKey MSG_COMPILE_TIME_CONSTANT_REQUIRED = new ResourceKey( "MSG_COMPILE_TIME_CONSTANT_REQUIRED" );
  public static final ResourceKey MSG_OVERLOADING_NOT_ALLOWED_WITH_OPTIONAL_PARAMS = new ResourceKey( "MSG_OVERLOADING_NOT_ALLOWED_WITH_OPTIONAL_PARAMS" );
  public static final ResourceKey MSG_MISSING_REQUIRED_ARGUMENTS = new ResourceKey( "MSG_MISSING_REQUIRED_ARGUMENTS" );
  public static final ResourceKey MSG_ARGUMENT_ALREADY_DEFINED = new ResourceKey( "MSG_ARGUMENT_ALREADY_DEFINED" );
  public static final ResourceKey MSG_EXPECTING_NAME_PARAM = new ResourceKey( "MSG_EXPECTING_NAME_PARAM" );
  public static final ResourceKey MSG_EXPECTING_NAMED_ARG = new ResourceKey( "MSG_EXPECTING_NAMED_ARG" );
  public static final ResourceKey MSG_PARAM_NOT_FOUND = new ResourceKey( "MSG_PARAM_NOT_FOUND" );
  public static final ResourceKey MSG_PARAMETERIZED_ARRAY_COMPONENT = new ResourceKey( "MSG_PARAMETERIZED_ARRAY_COMPONENT" );
  public static final ResourceKey MSG_CANNOT_EXTEND_RAW_GENERIC_TYPE = new ResourceKey( "MSG_CANNOT_EXTEND_RAW_GENERIC_TYPE" );
  public static final ResourceKey MSG_SUPER_CLASS_METHOD_NAME_SAME_AS_SUBCLASS = new ResourceKey( "MSG_SUPER_CLASS_METHOD_NAME_SAME_AS_SUBCLASS" );
  public static final ResourceKey MSG_EXPECTING_TYPE_TO_FOLLOW_PACKAGE_NAME = new ResourceKey( "MSG_EXPECTING_TYPE_TO_FOLLOW_PACKAGE_NAME" );
  public static final ResourceKey MSG_CANNOT_REFERENCE_OUTER_SYMBOL_WITHIN_ENHANCEMENTS = new ResourceKey( "MSG_CANNOT_REFERENCE_OUTER_SYMBOL_WITHIN_ENHANCEMENTS" );
  public static final ResourceKey MSG_CANNOT_REFERENCE_ENCLOSING_PROPERTIES_WITHIN_ENHANCEMENTS = new ResourceKey( "MSG_CANNOT_REFERENCE_ENCLOSING_PROPERTIES_WITHIN_ENHANCEMENTS" );
  public static final ResourceKey MSG_CANNOT_REFERENCE_ENCLOSING_METHODS_WITHIN_ENHANCEMENTS = new ResourceKey( "MSG_CANNOT_REFERENCE_ENCLOSING_METHODS_WITHIN_ENHANCEMENTS" );
  public static final ResourceKey MSG_NO_TYPE_AFTER_CONSTRUCTOR = new ResourceKey( "MSG_NO_TYPE_AFTER_CONSTRUCTOR" );
  public static final ResourceKey MSG_INVALID_LITERAL = new ResourceKey( "MSG_INVALID_LITERAL" );
  public static final ResourceKey MSG_LOSS_OF_PRECISION_IN_NUMERIC_LITERAL = new ResourceKey( "MSG_LOSS_OF_PRECISION_IN_NUMERIC_LITERAL");
  public static final ResourceKey MSG_PROPERTIES_MUST_AGREE_ON_STATIC_MODIFIERS = new ResourceKey( "MSG_PROPERTIES_MUST_AGREE_ON_STATIC_MODIFIERS");
  public static final ResourceKey MSG_PROPERTIES_MUST_AGREE_ON_TYPE = new ResourceKey( "MSG_PROPERTIES_MUST_AGREE_ON_TYPE");
  public static final ResourceKey MSG_PROPERTY_OVERRIDES_WITH_INCOMPATIBLE_TYPE = new ResourceKey( "MSG_PROPERTY_OVERRIDES_WITH_INCOMPATIBLE_TYPE");
  public static final ResourceKey MSG_NO_ENCLOSING_INSTANCE_IN_SCOPE = new ResourceKey( "MSG_NO_ENCLOSING_INSTANCE_IN_SCOPE" );
  public static final ResourceKey MSG_EXPECTING_CONDITION_FOR_ASSERT = new ResourceKey( "MSG_EXPECTING_CONDITION_FOR_ASSERT" );
  public static final ResourceKey MSG_EXPECTING_MESSAGE_FOR_ASSERT = new ResourceKey( "MSG_EXPECTING_MESSAGE_FOR_ASSERT" );
  public static final ResourceKey MSG_ASSERTIONS_NOT_ALLOWED_HERE = new ResourceKey( "MSG_ASSERTIONS_NOT_ALLOWED_HERE" );
  public static final ResourceKey MSG_ARRAY_NOT_SUPPORTED = new ResourceKey( "MSG_ARRAY_NOT_SUPPORTED" );
  public static final ResourceKey MSG_IMPROPER_USE_OF_KEYWORD = new ResourceKey( "MSG_IMPROPER_USE_OF_KEYWORD" );
  public static final ResourceKey MSG_USES_STMT_CONFLICT = new ResourceKey( "MSG_USES_STMT_CONFLICT" );
  public static final ResourceKey MSG_USES_STMT_DUPLICATE = new ResourceKey( "MSG_USES_STMT_DUPLICATE" );
  public static final ResourceKey MSG_PARAM_TYPE_CANT_BE_INFERRED_FROM_LATE_BOUND_EXPRESSION = new ResourceKey( "MSG_PARAM_TYPE_CANT_BE_INFERRED_FROM_LATE_BOUND_EXPRESSION" );

  public static final ResourceKey MSG_FL_EXPECTING_FEATURE_NAME = new ResourceKey( "MSG_FL_EXPECTING_FEATURE_NAME");
  public static final ResourceKey MSG_FL_EXPECTING_RIGHT_CARET = new ResourceKey( "MSG_FL_EXPECTING_RIGHT_CARET");
  public static final ResourceKey MSG_FL_GENERIC_FUNCTION_REFERENCES_NOT_YET_SUPPORTED = new ResourceKey( "MSG_FL_GENERIC_FUNCTION_REFERENCES_NOT_YET_SUPPORTED");
  public static final ResourceKey MSG_FL_EXPECTING_RIGHT_PAREN = new ResourceKey( "MSG_FL_EXPECTING_RIGHT_PAREN");
  public static final ResourceKey MSG_FL_CONSTRUCTOR_NOT_FOUND = new ResourceKey( "MSG_FL_CONSTRUCTOR_NOT_FOUND");
  public static final ResourceKey MSG_FL_METHOD_NOT_FOUND = new ResourceKey( "MSG_FL_METHOD_NOT_FOUND");
  public static final ResourceKey MSG_FL_PROPERTY_NOT_FOUND = new ResourceKey( "MSG_FL_PROPERTY_NOT_FOUND");
  public static final ResourceKey MSG_FL_STATIC_FEATURES_MUST_BE_REFERENCED_FROM_THEIR_TYPES = new ResourceKey( "MSG_FL_STATIC_FEATURES_MUST_BE_REFERENCED_FROM_THEIR_TYPES");


  public static final ResourceKey MSG_ARRAY_INDEX_MUST_BE_INT = new ResourceKey( "MSG_ARRAY_INDEX_MUST_BE_INT");

  public static final ResourceKey MSG_AMBIGUOUS_SYMBOL_REFERENCE = new ResourceKey( "MSG_AMBIGUOUS_SYMBOL_REFERENCE");

  // Web service errors
  public static final ResourceKey WSDL_DUPLICATE_WEB_SERVICE = new ResourceKey("WSDL_DUPLICATE_WEB_SERVICE");
  public static final ResourceKey WSDL_DUPLICATE_METHOD = new ResourceKey("WSDL_DUPLICATE_METHOD");
  public static final ResourceKey WSDL_UNSUPPORTED_TYPE = new ResourceKey("WSDL_UNSUPPORTED_TYPE");
  public static final ResourceKey WSDL_NOT_WEBSERVICE = new ResourceKey("WSDL_NOT_WEBSERVICE");
  public static final ResourceKey WSDL_REFERENCE_ANNOTATION = new ResourceKey("WSDL_REFERENCE_ANNOTATION");
  public static final ResourceKey WSDL_REFERENCE_ABSTRACT_CLASS = new ResourceKey("WSDL_REFERENCE_ABSTRACT_CLASS");
  public static final ResourceKey WSDL_REFERENCE_INTERFACE = new ResourceKey("WSDL_REFERENCE_INTERFACE");
  public static final ResourceKey WSDL_REFERENCE_TRANSACTION_TYPE = new ResourceKey("WSDL_REFERENCE_TRANSACTION_TYPE");
  public static final ResourceKey WSDL_REFERENCE_LIST = new ResourceKey("WSDL_REFERENCE_LIST");
  public static final ResourceKey WSDL_REFERENCE_MAP = new ResourceKey("WSDL_REFERENCE_MAP");
  public static final ResourceKey WSDL_REFERENCE_COLLECTION = new ResourceKey("WSDL_REFERENCE_COLLECTION");
  public static final ResourceKey WSDL_REFERENCE_PARAMETERIZED_TYPE = new ResourceKey("WSDL_REFERENCE_PARAMETERIZED_TYPE");
  public static final ResourceKey WSDL_REFERENCE_GENERIC_TYPE = new ResourceKey("WSDL_REFERENCE_GENERIC_TYPE");
  public static final ResourceKey WSDL_REFERENCE_UNSUPPORTED_NAMESPACE = new ResourceKey("WSDL_REFERENCE_UNSUPPORTED_NAMESPACE");
  public static final ResourceKey WSDL_REFERENCE_CLASS_WITHOUT_DEFAULT_CONSTRUCTOR = new ResourceKey("WSDL_REFERENCE_CLASS_WITHOUT_DEFAULT_CONSTRUCTOR");
  public static final ResourceKey WSDL_REFERENCE_WEBSERVICE = new ResourceKey("WSDL_REFERENCE_WEBSERVICE"); //Web Service as a referenced type in a web service.
  public static final ResourceKey WSDL_REFERENCE_SOAP_TYPE = new ResourceKey("WSDL_REFERENCE_SOAP_TYPE");  //Soap type as a refrenced type in a web service.
  public static final ResourceKey WSDL_REFERENCE_XML_TYPE = new ResourceKey("WSDL_REFERENCE_XML_TYPE");
  public static final ResourceKey WSDL_READONLY_PROPERTIES = new ResourceKey("WSDL_READONLY_PROPERTIES");
  public static final ResourceKey WSDL_WRITEONLY_PROPERTIES = new ResourceKey("WSDL_WRITEONLY_PROPERTIES");
  public static final ResourceKey WSDL_REFERENCE_CAUSES_NAMESPACE_COLLISION = new ResourceKey("WSDL_REFERENCE_CAUSES_NAMESPACE_COLLISION");
  public static final ResourceKey MSG_CANNOT_READ_A_WRITE_ONLY_PROPERTY = new ResourceKey( "MSG_CANNOT_READ_A_WRITE_ONLY_PROPERTY" );
  public static final ResourceKey MSG_APPLICATION_MODIFIER_HAS_BEEN_DEPRECATED = new ResourceKey( "MSG_APPLICATION_MODIFIER_HAS_BEEN_DEPRECATED" );
  public static final ResourceKey MSG_REQUEST_MODIFIER_HAS_BEEN_DEPRECATED = new ResourceKey( "MSG_REQUEST_MODIFIER_HAS_BEEN_DEPRECATED" );
  public static final ResourceKey MSG_SESSION_MODIFIER_HAS_BEEN_DEPRECATED = new ResourceKey( "MSG_SESSION_MODIFIER_HAS_BEEN_DEPRECATED" );
  public static final ResourceKey MSG_EXECUTION_MODIFIER_HAS_BEEN_DEPRECATED = new ResourceKey( "MSG_EXECUTION_MODIFIER_HAS_BEEN_DEPRECATED" );
  public static final ResourceKey MSG_CALL_TO_SUPER_THIS_MUST_BE_FIRST = new ResourceKey( "MSG_CALL_TO_SUPER_THIS_MUST_BE_FIRST" );
  public static final ResourceKey MSG_COMPILE_TIME_ANNOTATION_FAILED_TO_EXECUTE = new ResourceKey( "MSG_COMPILE_TIME_ANNOTATION_FAILED_TO_EXECUTE" );
  public static final ResourceKey MSG_METHOD_REIFIES_TO_SAME_SIGNATURE_AS_ANOTHER_METHOD = new ResourceKey("MSG_METHOD_REIFIES_TO_SAME_SIGNATURE_AS_ANOTHER_METHOD");
  public static final ResourceKey MSG_PROPERTY_AND_FUNCTION_CONFLICT = new ResourceKey("MSG_PROPERTY_AND_FUNCTION_CONFLICT");
  public static final ResourceKey MSG_PROPERTY_AND_FUNCTION_CONFLICT_UPON_REIFICATION = new ResourceKey("MSG_PROPERTY_AND_FUNCTION_CONFLICT_UPON_REIFICATION");
  public static final ResourceKey MSG_CANNOT_OVERRIDE_FUNCTION_FROM_ENHANCEMENT = new ResourceKey("MSG_CANNOT_OVERRIDE_FUNCTION_FROM_ENHANCEMENT");
  public static final ResourceKey MSG_MASKING_ENHANCEMENT_METHODS_MAY_BE_CONFUSING = new ResourceKey("MSG_MASKING_ENHANCEMENT_METHODS_MAY_BE_CONFUSING");
  public static final ResourceKey MSG_STRING_COERCION_ON_RHS_OF_ADDITIVE_EXPRESSION_MUST_BE_PARENTHESIZED = new ResourceKey( "MSG_STRING_COERCION_ON_RHS_OF_ADDITIVE_EXPRESSION_MUST_BE_PARENTHESIZED" );
  public static final ResourceKey MSG_EXPLICIT_TYPE_RECOMMENDED_FOR_CATCH_STMTS = new ResourceKey( "MSG_EXPLICIT_TYPE_RECOMMENDED_FOR_CATCH_STMTS");
  public static final ResourceKey MSG_SUPER_NOT_ACCESSIBLE_FROM_BLOCK= new ResourceKey("MSG_SUPER_NOT_ACCESSIBLE_FROM_BLOCK");
  public static final ResourceKey MSG_GENERIC_PROPERTIES_NOT_SUPPORTED = new ResourceKey( "MSG_GENERIC_PROPERTIES_NOT_SUPPORTED" );
  public static final ResourceKey MSG_GENERIC_ANNOTATIONS_NOT_SUPPORTED = new ResourceKey( "MSG_GENERIC_ANNOTATIONS_NOT_SUPPORTED" );
  public static final ResourceKey MSG_CATCH_STMT_CANNOT_EXECUTE = new ResourceKey( "MSG_CATCH_STMT_CANNOT_EXECUTE" );
  public static final ResourceKey MSG_IMONITOR_LOCK_SHOULD_ONLY_BE_USED_WITHIN_USING_STMTS = new ResourceKey( "MSG_IMONITOR_LOCK_SHOULD_ONLY_BE_USED_WITHIN_USING_STMTS" );
  public static final ResourceKey MSG_STATIC_MODIFIER_NOT_ALLOWED_HERE = new ResourceKey( "MSG_STATIC_MODIFIER_NOT_ALLOWED_HERE" );
  public static final ResourceKey MSG_CANNOT_INSTANTIATE_NON_STATIC_CLASSES_HERE = new ResourceKey( "MSG_CANNOT_INSTANTIATE_NON_STATIC_CLASSES_HERE" );
  public static final ResourceKey MSG_WARN_ON_SUSPICIOUS_THIS_COMPARISON = new ResourceKey( "MSG_WARN_ON_SUSPICIOUS_THIS_COMPARISON" );
  public static final ResourceKey MSG_ANY = new ResourceKey("MSG_ANY");
  public static final String ERROR = "##Error";
  public static final ResourceKey WS_ERR_Annotation_Duplicate_Namespace = new ResourceKey("WS_ERR_Annotation_Duplicate_Namespace");
  public static final ResourceKey WS_ERR_Annotation_Exception = new ResourceKey("WS_ERR_Annotation_Exception");
  public static final ResourceKey WS_ERR_Annotation_Invalid_Namespace = new ResourceKey("WS_ERR_Annotation_Invalid_Namespace");
  public static final ResourceKey WS_ERR_Annotation_Only_For_WebService = new ResourceKey("WS_ERR_Annotation_Only_For_WebService");
  public static final ResourceKey WS_ERR_Annotation_Operation_Duplicate = new ResourceKey("WS_ERR_Annotation_Operation_Duplicate");
  public static final ResourceKey WS_ERR_Annotation_Operation_Response_Reserved = new ResourceKey("WS_ERR_Annotation_Operation_Response_Reserved");
  public static final ResourceKey WS_ERR_Annotation_Operation_OneWay_Invalid = new ResourceKey("WS_ERR_Annotation_Operation_OneWay_Invalid");
  public static final ResourceKey WS_ERR_Can_Not_Marshal = new ResourceKey("WS_ERR_Can_Not_Marshal");
  public static final ResourceKey WS_ERR_Export_Inner_Only_On_WebService   = new ResourceKey("WS_ERR_Export_Inner_Only_On_WebService");
  public static final ResourceKey WS_ERR_Export_Inner_Not_Name_Of_Method   = new ResourceKey("WS_ERR_Export_Inner_Not_Name_Of_Method");
  public static final ResourceKey WS_ERR_Export_Inner_Not_Name_Of_Response = new ResourceKey("WS_ERR_Export_Inner_Not_Name_Of_Response");
  public static final ResourceKey WS_ERR_Export_No_Constructor = new ResourceKey("WS_ERR_Export_No_Constructor");
  public static final ResourceKey WS_ERR_Export_No_Extends = new ResourceKey("WS_ERR_Export_No_Extends");
  public static final ResourceKey WS_ERR_Export_Not_Final = new ResourceKey("WS_ERR_Export_Not_Final");
  public static final ResourceKey WS_ERR_Export_Not_WebService = new ResourceKey("WS_ERR_Export_Not_WebService");
  public static final ResourceKey WS_ERR_Export_Recursive = new ResourceKey("WS_ERR_Export_Recursive");
  public static final ResourceKey WS_WARN_Annontation_Some_Generators_Dont_Support_Schema = new ResourceKey("WS_WARN_Annontation_Some_Generators_Dont_Support_Schema");
  public static final ResourceKey WS_WARN_Annontation_Not_Available = new ResourceKey("WS_WARN_Annontation_Not_Available");
  public static final ResourceKey WS_WARN_Ignored = new ResourceKey("WS_WARN_Ignored");

  static
  {
    g_resources = ResourceBundle.getBundle( gw.lang.parser.resources.Strings.class.getName() );

    if( g_resources == null )
    {
      throw new RuntimeException( "Parser: Failure loading Strings.proerties" );
    }
  }

  /**
   * @param key the resource key to get
   * @param args the object to insert into the message
   * @return The String mapped to the specified key. If the key is not mapped
   *         or an exception ocurrs during the attempt to access the String, returns
   *         "#Error".
   */
  public static String get( ResourceKey key, Object... args )
  {
    try
    {
      String strRes = g_resources.getString( key.getKey() );
      if( args != null && args.length > 0 )
      {
        strRes = MessageFormat.format( strRes, args );
      }
      return strRes;
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }

    return ERROR;
  }

  /**
   * @param key the key to check
   * @return true if the specified key is mapped.
   */
  public static boolean exists( ResourceKey key ) {
    try {
      return g_resources.getString( key.getKey() ) != null;
    }
    catch ( MissingResourceException ex ) {
      return false;
    }

  }

}

/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.internal.gosu.parser.Expression;

public class JavaSourceDefaultValue {
  public static final JavaSourceDefaultValue NULL = new JavaSourceDefaultValue(null, "No default value.");
  private JavaSourceAnnotationMethod _method;
  private String _text;
  Expression _expr;

  public JavaSourceDefaultValue(JavaSourceAnnotationMethod method, String value) {
    _method = method;
    _text = value;
  }

  public String toString() {
    return _text;
  }

  public String getValue() {
    return _text;
  }

  public Expression parse() {
    if (_expr == null) {
      _expr = (Expression)CompileTimeExpressionParser.parse( _text, _method.getEnclosingClass(), _method.getReturnType() );
    }
    return _expr;
  }

  public Object evaluate() {
    Expression expr = parse();
    return expr.evaluate();
  }}

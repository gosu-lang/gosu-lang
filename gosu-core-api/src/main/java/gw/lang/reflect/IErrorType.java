/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.IExpression;

import java.util.List;

public interface IErrorType extends INonLoadableType
{
  String NAME = "ErrorType";

  String getErrantTypeName();

  ParseResultsException getError();

  IFunctionType getErrorTypeFunctionType( IExpression[] eArgs, String strMethod, List listAllMatchingMethods );

  IConstructorType getErrorTypeConstructorType( IExpression[] eArgs, List listAllMatchingMethods );
}

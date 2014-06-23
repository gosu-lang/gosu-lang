/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.parser.exceptions.IEvaluationException;

import java.io.PrintStream;
import java.io.PrintWriter;

public class EvaluationException extends IEvaluationException
{
  private String _additionalDetails;

  public EvaluationException( String message )
  {
    super( message );
  }

  private EvaluationException( Throwable t )
  {
    super( t );
  }

  /**
   * @deprecated This method is here to support the historical runtime semantics, and should not be used in standard
   * gosu (it is used in code gen, see gw.internal.gosu.ir.transform.statement.TryCatchFinallyStatementTransformer#wrapCatchSymbol(gw.lang.ir.IRExpression)
   */
  @Deprecated()
  public static RuntimeException wrap( Throwable cause )
  {
    if( cause instanceof EvaluationException )
    {
      return (EvaluationException)cause;
    }
    else
    {
      return new EvaluationException( cause );
    }
  }

  public String getMessage()
  {
    String strMsg = super.getMessage();
    if (_additionalDetails != null) {
      strMsg = strMsg + "\nAdditional Context Information:\n" + _additionalDetails;
    }
    return strMsg;
  }

  public void printStackTrace( PrintStream print )
  {
    if (_additionalDetails != null) {
      print.println("\nAdditional Context Information:");
      print.println(_additionalDetails);
    }
    super.printStackTrace( print );
  }

  public void printStackTrace( PrintWriter print )
  {
    if (_additionalDetails != null) {
      print.println("\nAdditional Context Information:");
      print.println(_additionalDetails);
    }
    super.printStackTrace( print );
  }

  public void setAdditionalDetails(String additionalDetails) {
    _additionalDetails = additionalDetails;
  }

}

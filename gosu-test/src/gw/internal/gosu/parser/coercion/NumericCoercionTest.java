/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.coercion;

import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.IType;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

/**
 */
public class NumericCoercionTest extends TestClass
{
  public void testCoercion() throws ParseResultsException
  {
    IType[] types =
    {
      JavaTypes.pBYTE(),
      JavaTypes.pCHAR(),
      JavaTypes.pSHORT(),
      JavaTypes.pINT(),
      JavaTypes.pLONG(),
      JavaTypes.pFLOAT(),
      JavaTypes.pDOUBLE(),

      JavaTypes.BYTE(),
      JavaTypes.CHARACTER(),
      JavaTypes.SHORT(),
      JavaTypes.INTEGER(),
      JavaTypes.LONG(),
      JavaTypes.FLOAT(),
      JavaTypes.DOUBLE(),

      JavaTypes.BIG_INTEGER(),
      JavaTypes.BIG_DECIMAL(),
    };


    boolean[][] matrix =
    {
    //             pBYTE, pCHAR, pSHORT, pINT,  pLONG, pFLOAT, pDOUBLE,  BYTE,  CHAR,  SHORT, INT,   LONG,  FLOAT, DOUBLE, BI,    BD
    /* pBYTE */  { false, true,  true,   true,  true,  true,   true,     false, true,  true,  true,  true,  true,  true,   true,  true },
    /* pCHAR */  { true,  false, true,   true,  true,  true,   true,     true,  false, true,  true,  true,  true,  true,   true,  true },
    /* pSHORT */ { false, true,  false,  true,  true,  true,   true,     false, true,  false, true,  true,  true,  true,   true,  true },
    /* pINT */   { false, false, false,  false, true,  true,   true,     false, false, false, false, true,  true,  true,   true,  true },
    /* pLONG */  { false, false, false,  false, false, true,   true,     false, false, false, false, false, true,  true,   true,  true },
    /* pFLOAT */ { false, false, false,  false, false, false,  true,     false, false, false, false, false, false, true,   true,  true },
    /* pDOUBLE */{ false, false, false,  false, false, false,  false,    false, false, false, false, false, false, false,  true,  true },
    /* BYTE */   { false, true,  true,   true,  true,  true,   true,     false, true,  true,  true,  true,  true,  true,   true,  true },
    /* CHAR */   { true,  false, true,   true,  true,  true,   true,     true,  false, true,  true,  true,  true,  true,   true,  true },
    /* SHORT */  { false, true,  false,  true,  true,  true,   true,     false, true,  false, true,  true,  true,  true,   true,  true },
    /* INT */    { false, false, false,  false, true,  true,   true,     false, false, false, false, true,  true,  true,   true,  true },
    /* LONG */   { false, false, false,  false, false, true,   true,     false, false, false, false, false, true,  true,   true,  true },
    /* FLOAT */  { false, false, false,  false, false, false,  true,     false, false, false, false, false, false, true,   true,  true },
    /* DOUBLE */ { false, false, false,  false, false, false,  false,    false, false, false, false, false, false, false,  true,  true },
    /* BI */     { false, false, false,  false, false, true,   true,     false, false, false, false, false, true,  true,   false, true },
    /* BD */     { false, false, false,  false, false, false,  false,    false, false, false, false, false, false, false,  false, false },
    };

    for( int i = 0; i < types.length; i++ )
    {
      IType toType = types[i];
      for( int j = 0; j < types.length; j++ )
      {
        IType fromType = types[j];
        IExpression e = null;
        try
        {
          e = GosuTestUtil.compileExpression( "var x : " + fromType.getName() + "\n" +
                       "var v : " + toType.getName() + " = x" );
        }
        catch( ParseResultsException e1 )
        {
          e = (IExpression)e1.getParsedElement();
        }
        boolean bHasCoercionWarning = false;
        for( IParseIssue w : e.getParseExceptions() )
        {
          if( bHasCoercionWarning = w.getMessageKey() == Res.MSG_IMPLICIT_COERCION_ERROR )
          {
            break;
          }
        }
        boolean bShouldHaveCoercionWarning = matrix[i][j];
        if( bShouldHaveCoercionWarning != bHasCoercionWarning )
        {
          System.out.println("Failed at from " + types[i].getName() + ", to = " + types[j].getName() );
          System.out.println(" Was " + bHasCoercionWarning);
          System.out.println(" Should be " + bShouldHaveCoercionWarning);
        }
        assertEquals( bShouldHaveCoercionWarning, bHasCoercionWarning );
      }
    }
  }
}

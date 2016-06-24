/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.ir.transform.AbstractElementTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.template.TemplateGenerator;
import gw.lang.ir.IRStatement;
import gw.lang.parser.IStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuProgram;
import gw.util.GosuStringUtil;

/**
 */
public abstract class AbstractStatementTransformer<T extends IStatement> extends AbstractElementTransformer<T> {
  public AbstractStatementTransformer( TopLevelTransformationContext cc, T parsedElem ) {
    super( cc, parsedElem );
  }

  public T _stmt() {
    return getParsedElement();
  }

  protected final IRStatement compile() {
    IRStatement irStmt = compile_impl();
    visitStatementLineNumber( irStmt );

    return irStmt;
  }

  protected abstract IRStatement compile_impl();

  protected void visitStatementLineNumber( IRStatement irStatement ) {
    visitStatementLineNumber( irStatement, _stmt() );
  }

  protected void visitStatementLineNumber( IRStatement irStatement, IStatement stmt ) {
    if( irStatement != null ) {
      int lineNumber = stmt.getLineNum();
      lineNumber = maybeGetTemplateLineNumber( stmt, lineNumber );
      irStatement.setLineNumber( lineNumber );

      //## todo: this should be turned on only in cases where it's needed
      if( lineNumber > 0 ) {
        irStatement.setOriginalSourceStatement( _cc().getSourceFileRef() + ":" + lineNumber + " " + _cc().getSourceLine( lineNumber ) );
      }
    }
  }

  private int maybeGetTemplateLineNumber( IStatement stmt, int lineNumber )
  {
    if( lineNumber <= 0 )
    {
      return lineNumber;
    }

    IGosuClass gsClass = stmt.getGosuClass();
    if( gsClass instanceof IGosuProgram )
    {
      IType contextType = ((IGosuProgram)gsClass).getContextType();
      if( contextType != null )
      {
        try
        {
          String source = gsClass.getSource();
          int iIndex = GosuStringUtil.getIndexForLineNumber( source, lineNumber );
          int iTemplateLineIndex = source.indexOf( TemplateGenerator.TEMPLATE_LINE_NUMBER, iIndex );
          if( iTemplateLineIndex > 0 )
          {
            String strLine = source.substring( iTemplateLineIndex + TemplateGenerator.TEMPLATE_LINE_NUMBER.length() + 1, source.indexOf( '\r', iTemplateLineIndex ) );
            lineNumber = Integer.parseInt( strLine );
          }
        }
        catch( Exception e )
        {
          // don't care
        }
      }
    }
    return lineNumber;
  }
}

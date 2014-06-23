/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.expressions.ITemplateStringLiteral;
import gw.lang.parser.expressions.IProgram;
import gw.lang.parser.template.ITemplateGenerator;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.lang.parser.template.TemplateParseException;

import java.util.List;

public class TemplateStringLiteral extends Expression implements ITemplateStringLiteral {
  private ITemplateGenerator _template;

  public TemplateStringLiteral(ITemplateGenerator template) {
    _template = template;
    setType( GosuParserTypes.STRING_TYPE() );
  }

  public Object evaluate() {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    throw new CannotExecuteGosuException();
  }

  public IProgram getProgram()
  {
    if (_template.isValid()) {
      return _template.getProgram();
    } else {
      String s = "";
      List<TemplateParseException> parseExceptions = _template.getTemplateSyntaxProblems();
      for (TemplateParseException parseException : parseExceptions) {
        s += parseException.getParseException().getMessage() + "\n";
      }
      throw new RuntimeException( "String Template failed to parse: \n" +
                                  this + 
                                  "\nOn class: " + getGosuClass().getName() + 
                                  "\n Parse Errors:\n" +
                                  s );
    }
  }

  @Override
  public String toString() {
    return "\"" + _template.toString() + "\"";
  }

}

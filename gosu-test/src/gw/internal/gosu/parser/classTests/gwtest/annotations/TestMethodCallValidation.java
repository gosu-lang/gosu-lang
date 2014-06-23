/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.classTests.gwtest.annotations;

import gw.lang.parser.IParsedElement;
import gw.lang.parser.IUsageSiteValidator;
import gw.lang.parser.resources.Res;

class TestMethodCallValidation implements IUsageSiteValidator
{  
  public void validate( IParsedElement pe )
  {
    pe.addParseException( Res.MSG_ANY, "Go Gosu" );
  }
}

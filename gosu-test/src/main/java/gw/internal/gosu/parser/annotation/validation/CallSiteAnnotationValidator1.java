/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.annotation.validation;

import gw.lang.parser.IParsedElement;
import gw.lang.parser.IUsageSiteValidator;
import gw.lang.parser.resources.Res;

public class CallSiteAnnotationValidator1 implements IUsageSiteValidator {

  public void validate(IParsedElement pe) {
    pe.addParseException(Res.MSG_ANY, "Screw you, guys");
  }

}

/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.parser.template.ITemplateGenerator;
import gw.util.fingerprint.FP64;

public interface ITemplateType extends IGosuProgram
{
  ITemplateGenerator getTemplateGenerator();

  ISourceFileHandle getSourceFileHandle();
}

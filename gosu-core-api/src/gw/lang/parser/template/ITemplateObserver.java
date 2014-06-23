/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.template;

import gw.lang.GosuShop;
import gw.lang.reflect.IType;

import java.io.Writer;

public interface ITemplateObserver {

  public boolean beforeTemplateRender(IType type, Writer writer);
  public StringEscaper getEscaper();
  public void afterTemplateRender(IType type, Writer writer);

  public static final ITemplateObserverManager MANAGER = GosuShop.makeTemplateObserverManager();

  interface ITemplateObserverManager {
    public void pushTemplateObserver(ITemplateObserver observer);
    public void popTemplateObserver();
  }
}

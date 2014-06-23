/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.template;

import gw.lang.parser.template.ITemplateObserver;

public class TemplateObserverAccess implements ITemplateObserver.ITemplateObserverManager {
  @Override
  public void pushTemplateObserver(ITemplateObserver observer) {
    TemplateRenderer.pushTemplateObserver(observer);
  }
  @Override
  public void popTemplateObserver() {
    TemplateRenderer.popTemplateObserver();
  }
}

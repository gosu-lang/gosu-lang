/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

public interface IActivationContext
{
  Object getContext();

  int getCurrentScopeIndex();

  void setCurrentScopeIndex( int iIndex );

  int getCurrentPrivateGlobalScopeIndex();

  void setCurrentPrivateGlobalScopeIndex( int iIndex );

  String getLabel();

  boolean isTransparent();
}

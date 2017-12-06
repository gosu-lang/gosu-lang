/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.regression;

import gw.lang.SimplePropertyProcessing;

@SimplePropertyProcessing
public class JavaClassWithDollarSignSimpleProperties {
  public static final String $100 = "$100";
  
  public String getfoo() { 
    return null;
  }
  
  public void setfoo(String s) {}  
  
}

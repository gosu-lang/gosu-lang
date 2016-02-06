/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.regression;

public class JavaClassWithPublicFieldAndMatchingPublicGetter {
  public JavaClassWithPublicFieldAndMatchingPublicGetter() {
    AField = 42;
  }

  public Integer AField;
  public Integer getAField() {
    return AField + 1;
  }
}

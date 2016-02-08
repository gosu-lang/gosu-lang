/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler.sample.annotations;

@InheritedJavaAnnotation
public class JavaSuperWithAnnotations {

  @InheritedJavaAnnotation
  public JavaSuperWithAnnotations()
  {
  }

  @InheritedJavaAnnotation
  public void methodWithAnnotation() {
  }

  @InheritedJavaAnnotation
  public String getPropertyWithAnnotation() {
    return null;
  }
}

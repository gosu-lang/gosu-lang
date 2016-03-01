/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler.sample.annotations;

@InheritedJavaAnnotation(42)
public class JavaSubWithAnnotations extends JavaSuperWithAnnotations
{

  @InheritedJavaAnnotation(42)
  public JavaSubWithAnnotations()
  {
  }

  @InheritedJavaAnnotation(42)
  public void methodWithAnnotation() {
  }

  @InheritedJavaAnnotation(42)
  public String getPropertyWithAnnotation() {
    return null;
  }
}

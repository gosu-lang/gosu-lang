/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.annotation;

/**
 * User: dbrewster
* Date: May 11, 2007
* Time: 1:17:31 PM
*/
public @interface TestClassAnnotationWithArgsEndpoint {

  public enum AuthenticationTypes {
    None,
    Http
  }

  //　TODO cgross - use a local resource that satisfies this critiera
  AuthenticationTypes authType() default AuthenticationTypes.None;

  int callTimeout() default 0;

  String name();

  String password() default "";

  String userName() default "";

  String wsdlLocation();
}

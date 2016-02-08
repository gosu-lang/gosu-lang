/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.regression;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: 9/13/11
 * Time: 11:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class PL17056JavaClass {

  public static <T> BindResult<T> bind(Class<T> arg) {
    return new BindResult<T>();
  }

  public static class BindResult<T> {

  }
}

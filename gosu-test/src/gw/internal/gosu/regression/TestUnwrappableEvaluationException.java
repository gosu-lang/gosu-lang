/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.regression;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Feb 2, 2010
 * Time: 10:51:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestUnwrappableEvaluationException extends RuntimeException
{
  public static void throwException() {
    throw new TestUnwrappableEvaluationException();
  }
}

/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.testloader;

import java.util.concurrent.Callable;

public class BasicClass implements Callable<String>, Runnable {

  private String _str;
  private int _int;

  private String _staticStr;
  private int _staticInt;

  public String getString() {
    return _str;
  }

  public void setString(String str) {
    _str = str;
  }

  public int getInt() {
    return _int;
  }

  public void setInt(int anInt) {
    _int = anInt;
  }
  
  public String functionThatGetsString() {
    return getString();
  }

  public int functionThatGetsInt() {
    return getInt();
  }

  public String getStaticString() {
    return _staticStr;
  }

  public void setStaticString(String str) {
    _staticStr = str;
  }

  public int getStaticInt() {
    return _staticInt;
  }

  public void setStaticInt(int anInt) {
    _staticInt = anInt;
  }
  
  public String functionThatGetsStaticString() {
    return getStaticString();
  }

  public int functionThatGetsStaticInt() {
    return getStaticInt();
  }

  @Override
  public String call() throws Exception {
    return getString();
  }

  @Override
  public void run() {
  }
}

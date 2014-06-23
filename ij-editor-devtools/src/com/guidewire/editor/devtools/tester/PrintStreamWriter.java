/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.tester;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;

public class PrintStreamWriter extends PrintStream {
  protected StringWriter _writer;
  protected OutListener listener;

  public PrintStreamWriter(StringWriter writer) {
    super(new ByteArrayOutputStream());
    _writer = writer;
  }

  public void setListener(OutListener listener) {
    this.listener = listener;
  }

  @Override
  public void print(boolean b) {
    print(String.valueOf(b));
  }

  @Override
  public void print(char c) {
    print(String.valueOf(c));
  }

  @Override
  public void print(int i) {
    print(String.valueOf(i));
  }

  @Override
  public void print(long l) {
    print(String.valueOf(l));
  }

  @Override
  public void print(float f) {
    print(String.valueOf(f));
  }

  @Override
  public void print(double d) {
    print(String.valueOf(d));
  }

  @Override
  public void print(char[] s) {
    print(String.valueOf(s));
  }

  @Override
  public void print(String strOutput) {
    _writer.write(strOutput);
    if (listener != null) {
      listener.notifyNewText(strOutput);
    }
  }

  @Override
  public void print(Object obj) {
    print(String.valueOf(obj));
  }

  @Override
  public void println() {
    print("\n");
  }

  @Override
  public void println(boolean b) {
    println(String.valueOf(b));
  }

  @Override
  public void println(char c) {
    println(String.valueOf(c));
  }

  @Override
  public void println(int i) {
    println(String.valueOf(i));
  }

  @Override
  public void println(long l) {
    println(String.valueOf(l));
  }

  @Override
  public void println(float f) {
    println(String.valueOf(f));
  }

  @Override
  public void println(double d) {
    println(String.valueOf(d));
  }

  @Override
  public void println(char[] s) {
    println(String.valueOf(s));
  }

  @Override
  public void println(String strOutput) {
    print(strOutput + "\n");
  }

  @Override
  public void println(Object obj) {
    println(String.valueOf(obj));
  }

  @Override
  public void write(byte[] buf, int off, int len) {
    print(new String(buf, off, len));
  }

  @Override
  public void write(int b) {
    print(b);
  }
}

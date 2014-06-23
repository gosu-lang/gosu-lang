/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;

public class JavaStringObject implements JavaFileObject {
  StringReader javaSource;
  String source;

  public JavaStringObject(String source) {
    this.source = source;
    javaSource = new StringReader(source);
  }

  @Override
  public Kind getKind() {
    return Kind.SOURCE;
  }

  @Override
  public boolean isNameCompatible(String simpleName, Kind kind) {
    return false;
  }

  @Override
  public NestingKind getNestingKind() {
    return null;
  }

  @Override
  public Modifier getAccessLevel() {
    return null;
  }


  @Override
  public URI toUri() {
    return null;
  }

  @Override
  public String getName() {
    return "JavaStringObject";
  }

  @Override
  public InputStream openInputStream() throws IOException {
    return new ByteArrayInputStream(source.getBytes(Charset.forName("UTF-8")));
  }

  @Override
  public OutputStream openOutputStream() throws IOException {
    throw new IllegalStateException();
  }

  @Override
  public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
    return javaSource;
  }

  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
    return source;
  }

  @Override
  public Writer openWriter() throws IOException {
    throw new IllegalStateException();
  }

  @Override
  public long getLastModified() {
    return 0;
  }

  @Override
  public boolean delete() {
    return true;
  }
}

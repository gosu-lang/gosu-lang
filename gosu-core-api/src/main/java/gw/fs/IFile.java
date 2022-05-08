/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs;

import gw.lang.Deprecated;
import gw.lang.UnstableAPI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@UnstableAPI
public interface IFile extends IResource {
  IFile[] EMPTY_ARRAY = new IFile[0];

  default String getContent() throws IOException {
    try (InputStream is = openInputStream()) {
      return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }
  }

  @Deprecated("Use #getContent to read the contents instead of rolling your own")
  InputStream openInputStream() throws IOException;

  OutputStream openOutputStream() throws IOException;

  OutputStream openOutputStreamForAppend() throws IOException;

  String getExtension();

  String getBaseName();
}

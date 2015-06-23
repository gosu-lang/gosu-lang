/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.filesystem;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ex.ApplicationManagerEx;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import gw.fs.IFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class IDEAFile extends IDEAResource implements IFile
{
  Charset charset = StandardCharsets.UTF_8;

  public IDEAFile(VirtualFile file) {
    super(file);
    if (file != null && file.isCharsetSet()) {
      charset = file.getCharset();
    }
  }

  public IDEAFile(String file) {
    super(file);
    if (_virtualFile != null && _virtualFile.isCharsetSet()) {
      charset = _virtualFile.getCharset();
    }
  }

  @Nullable
  @Override
  public InputStream openInputStream() throws IOException {
    String temporaryBuffer = getTemporaryBuffer(this);
    if (temporaryBuffer != null) {
      return new ByteArrayInputStream(temporaryBuffer.getBytes( charset ));
    } else {
      return _virtualFile != null ? _virtualFile.getInputStream() : new FileInputStream(new File(_path));
    }
  }

  @Nullable
  private String getTemporaryBuffer(@NotNull IDEAFile file) {
    final VirtualFile virtualFile = file.getVirtualFile();

    // we're getting the cached documents since getDocument() forces PSI creating which will cause deadlock !!!
    if (virtualFile != null && !virtualFile.getFileType().isBinary()) {
      final Document document = FileDocumentManager.getInstance().getCachedDocument(virtualFile);
      final String[] result = new String[1];
      if (document != null) {
        if (ApplicationManagerEx.getApplicationEx().tryRunReadAction(new Runnable() {
          @Override
          public void run() {
            result[0] = document.getText();
          }
        })) {
          return result[0];
        } else {
          return document.getCharsSequence().toString();
        }
      }
    }

    return null;
  }

  @Nullable
  @Override
  public OutputStream openOutputStream() throws IOException {
    return _virtualFile != null ? _virtualFile.getOutputStream(this) : null;
  }

  @Nullable
  @Override
  public OutputStream openOutputStreamForAppend() throws IOException {
    return _virtualFile != null ? _virtualFile.getOutputStream(this) : null;
  }

  @Nullable
  @Override
  public String getExtension() {
    return _virtualFile != null ? _virtualFile.getExtension() : FileUtil.getExtension(_path);
  }

  @NotNull
  @Override
  public String getBaseName() {
    if (_virtualFile != null) {
     return _virtualFile.getNameWithoutExtension();
    } else {
      String name = _path.substring(_path.lastIndexOf('/') + 1);
      name = name.substring(0, name.lastIndexOf('.'));
      return name;
    }
  }

  protected VirtualFile create(@NotNull final VirtualFile virtualFile, @NotNull final String name) throws IOException {
    final VirtualFile[] result = new VirtualFile[1];
    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      @Override
      public void run() {
        try {
          result[0] = virtualFile.createChildData(this, name);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

    });
    return result[0];
  }
}

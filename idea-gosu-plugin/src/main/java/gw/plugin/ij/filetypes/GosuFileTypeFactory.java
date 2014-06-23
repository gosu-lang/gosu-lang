/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.filetypes;

import com.google.common.base.Joiner;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class GosuFileTypeFactory extends FileTypeFactory {
  @Override
  public void createFileTypes(@NotNull FileTypeConsumer consumer) {
    String extensions = Joiner.on(';').join(GosuCodeFileTypesManager.INSTANCE.getRegisteredExtensions());
    consumer.consume(GosuCodeFileType.INSTANCE, extensions);
  }
}

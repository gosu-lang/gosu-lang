/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.gs.IFileSystemGosuClassRepository;

public class FileSource implements ISource {
  private IFileSystemGosuClassRepository.IClassFileInfo _file;
  private ISourceCodeTokenizer _tokenizer;

  public FileSource(IFileSystemGosuClassRepository.IClassFileInfo file) {
    _file = file;
  }

  public String getSource() {
    return _file.getContent();
  }

  @Override
  public void stopCachingSource() {
    _file.stopCachingContent();
  }

  @Override
  public ISourceCodeTokenizer getTokenizer() {
    return _tokenizer;
  }
  @Override
  public void setTokenizer( ISourceCodeTokenizer tokenizer ) {
    _tokenizer = tokenizer;
  }
}

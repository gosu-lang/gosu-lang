/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.stubs.PsiFileStubImpl;
import com.intellij.psi.tree.IStubFileElementType;
import com.intellij.util.io.StringRef;
import gw.plugin.ij.lang.parser.GosuCodeParserDefinition;
import gw.plugin.ij.lang.psi.IGosuFile;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import org.jetbrains.annotations.NotNull;

public class GosuFileStub extends PsiFileStubImpl<IGosuFile> {
  private StringRef _packageName;
  private StringRef _name;
  private StringRef _ext;

  public GosuFileStub(IGosuFile file) {
    super(file);
  }

  public GosuFileStub(StringRef packName, StringRef name, StringRef ext) {
    super(null);
    _packageName = packName;
    _name = name;
    _ext = ext;
  }

  @NotNull
  public IStubFileElementType getType() {
    return GosuCodeParserDefinition.GOSU_FILE;
  }

  public StringRef getPackageName() {
    if (_packageName == null) {
      _packageName = StringRef.fromString(getPsi().getPackageName());
    }
    return _packageName;
  }

  public StringRef getName() {
    if (_name == null) {
      _name = StringRef.fromString(StringUtil.trimEnd(getPsi().getName(), "." + getExt().getString()));
    }
    return _name;
  }

  @NotNull
  public String getTypeName() {
    return getPackageName().getString() + '.' + getName().getString();
  }

  public StringRef getExt() {
    if (_ext == null) {
      _ext = StringRef.fromString(((AbstractGosuClassFileImpl) getPsi()).getFileExtension());
    }
    return _ext;
  }
}

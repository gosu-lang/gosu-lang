/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor.signature;

import com.intellij.psi.PsiType;

public interface ChangeSignatureHandler {

  public static final ChangeSignatureHandler NULL = new ChangeSignatureHandler() {
    public void afterRefactoring() {
    }

    public String replaceReturnType(String type) {
      return type;
    }

    public ParamInfo replaceParameter(ParamInfo info) {
      return info;
    }
  };

  void afterRefactoring();

  public static class ParamInfo {
    public final PsiType type;
    public final String paramName;

    public ParamInfo(PsiType type, String paramName) {
      this.type = type;
      this.paramName = paramName;
    }
  }

  String replaceReturnType(String type);

  ParamInfo replaceParameter(ParamInfo paramShowUpInfo);
}

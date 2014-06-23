/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.util.NotNullLazyValue;
import gw.lang.GosuShop;
import gw.lang.parser.IGosuValidator;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public class GosuParserConfigurer {
  private static final NotNullLazyValue<IGosuParserConfigurer[]> providers = new NotNullLazyValue<IGosuParserConfigurer[]>() {
    @NotNull
    @Override
    protected IGosuParserConfigurer[] compute() {
      final GosuParserConfigurerExtensionBean[] extensions = Extensions.getExtensions(GosuParserConfigurerExtensionBean.EP_NAME);
      final IGosuParserConfigurer[] providers = new IGosuParserConfigurer[extensions.length];
      for (int i = 0; i < extensions.length; i++) {
        providers[i] = extensions[i].getHandler();
      }
      return providers;
    }
  };

  @Nullable
  public static ISymbolTable getSymbolTable(AbstractGosuClassFileImpl psiFile) {
    for (IGosuParserConfigurer provider : providers.getValue()) {
      final ISymbolTable symbolTable = provider.getSymbolTable(psiFile);
      if (symbolTable != null) {
        return symbolTable;
      }
    }
    return null;
  }

  @Nullable
  public static ITypeUsesMap getTypeUsesMap(AbstractGosuClassFileImpl psiFile) {
    for (IGosuParserConfigurer provider : providers.getValue()) {
      final ITypeUsesMap typeUsesMap = provider.getTypeUsesMap(psiFile);
      if (typeUsesMap != null) {
        return typeUsesMap;
      }
    }
    return null;
  }

  @Nullable
  public static IGosuValidator getValidator(AbstractGosuClassFileImpl psiFile) {
    for (IGosuParserConfigurer provider : providers.getValue()) {
      final IGosuValidator validator = provider.getValidator(psiFile);
      if (validator != null) {
        return validator;
      }
    }
    return null;
  }
}

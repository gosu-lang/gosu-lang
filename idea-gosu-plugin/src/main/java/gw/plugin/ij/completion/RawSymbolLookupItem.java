/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion;

import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupItem;
import gw.lang.GosuShop;
import gw.lang.parser.ISymbol;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import org.jetbrains.annotations.NotNull;

public class RawSymbolLookupItem extends LookupItem {
  @NotNull
  private final ISymbol _symbol;
  private final IModule _module;

  public RawSymbolLookupItem(@NotNull ISymbol symbol, IModule module) {
    super(symbol, symbol.getDisplayName());
    _symbol = symbol;
    _module = module;
  }

  private String getLookupString(IFunctionType funcType) {
    final StringBuilder sb = new StringBuilder(funcType.getDisplayName());
    sb.append("(");
    final String[] parameterNames = funcType.getParameterNames();
    for (int i = 0; i < parameterNames.length; i++) {
      if (i != 0) {
        sb.append(", ");
      }
      sb.append(parameterNames[i]);
      if (i < funcType.getParameterTypes().length) {
        sb.append(": ");
        sb.append(funcType.getParameterTypes()[i].getRelativeName());
      }
    }
    sb.append(")");
    return sb.toString();
  }

  @Override
  public void renderElement(@NotNull LookupElementPresentation presentation) {
    IType type = _symbol.getType();
    TypeSystem.pushModule(_module);
    try {
      final String lookupString;
      if (type instanceof IFunctionType && !(type instanceof IBlockType)) {
        final IFunctionType funcType = (IFunctionType) type;
        type = funcType.getReturnType();
        lookupString = getLookupString(funcType);
      } else {
        lookupString = getLookupString();
      }

      presentation.setItemText(lookupString);
      presentation.setTypeText(type.getRelativeName());
    } finally {
      TypeSystem.popModule(_module);
    }
  }
}

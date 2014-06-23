/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang;

import com.intellij.psi.tree.IElementType;
import gw.lang.parser.IParsedElement;
import org.jetbrains.annotations.NotNull;

public class GosuElementType extends IElementType implements IGosuElementType{
  private Class<? extends IParsedElement> peType;

  public GosuElementType(@NotNull String strDebugName) {
    super(strDebugName, GosuLanguage.instance());
  }

  public GosuElementType(@NotNull Class<? extends IParsedElement> peType) {
    this(getDebugName(peType));
    this.peType = peType;
  }

  public static String getDebugName(@NotNull Class<? extends IParsedElement> peType) {
    String simpleName = peType.getSimpleName();
    if (simpleName.startsWith("I")) {
      simpleName = simpleName.substring(1);
    }
    return simpleName;
  }

  public Class<? extends IParsedElement> getParsedElementType() {
    return peType;
  }
}

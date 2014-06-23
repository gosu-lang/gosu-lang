/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.plugin.ij.util;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.IProgram;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.gs.IGosuClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParseTreeUtil {


  @Nullable
  public static IGosuClass getContainingGosuClass(@NotNull IParsedElement pe) {
    if (pe instanceof IClassFileStatement) {
      return pe.getGosuClass();
    }
    while (pe != null) {
      if (pe instanceof IClassStatement) {
        return pe.getGosuClass();
      }
      if (pe instanceof IProgram) {
        return pe.getGosuProgram();
      }
      pe = pe.getParent();
    }
    return null;
  }

  public static void dumpParseTree(@NotNull IParsedElement pe, String indent) {
    System.out.println(indent + pe.getClass().getSimpleName() + " " + getRange(pe) + ": " + pe.toString().replace("\n", " "));
    for (IParseTree child : pe.getLocation().getChildren()) {
      dumpParseTree(child.getParsedElement(), indent + "  ");
    }
  }

  @NotNull
  private static String getRange(@NotNull IParsedElement pe) {
    return "(" + pe.getLocation().getOffset() + ":" + pe.getLocation().getExtent() + ")";
  }

  public static void dumpPsi(@NotNull PsiElement psi) {
    dumpPsi(psi, "");
  }

  public static void dumpPsi(@NotNull PsiElement psi, String indent) {
    System.out.println(indent + psi);
    for (PsiElement child : psi.getChildren()) {
      dumpPsi(child, indent + "  ");
    }
  }

  public static void dumpParseTree(@NotNull IClassFileStatement cfs) {
    System.out.println("------------------------------------");
    dumpParseTree(cfs, "");
  }

  public static <T> T lookupAncestor(IParsedElement e, Class<T> ancestorType) {
    return ancestorType.cast(e.findAncestorParsedElementByType(ancestorType));
  }

  public static <T> T lookupLastAncestor(IParsedElement e, Class<T> type) {
    IParsedElement result = null;
    while (e != null) {
      if (type.isInstance(e)) {
        result = e;
      }
      e = e.getParent();
    }
    return type.cast(result);
  }

  public static TextRange rangeFor(IParsedElement pe) {
    IParseTree location = pe.getLocation();
    if (location != null) {
      return TextRange.from(location.getOffset(), location.getLength());
    }
    return null;
  }
}

/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.model;

import com.google.common.base.Strings;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class MemberNameUtil {
  public static String buildFunctionIntellisenseString(@NotNull IFunctionType functionType) {
    return buildFunctionIntellisenseString(functionType, null);
  }

  public static String buildFunctionIntellisenseString(@NotNull IFunctionType functionType, String[] paramNames) {
    final StringBuilder sb = new StringBuilder(functionType.getDisplayName());
    sb.append("(");
    buildArgListFromType(functionType, sb, true, paramNames);
    sb.append(")");
    return sb.toString();
  }

  private static void buildArgListFromType(@NotNull IFunctionType functionType, @NotNull StringBuilder sb, boolean topLevel, @Nullable String[] paramNames) {
    IType[] parameters = functionType.getParameterTypes();
    HashSet<String> generatedNames = new HashSet<>();
    for (int i = 0; i < parameters.length; i++) {
      if (i != 0) {
        sb.append(", ");
      }

      IType paramType = parameters[i];
      if (topLevel && paramType instanceof IFunctionType) {
        IFunctionType blockType = (IFunctionType) paramType;
        sb.append("\\");

        buildArgListFromType(blockType, sb, false, null);

        sb.append(" -> ");
      } else {
        // If we have a method info, we can use the actual parameter name
        String name;
        if (paramNames != null) {
          name = paramNames[i];
        } else if (functionType.getMethodInfo() != null) {
          IParameterInfo info = functionType.getMethodInfo().getParameters()[i];
          name = info.getName();
        } else {
          name = createUniqueParamNameFromType(paramType, generatedNames);
        }
        sb.append(uncapitalize(name));
      }
    }
  }

  static String createUniqueParamNameFromType(@NotNull IType paramType, @NotNull HashSet<String> generatedNames) {
    String initialName = paramType.getRelativeName();
    if (!isAlphanumeric(initialName.substring(0, 1))) {
      initialName = paramType.getDisplayName();
    }
    String name = initialName.substring(0, 1).toLowerCase();
    int j = 2;
    while (generatedNames.contains(name)) {
      name = initialName + j;
      j++;
    }
    generatedNames.add(name);
    return name;
  }

  @NotNull
  public static String uncapitalize(@NotNull String str) {
    return Strings.isNullOrEmpty(str) ? "" : Character.toLowerCase(str.charAt(0)) + str.substring(1);
  }

  public static boolean isAlphanumeric(@Nullable String str) {
    if (str == null) {
      return false;
    }

    for (int i = 0; i < str.length(); i++) {
      if (!Character.isLetterOrDigit(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

}

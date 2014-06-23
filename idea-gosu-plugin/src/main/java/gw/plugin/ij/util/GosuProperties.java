/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.JavaTypes;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuProperties {
  // Setter
  public static boolean isSetter(@NotNull PsiMethod method) {
    return getSetterName(method) != null;
  }

  @Nullable
  public static String getSetterName(@NotNull PsiMethod method) {
    if (!method.hasModifierProperty(PsiModifier.STATIC)) {
      final String name = method.getName();
      if (method instanceof IGosuMethod) {
        if (((IGosuMethod) method).isForPropertySetter()) {
          return name;
        }
      } else {
        if (method.getParameterList().getParametersCount() == 1) {
          if (name.startsWith("set") && PsiType.VOID.equals( method.getReturnType() ) && isPropertyMethod( method ) )  {
            return name.substring(3);
          }
        }
      }
    }
    return null;
  }

  private static boolean isPropertyMethod(final PsiMethod method) {
    try {
      return
        ExecutionUtil.execute(new SafeCallable<Boolean>(method) {
          public Boolean execute() throws Exception {
            PsiClass containingClass = method.getContainingClass();
            if (containingClass == null) {
              return true;
            }
            IType type = TypeSystem.getByFullNameIfValid(containingClass.getQualifiedName());
            if (type == null) {
              return true;
            }
            IGosuClass gsClass = IGosuClassInternal.Util.getGosuClassFrom(type);
            if (gsClass != null) {
              for (IMethodInfo mi : gsClass.getTypeInfo().getMethods(gsClass)) {
                if (mi.getDisplayName().equals(method.getName()) && mi.getParameters().length == 1 && mi.getReturnType() == JavaTypes.pVOID()) {
                  // This is a Method not a Property in the Proxy class for the Java type
                  return false;
                }
              }
            }
            return true;
          }
        });
    }
    catch( NullPointerException e ) {
      // gulp (PL-28324)
      return true;
    }
  }

  // Getter
  public static boolean isGetter(@NotNull PsiMethod method) {
    return getGetterName(method) != null;
  }

  @Nullable
  public static String getGetterName(@NotNull PsiMethod method) {
    if (!method.hasModifierProperty(PsiModifier.STATIC)) {
      final String name = method.getName();
      if (method instanceof IGosuMethod) {
        if (((IGosuMethod) method).isForPropertyGetter()) {
          return name;
        }
      } else {
        if (method.getParameterList().getParametersCount() == 0) {
          if (name.startsWith("get") && name.length() > 3) {
            return name.substring(3);
          }

          if (name.startsWith("is") && name.length() > 2 && PsiType.BOOLEAN.equals(method.getReturnType())) {
            return name.substring(2);
          }
        }
      }
    }
    return null;
  }
}

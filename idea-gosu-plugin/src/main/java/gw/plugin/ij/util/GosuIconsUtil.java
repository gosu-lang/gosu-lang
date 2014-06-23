/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.psi.impl.ElementBase;
import com.intellij.psi.util.PsiUtil;
import com.intellij.ui.RowIcon;
import com.intellij.util.PlatformIcons;
import com.intellij.util.ui.EmptyIcon;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuTypeDefinitionImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * This class exist does icon computation in a much faster way that the corresponding IJ code by using the
 * TypeSystem rather that the PSI. This avoids definition parsing a lot of types especially for the open type dialog.
 */
public class GosuIconsUtil {
  // these constants are coped from ElementPresentationUtil
  private static final int FLAGS_ABSTRACT = 0x100;
  private static final int FLAGS_STATIC = 0x200;
  private static final int FLAGS_FINAL = 0x400;
  private static final int FLAGS_JUNIT_TEST = 0x2000;
  private static final int FLAGS_RUNNABLE = 0x4000;

  private GosuIconsUtil() {
  }

  // replacemnt of method from ElementPresentationUtil
  public static int getFlags(@NotNull GosuTypeDefinitionImpl definiton, @NotNull IGosuClass gosuClass, final boolean isLocked) {
    final boolean isEnum = definiton.isEnum();
    int flags = (gosuClass.isFinal() && !isEnum ? FLAGS_FINAL : 0)
        | (gosuClass.isStatic() && !isEnum ? FLAGS_STATIC : 0)
        | (isLocked ? ElementBase.FLAGS_LOCKED : 0);
    if (gosuClass.isAbstract() && !definiton.isInterface()) {
      flags |= FLAGS_ABSTRACT;
    }

    IModule module = gosuClass.getTypeLoader().getModule();
    TypeSystem.pushModule(module);
    try {
      IType testCase = TypeSystem.getByFullNameIfValid("junit.framework.TestCase", TypeSystem.getGlobalModule());
      if (gosuClass.getAllTypesInHierarchy().contains(testCase)) {
        flags |= FLAGS_JUNIT_TEST;
      } else if (gosuClass.getAllTypesInHierarchy().contains(JavaTypes.RUNNABLE())) {
        flags |= FLAGS_RUNNABLE;
      }
    } finally {
      TypeSystem.popModule(module);
    }
    return flags;
  }

  // these methods are copied from VisibilityIcons
  public static void setVisibilityIcon(@Nullable IGosuClass gosuClass, @NotNull RowIcon baseIcon) {
    if (gosuClass != null) {
      if (Modifier.isPublic(gosuClass.getModifiers())) {
        setVisibilityIcon(PsiUtil.ACCESS_LEVEL_PUBLIC, baseIcon);
      } else if (Modifier.isPrivate(gosuClass.getModifiers())) {
        setVisibilityIcon(PsiUtil.ACCESS_LEVEL_PRIVATE, baseIcon);
      } else if (Modifier.isProtected(gosuClass.getModifiers())) {
        setVisibilityIcon(PsiUtil.ACCESS_LEVEL_PROTECTED, baseIcon);
      } else if (Modifier.isInternal(gosuClass.getModifiers())) {
        setVisibilityIcon(PsiUtil.ACCESS_LEVEL_PACKAGE_LOCAL, baseIcon);
      } else {
        Icon emptyIcon = new EmptyIcon(PlatformIcons.PUBLIC_ICON.getIconWidth(), PlatformIcons.PUBLIC_ICON.getIconHeight());
        baseIcon.setIcon(emptyIcon, 1);
      }
    } else if (PlatformIcons.PUBLIC_ICON != null) {
      Icon emptyIcon = new EmptyIcon(PlatformIcons.PUBLIC_ICON.getIconWidth(), PlatformIcons.PUBLIC_ICON.getIconHeight());
      baseIcon.setIcon(emptyIcon, 1);
    }
  }

  public static void setVisibilityIcon(int accessLevel, @NotNull RowIcon baseIcon) {
    Icon icon;
    switch (accessLevel) {
      case PsiUtil.ACCESS_LEVEL_PUBLIC:
        icon = PlatformIcons.PUBLIC_ICON;
        break;
      case PsiUtil.ACCESS_LEVEL_PROTECTED:
        icon = PlatformIcons.PROTECTED_ICON;
        break;
      case PsiUtil.ACCESS_LEVEL_PACKAGE_LOCAL:
        icon = PlatformIcons.PACKAGE_LOCAL_ICON;
        break;
      case PsiUtil.ACCESS_LEVEL_PRIVATE:
        icon = PlatformIcons.PRIVATE_ICON;
        break;
      default:
        if (PlatformIcons.PUBLIC_ICON != null) {
          icon = new EmptyIcon(PlatformIcons.PUBLIC_ICON.getIconWidth(), PlatformIcons.PUBLIC_ICON.getIconHeight());
        } else {
          return;
        }
    }
    baseIcon.setIcon(icon, 1);
  }
}

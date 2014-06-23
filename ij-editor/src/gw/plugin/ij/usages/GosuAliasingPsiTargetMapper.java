/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.usages;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Computable;
import com.intellij.pom.PomTarget;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.targets.AliasingPsiTarget;
import com.intellij.psi.targets.AliasingPsiTargetMapper;
import gw.lang.parser.Keyword;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GosuAliasingPsiTargetMapper implements AliasingPsiTargetMapper {
  private Set<AliasingPsiTarget> getMethodTargets(@NotNull final PsiMethod method) {
    return ApplicationManager.getApplication().runReadAction(new Computable<Set<AliasingPsiTarget>>() {
      @Override
      public Set<AliasingPsiTarget> compute() {
        final List<String> aliasNames;
        if (method.isConstructor()) {
          aliasNames = ImmutableList.of(Keyword.KW_construct.getName(), Keyword.KW_this.getName(), Keyword.KW_super.getName());
        } else {
          aliasNames = getGosuPropertyNames(method);
        }

        final Set<AliasingPsiTarget> aliases = Sets.newHashSet();
        for (String propName : aliasNames) {
          final GosuAliasingPsiTarget alias = new GosuAliasingPsiTarget(method);
          alias.setName(propName);
          aliases.add(alias);
        }
        return aliases;
      }
    });
  }

  private Set<AliasingPsiTarget> getClassTargets(@NotNull final PsiClass klass) {
    final String qualifiedName = ApplicationManager.getApplication().runReadAction(new Computable<String>() {
      @Nullable
      public String compute() {
        return klass.getQualifiedName();
      }
    });

    IModule rootModule = GosuModuleUtil.getGlobalModule(klass.getProject());
    TypeSystem.pushModule( rootModule );
    try {
      final Set<AliasingPsiTarget> aliases = Sets.newHashSet();
      if (TypeSystem.getDefaultType().getType().getName().equals(qualifiedName)) {
        final GosuAliasingPsiTarget alias = new GosuAliasingPsiTarget(klass);
        alias.setName("Type");
        aliases.add(alias);
      }
      return aliases;
    }
    finally {
      TypeSystem.popModule( rootModule );
    }
  }

  @NotNull
  @Override
  public Set<AliasingPsiTarget> getTargets(@NotNull final PomTarget target) {
    if (target instanceof PsiMethod) {
      return getMethodTargets((PsiMethod) target);
    } else if (target instanceof PsiClass) {
      return getClassTargets((PsiClass) target);
    }

    return Collections.emptySet();
  }

  // TODO: use GosuProperties class here somehow
  @NotNull
  public static List<String> getGosuPropertyNames(@NotNull final PsiMethod method) {
    List<String> propNames = new ArrayList<>();
    String name = method.getName();
    int nameLen = name.length();
    String returnTypeName = ApplicationManager.getApplication().runReadAction(new Computable<String>() {
      @Override
      public String compute() {
        return method.getReturnType().getCanonicalText();
      }
    });
    if (nameLen > 3 && name.startsWith("get") &&
        !returnTypeName.equals("void") &&
        method.getParameterList().getParametersCount() == 0) {
      propNames.add(name.substring(3));
      propNames.add("is" + name.substring(3));
    } else if (nameLen > 3 && name.startsWith("set") &&
        returnTypeName.equals("void") &&
        method.getParameterList().getParametersCount() == 1) {
      propNames.add(name.substring(3));
    } else if (nameLen > 2 && name.startsWith("is") &&
        (returnTypeName.equals("boolean") ||
            returnTypeName.equals("java.lang.Boolean")) &&
        method.getParameterList().getParametersCount() == 0) {
      propNames.add(name.substring(2));
      propNames.add("get" + name.substring(2));
    } else if (method instanceof IGosuMethod && ((IGosuMethod) method).isForProperty()) {
      if (returnTypeName.equals("void")) {
        propNames.add("set" + name);
      } else {
        propNames.add("get" + name);
        propNames.add("is" + name);
      }
    }
    return propNames;
  }
}

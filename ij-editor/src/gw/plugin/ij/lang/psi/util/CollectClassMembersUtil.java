/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.util;

import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Trinity;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.infos.CandidateInfo;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.psi.util.TypeConversionUtil;
import com.intellij.util.containers.HashMap;
import com.intellij.util.containers.HashSet;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifierList;
import gw.plugin.ij.lang.psi.api.statements.IGosuField;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectClassMembersUtil {
  private static final Key<CachedValue<Trinity<Map<String, CandidateInfo>, Map<String, List<CandidateInfo>>, Map<String, CandidateInfo>>>> CACHED_MEMBERS = Key.create("CACHED_CLASS_MEMBERS");

  private static final Key<CachedValue<Trinity<Map<String, CandidateInfo>, Map<String, List<CandidateInfo>>, Map<String, CandidateInfo>>>> CACHED_MEMBERS_INCLUDING_SYNTHETIC = Key.create("CACHED_MEMBERS_INCLUDING_SYNTHETIC");

  private CollectClassMembersUtil() {
  }


  public static Map<String, List<CandidateInfo>> getAllMethods(@NotNull final PsiClass aClass, boolean includeSynthetic) {
    return getCachedMembers(aClass, includeSynthetic).getSecond();
  }

  @NotNull
  private static Trinity<Map<String, CandidateInfo>, Map<String, List<CandidateInfo>>, Map<String, CandidateInfo>> getCachedMembers(
      @NotNull PsiClass aClass,
      boolean includeSynthetic) {
    Key<CachedValue<Trinity<Map<String, CandidateInfo>, Map<String, List<CandidateInfo>>, Map<String, CandidateInfo>>>> key =
        includeSynthetic ? CACHED_MEMBERS_INCLUDING_SYNTHETIC : CACHED_MEMBERS;
    CachedValue<Trinity<Map<String, CandidateInfo>, Map<String, List<CandidateInfo>>, Map<String, CandidateInfo>>> cachedValue = aClass.getUserData(key);
    if (cachedValue == null) {
      cachedValue = buildCache(aClass, includeSynthetic);
      aClass.putUserData(key, cachedValue);
    }
    return cachedValue.getValue();
  }

  public static Map<String, CandidateInfo> getAllInnerClasses(@NotNull final PsiClass aClass, boolean includeSynthetic) {
    return getCachedMembers(aClass, includeSynthetic).getThird();
  }

  public static Map<String, CandidateInfo> getAllFields(@NotNull final PsiClass aClass) {
    return getCachedMembers(aClass, false).getFirst();
  }

  private static CachedValue<Trinity<Map<String, CandidateInfo>, Map<String, List<CandidateInfo>>, Map<String, CandidateInfo>>> buildCache(@NotNull final PsiClass aClass, final boolean includeSynthetic) {
    return CachedValuesManager.getManager(aClass.getProject()).createCachedValue(new CachedValueProvider<Trinity<Map<String, CandidateInfo>, Map<String, List<CandidateInfo>>, Map<String, CandidateInfo>>>() {
          public Result<Trinity<Map<String, CandidateInfo>, Map<String, List<CandidateInfo>>, Map<String, CandidateInfo>>> compute() {
            Map<String, CandidateInfo> allFields = new HashMap<>();
            Map<String, List<CandidateInfo>> allMethods = new HashMap<>();
            Map<String, CandidateInfo> allInnerClasses = new HashMap<>();

            processClass(aClass, allFields, allMethods, allInnerClasses, new HashSet<PsiClass>(), PsiSubstitutor.EMPTY, includeSynthetic);
            return Result.create(Trinity.create(allFields, allMethods, allInnerClasses), PsiModificationTracker.OUT_OF_CODE_BLOCK_MODIFICATION_COUNT);
          }
        }, false);
  }

  private static void processClass(@NotNull PsiClass aClass, @NotNull Map<String, CandidateInfo> allFields, @NotNull Map<String, List<CandidateInfo>> allMethods, @NotNull Map<String, CandidateInfo> allInnerClasses, @NotNull Set<PsiClass> visitedClasses, @NotNull PsiSubstitutor substitutor, boolean includeSynthetic) {
    if (visitedClasses.contains(aClass)) {
      return;
    }
    visitedClasses.add(aClass);

    for (PsiField field : aClass.getFields()) {
      String name = field.getName();
      if (!allFields.containsKey(name)) {
        allFields.put(name, new CandidateInfo(field, substitutor));
      } else if (hasExplicitVisibilityModifiers(field)) {
        final CandidateInfo candidateInfo = allFields.get(name);
        final PsiElement element = candidateInfo.getElement();
        if (element instanceof IGosuField && (((IGosuField) element).getModifierList() == null ||
            !(((IGosuField) element).getModifierList()).hasExplicitVisibilityModifiers()) &&
            aClass == ((IGosuField) element).getContainingClass()) {
          //replace property-field with field with explicit visibilityModifier
          allFields.put(name, new CandidateInfo(field, substitutor));
        }
      }
    }

    for (PsiMethod method : includeSynthetic || !(aClass instanceof IGosuTypeDefinition) ? aClass.getMethods() : aClass.getMethods()) {
      addMethod(allMethods, method, substitutor);
    }

    for (final PsiClass inner : aClass.getInnerClasses()) {
      final String name = inner.getName();
      if (name != null && !allInnerClasses.containsKey(name)) {
        allInnerClasses.put(name, new CandidateInfo(inner, substitutor));
      }
    }

    for (PsiClassType superType : aClass.getSuperTypes()) {
      PsiClass superClass = superType.resolve();
      if (superClass != null) {
        final PsiSubstitutor superSubstitutor = TypeConversionUtil.getSuperClassSubstitutor(superClass, aClass, substitutor);
        processClass(superClass, allFields, allMethods, allInnerClasses, visitedClasses, superSubstitutor, includeSynthetic);
      }
    }
  }

  private static boolean hasExplicitVisibilityModifiers(@NotNull PsiField field) {
    if (field instanceof IGosuField) {
      final IGosuModifierList list = (IGosuModifierList) field.getModifierList();
      return list == null || list.hasExplicitVisibilityModifiers();
    } else {
      return true;
    }
  }

  private static void addMethod(@NotNull Map<String, List<CandidateInfo>> allMethods, @NotNull PsiMethod method, PsiSubstitutor substitutor) {
    String name = method.getName();
    List<CandidateInfo> methods = allMethods.get(name);
    if (methods == null) {
      methods = new ArrayList<>();
      allMethods.put(name, methods);
      methods.add(new CandidateInfo(method, substitutor));
    } else {
      methods.add(new CandidateInfo(method, substitutor));
    }
  }
}

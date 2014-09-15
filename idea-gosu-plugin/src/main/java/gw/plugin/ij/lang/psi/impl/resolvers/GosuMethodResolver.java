/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.resolvers;

import com.google.common.collect.Maps;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiArrayType;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiEllipsisType;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiReferenceList;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeParameter;
import com.intellij.psi.PsiWildcardType;
import gw.internal.gosu.parser.GosuMethodInfo;
import gw.internal.gosu.parser.IReducedDelegateFunctionSymbol;
import gw.internal.gosu.parser.ReducedDynamicFunctionSymbol;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.parser.IReducedDynamicFunctionSymbol;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.ICompoundType;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IHasParameterInfos;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.ModifiedParameterInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuMethodInfo;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.plugin.ij.lang.psi.api.AbstractFeatureResolver;
import gw.plugin.ij.lang.psi.api.IGosuResolveResult;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;
import gw.plugin.ij.lang.psi.custom.CustomGosuClass;
import gw.plugin.ij.lang.psi.impl.CustomPsiClassCache;
import gw.plugin.ij.lang.psi.impl.GosuPsiSubstitutor;
import gw.plugin.ij.lang.psi.impl.GosuResolveResultImpl;
import gw.plugin.ij.util.JavaPsiFacadeUtil;
import gw.plugin.ij.util.TypeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GosuMethodResolver extends AbstractFeatureResolver {
  @Nullable
  public IGosuResolveResult resolveMethodOrConstructor(@NotNull IHasParameterInfos info, @NotNull PsiElement context) {
    if (info instanceof IGosuMethodInfo) {
      final IReducedDynamicFunctionSymbol dfs = ((IGosuMethodInfo) info).getDfs();
      if (dfs instanceof IReducedDelegateFunctionSymbol) {
        info = ((IReducedDelegateFunctionSymbol) dfs).getTargetMethodInfo();
      }
    }

    IType ownersType = findRealOwnersType(info);
    if (ownersType == null || TypeSystem.isDeleted(ownersType)) {
      return null;
    }

    IType concreteOwnersType = TypeUtil.getConcreteType(ownersType);
    if (concreteOwnersType == null) {
      return null;
    }

    if (concreteOwnersType instanceof ICompoundType) {
      for (IType type : ((ICompoundType) concreteOwnersType).getTypes()) {
        final IGosuResolveResult result = resolveGosuOrJavaMethod(info, context, type, TypeUtil.getConcreteType(type));
        if (result != null) {
          return result;
        }
      }
      return null;
    }

    return resolveGosuOrJavaMethod(info, context, ownersType, concreteOwnersType);
  }

  private IGosuResolveResult resolveAsCustomClass(IHasParameterInfos info) {
    IType type = info.getOwnersType();
    if (info instanceof IMethodInfo &&
        !(type instanceof IGosuClass || type instanceof IJavaType || type instanceof IBlockType || type instanceof IErrorType)) {
      CustomGosuClass psiClass = CustomPsiClassCache.instance().getPsiClass(type);
      if (psiClass != null) {
        PsiElement psiMethod = psiClass.getMethod((IMethodInfo) info, null);
        if (psiMethod != null) {
          return new GosuResolveResultImpl(psiMethod, true, info);
        }
      }
    }
    return null;
  }


  private IType findRealOwnersType(@NotNull IHasParameterInfos info) {
    if (info instanceof IJavaMethodInfo) {
      final IJavaClassInfo enclosingClass = ((IJavaMethodInfo) info).getMethod().getEnclosingClass();
      final IType type = enclosingClass.getJavaType();
      IType ownersType = info.getOwnersType();
      if (ownersType instanceof IJavaType) {
        return ownersType;
      }
      return type;
    } else {
      return info.getOwnersType();
    }
  }

  //private

  @Nullable
  public static IGosuResolveResult findMethodOrConstructor(@NotNull PsiClass targetClass, @NotNull IHasParameterInfos targetMethodInfo, @NotNull Map<String, IType> typeVarMap) {
    String targetMethodName = normalize(targetMethodInfo.getDisplayName());
    IParameterInfo[] targetParameters = targetMethodInfo.getParameters();
    IType[] paramTypes = new IType[targetParameters.length];
    for (int i = 0; i < paramTypes.length; i++) {
      paramTypes[i] = targetParameters[i] instanceof ModifiedParameterInfo ?
          ((ModifiedParameterInfo) targetParameters[i]).getOriginalType() : targetParameters[i].getFeatureType();
    }
    PsiMethod result = null;
    if (maybeCheckSubstitutors(targetClass)) {
      List<Pair<PsiMethod, PsiSubstitutor>> methodsAndSubstitutors = targetClass.findMethodsAndTheirSubstitutorsByName(targetMethodName, true);
      for (Pair<PsiMethod, PsiSubstitutor> pair : methodsAndSubstitutors) {
        PsiMethod method = pair.getFirst();
        if (matchMethod(method, targetMethodInfo, targetMethodName, paramTypes, typeVarMap, pair.getSecond(), targetMethodInfo instanceof IConstructorInfo)) {
          result = method;
          break;
        }
      }
    }
    if (result == null) {
      result = findMethodOrConstructorDeep(targetClass, targetMethodInfo, targetMethodName, paramTypes, typeVarMap, new HashSet<PsiClass>());
    }

    if (result == null) {
      return null;
    } else {
      GosuPsiSubstitutor substitutor = new GosuPsiSubstitutor(typeVarMap, targetClass);
      return new GosuResolveResultImpl(result, null, substitutor, true, true, targetMethodInfo);
    }
  }

  @Nullable
  private static IGosuResolveResult resolveGosuOrJavaMethod(@NotNull IHasParameterInfos info, PsiElement ctx, @NotNull IType ownersType, @NotNull IType concreteOwnersType) {
    final PsiClass psiClass;
    if (IGosuClass.ProxyUtil.isProxyClass(concreteOwnersType.getName())) {
      psiClass = (PsiClass) PsiTypeResolver.getProxiedPsiClass(concreteOwnersType, ctx);
    } else {
      final PsiElement element = PsiTypeResolver.resolveType(concreteOwnersType, ctx);
      psiClass = element instanceof PsiClass ? (PsiClass) element : null;
    }

    if (psiClass == null) {
      return null;
    }

    if (info instanceof IConstructorInfo) {
      if (((IConstructorInfo) info).isDefault() ||
          (!(psiClass instanceof IGosuTypeDefinition) && psiClass.isAnnotationType())) {
        return new GosuResolveResultImpl(psiClass, true, info);
      }
    }

    final Map<String, IType> typeVarMap = ownersType.isParameterizedType() ?
        createTypeVarMap(ownersType.getTypeParameters(), psiClass.getTypeParameters()) :
        Collections.<String, IType>emptyMap();

    Set<IHasParameterInfos> visited = new HashSet<>();
    while( info instanceof GosuMethodInfo ) {
      ReducedDynamicFunctionSymbol dfs = ((GosuMethodInfo) info).getDfs();
      IReducedDynamicFunctionSymbol backingDfs = dfs.getBackingDfs();
      if( backingDfs != null && backingDfs != dfs ) {
        IAttributedFeatureInfo backingInfo = backingDfs.getMethodOrConstructorInfo();
        if( !(backingInfo instanceof IHasParameterInfos) || backingInfo == info ) {
          break;
        }
        if( visited.contains( info ) ) {
          throw new RuntimeException( "Dfs cycle detected: " + dfs.getType().getEnclosingType() + " : " + dfs.getDisplayName() );
        }
        visited.add( info );
        info = (IHasParameterInfos) backingInfo;
      }
      else {
        break;
      }
    }
    return findMethodOrConstructor(psiClass, info, typeVarMap);
  }

  @Nullable
  private static PsiMethod findMethodOrConstructorDeep(
      @Nullable PsiClass targetClass, @NotNull IHasParameterInfos targetMethodInfo, @NotNull String targetMethodName, @NotNull IType[] targetParamTypes, @NotNull Map<String, IType> typeVarMap, @NotNull Set<PsiClass> visited) {
    ProgressManager.checkCanceled();
    if (targetClass == null || visited.contains(targetClass)) {
      return null;
    }
    visited.add(targetClass);
    PsiMethod result = null;
    for (PsiMethod method : getMethodsOrContructors(targetMethodInfo, targetClass)) {
      if (matchMethod(method, targetMethodInfo, targetMethodName, targetParamTypes, typeVarMap, null, targetMethodInfo instanceof IConstructorInfo)) {
        result = method;
        break;
      }
    }
    if (result == null) {
      result = findMethodOrConstructorDeep(targetClass.getSuperClass(), targetMethodInfo, targetMethodName, targetParamTypes, typeVarMap, visited);
    }
    return result;
  }

  private static boolean maybeCheckSubstitutors(@NotNull PsiClass targetClass) {
    PsiTypeParameter[] typeParameters = targetClass.getTypeParameters();
    if (typeParameters.length > 0) {
      return true;
    }
    PsiClass superClass = targetClass.getSuperClass();
    if (superClass != null) {
      // XXX Hack to avoid IJ throwing an illegal state exception when asked for substitutors for an enum
      if ("java.lang.Enum".equals(superClass.getQualifiedName())) {
        return false;
      }
      typeParameters = superClass.getTypeParameters();
      return typeParameters.length > 0;
    }
    return false;
  }

  private static boolean matchMethod(@NotNull PsiMethod method, @NotNull IHasParameterInfos targetMethodInfo, @NotNull String targetMethodName, @NotNull IType[] paramTypes, @NotNull Map<String, IType> typeVarMap, PsiSubstitutor substitutor, boolean isConstructor) {
    if (matchName(method, targetMethodInfo, targetMethodName, isConstructor)) {
      return false;
    }

    boolean isStatic = targetMethodInfo.isStatic();
    if (!isConstructor && isStatic != method.getModifierList().hasModifierProperty(PsiModifier.STATIC)) {
      return false;
    }

    PsiParameterList parameterList = method.getParameterList();
    if (paramTypes.length != parameterList.getParametersCount()) {
      return false;
    }

    PsiParameter[] psiParams = parameterList.getParameters();
    for (int i = 0; i < paramTypes.length; i++) {
      if (!matchParameter(paramTypes[i], psiParams[i].getType(), typeVarMap, substitutor, isStatic)) {
        return false;
      }
    }
    return true;
  }

  // TODO: use GosuProperties class
  private static boolean matchName(@NotNull PsiMethod method, @NotNull IHasParameterInfos targetMethodInfo, @NotNull String targetMethodName, boolean isConstructor) {
    if (targetMethodName.startsWith("@")) {
      IType returnType = ((IGosuMethodInfo) targetMethodInfo).getReturnType();
      if ((returnType.equals(JavaTypes.BOOLEAN()) || returnType.equals(JavaTypes.pBOOLEAN()) &&
          !method.getName().equals("is" + targetMethodName.substring(1)))) {
        return true;
      }
      if (!method.getName().equals("get" + targetMethodName.substring(1)) && !method.getName().equals("set" + targetMethodName.substring(1))) {
        return true;
      }
    } else if (!targetMethodName.equals(method.getName()) && !(isConstructor && "construct".equals(method.getName()))) {
      return true;
    }
    return false;
  }

  private static boolean matchParameter(IType type, @NotNull PsiType candidate, @NotNull Map<String, IType> typeVarMap, @Nullable PsiSubstitutor substitutor, boolean isStatic) {
    // XXX maybe use PsiTypeVisitor & candidate.accept() instead of instanceof here?  See TypeParameterSearcher
    boolean result = false;

    if (type instanceof IMetaType) {
      type = ((IMetaType) type).getType();
    }

    IType basicPattern = type.isParameterizedType() ? type.getGenericType() : type;
    String patternName = basicPattern.getName();
    IType candidateType;

    if( candidate instanceof PsiEllipsisType ) {
      candidate = ((PsiEllipsisType)candidate).toArrayType();
    }

    if( candidate instanceof PsiArrayType && type.isArray() ) {
      return matchParameter( type.getComponentType(), ((PsiArrayType)candidate).getComponentType(), typeVarMap, substitutor, isStatic );
    }

    if (candidate instanceof PsiClassType) {
      PsiClassType candidateAsPsiClass = (PsiClassType) candidate;
      PsiClass resolvedCandidate = candidateAsPsiClass.resolve();
      String candidateName;
      if (resolvedCandidate != null) {
        if (resolvedCandidate instanceof PsiTypeParameter && substitutor != null) {
          resolvedCandidate = maybeSubstituteType(resolvedCandidate, substitutor);
          if (isStatic) {
            resolvedCandidate = stripToBoundingType((PsiTypeParameter) resolvedCandidate);
          }
        }
        if (resolvedCandidate instanceof PsiTypeParameter) {
          candidateName = resolvedCandidate.getName();
        } else {
          candidateName = resolvedCandidate.getQualifiedName();
        }
      } else {
        candidateName = candidate.getCanonicalText();
      }
      candidateType = typeVarMap.get(candidateName);
      if (candidateType != null) {
        result = type.equals(candidateType);
      }
      if( !result ) {
        result = candidateName.equals(patternName);
        if (result) {
          if( type instanceof ITypeVariableType && resolvedCandidate instanceof PsiTypeParameter ) {
            PsiClassType boundingType = JavaPsiFacadeUtil.getElementFactory( resolvedCandidate.getProject() ).createType( stripToBoundingType( (PsiTypeParameter)resolvedCandidate ) );
            result = matchParameter( TypeLord.getPureGenericType( ((ITypeVariableType)type).getBoundingType() ), boundingType, typeVarMap, substitutor, isStatic ) ||
                     matchParameter( ((ITypeVariableType)type).getBoundingType(), boundingType, typeVarMap, substitutor, isStatic );
          }
          else {
            PsiType[] candidateTypeParams = candidateAsPsiClass.getParameters();
            IType[] patternTypeParams = type.getTypeParameters();
            int candidateTypeParamLength = candidateTypeParams != null ? candidateTypeParams.length : 0;
            int patternTypeParamLength = patternTypeParams != null ? patternTypeParams.length : 0;
            if (patternTypeParamLength == candidateTypeParamLength) {
              for (int i = 0; i < patternTypeParamLength; i++) {
                if (!matchParameter(patternTypeParams[i], candidateTypeParams[i], typeVarMap, substitutor, isStatic)) {
                  result = false;
                  break;
                }
              }
            } else {
              result = false;
            }
          }
        }
      }
    } else {
      PsiType unboundedCandidate = removeBounds(candidate);
      candidateType = typeVarMap.get(unboundedCandidate.getCanonicalText());
      if (candidateType != null) {
        result = type.equals(candidateType);
      } else {
        result = unboundedCandidate.equalsToText(patternName);
      }
    }
    return result;
  }

  // Handles issue with getDisplayName() for java generic constructors, e.g. HashMap<String, String>
  @NotNull
  private static String normalize(@NotNull String displayName) {
    int angleIndex = displayName.indexOf('<');
    return angleIndex != -1 ? displayName.substring(0, angleIndex) : displayName;
  }

  @Nullable
  private static PsiType removeBounds(@NotNull PsiType type) {
    if( type instanceof PsiWildcardType ) {
      // Always use *Extends* bound because Gosu treats all wildcard types covariantly
      // i.e., the bound s/b Object for contravariant wildcards
      return ((PsiWildcardType)type).getExtendsBound();
    } else {
      return type;
    }
  }

  @Nullable
  private static PsiClass maybeSubstituteType(PsiClass resolvedCandidate, @NotNull PsiSubstitutor substitutor) {
    PsiType substitutedType = substitutor.getSubstitutionMap().get(resolvedCandidate);
    if (substitutedType != null && substitutedType instanceof PsiClassType) {
      PsiClass resolvedSubstitution = ((PsiClassType) substitutedType).resolve();
      if (resolvedSubstitution != null) {
        return resolvedSubstitution;
      }
    }
    return resolvedCandidate;
  }

  @NotNull
  private static PsiMethod[] getMethodsOrContructors(IHasParameterInfos targetMethodInfo, @NotNull PsiClass targetClass) {
    if (targetMethodInfo instanceof IMethodInfo) {
      return targetClass.getMethods();
    } else {
      return targetClass.getConstructors();
    }
  }

  @NotNull
  private static Map<String, IType> createTypeVarMap(@Nullable IType[] paramTypes, @Nullable PsiTypeParameter[] typeVars) {
    Map<String, IType> typeVarMap = Maps.newHashMap();
    if (typeVars != null && paramTypes != null) {
      int len = Math.min(typeVars.length, paramTypes.length);
      for (int i = 0; i < len; ++i) {
        typeVarMap.put(typeVars[i].getName(), paramTypes[i]);
      }
    }
    return typeVarMap;
  }

  @Nullable
  private static PsiClass stripToBoundingType(@NotNull PsiTypeParameter typeParam) {
    PsiClass result = null;
    PsiReferenceList extendsList = typeParam.getExtendsList();
    PsiClassType[] referencedTypes = extendsList.getReferencedTypes();
    if (referencedTypes.length > 0) {
      // todo deal with entire list as composite type (e.g. List & Map, etc.)
      result = referencedTypes[0].resolve();
    }
    if (result == null) {
      result = PsiTypeResolver.resolveType("java.lang.Object", typeParam);
    }
    return result != null ? result : typeParam;
  }

  @NotNull
  private static IHasParameterInfos resolveParamerizedFeatureInfo(@NotNull IHasParameterInfos featureInfo) {
    if (featureInfo.getOwnersType().isParameterizedType()) {
      if (featureInfo instanceof IGosuMethodInfo) {
        IReducedDynamicFunctionSymbol dfs = ((IGosuMethodInfo) featureInfo).getDfs();
        if (dfs != null) {
          IReducedDynamicFunctionSymbol backingDfs = dfs.getBackingDfs();
          if (backingDfs != null) {
            IAttributedFeatureInfo resolvedFeatureInfo = backingDfs.getMethodOrConstructorInfo();
            if (resolvedFeatureInfo instanceof IHasParameterInfos) {
              featureInfo = (IHasParameterInfos) resolvedFeatureInfo;
            }
          }
        }
      }
    }
    return featureInfo;
  }

}

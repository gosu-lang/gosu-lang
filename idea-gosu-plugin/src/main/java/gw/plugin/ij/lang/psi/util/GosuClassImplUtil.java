/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.*;
import com.intellij.psi.impl.PsiClassImplUtil;
import com.intellij.psi.impl.source.PsiImmediateClassType;
import com.intellij.psi.infos.CandidateInfo;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.MethodSignature;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.psi.util.TypeConversionUtil;
import com.intellij.util.ArrayUtil;
import com.intellij.util.Function;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.containers.ContainerUtil;
import gw.plugin.ij.lang.psi.api.statements.IGosuField;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuReferenceList;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuTypeDefinitionImpl;
import gw.plugin.ij.util.JavaPsiFacadeUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GosuClassImplUtil {
  private static final Condition<PsiClassType> IS_GOSU_OBJECT = new Condition<PsiClassType>() {
    public boolean value(@NotNull PsiClassType psiClassType) {
      return
          IGosuTypeDefinition.DEFAULT_BASE_CLASS_NAME.endsWith(psiClassType.getPresentableText()) &&
          IGosuTypeDefinition.DEFAULT_BASE_CLASS_NAME.equals(psiClassType.getCanonicalText());
    }
  };

  public static final String GOSU_OBJECT_SUPPORT = "gosu.lang.GosuObjectSupport";
  public static final String SYNTHETIC_METHOD_IMPLEMENTATION = "GosuSyntheticMethodImplementation";
  private static final Logger LOG = Logger.getInstance("#com.intellij.psi.impl.PsiClassImplUtil");
  private static final Key<Boolean> NAME_MAPS_BUILT_FLAG = Key.create("NAME_MAPS_BUILT_FLAG");
  private static final Key<CachedValue<Map>> MAP_IN_CLASS_KEY = Key.create("MAP_KEY");

  private GosuClassImplUtil() {
  }


  @Nullable
  public static PsiClass getSuperClass(@NotNull IGosuTypeDefinition grType) {
    if(grType.isAnonymous()) {
      getSuperClassForAnonymousClass(grType);
    }
    final PsiClassType[] extendsList = grType.getExtendsListTypes();
    if (extendsList.length == 0) {
      return getBaseClass(grType);
    }
    final PsiClass superClass = extendsList[0].resolve();
    return superClass != null ? superClass : getBaseClass(grType);
  }

  @Nullable
  private static PsiClass getSuperClassForAnonymousClass(@NotNull IGosuTypeDefinition grType) {
    PsiClassType baseClassReference = ((PsiAnonymousClass) grType).getBaseClassType();
    PsiClass baseClass = baseClassReference.resolve();
    return (baseClass != null && !baseClass.isInterface()) ? baseClass : getBaseClass(grType);
  }

  @Nullable
  public static PsiClass getBaseClass(@NotNull IGosuTypeDefinition grType) {
    if (grType.isEnum()) {
      return JavaPsiFacadeUtil.findClass(grType.getProject(), CommonClassNames.JAVA_LANG_ENUM, grType.getResolveScope());
    } else {
      return JavaPsiFacadeUtil.findClass(grType.getProject(), CommonClassNames.JAVA_LANG_OBJECT, grType.getResolveScope());
    }
  }

  private static final Key<CachedValue<Boolean>> HAS_GOSU_OBJECT_METHODS = Key.create("has gosu object methods");

  @NotNull
  public static PsiClassType[] getExtendsListTypes(@NotNull IGosuTypeDefinition gosuClass) {
    if (gosuClass.isEnum()) {
      PsiClassType enumSuperType = getEnumSuperType(gosuClass, JavaPsiFacade.getInstance(gosuClass.getProject()).getElementFactory());
      return enumSuperType == null ? PsiClassType.EMPTY_ARRAY : new PsiClassType[]{enumSuperType};
    }
    final PsiClassType[] extendsTypes = getReferenceListTypes(gosuClass.getExtendsClause());
    if (gosuClass.isInterface() /*|| extendsTypes.length > 0*/) {
      return extendsTypes;
    }
    for (PsiClassType type : extendsTypes) {
      final PsiClass superClass = type.resolve();
      if (superClass instanceof IGosuTypeDefinition && !superClass.isInterface()) {
        return extendsTypes;
      }
    }

    PsiClass grObSupport = JavaPsiFacadeUtil.findClass(gosuClass.getProject(), GOSU_OBJECT_SUPPORT, gosuClass.getResolveScope());
    if (grObSupport != null) {
      return ArrayUtil.append(extendsTypes, JavaPsiFacadeUtil.getElementFactory(gosuClass.getProject()).createType(grObSupport));
    }
    return extendsTypes;
  }

  private static PsiClassType getEnumSuperType(@NotNull PsiClass psiClass, @NotNull PsiElementFactory factory) {
    PsiClassType superType;
    final PsiManager manager = psiClass.getManager();
    final PsiClass enumClass = JavaPsiFacade.getInstance(manager.getProject()).findClass("java.lang.Enum", psiClass.getResolveScope());
    if (enumClass == null) {
      try {
        superType = (PsiClassType)factory.createTypeFromText("java.lang.Enum", null);
      }
      catch (IncorrectOperationException e) {
        superType = null;
      }
    }
    else {
      final PsiTypeParameter[] typeParameters = enumClass.getTypeParameters();
      PsiSubstitutor substitutor = PsiSubstitutor.EMPTY;
      if (typeParameters.length == 1) {
        substitutor = substitutor.put(typeParameters[0], factory.createType(psiClass));
      }
      superType = new PsiImmediateClassType(enumClass, substitutor);
    }
    return superType;
  }

  private static boolean hasGosuObjectSupportInner(@NotNull final PsiClass psiClass,
                                                   @NotNull Set<PsiClass> visited,
                                                   PsiClass gosuObjSupport,
                                                   @NotNull PsiManager manager) {
    final CachedValue<Boolean> userData = psiClass.getUserData(HAS_GOSU_OBJECT_METHODS);
    if (userData != null && userData.getValue() != null) {
      return userData.getValue();
    }

    if (manager.areElementsEquivalent(gosuObjSupport, psiClass)) {
      return true;
    }

    final PsiClassType[] supers;
    if (psiClass instanceof IGosuTypeDefinition) {
      supers = getReferenceListTypes(((IGosuTypeDefinition) psiClass).getExtendsClause());
    } else {
      supers = psiClass.getExtendsListTypes();
    }

    boolean result = false;
    for (PsiClassType superType : supers) {
      PsiClass aSuper = superType.resolve();
      if (aSuper == null || visited.contains(aSuper)) {
        continue;
      }
      visited.add(aSuper);
      if (hasGosuObjectSupportInner(aSuper, visited, gosuObjSupport, manager)) {
        result = true;
        break;
      }
    }
    final boolean finalResult = result;
    psiClass.putUserData(HAS_GOSU_OBJECT_METHODS,
        CachedValuesManager.getManager(manager.getProject()).createCachedValue(new CachedValueProvider<Boolean>() {
              public Result<Boolean> compute() {
                return Result.create(finalResult, PsiModificationTracker.JAVA_STRUCTURE_MODIFICATION_COUNT);
              }
            }, false));
    return finalResult;
  }

  @NotNull
  public static PsiClassType[] getImplementsListTypes(@NotNull IGosuTypeDefinition grType) {
    final PsiClassType[] implementsTypes = getReferenceListTypes(grType.getImplementsClause());
    if (!grType.isInterface() &&
        !ContainerUtil.or(implementsTypes, IS_GOSU_OBJECT) &&
        !ContainerUtil.or(getReferenceListTypes(grType.getExtendsClause()), IS_GOSU_OBJECT)) {
      return ArrayUtil.append(implementsTypes, getGosuObjectType(grType));
    }
    return implementsTypes;
  }

  @NotNull
  private static PsiClassType getGosuObjectType(@NotNull IGosuTypeDefinition grType) {
    return JavaPsiFacadeUtil.getElementFactory(grType.getProject())
        .createTypeByFQClassName(IGosuTypeDefinition.DEFAULT_BASE_CLASS_NAME, grType.getResolveScope());
  }

  @NotNull
  public static PsiClassType[] getSuperTypes(@NotNull IGosuTypeDefinition grType) {
    if(grType.isAnonymous()) {
      return getSuperTypesForAnonymousClass(grType);
    }
    PsiClassType[] extendsList = grType.getExtendsListTypes();
    if (extendsList.length == 0) {
      extendsList = new PsiClassType[]{createBaseClassType(grType)};
    }
    return ArrayUtil.mergeArrays(extendsList, grType.getImplementsListTypes(), PsiClassType.class);
  }

  @NotNull
  private static PsiClassType[] getSuperTypesForAnonymousClass(@NotNull IGosuTypeDefinition grType) {
    PsiClassType baseClassReference = ((PsiAnonymousClass) grType).getBaseClassType();
    PsiClass baseClass = baseClassReference.resolve();
    if (baseClass != null) {
      if (baseClass.isInterface()) {
        return new PsiClassType[] {createBaseClassType(grType), baseClassReference};
      } else {
        return new PsiClassType[] {baseClassReference};
      }
    } else {
      return new PsiClassType[]{createBaseClassType(grType)};
    }
  }

  @NotNull
  public static PsiClassType createBaseClassType(@NotNull IGosuTypeDefinition grType) {
    if (grType.isEnum()) {
      return JavaPsiFacadeUtil.getElementFactory(grType.getProject())
          .createTypeByFQClassName(CommonClassNames.JAVA_LANG_ENUM, grType.getResolveScope());
    } else {
      return JavaPsiFacadeUtil.getElementFactory(grType.getProject())
          .createTypeByFQClassName(CommonClassNames.JAVA_LANG_OBJECT, grType.getResolveScope());
    }
  }

  @NotNull
  public static PsiMethod[] getAllMethods(@NotNull IGosuTypeDefinition grType) {
    List<PsiMethod> allMethods = new ArrayList<>();
    getAllMethodsInner(grType, allMethods, new HashSet<PsiClass>());

    return allMethods.toArray(new PsiMethod[allMethods.size()]);
  }

  private static void getAllMethodsInner(@NotNull PsiClass clazz, @NotNull List<PsiMethod> allMethods, @NotNull HashSet<PsiClass> visited) {
    if (visited.contains(clazz)) {
      return;
    }
    visited.add(clazz);

    ContainerUtil.addAll(allMethods, clazz.getMethods());

    final PsiField[] fields = clazz.getFields();
    for (PsiField field : fields) {
      if (field instanceof IGosuField) {
        final IGosuField gosuField = (IGosuField) field;
        //## todo:
//        if( gosuField.isProperty() )
//        {
//          PsiMethod[] getters = gosuField.getGetters();
//          if( getters.length > 0 )
//          {
//            ContainerUtil.addAll( allMethods, getters );
//          }
//          PsiMethod setter = gosuField.getSetter();
//          if( setter != null )
//          {
//            allMethods.add( setter );
//          }
//        }
      }
    }

    final PsiClass[] supers = clazz.getSupers();
    for (PsiClass aSuper : supers) {
      getAllMethodsInner(aSuper, allMethods, visited);
    }
  }

  private static PsiClassType[] getReferenceListTypes(@Nullable IGosuReferenceList list) {
    if (list == null) {
      return PsiClassType.EMPTY_ARRAY;
    }
    return list.getReferenceTypes();
  }


  public static PsiClass[] getInterfaces(@NotNull IGosuTypeDefinition grType) {
    final PsiClassType[] implementsListTypes = grType.getImplementsListTypes();
    List<PsiClass> result = new ArrayList<>(implementsListTypes.length);
    for (PsiClassType type : implementsListTypes) {
      final PsiClass psiClass = type.resolve();
      if (psiClass != null) {
        result.add(psiClass);
      }
    }
    return result.toArray(new PsiClass[result.size()]);
  }

  @NotNull
  public static PsiClass[] getSupers(@NotNull IGosuTypeDefinition grType) {
    PsiClassType[] superTypes = grType.getSuperTypes();
    List<PsiClass> result = new ArrayList<>();
    for (PsiClassType superType : superTypes) {
      PsiClass superClass = superType.resolve();
      if (superClass != null) {
        result.add(superClass);
      }
    }

    return result.toArray(new PsiClass[result.size()]);
  }

  public static boolean processDeclarations(@NotNull IGosuTypeDefinition grType,
                                            @NotNull PsiScopeProcessor processor,
                                            @NotNull ResolveState state,
                                            PsiElement lastParent,
                                            @NotNull PsiElement place) {
    //## todo:
    return true;
//    for( final PsiTypeParameter typeParameter : grType.getTypeParameters() )
//    {
//      if( !ResolveUtil.processElement( processor, typeParameter, state ) )
//      {
//        return false;
//      }
//    }
//
//    NameHint nameHint = processor.getHint( NameHint.KEY );
//    //todo [DIANA] look more carefully
//    String name = nameHint == null ? null : nameHint.getName( state );
//    ClassHint classHint = processor.getHint( ClassHint.KEY );
//    final PsiSubstitutor substitutor = state.get( PsiSubstitutor.KEY );
//    final PsiElementFactory factory = JavaPsiFacade.getElementFactory( place.getProject() );
//
//    if( classHint == null || classHint.shouldProcess( ClassHint.ResolveKind.PROPERTY ) )
//    {
//      Map<String, CandidateInfo> fieldsMap = CollectClassMembersUtil.getAllFields( grType );
//      if( name != null )
//      {
//        CandidateInfo fieldInfo = fieldsMap.get( name );
//        if( fieldInfo != null )
//        {
//          final PsiField field = (PsiField)fieldInfo.getElement();
//          if( !isSameDeclaration( place, field ) )
//          { //the same variable declaration
//            final PsiSubstitutor finalSubstitutor = PsiClassImplUtil
//              .obtainFinalSubstitutor( field.getContainingClass(), fieldInfo.getSubstitutor(), grType, substitutor, place, factory );
//            if( !processor.execute( field, state.put( PsiSubstitutor.KEY, finalSubstitutor ) ) )
//            {
//              return false;
//            }
//          }
//        }
//      }
//      else
//      {
//        for( CandidateInfo info : fieldsMap.values() )
//        {
//          final PsiField field = (PsiField)info.getElement();
//          if( !isSameDeclaration( place, field ) )
//          {  //the same variable declaration
//            final PsiSubstitutor finalSubstitutor = PsiClassImplUtil
//              .obtainFinalSubstitutor( field.getContainingClass(), info.getSubstitutor(), grType, substitutor, place, factory );
//            if( !processor.execute( field, state.put( PsiSubstitutor.KEY, finalSubstitutor ) ) )
//            {
//              return false;
//            }
//          }
//        }
//      }
//    }
//
//    if( classHint == null || classHint.shouldProcess( ClassHint.ResolveKind.METHOD ) )
//    {
//      Map<String, List<CandidateInfo>> methodsMap = CollectClassMembersUtil.getAllMethods( grType, true );
//      boolean isPlaceGosu = place.getLanguage() == GosuFileType.GOSU_FILE_TYPE.getLanguage();
//      if( name == null )
//      {
//        for( List<CandidateInfo> list : methodsMap.values() )
//        {
//          for( CandidateInfo info : list )
//          {
//            PsiMethod method = (PsiMethod)info.getElement();
//            if( !isSameDeclaration( place, method ) && isMethodVisible( isPlaceGosu, method ) )
//            {
//              final PsiSubstitutor finalSubstitutor = PsiClassImplUtil
//                .obtainFinalSubstitutor( method.getContainingClass(), info.getSubstitutor(), grType, substitutor, place, factory );
//              if( !processor.execute( method, state.put( PsiSubstitutor.KEY, finalSubstitutor ) ) )
//              {
//                return false;
//              }
//            }
//          }
//        }
//      }
//      else
//      {
//        List<CandidateInfo> byName = methodsMap.get( name );
//        if( byName != null )
//        {
//          for( CandidateInfo info : byName )
//          {
//            PsiMethod method = (PsiMethod)info.getElement();
//            if( !isSameDeclaration( place, method ) && isMethodVisible( isPlaceGosu, method ) )
//            {
//              final PsiSubstitutor finalSubstitutor = PsiClassImplUtil
//                .obtainFinalSubstitutor( method.getContainingClass(), info.getSubstitutor(), grType, substitutor, place, factory );
//              if( !processor.execute( method, state.put( PsiSubstitutor.KEY, finalSubstitutor ) ) )
//              {
//                return false;
//              }
//            }
//          }
//        }
//      }
//    }
//
//    if( !isSuperClassReferenceResolving( grType, lastParent ) )
//    {
//      if( classHint == null || classHint.shouldProcess( ClassHint.ResolveKind.CLASS ) )
//      {
//        for( CandidateInfo info : CollectClassMembersUtil.getAllInnerClasses( grType, false ).values() )
//        {
//          final PsiClass innerClass = (PsiClass)info.getElement();
//          assert innerClass != null;
//          final String innerClassName = innerClass.getName();
//          if( nameHint != null && !innerClassName.equals( nameHint.getName( state ) ) )
//          {
//            continue;
//          }
//
//          if( !processor.execute( innerClass, state ) )
//          {
//            return false;
//          }
//        }
//      }
//    }
//
//
//    return true;
  }

//  private static boolean isSuperClassReferenceResolving( IGosuTypeDefinition grType, PsiElement lastParent )
//  {
//    return lastParent instanceof IGosuReferenceList ||
//           grType.isAnonymous() && lastParent == ((IGosuAnonymousClassDefinition)grType).getBaseClassReferenceGosu();
//  }


  private static boolean isSameDeclaration(@Nullable PsiElement place, PsiElement element) {
    if (!(element instanceof IGosuField)) {
      return false;
    }
    while (place != null) {
      place = place.getParent();
      if (place == element) {
        return true;
      }
    }
    return false;
  }

  private static boolean isMethodVisible(boolean isPlaceGosu, PsiMethod method) {
    return isPlaceGosu;
  }

  @Nullable
  public static PsiMethod findMethodBySignature(@NotNull IGosuTypeDefinition grType, @NotNull PsiMethod patternMethod, boolean checkBases) {
    final MethodSignature patternSignature = patternMethod.getSignature(PsiSubstitutor.EMPTY);
    for (PsiMethod method : findMethodsByName(grType, patternMethod.getName(), checkBases, false)) {
      final PsiClass clazz = method.getContainingClass();
      if (clazz == null) {
        continue;
      }
      PsiSubstitutor superSubstitutor = TypeConversionUtil.getClassSubstitutor(clazz, grType, PsiSubstitutor.EMPTY);
      if (superSubstitutor == null) {
        continue;
      }
      final MethodSignature signature = method.getSignature(superSubstitutor);
      if (signature.equals(patternSignature)) {
        return method;
      }
    }

    return null;
  }

  private static PsiMethod[] findMethodsByName(@NotNull IGosuTypeDefinition grType,
                                               @NotNull String name,
                                               boolean checkBases,
                                               boolean includeSyntheticAccessors) {
    List<PsiMethod> result = new ArrayList<>();

    if (!checkBases) {
      for (PsiMethod method : includeSyntheticAccessors ? grType.getMethods() : grType.getMethods()) {
        if (name.equals(method.getName())) {
          result.add(method);
        }
      }
    } else {
      PsiClass type = grType;
      while (type != null) {
        for (PsiMethod method : includeSyntheticAccessors ? type.getMethods() : type.getMethods()) {
          if (name.equals(method.getName())) {
            result.add(method);
          }
        }
        type = type.getSuperClass();
      }
    }

    return result.toArray(new PsiMethod[result.size()]);
  }

  @NotNull
  public static PsiMethod[] findMethodsBySignature(@NotNull IGosuTypeDefinition grType, @NotNull PsiMethod patternMethod, boolean checkBases) {
    return findMethodsBySignature(grType, patternMethod, checkBases, true);
  }

  @NotNull
  public static PsiMethod[] findCodeMethodsBySignature(@NotNull IGosuTypeDefinition grType, @NotNull PsiMethod patternMethod, boolean checkBases) {
    return findMethodsBySignature(grType, patternMethod, checkBases, false);
  }

  @NotNull
  public static PsiMethod[] findMethodsByName(@NotNull IGosuTypeDefinition grType, @NotNull @NonNls String name, boolean checkBases) {
    return findMethodsByName(grType, name, checkBases, true);
  }

  private static PsiMethod[] findMethodsBySignature(@NotNull IGosuTypeDefinition grType,
                                                    @NotNull PsiMethod patternMethod,
                                                    boolean checkBases,
                                                    boolean includeSynthetic) {
    ArrayList<PsiMethod> result = new ArrayList<>();
    final MethodSignature patternSignature = patternMethod.getSignature(PsiSubstitutor.EMPTY);
    for (PsiMethod method : findMethodsByName(grType, patternMethod.getName(), checkBases, includeSynthetic)) {
      final PsiClass clazz = method.getContainingClass();
      if (clazz == null) {
        continue;
      }
      PsiSubstitutor superSubstitutor = TypeConversionUtil.getClassSubstitutor(clazz, grType, PsiSubstitutor.EMPTY);
      if (superSubstitutor == null) {
        continue;
      }

      final MethodSignature signature = method.getSignature(superSubstitutor);
      if (signature.equals(patternSignature)) //noinspection unchecked
      {
        result.add(method);
      }
    }
    return result.toArray(new PsiMethod[result.size()]);
  }

  @NotNull
  public static PsiMethod[] findCodeMethodsByName(@NotNull IGosuTypeDefinition grType, @NotNull @NonNls String name, boolean checkBases) {
    return findMethodsByName(grType, name, checkBases, false);
  }

  @NotNull
  public static List<Pair<PsiMethod, PsiSubstitutor>> findMethodsAndTheirSubstitutorsByName(@NotNull IGosuTypeDefinition grType,
                                                                                            String name,
                                                                                            boolean checkBases) {
    final ArrayList<Pair<PsiMethod, PsiSubstitutor>> result = new ArrayList<>();

    if (!checkBases) {
      final PsiMethod[] methods = grType.findMethodsByName(name, false);
      for (PsiMethod method : methods) {
        result.add(new Pair<>(method, PsiSubstitutor.EMPTY));
      }
    } else {
      final Map<String, List<CandidateInfo>> map = CollectClassMembersUtil.getAllMethods(grType, true);
      final List<CandidateInfo> candidateInfos = map.get(name);
      if (candidateInfos != null) {
        for (CandidateInfo info : candidateInfos) {
          final PsiElement element = info.getElement();
          result.add(new Pair<>((PsiMethod) element, info.getSubstitutor()));
        }
      }
    }

    return result;
  }

  @NotNull
  public static List<Pair<PsiMethod, PsiSubstitutor>> getAllMethodsAndTheirSubstitutors(@NotNull IGosuTypeDefinition grType) {
    final Map<String, List<CandidateInfo>> allMethodsMap = CollectClassMembersUtil.getAllMethods(grType, true);
    List<Pair<PsiMethod, PsiSubstitutor>> result = new ArrayList<>();
    for (List<CandidateInfo> infos : allMethodsMap.values()) {
      for (CandidateInfo info : infos) {
        result.add(new Pair<>((PsiMethod) info.getElement(), info.getSubstitutor()));
      }
    }

    return result;
  }

  @Nullable
  public static PsiField findFieldByName(@NotNull IGosuTypeDefinition grType, @NotNull String name, boolean checkBases) {
    if (!checkBases) {
      for (PsiField field : grType.getFields()) {
        if (name.equals(field.getName())) {
          return field;
        }
      }

      return null;
    }

    Map<String, CandidateInfo> fieldsMap = CollectClassMembersUtil.getAllFields(grType);
    final CandidateInfo info = fieldsMap.get(name);
    return info == null ? null : (PsiField) info.getElement();
  }

  @NotNull
  public static PsiField[] getAllFields(@NotNull IGosuTypeDefinition grType) {
    Map<String, CandidateInfo> fieldsMap = CollectClassMembersUtil.getAllFields(grType);
    return ContainerUtil.map2Array(fieldsMap.values(), PsiField.class, new Function<CandidateInfo, PsiField>() {
      @Nullable
      public PsiField fun(@NotNull CandidateInfo entry) {
        return (PsiField) entry.getElement();
      }
    });
  }

  public static boolean isClassEquivalentTo(GosuTypeDefinitionImpl definition, PsiElement another) {
    return PsiClassImplUtil.isClassEquivalentTo(definition, another);
  }
}

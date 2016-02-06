/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.reflect.subpackage.SubTypeNewPackage;
import gw.test.TestClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class description...
 *
 * @author dbrewster
 */
public class FeatureManagerTest extends TestClass {
  public void testGetPrivateFeaturesOnClassWithNoInheritenceReturnsSameAsDeclared() {
    IRelativeTypeInfo typeInfo = (IRelativeTypeInfo) TypeSystem.get(Object.class).getTypeInfo();
    FeatureManager man = new FeatureManager(typeInfo, false);
    List<IMethodInfo> methods = new ArrayList<IMethodInfo>( man.getMethods( IRelativeTypeInfo.Accessibility.PRIVATE ) );
    Iterator<IMethodInfo> iter = methods.iterator();
    while( iter.hasNext() )
    {
      IMethodInfo mi = iter.next();
      if(mi.getDisplayName().equals("@itype"))
      {
        iter.remove();
      }
    }
    assertListEquals( methods, typeInfo.getDeclaredMethods());
    assertListEquals(man.getConstructors(IRelativeTypeInfo.Accessibility.PRIVATE), typeInfo.getDeclaredConstructors());
  }

  public void testGetMethodsReturnsCorrectSetOfFeaturesForAllTypes() {
    IRelativeTypeInfo typeInfo = (IRelativeTypeInfo) TypeSystem.get(TopLevelType.class).getTypeInfo();
    FeatureManager man = new FeatureManager(typeInfo, false);
    Map<String, String> publicMethods = new HashMap<String, String>();
    Map<String, String> internalMethods = new HashMap<String, String>();
    Map<String, String> protectedMethods = new HashMap<String, String>();
    Map<String, String> privateMethods = new HashMap<String, String>();

    publicMethods.put("hashCode()", Object.class.getName());
    publicMethods.put("toString()", Object.class.getName());
    publicMethods.put("notify()", Object.class.getName());
    publicMethods.put("wait( long )", Object.class.getName());
    publicMethods.put("equals( java.lang.Object )", Object.class.getName());
    publicMethods.put("wait( long, int )", Object.class.getName());
    publicMethods.put("getClass()", Object.class.getName());
    publicMethods.put("wait()", Object.class.getName());
    publicMethods.put("notifyAll()", Object.class.getName());

    protectedMethods.put("clone()", Object.class.getName());
    protectedMethods.put("finalize()", Object.class.getName());

    publicMethods.put("baseFunc()", SubType.class.getName());
    publicMethods.put("getBaseProp()", SubType.class.getName());

    publicMethods.put("matchingFunc()", BaseType.class.getName());
    publicMethods.put("getMatchingProp()", BaseType.class.getName());

    publicMethods.put("EnhPublicFunc()", "gw.lang.reflect.gwtest.typeinfo.InterfaceEnhancement");
    publicMethods.put("@EnhPublicProp()", "gw.lang.reflect.gwtest.typeinfo.InterfaceEnhancement");
    protectedMethods.put("EnhProtectedFunc()", "gw.lang.reflect.gwtest.typeinfo.InterfaceEnhancement");
    protectedMethods.put("@EnhProtectedProp()", "gw.lang.reflect.gwtest.typeinfo.InterfaceEnhancement");

    publicMethods.put("publicSubMethod()", BaseType.class.getName());
    publicMethods.put("getPublicSubProp()", BaseType.class.getName());
    publicMethods.put("getPublicFieldProp()", BaseType.class.getName());
    protectedMethods.put("protectedSubMethod()", BaseType.class.getName());
    protectedMethods.put("getProtectedSubProp()", BaseType.class.getName());
    internalMethods.put("internalSubMethod()", BaseType.class.getName());
    internalMethods.put("getInternalSubProp()", BaseType.class.getName());

    internalMethods.put("internalStaticBaseMethod()", BaseType.class.getName());
    internalMethods.put("getInternalStaticBaseProp()", BaseType.class.getName());

    privateMethods.put("getPrivateProp()", TopLevelType.class.getName());
    internalMethods.put("getInternalProp()", SubType.class.getName());
    protectedMethods.put("getProtectedProp()", SubType.class.getName());
    publicMethods.put("getPublicProp()", SubType.class.getName());
    privateMethods.put("privateMethod()", TopLevelType.class.getName());
    internalMethods.put("internalMethod()", SubType.class.getName());
    protectedMethods.put("protectedMethod()", SubType.class.getName());
    publicMethods.put("publicMethod()", SubType.class.getName());
    publicMethods.put("publicSubMethod( java.lang.String )", SubType.class.getName());
    protectedMethods.put("protectedSubMethod( java.lang.String )", SubType.class.getName());
    internalMethods.put("internalStaticSubMethod()", SubType.class.getName());
    internalMethods.put("getInternalStaticSubProp()", SubType.class.getName());

    assertFeaturesMatch(union(publicMethods, protectedMethods, internalMethods, privateMethods), man.getMethods(IRelativeTypeInfo.Accessibility.PRIVATE));
    assertFeaturesMatch(union(publicMethods, protectedMethods, internalMethods), man.getMethods(IRelativeTypeInfo.Accessibility.INTERNAL));
    assertFeaturesMatch(union(publicMethods, protectedMethods), man.getMethods(IRelativeTypeInfo.Accessibility.PROTECTED));
    assertFeaturesMatch(union(publicMethods), man.getMethods(IRelativeTypeInfo.Accessibility.PUBLIC));
  }

  public void testGetPropertiesReturnsCorrectListsOfPropertiesForEachAccessType() {
    IRelativeTypeInfo typeInfo = (IRelativeTypeInfo) TypeSystem.get(TopLevelType.class).getTypeInfo();
    FeatureManager man = new FeatureManager(typeInfo, false);

    Map<String, String> publicProperties = new HashMap<String, String>();
    Map<String, String> internalProperties = new HashMap<String, String>();
    Map<String, String> protectedProperties = new HashMap<String, String>();
    Map<String, String> privateProperties = new HashMap<String, String>();

    publicProperties.put("BaseProp", SubType.class.getName());
    publicProperties.put("MatchingProp", BaseType.class.getName());
    publicProperties.put("PublicSubProp", BaseType.class.getName());
    publicProperties.put("PublicFieldProp", SubType.class.getName());
    publicProperties.put("publicField", SubType.class.getName());
    publicProperties.put("PublicProp", SubType.class.getName());
    publicProperties.put("Class", Object.class.getName());

    internalProperties.put("InternalSubProp", BaseType.class.getName());
    internalProperties.put("InternalStaticSubProp", SubType.class.getName());
    internalProperties.put("InternalStaticBaseProp", BaseType.class.getName());
    internalProperties.put("InternalProp", SubType.class.getName());
    internalProperties.put("internalField", SubType.class.getName());

    protectedProperties.put("ProtectedSubProp", BaseType.class.getName());
    protectedProperties.put("ProtectedProp", SubType.class.getName());
    protectedProperties.put("protectedField", SubType.class.getName());

    privateProperties.put("PrivateProp", TopLevelType.class.getName());
    privateProperties.put("privateField", TopLevelType.class.getName());

    publicProperties.put("EnhPublicProp", "gw.lang.reflect.gwtest.typeinfo.InterfaceEnhancement");
    protectedProperties.put("EnhProtectedProp", "gw.lang.reflect.gwtest.typeinfo.InterfaceEnhancement");

    assertFeaturesMatch(union(publicProperties, protectedProperties, internalProperties, privateProperties), man.getProperties(IRelativeTypeInfo.Accessibility.PRIVATE));
    assertFeaturesMatch(union(publicProperties, protectedProperties, internalProperties), man.getProperties(IRelativeTypeInfo.Accessibility.INTERNAL));
    assertFeaturesMatch(union(publicProperties, protectedProperties), man.getProperties(IRelativeTypeInfo.Accessibility.PROTECTED));
    assertFeaturesMatch(union(publicProperties), man.getProperties(IRelativeTypeInfo.Accessibility.PUBLIC));
  }

  public void testGetConstructorsProducesCorrectResultsForEachAccessType() {
    IRelativeTypeInfo typeInfo = (IRelativeTypeInfo) TypeSystem.get(SubType.class).getTypeInfo();
    FeatureManager man = new FeatureManager(typeInfo, false);

    Map<String, String> publicConstructors = new HashMap<String, String>();
    Map<String, String> internalConstructors = new HashMap<String, String>();
    Map<String, String> protectedConstructors = new HashMap<String, String>();
    Map<String, String> privateConstructors = new HashMap<String, String>();

    publicConstructors.put("SubType()", SubType.class.getName());
    internalConstructors.put("SubType( int )", SubType.class.getName());
    protectedConstructors.put("SubType( long )", SubType.class.getName());
    privateConstructors.put("SubType( java.lang.String )", SubType.class.getName());

    assertFeaturesMatch(union(publicConstructors, protectedConstructors, internalConstructors, privateConstructors), man.getConstructors(IRelativeTypeInfo.Accessibility.PRIVATE));
    assertFeaturesMatch(union(publicConstructors, protectedConstructors, internalConstructors), man.getConstructors(IRelativeTypeInfo.Accessibility.INTERNAL));
    assertFeaturesMatch(union(publicConstructors, protectedConstructors), man.getConstructors(IRelativeTypeInfo.Accessibility.PROTECTED));
    assertFeaturesMatch(union(publicConstructors), man.getConstructors(IRelativeTypeInfo.Accessibility.PUBLIC));
  }

  public void testSubtypeEnhancementCanAccessProtectedFeatures() {
    IType subTypeEnhancementType = TypeSystem.getByFullName("gw.lang.reflect.gwtest.typeinfo.SubTypeEnhancement");
    FeatureManager man = new FeatureManager((IRelativeTypeInfo) subTypeEnhancementType.getTypeInfo(), false);
    assertEquals(IRelativeTypeInfo.Accessibility.PROTECTED, man.getAccessibilityForClass(TypeSystem.get(BaseType.class), subTypeEnhancementType));
  }

  public void testSubtypeInDifferentNamespaceCanAccessProtectedFeatures() {
    IType subType = TypeSystem.get(SubTypeNewPackage.class);
    FeatureManager man = new FeatureManager((IRelativeTypeInfo) subType.getTypeInfo(), false);
    assertEquals(IRelativeTypeInfo.Accessibility.PROTECTED, man.getAccessibilityForClass(TypeSystem.get(BaseType.class), subType));
  }

  private Map<String, String> union(Map<String, String>... maps) {
    Map<String, String> myMap = new HashMap<String, String>();
    for (Map<String, String> map : maps) {
      myMap.putAll(map);
    }

    return myMap;
  }

  private void assertFeaturesMatch( Map<String, String> exptectedFeatures, List<? extends IFeatureInfo> featuresFromType ) {
    HashSet<String> missingMethods = new HashSet<String>();
    List<String> badOwningTypes = new ArrayList<String>();
    for( IFeatureInfo fi : featuresFromType ) {
      String featureName = fi.getName();
      if( featureName.equals( "itype" ) || featureName.equals( "@itype()" ) ) {
        continue;
      }
      String owningType = exptectedFeatures.remove( featureName );
      if( owningType == null ) {
        missingMethods.add( featureName );
      }
      else {
        if( !owningType.equals( fi.getOwnersType().getName() ) ) {
          badOwningTypes.add( "owning type for method " + featureName + " is wrong.  Expected " + owningType + " got " + fi.getOwnersType() );
        }
      }
    }

    if( !missingMethods.isEmpty() ) {
      fail( "found extra features " + missingMethods );
    }

    if( !badOwningTypes.isEmpty() ) {
      fail( badOwningTypes.toString() );
    }

    if( !exptectedFeatures.isEmpty() ) {
      fail( "could not find features " + exptectedFeatures.keySet() );
    }
  }
}

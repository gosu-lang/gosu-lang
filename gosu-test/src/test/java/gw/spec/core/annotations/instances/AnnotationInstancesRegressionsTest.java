/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.annotations.instances;

import gw.test.TestClass;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.IType;
import gw.lang.reflect.IAnnotationInfo;

import java.util.List;

public class AnnotationInstancesRegressionsTest extends TestClass
{
  public void testStaticVarsHaveAnnotations()
  {
    IType regressions = getRegressionHolder();
    List<IAnnotationInfo> infoList = regressions.getTypeInfo().getProperty("publicStaticVar").getAnnotationsOfType(TypeSystem.get(gw.lang.Deprecated.class));
    assertEquals( 1, infoList.size() );
    assertEquals( "foo", ((gw.lang.Deprecated) infoList.get(0 ).getInstance()).value() );
  }

  public void testCommentWithTextThenDeprecatedAnnotationWorksAsExpected()
  {
    IType regressions = getRegressionHolder();
    List<IAnnotationInfo> infoList = regressions.getTypeInfo().getProperty( "deprecatedWComment" ).getAnnotationsOfType(TypeSystem.get(gw.lang.Deprecated.class));
    assertEquals( 1, infoList.size() );
    assertEquals( "foo", ((gw.lang.Deprecated) infoList.get(0 ).getInstance()).value() );
  }

  public void testMethodAfterDeprecatedMethodIsNotDeprecated()
  {
    IType regressions = getRegressionHolder();
    List<IAnnotationInfo> infoList = regressions.getTypeInfo().getMethod( "nonDeprecatedFunction" ).getAnnotationsOfType(TypeSystem.get(gw.lang.Deprecated.class));
    assertTrue( infoList.isEmpty() );
  }

  public void testTypeAfterDeprecatedMethodIsNotDeprecated()
  {
    IType inner = getRegressionHolderInner();
    assertFalse( inner.getTypeInfo().isDeprecated() );
  }

  private IType getRegressionHolder()
  {
    return TypeSystem.getByFullName( getClass().getPackage().getName() + ".RegressionAnnotationHolderClass" );
  }

  private IType getRegressionHolderInner()
  {
    return TypeSystem.getByFullName( getClass().getPackage().getName() + ".RegressionAnnotationHolderClass.InnerTypeNotDeprecated" );
  }
}

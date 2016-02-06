/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.internal.gosu.ir.transform.GosuClassTransformer;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuEnhancement;

import java.lang.reflect.Field;

/**
 */
public class EnhancedTypeFieldTest extends ByteCodeTestBase
{
  public void testThis() throws Exception
  {
    Class cls = newTestClass();

    // The compiler adds the ENHANCED$TYPE field for tooling
    Field f = cls.getDeclaredField( GosuClassTransformer.ENHANCED_TYPE_FIELD );
    assertTrue( Modifier.isPrivate( f.getModifiers() ) );
    IGosuEnhancement type = (IGosuEnhancement)TypeSystem.get( cls );
    assertEquals( IRTypeResolver.getDescriptor( type.getEnhancedType() ).getName(), f.getType().getName() );
  }

  private Class newTestClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String classPropertyIdentifier = "gw.internal.gosu.compiler.sample.statement.classes.EnhancedTypeFieldTest_Enh";
    Class<?> javaClass = GosuClassLoader.instance().findClass( classPropertyIdentifier );
    assertNotNull( javaClass );
    assertEquals( classPropertyIdentifier, javaClass.getName() );
    return javaClass;
  }
}
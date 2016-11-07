package gw.internal.gosu.regression;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.test.TestClass;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Indexer;
import org.jboss.jandex.MethodInfo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;

public class JandexIntegrationTest extends TestClass
{
  public void testJandexIndexingWorksWithArrays() throws IOException
  {
    IGosuClass type = (IGosuClass)TypeSystem.getByFullName( "gw.internal.gosu.regression.JandexRegressionTest" );
    byte[] bytes = type.compile();
    Indexer indexer = new Indexer();
    ClassInfo index = indexer.index( new ByteArrayInputStream( bytes ) );
    MethodInfo foo = index.asClass().method( "foo", org.jboss.jandex.Type.create( DotName.createSimple( "[I" ), org.jboss.jandex.Type.Kind.ARRAY ) );
    assertNotNull( foo );
  }

  public void testJandexIndexingWorksWithArrayTypeVarsOnEnhancements() throws IOException
  {
    IGosuClass type = (IGosuClass)TypeSystem.getByFullName( "gw.internal.gosu.regression.JandexRegressionTestEnhancement" );
    byte[] bytes = type.compile();
    Indexer indexer = new Indexer();
    ClassInfo index = indexer.index( new ByteArrayInputStream( bytes ) );
    assertEquals( 2, index.asClass().methods().size() );
  }
}

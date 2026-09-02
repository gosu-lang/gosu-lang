package gw.internal.gosu.ir.nodes;

import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.ir.IRType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.java.IJavaType;
import gw.test.TestClass;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashSet;
import java.util.Set;

import junit.framework.TestCase;

/**
 * Covers {@link JavaClassIRType#isAssignableFrom} when the operand is a Gosu class.
 *
 * <p>That answer decides whether {@code AbstractExpressionTransformer.maybeCast} emits a
 * {@code checkcast} for a call argument. It used to be only a scan of the Gosu class's
 * {@code getAllTypesInHierarchy()} for a Java ancestor by name, falling through to {@code false} on
 * a miss. That set is populated lazily, and when a Gosu class is compiled after its Gosu supertype
 * it can come back holding only the Gosu links -- every Java ancestor absent -- even though the
 * supertype chain resolves all the way down. A miss therefore did not mean "not assignable", and
 * whether the scan hit depended on compile order.
 *
 * <p>The fixture is a real Gosu-extends-Java hierarchy:
 * {@code gw.test.TestClassGosuTest} (Gosu) -&gt; {@code gw.test.TestClass} (Java) -&gt;
 * {@code junit.framework.TestCase} (Java), which also implements {@code IGosuObject}.
 */
public class JavaClassIRTypeTest extends TestClass
{
  private static final String GOSU_SUBCLASS = "gw.test.TestClassGosuTest";

  private static IRType gosuSubclassIRType()
  {
    IType gosuType = TypeSystem.getByFullName( GOSU_SUBCLASS );
    assertNotNull( "fixture " + GOSU_SUBCLASS + " must resolve", gosuType );
    return IRTypeResolver.getDescriptor( gosuType );
  }

  private static IRType irTypeOf( Class<?> cls )
  {
    return IRTypeResolver.getDescriptor( TypeSystem.get( cls ) );
  }

  public void testJavaIRTypeIsAssignableFromAGosuSubclassOfIt()
  {
    assertTrue( GOSU_SUBCLASS + " directly extends TestClass, so it must be assignable to it",
                irTypeOf( TestClass.class ).isAssignableFrom( gosuSubclassIRType() ) );
  }

  /**
   * TestCase is reached only through the Java class in between. The deeper the ancestor, the more
   * the old scan depended on how much of the hierarchy had been cached when the question was asked.
   */
  public void testJavaIRTypeIsAssignableFromAGosuSubclassThroughADeeperJavaAncestor()
  {
    assertTrue( "TestCase is a transitive Java ancestor of " + GOSU_SUBCLASS,
                irTypeOf( TestCase.class ).isAssignableFrom( gosuSubclassIRType() ) );
  }

  /**
   * The chain walk has to follow interfaces as well as supertypes, otherwise assignability to an
   * interface a Gosu class picks up implicitly would regress.
   */
  public void testJavaInterfaceIRTypeIsAssignableFromAGosuClass()
  {
    assertTrue( "every Gosu class implements IGosuObject",
                irTypeOf( IGosuObject.class ).isAssignableFrom( gosuSubclassIRType() ) );
  }

  /**
   * The chain walk must not make everything assignable -- an unrelated Java type still has to come
   * back false, otherwise casts that are genuinely required would be dropped and the verifier would
   * reject the class.
   */
  public void testJavaIRTypeIsNotAssignableFromAnUnrelatedGosuType()
  {
    assertFalse( "String is not in the hierarchy of " + GOSU_SUBCLASS,
                 irTypeOf( String.class ).isAssignableFrom( gosuSubclassIRType() ) );
  }

  /**
   * The IRType answer must be true for every ancestor that is genuinely in the chain, however it is
   * reached -- directly, transitively, or through a proxy class.
   *
   * <p>This deliberately does not assert that {@code IType.isAssignableFrom} agrees. It does not:
   * for {@code IGosuObject}, which a Gosu class picks up as the proxy
   * {@code _proxy_.gw.lang.reflect.gs.IGosuObject}, the type system reports false while the IRType
   * walk unwraps the proxy and finds it. That divergence is part of why IType assignability could
   * not be used to fix the order dependence -- it consults the same lazily-populated hierarchy that
   * was incomplete in the first place.
   */
  public void testIRTypeReportsEveryReachableAncestorAssignable()
  {
    IRType gosuIR = gosuSubclassIRType();

    for( Class<?> cls: new Class<?>[]{ TestClass.class, TestCase.class, IGosuObject.class, Object.class } )
    {
      assertTrue( "IRType must report " + cls.getName() + " assignable from " + GOSU_SUBCLASS,
                  irTypeOf( cls ).isAssignableFrom( gosuIR ) );
    }
  }

  /**
   * <p>Every assertion above resolves the fixture from a settled type system, which hands back a fully
   * populated {@code getAllTypesInHierarchy()} -- so the old cache scan finds its Java ancestor and they
   * pass whether or not the bug is present. The incomplete-cache state cannot be reached that way.
   *
   * <p>So the state is supplied directly, via {@link #withGosuOnlyHierarchy}, and what gets asserted is
   * the contract the fix establishes: <em>a miss in the hierarchy cache means "not resolved yet", not
   * "not assignable"</em>. This fails when {@code isAssignableFrom} trusts the cache and passes when it
   * falls back to walking supertypes and interfaces on demand.
   */
  public void testJavaIRTypeIsAssignableFromAGosuClassWhoseHierarchyCacheOmitsItsJavaAncestors()
  {
    IType real = TypeSystem.getByFullName( GOSU_SUBCLASS );
    assertNotNull( "fixture " + GOSU_SUBCLASS + " must resolve", real );

    IType incomplete = withGosuOnlyHierarchy( real );

    // Precondition: the doubled hierarchy really is missing the Java ancestor being asked about,
    // otherwise the assertion below would hold trivially.
    for( IType hierarchyType : incomplete.getAllTypesInHierarchy() )
    {
      assertFalse( "the doubled hierarchy must not contain " + TestClass.class.getName() +
                   " -- the test would pass vacuously",
                   TestClass.class.getName().equals( hierarchyType.getName() ) );
    }

    assertTrue( "assignability must not depend on getAllTypesInHierarchy() being fully populated: " +
                GOSU_SUBCLASS + " extends " + TestClass.class.getName() +
                " whether or not the cached hierarchy says so",
                irTypeOf( TestClass.class ).isAssignableFrom( GosuClassIRType.get( incomplete ) ) );
  }

  /**
   * The other half of the contract: falling back to a supertype walk must not turn the answer into a
   * blanket {@code true}. A genuinely unrelated Java type has to stay unassignable even when the cache
   * is incomplete, or casts that are actually required would be dropped and the verifier would reject
   * the class at load time.
   */
  public void testUnrelatedJavaTypeStaysUnassignableWhenTheHierarchyCacheIsIncomplete()
  {
    IType incomplete = withGosuOnlyHierarchy( TypeSystem.getByFullName( GOSU_SUBCLASS ) );

    assertFalse( "String is not in the hierarchy of " + GOSU_SUBCLASS + ", incomplete cache or not",
                 irTypeOf( String.class ).isAssignableFrom( GosuClassIRType.get( incomplete ) ) );
  }

  /**
   * The real Gosu type with exactly one behaviour replaced: {@code getAllTypesInHierarchy()} returns only
   * the Gosu links, every {@link IJavaType} dropped. That is the shape {@code GosuClass._setTypes} caches
   * -- and caches permanently -- when the set is first computed before the Java ancestors have resolved;
   * on {@code PolicyDocumentsCoreResource} it was measured at 4 Gosu-only entries against 11 complete.
   *
   * <p>Everything else, {@code getSupertype()} and {@code getInterfaces()} included, delegates to the real
   * type, so the supertype walk the fix relies on sees a truthful hierarchy. Only the cache lies.
   */
  private static IType withGosuOnlyHierarchy( final IType real )
  {
    final Set<IType> gosuOnly = new LinkedHashSet<IType>();
    for( IType hierarchyType : real.getAllTypesInHierarchy() )
    {
      if( !(hierarchyType instanceof IJavaType) )
      {
        gosuOnly.add( hierarchyType );
      }
    }

    InvocationHandler handler = new InvocationHandler()
    {
      @Override
      public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable
      {
        if( method.getName().equals( "getAllTypesInHierarchy" ) && (args == null || args.length == 0) )
        {
          return gosuOnly;
        }
        try
        {
          return method.invoke( real, args );
        }
        catch( InvocationTargetException e )
        {
          throw e.getCause();
        }
      }
    };

    // IGosuClassInternal is what GosuClassIRType.get() requires of its operand.
    return (IType)Proxy.newProxyInstance( IGosuClassInternal.class.getClassLoader(),
                                          new Class<?>[]{ IGosuClassInternal.class },
                                          handler );
  }
}

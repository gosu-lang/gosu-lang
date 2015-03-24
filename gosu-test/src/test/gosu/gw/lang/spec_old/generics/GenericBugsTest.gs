package gw.lang.spec_old.generics
uses gw.test.TestClass
uses java.lang.Iterable
uses java.util.Iterator
uses gw.lang.reflect.gs.IGosuClass
uses gw.lang.parser.resources.Res
uses java.util.ArrayList
uses java.lang.Integer
uses java.util.NoSuchElementException
uses java.lang.UnsupportedOperationException
uses javax.swing.text.TabableView

class GenericBugsTest extends TestClass
{
  static class GenericHelper<T> {
    function foo() : GenericHelper<T> {
      return bar()
    } 

    function bar() : GenericHelper<T> {
      return new GenericHelper<T>()
    }
  }
  
  static class Concrete extends GenericHelper<String> {}

  function genericMethod<R>( blk():R ) : Iterable<R>{
    return new Iterable<R>() {   
      override function iterator() : Iterator<R> { 
        return new Iterator<R>() {
          var _called = false
          override function hasNext() : boolean {
            return  not _called
          }
          override function next() : R {
            _called = true 
            return blk() 
          }
          override function remove() {
            throw "unsupported"
          }
        }
      }
    }
  }

  function testConcreteSubtypeOfParameterizedVersionOfGenericSuperTypeProperlyReifiesAtRuntime() {
    var concreteSubtype = new Concrete()
    var parameterizedVersion1 = concreteSubtype.bar()
    var parameterizedVersion2 = concreteSubtype.foo()
    assertTrue( concreteSubtype typeis GenericHelper<String> )
    assertTrue( parameterizedVersion1 typeis GenericHelper<String> )
    assertTrue( parameterizedVersion2 typeis GenericHelper<String> )
  }
   
  function testGenericMethodCanCorrectlyConstructInnerClasses() {
    var iterable = genericMethod( \-> 10 )
    var iterator = iterable.iterator()
    assertTrue( iterator.hasNext() )
    assertEquals( 10, iterator.next() )
    assertFalse( iterator.hasNext() )
  }
  
  function testGenericAnonymousInnerClassesDependentOnFunctionTypeVarsWork() {
    var x = genericMethod( \-> 10 )
    assertEquals( 10, x.single() )
  }

  function testStaticMethodsAndPropertiesCannotReferenceClassTypeVariables() {
    var clazz = Errant_BadStaticReferenceToClassTypeVars as IGosuClass
    assertFalse( clazz.Valid )
    var badRefs = clazz.ParseResultsException.ParseIssues.where( \ i -> i.MessageKey == Res.MSG_CANNOT_REFERENCE_CLASS_TYPE_VAR_IN_STATIC_CONTEXT )
    assertEquals( 10, badRefs.Count ) 
  }

  function testBadlyContrainedGosuClassWithSuperSelfParamerizedDoesNotCauseException() {
    var clazz = Errant_BadlyConstrainedClass as IGosuClass
    assertFalse( clazz.Valid )
  }

  static class Test1<A> {
    var _field : List<Test2<A>> as List = new ArrayList<Test2<A>>()
    
    function doIt() {
      _field.add( new Test2<A>() )
      var test3 = new Test3<Test2<A>>()
      _field.each( \ i -> test3.fun( i ) )   
    }
  }

  static class Test2<T> {
  } 
  
  static class Test3<X> {
    function fun( elt : X ) : Test3<X> {
      return null
    }
  }
  
  function testNestedTypeVarsAreProperlyPropagatedInInferenceContexts() {
    var test = new Test1<String>()
    test.doIt()
  }

  function testUnrelatedTypeVarsDoNotCoerce() {
    var clz = Errant_UnrelatedTypeVars as IGosuClass
    assertFalse( clz.Valid )
    var pre = clz.ParseResultsException
    print( pre.ParseIssues.allMatch( \ i -> i.Line == 13 or i.Line == 14 ) )
  }


  class Foo<T> {
    function bar( blk():T ) {
      bar( \-> null )
    }
  }

  //======================================================================
  // A bug from Eric
  //======================================================================
  function testXSelect() { 
    var selected = xselect( {"a"}, \ s -> s ) 
    assertEquals( "a", selected.single() ) 
  }
  
  function xselect<T, R>(lst:List<T>, mapper(elt:T):R) : Iterable<R> {
    var gen():block(done:block():R):R
    gen = \ -> {
      var iter = lst.iterator()
      var too(done:block():R):R
      too = \ blahblah -> iter.hasNext() ? mapper(iter.next()) : blahblah()
      return too
    }
    return new BlockyIterable<R>(gen)
  } 
  
  class BlockyIterable<T> implements Iterable<T> {
    var _gen():block(done:block():T):T
    construct(gen():block(done:block():T):T) {
      _gen = gen
    }

    override function iterator() : Iterator<T> {
      return new BlockyIterator<T>(_gen())
    }
  }
  
  class BlockyIterator<T> implements Iterator<T> {
    var _getNextItem(done:block():T):T //done always returns null
    var hasCachedItem : boolean = false //whether cachedItem contains valid item
    var cachedItem : T

    construct(__getNextItem(done:block():T):T) {
      _getNextItem = __getNextItem
    }

    override final function hasNext() : boolean {
      loadCache()
      return hasCachedItem
    }

    override final function next() : T {
      loadCache()
      if (not hasCachedItem) {
        throw new NoSuchElementException()
      }
      hasCachedItem = false
      return cachedItem
    }

    override function remove() : void {
      throw new UnsupportedOperationException() //implementors may enable
    }
  
    private function loadCache() {
      if (hasCachedItem) {
        return
      }
      cachedItem = _getNextItem( \ -> null )
      hasCachedItem = cachedItem != null
    }
  } 
}
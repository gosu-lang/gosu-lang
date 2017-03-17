package gw.internal.gosu.compiler.blocks
uses java.util.concurrent.Callable

class BlockTypeVariableTest extends gw.test.TestClass
{
  function testNestedBlockInMethodReturnsProperTypeVar() {
    assertEquals( String, returnsBlockOfBlockToT1( String )() )
    assertEquals( String, returnsBlockOfBlockToT2( String )()() )
    assertEquals( String, returnsBlockOfBlockToT3( String )()()() )
    assertEquals( String, returnsBlockOfBlockToT4( String )()()()() )
  }

  function testNestedBlockInStaticMethodReturnsProperTypeVar() {
    assertEquals( String, staticReturnsBlockOfBlockToT1( String )() )
    assertEquals( String, staticReturnsBlockOfBlockToT2( String )()() )
    assertEquals( String, staticReturnsBlockOfBlockToT3( String )()()() )
    assertEquals( String, staticReturnsBlockOfBlockToT4( String )()()()() )
  }

  reified function returnsBlockOfBlockToT1<T>( t : Type<T> ) : block():Type {
    return \->T
  }
  reified function returnsBlockOfBlockToT2<T>( t : Type<T> ) : block():block():Type {
    return \->\->T
  }
  reified function returnsBlockOfBlockToT3<T>( t : Type<T> ) : block():block():block():Type {
    return \->\->\->T
  }
  reified function returnsBlockOfBlockToT4<T>( t : Type<T> ) : block():block():block():block():Type {
    return \->\->\->\->T
  }
  reified static function staticReturnsBlockOfBlockToT1<T>( t : Type<T> ) : block():Type {
    return \->T
  }
  reified static function staticReturnsBlockOfBlockToT2<T>( t : Type<T> ) : block():block():Type {
    return \->\->T
  }
  reified static function staticReturnsBlockOfBlockToT3<T>( t : Type<T> ) : block():block():block():Type {
    return \->\->\->T
  }
  reified static function staticReturnsBlockOfBlockToT4<T>( t : Type<T> ) : block():block():block():block():Type {
    return \->\->\->\->T
  }
}
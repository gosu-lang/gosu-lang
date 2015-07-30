package gw.internal.gosu.compiler.blocks
uses java.util.concurrent.Callable
uses java.lang.Integer
uses java.util.ArrayList
uses java.lang.Character
uses java.util.stream.Collectors
uses java.util.Arrays

class BlockInterfaceCompatibilityTest extends gw.test.TestClass
{
  function testGenericInterfaceCompatibility() {
    var caller = new BlockInterfaceCompatibiltyHelper<String>()
    assertEquals( "test", caller.callItUnparameterized( \-> "test" ) )
    assertEquals( "test", caller.callItFunctionParameterized( \-> "test" ) )
    assertEquals( "test", caller.callItClassParameterized( \-> "test" ) )
  }

  function testGenericInterfaceCompatibility2() {
    var caller = new BlockInterfaceCompatibiltyHelper<String>()
    assertEquals( "test", caller.callItIndirectlyUnparameterized( \ genericIface -> genericIface.returnTestString() ) )
  }

  function testJavaFunctionalInterfaceHavingGetter() {
    var hiya = TestClassLocal.constant( \ -> "hi" )
    assertEquals( "hi", hiya.Init )
  }

  function testJava8Stream() {
    var l = {"a", "b", "bb", "c", "cb", "d" }
    var filterVar = l.stream().filter( \s -> s.contains( "b" ) ).collect( Collectors.toList() )
    assertEquals( {"b", "bb", "cb"}, filterVar )

    var chars = new ArrayList<Character>()
    l.stream().forEach( \s -> chars.add( s.charAt( 0 ) ) )
    assertEquals( {'a', 'b', 'b', 'c', 'c', 'd'}, chars )

    var mapVar = l.stream().map( \ s -> s.length() ).toArray<Integer>( \ len -> new Integer[len] )
    assertArrayEquals( {1, 1, 2, 1, 2, 1}, mapVar )
  }

  function testComparator() {
    var strings: String[] = {"b", "c", "a"}
    Arrays.sort( strings, \ s1, s2 -> s1.compareTo( s2 ) )
    assertArrayEquals( {"a", "b", "c"}, strings )
  }
}
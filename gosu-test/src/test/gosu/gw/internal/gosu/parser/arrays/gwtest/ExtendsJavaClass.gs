package gw.internal.gosu.parser.arrays.gwtest

uses gw.internal.gosu.parser.arrays.gwtest.JavaClass

class ExtendsJavaClass extends JavaClass
{
  function makeJavaArray() : JavaClass[]
  {
    return new ExtendsJavaClass[] {new ExtendsJavaClass()}
  }

  function callJavaMethodWithArray() : JavaClass[]
  {
    var jc = new JavaClass()
    return jc.callWithJavaArray( new ExtendsJavaClass[] {new ExtendsJavaClass()} )
  }
}
package gw.internal.gosu.parser.generics.gwtest
  
class SubclassOfJavaClassWithProtectedMembers extends gw.internal.gosu.parser.generics.gwtest.java.BaseWithProtectedMembers
{
  function getProtectedFieldFromBase() : int
  {
    return _protectedField
  }
}
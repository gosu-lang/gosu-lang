package gw.lang.spec_old.generics
uses gw.util.concurrent.LocklessLazyVar
uses java.util.ArrayList
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ParameterizedTypeAssignabilityCanvas<T> 
{
  var _t : T
  var _l : List<T>
  var _z : LocklessLazyVar<List<T>>
  var _ta : T[]
  
  function TassignableFromObject() : Object
  {
    return _t as Object
  }
  
  function TassignableFromWhatever() : Object
  {
    return _t as Whatever
  }
  
  function TassignableFromListT() : Object
  {
    return _t as List<T>
  }

  function ListTassignableFromListT() : Object
  {
    return _l as List<T>
  }

  function ListTassignableFromListObject() : Object
  {
    return _l as List<Object>
  }

  function ListTassignableFromList() : Object
  {
    return _l as List
  }

  function ListTassignableFromObject() : Object
  {
    return _l as Object
  }
  
  function ListTassignableFromT() : Object
  {
    return _l as T
  }

  function LazyListTassignableFromT() : Object
  {
    return _z as T
  }

  function LazyListTassignableFromListT() : Object
  {
    return _z as List<T>
  }

  function LazyListTassignableFromArrayListT() : Object
  {
    return _z as ArrayList<T> // err
  }

  function ArrayTassignableFromObject() : Object
  {
    return _ta as Object
  }

  function ArrayTassignableFromObjectArray() : Object
  {
    return _ta as Object[]
  }

  static class Whatever
  {
  }
}

package gw.internal.gosu.compiler.sample.statement.classes

uses java.lang.Runnable
  
class IdentifierTestClass extends IdentifierTestBaseClass
{
  static var g_staticData : String as StaticData = "Static"
  var _instanceData : String as InstanceData = "Instance"

  function testThis() : IdentifierTestClass
  {
    return this
  }

  function testLocalVar() : String
  {
    var strLocal = "Local"
    return strLocal
  }

  function testParamVar( param : String ) : String
  {
    return param
  }

  function testInstanceVar() : String
  {
    return _instanceData
  }

  function testStaticVar() : String
  {
    return g_staticData
  }

  function testInstanceProperty() : String
  {
    return InstanceData
  }

  function testStaticProperty() : String
  {
    return StaticData
  }

  function testOuter() : IdentifierTestClass
  {
    var me : IdentifierTestClass = null
    new Runnable()
    {
      override function run()
      {
        me = outer
      }
    }.run()
    return me
  }

  function testInstanceVarFromOuter() : String
  {
    var str : String
    new Runnable()
    {
      override function run()
      {
        str = _instanceData
      }
    }.run()
    return str
  }

  function testInstancePropertyFromOuter() : String
  {
    var str : String
    new Runnable()
    {
      override function run()
      {
        str = InstanceData
      }
    }.run()
    return str
  }

  function testStaticVarFromOuter() : String
  {
    var str : String
    new Runnable()
    {
      override function run()
      {
        str = g_staticData
      }
    }.run()
    return str
  }

  function testStaticPropertyFromOuter() : String
  {
    var str : String
    new Runnable()
    {
      override function run()
      {
        str = StaticData
      }
    }.run()
    return str
  }
}
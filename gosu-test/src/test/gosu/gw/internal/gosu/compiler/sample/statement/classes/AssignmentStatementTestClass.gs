package gw.internal.gosu.compiler.sample.statement.classes

uses java.lang.Runnable
  
class AssignmentStatementTestClass extends IdentifierTestBaseClass
{
  static var g_staticData : String as StaticData
  var _instanceData : String as InstanceData

  function testLocalVar() : String
  {
    var strLocal : String
    strLocal = "Local"
    return strLocal
  }

  function testParamVar( param : String ) : String
  {
    param = "Param"
    return param
  }

  function testInstanceVar() : String
  {
    _instanceData = "Instance"
    return _instanceData
  }

  function testStaticVar() : String
  {
    g_staticData = "testStaticVar"
    return g_staticData
  }

  function testInstanceProperty() : String
  {
    InstanceData = "Instance"
    return InstanceData
  }

  function testStaticProperty() : String
  {
    StaticData = "testStaticProperty"
    return StaticData
  }

  function testInstanceVarFromOuter() : String
  {
    new Runnable()
    {
      override function run()
      {
        _instanceData = "Instance"
      }
    }.run()
    return _instanceData
  }

  function testInstancePropertyFromOuter() : String
  {
    new Runnable()
    {
      override function run()
      {
        InstanceData = "Instance"
      }
    }.run()
    return InstanceData
  }

  function testStaticVarFromOuter() : String
  {
    new Runnable()
    {
      override function run()
      {
        g_staticData = "testStaticVarFromOuter"
      }
    }.run()
    return g_staticData
  }

  function testStaticPropertyFromOuter() : String
  {
    var str : String
    new Runnable()
    {
      override function run()
      {
        StaticData = "testStaticPropertyFromOuter"
      }
    }.run()
    return StaticData
  }
}
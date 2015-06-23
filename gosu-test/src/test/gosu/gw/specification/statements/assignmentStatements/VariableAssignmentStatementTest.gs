package gw.specification.statements.assignmentStatements

uses java.lang.Runnable
uses gw.BaseVerifyErrantTest

class VariableAssignmentStatementTest extends BaseVerifyErrantTest {
    static var g_staticData : String as StaticData
    var _instanceData : String as InstanceData

    function testLocalVar()
    {
      var strLocal : String
      strLocal = "Local"
      assertEquals( "Local", strLocal)
    }

    function testParamVar( param : String )
    {
      param = "Param"
      assertEquals( "Param", param )
    }

    function testInstanceVar()
    {
      _instanceData = "Instance"
      assertEquals( "Instance", _instanceData )
    }

    function testStaticVar()
    {
      g_staticData = "testStaticVar"
      assertEquals( g_staticData, "testStaticVar")
    }

    function testInstanceProperty()
    {
      InstanceData = "Instance"
      assertEquals( InstanceData, "Instance")
    }

    function testStaticProperty()
    {
      StaticData = "testStaticProperty"
      assertEquals(StaticData, "testStaticProperty")
    }

    function testInstanceVarFromOuter()
    {
      new Runnable()
    {
      override function run()
      {
        _instanceData = "Instance"
      }
    }.run()
      assertEquals(_instanceData, "Instance")
    }

    function testInstancePropertyFromOuter()
    {
      new Runnable()
    {
      override function run()
      {
        InstanceData = "Instance"
      }
    }.run()
      assertEquals(InstanceData, "Instance")
    }

    function testStaticVarFromOuter()
    {
      new Runnable()
    {
      override function run()
      {
        g_staticData = "testStaticVarFromOuter"
      }
    }.run()
      assertEquals(g_staticData, "testStaticVarFromOuter")
    }

    function testStaticPropertyFromOuter()
    {
      var str : String
      new Runnable()
    {
      override function run()
      {
        StaticData = "testStaticPropertyFromOuter"
      }
    }.run()
      assertEquals(StaticData, "testStaticPropertyFromOuter" )
    }
}
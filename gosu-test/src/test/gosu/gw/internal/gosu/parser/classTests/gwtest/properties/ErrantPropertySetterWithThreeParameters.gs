package gw.internal.gosu.parser.classTests.gwtest.properties
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ErrantPropertySetterWithThreeParameters
{

    property set Foo( value1 : String, value2 : String, value3 : String )
    {
    }

}
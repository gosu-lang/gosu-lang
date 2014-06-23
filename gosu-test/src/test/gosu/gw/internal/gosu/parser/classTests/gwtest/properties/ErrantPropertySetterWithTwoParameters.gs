package gw.internal.gosu.parser.classTests.gwtest.properties
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ErrantPropertySetterWithTwoParameters
{

    property set Foo( value1 : String, value2 : int )
    {
    }

}
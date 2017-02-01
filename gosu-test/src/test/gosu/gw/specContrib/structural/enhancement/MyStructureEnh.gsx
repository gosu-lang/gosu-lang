package gw.specContrib.structural.enhancement

uses gw.specContrib.structural.MyStructure

enhancement MyStructureEnh : MyStructure
{
  property get MyProp() : String {
    return "MyProp"
  }

  function bar() : String {
    return "bar"
  }

  function hard( ms: MyStructure ) : String {
    return ms.bar()
  }
}
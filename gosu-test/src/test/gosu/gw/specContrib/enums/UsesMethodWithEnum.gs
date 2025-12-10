package gw.specContrib.enums
uses gw.specContrib.interfaces.Methods

class UsesMethodWithEnum {
  
  function foo(m: Methods) {
    m.usesEnum("This uses an implicitly-imported enum value", Val1);
  }
}

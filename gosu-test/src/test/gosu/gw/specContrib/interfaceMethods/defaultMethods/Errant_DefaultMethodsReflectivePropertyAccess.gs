package gw.specContrib.interfaceMethods.defaultMethods

class Errant_DefaultMethodsReflectivePropertyAccess {

  interface IA {
  }

  class AImpl implements IA {
    function test() {
      var x0 = super[0]      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'JAVA.LANG.STRING'
      var x1 = super["foo"]
      var x2 = super[IA]["foo"]
      var x3 = super[]["foo"]      //## issuekeys: '.' EXPECTED AFTER 'SUPER'
      var x4 = super["foo"]["bar"]
      var x5 = super[IA]["foo"]["bar"]["foobar"]
    }
  }
}
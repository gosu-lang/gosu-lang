package gw.lang.spec_old.enhancements
uses gw.testharness.DoNotVerifyResource
uses java.lang.Runnable

@DoNotVerifyResource
enhancement Errant_BaseEnhancementTestEnhancement : BaseEnhancementTest {

  function badOuterReference() {
    var x = new Runnable() {
      override function run() {
        var y = outer
        print( y )
      }
    }
    print( x )
  }

  function badQualifiedMethodReference() {
    var x = new Runnable() {
      override function run() {
        var y = outer.aMethod()
        print( y )
      }
    }
    print( x )
  }
  
  function badUnqualifiedMethodReference() {
    var x = new Runnable() {
      override function run() {
        var y = aMethod()
        print( y )
      }
    }
    print( x )
  }

  function badQualifiedPropertyReference() {
    var x = new Runnable() {
      override function run() {
        var y = outer.AProperty
        print( y )
      }
    }
    print( x )
  }
  
  function badUnqualifiedPropertyReference() {
    var x = new Runnable() {
      override function run() {
        var y = AProperty
        print( y )
      }
    }
    print( x )
  }
  
  function aMethod() : boolean {
    return true 
  }
  
  property get AProperty() : boolean {
    return true 
  }
  
}

package gw.lang.parser
uses java.lang.Runnable
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_FunctionPropertyConflicts {

  //--------------------------------------------
  // Functions after var
  //--------------------------------------------
  var _prop1 : String as Prop1

  //errors 
  function setProp1( s : String ) {}
  function getProp1() : String { return null }
  
  //ok  
  function getProp1( s : String ) {}
  function getProp1( s : String, s2 : String ) {}
  function setProp1() {}
  function setProp1( s : String, s2 : String) {}
  function setProp1( s : Runnable ) {}

  //--------------------------------------------
  // Functions after readonly var
  //--------------------------------------------
  var _prop1_1 : String as readonly Prop1_1

  //errors 
  function getProp1_1() : String { return null }
  
  //ok  
  function setProp1_1( s : String ) {}
  function getProp1_1( s : String ) {}
  function getProp1_1( s : String, s2 : String ) {}
  function setProp1_1() {}
  function setProp1_1( s : String, s2 : String) {}
  function setProp1_1( s : Runnable ) {}

  //--------------------------------------------
  // Functions after set property
  //--------------------------------------------
  var _prop2 : String
  
  property set Prop2( s : String ) {}

  //errors 
  function setProp2( s : String ) {}
  
  //ok  
  function getProp2() : String { return null }
  function getProp2( s : String ) {}
  function getProp2( s : String, s2 : String ) {}
  function setProp2() {}
  function setProp2( s : String, s2 : String) {}
  function setProp2( s : Runnable ) {}

  //--------------------------------------------
  // Functions after get property
  //--------------------------------------------
  var _prop3 : String
  
  property get Prop3() : String { return null }

  //errors 
  function getProp3() : String { return null }
  
  //ok  
  function setProp3( s : String ) {}
  function getProp3( s : String ) {}
  function getProp3( s : String, s2 : String ) {}
  function setProp3() {}
  function setProp3( s : String, s2 : String) {}
  function setProp3( s : Runnable ) {}

  //--------------------------------------------
  // Functions after get and set property
  //--------------------------------------------
  var _prop4 : String
  
  property set Prop4( s : String ) {}
  property get Prop4() : String { return null }

  //errors 
  function getProp4() : String { return null }
  function setProp4( s : String ) {}
  
  //ok  
  function getProp4( s : String ) {}
  function getProp4( s : String, s2 : String ) {}
  function setProp4() {}
  function setProp4( s : String, s2 : String) {}
  function setProp4( s : Runnable ) {}

  //--------------------------------------------
  // Var property after setter
  //--------------------------------------------

  function setProp5( s : String ) {}

  //errors 
  var _prop5 : String as Prop5
  
  //ok  
  function getProp5( s : String ) {}
  function getProp5( s : String, s2 : String ) {}
  function setProp5() {}
  function setProp5( s : String, s2 : String) {}
  function setProp5( s : Runnable ) {}

  //--------------------------------------------
  // Read only var property after setter
  //--------------------------------------------

  function setProp5_1( s : String ) {}

  //errors 
  
  //ok  
  var _prop5_1 : String as readonly Prop5_1
  function getProp5_1( s : String ) {}
  function getProp5_1( s : String, s2 : String ) {}
  function setProp5_1() {}
  function setProp5_1( s : String, s2 : String) {}
  function setProp5_1( s : Runnable ) {}

  //--------------------------------------------
  // Var property after getter
  //--------------------------------------------

  function getProp6() : String {return null}

  //errors 
  var _prop6 : String as Prop6
  
  //ok  
  function getProp6( s : String ) {}
  function getProp6( s : String, s2 : String ) {}
  function setProp6() {}
  function setProp6( s : String, s2 : String) {}
  function setProp6( s : Runnable ) {}

  //--------------------------------------------
  // Var property after getter and setter
  //--------------------------------------------

  function setProp7( s : String ) {}
  function getProp7() : String {return null}

  //errors 
  var _prop7 : String as Prop7
  
  //ok  
  function getProp7( s : String ) {}
  function getProp7( s : String, s2 : String ) {}
  function setProp7() {}
  function setProp7( s : String, s2 : String) {}
  function setProp7( s : Runnable ) {}

  //--------------------------------------------
  // Set property after getter and setter
  //--------------------------------------------

  function setProp8( s : String ) {}
  function getProp8() : String {return null}

  //errors 
  property set Prop8( s : String ) {}
  
  //ok  
  function getProp8( s : String ) {}
  function getProp8( s : String, s2 : String ) {}
  function setProp8() {}
  function setProp8( s : String, s2 : String) {}
  function setProp8( s : Runnable ) {}
    
  //--------------------------------------------
  // Get property after getter and setter
  //--------------------------------------------

  function setProp9( s : String ) {}
  function getProp9() : String {return null}

  //errors 
  property get Prop9() : String {return null}
  
  //ok  
  function getProp9( s : String ) {}
  function getProp9( s : String, s2 : String ) {}
  function setProp9() {}
  function setProp9( s : String, s2 : String) {}
  function setProp9( s : Runnable ) {}
    
  //--------------------------------------------
  // Get and set property after getter and setter
  //--------------------------------------------

  function setProp10( s : String ) {}
  function getProp10() : String {return null}

  //errors 
  property get Prop10() : String {return null}
  property set Prop10(s : String) {}
  
  //ok  
  function getProp10( s : String ) {}
  function getProp10( s : String, s2 : String ) {}
  function setProp10() {}
  function setProp10( s : String, s2 : String) {}
  function setProp10( s : Runnable ) {}
  
  //--------------------------------------------
  // Property then getter and setter reification conflict
  //--------------------------------------------
  
  var _prop11 : String as Prop11
 
  //errors 
  function getProp11<T extends String>() : T { return null }
  function setProp11<T extends String>(s : T) : T { return null }
    
  //ok
  function setProp11<Q extends Boolean>(s : Q) : Q { return null }

  //--------------------------------------------
  // Getter and setter then property reification conflict
  //--------------------------------------------
  
  function getProp12<T extends String>() : T { return null }
  function setProp12<T extends String>(s : T) : T { return null }
 
  //errors 
  var _prop12 : String as Prop12
    
  //ok
  function setProp12<Q extends Boolean>(s : Q) : Q { return null }

  //--------------------------------------------
  // Getter and setter then property reification conflict
  //--------------------------------------------
  
  function getProp13<T extends String>() : T { return null }
  function setProp13<T extends String>(s : T) : T { return null }
 
  //errors 
  property get Prop13<T extends String>() : T { return null }
  property set Prop13<T extends String>(s : T) : T {}
    
  //ok
  function setProp13<Q extends Boolean>(s : Q) : Q { return null }

  //--------------------------------------------
  // Inner class does not have conflicts
  //--------------------------------------------
  
  var _prop14 : String as Prop14  

  class Inner1{ function getProp14() : String{ return null } }
  class Inner2{ function setProp14(s: String){} }
  class Inner3{ property get Prop14() : String{ return null } }
  class Inner4{ property set Prop14(s: String){} }
  class Inner5{ var _prop14 : String as Prop14 }
  class Inner6{ override function toString() : String { return null } }


}
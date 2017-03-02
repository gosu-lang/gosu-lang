package gw.specContrib.statements.usesStatement

class SuperClassForStaticImportTesting {
  public static var ConstField1: String = "ConstField1: from Super"
  
  static function StaticFunc1( s: String ) : String {
     return "StaticFunc1: from Super" 
  }
  
  static property get StaticProp1() : String {
     return "StaticProp1: from Super" 
  }
  
  static function valueOf( b: boolean ) : String { 
    
    return "valueOf( boolean ): from Super" 
  }
}
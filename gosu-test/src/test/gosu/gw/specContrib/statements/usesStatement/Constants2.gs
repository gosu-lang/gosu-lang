package gw.specContrib.statements.usesStatement

interface Constants2 {
  public var ConstField1: String = "ConstField1: from Constants2"
  public var ConstField2: String = "ConstField2: from Constants2"
  public var ConstField3: String = "ConstField3: from Constants2"

  static function StaticFunc1(s: String) : String {
     return "StaticFunc1: from Constants2" 
  }
  static function StaticFunc2(s: String) : String {
     return "StaticFunc2: from Constants2" 
  }
  static function StaticFunc3(s: String) : String {
     return "StaticFunc3: from Constants2" 
  }

  static property get StaticProp1() : String {
     return "StaticProp1: from Constants2" 
  }
  static property get StaticProp2() : String {
     return "StaticProp2: from Constants2" 
  }
  static property get StaticProp3() : String {
     return "StaticProp2: from Constants2" 
  }
}
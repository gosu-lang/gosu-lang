package gw.specContrib.statements.usesStatement

interface Constants {
  public var ConstField1: String = "ConstField1: from Constants"
  public var ConstField2: String = "ConstField2: from Constants"
  public var ConstField3: String = "ConstField3: from Constants"

  public var ConstFieldOrMethod: String = "ConstFieldOrMethod field: from Constants"

  static function ConstFieldOrMethod(): String {
    return "ConstFieldOrMethod method: from Constants"
  }

  static function MethodOrProperty(): String {
    return "MethodOrProperty method: from Constants"
  }

  static property get MethodOrProperty(): String {
    return "MethodOrProperty property: from Constants"
  }

  static function StaticFunc1( s: String ) : String {
     return "StaticFunc1: from Constants"
  }
  static function StaticFunc2( s: String ) : String {
     return "StaticFunc2: from Constants"
  }
  static function StaticFunc3( s: String ) : String {
     return "StaticFunc3: from Constants" 
  }
  static function StaticFuncWithArrayArgument( ss: String[] ) {}

  static property get StaticProp1() : String {
     return "StaticProp1: from Constants" 
  }
  static property get StaticProp2() : String {
     return "StaticProp2: from Constants" 
  }
  static property get StaticProp3() : String {
     return "StaticProp3: from Constants"
  }
}
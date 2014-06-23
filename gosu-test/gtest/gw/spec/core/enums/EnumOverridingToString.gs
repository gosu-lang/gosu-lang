package gw.spec.core.enums

enum EnumOverridingToString {

  RED("alt-red"), GREEN("alt-green"), BLUE("alt-blue")
  
  private var _altName : String
  
  private construct(altName : String) {
    _altName = altName
  }
  
  override function toString() : String {
    return _altName    
  }
}

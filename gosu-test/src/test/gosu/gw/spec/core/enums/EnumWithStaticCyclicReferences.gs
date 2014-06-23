package gw.spec.core.enums

enum EnumWithStaticCyclicReferences {
  RED, GREEN, BLUE
  
  public static var AllFields : List<EnumWithStaticCyclicReferences> = EnumWithStaticCyclicReferences.AllValues
  public static var IntField : int = 11
  
  private var _redAtConstructTime : EnumWithStaticCyclicReferences as RedAtConstructTime
  private var _greenAtConstructTime : EnumWithStaticCyclicReferences as GreenAtConstructTime
  private var _blueAtConstructTime : EnumWithStaticCyclicReferences as BlueAtConstructTime
  
  private construct() {
    _redAtConstructTime = EnumWithStaticCyclicReferences.RED  
    _greenAtConstructTime = EnumWithStaticCyclicReferences.GREEN  
    _blueAtConstructTime = EnumWithStaticCyclicReferences.BLUE  
  }

}

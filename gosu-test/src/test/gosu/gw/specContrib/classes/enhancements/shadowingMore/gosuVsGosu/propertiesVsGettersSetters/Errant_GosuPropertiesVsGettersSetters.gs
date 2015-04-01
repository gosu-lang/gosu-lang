package gw.specContrib.classes.enhancements.shadowingMore.gosuVsGosu.propertiesVsGettersSetters

class Errant_GosuPropertiesVsGettersSetters {


  internal var nameInternal: String as NameInternal
  private var namePrivate: String as NamePrivate
  protected var nameProtected: String as NameProtected
  public var namePublic: String as NamePublic

  internal property get Prop1(): String {
    return null
  }

  private property get Prop2(): String {
    return null
  }

  protected property get Prop3(): String {
    return null
  }

  public property get Prop4(): String {
    return null
  }

  internal property set Prop1(str1: String) {
  }

  private property set Prop2(str2: String) {
  }

  protected property set Prop3(str3: String) {
  }

  public property set Prop4(str4: String) {
  }
}
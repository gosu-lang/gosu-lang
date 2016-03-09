package gw.specContrib.classes.enhancements.package_visibility.subpackage

uses gw.specContrib.classes.enhancements.package_visibility.Errant_EnhancedClass

enhancement SubpackageEnhancement : Errant_EnhancedClass {

  protected function protectedFunction() : String {
    return "I am declared in a different package than the class I enhance"
  }

}

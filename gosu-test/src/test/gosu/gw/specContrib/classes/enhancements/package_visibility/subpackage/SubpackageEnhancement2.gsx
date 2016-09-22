package gw.specContrib.classes.enhancements.package_visibility.subpackage

uses gw.specContrib.classes.enhancements.package_visibility.EnhancedClass

enhancement SubpackageEnhancement2: EnhancedClass {

  protected function protectedFunction() : String {
    return "I am declared in a different package than the class I enhance"
  }

}

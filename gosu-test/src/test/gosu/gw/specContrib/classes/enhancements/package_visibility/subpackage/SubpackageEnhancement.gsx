package gw.specContrib.classes.enhancements.package_visibility.subpackage

uses gw.specContrib.classes.enhancements.package_visibility.EnhancedClass2

enhancement SubpackageEnhancement : EnhancedClass2 {

  protected function protectedFunction() : String {
    return "I am declared in a different package than the class I enhance"
  }

}

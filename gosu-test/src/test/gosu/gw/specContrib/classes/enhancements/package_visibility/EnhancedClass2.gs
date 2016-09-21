package gw.specContrib.classes.enhancements.package_visibility

class EnhancedClass2 {

  function doLegalAccess() {
    // a class should be allowed to invoke its own protected enhancement
    this.protectedFunction()
  }
}

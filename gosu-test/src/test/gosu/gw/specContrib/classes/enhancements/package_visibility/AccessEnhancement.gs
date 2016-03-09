package gw.specContrib.classes.enhancements.package_visibility

/**
 * Created by rberlin on 3/8/16.
 */
class AccessEnhancement {
  var ec = new EnhancedClass()

  // access protected function by invoking in the same package as EnhancedClass.
  function accessProtectedEnhancementFunction() {
    ec.protectedFunction()
  }
}
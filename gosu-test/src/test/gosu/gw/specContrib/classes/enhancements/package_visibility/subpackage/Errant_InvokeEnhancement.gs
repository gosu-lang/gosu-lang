package gw.specContrib.classes.enhancements.package_visibility.subpackage

uses gw.specContrib.classes.enhancements.package_visibility.EnhancedClass

class Errant_InvokeEnhancement {
  var cl = new EnhancedClass()

  // cannot access protected enhancement here because this isn't EnhancedClass' package.
  function doInvalidAccess() {
    cl.protectedFunction()      //## issuekeys: 'PROTECTEDFUNCTION()' HAS PROTECTED ACCESS IN 'GW.SPECCONTRIB.CLASSES.ENHANCEMENTS.PACKAGE_VISIBILITY.SUBPACKAGE.SUBPACKAGEENHANCEMENT'
  }

  // but a subclass of ExtendedClass can see it.
  var cl2 = new EnhancedClass() {
    function doValidAccess() {
      this.protectedFunction()
    }
  }

}
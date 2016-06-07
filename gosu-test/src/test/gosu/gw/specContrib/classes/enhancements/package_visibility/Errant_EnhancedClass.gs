package gw.specContrib.classes.enhancements.package_visibility

class Errant_EnhancedClass {

  function doLegalAccess() {
    // a class can't invoke its own enhancement
    this.protectedFunction()      //## issuekeys: CANNOT RESOLVE METHOD 'PROTECTEDFUNCTION()'
  }
}
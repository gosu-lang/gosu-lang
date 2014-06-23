package gw.internal.gosu.regression.subpackage

enhancement EnhancedClassEnhancement : gw.internal.gosu.regression.EnhancedClass {
  protected function protectedFunction() : String {
    return "protected"  
  }

  public function echo(arg : String) : String {
    return arg  
  }
}

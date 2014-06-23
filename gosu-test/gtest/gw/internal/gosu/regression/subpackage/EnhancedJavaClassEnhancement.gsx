package gw.internal.gosu.regression.subpackage

enhancement EnhancedJavaClassEnhancement : gw.internal.gosu.regression.EnhancedJavaClass {
  protected function protectedFunction() : String {
    return "protected"  
  }

  public function echo(arg : String) : String {
    return arg  
  }
}

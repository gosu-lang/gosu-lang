package gw.internal.gosu.regression

enhancement EnhancedInterfaceEnhancement : EnhancedInterface {
   static function callInternalMethod() : String {
     return InternalJavaClass.doStuff()  
   }
}

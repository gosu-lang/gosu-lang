package gw.internal.gosu.compiler.sample.enhancements.foo

uses gw.internal.gosu.compiler.sample.enhancements._ExtendsEnhanced
uses gw.internal.gosu.compiler.sample.enhancements._Enhanced

enhancement SampleExtendsEnhancedAccessEnhancement : _ExtendsEnhanced {

  function ex_accessesProtectedMethod() : String {
    return this.protectedMethod() 
  }

  function ex_accessesStaticProtectedMethod() : String {
    return _ExtendsEnhanced.staticProtectedMethod() 
  }

// TODO cgross - reenable pending http://jira/jira/browse/PL-8747
//  function ex_accessesStaticProtectedMethod() : String {
//    return _Enhanced.staticProtectedMethod() 
//  }

  function ex_accessesProtectedProperty() : String {
    return this.ProtectedProperty
  }

// TODO cgross - reenable pending http://jira/jira/browse/PL-8747
//  function ex_accessesStaticProtectedProperty() : String {
//    return _Enhanced.StaticProtectedProperty
//  }
  
  function ex_accessesStaticProtectedProperty() : String {
    return _ExtendsEnhanced.StaticProtectedProperty
  }
  
  function ex_writesProtectedProperty() : String {
    this.ProtectedProperty = "updated property"
    return this.ProtectedProperty
  }

  function ex_writesStaticProtectedProperty() : String {
    var initialValue = _ExtendsEnhanced.StaticProtectedProperty
    try
    {
      _ExtendsEnhanced.StaticProtectedProperty = "updated static property"
      return _ExtendsEnhanced.StaticProtectedProperty
    }
    finally
    {
      _ExtendsEnhanced.StaticProtectedProperty = initialValue
    }
  }

// TODO cgross - reenable pending http://jira/jira/browse/PL-8747
//  function ex_writesStaticProtectedProperty() : String {
//    var initialValue = _Enhanced.StaticProtectedProperty
//    try
//    {
//      _Enhanced.StaticProtectedProperty = "updated static property"
//      return _Enhanced.StaticProtectedProperty
//    }
//    finally
//    {
//      _Enhanced.StaticProtectedProperty = initialValue
//    }
//  }

  function ex_accessesProtectedField() : String {
    return this._protectedField
  }

  function ex_accessesStaticProtectedField() : String {
    return _ExtendsEnhanced._staticProtectedField
  }

// TODO cgross - reenable pending http://jira/jira/browse/PL-8747
//  function ex_accessesStaticProtectedField() : String {
//    return _Enhanced._staticProtectedField
//  }

  function ex_writesProtectedField() : String {
    this._protectedField = "updated field"
    return this._protectedField
  }

  function ex_writesStaticProtectedField() : String {
    var initialValue = _ExtendsEnhanced._staticProtectedField
    try
    {
      _ExtendsEnhanced._staticProtectedField = "updated static field"
      return _ExtendsEnhanced._staticProtectedField
    }
    finally
    {
      _ExtendsEnhanced._staticProtectedField = initialValue
    }
  }

// TODO cgross - reenable pending http://jira/jira/browse/PL-8747
//  function ex_writesStaticProtectedField() : String {
//    var initialValue = _Enhanced._staticProtectedField
//    try
//    {
//      _Enhanced._staticProtectedField = "updated static field"
//      return _Enhanced._staticProtectedField
//    }
//    finally
//    {
//      _Enhanced._staticProtectedField = initialValue
//    }
//  }

  function ex_accessesProtectedConstructor() : boolean {
    var x = new _Enhanced( 42 )
    return x.ProtectedConstructorCalled
  }

  function ex_accessesProtectedMethodIndirectly() : String {
    var x = new _Enhanced()
    return x.protectedMethod() 
  }

  function ex_accessesProtectedPropertyIndirectly() : String {
    var x = new _Enhanced()
    return x.ProtectedProperty 
  }

  function ex_writesProtectedPropertyIndirectly() : String {
    var x = new _Enhanced()
    x.ProtectedProperty = "updated property"
    return x.ProtectedProperty
  }

  function ex_accessesProtectedFieldIndirectly() : String {
    var x = new _Enhanced()
    return x._protectedField 
  }

  function ex_writesProtectedFieldIndirectly() : String {
    var x = new _Enhanced()
    x._protectedField = "updated field"
    return x._protectedField
  }

  function ex_accessesStaticProtectedMethodThroughThisPointer() : String {
    return this.staticProtectedMethod() 
  }

  function ex_accessesStaticProtectedPropertyThroughThisPointer() : String {
    return this.StaticProtectedProperty 
  }

  function ex_writesStaticProtectedPropertyThroughThisPointer() : String {
    var initialValue = this.StaticProtectedProperty
    try
    {
      this.StaticProtectedProperty = "updated static property"
      return this.StaticProtectedProperty
    }
    finally
    {
      this.StaticProtectedProperty = initialValue
    }
  }

  function ex_accessesStaticProtectedFieldThroughThisPointer() : String {
    return this._staticProtectedField 
  }

  function ex_writesStaticProtectedFieldThroughThisPointer() : String {
    var initialValue = this._staticProtectedField
    try
    {
      this._staticProtectedField = "updated static field"
      return this._staticProtectedField
    }
    finally
    {
      this._staticProtectedField = initialValue
    }
  }
  
  function ex_accessesStaticProtectedMethodIndirectly() : String {
    var x = new _Enhanced()
    return x.staticProtectedMethod() 
  }

  function ex_accessesStaticProtectedPropertyIndirectly() : String {
    var x = new _Enhanced()
    return x.StaticProtectedProperty 
  }

  function ex_writesStaticProtectedPropertyIndirectly() : String {
    var x = new _Enhanced()
    var initialValue = x.StaticProtectedProperty
    try
    {
      x.StaticProtectedProperty = "updated static property"
      return x.StaticProtectedProperty
    }
    finally
    {
      this.StaticProtectedProperty = initialValue
    }
  }

  function ex_accessesStaticProtectedFieldIndirectly() : String {
    var x = new _Enhanced()
    return x._staticProtectedField 
  }

  function ex_writesStaticProtectedFieldIndirectly() : String {
    var x = new _Enhanced()
    var initialValue = x._staticProtectedField
    try
    {
      x._staticProtectedField = "updated static field"
      return x._staticProtectedField
    }
    finally
    {
      x._staticProtectedField = initialValue
    }
  }
  
}

package gw.internal.gosu.compiler.sample.enhancements.foo
uses gw.internal.gosu.compiler.sample.enhancements._Enhanced

enhancement SampleAccessEnhancement : gw.internal.gosu.compiler.sample.enhancements._Enhanced {

  function accessesProtectedMethod() : String {
    return this.protectedMethod() 
  }

  function i_accessesProtectedMethod() : int {
    return this.i_protectedMethod() 
  }

  function accessesStaticProtectedMethod() : String {
    return _Enhanced.staticProtectedMethod() 
  }

  function i_accessesStaticProtectedMethod() : int {
    return _Enhanced.i_staticProtectedMethod() 
  }

  function accessesProtectedProperty() : String {
    return this.ProtectedProperty
  }

  function i_accessesProtectedProperty() : int {
    return this.i_ProtectedProperty
  }

  function accessesStaticProtectedProperty() : String {
    return _Enhanced.StaticProtectedProperty
  }

  function i_accessesStaticProtectedProperty() : int {
    return _Enhanced.i_StaticProtectedProperty
  }
  
  function writesProtectedProperty() : String {
    this.ProtectedProperty = "updated property"
    return this.ProtectedProperty
  }

  function i_writesProtectedProperty() : int {
    this.i_ProtectedProperty = 52
    return this.i_ProtectedProperty
  }

  function writesStaticProtectedProperty() : String {
    var initialValue = _Enhanced.StaticProtectedProperty
    try
    {
      _Enhanced.StaticProtectedProperty = "updated static property"
      return _Enhanced.StaticProtectedProperty
    }
    finally
    {
      _Enhanced.StaticProtectedProperty = initialValue
    }
  }

  function i_writesStaticProtectedProperty() : int {
    var initialValue = _Enhanced.i_StaticProtectedProperty
    try
    {
      _Enhanced.i_StaticProtectedProperty = 52
      return _Enhanced.i_StaticProtectedProperty
    }
    finally
    {
      _Enhanced.i_StaticProtectedProperty = initialValue
    }
  }

  function accessesProtectedField() : String {
    return this._protectedField
  }

  function i_accessesProtectedField() : int {
    return this._i_protectedField
  }

  function accessesStaticProtectedField() : String {
    return _Enhanced._staticProtectedField
  }

  function i_accessesStaticProtectedField() : int {
    return _Enhanced._i_staticProtectedField
  }

  function writesProtectedField() : String {
    this._protectedField = "updated field"
    return this._protectedField
  }

  function i_writesProtectedField() : int {
    this._i_protectedField = 52
    return this._i_protectedField
  }

  function writesStaticProtectedField() : String {
    var initialValue = _Enhanced._staticProtectedField
    try
    {
      _Enhanced._staticProtectedField = "updated static field"
      return _Enhanced._staticProtectedField
    }
    finally
    {
      _Enhanced._staticProtectedField = initialValue
    }
  }

  function i_writesStaticProtectedField() : int {
    var initialValue = _Enhanced._i_staticProtectedField
    try
    {
      _Enhanced._i_staticProtectedField = 52
      return _Enhanced._i_staticProtectedField
    }
    finally
    {
      _Enhanced._i_staticProtectedField = initialValue
    }
  }

  function accessesProtectedConstructor() : boolean {
    var x = new _Enhanced( 42 )
    return x.ProtectedConstructorCalled
  }

  function accessesProtectedMethodIndirectly() : String {
    var x = new _Enhanced()
    return x.protectedMethod() 
  }

  function accessesProtectedPropertyIndirectly() : String {
    var x = new _Enhanced()
    return x.ProtectedProperty 
  }

  function writesProtectedPropertyIndirectly() : String {
    var x = new _Enhanced()
    x.ProtectedProperty = "updated property"
    return x.ProtectedProperty
  }

  function accessesProtectedFieldIndirectly() : String {
    var x = new _Enhanced()
    return x._protectedField 
  }

  function writesProtectedFieldIndirectly() : String {
    var x = new _Enhanced()
    x._protectedField = "updated field"
    return x._protectedField
  }

  function accessesStaticProtectedMethodThroughThisPointer() : String {
    return this.staticProtectedMethod() 
  }

  function accessesStaticProtectedPropertyThroughThisPointer() : String {
    return this.StaticProtectedProperty 
  }

  function writesStaticProtectedPropertyThroughThisPointer() : String {
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

  function accessesStaticProtectedFieldThroughThisPointer() : String {
    return this._staticProtectedField 
  }

  function writesStaticProtectedFieldThroughThisPointer() : String {
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
  
  function accessesStaticProtectedMethodIndirectly() : String {
    var x = new _Enhanced()
    return x.staticProtectedMethod() 
  }

  function accessesStaticProtectedPropertyIndirectly() : String {
    var x = new _Enhanced()
    return x.StaticProtectedProperty 
  }

  function writesStaticProtectedPropertyIndirectly() : String {
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

  function accessesStaticProtectedFieldIndirectly() : String {
    var x = new _Enhanced()
    return x._staticProtectedField 
  }

  function writesStaticProtectedFieldIndirectly() : String {
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

  //--
  
  function accessesProtectedMethodInBlock() : block():String {
    return \-> this.protectedMethod()
  }

  function accessesStaticProtectedMethodInBlock() : block():String {
    return \-> _Enhanced.staticProtectedMethod()
  }

  function accessesProtectedPropertyInBlock() : block():String {
    return \-> this.ProtectedProperty
  }

  function accessesStaticProtectedPropertyInBlock() : block():String {
    return \-> _Enhanced.StaticProtectedProperty
  }
  
  function writesProtectedPropertyInBlock() : block():String {
    return \->{
       this.ProtectedProperty = "updated property"
       return this.ProtectedProperty
    }
  }

  function writesStaticProtectedPropertyInBlock() : block():String {
    return \->{
      var initialValue = _Enhanced.StaticProtectedProperty
      try
      {
        _Enhanced.StaticProtectedProperty = "updated static property"
        return _Enhanced.StaticProtectedProperty
      }
      finally
      {
        _Enhanced.StaticProtectedProperty = initialValue
      }
    }
  }

  function accessesProtectedFieldInBlock() : block():String {
    return \-> this._protectedField
  }

  function accessesStaticProtectedFieldInBlock() : block():String {
    return \-> _Enhanced._staticProtectedField
  }

  function writesProtectedFieldInBlock() : block():String {
    return \-> {
      this._protectedField = "updated field"
      return this._protectedField
    }
  }

  function writesStaticProtectedFieldInBlock() : block():String {
    return \-> {var initialValue = _Enhanced._staticProtectedField
      try
      {
        _Enhanced._staticProtectedField = "updated static field"
         return _Enhanced._staticProtectedField
      }
      finally
      {
        _Enhanced._staticProtectedField = initialValue
      }
    }
  }

  function accessesProtectedConstructorInBlock() : block():boolean {
    return \-> {
      var x = new _Enhanced( 10 )
      return x.ProtectedConstructorCalled
    }
  }

  function accessesProtectedMethodIndirectlyInBlock() : block():String {
    var x = new _Enhanced()
    return \-> x.protectedMethod()
  }

  function accessesProtectedPropertyIndirectlyInBlock() : block():String {
    var x = new _Enhanced()
    return \-> x.ProtectedProperty
  }

  function writesProtectedPropertyIndirectlyInBlock() : block():String {
    var x = new _Enhanced()
    x.ProtectedProperty = "updated property"
    return \-> x.ProtectedProperty
  }

  function accessesProtectedFieldIndirectlyInBlock() : block():String {
    var x = new _Enhanced()
    return \-> x._protectedField
  }

  function writesProtectedFieldIndirectlyInBlock() : block():String {
    var x = new _Enhanced()
    return \-> {
          x._protectedField = "updated field"
          return x._protectedField
    }
  }

  function accessesStaticProtectedMethodThroughThisPointerInBlock() : block():String {
    return \-> this.staticProtectedMethod()
  }

  function accessesStaticProtectedPropertyThroughThisPointerInBlock() : block():String {
    return \-> this.StaticProtectedProperty
  }

  function writesStaticProtectedPropertyThroughThisPointerInBlock() : block():String {
     return \-> {
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
  }

  function accessesStaticProtectedFieldThroughThisPointerInBlock() : block():String {
    return \-> this._staticProtectedField
  }

  function writesStaticProtectedFieldThroughThisPointerInBlock() : block():String {
    return \-> {
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
  }
  
  function accessesStaticProtectedMethodIndirectlyInBlock() : block():String {
    var x = new _Enhanced()
    return \-> x.staticProtectedMethod()
  }

  function accessesStaticProtectedPropertyIndirectlyInBlock() : block():String {
    var x = new _Enhanced()
    return \-> x.StaticProtectedProperty
  }

  function writesStaticProtectedPropertyIndirectlyInBlock() : block():String {
    var x = new _Enhanced()
    return \-> {
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
  }

  function accessesStaticProtectedFieldIndirectlyInBlock() : block():String {
    var x = new _Enhanced()
    return \-> x._staticProtectedField
  }

  function writesStaticProtectedFieldIndirectlyInBlock() : block():String {
    var x = new _Enhanced()
    return \-> {
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

  //--

  function accessesProtectedMethodInBlockInBlock() : block():block():String {
    return \-> \-> this.protectedMethod()
  }

  function accessesStaticProtectedMethodInBlockInBlock() : block():block():String {
    return \-> \-> _Enhanced.staticProtectedMethod()
  }

  function accessesProtectedPropertyInBlockInBlock() : block():block():String {
    return \-> \-> this.ProtectedProperty
  }

  function accessesStaticProtectedPropertyInBlockInBlock() : block():block():String {
    return \-> \-> _Enhanced.StaticProtectedProperty
  }

  function writesProtectedPropertyInBlockInBlock() : block():block():String {
    return \-> \->{
       this.ProtectedProperty = "updated property"
       return this.ProtectedProperty
    }
  }

  function writesStaticProtectedPropertyInBlockInBlock() : block():block():String {
    return \-> \->{
      var initialValue = _Enhanced.StaticProtectedProperty
      try
      {
        _Enhanced.StaticProtectedProperty = "updated static property"
        return _Enhanced.StaticProtectedProperty
      }
      finally
      {
        _Enhanced.StaticProtectedProperty = initialValue
      }
    }
  }

  function accessesProtectedFieldInBlockInBlock() : block():block():String {
    return \-> \-> this._protectedField
  }

  function accessesStaticProtectedFieldInBlockInBlock() : block():block():String {
    return \-> \-> _Enhanced._staticProtectedField
  }

  function writesProtectedFieldInBlockInBlock() : block():block():String {
    return \-> \-> {
      this._protectedField = "updated field"
      return this._protectedField
    }
  }

  function writesStaticProtectedFieldInBlockInBlock() : block():block():String {
    return \-> \-> {var initialValue = _Enhanced._staticProtectedField
      try
      {
        _Enhanced._staticProtectedField = "updated static field"
         return _Enhanced._staticProtectedField
      }
      finally
      {
        _Enhanced._staticProtectedField = initialValue
      }
    }
  }

  function accessesProtectedConstructorInBlockInBlock() : block():block():boolean {
    return \-> \-> {
      var x = new _Enhanced( 10 )
      return x.ProtectedConstructorCalled
    }
  }

  function accessesProtectedMethodIndirectlyInBlockInBlock() : block():block():String {
    var x = new _Enhanced()
    return \-> \-> x.protectedMethod()
  }

  function accessesProtectedPropertyIndirectlyInBlockInBlock() : block():block():String {
    var x = new _Enhanced()
    return \-> \-> x.ProtectedProperty
  }

  function writesProtectedPropertyIndirectlyInBlockInBlock() : block():block():String {
    var x = new _Enhanced()
    x.ProtectedProperty = "updated property"
    return \-> \-> x.ProtectedProperty
  }

  function accessesProtectedFieldIndirectlyInBlockInBlock() : block():block():String {
    var x = new _Enhanced()
    return \-> \-> x._protectedField
  }

  function writesProtectedFieldIndirectlyInBlockInBlock() : block():block():String {
    var x = new _Enhanced()
    return \-> \-> {
          x._protectedField = "updated field"
          return x._protectedField
    }
  }

  function accessesStaticProtectedMethodThroughThisPointerInBlockInBlock() : block():block():String {
    return \-> \-> this.staticProtectedMethod()
  }

  function accessesStaticProtectedPropertyThroughThisPointerInBlockInBlock() : block():block():String {
    return \-> \-> this.StaticProtectedProperty
  }

  function writesStaticProtectedPropertyThroughThisPointerInBlockInBlock() : block():block():String {
     return \-> \-> {
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
  }

  function accessesStaticProtectedFieldThroughThisPointerInBlockInBlock() : block():block():String {
    return \-> \-> this._staticProtectedField
  }

  function writesStaticProtectedFieldThroughThisPointerInBlockInBlock() : block():block():String {
    return \-> \-> {
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
  }

  function accessesStaticProtectedMethodIndirectlyInBlockInBlock() : block():block():String {
    var x = new _Enhanced()
    return \-> \-> x.staticProtectedMethod()
  }

  function accessesStaticProtectedPropertyIndirectlyInBlockInBlock() : block():block():String {
    var x = new _Enhanced()
    return \-> \-> x.StaticProtectedProperty
  }

  function writesStaticProtectedPropertyIndirectlyInBlockInBlock() : block():block():String {
    var x = new _Enhanced()
    return \-> \-> {
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
  }

  function accessesStaticProtectedFieldIndirectlyInBlockInBlock() : block():block():String {
    var x = new _Enhanced()
    return \-> \-> x._staticProtectedField
  }

  function writesStaticProtectedFieldIndirectlyInBlockInBlock() : block():block():String {
    var x = new _Enhanced()
    return \-> \-> {
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

}
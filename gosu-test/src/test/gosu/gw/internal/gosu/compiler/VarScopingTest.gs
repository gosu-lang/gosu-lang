package gw.internal.gosu.compiler
uses gw.test.TestClass
uses java.util.concurrent.Callable

class VarScopingTest extends TestClass {

  //NOTE: excution and application both mean the same thing: static.  Therefore they are not tested
  var _requestClassVar request : int = 42
  var _sessionClassVar session : int = 62

  function testReadRequestScopeWorks() {
    assertEquals( 42, _requestClassVar )
  }

  function testIdentifierWriteRequestScopeWorks() {
    try {
      _requestClassVar = 52
      assertEquals( 52, _requestClassVar )
    } finally {
      _requestClassVar = 42
    }
  }

  function testReadRequestScopeWorksIndirectly() {
    assertEquals( 42, this._requestClassVar)
  }

  function testIdentifierWriteRequestScopeWorksIndirectly() {
    try {
      this._requestClassVar = 52
      assertEquals( 52, this._requestClassVar )
    } finally {
      this._requestClassVar = 42
    }
  }

  function testReadRequestScopeWorksStatically() {
    assertEquals( 42, VarScopingTest._requestClassVar)
  }
  
  function testIdentifierWriteRequestScopeWorksStatically() {
    try {
      VarScopingTest._requestClassVar = 52
      assertEquals( 52, VarScopingTest._requestClassVar )
    } finally {
      VarScopingTest._requestClassVar = 42
    }
  }

  function testReadRequestScopeWorksInBlock() {
    var blk = \->_requestClassVar
    assertEquals( 42, blk() )
  }

  function testIdentifierWriteRequestScopeWorksInblock() {
    try {
      var blk = \-> { _requestClassVar = 52 }
      blk()
      assertEquals( 52, _requestClassVar )
    } finally {
      _requestClassVar = 42
    }
  }

  function testReadRequestScopeWorksIndirectlyInBlock() {
    var blk = \-> this._requestClassVar
    assertEquals( 42, blk() )
  }

  function testIdentifierWriteRequestScopeWorksIndirectlyInBlock() {
    var blk = \-> {    
      try {
        this._requestClassVar = 52
        return this._requestClassVar
      } finally {
        this._requestClassVar = 42
      }
    }
    assertEquals( 52, blk() )
  }

  function testReadRequestScopeWorksStaticallyInBlock() {
    var blk = \-> VarScopingTest._requestClassVar
    assertEquals( 42, blk() )
  }
  
  function testIdentifierWriteRequestScopeWorksStaticallyInBlock() {
    var blk = \-> {
      try {
        VarScopingTest._requestClassVar = 52
        return VarScopingTest._requestClassVar
      } finally {
        VarScopingTest._requestClassVar = 42
      }
    }
    assertEquals( 52, blk() )
  }

  function testReadRequestScopeWorksInAnonymousClass() {
    var callable = new Callable() {
      function call() : Object {
        return _requestClassVar
      }
    }
    assertEquals( 42, callable.call() )
  }

  function testIdentifierWriteRequestScopeWorksInAnonymousClass() {
    try {
      var callable = new Callable() {
        function call() : Object {
          _requestClassVar = 52
          return null
        }
      }
      callable.call()
      assertEquals( 52, _requestClassVar )
    } finally {
      _requestClassVar = 42
    }
  }

  function testReadRequestScopeWorksIndirectlyInAnonymousClass() {
    var callable = new Callable() {
      function call() : Object {
        return outer._requestClassVar
      }
    }
    assertEquals( 42, callable.call() )
  }

  function testIdentifierWriteRequestScopeWorksIndirectlyInAnonymousClass() {
    var callable = new Callable() {
      function call() : Object {
        try {
          outer._requestClassVar = 52
          return outer._requestClassVar
        } finally {
          outer._requestClassVar = 42
        }
      }
    }
    assertEquals( 52, callable.call() )
  }

  function testReadRequestScopeWorksStaticallyInAnonymousClass() {
    var callable = new Callable() {
      function call() : Object {
        return VarScopingTest._requestClassVar
      }
    }
    assertEquals( 42, callable.call() )
  }
  
  function testIdentifierWriteRequestScopeWorksStaticallyInAnonymousClass() {
    var callable = new Callable() {
      function call() : Object {
        try {
          VarScopingTest._requestClassVar = 52
          return VarScopingTest._requestClassVar
        } finally {
          VarScopingTest._requestClassVar = 42
        }
      }
    }
    assertEquals( 52, callable.call() )
  }

  function testReadRequestScopeWorksInEval() {
    assertEquals( 42, eval( "_requestClassVar" ) )
  }

  function testIdentifierWriteRequestScopeWorksInEval() {
    try {
      print( eval( "_requestClassVar = 52 return null" ) )
      assertEquals( 52, _requestClassVar )
    } finally {
      _requestClassVar = 42
    }
  }

  function testReadRequestScopeWorksIndirectlyInEval() {
    assertEquals( 42, eval( "this._requestClassVar" ) )
  }

  function testIdentifierWriteRequestScopeWorksIndirectlyInEval() {
    try {
      print( eval( "this._requestClassVar = 52 return null" ) )
      assertEquals( 52, this._requestClassVar )
    } finally {
      this._requestClassVar = 42
    }
  }

  function testReadRequestScopeWorksStaticallyInEval() {
    assertEquals( 42, eval( "VarScopingTest._requestClassVar" ) )
  }
  
  function testIdentifierWriteRequestScopeWorksStaticallyInEval() {
    try {
      print( eval( "VarScopingTest._requestClassVar = 52 return null" ) )
      assertEquals( 52, VarScopingTest._requestClassVar )
    } finally {
      VarScopingTest._requestClassVar = 42
    }
  }

  function testReadSessionScopeWorks() {
    assertEquals( 62, _sessionClassVar )
  }

  function testIdentifierWriteSessionScopeWorks() {
    try {
      _sessionClassVar = 72
      assertEquals( 72, _sessionClassVar )
    } finally {
      _sessionClassVar = 62
    }
  }

  function testReadSessionScopeWorksIndirectly() {
    assertEquals( 62, this._sessionClassVar)
  }

  function testIdentifierWriteSessionScopeWorksIndirectly() {
    try {
      this._sessionClassVar = 72
      assertEquals( 72, this._sessionClassVar )
    } finally {
      this._sessionClassVar = 62
    }
  }

  function testReadSessionScopeWorksStatically() {
    assertEquals( 62, VarScopingTest._sessionClassVar)
  }
  
  function testIdentifierWriteSessionScopeWorksStatically() {
    try {
      VarScopingTest._sessionClassVar = 72
      assertEquals( 72, VarScopingTest._sessionClassVar )
    } finally {
      VarScopingTest._sessionClassVar = 62
    }
  }

  function testReadSessionScopeWorksInBlock() {
    var blk = \->_sessionClassVar
    assertEquals( 62, blk() )
  }

  function testIdentifierWriteSessionScopeWorksInblock() {
    try {
      var blk = \-> { _sessionClassVar = 72 }
      blk()
      assertEquals( 72, _sessionClassVar )
    } finally {
      _sessionClassVar = 62
    }
  }

  function testReadSessionScopeWorksIndirectlyInBlock() {
    var blk = \-> this._sessionClassVar
    assertEquals( 62, blk() )
  }

  function testIdentifierWriteSessionScopeWorksIndirectlyInBlock() {
    var blk = \-> {    
      try {
        this._sessionClassVar = 72
        return this._sessionClassVar
      } finally {
        this._sessionClassVar = 62
      }
    }
    assertEquals( 72, blk() )
  }

  function testReadSessionScopeWorksStaticallyInBlock() {
    var blk = \-> VarScopingTest._sessionClassVar
    assertEquals( 62, blk() )
  }
  
  function testIdentifierWriteSessionScopeWorksStaticallyInBlock() {
    var blk = \-> {
      try {
        VarScopingTest._sessionClassVar = 72
        return VarScopingTest._sessionClassVar
      } finally {
        VarScopingTest._sessionClassVar = 62
      }
    }
    assertEquals( 72, blk() )
  }

  function testReadSessionScopeWorksInAnonymousClass() {
    var callable = new Callable() {
      function call() : Object {
        return _sessionClassVar
      }
    }
    assertEquals( 62, callable.call() )
  }

  function testIdentifierWriteSessionScopeWorksInAnonymousClass() {
    try {
      var callable = new Callable() {
        function call() : Object {
          _sessionClassVar = 72
          return null
        }
      }
      callable.call()
      assertEquals( 72, _sessionClassVar )
    } finally {
      _sessionClassVar = 62
    }
  }

  function testReadSessionScopeWorksIndirectlyInAnonymousClass() {
    var callable = new Callable() {
      function call() : Object {
        return outer._sessionClassVar
      }
    }
    assertEquals( 62, callable.call() )
  }

  function testIdentifierWriteSessionScopeWorksIndirectlyInAnonymousClass() {
    var callable = new Callable() {
      function call() : Object {
        try {
          outer._sessionClassVar = 72
          return outer._sessionClassVar
        } finally {
          outer._sessionClassVar = 62
        }
      }
    }
    assertEquals( 72, callable.call() )
  }

  function testReadSessionScopeWorksStaticallyInAnonymousClass() {
    var callable = new Callable() {
      function call() : Object {
        return VarScopingTest._sessionClassVar
      }
    }
    assertEquals( 62, callable.call() )
  }
  
  function testIdentifierWriteSessionScopeWorksStaticallyInAnonymousClass() {
    var callable = new Callable() {
      function call() : Object {
        try {
          VarScopingTest._sessionClassVar = 72
          return VarScopingTest._sessionClassVar
        } finally {
          VarScopingTest._sessionClassVar = 62
        }
      }
    }
    assertEquals( 72, callable.call() )
  }

  function testReadSessionScopeWorksInEval() {
    assertEquals( 62, eval( "_sessionClassVar" ) )
  }

  function testIdentifierWriteSessionScopeWorksInEval() {
    try {
      print( eval( "_sessionClassVar = 72 return null" ) )
      assertEquals( 72, _sessionClassVar )
    } finally {
      _sessionClassVar = 62
    }
  }

  function testReadSessionScopeWorksIndirectlyInEval() {
    assertEquals( 62, eval( "this._sessionClassVar" ) )
  }

  function testIdentifierWriteSessionScopeWorksIndirectlyInEval() {
    try {
      print( eval( "this._sessionClassVar = 72 return null" ) )
      assertEquals( 72, this._sessionClassVar )
    } finally {
      this._sessionClassVar = 62
    }
  }

  function testReadSessionScopeWorksStaticallyInEval() {
    assertEquals( 62, eval( "VarScopingTest._sessionClassVar" ) )
  }
  
  function testIdentifierWriteSessionScopeWorksStaticallyInEval() {
    try {
      print( eval( "VarScopingTest._sessionClassVar = 72 return null" ) )
      assertEquals( 72, VarScopingTest._sessionClassVar )
    } finally {
      VarScopingTest._sessionClassVar = 62
    }
  }

}
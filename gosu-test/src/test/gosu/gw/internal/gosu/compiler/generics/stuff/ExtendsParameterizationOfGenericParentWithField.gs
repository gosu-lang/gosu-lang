package gw.internal.gosu.compiler.generics.stuff

class ExtendsParameterizationOfGenericParentWithField extends GenericParentWithField<String> {

  construct() {
    _tee1 = "test1"
    this._tee2 = "test2"
  }
  
  function getTee1() : String { 
    return _tee1
  }

  function getTee2() : String { 
    return _tee2
  }

  function getTee1Indirectly() : String { 
    return this._tee1
  }

  function setTee1( s : String ) { 
    _tee1 = s
  }

  function setTee1Indirectly( s : String ) { 
    this._tee1 = s
  }
}

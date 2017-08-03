
var pBool: boolean = fn(\ -> "hi") == "hi"
var Bool: Boolean = fn(\ -> "hi") == "hi"

var pBoolParen: boolean = (fn(\ -> "hi") == "hi")
var BoolParen: Boolean = (fn(\ -> "hi") == "hi")

var i: int = fn(\ -> "hi") == "hi"   //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
var iParen: int = (fn(\ -> "hi") == "hi")   //## issuekeys: MSG_IMPLICIT_COERCION_ERROR


function fn<T>( code():T ) : T { return code() }

uses java.util.function.Consumer
uses java.util.function.Function

var x = make( \ a ->a.Alpha, \ b ->b.Code, MyEnum.First )
var xx = make( \ a ->a.Alpha, \ b: MyEnum ->b.Code, First )
x.substring( 0 ) // verify x is String
function make<B, A>( cb(a: A), iter(b:B): A, ref: B ) : A { return null }

var y = makeFi( \ a ->a.Alpha, \ b ->b.Code, MyEnum.First )
print( y.Code ) // verify y is MyEnum
function makeFi<B, A>( cb: Consumer<A>, iter: Function<B, A>, ref: B ) : B { return null }

var z = make( \ a ->a.Alpha, \ b ->b.Code, First )  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND, MSG_NO_PROPERTY_DESCRIPTOR_FOUND, MSG_BAD_IDENTIFIER_NAME

static enum MyEnum { First, Second }

//
// This bit demonstrates type variables don't bleed into other calls and cause
// unnecessary out-of-order reparsing which would create parse errors
//
execute( \ -> {
  foo( "" )
  execute( \ p -> {} )
} )

function execute( runme() ) {}
function execute( runMe(p: String) ) {}

function foo<T>( t: T ): T { return null }


